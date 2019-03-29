package com.zh.program.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zh.program.Common.Constant;
import com.zh.program.Common.RedisKey;
import com.zh.program.Common.exception.WeixinException;
import com.zh.program.Common.utils.CommonMethod;
import com.zh.program.Common.utils.RedisUtil;
import com.zh.program.Common.utils.SignatureUtils;
import com.zh.program.Common.utils.StrUtils;
import com.zh.program.Dto.ReferInfo;
import com.zh.program.Entity.Message;
import com.zh.program.Entity.QRCodeImg;
import com.zh.program.Entity.Sysparams;
import com.zh.program.Entity.WechatUser;
import com.zh.program.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.weixin4j.model.user.User;
import org.weixin4j.util.SHA1;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/web")
public class WebController {
    private String PREFIX = "/web/";
    @Autowired
    private SysparamsService sysparamsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private BrowseRecordService browseRecordService;
    @Autowired
    private ReferInfoService referInfoService;
    @Autowired
    private QRCodeImgService qrCodeImgService;
    @Resource
    private RedisTemplate<String, String> redis;

    /**
     * 查询邀请相关信息 重定向
     * @return
     */
    @GetMapping("/queryReferInfoUrl")
    public String getReferUrl(){
        String requestUrl = Constant.WX_OAUTH_URL
                .replace("APPID", Constant.WX_OPEN_ID)
                .replace("REDIRECT_URI",
                        CommonMethod.urlEncodeUTF8(Constant.WX_REDIRECT_URL_QUERY_INFO))
                .replace("SCOPE", Constant.SNSAPI_USERINFO).replace("STATE","");
        return "redirect:" + requestUrl;
    }
    /**
     * 查询邀请相关信息
     * @return
     */
    @ResponseBody
    @GetMapping("/getReferInfo")
    public String getReferInfo(String code, String callback){
        String result;
        WechatUser wechatUser = wechatUserService.getUser(code);
        if(wechatUser == null){
            result =  "{'ret':''}";
            return callback + "("+result+")";
        }
        String openid = wechatUser.getOpenId();
        List<ReferInfo> referInfos = browseRecordService.queryReferInfo(openid);
        if(referInfos == null || referInfos.size() == 0) {
            result =  "{'ret':''}";
            result = callback + "("+result+")";
        }else{
            JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(referInfos));
            result =  "{'ret':'" + jsonArray.toJSONString() + "','openid':'" + openid + "'}";
            result = callback + "("+result+")";
        }
        return result;
    }

    /**
     * 提交提现审核
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/changeReferInfo")
    public String changeReferInfo(Integer id){
        //1:审核中
        Integer state = 1;
        referInfoService.changeReferInfo(id, state);
        return "success";
    }

    /**
     * 广告页内容初始化
     * @param myOpenid 被邀请人id
     * @param openid  邀请人id
     * @return
     */
    @ResponseBody
    @GetMapping("/getMesInfo")
    public String getMesInfo(Integer id, String myOpenid, String code, String openid, Integer scenePage, String callback){
        Sysparams sysparams = sysparamsService.getSysparams(RedisKey.SYSTEM_URL);
        if(sysparams == null){
            return WeixinException.ERROR_SERVER_URL;
        }
        Map<String, Object> map = new HashMap<>();
        Message message = messageService.selectById(id);
        String sys_url = sysparams.getKeyValue();
        map.put("title", message.getTitle());
        map.put("desc", message.getDesc());
        map.put("link", sys_url + "/index/" + id + "/" + myOpenid);
        Map<String, Object> signMap = getSign(id, code, openid, scenePage);
        map.put("noncestr", signMap.get("noncestr"));
        map.put("signature", signMap.get("signature"));
        map.put("timestamp", signMap.get("timestamp"));
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(map));
        String result =  "{'ret':" + jsonObject + "}";
        result = callback + "("+result+")";
        return result;
    }

    /**
     * 格式化签名返回
     */
    @ResponseBody
    @GetMapping("/getSignInfo")
    public String getSignInfo(Integer id, String code, String openid, Integer scenePage, String callback){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> signMap = getSign(id, code, openid, scenePage);
        map.put("noncestr", signMap.get("noncestr"));
        map.put("signature", signMap.get("signature"));
        map.put("timestamp", signMap.get("timestamp"));
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(map));
        String result =  "{'ret':" + jsonObject + "}";
        result = callback + "("+result+")";
        return result;
    }

    /**
     * 获取签名对象
     */
    public Map<String, Object> getSign(Integer id, String code, String openid, Integer scenePage){

        //1、获取AccessToken
        String accessToken = RedisUtil.searchString(redis, RedisKey.SYSTEM_ACCESS_TOKEN);
        if(StrUtils.isBlank(accessToken)) {
            accessToken = SignatureUtils.getAccessToken();
            RedisUtil.addString(redis, RedisKey.SYSTEM_ACCESS_TOKEN,7100, accessToken);
        }
        //2、获取Ticket
        String jsapi_ticket = SignatureUtils.getTicket(accessToken);

        //3、时间戳和随机字符串
        String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳

        log.info("accessToken:"+accessToken+"\njsapi_ticket:"+jsapi_ticket+"\n时间戳："+timestamp+"\n随机字符串："+noncestr);

        //4、获取url
        String url = sysparamsService.getSysparams(RedisKey.SYSTEM_REFER_URL).getKeyValue();
        if(Constant.WEB_PAGE.equals(scenePage)) {
            url = url + "?code=" + code + "&state=id%3D" + id + "%26openid%3D" + openid;
        }else if(Constant.QUERY_PAGE.equals(scenePage)){
            url = url + "?code=" + code + "&state=";
        }

        //5、将参数排序并拼接字符串
        String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;

        //6、将字符串进行sha1加密
        String signature = SHA1.encode(str);
        log.info("参数："+str+"\n签名："+signature);

        Map<String, Object> map = new HashMap<>();
        map.put("noncestr", noncestr);
        map.put("signature", signature);
        map.put("timestamp", timestamp);
        return map;
    }

    @ResponseBody
    @GetMapping("saveImg")
    public void saveImg(String localData, String openid){
        QRCodeImg qrCodeImg = qrCodeImgService.queryByOpenid(openid);
        if(qrCodeImg != null){
            qrCodeImg.setImg(localData);
            qrCodeImgService.update(qrCodeImg);
        }else {
            qrCodeImg = new QRCodeImg();
            qrCodeImg.setOpenId(openid);
            qrCodeImg.setImg(localData);
            qrCodeImg.setType(Constant.WECHATPAY);
            qrCodeImgService.save(qrCodeImg);
        }
    }
}

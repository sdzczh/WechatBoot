package com.zh.program.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zh.program.Common.RedisKey;
import com.zh.program.Common.exception.WeixinException;
import com.zh.program.Common.utils.RedisUtil;
import com.zh.program.Common.utils.SignatureUtils;
import com.zh.program.Common.utils.StrUtils;
import com.zh.program.Entity.Sysparams;
import com.zh.program.Service.BrowseRecordService;
import com.zh.program.Service.MessageService;
import com.zh.program.Service.SysparamsService;
import com.zh.program.Service.WechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.weixin4j.util.SHA1;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/web")
public class WebController {
    private String PREFIX = "/web/";
    @Autowired
    private SysparamsService sysparamsService;
    @Resource
    private RedisTemplate<String, String> redis;

    /**
     * 获取邀请地址
     * @param id 广告id
     * @param openid 用户id
     * @param callback
     * @return
     */
    @ResponseBody
    @GetMapping("/getReferUrl")
    public String getReferUrl(Integer id, String openid, String callback){
        Sysparams sysparams = sysparamsService.getSysparams(RedisKey.SYSTEM_URL);
        if(sysparams == null){
            return WeixinException.ERROR_SERVER_URL;
        }
        String sys_url = sysparams.getKeyValue();
        StringBuffer referUrl = new StringBuffer();
        referUrl.append(sys_url).append("/index/").append(id).append("/").append(openid);
        String result =  "{'ret':'" + referUrl.toString() + "'}";
        result = callback + "("+result+")";
        return result;
    }

    @ResponseBody
    @GetMapping("getSign")
    public String getSign(String callback){

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
        String url="http://tlhe.cn/index2.html";

        //5、将参数排序并拼接字符串
        String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;

        //6、将字符串进行sha1加密
        String signature = SHA1.encode(str);
        log.info("参数："+str+"\n签名："+signature);
        Map<String, Object> map = new HashMap<>();
        map.put("noncestr", noncestr);
        map.put("signature", signature);
        map.put("timestamp", timestamp);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
        String result =  "{'ret':'" + jsonObject.toJSONString() + "'}";
        result = callback + "("+result+")";
        return result;
    }

}

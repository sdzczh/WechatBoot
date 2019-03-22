package com.zh.program.Controller;

import com.alibaba.fastjson.JSONObject;
import com.zh.program.Common.Constant;
import com.zh.program.Common.utils.CommonMethod;
import com.zh.program.Common.utils.StrUtils;
import com.zh.program.Entity.Message;
import com.zh.program.Service.MessageService;
import com.zh.program.Service.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WechatUserController {
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private MessageService messageService;

    private String PREFIX = "/web/";

    /**
     * 请求获取用户授权
     * @param id
     * @param openid
     * @return
     */
    @RequestMapping("/index/{id}/{openid}")
    public String getAuth(@PathVariable Integer id, @PathVariable String openid) {
        String requestUrl = Constant.WX_OAUTH_URL
                .replace("APPID", Constant.WX_OPEN_ID)
                .replace("REDIRECT_URI",
                        CommonMethod.urlEncodeUTF8(Constant.WX_REDIRECT_URL))
                .replace("SCOPE", Constant.SNSAPI_USERINFO).replace("STATE", "id=" + id.toString() + "%26openid=" + openid);
        return "redirect:" + requestUrl;
    }

    /**
     * 邀请业务处理
     * @param code
     */
    @ResponseBody
    @RequestMapping("/saveUser")
    public void saveUser(String code, String openid, Integer id) {
        wechatUserService.saveUser(code, openid, id);
    }

    /**
     * 获取渲染页面数据 业务处理
     * @param id
     * @param callback
     * @return
     */
    @ResponseBody
    @RequestMapping("/getData")
    public String getData(Integer id, String callback){
        Message message = messageService.selectById(id);
        if(message == null || StrUtils.isBlank(message.getInfo())){
            return PREFIX + "ID填写错误";
        }
        String Info = message.getInfo();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", Info);
        jsonObject.put("title", message.getTitle());
        String result =  "{'ret':" + jsonObject + "}";
        result = callback + "("+result+")";
        return result;
    }
}

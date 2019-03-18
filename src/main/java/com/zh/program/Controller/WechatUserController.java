package com.zh.program.Controller;

import com.zh.program.Common.Constant;
import com.zh.program.Common.utils.CommonMethod;
import com.zh.program.Service.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WechatUserController {
    @Autowired
    private WechatUserService wechatUserService;

    @RequestMapping("/index")
    public String getAuth() {
        // 请求获取用户授权
        String requestUrl = Constant.WX_OAUTH_URL
                .replace("APPID", Constant.WX_OPEN_ID)
                .replace("REDIRECT_URI",
                        CommonMethod.urlEncodeUTF8(Constant.WX_REDIRECT_URL))
                .replace("SCOPE", Constant.SNSAPI_USERINFO).replace("STATE", "123");
        return "redirect:" + requestUrl;
    }

    @ResponseBody
    @RequestMapping("/saveUser")
    public void saveUser(String code) {
        wechatUserService.saveUser(code);
    }
}

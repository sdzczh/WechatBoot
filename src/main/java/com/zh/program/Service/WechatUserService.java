package com.zh.program.Service;

import com.zh.program.Entity.WechatUser;

import javax.servlet.http.HttpSession;

public interface WechatUserService {
    void saveUser(String code, HttpSession session);

    WechatUser queryByOpenId(String openid);
}

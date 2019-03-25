package com.zh.program.Service;

import com.zh.program.Entity.WechatUser;

public interface WechatUserService {
    String saveUser(String code, String openid, Integer id);

    WechatUser getUser(String code);

    WechatUser queryByOpenId(String openid);
}

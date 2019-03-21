package com.zh.program.Service;

import com.zh.program.Entity.WechatUser;

public interface WechatUserService {
    void saveUser(String code, String openid, Integer id);

    WechatUser queryByOpenId(String openid);
}

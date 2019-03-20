package com.zh.program.Service.impl;

import com.zh.program.Common.Constant;
import com.zh.program.Common.exception.WeixinException;
import com.zh.program.Common.utils.CommonMethod;
import com.zh.program.Dao.WechatUserDao;
import com.zh.program.Entity.Token;
import com.zh.program.Entity.WechatUser;
import com.zh.program.Service.WechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class WechatWechatUserServiceImpl implements WechatUserService {
    @Autowired
    private WechatUserDao wechatUserDao;
    @Override
    public void saveUser(String code) {
        Token accessToken;
        WechatUser wechatUser;
        try {
            // 获取TOKEN
            accessToken = CommonMethod.getToken(Constant.WX_OPEN_ID, Constant.WX_APP_SECRET, code);
            //获取User对象
            wechatUser = CommonMethod.getUser(accessToken.getAccessToken(), Constant.WX_OPEN_ID);
        } catch (Exception e) {
            log.info(WeixinException.ACCESS_TOKEN_ERROR);
            return;
        }
        WechatUser wechatUser1 = wechatUserDao.findByOpenId(wechatUser.getOpenId());
        if(wechatUser1 == null){
            wechatUserDao.save(wechatUser);
        }
    }
}

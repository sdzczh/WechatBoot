package com.zh.program.Service.impl;

import com.zh.program.Common.Constant;
import com.zh.program.Common.exception.WeixinException;
import com.zh.program.Common.utils.CommonMethod;
import com.zh.program.Dao.BrowseRecordDao;
import com.zh.program.Dao.MessageDao;
import com.zh.program.Dao.WechatUserDao;
import com.zh.program.Entity.BrowseRecord;
import com.zh.program.Entity.Message;
import com.zh.program.Dto.Token;
import com.zh.program.Entity.WechatUser;
import com.zh.program.Service.WechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@Transactional
public class WechatWechatUserServiceImpl implements WechatUserService {
    @Autowired
    private WechatUserDao wechatUserDao;
    @Autowired
    private BrowseRecordDao browseRecordDao;
    @Autowired
    private MessageDao messageDao;
    @Override
    public String saveUser(String code, String openid, Integer id) {
        WechatUser wechatUser = getUser(code);
        if(wechatUser == null){
            return null;
        }
        String userOpenId = wechatUser.getOpenId();
        WechatUser wechatUser1 = wechatUserDao.findByOpenId(userOpenId);
        if(wechatUser1 == null){
            wechatUserDao.save(wechatUser);
        }
        BrowseRecord browseRecord = browseRecordDao.queryOne(id, userOpenId, openid);
        if(browseRecord == null){
            browseRecord = new BrowseRecord();
            browseRecord.setMsg_id(id);
            browseRecord.setUser_open_id(wechatUser.getOpenId());
            browseRecord.setInvite_open_id(openid);
            browseRecord.setState(Constant.ORDER_NOT_COMPLETE);
            browseRecordDao.save(browseRecord);
            browseRecord.setNumber(0);
        }
        Message message = messageDao.getOne(id);
        //当前已浏览次数
        Integer number = browseRecord.getNumber();
        //完成任务需浏览次数
        Integer shouldNumber = message.getNumber();
        if(number < shouldNumber){
            //当前已浏览次数加一
            browseRecord.setNumber(number + 1);
            browseRecordDao.save(browseRecord);
            //MESSAGE表中剩余推广数量减一
            message.setRemain(message.getRemain().subtract(BigDecimal.ONE));
            messageDao.save(message);
        }
        return userOpenId;

    }

    @Override
    public WechatUser getUser(String code) {
        Token accessToken;
        try {
            // 获取TOKEN
            accessToken = CommonMethod.getToken(Constant.WX_OPEN_ID, Constant.WX_APP_SECRET, code);
            //获取User对象
            return CommonMethod.getUser(accessToken.getAccessToken(), Constant.WX_OPEN_ID);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(WeixinException.ACCESS_TOKEN_ERROR);
            return null;
        }
    }

    @Override
    public WechatUser queryByOpenId(String openid) {
        return wechatUserDao.findByOpenId(openid);
    }
}

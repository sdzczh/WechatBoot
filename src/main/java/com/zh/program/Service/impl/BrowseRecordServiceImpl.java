package com.zh.program.Service.impl;

import com.zh.program.Dao.BrowseRecordDao;
import com.zh.program.Dao.WechatUserDao;
import com.zh.program.Entity.BrowseRecord;
import com.zh.program.Entity.Message;
import com.zh.program.Entity.WechatUser;
import com.zh.program.Service.BrowseRecordService;
import com.zh.program.Service.WechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;


@Service
@Slf4j
@Transactional
public class BrowseRecordServiceImpl implements BrowseRecordService {
    @Autowired
    private BrowseRecordDao browseRecordDao;
    @Autowired
    private WechatUserService wechatUserService;
    @Override
    public BrowseRecord queryOne(Integer msgId, String referOpenId, String openId) {
        return browseRecordDao.queryOne(msgId, openId, referOpenId);
    }

    @Override
    public void check(Message message, HttpSession session, String openid) {
        WechatUser wechatUser = (WechatUser) session.getAttribute("user");
        WechatUser referUser = wechatUserService.queryByOpenId(openid);
        BrowseRecord browseRecord = this.queryOne(message.getId(), referUser.getOpenId(), wechatUser.getOpenId());
        if(browseRecord == null){
            browseRecord = new BrowseRecord();
            browseRecord.setMsg_id(message.getId());
            browseRecord.setUser_open_id(openid);
            browseRecord.setInvite_open_id(wechatUser.getOpenId());
            browseRecord.setNumber(1);
            browseRecordDao.save(browseRecord);
        }else{
            browseRecord.setNumber(browseRecord.getNumber() + 1);
        }
    }
}

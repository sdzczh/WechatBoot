package com.zh.program.Service;

import com.zh.program.Entity.BrowseRecord;
import com.zh.program.Entity.Message;

import javax.servlet.http.HttpSession;

public interface BrowseRecordService {
    /**
     * 获取浏览记录
     * @param msgId  广告id
     * @param referOpenId  邀请人openid
     * @param openId 点击用户openid
     * @return
     */
    BrowseRecord queryOne(Integer msgId, String referOpenId, String openId);

    void check(Message message, HttpSession session, String openid);
}

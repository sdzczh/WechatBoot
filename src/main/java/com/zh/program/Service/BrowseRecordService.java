package com.zh.program.Service;

import com.zh.program.Dto.ReferInfo;
import com.zh.program.Entity.BrowseRecord;
import com.zh.program.Entity.Message;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface BrowseRecordService {
    /**
     * 获取浏览记录
     * @param msgId  广告id
     * @param referOpenId  邀请人openid
     * @param openId 点击用户openid
     * @return
     */
    BrowseRecord queryOne(Integer msgId, String referOpenId, String openId);

    /**
     * 获取个人分享状态信息
     * @param openid
     * @return
     */
    List<ReferInfo> queryReferInfo(String openid);
}

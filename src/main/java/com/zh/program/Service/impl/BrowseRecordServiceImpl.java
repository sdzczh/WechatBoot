package com.zh.program.Service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zh.program.Dao.BrowseRecordDao;
import com.zh.program.Dao.ReferInfoDao;
import com.zh.program.Dao.WechatUserDao;
import com.zh.program.Dto.ReferInfo;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@Transactional
public class BrowseRecordServiceImpl implements BrowseRecordService {
    @Autowired
    private BrowseRecordDao browseRecordDao;
    @Autowired
    private ReferInfoDao referInfoDao;
    @Override
    public BrowseRecord queryOne(Integer msgId, String referOpenId, String openId) {
        return browseRecordDao.queryOne(msgId, openId, referOpenId);
    }

    @Override
    public List<ReferInfo> queryReferInfo(String openid) {
        List<Object> list = referInfoDao.queryReferInfo(openid);
        String stateStr;
        if(list.size() > 0) {
            List<ReferInfo> referInfos = new LinkedList<>();
            for(Object object : list) {
                String result = JSON.toJSONString(object);
                JSONArray jsonArray = JSONArray.parseArray(result);
                ReferInfo referInfo = new ReferInfo();
                referInfo.setId(jsonArray.getInteger(0));
                referInfo.setTitle(jsonArray.getString(1));
                referInfo.setPrice(new BigDecimal(jsonArray.getString(2)));
                referInfo.setTotal(new BigDecimal(jsonArray.getString(3)));
                Integer state = jsonArray.getInteger(4);
                if(state == 0) {
                    stateStr = "未提现";
                }else if(state == 1) {
                        stateStr = "审核中";
                }else {
                    stateStr = "已提现";
                }
                referInfo.setState(state);
                referInfo.setStateStr(stateStr);
                referInfos.add(referInfo);
            }
            return referInfos;
        }else{
            return null;
        }
    }
}

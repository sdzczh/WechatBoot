package com.zh.program.Service.impl;

import com.zh.program.Common.utils.RedisUtil;
import com.zh.program.Common.utils.StrUtils;
import com.zh.program.Dao.SysparamsDao;
import com.zh.program.Entity.Sysparams;
import com.zh.program.Service.SysparamsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
@Transactional
public class SysparamsServiceImpl implements SysparamsService {
    @Resource
    private RedisTemplate<String, String> redis;

    @Autowired
    private SysparamsDao sysparamsDao;
    @Override
    public Sysparams getSysparams(String keyName) {
        StringBuffer redisKey = new StringBuffer();
        redisKey.append("wechat:sysparams:").append(keyName);
        Sysparams sysparams = RedisUtil.searchStringObj(redis, keyName, Sysparams.class);
        if(sysparams == null){
            sysparams = sysparamsDao.findByKeyName(keyName);
            RedisUtil.addStringObj(redis, keyName, sysparams);
        }
        return sysparams;
    }
}

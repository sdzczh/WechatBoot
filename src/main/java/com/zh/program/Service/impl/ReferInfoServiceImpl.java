package com.zh.program.Service.impl;

import com.zh.program.Dao.ReferInfoDao;
import com.zh.program.Service.ReferInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ReferInfoServiceImpl implements ReferInfoService {
    @Autowired
    private ReferInfoDao referInfoDao;

    @Override
    public void changeReferInfo(Integer id, Integer state) {
        referInfoDao.changeReferInfo(id, state);
    }
}

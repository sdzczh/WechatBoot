package com.zh.program.Service.impl;

import com.zh.program.Dao.QRCodeImgDao;
import com.zh.program.Dao.ReferInfoDao;
import com.zh.program.Entity.QRCodeImg;
import com.zh.program.Service.QRCodeImgService;
import com.zh.program.Service.ReferInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class QRCodeImgServiceImpl implements QRCodeImgService {
    @Autowired
    private QRCodeImgDao qrCodeImgDao;

    @Override
    public void save(QRCodeImg qrCodeImg) {
        qrCodeImgDao.save(qrCodeImg);
    }

    @Override
    public QRCodeImg queryByOpenid(String openid) {
        return qrCodeImgDao.queryByOpenid(openid);
    }

    @Override
    public void update(QRCodeImg qrCodeImg) {
        qrCodeImgDao.update(qrCodeImg.getImg(), qrCodeImg.getOpenId());
    }
}

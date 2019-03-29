package com.zh.program.Service;


import com.zh.program.Entity.QRCodeImg;

public interface QRCodeImgService {
    void save(QRCodeImg qrCodeImg);

    QRCodeImg queryByOpenid(String openid);

    void update(QRCodeImg qrCodeImg);
}

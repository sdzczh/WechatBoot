package com.zh.program.Dao;

import com.zh.program.Entity.QRCodeImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Service
@Transactional
public interface QRCodeImgDao extends JpaRepository<QRCodeImg, Integer> {

    @Query("select u from QRCodeImg u where u.openId=:openid")
    QRCodeImg queryByOpenid(@Param("openid") String openid);

    @Modifying
    @Query("update QRCodeImg br set br.img=:img where br.openId=:openid")
    void update(@Param("img") String img, @Param("openid") String openid);
}

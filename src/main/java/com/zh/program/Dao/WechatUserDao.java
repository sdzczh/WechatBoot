package com.zh.program.Dao;

import com.zh.program.Entrty.WechatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Service
@Transactional
public interface WechatUserDao extends JpaRepository<WechatUser, Integer> {
    @Query("select u from WechatUser u where u.openId=:openId")
    WechatUser findByOpenId(@Param("openId") String openId);
}

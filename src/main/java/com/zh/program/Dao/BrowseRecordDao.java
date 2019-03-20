package com.zh.program.Dao;

import com.zh.program.Entity.BrowseRecord;
import com.zh.program.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Service
@Transactional
public interface BrowseRecordDao extends JpaRepository<BrowseRecord, Integer> {
    @Query("select u from BrowseRecord u where u.msg_id=:msgId and u.user_open_id=:openId and u.invite_open_id=:referOpenId")
    BrowseRecord queryOne(@Param("msgId") Integer msgId, @Param("openId") String openId, @Param("referOpenId") String referOpenId);
}

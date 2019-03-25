package com.zh.program.Dao;

import com.zh.program.Entity.Message;
import com.zh.program.Entity.Sysparams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Service
@Transactional
public interface SysparamsDao extends JpaRepository<Sysparams, Integer> {

    @Query("select u from Sysparams u where u.keyName=:keyName")
    Sysparams findByKeyName(@Param("keyName") String keyName);
}

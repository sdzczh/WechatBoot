package com.zh.program.Dao;

import com.zh.program.Dto.ReferInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Service
@Transactional
public interface ReferInfoDao extends JpaRepository<ReferInfo, Integer> {
    @Query(nativeQuery = true,value = "SELECT br.id, m.title, m.money as price ,(m.money*m.number) as total , br.state FROM browse_record br LEFT JOIN message m ON m.id=br.msg_id WHERE br.number >= m.number AND br.user_open_id=:openid")
    List<Object> queryReferInfo(@Param("openid") String openid);

    @Modifying
    @Query("update BrowseRecord br set br.state=:state where br.id=:id")
    void changeReferInfo(@Param("id") Integer id, @Param("state") Integer state);
}

package com.zh.program.Dao;

import com.zh.program.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Service
@Transactional
public interface MessageDao  extends JpaRepository<Message, Integer> {
}

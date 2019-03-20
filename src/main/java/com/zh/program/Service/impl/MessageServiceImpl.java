package com.zh.program.Service.impl;

import com.zh.program.Dao.MessageDao;
import com.zh.program.Entity.Message;
import com.zh.program.Service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;
    @Override
    public Message selectById(Integer id) {
        return messageDao.getOne(id);
    }
}

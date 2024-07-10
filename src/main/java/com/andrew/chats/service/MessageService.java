package com.andrew.chats.service;

import com.andrew.chats.dao.mapper.MessageMapper;
import com.andrew.chats.dao.model.Message;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@Service
public class MessageService extends ServiceImpl<MessageMapper, Message> {

    public Long save(Integer type, String content) {
        Message message = new Message();
        message.setContent(content);
        message.setType(type);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        save(message);
        return message.getId();
    }

}

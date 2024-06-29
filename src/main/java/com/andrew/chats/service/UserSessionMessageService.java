package com.andrew.chats.service;

import com.andrew.chats.dao.model.UserSessionMessage;
import com.andrew.chats.dao.mapper.UserSessionMessageMapper;
import com.andrew.chats.enums.MessageStatusEnum;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户联系人消息表 服务实现类
 * </p>
 *
 * @author andrew
 * @since 2024-06-28
 */
@Service
public class UserSessionMessageService extends ServiceImpl<UserSessionMessageMapper, UserSessionMessage> {

    public boolean save(String senderId, String receiveId, Long messageId) {
        UserSessionMessage sessionMessage = new UserSessionMessage();
        sessionMessage.setSenderId(senderId);
        sessionMessage.setReceiveId(receiveId);
        sessionMessage.setMessageId(messageId);
        sessionMessage.setStatus(MessageStatusEnum.UN_READ.getCode());
        sessionMessage.setCreateTime(LocalDateTime.now());
        sessionMessage.setUpdateTime(LocalDateTime.now());
        return save(sessionMessage);
    }

}


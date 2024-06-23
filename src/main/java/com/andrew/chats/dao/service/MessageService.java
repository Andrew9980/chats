package com.andrew.chats.dao.service;

import com.andrew.chats.dao.model.Message;
import com.andrew.chats.dao.mapper.MessageMapper;
import com.andrew.chats.enums.MessageStatusEnum;
import com.andrew.chats.vo.UserSendMsgReqVO;
import com.baomidou.mybatisplus.extension.service.IService;
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
public class MessageService extends ServiceImpl<MessageMapper, Message> implements IService<Message> {

    public boolean save(UserSendMsgReqVO userSendMsgReqVO) {
        Message message = new Message();
        message.setSenderId(userSendMsgReqVO.getSenderId());
        message.setReceiveId(userSendMsgReqVO.getReceiveId());
        message.setContent(userSendMsgReqVO.getContent());
        message.setType(userSendMsgReqVO.getType());
        message.setStatus(MessageStatusEnum.UN_READ.getCode());
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        return save(message);
    }

}

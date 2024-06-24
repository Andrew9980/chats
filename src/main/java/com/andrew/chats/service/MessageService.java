package com.andrew.chats.service;

import com.andrew.chats.dao.mapper.MessageMapper;
import com.andrew.chats.dao.model.Message;
import com.andrew.chats.enums.MessageStatusEnum;
import com.andrew.chats.utils.util.ObjUtils;
import com.andrew.chats.vo.MessageReqVO;
import com.andrew.chats.vo.MessageResVO;
import com.andrew.chats.vo.UserSendMsgReqVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.bloomfilter.BitMapProducer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 批量保存群申请消息
     * @param userIds
     * @param userSendMsgReqVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveGroupApply(List<String> userIds, UserSendMsgReqVO userSendMsgReqVO) {
        List<Message> list = new ArrayList<>(userIds.size());
        for (String userId : userIds) {
            Message message = new Message();
            message.setSenderId(userSendMsgReqVO.getSenderId());
            message.setReceiveId(userId);
            message.setContent(userSendMsgReqVO.getContent());
            message.setType(userSendMsgReqVO.getType());
            message.setStatus(MessageStatusEnum.UN_READ.getCode());
            message.setCreateTime(LocalDateTime.now());
            message.setUpdateTime(LocalDateTime.now());
            list.add(message);
        }
        return saveBatch(list);
    }

    public List<MessageResVO> listMessage(MessageReqVO messageReqVO) {
        List<Message> list = list(Wrappers.<Message>lambdaQuery()
                .eq(StringUtils.isNotEmpty(messageReqVO.getSenderId()), Message::getSenderId, messageReqVO.getSenderId())
                .eq(StringUtils.isNotEmpty(messageReqVO.getReceiveId()), Message::getReceiveId, messageReqVO.getReceiveId())
                .eq(messageReqVO.getStatus() != null, Message::getStatus, messageReqVO.getStatus())
                .eq(messageReqVO.getType() != null, Message::getType, messageReqVO.getType())
                .orderByDesc(Message::getCreateTime));
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(e -> ObjUtils.copy(e, MessageResVO.class)).collect(Collectors.toList());
    }

}

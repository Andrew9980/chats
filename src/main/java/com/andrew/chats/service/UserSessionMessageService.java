package com.andrew.chats.service;

import com.andrew.chats.common.utils.ObjUtils;
import com.andrew.chats.common.vo.UserSessionMessageVO;
import com.andrew.chats.dao.model.Message;
import com.andrew.chats.dao.model.UserSessionMessage;
import com.andrew.chats.dao.mapper.UserSessionMessageMapper;
import com.andrew.chats.enums.MessageStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private MessageService messageService;

    public boolean save(String senderId, String receiveId, Long messageId) {
        UserSessionMessage sessionMessage = commonField(senderId, messageId, receiveId);
        return save(sessionMessage);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchSave(String senderId, List<String> receiveIds, Long messageId) {
        List<UserSessionMessage> list = new ArrayList<>();
        for (String receiveId : receiveIds) {
            list.add(commonField(senderId, messageId, receiveId));
        }
        return saveBatch(list);
    }

    private UserSessionMessage commonField(String senderId, Long messageId, String receiveId) {
        UserSessionMessage sessionMessage = new UserSessionMessage();
        sessionMessage.setSenderId(senderId);
        sessionMessage.setReceiveId(receiveId);
        sessionMessage.setMessageId(messageId);
        sessionMessage.setStatus(MessageStatusEnum.UN_READ.getCode());
        sessionMessage.setCreateTime(LocalDateTime.now());
        sessionMessage.setUpdateTime(LocalDateTime.now());
        return sessionMessage;
    }

    /**
     * 聊天记录 每次请求50条
     * @param userSessionMessageVO
     * @return
     */
    public List<UserSessionMessageVO> getSessionMessage(UserSessionMessageVO userSessionMessageVO) {
        LambdaQueryWrapper<UserSessionMessage> queryWrapper = Wrappers.<UserSessionMessage>lambdaQuery()
                .eq(UserSessionMessage::getSenderId, userSessionMessageVO.getSenderId())
                .eq(UserSessionMessage::getReceiveId, userSessionMessageVO.getReceiveId())
                .lt(UserSessionMessage::getId, userSessionMessageVO.getId())
                .last("limit 50");
        List<UserSessionMessage> sessionMessageList = list(queryWrapper);
        if (CollectionUtils.isEmpty(sessionMessageList)) {
            return Collections.emptyList();
        }
        List<Long> messageIdList = sessionMessageList.stream().map(UserSessionMessage::getMessageId).collect(Collectors.toList());
        List<Message> messageList = messageService.listByIds(messageIdList);
        Map<Long, String> msgMap = messageList.stream().collect(Collectors.toMap(Message::getId, Message::getContent));
        List<UserSessionMessageVO> list = new ArrayList<>();
        for (UserSessionMessage sessionMessage : sessionMessageList) {
            UserSessionMessageVO vo = new UserSessionMessageVO();
            vo.setId(sessionMessage.getId());
            vo.setSenderId(sessionMessage.getSenderId());
            vo.setReceiveId(sessionMessage.getReceiveId());
            vo.setCreateTime(sessionMessage.getCreateTime());
            vo.setContent(msgMap.get(sessionMessage.getMessageId()));
            list.add(vo);
        }
        return list;
    }
}


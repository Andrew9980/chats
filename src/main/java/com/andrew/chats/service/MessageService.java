package com.andrew.chats.service;

import com.andrew.chats.dao.mapper.MessageMapper;
import com.andrew.chats.dao.model.Message;
import com.andrew.chats.common.params.UserContactParam;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 批量保存群申请消息
     * @param userIds
     * @param UserContactReqVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveGroupApply(List<String> userIds, UserContactParam userContactParam) {
        List<Message> list = new ArrayList<>(userIds.size());
        for (String userId : userIds) {
            Message message = new Message();
            message.setContent(userContactParam.getOpinion());
            message.setType(userContactParam.getType());
            message.setCreateTime(LocalDateTime.now());
            message.setUpdateTime(LocalDateTime.now());
            list.add(message);
        }
        return saveBatch(list);
    }

}

package com.andrew.chats.service;

import com.andrew.chats.dao.model.ChatGroup;
import com.andrew.chats.dao.mapper.ChatGroupMapper;
import com.andrew.chats.enums.GroupStatusEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 群 服务实现类
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@Service
public class ChatGroupService extends ServiceImpl<ChatGroupMapper, ChatGroup> {

    public List<ChatGroup> listByName(String name) {
        return list(Wrappers.<ChatGroup>lambdaQuery()
                .like(ChatGroup::getGroupName, name)
                .eq(ChatGroup::getStatus, GroupStatusEnum.VALID.getCode()));
    }

    public ChatGroup getByGroupId(String groupId) {
        return getOne(Wrappers.<ChatGroup>lambdaQuery()
                .eq(ChatGroup::getGroupId, groupId)
                .eq(ChatGroup::getStatus, GroupStatusEnum.VALID.getCode()));
    }

}

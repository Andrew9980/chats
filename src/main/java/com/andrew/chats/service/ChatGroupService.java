package com.andrew.chats.service;

import com.andrew.chats.config.ServiceException;
import com.andrew.chats.dao.model.ChatGroup;
import com.andrew.chats.dao.mapper.ChatGroupMapper;
import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.enums.GroupStatusEnum;
import com.andrew.chats.enums.GroupTypeEnum;
import com.andrew.chats.utils.Constants;
import com.andrew.chats.utils.util.ObjUtils;
import com.andrew.chats.utils.util.ServiceUtil;
import com.andrew.chats.vo.GroupApplyReqVO;
import com.andrew.chats.vo.GroupReqVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private GroupMemberService groupMemberService;

    public boolean create(GroupReqVO groupReqVO) {
        ChatGroup chatGroup = ObjUtils.copy(groupReqVO, ChatGroup.class);
        chatGroup.setGroupId(ServiceUtil.getRandomId());
        chatGroup.setCreateTime(LocalDateTime.now());
        chatGroup.setUpdateTime(LocalDateTime.now());
        chatGroup.setStatus(GroupStatusEnum.VALID.getCode());
        chatGroup.setType(GroupTypeEnum.NORMAL.getCode());
        chatGroup.setMaxCount(Constants.GROUP_MAX_COUNT);
        return save(chatGroup);
    }

    public boolean update(GroupReqVO groupReqVO) {
        ChatGroup chatGroup = ObjUtils.copy(groupReqVO, ChatGroup.class);
        chatGroup.setUpdateTime(LocalDateTime.now());
        return updateById(chatGroup);
    }

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

    /**
     * 加群 后期改成分布式锁
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized boolean joinGroup(GroupApplyReqVO reqVO) {
        ChatGroup chatGroup = getByGroupId(reqVO.getGroupId());
        if (chatGroup == null || Objects.equals(chatGroup.getStatus(), GroupStatusEnum.BANED.getCode())) {
            throw new ServiceException(ExceptionEnum.GROUP_NOT_EXIST);
        }
        if (chatGroup.getCurCount() + 1 >= chatGroup.getMaxCount()) {
            throw new ServiceException(ExceptionEnum.GROUP_FULL);
        }
        chatGroup.setCurCount(chatGroup.getCurCount() + 1);
        chatGroup.setUpdateTime(LocalDateTime.now());
        groupMemberService.create(reqVO.getGroupId(), reqVO.getUserId());
        return updateById(chatGroup);
    }

}

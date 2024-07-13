package com.andrew.chats.service;

import com.andrew.chats.common.params.GroupMemberParam;
import com.andrew.chats.common.params.UserContactParam;
import com.andrew.chats.common.params.UserSendMsgParam;
import com.andrew.chats.common.utils.ObjUtils;
import com.andrew.chats.common.vo.GroupMemberVO;
import com.andrew.chats.common.vo.UserContactVO;
import com.andrew.chats.common.vo.UserInfoVO;
import com.andrew.chats.config.ServiceException;
import com.andrew.chats.dao.mapper.UserContactMapper;
import com.andrew.chats.dao.model.ChatGroup;
import com.andrew.chats.dao.model.UserContact;
import com.andrew.chats.dao.model.UserSessionMessage;
import com.andrew.chats.enums.*;
import com.andrew.chats.netty.WebSocketContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户联系 服务实现类
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@Slf4j
@Service
public class UserContactService extends ServiceImpl<UserContactMapper, UserContact> {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserSessionMessageService userSessionMessageService;

    @Autowired
    private ChatGroupService chatGroupService;

    @Autowired
    private GroupMemberService groupMemberService;

    /**
     * 根据状态查询用户联系人列表
     *
     * @param userId
     * @param status
     * @return
     */
    public List<UserContact> getUserContactList(String userId, Integer status) {
        return list(
                Wrappers.<UserContact>lambdaQuery().eq(UserContact::getUserId, userId)
                        .eq(UserContact::getStatus, status)
        );
    }

    /**
     * 查询用户联系人信息
     *
     * @param userId
     * @param contactId
     * @return
     */
    public UserContact getUserContact(String userId, String contactId) {
        return getOne(
                Wrappers.<UserContact>lambdaQuery().eq(UserContact::getUserId, userId)
                        .eq(UserContact::getContactId, contactId)
        );
    }

    /**
     * 好友申请
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean apply(UserContactParam userContactParam) {
        UserInfoVO userInfo;
        ChatGroup chatGroup;
        if (Objects.equals(userContactParam.getType(), UserContactEnum.FRIEND.getCode())) { // 加人
            userInfo = userInfoService.getByUserId(userContactParam.getContactId());
            if (Objects.isNull(userInfo) || Objects.equals(userInfo.getStatus(), UserStatusEnum.BANED.getCode())) {
                throw new ServiceException(ExceptionEnum.USER_NOT_EXIST);
            }
        } else { // 加群
            chatGroup = chatGroupService.getByGroupId(userContactParam.getContactId());
            if (Objects.isNull(chatGroup) || Objects.equals(chatGroup.getStatus(), GroupStatusEnum.BANED.getCode())) {
                throw new ServiceException(ExceptionEnum.GROUP_NOT_EXIST);
            }
        }
        // 校验重复申请
        UserContact userContact = getOne(Wrappers.<UserContact>lambdaQuery()
                .eq(UserContact::getUserId, userContactParam.getUserId())
                .eq(UserContact::getContactId, userContactParam.getContactId()));
        if (Objects.nonNull(userContact)) {
            throw new ServiceException(ExceptionEnum.USER_CONTACT_EXIST);
        }
        // 记录申请中的联系人
        userContact = new UserContact();
        userContact.setUserId(userContactParam.getUserId());
        userContact.setContactId(userContactParam.getContactId());
        userContact.setType(userContactParam.getType());
        userContact.setStatus(UserContactStatusEnum.APPLY.getCode());
        userContact.setLastContractTime(LocalDateTime.now());
        userContact.setCreateTime(LocalDateTime.now());
        userContact.setUpdateTime(LocalDateTime.now());
        save(userContact);

        if (Objects.equals(userContactParam.getType(), UserContactEnum.FRIEND.getCode())) {
            // 消息保存
            Long messageId = messageService.save(MessageTypeEnum.FRIEND_APPLY.getCode(), userContactParam.getOpinion());
            if (messageId == null) {
                return false;
            }
            // 创建消息记录
            boolean result = userSessionMessageService.save(userContactParam.getUserId(), userContactParam.getContactId(), messageId);
            if (!result) {
                return false;
            }
            // 发送申请
            UserSendMsgParam userSendMsgParam = new UserSendMsgParam();
            userSendMsgParam.setType(WSSendMessageTypeEnum.FRIEND_APPLY.getCode());
            userSendMsgParam.setSenderId(userContactParam.getUserId());
            userSendMsgParam.setReceiveId(userContactParam.getContactId());
            userSendMsgParam.setContent(userContactParam.getOpinion());
            WebSocketContext.sendMsg(userSendMsgParam);
        } else {
            // 发送群申请消息，将申请消息发送到群主和群管理员
            Long messageId = messageService.save(MessageTypeEnum.GROUP_APPLY.getCode(), userContactParam.getOpinion());
            if (messageId == null) {
                return false;
            }
            // 查询群主和群管理员，发送加群申请消息
            GroupMemberParam groupMemberParam = new GroupMemberParam();
            groupMemberParam.setGroupId(userContactParam.getContactId());
            groupMemberParam.setUserRoleList(Arrays.asList(MemberRoleEnum.OWNER.getCode(), MemberRoleEnum.MANAGER.getCode()));
            List<GroupMemberVO> groupMemberVOS = groupMemberService.listMember(groupMemberParam);
            List<String> userIds = groupMemberVOS.stream().map(GroupMemberVO::getUserId).collect(Collectors.toList());
            boolean save = userSessionMessageService.batchSave(userContactParam.getUserId(), userIds, messageId);
            if (!save) {
                return false;
            }

            userIds.forEach(userId -> {
                UserSendMsgParam userSendMsgParam = new UserSendMsgParam();
                userSendMsgParam.setSenderId(userContactParam.getUserId());
                userSendMsgParam.setReceiveId(userId);
                userSendMsgParam.setType(WSSendMessageTypeEnum.GROUP_APPLY.getCode());
                WebSocketContext.sendMsg(userSendMsgParam);
            });
        }
        return true;
    }

    public Boolean accept(UserContactParam userContactParam) {
        UserSessionMessage message = userSessionMessageService.getById(userContactParam.getMessageId());
        if (MessageStatusEnum.READ.getCode().equals(message.getStatus())) {
            throw new ServiceException(ExceptionEnum.USER_APPLY_HANDLED);
        }
        message.setStatus(MessageStatusEnum.READ.getCode());
        message.setUpdateTime(LocalDateTime.now());
        // 申请消息更新已读
        userSessionMessageService.updateById(message);

        // 拒绝直接返回
        if (!userContactParam.getAccept()) {
            return true;
        }
        // 同意好友, 保存为联系人
        UserContact userContact = new UserContact();
        userContact.setUserId(userContactParam.getUserId());
        userContact.setContactId(userContactParam.getContactId());
        userContact.setType(UserContactEnum.FRIEND.getCode());
        userContact.setStatus(UserContactStatusEnum.VALID.getCode());
        userContact.setLastContractTime(LocalDateTime.now());
        userContact.setCreateTime(LocalDateTime.now());
        userContact.setUpdateTime(LocalDateTime.now());
        save(userContact);

        // 更改申请人联系人状态
        UserContact destUserContact = getUserContact(userContactParam.getContactId(), userContactParam.getUserId());
        destUserContact.setStatus(UserContactStatusEnum.VALID.getCode());
        destUserContact.setUpdateTime(LocalDateTime.now());
        updateById(destUserContact);

        UserSendMsgParam userSendMsgParam = new UserSendMsgParam();
        userSendMsgParam.setSenderId(userContactParam.getUserId());
        userSendMsgParam.setReceiveId(userContactParam.getContactId());
        userSendMsgParam.setType(WSSendMessageTypeEnum.FRIEND_APPLY_SUCCESS.getCode());
        WebSocketContext.sendMsg(userSendMsgParam);
        return true;
    }

    /**
     * 最近60天的联系人
     * @param userId
     * @return
     */
    public List<UserContactVO> recentContact(String userId) {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<UserContact> queryWrapper = Wrappers.<UserContact>lambdaQuery()
                .eq(UserContact::getUserId, userId)
                .between(UserContact::getLastContractTime, now.minusDays(60), now);
        List<UserContact> userContacts = list(queryWrapper);
        if (CollectionUtils.isEmpty(userContacts)) {
            return Collections.emptyList();
        }
        // 好友信息
        List<String> friendIds = userContacts.stream()
                .filter(e -> Objects.equals(e.getType(), UserContactEnum.FRIEND.getCode()))
                .map(UserContact::getContactId)
                .toList();
        List<UserInfoVO> friendList = userInfoService.getByUserIds(friendIds);
        Map<String, UserInfoVO> friendMap = friendList.stream().collect(Collectors.toMap(UserInfoVO::getUserId, Function.identity()));

        //todo 群信息
        List<UserContactVO> userContactVOS = new ArrayList<>(userContacts.size());
        for (UserContact userContact : userContacts) {
            UserInfoVO friend = friendMap.get(userContact.getContactId());
            UserContactVO userContactVO = ObjUtils.copy(userContact, UserContactVO.class);
            userContactVO.setContactName(friend.getNickName());
            userContactVOS.add(userContactVO);
        }
        return userContactVOS;
    }
}

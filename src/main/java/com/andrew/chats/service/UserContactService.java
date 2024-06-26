package com.andrew.chats.service;

import com.andrew.chats.common.params.GroupMemberParam;
import com.andrew.chats.common.params.UserContactParam;
import com.andrew.chats.common.params.UserSendMsgParam;
import com.andrew.chats.common.vo.GroupMemberVO;
import com.andrew.chats.common.vo.UserInfoVO;
import com.andrew.chats.config.ServiceException;
import com.andrew.chats.dao.mapper.UserContactMapper;
import com.andrew.chats.dao.model.ChatGroup;
import com.andrew.chats.dao.model.UserContact;
import com.andrew.chats.dao.model.UserSessionMessage;
import com.andrew.chats.enums.*;
import com.andrew.chats.netty.WebSocketContext;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
        if (Objects.equals(userContactParam.getType(), UserContactEnum.USER.getCode())) { // 加人
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

        if (Objects.equals(userContactParam.getType(), UserContactEnum.USER.getCode())) {
            // 消息保存
            Long messageId = messageService.save(MessageTypeEnum.FRIEND_APPLY.getCode(), userContactParam.getOpinion());
            if (messageId == null) {
                return false;
            }
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
//            GroupMemberParam groupMemberParam = new GroupMemberParam();
//            groupMemberParam.setGroupId(userContactParam.getContactId());
//            groupMemberParam.setUserRoleList(Arrays.asList(MemberRoleEnum.OWNER.getCode(), MemberRoleEnum.MANAGER.getCode()));
//            List<GroupMemberVO> groupMemberVOS = groupMemberService.listMember(groupMemberParam);
//            List<String> userIds = groupMemberVOS.stream().map(GroupMemberVO::getUserId).collect(Collectors.toList());
//            messageService.saveGroupApply(userIds, userContactParam);
//            userIds.forEach(userId -> {
//                userSendMsgReqVO.setReceiveId(userId);
//                WebSocketContext.sendMsg(userSendMsgReqVO);
//            });
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
        userContact.setType(UserContactEnum.USER.getCode());
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
}

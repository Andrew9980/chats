package com.andrew.chats.service;

import com.andrew.chats.config.ServiceException;
import com.andrew.chats.dao.mapper.UserContactMapper;
import com.andrew.chats.dao.model.ChatGroup;
import com.andrew.chats.dao.model.UserContact;
import com.andrew.chats.enums.*;
import com.andrew.chats.netty.WebSocketContext;
import com.andrew.chats.vo.GroupMemberReqVO;
import com.andrew.chats.vo.GroupMemberResVO;
import com.andrew.chats.vo.UserInfoVO;
import com.andrew.chats.vo.UserSendMsgReqVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    private ChatGroupService chatGroupService;

    @Autowired
    private GroupMemberService groupMemberService;

    /**
     * 根据状态查询用户联系人列表
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
    public boolean apply(UserSendMsgReqVO userSendMsgReqVO) {
        UserInfoVO userInfo;
        ChatGroup chatGroup;
        if (Objects.equals(userSendMsgReqVO.getType(), UserContactEnum.USER.getCode())) { // 加人
            userInfo = userInfoService.getByUserId(userSendMsgReqVO.getReceiveId());
            if (Objects.isNull(userInfo) || Objects.equals(userInfo.getStatus(), UserStatusEnum.BANED.getCode())) {
                throw new ServiceException(ExceptionEnum.USER_NOT_EXIST);
            }
        } else { // 加群
            chatGroup = chatGroupService.getByGroupId(userSendMsgReqVO.getReceiveId());
            if (Objects.isNull(chatGroup) || Objects.equals(chatGroup.getStatus(), GroupStatusEnum.BANED.getCode())) {
                throw new ServiceException(ExceptionEnum.GROUP_NOT_EXIST);
            }
        }
        try {
            // 校验重复申请
            UserContact userContact = getOne(Wrappers.<UserContact>lambdaQuery()
                    .eq(UserContact::getUserId, userSendMsgReqVO.getSenderId())
                    .eq(UserContact::getContactId, userSendMsgReqVO.getReceiveId()));
            if (Objects.nonNull(userContact)) {
                throw new ServiceException(ExceptionEnum.USER_CONTACT_EXIST);
            }
            // 记录申请中的联系人
            userContact = new UserContact();
            userContact.setUserId(userSendMsgReqVO.getSenderId());
            userContact.setContactId(userSendMsgReqVO.getReceiveId());
            userContact.setType(userSendMsgReqVO.getType());
            userContact.setStatus(UserContactStatusEnum.APPLY.getCode());
            userContact.setLastContractTime(LocalDateTime.now());
            userContact.setCreateTime(LocalDateTime.now());
            userContact.setUpdateTime(LocalDateTime.now());
            save(userContact);

            if (Objects.equals(userSendMsgReqVO.getType(), UserContactEnum.USER.getCode())) {
                // 消息保存
                messageService.save(userSendMsgReqVO);
                // 发送申请
                WebSocketContext.sendMsg(userSendMsgReqVO);
            } else {
                // 发送群申请消息，将申请消息发送到群主和群管理员
                GroupMemberReqVO groupMemberReqVO = new GroupMemberReqVO();
                groupMemberReqVO.setGroupId(userSendMsgReqVO.getReceiveId());
                groupMemberReqVO.setUserRoleList(Arrays.asList(MemberRoleEnum.OWNER.getCode(), MemberRoleEnum.MANAGER.getCode()));
                List<GroupMemberResVO> groupMemberResVOS = groupMemberService.listMember(groupMemberReqVO);
                List<String> userIds = groupMemberResVOS.stream().map(GroupMemberResVO::getUserId).collect(Collectors.toList());
                messageService.saveGroupApply(userIds, userSendMsgReqVO);
                userIds.forEach(userId -> {
                    userSendMsgReqVO.setReceiveId(userId);
                    WebSocketContext.sendMsg(userSendMsgReqVO);
                });
            }
            return true;
        } catch (Exception e) {
            log.error("发送申请失败 e={}", ExceptionUtils.getStackTrace(e));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
}

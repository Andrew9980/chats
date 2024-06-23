package com.andrew.chats.dao.service;

import com.andrew.chats.config.ServiceException;
import com.andrew.chats.dao.mapper.UserContactMapper;
import com.andrew.chats.dao.model.UserContact;
import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.enums.UserContactStatusEnum;
import com.andrew.chats.netty.NettyContext;
import com.andrew.chats.vo.UserInfoVO;
import com.andrew.chats.vo.UserSendMsgReqVO;
import com.andrew.chats.vo.base.RespResult;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户联系 服务实现类
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@Service
public class UserContactService extends ServiceImpl<UserContactMapper, UserContact> implements IService<UserContact> {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserInfoService userInfoService;

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
        UserInfoVO userInfo = userInfoService.getByUserId(userSendMsgReqVO.getReceiveId());
        if (Objects.isNull(userInfo)) {
            throw new ServiceException(ExceptionEnum.USER_NOT_EXIST);
        }
        UserContact userContact = getOne(Wrappers.<UserContact>lambdaQuery()
                .eq(UserContact::getUserId, userSendMsgReqVO.getSenderId()).eq(UserContact::getContactId, userSendMsgReqVO.getReceiveId()));
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
        // 发送好友申请
        NettyContext.sendMsg(userSendMsgReqVO);
        // 消息保存
        messageService.save(userSendMsgReqVO);
        return true;
    }
}

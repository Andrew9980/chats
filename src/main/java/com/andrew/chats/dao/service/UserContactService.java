package com.andrew.chats.dao.service;

import com.andrew.chats.dao.model.UserContact;
import com.andrew.chats.dao.mapper.UserContactMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public UserContact getUserContact(String userId, String contactId) {
        return getOne(
                Wrappers.<UserContact>lambdaQuery().eq(UserContact::getUserId, userId)
                        .eq(UserContact::getContactId, contactId)
        );
    }

}

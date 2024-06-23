package com.andrew.chats.service;

import com.andrew.chats.dao.service.UserContactService;
import com.andrew.chats.dao.service.UserInfoService;
import com.andrew.chats.enums.UserContactEnum;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ContactManager {

    private UserContactService userContactService;

    private UserInfoService userInfoService;

    /**
     * 好友/群申请
     */
    public void apply(String objId, Integer type) {
        if (Objects.equals(UserContactEnum.USER.getCode(), type)) {

        }
    }

}

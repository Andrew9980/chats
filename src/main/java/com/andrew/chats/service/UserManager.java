package com.andrew.chats.service;

import com.andrew.chats.config.ServiceException;
import com.andrew.chats.dao.model.UserInfo;
import com.andrew.chats.dao.service.UserInfoService;
import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.enums.UserStatusEnum;
import com.andrew.chats.utils.util.SecretUtil;
import com.andrew.chats.utils.util.ObjUtils;
import com.andrew.chats.vo.UserInfoReqVO;
import com.andrew.chats.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserManager {

    @Autowired
    private UserInfoService userInfoService;

}

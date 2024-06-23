package com.andrew.chats.dao.service;

import com.andrew.chats.config.ServiceException;
import com.andrew.chats.dao.model.UserInfo;
import com.andrew.chats.dao.mapper.UserInfoMapper;
import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.enums.UserStatusEnum;
import com.andrew.chats.utils.util.ObjUtils;
import com.andrew.chats.utils.util.SecretUtil;
import com.andrew.chats.vo.UserInfoReqVO;
import com.andrew.chats.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author andrew
 * @since 2024-06-16
 */
@Service
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo> implements IService<UserInfo> {

    public UserInfoVO getByUserId(String userId) {
        UserInfo userInfo = getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        return ObjUtils.copy(userInfo, UserInfoVO.class);
    }

    public Boolean register(UserInfoReqVO vo) {
        String salt = SecretUtil.getSalt();
        UserInfo userInfo = new UserInfo();
        String userId = RandomStringUtils.random(11, false, true);
        userInfo.setUserId(userId);
        userInfo.setEmail(vo.getEmail());
        userInfo.setNickName(vo.getEmail());
        userInfo.setStatus(UserStatusEnum.VALID.getCode());
        userInfo.setSalt(salt);
        userInfo.setPassword(SecretUtil.encode(salt, vo.getPassword()));
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());
        return save(userInfo);
    }

    public String login(UserInfoReqVO vo) {
        UserInfo userInfo = getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getEmail, vo.getEmail()));
        if (Objects.isNull(userInfo)) {
            throw new ServiceException(ExceptionEnum.USER_NOT_EXIST);
        }
        if (!Objects.equals(userInfo.getStatus(), UserStatusEnum.VALID.getCode())) {
            throw new ServiceException(ExceptionEnum.USER_STATUS_ERROR);
        }
        if (!SecretUtil.check(vo.getPassword(), userInfo.getSalt(), userInfo.getPassword())) {
            throw new ServiceException(ExceptionEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        return SecretUtil.getToken(userInfo.getUserId());
    }

}

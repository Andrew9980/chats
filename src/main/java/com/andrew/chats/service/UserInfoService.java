package com.andrew.chats.service;

import com.andrew.chats.config.ServiceException;
import com.andrew.chats.dao.model.UserInfo;
import com.andrew.chats.dao.mapper.UserInfoMapper;
import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.enums.UserStatusEnum;
import com.andrew.chats.common.utils.ObjUtils;
import com.andrew.chats.common.utils.SecretUtil;
import com.andrew.chats.common.utils.ServiceUtil;
import com.andrew.chats.common.params.UserInfoParam;
import com.andrew.chats.common.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo> {

    public UserInfoVO getByUserId(String userId) {
        UserInfo userInfo = getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        return ObjUtils.copy(userInfo, UserInfoVO.class);
    }

    public Boolean register(UserInfoParam vo) {
        String salt = SecretUtil.getSalt();
        UserInfo userInfo = new UserInfo();
        String userId = ServiceUtil.getRandomId();
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

    public String login(UserInfoParam vo) {
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

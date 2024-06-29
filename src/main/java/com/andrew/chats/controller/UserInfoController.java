package com.andrew.chats.controller;

import com.andrew.chats.service.UserInfoService;
import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.common.utils.SecretUtil;
import com.andrew.chats.common.params.UserInfoParam;
import com.andrew.chats.common.vo.UserInfoVO;
import com.andrew.chats.common.base.RespResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author andrew
 * @since 2024-06-16
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/register")
    public RespResult<Boolean> register(@RequestBody UserInfoParam vo) {
        if (StringUtils.isBlank(vo.getEmail())) {
            return RespResult.fail(ExceptionEnum.EMAIL_NOT_EMPTY);
        }
        if (StringUtils.isBlank(vo.getPassword())) {
            return RespResult.fail(ExceptionEnum.PASSWORD_NOT_EMPTY);
        }
        return RespResult.success(userInfoService.register(vo));
    }

    @PostMapping("/login")
    public RespResult<String> login(@RequestBody UserInfoParam vo) {
        if (StringUtils.isBlank(vo.getEmail())) {
            return RespResult.fail(ExceptionEnum.EMAIL_NOT_EMPTY);
        }
        if (StringUtils.isBlank(vo.getPassword())) {
            return RespResult.fail(ExceptionEnum.PASSWORD_NOT_EMPTY);
        }
        return RespResult.success(userInfoService.login(vo));
    }

    @GetMapping("/jwtVerify")
    public RespResult<String> verify(String token) {
        return RespResult.success(SecretUtil.verify(token));
    }

    @GetMapping("/queryUser")
    public RespResult<UserInfoVO> queryUser(UserInfoParam userInfoParam) {
        return RespResult.success(userInfoService.getByUserId(userInfoParam.getUserId()));
    }
}

package com.andrew.chats.controller;

import com.andrew.chats.common.base.RespResult;
import com.andrew.chats.common.vo.UserSessionMessageVO;
import com.andrew.chats.dao.model.UserSessionMessage;
import com.andrew.chats.service.UserSessionMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import com.andrew.chats.common.base.RespResult;
import com.andrew.chats.dao.model.UserSessionMessage;
import com.andrew.chats.service.UserSessionMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户联系人消息表 前端控制器
 * </p>
 *
 * @author andrew
 * @since 2024-06-28
 */
@RestController
@RequestMapping("/userSessionMessage")
public class UserSessionMessageController {

    @Autowired
    private UserSessionMessageService userSessionMessageService;

    @GetMapping("/list")
    public RespResult<List<UserSessionMessageVO>> list(String userId) {
        return RespResult.success(userSessionMessageService.list(userId));
    }

}

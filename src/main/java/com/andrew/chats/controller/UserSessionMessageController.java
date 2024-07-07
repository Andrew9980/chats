package com.andrew.chats.controller;

import com.andrew.chats.common.base.RespResult;
import com.andrew.chats.dao.model.UserSessionMessage;
import com.andrew.chats.service.UserSessionMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户联系人消息表 前端控制器
 * </p>
 *
 * @author andrew
 * @since 2024-06-28
 */
@Controller
@RequestMapping("/userSessionMessage")
public class UserSessionMessageController {

    @Autowired
    private UserSessionMessageService userSessionMessageService;

    /**
     * 查询用户联系人消息列表
     */
    @GetMapping("/list")
    public RespResult list() {
        userSessionMessageService.

        return RespResult.success();
    }

}

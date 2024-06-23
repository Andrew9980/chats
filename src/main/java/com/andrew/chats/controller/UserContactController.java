package com.andrew.chats.controller;

import com.andrew.chats.dao.service.UserContactService;
import com.andrew.chats.vo.base.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户联系 前端控制器
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@Controller
@RequestMapping("/userContact")
public class UserContactController {

    @Autowired
    private UserContactService userContactService;

    @GetMapping("/apply")
    public RespResult apply(String userId) {

    }

}

package com.andrew.chats.controller;

import com.andrew.chats.dao.service.UserContactService;
import com.andrew.chats.vo.UserSendMsgReqVO;
import com.andrew.chats.vo.base.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户联系 前端控制器
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@RestController
@RequestMapping("/userContact")
public class UserContactController {

    @Autowired
    private UserContactService userContactService;

    @PostMapping("/apply")
    public RespResult apply(@RequestBody UserSendMsgReqVO userSendMsgReqVO) {
        userContactService.apply(userSendMsgReqVO);
        return RespResult.success();
    }

}

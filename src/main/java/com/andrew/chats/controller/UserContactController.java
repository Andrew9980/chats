package com.andrew.chats.controller;

import com.andrew.chats.service.UserContactService;
import com.andrew.chats.vo.UserSendMsgReqVO;
import com.andrew.chats.vo.base.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public RespResult<Boolean> apply(@RequestBody UserSendMsgReqVO userSendMsgReqVO) {
        return RespResult.success(userContactService.apply(userSendMsgReqVO));
    }

}

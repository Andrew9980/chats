package com.andrew.chats.controller;

import com.andrew.chats.service.UserContactService;
import com.andrew.chats.common.params.UserContactParam;
import com.andrew.chats.common.base.RespResult;
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

    /**
     * 好友申请
     * @param userContactParam
     * @return
     */
    @PostMapping("/apply")
    public RespResult<Boolean> apply(@RequestBody UserContactParam userContactParam) {
        return RespResult.success(userContactService.apply(userContactParam));
    }

    /**
     * 同意好友申请
     */
    @PostMapping("/accept")
    public RespResult<Boolean> accept(@RequestBody UserContactParam userContactParam) {
        return RespResult.success(userContactService.accept(userContactParam));
    }

}

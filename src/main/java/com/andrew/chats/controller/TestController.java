package com.andrew.chats.controller;

import com.andrew.chats.common.base.RespResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public RespResult test() {
        return RespResult.success();
    }

}

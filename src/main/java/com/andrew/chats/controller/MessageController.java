package com.andrew.chats.controller;

import com.andrew.chats.service.MessageService;
import com.andrew.chats.vo.MessageReqVO;
import com.andrew.chats.vo.MessageResVO;
import com.andrew.chats.vo.base.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 消息表 前端控制器
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/list")
    public RespResult<List<MessageResVO>> list(@RequestBody MessageReqVO messageReqVO) {
        return RespResult.success(messageService.listMessage(messageReqVO));
    }

}

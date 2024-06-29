package com.andrew.chats.controller;

import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.service.ChatGroupService;
import com.andrew.chats.common.params.GroupApplyParam;
import com.andrew.chats.common.params.GroupParam;
import com.andrew.chats.common.base.RespResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 群 前端控制器
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@Controller
@RequestMapping("/chatGroup")
public class ChatGroupController {

    @Autowired
    private ChatGroupService chatGroupService;

    @PostMapping("/create")
    public RespResult<Boolean> create(@RequestBody GroupParam reqVO) {
        return RespResult.success(chatGroupService.create(reqVO));
    }

    @GetMapping("/update")
    public RespResult<Boolean> update(@RequestBody GroupParam reqVO) {
        if (StringUtils.isEmpty(reqVO.getGroupId())) {
            return RespResult.fail(ExceptionEnum.GROUP_ID_NOT_EMPTY);
        }
        return RespResult.success(chatGroupService.update(reqVO));
    }

    @GetMapping("/joinGroup")
    public RespResult<Boolean> joinGroup(@RequestBody GroupApplyParam reqVO) {
        return RespResult.success(chatGroupService.joinGroup(reqVO));
    }

}

package com.andrew.chats.controller;

import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.service.GroupMemberService;
import com.andrew.chats.vo.GroupMemberReqVO;
import com.andrew.chats.vo.GroupMemberResVO;
import com.andrew.chats.vo.base.RespResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 群成员 前端控制器
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@Controller
@RequestMapping("/groupMember")
public class GroupMemberController {

    @Autowired
    private GroupMemberService groupMemberService;


    @GetMapping("/list")
    public RespResult<List<GroupMemberResVO>> listMember(GroupMemberReqVO reqVO) {
        if (StringUtils.isNotEmpty(reqVO.getGroupId())) {
            return RespResult.fail(ExceptionEnum.GROUP_ID_NOT_EMPTY);
        }
        return RespResult.success(groupMemberService.listMember(reqVO));
    }

}

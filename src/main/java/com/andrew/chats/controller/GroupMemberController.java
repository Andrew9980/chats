package com.andrew.chats.controller;

import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.service.GroupMemberService;
import com.andrew.chats.common.params.GroupMemberParam;
import com.andrew.chats.common.vo.GroupMemberVO;
import com.andrew.chats.common.base.RespResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 查询群成员
     * @param reqVO
     * @return
     */
    @GetMapping("/list")
    public RespResult<List<GroupMemberVO>> listMember(GroupMemberParam reqVO) {
        if (StringUtils.isNotEmpty(reqVO.getGroupId())) {
            return RespResult.fail(ExceptionEnum.GROUP_ID_NOT_EMPTY);
        }
        return RespResult.success(groupMemberService.listMember(reqVO));
    }



}

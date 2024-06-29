package com.andrew.chats.service;

import com.andrew.chats.dao.model.GroupMember;
import com.andrew.chats.dao.mapper.GroupMemberMapper;
import com.andrew.chats.enums.GroupMemberStatusEnum;
import com.andrew.chats.enums.MemberRoleEnum;
import com.andrew.chats.common.utils.ObjUtils;
import com.andrew.chats.common.params.GroupMemberParam;
import com.andrew.chats.common.vo.GroupMemberVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 群成员 服务实现类
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@Service
public class GroupMemberService extends ServiceImpl<GroupMemberMapper, GroupMember> {

    public List<GroupMemberVO> listMember(GroupMemberParam memberReqVO) {
        List<GroupMember> list = list(Wrappers.<GroupMember>lambdaQuery()
                .eq(StringUtils.isNotEmpty(memberReqVO.getGroupId()), GroupMember::getGroupId, memberReqVO.getGroupId())
                .in(CollectionUtils.isNotEmpty(memberReqVO.getUserRoleList()), GroupMember::getUserRole, memberReqVO.getUserRoleList()));
        return ObjUtils.copyList(list, GroupMemberVO.class);
    }

    public boolean create(String groupId, String userId) {
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(groupId);
        groupMember.setUserId(userId);
        groupMember.setJoinTime(LocalDateTime.now());
        groupMember.setStatus(GroupMemberStatusEnum.VALID.getCode());
        groupMember.setUserRole(MemberRoleEnum.MEMBER.getCode());
        groupMember.setCreateTime(LocalDateTime.now());
        groupMember.setUpdateTime(LocalDateTime.now());
        return save(groupMember);
    }

}

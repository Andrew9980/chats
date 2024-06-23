package com.andrew.chats.service;

import com.andrew.chats.dao.model.GroupMember;
import com.andrew.chats.dao.mapper.GroupMemberMapper;
import com.andrew.chats.utils.util.ObjUtils;
import com.andrew.chats.vo.GroupMemberReqVO;
import com.andrew.chats.vo.GroupMemberResVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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

    public List<GroupMemberResVO> listMember(GroupMemberReqVO memberReqVO) {
        List<GroupMember> list = list(Wrappers.<GroupMember>lambdaQuery()
                .eq(StringUtils.isNotEmpty(memberReqVO.getGroupId()), GroupMember::getGroupId, memberReqVO.getGroupId())
                .in(CollectionUtils.isNotEmpty(memberReqVO.getUserRoleList()), GroupMember::getUserRole, memberReqVO.getUserRoleList()));
        return ObjUtils.copyList(list, GroupMemberResVO.class);
    }

}

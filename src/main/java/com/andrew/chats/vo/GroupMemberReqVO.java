package com.andrew.chats.vo;

import lombok.Data;

import java.util.List;

@Data
public class GroupMemberReqVO {

    private String groupId;

    private String groupName;

    private String groupInfo;

    private List<Integer> userRoleList;

}

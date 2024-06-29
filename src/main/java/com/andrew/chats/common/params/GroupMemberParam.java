package com.andrew.chats.common.params;

import lombok.Data;

import java.util.List;

@Data
public class GroupMemberParam {

    private String groupId;

    private String groupName;

    private String groupInfo;

    private List<Integer> userRoleList;

}

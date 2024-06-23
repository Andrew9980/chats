package com.andrew.chats.vo;

import lombok.Data;

import java.util.List;

@Data
public class GroupMemberReqVO {

    private String groupId;

    private List<Integer> userRoleList;

}

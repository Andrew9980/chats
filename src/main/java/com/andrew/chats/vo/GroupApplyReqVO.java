package com.andrew.chats.vo;

import lombok.Data;

/**
 * 加群申请VO
 */
@Data
public class GroupApplyReqVO {

    private String groupId;

    private String userId;

    private String applyReason;

}

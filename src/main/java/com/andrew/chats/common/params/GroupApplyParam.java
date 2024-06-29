package com.andrew.chats.common.params;

import lombok.Data;

/**
 * 加群申请VO
 */
@Data
public class GroupApplyParam {

    private String groupId;

    private String userId;

    private String applyReason;

}

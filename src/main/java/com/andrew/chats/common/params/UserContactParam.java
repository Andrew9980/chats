package com.andrew.chats.common.params;

import lombok.Data;

@Data
public class UserContactParam {

    private Long messageId;

    private String userId;

    private String contactId;

    private Boolean accept;

    // 1 好友申请 2 群申请
    private Integer type;

    private String opinion;

}

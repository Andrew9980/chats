package com.andrew.chats.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserContactVO {

    /**
     * user_info表user_id
     */
    private String userId;

    /**
     * 联系人id type=1 user_info表user_id type=2 group表group_id
     */
    private String contactId;

    private String contactName;

    /**
     * 备注
     */
    private String notes;

    /**
     * 联系类型，1：联系用户 2：联系群
     */
    private Integer type;

    /**
     * 联系状态，1：正常 2：拉黑
     */
    private Integer status;

    /**
     * 消息未读数
     */
    private Integer unreadCount;

    /**
     * 最后联系时间
     */
    private LocalDateTime lastContractTime;

}

package com.andrew.chats.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSessionMessageVO {

    /**
     * 发送者
     */
    private String senderId;

    /**
     * 接收者
     */
    private String receiveId;

    /**
     * 消息id
     */
    private Long messageId;

    /**
     * 1：未读 2：已读
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}

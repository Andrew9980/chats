package com.andrew.chats.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSessionMessageVO {

    /**
     * 会话消息id
     */
    private Long id;

    /**
     * 发送者
     */
    private String senderId;

    private String senderAvatar;

    private String senderName;

    /**
     * 接收者
     */
    private String receiveId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 1：未读 2：已读
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}

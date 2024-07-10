package com.andrew.chats.common.vo;

import lombok.Data;

@Data
public class MessageVO {

    private String senderId;

    private String receiveId;

    private String content;

    private String sendTime;

}

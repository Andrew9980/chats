package com.andrew.chats.vo;

import lombok.Data;

@Data
public class MessageResVO {

    private String senderId;

    private String receiveId;

    private String content;

}

package com.andrew.chats.vo;

import lombok.Data;

@Data
public class MessageReqVO {

    private String senderId;

    private String receiveId;

    private Integer status;

    private Integer type;

}

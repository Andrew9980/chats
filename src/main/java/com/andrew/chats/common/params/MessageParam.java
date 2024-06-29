package com.andrew.chats.common.params;

import lombok.Data;

@Data
public class MessageParam {

    private String senderId;

    private String receiveId;

    private Integer status;

    private Integer type;

}

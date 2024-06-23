package com.andrew.chats.vo;

import lombok.Data;

@Data
public class UserSendMsgReqVO {

    /**
     * 发送人
     */
    private String senderId;

    /**
     * 接收人
     */
    private String receiveId;

    /**
     * 消息类型
     */
    private Byte type;

    /**
     * 消息内容
     */
    private String content;

}

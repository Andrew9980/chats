package com.andrew.chats.common.params;

import lombok.Data;

@Data
public class UserSendMsgParam {

    /**
     * 消息id
     */
    private Long messageId;

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
     * {@link com.andrew.chats.enums.WSSendMessageTypeEnum}
     */
    private Integer type;

    /**
     * 消息内容
     */
    private String content;

}

package com.andrew.chats.enums;

public enum MessageStatusEnum {

    UN_READ(1, "未读"),
    READ(2, "已读");

    private Integer code;

    private String desc;

    MessageStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

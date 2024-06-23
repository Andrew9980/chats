package com.andrew.chats.enums;

public enum MessageTypeEnum {

    FRIEND_APPLY(0, "好友申请"),
    GROUP_APPLY(1, "群申请"),
    TEXT(2, "文字"),
    IMAGE(3, "图片"),
    FILE(4, "文件");

    private Integer code;

    private String desc;

    MessageTypeEnum(Integer code, String desc) {
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

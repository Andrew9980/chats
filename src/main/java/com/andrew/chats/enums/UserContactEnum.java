package com.andrew.chats.enums;

public enum UserContactEnum {

    ROBOT(0, "机器人"),
    USER(1, "好友"),
    GROUP(2, "群");

    private Integer code;

    private String desc;

    UserContactEnum(Integer code, String desc) {
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

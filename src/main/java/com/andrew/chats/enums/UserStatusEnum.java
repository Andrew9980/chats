package com.andrew.chats.enums;

public enum UserStatusEnum {

    VALID(1, "正常");

    private Integer code;

    private String desc;

    UserStatusEnum(Integer code, String desc) {
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

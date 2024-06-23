package com.andrew.chats.enums;

public enum GroupStatusEnum {

    VALID(1, "正常"),
    BANED(2, "封禁");

    private Integer code;

    private String desc;

    GroupStatusEnum(Integer code, String desc) {
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

package com.andrew.chats.enums;

public enum GroupMemberStatusEnum {

    VALID(1, "正常"),
    MUTED(2, "禁言"),
    KICKED(3, "踢出");

    private Integer code;

    private String desc;

    GroupMemberStatusEnum(Integer code, String desc) {
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

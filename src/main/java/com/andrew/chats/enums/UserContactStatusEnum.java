package com.andrew.chats.enums;

public enum UserContactStatusEnum {

    APPLY((byte) 0, "申请中"),
    VALID((byte) 1, "正常"),
    BLACK((byte) 2, "黑名单");

    private Byte code;

    private String desc;

    UserContactStatusEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

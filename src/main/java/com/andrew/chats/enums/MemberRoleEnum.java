package com.andrew.chats.enums;


public enum MemberRoleEnum {

    OWNER(1, "群主"),
    MANAGER(2, "管理员");

    private Integer code;

    private String desc;

    MemberRoleEnum(Integer code, String desc) {
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

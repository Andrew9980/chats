package com.andrew.chats.enums;

public enum GroupTypeEnum {

    NORMAL(1, "普通群"),
    FREE(2, "自由群"),
    FORBID(3, "禁言群");

    private Integer code;

    private String desc;

    GroupTypeEnum(Integer code, String desc) {
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

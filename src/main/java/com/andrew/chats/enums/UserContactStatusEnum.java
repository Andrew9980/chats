package com.andrew.chats.enums;

public enum UserContactStatusEnum {

    APPLY(0, "申请中"),
    VALID(1, "正常"),
    BLACK(2, "黑名单"),
    QUIT(3, "退出群聊");

    private Integer code;

    private String desc;

    UserContactStatusEnum(Integer code, String desc) {
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

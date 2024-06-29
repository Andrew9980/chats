package com.andrew.chats.enums;

public enum WSSendMessageTypeEnum {

    FRIEND_APPLY(1, "发送好友申请"),
    FRIEND_APPLY_SUCCESS(2, "好友申请成功"),
    GROUP_APPLY(3, "发送群申请"),
    GROUP_APPLY_SUCCESS(4, "群申请成功"),
    BANED(5, "账户封禁");

    private Integer code;

    private String desc;

    WSSendMessageTypeEnum(Integer code, String desc) {
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

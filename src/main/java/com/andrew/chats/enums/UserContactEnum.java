package com.andrew.chats.enums;

import lombok.Getter;

@Getter
public enum UserContactEnum {

    ROBOT(0, "机器人"),
    FRIEND(1, "好友"),
    GROUP(2, "群");

    private Integer code;

    private String desc;

    UserContactEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}

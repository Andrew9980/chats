package com.andrew.chats.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVO implements Serializable {

    private String userId;

    private String nickName;

    private Integer status;

}

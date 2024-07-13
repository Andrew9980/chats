package com.andrew.chats.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserInfoVO implements Serializable {

    private String userId;

    private String email;

    private String nickName;

    private String personalSignature;

    private Integer sex;

    private LocalDate birthday;

    private String areaId;

    private String areaName;

    private Integer status;

    private LocalDateTime lastLoginTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer yn;

}

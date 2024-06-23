package com.andrew.chats.config;

import com.andrew.chats.enums.ExceptionEnum;
import lombok.Data;

@Data
public class ServiceException extends RuntimeException {

    private String code;

    private String desc;

    public ServiceException(ExceptionEnum ee) {
        this.code = ee.getCode();
        this.desc = ee.getDesc();
    }

}

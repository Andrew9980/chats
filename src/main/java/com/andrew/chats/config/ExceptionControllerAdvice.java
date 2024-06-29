package com.andrew.chats.config;

import com.andrew.chats.common.base.RespResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.andrew.chats.common.Constants.EXCEPTION_CODE;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public RespResult<Boolean> exceptionHandle(Exception e) {
        return RespResult.fail(EXCEPTION_CODE, e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public RespResult<Boolean> serviceExceptionHandle(ServiceException se) {
        return RespResult.fail(se.getCode(), se.getDesc());
    }

}

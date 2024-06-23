package com.andrew.chats.config;

import com.andrew.chats.vo.base.RespResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.andrew.chats.utils.Constants.EXCEPTION_CODE;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public RespResult<String> exceptionHandle(Exception e) {
        return RespResult.fail(EXCEPTION_CODE, e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public RespResult<String> serviceExceptionHandle(ServiceException se) {
        return RespResult.fail(se.getCode(), se.getDesc());
    }

}

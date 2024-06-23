package com.andrew.chats.vo.base;

import com.andrew.chats.enums.ExceptionEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RespResult<T> {

    private String code;

    private T data;

    private String message;

    private static String SUCCESS_CODE = "0000";

    public static <T> RespResult<T> success(T data) {
        return RespResult.<T>builder().data(data).code(SUCCESS_CODE).build();
    }
    public static RespResult<Boolean> success() {
        return RespResult.<Boolean>builder().data(Boolean.TRUE).code(SUCCESS_CODE).build();
    }

    public static RespResult<Boolean> fail(String code, String message) {
        return RespResult.<Boolean>builder().data(Boolean.FALSE).code(code).message(message).build();
    }

    public static RespResult<Boolean> fail(ExceptionEnum ee) {
        return RespResult.<Boolean>builder().data(Boolean.FALSE).code(ee.getCode()).message(ee.getDesc()).build();
    }
}

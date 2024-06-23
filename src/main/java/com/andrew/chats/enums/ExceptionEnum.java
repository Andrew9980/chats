package com.andrew.chats.enums;

public enum ExceptionEnum {

    EMAIL_NOT_EMPTY("0001", "邮箱不能为空"),
    PASSWORD_NOT_EMPTY("0002", "密码不能为空"),
    USER_NOT_EXIST("0003", "用户不存在"),
    ACCOUNT_OR_PASSWORD_ERROR("0004", "账号或密码错误"),
    USER_STATUS_ERROR("0005", "用户状态异常"),
    TOKEN_ERROR("0006", "登录失效，请重新登陆"),
    USER_CONTACT_ERROR("0007", "对方还不是您的好友，请申请好友后发送");

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    ExceptionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

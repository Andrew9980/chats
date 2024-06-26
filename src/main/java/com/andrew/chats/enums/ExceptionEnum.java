package com.andrew.chats.enums;

public enum ExceptionEnum {

    EMAIL_NOT_EMPTY("0001", "邮箱不能为空"),
    PASSWORD_NOT_EMPTY("0002", "密码不能为空"),
    USER_NOT_EXIST("0003", "用户不存在"),
    ACCOUNT_OR_PASSWORD_ERROR("0004", "账号或密码错误"),
    USER_STATUS_ERROR("0005", "用户状态异常"),
    TOKEN_ERROR("0006", "登录状态失效，请重新登陆"),
    USER_CONTACT_ERROR("0007", "对方还不是您的好友，请加好友后发送"),
    USER_CONTACT_EXIST("0008", "您已提交申请，请勿重复申请"),
    GROUP_NOT_EXIST("0009", "群不存在"),
    GROUP_CONTACT_ERROR("0010", "您已不在群聊，请加群后发送"),
    GROUP_ID_NOT_EMPTY("0011", "群id不能为空"),
    GROUP_FULL("0012", "群人数已满"),
    USER_APPLY_HANDLED("0013", "好友申请已处理");

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

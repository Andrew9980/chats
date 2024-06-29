package com.andrew.chats.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class ServiceUtil {

    /**
     * 纯数字11位随机数
     * @return
     */
    public static String getRandomId() {
        return RandomStringUtils.random(11, false, true);
    }
}

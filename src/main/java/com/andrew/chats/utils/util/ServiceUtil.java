package com.andrew.chats.utils.util;

import org.apache.commons.lang3.RandomStringUtils;

public class ServiceUtil {

    public String getUserId() {
        return RandomStringUtils.random(11, false, true);
    }
}

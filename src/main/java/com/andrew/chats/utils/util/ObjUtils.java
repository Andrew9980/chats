package com.andrew.chats.utils.util;


import org.springframework.beans.BeanUtils;

public class ObjUtils {

    public static <T> T copy(Object sourceData, Class<T> clazz) {
        try {
            T t = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(t, sourceData);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("复制对象失败" + e.getMessage());
        }
    }

}

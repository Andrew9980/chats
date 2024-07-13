package com.andrew.chats.common.utils;


import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ObjUtils {

    public static <T> T copy(Object sourceData, Class<T> clazz) {
        try {
            T t = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(sourceData, t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("复制对象失败" + e.getMessage());
        }
    }

    public static <T> List<T> copyList(List<?> sourceList, Class<T> clazz) {
        List<T> list = new ArrayList<>(sourceList.size());
        for (Object object : sourceList) {
            list.add(copy(object, clazz));
        }
        return list;
    }

}

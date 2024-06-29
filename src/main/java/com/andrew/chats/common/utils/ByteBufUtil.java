package com.andrew.chats.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class ByteBufUtil {


    public static ByteBuf getByteBuf(String str) {
        return Unpooled.copiedBuffer(str.getBytes(Charset.defaultCharset()));
    }

}

package com.andrew.chats.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartBeatHandle extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                Attribute<String> attr = channel.attr(AttributeKey.valueOf(channel.id().toString()));
                String user = attr.get();
                log.info("user {} 超时", user);
                ctx.close();
            } else if (e.state() == IdleState.READER_IDLE){
                ctx.writeAndFlush("heart");
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}

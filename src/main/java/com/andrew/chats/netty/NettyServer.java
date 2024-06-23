package com.andrew.chats.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NettyServer {

    private ServerBootstrap serverBootstrap = null;


    @Value("${wsPort}")
    private Integer wsPort;

    @Autowired
    private MessageHandle messageHandle;

    public NettyServer() {
        EventLoopGroup work = new NioEventLoopGroup();
        EventLoopGroup boss = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap()
                .group(work, boss)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new HttpServerCodec()) // http编解码器
                                .addLast(new HttpObjectAggregator(64 * 1024)) // 对http消息对象的聚合
                                .addLast(new ChunkedWriteHandler()) // 对大数据流写入的支持
                                .addLast(new IdleStateHandler(6, 0, 0, TimeUnit.SECONDS))
                                .addLast(new WebSocketServerProtocolHandler("/ws", null, true, 64*1024, true, true, 1000L))
                                .addLast(messageHandle);

                    }
                });
    }

    /**
     * 启动netty
     */
    public void start() {
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(wsPort).sync();
            log.info("netty websocket服务启动成功...");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("netty websocket启动失败, {}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

}

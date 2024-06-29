package com.andrew.chats.netty;

import com.andrew.chats.netty.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * netty服务在spring启动后启动
 */
@Component
public class ChatsNettyRunner implements ApplicationRunner {

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        webSocketServer.start();
    }
}

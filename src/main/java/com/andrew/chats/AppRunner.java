package com.andrew.chats;

import com.andrew.chats.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    private NettyServer nettyServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        nettyServer.start();
    }
}

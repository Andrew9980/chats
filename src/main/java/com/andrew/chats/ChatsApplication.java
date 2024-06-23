package com.andrew.chats;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.andrew.chats.dao.mapper")
public class ChatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatsApplication.class, args);
    }

}

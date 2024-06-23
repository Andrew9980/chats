package com.andrew.chats;

import com.andrew.chats.dao.mapper.UserInfoMapper;
import com.andrew.chats.dao.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class ChatsApplicationTests {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    void contextLoads() {


        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("000000000000");
        userInfo.setPassword("1234");
        userInfo.setSalt("123");
        userInfo.setEmail("2430209342@qq.com");
        userInfo.setNickName("andrew");
        userInfo.setPersonalSignature("");
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());
        userInfo.setYn(1);
        userInfoMapper.insert(userInfo);
        List<UserInfo> userInfos = userInfoMapper.selectList(null);
        System.out.println(userInfos);
    }

}

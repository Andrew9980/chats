package com.andrew.chats.netty;

import com.andrew.chats.dao.model.Message;
import com.andrew.chats.dao.model.UserContact;
import com.andrew.chats.dao.service.MessageService;
import com.andrew.chats.dao.service.UserContactService;
import com.andrew.chats.dao.service.UserInfoService;
import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.enums.UserContactStatusEnum;
import com.andrew.chats.utils.util.JSONUtil;
import com.andrew.chats.vo.UserSendMsgReqVO;
import com.andrew.chats.vo.base.RespResult;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NettyContext {

    private static final ConcurrentHashMap<String, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * 将userId放入channel中
     * @param userId
     * @param channel
     */
    public static void addUser2Channel(String userId, Channel channel) {
        String channelId = channel.id().asShortText();
        AttributeKey<String> attributeKey;
        if (!AttributeKey.exists(channelId)) { // 不存在就创建
            attributeKey = AttributeKey.newInstance(channelId);
        } else {
            attributeKey = AttributeKey.valueOf(channelId);
        }
        channel.attr(attributeKey).set(userId);
        addUserChannelMap(userId, channel);
    }

    public static void addUserChannelMap(String userId, Channel channel) {
        USER_CHANNEL_MAP.put(userId, channel);
    }

    public static Channel getUserChannel(String userId) {
        return USER_CHANNEL_MAP.get(userId);
    }

    /**
     * 推送消息给用户
     * @return
     */
    public static void sendMsg(UserSendMsgReqVO userSendMsgReqVO) {
        Channel channel = getUserChannel(userSendMsgReqVO.getReceiveId());
        if (channel == null) {
            return;
        }
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJSONString(userSendMsgReqVO)));
    }
}

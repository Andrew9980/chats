package com.andrew.chats.netty;

import com.andrew.chats.common.utils.JSONUtil;
import com.andrew.chats.common.params.UserSendMsgParam;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketContext {

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
     */
    public static void sendMsg(UserSendMsgParam userSendMsgParam) {
        Channel channel = getUserChannel(userSendMsgParam.getReceiveId());
        if (channel == null) {
            return;
        }
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJSONString(userSendMsgParam)));
    }
}

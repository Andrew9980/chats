package com.andrew.chats.netty;

import com.andrew.chats.config.ServiceException;
import com.andrew.chats.dao.model.UserContact;
import com.andrew.chats.service.UserContactService;
import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.common.utils.JSONUtil;
import com.andrew.chats.common.utils.SecretUtil;
import com.andrew.chats.common.params.UserSendMsgParam;
import com.andrew.chats.common.base.RespResult;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static com.andrew.chats.enums.UserContactStatusEnum.*;

@Slf4j
@Component
@ChannelHandler.Sharable
public class TextWebSocketMessageHandle extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private UserContactService userContactService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("新连接接入");
        super.channelActive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete handshake) {
            // WebSocket连接握手完成事件后校验token信息是否正确
            HttpHeaders headers = handshake.requestHeaders();
            String token = headers.get("token");
            if (token == null) {
                ctx.channel().close();
            }

            try {
                String userId = SecretUtil.verify(token);
                WebSocketContext.addUser2Channel(userId, ctx.channel());
            } catch (ServiceException e) {
                // token验证失败返回
                ctx.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJSONString(Map.of("code", e.getCode(), "message", e.getDesc()))));
                ctx.channel().close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame text) throws Exception {
        String channelId = ctx.channel().id().asShortText();
        String userId = ctx.channel().attr(AttributeKey.valueOf(channelId)).get().toString();
        log.info("收到用户{}消息{}", userId, text.text());
        if (StringUtils.isEmpty(text.text())) {
            return;
        }
        UserSendMsgParam userSendMsgParam = JSONUtil.parseJSON(text.text(), UserSendMsgParam.class);
        if (userSendMsgParam != null) {
            RespResult<Boolean> respResult;
            UserContact userContact = userContactService.getUserContact(userSendMsgParam.getSenderId(), userSendMsgParam.getReceiveId());
            if (Objects.equals(userContact.getStatus(), BLACK.getCode())) { // 好友拉黑
                respResult = RespResult.fail(ExceptionEnum.USER_CONTACT_ERROR);
            } else if (Objects.equals(userContact.getStatus(), QUIT.getCode())) { // 不在群聊
                respResult = RespResult.fail(ExceptionEnum.GROUP_CONTACT_ERROR);
            } else {
                WebSocketContext.sendMsg(userSendMsgParam);
                respResult = RespResult.success();
            }
            // 发送成功
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJSONString(respResult)));
        }
    }
}

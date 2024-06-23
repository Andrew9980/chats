package com.andrew.chats.netty;

import com.andrew.chats.config.ServiceException;
import com.andrew.chats.dao.model.UserContact;
import com.andrew.chats.dao.service.UserContactService;
import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.enums.UserContactStatusEnum;
import com.andrew.chats.utils.util.JSONUtil;
import com.andrew.chats.utils.util.SecretUtil;
import com.andrew.chats.vo.UserSendMsgReqVO;
import com.andrew.chats.vo.base.RespResult;
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

@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageHandle extends SimpleChannelInboundHandler<TextWebSocketFrame> {

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
                NettyContext.addUser2Channel(userId, ctx.channel());
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
        UserSendMsgReqVO userSendMsgReqVO = JSONUtil.parseJSON(text.text(), UserSendMsgReqVO.class);
        if (userSendMsgReqVO != null) {
            RespResult respResult;
            UserContact userContact = userContactService.getUserContact(userSendMsgReqVO.getSenderId(), userSendMsgReqVO.getReceiveId());
            if (Objects.isNull(userContact) || !Objects.equals(userContact.getStatus(), UserContactStatusEnum.VALID.getCode())) {
                respResult = RespResult.fail(ExceptionEnum.USER_CONTACT_ERROR);
            } else {
                NettyContext.sendMsg(userSendMsgReqVO);
                respResult = RespResult.success();
            }
            // 发送成功
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJSONString(respResult)));
        }
    }
}

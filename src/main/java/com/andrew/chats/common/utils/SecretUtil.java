package com.andrew.chats.common.utils;

import com.andrew.chats.config.ServiceException;
import com.andrew.chats.enums.ExceptionEnum;
import com.andrew.chats.common.Constants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class SecretUtil {

    /**
     * md5加密
     * @param salt
     * @param text
     * @return
     */
    public static String encode(String salt, String text) {
        return DigestUtils.md5DigestAsHex((salt + text).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 判断输入是否正确
     * @param text
     * @param salt
     * @param originText
     * @return
     */
    public static boolean check(String text, String salt, String originText) {
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(salt)) {
            return false;
        }
        return encode(salt, text).equals(originText);
    }

    /**
     * 8位盐值
     * @return
     */
    public static String getSalt() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    /**
     * 生成token
     * @param userId
     * @return
     */
    public static String getToken(String userId) {
        return JWT.create()
                .withIssuedAt(new Date()) // 生成时间
                .withExpiresAt(new Date(System.currentTimeMillis() + Constants.TOKEN_EXPIRE)) // 过期时间
                .withClaim("userId", userId)
                .sign(Algorithm.HMAC256(Constants.SECRET_KEY));
    }

    /**
     * 校验token并取出信息
     * @param token
     * @return
     */
    public static String verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(Constants.SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("userId").asString();
        } catch (JWTVerificationException e) {
            throw new ServiceException(ExceptionEnum.TOKEN_ERROR);
        }
    }
}

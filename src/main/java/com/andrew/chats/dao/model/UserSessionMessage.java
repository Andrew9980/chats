package com.andrew.chats.dao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户联系人消息表
 * </p>
 *
 * @author andrew
 * @since 2024-06-28
 */
@TableName("user_session_message")
public class UserSessionMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送者
     */
    private String senderId;

    /**
     * 接收者
     */
    private String receiveId;

    /**
     * 消息id
     */
    private Long messageId;

    /**
     * 1：未读 2：已读
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UserSessionMessage{" +
            "id = " + id +
            ", senderId = " + senderId +
            ", receiveId = " + receiveId +
            ", messageId = " + messageId +
            ", status = " + status +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
        "}";
    }
}

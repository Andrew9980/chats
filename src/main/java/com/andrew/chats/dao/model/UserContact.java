package com.andrew.chats.dao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户联系
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@TableName("user_contact")
public class UserContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * user_info表user_id
     */
    private String userId;

    /**
     * 联系人id type=1 user_info表user_id type=2 group表group_id
     */
    private String contactId;

    /**
     * 联系类型，1：联系用户 2：联系群
     */
    private Integer type;

    /**
     * 联系状态，1：正常 2：拉黑
     */
    private Byte status;

    /**
     * 消息未读数
     */
    private Integer unreadCount;

    /**
     * 最后联系时间
     */
    private LocalDateTime lastContractTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public LocalDateTime getLastContractTime() {
        return lastContractTime;
    }

    public void setLastContractTime(LocalDateTime lastContractTime) {
        this.lastContractTime = lastContractTime;
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
        return "UserContact{" +
            "id = " + id +
            ", userId = " + userId +
            ", contactId = " + contactId +
            ", type = " + type +
            ", status = " + status +
            ", unreadCount = " + unreadCount +
            ", lastContractTime = " + lastContractTime +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
        "}";
    }
}

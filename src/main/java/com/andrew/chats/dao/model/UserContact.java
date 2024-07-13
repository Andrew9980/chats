package com.andrew.chats.dao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

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
@Data
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
     * 备注
     */
    private String notes;

    /**
     * 联系类型，1：联系用户 2：联系群
     */
    private Integer type;

    /**
     * 联系状态，1：正常 2：拉黑
     */
    private Integer status;

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

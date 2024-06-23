package com.andrew.chats.dao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 群成员
 * </p>
 *
 * @author andrew
 * @since 2024-06-23
 */
@TableName("group_member")
public class GroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群id
     */
    private String groupId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 群成员权限，1：群主 2：管理员 3：群员
     */
    private Integer userRole;

    /**
     * 加入时间
     */
    private LocalDateTime joinTime;

    /**
     * 状态，1：正常 2：禁言 3：被踢
     */
    private Integer status;

    /**
     * 禁言生效时间
     */
    private LocalDateTime mutedTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getMutedTime() {
        return mutedTime;
    }

    public void setMutedTime(LocalDateTime mutedTime) {
        this.mutedTime = mutedTime;
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
        return "GroupMember{" +
            "id = " + id +
            ", groupId = " + groupId +
            ", userId = " + userId +
            ", userRole = " + userRole +
            ", joinTime = " + joinTime +
            ", status = " + status +
            ", mutedTime = " + mutedTime +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
        "}";
    }
}

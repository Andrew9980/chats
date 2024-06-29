package com.andrew.chats.common.params;

import lombok.Data;

@Data
public class GroupParam {

    /**
     * 群id
     */
    private String groupId;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 群信息
     */
    private String groupInfo;

    /**
     * 群类型
     */
    private Integer type;

    /**
     * 是否可被搜索 0：不可搜索 1：可搜索
     */
    private Integer search;

    /**
     * 当前人数
     */
    private Integer curCount;

    /**
     * 最大群人数
     */
    private Integer maxCount;

    /**
     * 0：封禁，1：正常
     */
    private Integer status;

}

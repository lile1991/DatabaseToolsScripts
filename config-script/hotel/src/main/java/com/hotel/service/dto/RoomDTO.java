package com.hotel.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomDTO implements Serializable {
    
    /**
     * 房间ID
     */
    private Long id;
    
    /**
     * 房号
     */
    private Integer number;
    
    /**
     * 房间类型
     */
    private Integer type;
    
    /**
     * 楼层
     */
    private Integer floor;
    
    /**
     * 房号别名
     */
    private String alias;
    
    /**
     * 状态
     */
    private String state;
    
    /**
     * 房费
     */
    private Long charge;
    
    /**
     * 押金
     */
    private Long deposit;
    
    /**
     * 创建时间
     */
    private java.util.Date createTime;
    
    /**
     * 创建人
     */
    private Long createUserId;
    
    /**
     * 更新时间
     */
    private java.util.Date updateTime;
    
    /**
     * 更新人
     */
    private Long updateUserId;
    
}

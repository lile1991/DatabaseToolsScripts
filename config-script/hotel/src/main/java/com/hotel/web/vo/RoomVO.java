package com.hotel.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("房间")
public class RoomVO implements Serializable {
    
    @ApiModelProperty(value = "房间ID")
    private Long id;
    
    @ApiModelProperty(value = "房号")
    private Integer number;
    
    @ApiModelProperty(value = "房间类型")
    private Integer type;
    
    @ApiModelProperty(value = "楼层")
    private Integer floor;
    
    @ApiModelProperty(value = "房号别名")
    private String alias;
    
    @ApiModelProperty(value = "状态")
    private String state;
    
    @ApiModelProperty(value = "房费")
    private Long charge;
    
    @ApiModelProperty(value = "押金")
    private Long deposit;
    
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    
    @ApiModelProperty(value = "创建人")
    private Long createUserId;
    
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    
    @ApiModelProperty(value = "更新人")
    private Long updateUserId;
    
}

package com.zgt.opengauss.zeus.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangrb
 * @date 2021/7/6
 */
@Data
public class SysApplyVO {

    @ApiModelProperty(value = "申请人gitID")
    private String gitId;
    @ApiModelProperty(value = "申请人Git名")
    private String gitName;
    @ApiModelProperty(value = "申请理由")
    private String applyReason;

}

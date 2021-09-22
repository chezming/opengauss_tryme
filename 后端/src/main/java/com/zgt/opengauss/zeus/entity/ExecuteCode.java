package com.zgt.opengauss.zeus.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangrb
 * @date 2021/6/28
 */
@Data
public class ExecuteCode {

    @ApiModelProperty(value = "gitID", required = true)
    private String gitId;
    @ApiModelProperty(value = "执行语句", required = true)
    private String code;
    @ApiModelProperty(value = "执行的数据库名称")
    private String dbName;
    @ApiModelProperty(value = "是否是简单体验 0不是，1是")
    private Integer simple = 0;
}

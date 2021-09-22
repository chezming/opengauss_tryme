package com.zgt.opengauss.zeus.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author luowq
 * @date 2021/8/30
 */
@Data
public class QueryInfo {
    @ApiModelProperty(value="git用户id")
    private  String gitId;

    //0：数据库  1：模式  2：数据表 3：视图 4：函数
    @ApiModelProperty(value="查询类型")
    private int queryType;

    @ApiModelProperty(value="指定数据库名")
    private String datName;

    @ApiModelProperty(value="指定模式")
    private String schemaName;

}

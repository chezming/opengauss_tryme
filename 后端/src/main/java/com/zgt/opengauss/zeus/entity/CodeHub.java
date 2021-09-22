package com.zgt.opengauss.zeus.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luowq
 * @date 2021/7/6
 */
@Data
public class CodeHub {
    @ApiModelProperty("代码仓库id")
    private String id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("sql语句")
    private String sql;

    @ApiModelProperty("码云账户id")
    private String gitId;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("更新时间")
    private String updateTime;

    @ApiModelProperty("删除标记")
    private int delFlag;
}

package com.zgt.opengauss.zeus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangrb
 * @date 2021/8/30
 */

@Data
public class SysPort {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private String id;
    @ApiModelProperty(value = "端口号")
    private Integer port;
}

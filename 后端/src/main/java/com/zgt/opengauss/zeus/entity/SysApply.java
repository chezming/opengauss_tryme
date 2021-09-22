package com.zgt.opengauss.zeus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhangrb
 * @date 2021/7/6
 */
@Data
public class SysApply {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    @ApiModelProperty(value = "申请人git ID")
    private String gitId;
    @ApiModelProperty(value = "申请人Git名")
    private String gitName;
    @ApiModelProperty(value = "申请理由")
    private String applyReason;
    @ApiModelProperty(value = "拒绝理由")
    private String rejectReason;
    @ApiModelProperty(value = "审批状态")
    private String status;
    @ApiModelProperty(value = "有效时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validTime;
    @ApiModelProperty(value = "创建时间")
    private String createTime;

}

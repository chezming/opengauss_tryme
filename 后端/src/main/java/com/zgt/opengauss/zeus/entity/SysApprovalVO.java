package com.zgt.opengauss.zeus.entity;

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
public class SysApprovalVO {

    @ApiModelProperty(value = "申请单号", required = true)
    private String id;
    @ApiModelProperty(value = "申请人git ID", required = true)
    private String gitId;
    @ApiModelProperty(value = "拒绝理由")
    private String rejectReason;
    @ApiModelProperty(value = "审批状态 审核通过 1；驳回 2", required = true)
    private String status;
    @ApiModelProperty(value = "有效日期 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validTime;

}

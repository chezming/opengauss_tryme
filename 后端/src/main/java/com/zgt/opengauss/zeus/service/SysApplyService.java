package com.zgt.opengauss.zeus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgt.opengauss.zeus.entity.*;

/**
 * @author zhangrb
 * @date 2021/6/29
 */
public interface SysApplyService extends IService<SysApply> {

    void saveApply(SysApplyVO applyVO);

    Result<Object> approval(SysApprovalVO approvalVO);
}

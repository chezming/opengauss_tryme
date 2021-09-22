package com.zgt.opengauss.zeus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgt.opengauss.zeus.entity.Result;
import com.zgt.opengauss.zeus.entity.SysApply;
import com.zgt.opengauss.zeus.entity.SysApplyVO;
import com.zgt.opengauss.zeus.entity.SysApprovalVO;
import com.zgt.opengauss.zeus.mapper.SysApplyMapper;
import com.zgt.opengauss.zeus.service.SysApplyService;
import com.zgt.opengauss.zeus.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangrb
 * @date 2021/6/29
 */
@Slf4j
@Service
public class SysApplyServiceImpl extends ServiceImpl<SysApplyMapper, SysApply> implements SysApplyService {


    @Autowired
    private SysUserService sysUserService;

    @Override
    public void saveApply(SysApplyVO applyVO) {

        SysApply sysApply = new SysApply();
        BeanUtils.copyProperties(applyVO,sysApply);
        sysApply.setStatus("0");
        baseMapper.insert(sysApply);

    }

    @Override
    public Result<Object> approval(SysApprovalVO approvalVO) {
        SysApply sysApply = new SysApply();
        BeanUtils.copyProperties(approvalVO,sysApply);
        try {
            baseMapper.updateById(sysApply);
            //更新用户信息
            if ("1".equals(sysApply.getStatus())) {
                int i = sysUserService.updateUserRole(approvalVO.getGitId(),"1",approvalVO.getValidTime());
                if (i > 0)
                    return Result.OK();
                return Result.error("更新用户角色失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.OK();
    }
}

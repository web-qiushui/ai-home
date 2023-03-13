package com.home.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;
import com.home.modules.generator.dao.ReportDao;
import com.home.modules.generator.dto.ReportDTO;
import com.home.modules.generator.entity.ReportEntity;
import com.home.modules.generator.service.ReportService;
import com.home.modules.generator.vo.ReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("reportService")
public class ReportServiceImpl extends ServiceImpl<ReportDao, ReportEntity> implements ReportService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ReportEntity> page = this.page(
                new Query<ReportEntity>().getPage(params),
                new QueryWrapper<ReportEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public JsonResult saveHomeReport(ReportDTO dto) {
        ReportEntity reportEntity;
        if (dto.getId() != null) {
            reportEntity = baseMapper.selectById(dto.getId());
            if (reportEntity == null) {
                return JsonResult.fail("数据为空");
            }
        } else {
            reportEntity = new ReportEntity();
        }
        BeanUtils.copyProperties(dto, reportEntity);
        this.saveOrUpdate(reportEntity);
        log.info("新增或修改购房报告成功，id：{}", dto.getId());
        return JsonResult.success();
    }

    @Override
    public PageBean<ReportVO> getHomeReportList(ReportDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<ReportVO> demandList = baseMapper.getHomeReportList(dto);
        PageBean<ReportVO> pageBean = new PageBean<>(demandList);
        return pageBean;
    }

}
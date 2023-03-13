package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.dto.ReportDTO;
import com.home.modules.generator.entity.ReportEntity;
import com.home.modules.generator.vo.ReportVO;

import java.util.Map;

/**
 * 购房报告表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-07-25 13:53:42
 */
public interface ReportService extends IService<ReportEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 新增或修改购房报告
     */
    JsonResult saveHomeReport(ReportDTO dto);

    /**
     * 查询购房报告列表
     */
    PageBean<ReportVO> getHomeReportList(ReportDTO dto);
}


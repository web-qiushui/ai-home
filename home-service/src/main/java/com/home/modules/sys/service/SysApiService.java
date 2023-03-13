package com.home.modules.sys.service;

import com.home.common.utils.JsonResult;
import com.home.modules.generator.dto.*;
import com.home.modules.generator.vo.OpeningNoticeVO;
import com.home.modules.generator.vo.StatisticslVO;

/**
 * Web对外接口
 *
 * @author xiewei 1048516133@qq.com
 */
public interface SysApiService {

    /**
     * 测评评论审核
     */
    JsonResult auditEvaluationComment(EvaluationCommentDTO dto, Integer sysUserId);

    /**
     * 修改或新增轮播图
     */
    JsonResult saveCarousel(SaveCarouselVO dto);

    /**
     * 顾问审核审核
     */
    JsonResult auditConsultant(AuditConsultantDTO dto);

    /**
     * 新增AI顾问
     */
    JsonResult saveConsultant(SaveConsultantDTO dto, Long sysUserId);

    /**
     * 修改或新增开盘预告
     */
    JsonResult saveOpeningNotice(OpeningNoticeVO dto);

    /**
     * 修改信息
     */
    JsonResult saveDemand(DemandDTO dto);

    /**
     * 查询统计数据
     * @param type 类型 day/month/year 日、月、年
     */
    StatisticslVO getStatistics(String type);


}

package com.home.modules.app.service;

import com.home.common.constant.config.ChatCardConfig;
import com.home.common.utils.JsonResult;
import com.home.modules.generator.dto.*;
import com.home.modules.generator.vo.ConsultantVO;
import com.home.modules.generator.vo.DemandVO;
import com.home.modules.generator.vo.MineInfoVO;

import java.util.List;

/**
 * App对外接口
 *
 * @author xiewei 1048516133@qq.com
 */
public interface AppApiService {

    /**
     * 发布信息
     */
    JsonResult<DemandVO> saveDemand(DemandDTO dto);

    /**
     * 新增置业顾问
     */
    JsonResult saveConsultant(SaveConsultantDTO dto);

    /**
     * 顾问评价
     */
    JsonResult saveConsultantComment(SaveConsultantCommentDTO dto);

    /**
     * 测评评论
     */
    JsonResult saveEvaluationComment(SaveEvaluationCommentDTO dto);

    /**
     * 测评转发
     */
    JsonResult saveEvaluationForward(SaveEvaluationForwardDTO dto);

    /**
     * 测评收藏
     */
    JsonResult saveEvaluatioCollect(SaveEvaluatioCollectDTO dto);

    /**
     * 测评点赞
     */
    JsonResult saveEvaluationLike(SaveEvaluationLikeDTO dto);

    /**
     * 测评评论点赞
     */
    JsonResult saveEvaluationCommentLike(SaveEvaluationCommentLikeDTO dto);

    /**
     * 测评评论点赞
     */
    MineInfoVO getMineInfo(Integer userId);

    /**
     * 查询聊天卡片
     */
    List<ChatCardConfig> getChatCard(Integer userId);

    /**
     * 查询AI顾问
     */
    ConsultantVO getAiConsultant(Integer evaluationId, Integer userId);

}

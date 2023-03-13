package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageReq;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.dto.EvaluationCommentDTO;
import com.home.modules.generator.entity.EvaluationCommentEntity;
import com.home.modules.generator.vo.EvaluationCommentVO;

import java.util.Map;

/**
 * 测评评论表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
public interface EvaluationCommentService extends IService<EvaluationCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询我的评论列表
     */
    PageBean<EvaluationCommentVO> getEvaluationCommentList(EvaluationCommentDTO dto);

    /**
     * 查询评论列表
     */
    PageBean<EvaluationCommentVO> getEvaluationComment(EvaluationCommentDTO dto);

}


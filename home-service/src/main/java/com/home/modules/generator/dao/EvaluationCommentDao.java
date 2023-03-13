package com.home.modules.generator.dao;

import com.home.common.utils.PageBean;
import com.home.modules.generator.dto.EvaluationCommentDTO;
import com.home.modules.generator.entity.EvaluationCommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.generator.vo.EvaluationCommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 测评评论表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Mapper
public interface EvaluationCommentDao extends BaseMapper<EvaluationCommentEntity> {

    /**
     * 查询我的评论列表
     */
    List<EvaluationCommentVO> getEvaluationCommentList(EvaluationCommentDTO dto);

    /**
     * 查询评论列表
     */
    List<EvaluationCommentVO> getEvaluationComment(EvaluationCommentDTO dto);
	
}

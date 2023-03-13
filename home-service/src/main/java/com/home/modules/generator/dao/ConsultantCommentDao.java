package com.home.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.common.utils.PageReq;
import com.home.modules.generator.dto.ConsultantCommentDTO;
import com.home.modules.generator.entity.ConsultantCommentEntity;
import com.home.modules.generator.vo.ConsultantCommentVO;
import com.home.modules.generator.vo.StarAndCountVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 顾问评论表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Mapper
public interface ConsultantCommentDao extends BaseMapper<ConsultantCommentEntity> {


    /**
     * 查询我的评价列表
     */
    List<ConsultantCommentVO> getConsultantCommentList(ConsultantCommentDTO dto);

    /**
     * 查询评价列表
     */
    List<ConsultantCommentVO> getConsultantComment(Integer consultantId);

    /**
     * 查询顾问评价列表
     */
    StarAndCountVO getStarAndCount(Integer consultentId);
	
}

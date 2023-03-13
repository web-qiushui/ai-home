package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.dto.ConsultantCommentDTO;
import com.home.modules.generator.entity.ConsultantCommentEntity;
import com.home.modules.generator.vo.ConsultantCommentVO;
import com.home.modules.generator.vo.StarAndCountVO;

import java.util.Map;

/**
 * 顾问评论表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
public interface ConsultantCommentService extends IService<ConsultantCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询我的评价列表
     */
    PageBean<ConsultantCommentVO> getConsultantCommentList(ConsultantCommentDTO dto);

    /**
     * 查询顾问评价列表
     */
    PageBean<ConsultantCommentVO> getConsultantComment(ConsultantCommentDTO dto);

    /**
     * 查询顾问评价列表
     */
    StarAndCountVO getStarAndCount(Integer consultentId);
}


package com.home.modules.generator.dao;

import com.home.modules.generator.dto.EvaluationDTO;
import com.home.modules.generator.entity.EvaluationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.generator.vo.EvaluationListVO;
import com.home.modules.generator.vo.EvaluationVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 测评表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:13
 */
@Mapper
public interface EvaluationDao extends BaseMapper<EvaluationEntity> {

    /**
     * 查询首页测评列表
     */
    List<EvaluationListVO>  getMainPageInfo(EvaluationDTO dto);

    /**
     * 查询测评列表
     * @param dto
     * @return
     */
    List<EvaluationVO>  getEvaluationList(EvaluationDTO dto);

    /**
     * 查询24h快讯
     */
    List<EvaluationListVO>  get24hNews(EvaluationDTO dto);

    /**
     * 根据Id查询推荐测评
     */
    List<EvaluationListVO>  getRecommendList(EvaluationDTO dto);
}

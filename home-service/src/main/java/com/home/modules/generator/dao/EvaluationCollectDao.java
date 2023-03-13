package com.home.modules.generator.dao;

import com.home.modules.generator.entity.EvaluationCollectEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.generator.vo.EvaluationVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 测评收藏表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@Mapper
public interface EvaluationCollectDao extends BaseMapper<EvaluationCollectEntity> {

    List<EvaluationVO> getEvaluationCollectList(Integer userId);
	
}

package com.home.modules.generator.dao;

import com.home.modules.generator.entity.EvaluationForwardEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.generator.vo.EvaluationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测评转发表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-18 23:57:55
 */
@Mapper
public interface EvaluationForwardDao extends BaseMapper<EvaluationForwardEntity> {


    List<EvaluationVO> getEvaluationForwardList(@Param("search") String search, @Param("userId") Integer userId);
	
}

package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageReq;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.entity.EvaluationCollectEntity;
import com.home.modules.generator.vo.EvaluationVO;

import java.util.Map;

/**
 * 测评收藏表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
public interface EvaluationCollectService extends IService<EvaluationCollectEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询测评关注列表
     */
    PageBean<EvaluationVO> getEvaluationCollectList(PageReq page, Integer userId);

    /**
     * 查询测评关注数量
     */
    Integer getEvaluationCollectSize(Integer userId);
}


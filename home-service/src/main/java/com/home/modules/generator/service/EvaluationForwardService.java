package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageReq;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.dto.EvaluationForwardDTO;
import com.home.modules.generator.entity.EvaluationForwardEntity;
import com.home.modules.generator.vo.EvaluationVO;

import java.util.Map;

/**
 * 测评转发表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-18 23:57:55
 */
public interface EvaluationForwardService extends IService<EvaluationForwardEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询测评转发列表
     */
    PageBean<EvaluationVO> getEvaluationForwardList(EvaluationForwardDTO dto, Integer userId);
}


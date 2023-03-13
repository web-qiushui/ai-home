package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.dto.EvaluationDTO;
import com.home.modules.generator.dto.SaveEvaluationDTO;
import com.home.modules.generator.entity.EvaluationEntity;
import com.home.modules.generator.vo.EvaluationListVO;
import com.home.modules.generator.vo.EvaluationVO;

import java.util.List;
import java.util.Map;

/**
 * 测评表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:13
 */
public interface EvaluationService extends IService<EvaluationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取测评列表
     */
    PageBean<EvaluationVO> getEvaluationList(EvaluationDTO dto);

    /**
     * 新增或修改测评
     */
    JsonResult saveEvaluation(SaveEvaluationDTO dto);

    /**
     * 根据Id查询测评报告
     */
    EvaluationVO getEvaluationInfo(Integer id, Integer userId);

    /**
     * 根据Id查询推荐测评
     */
    List<EvaluationListVO> getRecommendList(Integer id);

    /**
     * 获取首页测评数据
     */
    PageBean<EvaluationListVO> getMainPageInfo(EvaluationDTO dto);

    /**
     * 获取金刚区列表
     */
    List<EvaluationListVO> getKingKongList(String type);

    /**
     * 获取24h快讯列表
     */
    PageBean<EvaluationListVO> get24hNews(EvaluationDTO dto);


}


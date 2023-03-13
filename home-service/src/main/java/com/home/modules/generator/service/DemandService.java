package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageReq;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.dto.DemandDTO;
import com.home.modules.generator.entity.DemandEntity;
import com.home.modules.generator.vo.DemandVO;

import java.util.List;
import java.util.Map;

/**
 * 购房需求表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
public interface DemandService extends IService<DemandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     *  查询用户购房需求列表
     */
    PageBean<DemandVO> getMyPublishByUserId (PageReq page, Integer userId);

    /**
     * 查询购房需求列表
     * @param dto
     * @return
     */
    PageBean<DemandVO> getDemandList (DemandDTO dto);

    /**
     *  查询购房需求提交数量
     */
    Integer getDemandSize ();

}


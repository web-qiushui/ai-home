package com.home.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.dto.SinglePageDTO;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageUtils;
import com.home.common.vo.SinglePageVO;
import com.home.modules.sys.entity.SysSinglePageEntity;

import java.util.Map;

/**
 * 系统单页面表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-28 22:14:45
 */
public interface SysSinglePageService extends IService<SysSinglePageEntity> {

    /**
     * 根据编码查询单页面数据
     * @param code
     * @return
     */
    SinglePageVO getSinglePageByCode(String code);

    /**
     * 查询静态单页面列表
     * @return
     */
    PageBean<SinglePageVO> getSinglePageList(SinglePageDTO dto);

    /**
     * 修改或新增单页面数据
     * @return
     */
    JsonResult saveSinglePage(SinglePageVO vo);
}


package com.home.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageReq;
import com.home.common.utils.PageUtils;
import com.home.modules.generator.entity.OpeningNoticeEntity;
import com.home.modules.generator.vo.OpeningNoticeVO;

import java.util.Map;

/**
 * 开盘预告表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
public interface OpeningNoticeService extends IService<OpeningNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询开盘预告
     */
    OpeningNoticeVO getOpeningNotice();

    /**
     * 查询开盘预告列表
     */
    PageBean<OpeningNoticeVO> getOpeningNoticeList(PageReq pageReq);

    /**
     * 修改开盘预售状态
     */
    JsonResult updateOpeningNoticeStatus();
}


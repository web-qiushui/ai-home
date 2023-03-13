package com.home.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.common.utils.Constant;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageUtils;
import com.home.common.dto.DatadictDTO;
import com.home.modules.sys.entity.SysDatadictEntity;
import com.home.common.vo.DatadictVO;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2019-12-13 17:39:07
 */
public interface SysDatadictService extends IService<SysDatadictEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /**
     * 根据type获取数据字典
     */
    DatadictVO getDatadict(String type);
    /**
     * 获取数据字典
     */
    List<DatadictVO> getDatadict();

    /**
     * 获取数据字典
     */
    PageBean<DatadictVO> getDatadictList(DatadictDTO dto);

    /**
     * 新增或修改数据字典
     */
    JsonResult saveDatadict(DatadictVO dto, Integer userId);

}


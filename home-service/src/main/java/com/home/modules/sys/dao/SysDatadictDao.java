package com.home.modules.sys.dao;

import com.home.common.dto.DatadictDTO;
import com.home.modules.sys.entity.SysDatadictEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.common.vo.DatadictVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数据字典
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2019-12-13 17:39:07
 */
@Mapper
public interface SysDatadictDao extends BaseMapper<SysDatadictEntity> {

    /**
     * 获取所有已开启的数据字典
     */
    List<DatadictVO> getDatadict();

    /**
     * 获取数据字典
     */
    List<DatadictVO> getDatadictList(DatadictDTO dto);

}

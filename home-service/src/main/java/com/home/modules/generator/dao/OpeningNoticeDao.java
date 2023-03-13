package com.home.modules.generator.dao;

import com.home.modules.generator.entity.OpeningNoticeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.modules.generator.vo.OpeningNoticeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 开盘预告表
 * 
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-22 20:41:42
 */
@Mapper
public interface OpeningNoticeDao extends BaseMapper<OpeningNoticeEntity> {

    List<OpeningNoticeVO> getOpeningNoticeList();

    int updateOpeningNoticeStatus();

}

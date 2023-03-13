package com.home.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.home.common.dto.UserDTO;
import com.home.common.vo.BeanVO;
import com.home.common.vo.UserVO;
import com.home.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户
 *
 * @author xw
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    List<UserVO> getUserList(UserDTO dto);

    /**
     * 获取用户列表
     */
    List<BeanVO> getUserBeanList();

}

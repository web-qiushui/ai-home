package com.home.modules.generator.service.impl;

import com.home.modules.generator.vo.LoginStatisticslVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;

import com.home.modules.generator.dao.LoginDao;
import com.home.modules.generator.entity.LoginEntity;
import com.home.modules.generator.service.LoginService;

@Slf4j
@Service("loginService")
public class LoginServiceImpl extends ServiceImpl<LoginDao, LoginEntity> implements LoginService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LoginEntity> page = this.page(
                new Query<LoginEntity>().getPage(params),
                new QueryWrapper<LoginEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public LoginStatisticslVO getLoginStatistics() {
        Integer day = baseMapper.getLoginStatistics("day");
        Integer month = baseMapper.getLoginStatistics("month");
        Integer total = baseMapper.getLoginStatistics(null);
        LoginStatisticslVO vo = new LoginStatisticslVO();
        vo.setDayData(day == null ? 0 : day);
        vo.setMonthData(month == null ? 0 : month);
        vo.setTotalData(total == null ? 0 : total);
        return vo;
    }

}
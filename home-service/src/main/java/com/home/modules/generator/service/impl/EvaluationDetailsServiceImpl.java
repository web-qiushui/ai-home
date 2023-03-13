package com.home.modules.generator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;

import com.home.modules.generator.dao.EvaluationDetailsDao;
import com.home.modules.generator.entity.EvaluationDetailsEntity;
import com.home.modules.generator.service.EvaluationDetailsService;

@Slf4j
@Service("evaluationDetailsService")
public class EvaluationDetailsServiceImpl extends ServiceImpl<EvaluationDetailsDao, EvaluationDetailsEntity> implements EvaluationDetailsService {


}
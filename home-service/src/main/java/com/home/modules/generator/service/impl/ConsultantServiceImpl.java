package com.home.modules.generator.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.home.common.exception.RRException;
import com.home.common.utils.Constant;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageUtils;
import com.home.common.utils.Query;
import com.home.modules.generator.dao.ConsultantDao;
import com.home.modules.generator.dto.ConsultantDTO;
import com.home.modules.generator.dto.ConsultantUserDTO;
import com.home.modules.generator.entity.ConsultantAuditEntity;
import com.home.modules.generator.entity.ConsultantEntity;
import com.home.modules.generator.service.ConsultantAuditService;
import com.home.modules.generator.service.ConsultantService;
import com.home.modules.generator.vo.ConsultantVO;
import com.home.modules.generator.vo.FirmCountVO;
import com.home.modules.sys.entity.SysUserEntity;
import com.home.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("consultantService")
public class ConsultantServiceImpl extends ServiceImpl<ConsultantDao, ConsultantEntity> implements ConsultantService {

    @Resource
    private ConsultantAuditService consultantAuditService;
    @Resource
    private SysUserService sysUserService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ConsultantEntity> page = this.page(
                new Query<ConsultantEntity>().getPage(params),
                new QueryWrapper<ConsultantEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageBean<ConsultantVO> getConsultantList(ConsultantUserDTO dto) {
        LambdaQueryWrapper<ConsultantEntity> lambdaQueryWrapper = Wrappers.<ConsultantEntity>lambdaQuery()
                .eq(ConsultantEntity::getHomeUserId, dto.getUserId())
                .eq(ConsultantEntity::getStatus, Constant.ConsultantStatus.SUCCESS)
                .eq(ConsultantEntity::getType, Constant.AppRole.AI);
        ConsultantEntity consultantEntity = baseMapper.selectOne(lambdaQueryWrapper);
        if (consultantEntity == null) {
            throw new RRException("权限不足");
        }
        List<FirmCountVO> list = baseMapper.getFirmServerCountList(dto);
        List<Integer> firms = new ArrayList();
        if(list != null && list.size() > 0) {
            List<Integer> firmIds = list.stream().map(FirmCountVO::getFirmId).collect(Collectors.toList());
            List<SysUserEntity> sysUserEntities = sysUserService.listByIds(firmIds);
            Map<Long, SysUserEntity> userEntityMap = sysUserEntities.stream().collect(Collectors.toMap(SysUserEntity::getUserId, member -> member));
            for (FirmCountVO vo : list) {
                SysUserEntity sysUserEntity = userEntityMap.get(vo.getFirmId().longValue());
                if (vo.getNum() != null && vo.getNum() > sysUserEntity.getServiceNumber()) {
                    firms.add(vo.getFirmId());
                }
            }
        }
        if (firms.size() > 0) {
            dto.setFirmIds(firms);
        }
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<ConsultantVO> consultantList = baseMapper.getHomeConsultantList(dto);
        PageBean<ConsultantVO> pageBean = new PageBean<>(consultantList);
        return pageBean;
    }

    @Override
    public PageBean<ConsultantVO> getConsultantList(ConsultantDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        dto.setOrderBy(dto.getDesc(), dto.getAsc());
        List<ConsultantVO> consultantList = baseMapper.getConsultantList(dto);
        PageBean<ConsultantVO> pageBean = new PageBean<>(consultantList);
        consultantList.forEach(vo -> {
            if (Constant.ConsultantStatus.AUDIT.equals(vo.getStatus())) {
                LambdaQueryWrapper<ConsultantAuditEntity> queryWrapper = Wrappers.<ConsultantAuditEntity>lambdaQuery()
                        .eq(ConsultantAuditEntity::getHomeConsultantId, vo.getId())
                        .orderByDesc(ConsultantAuditEntity::getCreateTime)
                        .last(" LIMIT 1");
                ConsultantAuditEntity consultantAuditEntity = consultantAuditService.getOne(queryWrapper);
                if (consultantAuditEntity != null) {
                    vo.setAuditType(consultantAuditEntity.getType());
                }
            }
        });
        pageBean.setList(consultantList);
        return pageBean;
    }

    @Override
    public PageBean<ConsultantVO> getConsultantUserList(ConsultantUserDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<ConsultantVO> consultantList = baseMapper.getConsultantUserList(dto.getUserId(), dto.getType());
        PageBean<ConsultantVO> pageBean = new PageBean<>(consultantList);
        return pageBean;
    }

    @Override
    public ConsultantVO getConsultantInfo(Integer id) {
        ConsultantEntity consultantEntity = baseMapper.selectById(id);
        if (consultantEntity == null) {
            return null;
        }
        ConsultantVO vo = new ConsultantVO();
        BeanUtils.copyProperties(consultantEntity, vo);
        vo.setUserId(consultantEntity.getHomeUserId());
        if (StringUtils.isNotBlank(consultantEntity.getProject())) {
            vo.setProject(JSON.parseObject(consultantEntity.getProject(), ConsultantVO.Project.class));
        }
        return vo;
    }

    @Override
    public ConsultantVO getConsultantByUserId(Integer userId) {
        LambdaQueryWrapper<ConsultantEntity> lambdaQueryWrapper = Wrappers.<ConsultantEntity>lambdaQuery()
                .eq(ConsultantEntity::getHomeUserId, userId);
        ConsultantEntity consultantEntity = baseMapper.selectOne(lambdaQueryWrapper);
        if (consultantEntity == null) {
            return null;
        }
        ConsultantVO vo = new ConsultantVO();
        BeanUtils.copyProperties(consultantEntity, vo);
        vo.setUserId(consultantEntity.getHomeUserId());
        if (StringUtils.isNotBlank(consultantEntity.getProject())) {
            vo.setProject(JSON.parseObject(consultantEntity.getProject(), ConsultantVO.Project.class));
        }
        return vo;
    }

    @Override
    public ConsultantVO getConsultantByUserIds(Set<Integer> userIds, Integer userId, Integer evaluationUserId) {
        return baseMapper.getConsultantByUserIds(userIds, userId, evaluationUserId);
    }

}
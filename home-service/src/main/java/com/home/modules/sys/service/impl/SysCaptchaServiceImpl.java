package com.home.modules.sys.service.impl;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.code.kaptcha.Producer;
import com.home.common.dto.CaptchaDTO;
import com.home.common.exception.RRException;
import com.home.common.utils.Constant;
import com.home.common.utils.DateUtils;
import com.home.common.utils.JsonResult;
import com.home.common.utils.SmsUtils;
import com.home.modules.sys.dao.SysCaptchaDao;
import com.home.modules.sys.entity.SysCaptchaEntity;
import com.home.modules.sys.service.SysCaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * 验证码
 *
 * @author xw
 */
@Slf4j
@Service("sysCaptchaService")
public class SysCaptchaServiceImpl extends ServiceImpl<SysCaptchaDao, SysCaptchaEntity> implements SysCaptchaService {
    @Autowired
    private Producer producer;

    @Override
    public BufferedImage getCaptcha(String uuid) {
        if(StringUtils.isBlank(uuid)){
            throw new RRException("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();

        SysCaptchaEntity captchaEntity = new SysCaptchaEntity();
        captchaEntity.setUuid(uuid);
        captchaEntity.setCode(code);
        captchaEntity.setType(Constant.CaptchaType.GENERAL);
        captchaEntity.setNum(0);
        //5分钟后过期
        captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        this.save(captchaEntity);

        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        SysCaptchaEntity captchaEntity = this.getOne(new QueryWrapper<SysCaptchaEntity>().eq("uuid", uuid));
        if(captchaEntity == null){
            return false;
        }

        //删除验证码
        this.removeById(uuid);

        if(captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime().getTime() >= System.currentTimeMillis()){
            return true;
        }

        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult sendCaptcha(CaptchaDTO dto) {
        Integer type;
        switch (dto.getType()) {
            case 0:
                type = Constant.CaptchaType.GENERAL;
                break;
            case 1:
                type = Constant.CaptchaType.REGISTERED;
                break;
            case 2:
                type = Constant.CaptchaType.LOGIN;
                break;
            case 3:
                type = Constant.CaptchaType.RECOVER_PASSWORD;
                break;
            default:
                return JsonResult.fail("类型错误");
        }

        LambdaQueryWrapper<SysCaptchaEntity> wrapper = Wrappers.<SysCaptchaEntity>lambdaQuery()
                .eq(SysCaptchaEntity::getPhone, dto.getPhone())
                .eq(SysCaptchaEntity::getType, type)
                .likeLeft(SysCaptchaEntity::getExpireTime, DateUtils.format(new Date()));
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 10) {
            return JsonResult.fail("短信发送次数达到上线，请联系管理员");
        }
        LambdaQueryWrapper<SysCaptchaEntity> queryWrapper = Wrappers.<SysCaptchaEntity>lambdaQuery()
                .eq(SysCaptchaEntity::getPhone, dto.getPhone())
                .eq(SysCaptchaEntity::getType, type)
                .orderByDesc(SysCaptchaEntity::getExpireTime)
                .last(" LIMIT 1");
        SysCaptchaEntity sysCaptchaEntity = baseMapper.selectOne(queryWrapper);
        String code = RandomUtil.randomNumbers(6);
        SysCaptchaEntity captchaEntity = new SysCaptchaEntity();
        captchaEntity.setUuid(dto.getUuid());
        captchaEntity.setPhone(dto.getPhone());
        captchaEntity.setCode(code);
        captchaEntity.setType(type);
        captchaEntity.setNum(0);
        //5分钟后过期
        captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        if (sysCaptchaEntity != null) {
            long between = DateUtil.between(captchaEntity.getExpireTime(), sysCaptchaEntity.getExpireTime(), DateUnit.MINUTE);
            if (between < 1) {
                return JsonResult.fail("短信发送间隔1分钟, 请稍后再试！");
            }
        }
        baseMapper.insert(captchaEntity);
        // 发送短信
        SmsUtils.SendSms(dto.getPhone(), code);
        log.info("手机号为：{}，的短信验证码为：{}", dto.getPhone(), code);
        return JsonResult.success();
    }

    @Override
    public JsonResult checkCaptcha(CaptchaDTO dto) {
        SysCaptchaEntity captchaEntity = baseMapper.selectById(dto.getUuid());
        if(captchaEntity == null){
            return JsonResult.fail("uuid错误！");
        }
        if (!dto.getPhone().equals(captchaEntity.getPhone())) {
            return JsonResult.fail("手机号错误！");
        }
        if (captchaEntity.getNum() > 5) {
            return JsonResult.fail("校验次数达到上限，请重新获取");
        }
        if (captchaEntity.getExpireTime().getTime() < System.currentTimeMillis()){
            return JsonResult.fail("验证码失效！");
        }
        if(!captchaEntity.getCode().equals(dto.getCode())) {
            return JsonResult.fail("验证码错误！");
        }
        captchaEntity.setNum(captchaEntity.getNum() + 1);
        baseMapper.updateById(captchaEntity);
        return JsonResult.success();
    }
}

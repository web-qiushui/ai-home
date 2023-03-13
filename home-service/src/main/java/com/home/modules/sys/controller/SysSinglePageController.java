package com.home.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.home.common.annotation.NoRepeatSubmit;
import com.home.common.annotation.SysLog;
import com.home.common.dto.SinglePageDTO;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.vo.SinglePageVO;
import com.home.modules.sys.entity.SysSinglePageEntity;
import com.home.modules.sys.service.SysSinglePageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 系统单页面表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-28 22:14:45
 */
@Slf4j
@Api(value = "WEB接口", tags = {"WEB相关接口"})
@RestController
@RequestMapping("/sys/singlePage")
public class SysSinglePageController {

    @Resource
    private SysSinglePageService sysSinglePageService;

    @NoRepeatSubmit
    @ApiOperation("删除系统单页面表")
    @SysLog("删除系统单页面表")
    @GetMapping("/deleteSinglePage")
    public JsonResult deleteSinglePage(@RequestParam List<Integer> ids){
        sysSinglePageService.removeByIds(ids);
        log.info("删除系统单页面表，Id列表为:{}", JSON.toJSONString(ids));
        return JsonResult.success();
    }

    @NoRepeatSubmit
    @ApiOperation("修改或新增单页面数据")
    @PostMapping("/saveSinglePage")
    public JsonResult saveSinglePage(@Valid @RequestBody SinglePageVO vo){
        return sysSinglePageService.saveSinglePage(vo);
    }

    @ApiOperation("根据编码查询单页面数据")
    @GetMapping("/anon/getSinglePageByCode")
    public JsonResult<SinglePageVO> getSinglePageByCode(@RequestParam String code){
        SinglePageVO singlePageByCode = sysSinglePageService.getSinglePageByCode(code);
        return JsonResult.success(singlePageByCode);
    }

    @ApiOperation("查询静态单页面列表")
    @GetMapping("/getSinglePageList")
    public JsonResult<PageBean<SinglePageVO>> getSinglePageList(SinglePageDTO dto){
        PageBean<SinglePageVO> singlePageList = sysSinglePageService.getSinglePageList(dto);
        return JsonResult.success(singlePageList);
    }

    @ApiOperation("根据Id查询单页面数据")
    @GetMapping("/getSinglePageInfo")
    public JsonResult<SinglePageVO> getSinglePageInfo(@RequestParam Integer id){
        SysSinglePageEntity singlePageEntity = sysSinglePageService.getById(id);
        if (singlePageEntity == null) {
            return JsonResult.fail("Id错误");
        }
        SinglePageVO vo = new SinglePageVO();
        BeanUtils.copyProperties(singlePageEntity, vo);
        return JsonResult.success(vo);
    }

}

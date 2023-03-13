package com.home.modules.generator.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.home.modules.generator.entity.ConsultantEntity;
import com.home.modules.generator.service.ConsultantService;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.annotation.SysLog;



/**
 * 顾问表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@RestController
@RequestMapping("generator/consultant")
public class ConsultantController {
    @Autowired
    private ConsultantService consultantService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("generator:consultant:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = consultantService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("generator:consultant:info")
    public R info(@PathVariable("id") Integer id){
		ConsultantEntity consultant = consultantService.getById(id);

        return R.ok().put("consultant", consultant);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("保存顾问表")
    @RequiresPermissions("generator:consultant:save")
    public R save(@RequestBody ConsultantEntity consultant){
		consultantService.save(consultant);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改顾问表")
    @RequiresPermissions("generator:consultant:update")
    public R update(@RequestBody ConsultantEntity consultant){
		consultantService.updateById(consultant);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @SysLog("删除顾问表")
    @RequiresPermissions("generator:consultant:delete")
    public R delete(@RequestBody Integer[] ids){
		consultantService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

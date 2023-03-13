package com.home.modules.generator.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.home.modules.generator.entity.DemandEntity;
import com.home.modules.generator.service.DemandService;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.annotation.SysLog;



/**
 * 购房需求表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@RestController
@RequestMapping("generator/demand")
public class DemandController {
    @Autowired
    private DemandService demandService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("generator:demand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = demandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("generator:demand:info")
    public R info(@PathVariable("id") Integer id){
		DemandEntity demand = demandService.getById(id);

        return R.ok().put("demand", demand);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("保存购房需求表")
    @RequiresPermissions("generator:demand:save")
    public R save(@RequestBody DemandEntity demand){
		demandService.save(demand);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改购房需求表")
    @RequiresPermissions("generator:demand:update")
    public R update(@RequestBody DemandEntity demand){
		demandService.updateById(demand);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @SysLog("删除购房需求表")
    @RequiresPermissions("generator:demand:delete")
    public R delete(@RequestBody Integer[] ids){
		demandService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

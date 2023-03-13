package com.home.modules.generator.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.home.modules.generator.entity.EvaluationEntity;
import com.home.modules.generator.service.EvaluationService;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.annotation.SysLog;



/**
 * 测评表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:13
 */
@RestController
@RequestMapping("generator/evaluation")
public class EvaluationController {
    @Autowired
    private EvaluationService evaluationService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("generator:evaluation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = evaluationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("generator:evaluation:info")
    public R info(@PathVariable("id") Integer id){
		EvaluationEntity evaluation = evaluationService.getById(id);

        return R.ok().put("evaluation", evaluation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("保存测评表")
    @RequiresPermissions("generator:evaluation:save")
    public R save(@RequestBody EvaluationEntity evaluation){
		evaluationService.save(evaluation);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改测评表")
    @RequiresPermissions("generator:evaluation:update")
    public R update(@RequestBody EvaluationEntity evaluation){
		evaluationService.updateById(evaluation);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @SysLog("删除测评表")
    @RequiresPermissions("generator:evaluation:delete")
    public R delete(@RequestBody Integer[] ids){
		evaluationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

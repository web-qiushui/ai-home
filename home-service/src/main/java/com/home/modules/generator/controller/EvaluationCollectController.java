package com.home.modules.generator.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.home.modules.generator.entity.EvaluationCollectEntity;
import com.home.modules.generator.service.EvaluationCollectService;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.annotation.SysLog;



/**
 * 测评收藏表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@RestController
@RequestMapping("generator/evaluationcollect")
public class EvaluationCollectController {
    @Autowired
    private EvaluationCollectService evaluationCollectService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("generator:evaluationcollect:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = evaluationCollectService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("generator:evaluationcollect:info")
    public R info(@PathVariable("id") Integer id){
		EvaluationCollectEntity evaluationCollect = evaluationCollectService.getById(id);

        return R.ok().put("evaluationCollect", evaluationCollect);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("保存测评收藏表")
    @RequiresPermissions("generator:evaluationcollect:save")
    public R save(@RequestBody EvaluationCollectEntity evaluationCollect){
		evaluationCollectService.save(evaluationCollect);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改测评收藏表")
    @RequiresPermissions("generator:evaluationcollect:update")
    public R update(@RequestBody EvaluationCollectEntity evaluationCollect){
		evaluationCollectService.updateById(evaluationCollect);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @SysLog("删除测评收藏表")
    @RequiresPermissions("generator:evaluationcollect:delete")
    public R delete(@RequestBody Integer[] ids){
		evaluationCollectService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

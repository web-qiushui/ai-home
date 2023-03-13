package com.home.modules.generator.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.home.modules.generator.entity.EvaluationLikeEntity;
import com.home.modules.generator.service.EvaluationLikeService;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.annotation.SysLog;



/**
 * 测评点赞表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@RestController
@RequestMapping("generator/evaluationlike")
public class EvaluationLikeController {
    @Autowired
    private EvaluationLikeService evaluationLikeService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("generator:evaluationlike:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = evaluationLikeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("generator:evaluationlike:info")
    public R info(@PathVariable("id") Integer id){
		EvaluationLikeEntity evaluationLike = evaluationLikeService.getById(id);

        return R.ok().put("evaluationLike", evaluationLike);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("保存测评点赞表")
    @RequiresPermissions("generator:evaluationlike:save")
    public R save(@RequestBody EvaluationLikeEntity evaluationLike){
		evaluationLikeService.save(evaluationLike);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改测评点赞表")
    @RequiresPermissions("generator:evaluationlike:update")
    public R update(@RequestBody EvaluationLikeEntity evaluationLike){
		evaluationLikeService.updateById(evaluationLike);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @SysLog("删除测评点赞表")
    @RequiresPermissions("generator:evaluationlike:delete")
    public R delete(@RequestBody Integer[] ids){
		evaluationLikeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

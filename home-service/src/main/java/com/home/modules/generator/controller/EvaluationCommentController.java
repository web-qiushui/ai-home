package com.home.modules.generator.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.home.modules.generator.entity.EvaluationCommentEntity;
import com.home.modules.generator.service.EvaluationCommentService;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.annotation.SysLog;



/**
 * 测评评论表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@RestController
@RequestMapping("generator/evaluationcomment")
public class EvaluationCommentController {
    @Autowired
    private EvaluationCommentService evaluationCommentService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("generator:evaluationcomment:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = evaluationCommentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("generator:evaluationcomment:info")
    public R info(@PathVariable("id") Integer id){
		EvaluationCommentEntity evaluationComment = evaluationCommentService.getById(id);

        return R.ok().put("evaluationComment", evaluationComment);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("保存测评评论表")
    @RequiresPermissions("generator:evaluationcomment:save")
    public R save(@RequestBody EvaluationCommentEntity evaluationComment){
		evaluationCommentService.save(evaluationComment);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改测评评论表")
    @RequiresPermissions("generator:evaluationcomment:update")
    public R update(@RequestBody EvaluationCommentEntity evaluationComment){
		evaluationCommentService.updateById(evaluationComment);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @SysLog("删除测评评论表")
    @RequiresPermissions("generator:evaluationcomment:delete")
    public R delete(@RequestBody Integer[] ids){
		evaluationCommentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

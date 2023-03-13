package com.home.modules.generator.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.home.modules.generator.entity.ConsultantCommentEntity;
import com.home.modules.generator.service.ConsultantCommentService;
import com.home.common.utils.PageUtils;
import com.home.common.utils.R;
import com.home.common.annotation.SysLog;



/**
 * 顾问评论表
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2020-06-11 21:12:12
 */
@RestController
@RequestMapping("generator/consultantcomment")
public class ConsultantCommentController {
    @Autowired
    private ConsultantCommentService consultantCommentService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("generator:consultantcomment:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = consultantCommentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("generator:consultantcomment:info")
    public R info(@PathVariable("id") Integer id){
		ConsultantCommentEntity consultantComment = consultantCommentService.getById(id);

        return R.ok().put("consultantComment", consultantComment);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @SysLog("保存顾问评论表")
    @RequiresPermissions("generator:consultantcomment:save")
    public R save(@RequestBody ConsultantCommentEntity consultantComment){
		consultantCommentService.save(consultantComment);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改顾问评论表")
    @RequiresPermissions("generator:consultantcomment:update")
    public R update(@RequestBody ConsultantCommentEntity consultantComment){
		consultantCommentService.updateById(consultantComment);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @SysLog("删除顾问评论表")
    @RequiresPermissions("generator:consultantcomment:delete")
    public R delete(@RequestBody Integer[] ids){
		consultantCommentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

package com.home.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.home.common.annotation.NoRepeatSubmit;
import com.home.common.annotation.SysLog;
import com.home.common.dto.DatadictDTO;
import com.home.common.utils.Constant;
import com.home.common.utils.JsonResult;
import com.home.common.utils.PageBean;
import com.home.common.vo.DatadictVO;
import com.home.modules.sys.entity.SysDatadictEntity;
import com.home.modules.sys.service.SysDatadictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.home.common.utils.ShiroUtils.getUserId;


/**
 * 数据字典
 *
 * @author xiewei
 * @email 1048516133@qq.com
 * @date 2019-12-31 10:11:22
 */
@Slf4j
@Api(value = "数据字典接口", tags = {"数据字典接口"})
@RestController
@RequestMapping("/sys/datadict")
public class SysDatadictController {
    @Resource
    private SysDatadictService sysDatadictService;

    @NoRepeatSubmit
    @ApiOperation("删除数据字典")
    @SysLog("删除数据字典")
    @GetMapping("/deleteDatadict")
    public JsonResult deleteDatadict(@RequestParam List<Integer> ids){
        sysDatadictService.removeByIds(ids);
        log.info("删除数据字典，Id列表为:{}", JSON.toJSONString(ids));
        return JsonResult.success();
    }

    @ApiOperation("根据Id查询数据字典")
    @GetMapping("/getDatadictInfo")
    public JsonResult<DatadictVO> getDatadictInfo(@RequestParam Integer id){
        SysDatadictEntity sysDatadictEntity = sysDatadictService.getById(id);
        if (sysDatadictEntity == null) {
            return JsonResult.fail("Id错误");
        }
        DatadictVO vo = new DatadictVO();
        BeanUtils.copyProperties(sysDatadictEntity, vo);
        return JsonResult.success(vo);
    }

    @ApiOperation("根据type获取数据字典")
    @GetMapping("/anon/getDatadict/{type}")
    public JsonResult<DatadictVO> getDatadict(@PathVariable("type") String type) {
        DatadictVO vo = sysDatadictService.getDatadict(type);
        return JsonResult.success(vo);
    }

    @ApiOperation("查询数据字典列表")
    @GetMapping("/getDatadictList")
    public JsonResult<PageBean<DatadictVO>> getDatadictList(DatadictDTO dto) {
        PageBean<DatadictVO> pageBean = sysDatadictService.getDatadictList(dto);
        return JsonResult.success(pageBean);
    }

    @ApiOperation("获取数据字典")
    @GetMapping("/anon/getDatadict")
    public JsonResult<List<DatadictVO>> getDatadict() {
        List<DatadictVO> list = sysDatadictService.getDatadict();
        return JsonResult.success(list);
    }

    @SysLog("新增或修改数据字典")
    @ApiOperation("新增或修改数据字典")
    @PostMapping("saveDatadict")
    public JsonResult saveDatadict(@RequestBody @Valid DatadictVO dto) {
        return sysDatadictService.saveDatadict(dto, getUserId().intValue());
    }

}

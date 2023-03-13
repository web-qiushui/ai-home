package com.home.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.home.common.annotation.NoRepeatSubmit;
import com.home.common.dto.MessageDTO;
import com.home.common.dto.UserDTO;
import com.home.common.utils.*;
import com.home.common.validator.Assert;
import com.home.common.vo.BeanVO;
import com.home.common.vo.MessageVO;
import com.home.common.vo.UserVO;
import com.home.modules.app.service.UserService;
import com.home.modules.generator.dto.*;
import com.home.modules.generator.entity.DemandEntity;
import com.home.modules.generator.entity.EvaluationEntity;
import com.home.modules.generator.entity.OpeningNoticeEntity;
import com.home.modules.generator.entity.ReportEntity;
import com.home.modules.generator.service.*;
import com.home.modules.generator.vo.*;
import com.home.modules.sys.entity.SysUserEntity;
import com.home.modules.sys.service.SysApiService;
import com.home.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Web对外接口
 *
 * @author xiewei 1048516133@qq.com
 */
@Slf4j
@Api(value = "WEB接口", tags = {"WEB相关接口"})
@RestController
@RequestMapping("/sys/api")
public class SysApiController extends AbstractController {

    @Resource
    private SysApiService sysApiService;
    @Resource
    private UserService userService;
    @Resource
    private EvaluationService evaluationService;
    @Resource
    private ConsultantService consultantService;
    @Resource
    private DemandService demandService;
    @Resource
    private ConsultantUserService consultantUserService;
    @Resource
    private EvaluationCommentService evaluationCommentService;
    @Resource
    private CarouselService carouselService;
    @Resource
    private ChatRecordService chatRecordService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private OpeningNoticeService openingNoticeService;
    @Resource
    private ReportService reportService;
    @Resource
    private ConsultantCommentService consultantCommentService;

    @ApiOperation("获取用户列表")
    @GetMapping("getUserList")
    public JsonResult<PageBean<UserVO>> getUserList(UserDTO dto) {
        return JsonResult.success(userService.getUserList(dto));
    }

    @ApiOperation("查询所有普通用户列表")
    @GetMapping("getUserBeanList")
    public JsonResult<List<BeanVO>> getUserBeanList() {
        return JsonResult.success(userService.getUserBeanList());
    }

    @ApiOperation("查询企业列表")
    @GetMapping("getFirmList")
    public JsonResult<List<BeanVO>> getFirmList() {
        return JsonResult.success(sysUserService.queryByRole(3));
    }

    @ApiOperation("根据ID获取用户信息")
    @GetMapping("getUserInfoById")
    public JsonResult<UserVO> getUserInfoById(@ApiParam("用户Id") @RequestParam Integer userId) {
        return JsonResult.success(userService.userInfo(userId));
    }

    @ApiOperation("根据ID修改用户信息")
    @GetMapping("updateUserInfo")
    public JsonResult updateUserInfo(@RequestBody UserDTO dto) {
        return userService.updateUserInfo(dto);
    }

    @ApiOperation("查询测评列表")
    @GetMapping("getEvaluationList")
    public JsonResult<PageBean<EvaluationVO>> getEvaluationList(EvaluationDTO dto) {
        SysUserEntity user = getUser();
        if(!user.getRoleIdList().contains(Constant.Role.SYSTEM) && !user.getRoleIdList().contains(Constant.Role.ADMIN_SYSTEM)) {
            if (user.getRoleIdList().contains(Constant.Role.AI) || user.getRoleIdList().contains(Constant.Role.HOME)) {
                dto.setSysUserId(user.getUserId().intValue());
            }
        }
        return JsonResult.success(evaluationService.getEvaluationList(dto));
    }

    @ApiOperation("根据Id查询测评详情")
    @GetMapping("getEvaluationInfo")
    public JsonResult<EvaluationVO> getEvaluationInfo(@RequestParam Integer id) {
        Integer userId = null;
        if (JwtUtil.isToken()) {
            userId = JwtUtil.getUserId();
        }
        EvaluationVO evaluationInfo = evaluationService.getEvaluationInfo(id, userId);
        return JsonResult.success(evaluationInfo);
    }

    @NoRepeatSubmit
    @ApiOperation("新增或修改测评")
    @PostMapping("saveEvaluation")
    public JsonResult saveEvaluation(@Valid @RequestBody SaveEvaluationDTO dto) {
        SysUserEntity sysUserEntity = getUser();
        dto.setSysUserId(sysUserEntity.getUserId().intValue());
        dto.setUserId(sysUserEntity.getAppUserId());
        return evaluationService.saveEvaluation(dto);
    }

    @NoRepeatSubmit
    @ApiOperation("修改测评状态")
    @GetMapping("updateEvaluationStatus")
    public JsonResult updateEvaluationStatus(@RequestParam Integer id) {
        EvaluationEntity evaluationEntity = evaluationService.getById(id);
        if (evaluationService == null) {
            return JsonResult.fail("Id错误");
        }
        if (Constant.Status.DISABLE.getType().equals(evaluationEntity.getStatus())) {
            evaluationEntity.setStatus(Constant.Status.ENABLE.getType());
        } else {
            evaluationEntity.setStatus(Constant.Status.DISABLE.getType());
        }
        evaluationService.updateById(evaluationEntity);
        return JsonResult.success();
    }

    @ApiOperation("查询购房需求列表")
    @GetMapping("getDemandList")
    public JsonResult<PageBean<DemandVO>> getDemandList(DemandDTO dto) {
        SysUserEntity user = getUser();
        if (user.getRoleIdList().contains(Constant.Role.AI)) {
            dto.setAiConsultantId(user.getAppUserId());
        }
        PageBean<DemandVO> pageBean = demandService.getDemandList(dto);
        return JsonResult.success(pageBean);
    }

    @ApiOperation("查询顾问列表")
    @GetMapping("getConsultantList")
    public JsonResult<PageBean<ConsultantVO>> getConsultantList(ConsultantDTO dto) {
        SysUserEntity user = getUser();
        if (user.getRoleIdList().contains(Constant.Role.FIRM_SYSTEM)) {
            dto.setFirmId(user.getUserId().intValue());
        }
        PageBean<ConsultantVO> pageBean = consultantService.getConsultantList(dto);
        return JsonResult.success(pageBean);
    }

    @ApiOperation("获取用户顾问列表")
    @GetMapping("/getConsultantUserList")
    public JsonResult<PageBean<ConsultantUserVO>> getConsultantUserList(PageReq req) {
        PageBean<ConsultantUserVO> consultantUserList = consultantUserService.getConsultantUserList(req);
        return JsonResult.success(consultantUserList);
    }

    @ApiOperation("获取聊天记录")
    @GetMapping("/getHistoricalNews")
    public JsonResult<PageBean<MessageVO>> getHistoricalNews(MessageDTO dto) {
        return chatRecordService.getHistoryRecord(dto);
    }

    @ApiOperation("查询测评评论列表")
    @GetMapping("getEvaluationComment")
    public JsonResult<PageBean<EvaluationCommentVO>> getEvaluationComment(EvaluationCommentDTO dto) {
        Assert.isNull(dto.getEvaluationId(), "测评Id不能为空");
        PageBean<EvaluationCommentVO> pageBean = evaluationCommentService.getEvaluationComment(dto);
        return JsonResult.success(pageBean);
    }

    @NoRepeatSubmit
    @ApiOperation("测评评论审核")
    @PostMapping("auditEvaluationComment")
    public JsonResult auditEvaluationComment(@RequestBody EvaluationCommentDTO dto) {
        Assert.isNull(dto.getEvaluationId(), "测评Id不能为空");
        Assert.isNull(dto.getStatus(), "状态不能为空");
        return sysApiService.auditEvaluationComment(dto, getUserId().intValue());
    }

    @ApiOperation("查询轮播图")
    @GetMapping("getCarouselList")
    public JsonResult<PageBean<CarouselVO>> getCarouselList(CarouselDTO dto) {
        PageBean<CarouselVO> carouselList = carouselService.getCarouselList(dto);
        return JsonResult.success(carouselList);
    }

    @NoRepeatSubmit
    @ApiOperation("修改或新增轮播图")
    @PostMapping("saveCarousel")
    public JsonResult saveCarousel(@Valid @RequestBody SaveCarouselVO dto) {
        return sysApiService.saveCarousel(dto);
    }

    @ApiOperation("删除轮播图")
    @NoRepeatSubmit
    @GetMapping("deleteCarousel")
    public JsonResult getCarouselList(@RequestParam List<Integer> ids) {
        carouselService.removeByIds(ids);
        return JsonResult.success();
    }

    @NoRepeatSubmit
    @ApiOperation("顾问审核")
    @PostMapping("auditConsultant")
    public JsonResult auditConsultant(@RequestBody AuditConsultantDTO dto) {
        Assert.isNull(dto.getId(), "顾问Id不能为空");
        Assert.isNull(dto.getStatus(), "状态不能为空");
        dto.setOperateId(getUserId());
        return sysApiService.auditConsultant(dto);
    }

    @NoRepeatSubmit
    @PostMapping("saveConsultant")
    @ApiOperation("新增顾问")
    public JsonResult saveConsultant(@RequestBody SaveConsultantDTO dto) {
        Assert.isBlank(dto.getAvatar(), "头像不能为空");
        Assert.isBlank(dto.getPhone(), "手机号不能为空");
        Assert.isBlank(dto.getIdCard(), "身份证不能为空");
        Assert.isBlank(dto.getName(), "姓名不能为空");
        if (!ValidatorUtils.isMobile(dto.getPhone())) {
            return JsonResult.fail("手机号码格式错误");
        }
        if (!ValidatorUtils.isIDCard(dto.getIdCard())) {
            return JsonResult.fail("身份证格式错误");
        }
        if (!ValidatorUtils.isName(dto.getName())) {
            return JsonResult.fail("姓名格式不正确");
        }
        if (dto.getUserId() == null) {
            return JsonResult.fail("请选择用户");
        }
        return sysApiService.saveConsultant(dto, getUserId());
    }

    @ApiOperation("根据顾问Id查询顾问详情")
    @GetMapping("getConsultantInfo")
    public JsonResult<ConsultantVO> getConsultantInfo(Integer id) {
        ConsultantVO consultantVO = consultantService.getConsultantInfo(id);
        return JsonResult.success(consultantVO);
    }

    @ApiOperation("查询开盘预告列表")
    @GetMapping("getOpeningNoticeList")
    public JsonResult<PageBean<OpeningNoticeVO>> getOpeningNoticeList(PageReq pageReq) {
        PageBean<OpeningNoticeVO> pageBean = openingNoticeService.getOpeningNoticeList(pageReq);
        return JsonResult.success(pageBean);
    }

    @ApiOperation("查询开盘预告详情")
    @GetMapping("getOpeningNoticeInfo")
    public JsonResult<OpeningNoticeVO> getOpeningNoticeInfo(Integer id) {
        OpeningNoticeEntity noticeEntity = openingNoticeService.getById(id);
        OpeningNoticeVO vo = new OpeningNoticeVO();
        BeanUtils.copyProperties(noticeEntity, vo);
        return JsonResult.success(vo);
    }

    @NoRepeatSubmit
    @ApiOperation("修改或新增开盘预告")
    @PostMapping("saveOpeningNotice")
    public JsonResult saveOpeningNotice(@RequestBody OpeningNoticeVO dto) {
        Assert.isBlank(dto.getDescription(), "内容不能为空");
        Assert.isBlank(dto.getImageUrl(), "图片不能为空");
        Assert.isNull(dto.getStatus(), "状态不能为空");
        sysApiService.saveOpeningNotice(dto);
        return JsonResult.success();
    }

    @ApiOperation("删除开盘预告")
    @NoRepeatSubmit
    @GetMapping("deleteOpeningNotice")
    public JsonResult deleteOpeningNotice(@RequestParam List<Integer> ids) {
        openingNoticeService.removeByIds(ids);
        return JsonResult.success();
    }

    @NoRepeatSubmit
    @PostMapping("saveDemand")
    @ApiOperation("修改购房需求")
    public JsonResult saveDemand(@RequestBody @Valid DemandDTO dto) {
        Assert.isNull(dto.getId(), "Id不能为空");
        if (StringUtils.isNotEmpty(dto.getPhone())) {
            if (!ValidatorUtils.isMobile(dto.getPhone())) {
                return JsonResult.fail("手机号码格式错误");
            }
        }
        return sysApiService.saveDemand(dto);
    }

    @ApiOperation("查询购房需求详情")
    @GetMapping("getDemandInfo")
    public JsonResult<DemandVO> getDemandInfo(@RequestParam Integer id) {
        DemandEntity demandEntity = demandService.getById(id);
        DemandVO vo = new DemandVO();
        BeanUtils.copyProperties(demandEntity, vo);
        vo.setLabel(JSON.parseArray(demandEntity.getLabel(), String.class));
        return JsonResult.success(vo);
    }

    @NoRepeatSubmit
    @ApiOperation("删除购房需求")
    @GetMapping("deleteDemand")
    public JsonResult deleteDemand(@RequestParam Integer id) {
        DemandEntity demandEntity = demandService.getById(id);
        if (demandEntity == null) {
            return JsonResult.fail("购房需求为空");
        }
        Integer userId = JwtUtil.getUserId();
        if (!demandEntity.getHomeUserId().equals(userId)) {
            return JsonResult.fail("购房需求为空");
        }
        demandService.removeById(id);
        return JsonResult.success();
    }

    @NoRepeatSubmit
    @ApiOperation("新增或修改购房报告")
    @PostMapping("saveHomeReport")
    public JsonResult saveHomeReport(@RequestBody ReportDTO dto) {
        SysUserEntity user = getUser();
        if (!user.getRoleIdList().contains(Constant.Role.AI) || !user.getRoleIdList().contains(Constant.Role.HOME)) {
            return JsonResult.success("无权限");
        }
        Assert.isNull(dto.getHomeDemandId(), "购房需求不能为空");
        ConsultantVO vo = consultantService.getConsultantByUserId(user.getAppUserId());
        dto.setHomeUserId(user.getAppUserId());
        dto.setHomeConsultantId(vo.getId());
        return reportService.saveHomeReport(dto);
    }

    @ApiOperation("查询购房报告详情")
    @GetMapping("getHomeReportInfo")
    public JsonResult<ReportVO> getHomeReportInfo(@RequestParam Integer id) {
        ReportEntity reportEntity = reportService.getById(id);
        ReportVO vo = new ReportVO();
        BeanUtils.copyProperties(reportEntity, vo);
        return JsonResult.success(vo);
    }

    @ApiOperation("查询购房报告列表")
    @GetMapping("getHomeReportList")
    public JsonResult<PageBean<ReportVO>> getHomeReportList(ReportDTO dto) {
        PageBean<ReportVO> pageBean = reportService.getHomeReportList(dto);
        return JsonResult.success(pageBean);
    }

    @ApiOperation("查询统计数据")
    @GetMapping("getStatistics")
    public JsonResult<StatisticslVO> getStatistics(@ApiParam("类型 day/month/year 日、月、年") String type) {
        StatisticslVO statistics = sysApiService.getStatistics(type);
        return JsonResult.success(statistics);
    }

    @ApiOperation("查询顾问评价列表")
    @GetMapping("getConsultantCommentList")
    public JsonResult<PageBean<ConsultantCommentVO>> getConsultantCommentList(ConsultantCommentDTO dto) {
        Assert.isNull(dto.getConsultantId(), "顾问Id不能为空");
        PageBean<ConsultantCommentVO> pageBean = consultantCommentService.getConsultantComment(dto);
        return JsonResult.success(pageBean);
    }

}

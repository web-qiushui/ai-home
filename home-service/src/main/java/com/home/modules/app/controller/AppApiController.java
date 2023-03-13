package com.home.modules.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.home.common.annotation.NoRepeatSubmit;
import com.home.common.constant.config.ChatCardConfig;
import com.home.common.utils.*;
import com.home.common.validator.Assert;
import com.home.modules.app.annotation.Login;
import com.home.modules.app.service.AppApiService;
import com.home.modules.generator.dto.*;
import com.home.modules.generator.entity.ChatRecordEntity;
import com.home.modules.generator.entity.DemandEntity;
import com.home.modules.generator.service.*;
import com.home.modules.generator.vo.*;
import com.home.modules.oss.service.SysOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * App对外接口
 *
 * @author xiewei 1048516133@qq.com
 */
@Slf4j
@Api(value = "APP接口", tags = {"APP相关接口"})
@RestController
@RequestMapping("/app/api")
public class AppApiController {

    @Resource
    private AppApiService appApiService;
    @Resource
    private SysOssService sysOssService;
    @Resource
    private EvaluationService evaluationService;
    @Resource
    private DemandService demandService;
    @Resource
    private ConsultantService consultantService;
    @Resource
    private EvaluationCollectService evaluationCollectService;
    @Resource
    private EvaluationForwardService evaluationForwardService;
    @Resource
    private EvaluationCommentService evaluationCommentService;
    @Resource
    private ConsultantCommentService consultantCommentService;
    @Resource
    private ConsultantUserService consultantUserService;
    @Resource
    private CarouselService carouselService;
    @Resource
    private OpeningNoticeService openingNoticeService;
    @Resource
    private ChatRecordService chatRecordService;

    @Login
    @NoRepeatSubmit
    @ApiOperation("上传文件")
    @PostMapping("/upload/images")
    public JsonResult uploadImages(@RequestParam("file") MultipartFile file) {
        return sysOssService.uploadImages(file);
    }

    @ApiOperation("根据Id查询推荐测评")
    @GetMapping("getRecommendList")
    public JsonResult<List<EvaluationListVO>> getRecommendList(@RequestParam Integer id) {
        List<EvaluationListVO> recommendList = evaluationService.getRecommendList(id);
        return JsonResult.success(recommendList);
    }

    @ApiOperation("查询测评列表")
    @GetMapping("getMainPageInfo")
    public JsonResult<PageBean<EvaluationListVO>> getMainPageInfo(EvaluationDTO dto) {
        dto.setType(1);
        PageBean<EvaluationListVO> mainPageInfo = evaluationService.getMainPageInfo(dto);
        return JsonResult.success(mainPageInfo);
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

    @ApiOperation("获取金刚区正在登记列表")
    @GetMapping("getRegistering")
    public JsonResult<List<EvaluationListVO>> getRegistering() {
        List<EvaluationListVO> list = evaluationService.getKingKongList("REGISTERING");
        return JsonResult.success(list);
    }

    @ApiOperation("获取金刚区开盘预售列表")
    @GetMapping("getPreSale")
    public JsonResult<List<EvaluationListVO>> getPreSale() {
        List<EvaluationListVO> list = evaluationService.getKingKongList("PRE_SALE");
        return JsonResult.success(list);
    }

    @ApiOperation("获取金刚区24h快讯列表")
    @GetMapping("get24hNews")
    public JsonResult<PageBean<EvaluationListVO>> get24hNews(EvaluationDTO dto) {
        dto.setType(2);
        if (JwtUtil.isToken()) {
            dto.setUserId(JwtUtil.getUserId());
        }
        PageBean<EvaluationListVO> list = evaluationService.get24hNews(dto);
        return JsonResult.success(list);
    }

    @Login
    @NoRepeatSubmit
    @PostMapping("saveDemand")
    @ApiOperation("发布购房需求")
    public JsonResult<DemandVO> saveDemand(@RequestBody @Valid DemandDTO dto) {
        if (StringUtils.isNotEmpty(dto.getPhone())) {
            if (!ValidatorUtils.isMobile(dto.getPhone())) {
                return JsonResult.fail("手机号码格式错误");
            }
        }
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        return appApiService.saveDemand(dto);
    }

    @Login
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

    @ApiOperation("查询购房需求提交数量")
    @GetMapping("getDemandSize")
    public JsonResult getDemandSize() {
        Integer demandSize = demandService.getDemandSize();
        return JsonResult.success(demandSize);
    }

    @Login
    @ApiOperation("查询购房需求列表")
    @GetMapping("getDemandList")
    public JsonResult<PageBean<DemandVO>> getDemandList(PageReq page) {
        Integer userId = JwtUtil.getUserId();
        PageBean<DemandVO> pageBean = demandService.getMyPublishByUserId(page, userId);
        return JsonResult.success(pageBean);
    }

    @Login
    @ApiOperation("查询顾问列表")
    @GetMapping("getConsultantUserList")
    public JsonResult<PageBean<ConsultantVO>> getConsultantUserList(ConsultantUserDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        PageBean<ConsultantVO> pageBean = consultantService.getConsultantUserList(dto);
        return JsonResult.success(pageBean);
    }

    @Login
    @ApiOperation("查询置业顾问列表")
    @GetMapping("getHomeConsultantList")
    public JsonResult<PageBean<ConsultantVO>> getHomeConsultantList(ConsultantUserDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        dto.setType(Constant.AppRole.HOME);
        dto.setStatus(Constant.ConsultantStatus.SUCCESS);
        PageBean<ConsultantVO> pageBean = consultantService.getConsultantList(dto);
        return JsonResult.success(pageBean);
    }

    @Login
    @ApiOperation("根据顾问Id查询顾问详情")
    @GetMapping("getConsultantInfo")
    public JsonResult<ConsultantVO> getConsultantInfo(Integer id) {
        ConsultantVO consultantVO = consultantService.getConsultantInfo(id);
        return JsonResult.success(consultantVO);
    }

    @Login
    @ApiOperation("查询认证信息")
    @GetMapping("getConsultantUser")
    public JsonResult<ConsultantVO> getConsultantUser() {
        Integer userId = JwtUtil.getUserId();
        ConsultantVO consultantVO = consultantService.getConsultantByUserId(userId);
        return JsonResult.success(consultantVO);
    }

    @Login
    @ApiOperation("查询关注列表")
    @GetMapping("getEvaluationCollectList")
    public JsonResult<PageBean<EvaluationVO>> getEvaluationCollectList(PageReq page) {
        Integer userId = JwtUtil.getUserId();
        PageBean<EvaluationVO> pageBean = evaluationCollectService.getEvaluationCollectList(page, userId);
        return JsonResult.success(pageBean);
    }

    @Login
    @ApiOperation("查询分享列表")
    @GetMapping("getEvaluationForwardList")
    public JsonResult<PageBean<EvaluationVO>> getEvaluationForwardList(EvaluationForwardDTO dto) {
        Integer userId = JwtUtil.getUserId();
        PageBean<EvaluationVO> pageBean = evaluationForwardService.getEvaluationForwardList(dto, userId);
        return JsonResult.success(pageBean);
    }

    @Login
    @ApiOperation("查询我的评论列表")
    @GetMapping("getEvaluationCommentList")
    public JsonResult<PageBean<EvaluationCommentVO>> getEvaluationCommentList(EvaluationCommentDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        dto.setEvaluationId(null);
        PageBean<EvaluationCommentVO> pageBean = evaluationCommentService.getEvaluationCommentList(dto);
        return JsonResult.success(pageBean);
    }

    @Login
    @ApiOperation("查询我的评价列表")
    @GetMapping("getConsultantCommentList")
    public JsonResult<PageBean<ConsultantCommentVO>> getConsultantCommentList(ConsultantCommentDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        dto.setConsultantId(null);
        PageBean<ConsultantCommentVO> pageBean = consultantCommentService.getConsultantCommentList(dto);
        return JsonResult.success(pageBean);
    }

    @Login
    @ApiOperation("查询关注数量")
    @GetMapping("getEvaluationCollectSize")
    public JsonResult getEvaluationCollectSize() {
        Integer userId = JwtUtil.getUserId();
        Integer evaluationCollectSize = evaluationCollectService.getEvaluationCollectSize(userId);
        return JsonResult.success(evaluationCollectSize);
    }

    @ApiOperation("查询测评评论列表")
    @GetMapping("getEvaluationComment")
    public JsonResult<PageBean<EvaluationCommentVO>> getEvaluationComment(EvaluationCommentDTO dto) {
        Assert.isNull(dto.getEvaluationId(), "测评Id不能为空");
        if (JwtUtil.isToken()) {
            Integer userId = JwtUtil.getUserId();
            dto.setUserId(userId);
        } else {
            dto.setUserId(null);
        }
        dto.setStatus(Constant.AuditStatus.SUCCESS);
        PageBean<EvaluationCommentVO> pageBean = evaluationCommentService.getEvaluationComment(dto);
        return JsonResult.success(pageBean);
    }

    @Login
    @ApiOperation("查询顾问评价列表")
    @GetMapping("getConsultantComment")
    public JsonResult<PageBean<ConsultantCommentVO>> getConsultantComment(ConsultantCommentDTO dto) {
        Assert.isNull(dto.getConsultantId(), "顾问Id不能为空");
        dto.setUserId(null);
        PageBean<ConsultantCommentVO> pageBean = consultantCommentService.getConsultantComment(dto);
        return JsonResult.success(pageBean);
    }

    @ApiOperation("查询轮播图")
    @GetMapping("getCarouselList")
    public JsonResult<List<CarouselVO>> getCarouselList() {
        List<CarouselVO> carouselList = carouselService.getCarouselList();
        return JsonResult.success(carouselList);
    }

    @ApiOperation("查询开盘预告")
    @GetMapping("getOpeningNotice")
    public JsonResult<OpeningNoticeVO> getOpeningNotice() {
        OpeningNoticeVO openingNotice = openingNoticeService.getOpeningNotice();
        return JsonResult.success(openingNotice);
    }

    @Login
    @NoRepeatSubmit
    @ApiOperation("新增顾问评价")
    @PostMapping("saveConsultantComment")
    public JsonResult saveConsultantComment(@RequestBody @Valid SaveConsultantCommentDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        return appApiService.saveConsultantComment(dto);
    }

    @Login
    @NoRepeatSubmit
    @PostMapping("saveConsultant")
    @ApiOperation("新增置业顾问")
    public JsonResult saveConsultant(@RequestBody @Valid SaveConsultantDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        return appApiService.saveConsultant(dto);
    }

    @Login
    @NoRepeatSubmit
    @ApiOperation("新增测评评论")
    @PostMapping("saveEvaluationComment")
    public JsonResult saveEvaluationComment(@RequestBody @Valid SaveEvaluationCommentDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        return appApiService.saveEvaluationComment(dto);
    }

    @Login
    @NoRepeatSubmit
    @ApiOperation("新增测评转发")
    @PostMapping("saveEvaluationForward")
    public JsonResult saveEvaluationForward(@RequestBody @Valid SaveEvaluationForwardDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        return appApiService.saveEvaluationForward(dto);
    }

    @Login
    @NoRepeatSubmit
    @ApiOperation("新增测评收藏")
    @PostMapping("saveEvaluationCollect")
    public JsonResult saveEvaluatioCollect(@RequestBody @Valid SaveEvaluatioCollectDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        return appApiService.saveEvaluatioCollect(dto);
    }

    @Login
    @NoRepeatSubmit
    @ApiOperation("新增测评点赞")
    @PostMapping("saveEvaluationLike")
    public JsonResult saveEvaluationLike(@RequestBody @Valid SaveEvaluationLikeDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        return appApiService.saveEvaluationLike(dto);
    }

    @Login
    @NoRepeatSubmit
    @ApiOperation("新增测评评论点赞")
    @PostMapping("saveEvaluationCommentLike")
    public JsonResult saveEvaluationCommentLike(@RequestBody @Valid SaveEvaluationCommentLikeDTO dto) {
        Integer userId = JwtUtil.getUserId();
        dto.setUserId(userId);
        return appApiService.saveEvaluationCommentLike(dto);
    }

    @Login
    @ApiOperation("获取我的信息")
    @GetMapping("/getMineInfo")
    public JsonResult<MineInfoVO> getMineInfo() {
        Integer userId = JwtUtil.getUserId();
        MineInfoVO mineInfo = appApiService.getMineInfo(userId);
        return JsonResult.success(mineInfo);
    }

    @Login
    @ApiOperation("获取好友列表")
    @GetMapping("/getBuddyList")
    public JsonResult<List<BuddyListVO>> getBuddyList(String search) {
        Integer userId = JwtUtil.getUserId();
        List<BuddyListVO> buddyList = consultantUserService.getBuddyList(userId, search);
        return JsonResult.success(buddyList);
    }

    @Login
    @ApiOperation("查询聊天卡片")
    @GetMapping("getChatCard")
    JsonResult<List<ChatCardConfig>> getChatCard() {
        Integer userId = JwtUtil.getUserId();
        List<ChatCardConfig> chatCardConfigs = appApiService.getChatCard(userId);
        return JsonResult.success(chatCardConfigs);
    }

    @Login
    @NoRepeatSubmit
    @ApiOperation("新增AI顾问好友")
    @GetMapping("saveAiBuddy")
    JsonResult<BuddyListVO> saveAiBuddy(@ApiParam("测评Id") Integer evaluationId, @ApiParam("购房需求Id") Integer demandId) {
        Integer userId = JwtUtil.getUserId();
        ConsultantVO vo = appApiService.getAiConsultant(evaluationId, userId);
        BuddyListVO buddyInfo = null;
        if (vo != null) {
            Integer id = consultantUserService.saveBuddy(userId, vo.getId(), Constant.AppRole.AI, null);
            buddyInfo = consultantUserService.getBuddyInfo(id);
            if (demandId != null) {
                DemandEntity demandEntity = demandService.getById(demandId);
                demandEntity.setAiConsultantId(buddyInfo.getConsultantUserId());
                demandService.updateById(demandEntity);
            }
        }
        return JsonResult.success(buddyInfo);
    }

    @Login
    @NoRepeatSubmit
    @ApiOperation("新增置业顾问好友")
    @GetMapping("saveHomeBuddy")
    JsonResult<BuddyListVO> saveHomeBuddy(@ApiParam("顾问Id") @RequestParam Integer consultantId,
                                          @ApiParam("推荐顾问Id") @RequestParam Integer recommendId,
                                          @ApiParam("UUID") String uuid) {
        Integer userId = JwtUtil.getUserId();
        Integer id = consultantUserService.saveBuddy(userId, consultantId, Constant.AppRole.HOME, recommendId);
        BuddyListVO buddyInfo = consultantUserService.getBuddyInfo(id);
        if (StringUtils.isNotEmpty(uuid)) {
            ChatRecordEntity chatRecordEntity = chatRecordService.getChatRecordByUuidAndType(uuid, Constant.ChatRecordType.BUSINESS_CARD);
            if (chatRecordEntity != null) {
                JSONObject jsonObject = JSON.parseObject(chatRecordEntity.getContent());
                if (jsonObject.get("status") == null || jsonObject.getInteger("status") == 0) {
                    jsonObject.put("status", 1);
                    chatRecordEntity.setContent(jsonObject.toJSONString());
                    chatRecordService.updateById(chatRecordEntity);
                }
            }
        }
        return JsonResult.success(buddyInfo);
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", null);
//        if (jsonObject.get("status") == null || jsonObject.getInteger("status") == 0) {
        if (jsonObject.getInteger("status") == 0) {
            System.out.println(1);
        }
    }

    @Login
    @NoRepeatSubmit
    @ApiOperation("根据顾问id查询好友信息")
    @GetMapping("getBuddyByConsultant")
    JsonResult<BuddyListVO> getBuddyByConsultant(@ApiParam("顾问Id") @RequestParam Integer consultantId) {
        Integer userId = JwtUtil.getUserId();
        Integer id = consultantUserService.saveBuddy(userId, consultantId, null, null);
        BuddyListVO buddyInfo = consultantUserService.getBuddyInfo(id);
        return JsonResult.success(buddyInfo);
    }

}

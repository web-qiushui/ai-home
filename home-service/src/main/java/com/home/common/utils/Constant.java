package com.home.common.utils;

/**
 * 常量
 *
 * @author xw
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;
    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     *  升序
     */
    public static final String ASC = "asc";
	/**
	 * 菜单类型
	 * 
	 * @author chenshun
	 * @email sunlightcs@gmail.com
	 * @date 2016年11月15日 下午1:24:29
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务状态
     * 
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    /**
     * 开启状态
     */
    public enum Status {
        /**
         * 禁用
         */
        DISABLE(0, "禁用"),
        /**
         * 启用
         */
        ENABLE(1, "启用");

        private Integer type;
        private String name;

        Status(Integer type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Integer getType() {
            return type;
        }
    }

    /**
     * 数字字典类型
     */
    public static class DatadictType {
        /**
         * 菜单
         */
        public static String MENU = "1";
        /**
         * 子菜单
         */
        public static String SUB_MENU = "2";
        /**
         * 常量
         */
        public static String CONSTANT = "3";

    }

    /**
     * 数字字典
     */
    public static class Datadict {
        // 区域选择
        public static String REGION_SELECT = "REGION_SELECT";
        // 价格选择
        public static String PRICE_SELECT = "PRICE_SELECT";
        // 面积选择
        public static String AREA_SELECT = "AREA_SELECT";
        // 户型选择
        public static String UNIT_TYPE_SELECT = "UNIT_TYPE_SELECT";
        // 类型选择
        public static String TYPE_SELECT = "TYPE_SELECT";
        // 其他选择
        public static String OTHER_SELECT = "OTHER_SELECT";
        // 标签数量
        public static String LABEL_SIZE = "LABEL_SIZE";
        // 打星数量
        public static String STAR_SIZE = "STAR_SIZE";
        // 购房需求数量
        public static String DEMAND_SIZE = "DEMAND_SIZE";
        // 自动回复
        public static String AUTO_RESPONSE = "AUTO_RESPONSE";
    }

    /**
     * 小程序用户角色
     */
    public static class AppRole {

        /**
         * 普通用户
         */
        public static Integer USER = 0;
        /**
         * AI顾问
         */
        public static Integer AI = 1;
        /**
         * 置业顾问
         */
        public static Integer HOME = 2;

    }

    /**
     * 顾问状态
     */
    public static class ConsultantStatus {

        /**
         * 未认证
         */
        public static Integer NO = 0;
        /**
         * 认证中
         */
        public static Integer AUDIT = 1;
        /**
         * 认证成功
         */
        public static Integer SUCCESS = 2;
        /**
         * 认证失败
         */
        public static Integer FAIL = 3;
    }

    /**
     * 顾问审核类型
     */
    public static class ConsultantAuditType {

        /**
         * 申请
         */
        public static Integer APPLICATION = 1;
        /**
         * 平台审核
         */
        public static Integer SYSTEM_AUDIT = 2;
        /**
         * 企业审核
         */
        public static Integer FIRM_AUDIT = 3;
    }

    /**
     * 验证码类型
     */
    public static class CaptchaType {
        /**
         * 通用
         */
        public static  Integer GENERAL = 0;
        /**
         * 注册
         */
        public static Integer REGISTERED = 1;
        /**
         * 登录
         */
        public static Integer LOGIN = 3;
        /**
         * 找回密码
         */
        public static Integer RECOVER_PASSWORD = 4;
    }

    /**
     * 消息类型（1、文字 2、测评 3、购房报告 4、评价 5、置业顾问名片 6、照片 7 快速回复）
     */
    public static class ChatRecordType {
        /**
         * 文字
         */
        public static  Integer TEXT = 1;
        /**
         * 测评
         */
        public static Integer EVALUATION = 2;
        /**
         * 购房报告
         */
        public static Integer PURCHASE_REPORT = 3;
        /**
         * 评价
         */
        public static Integer COMMENT = 4;
        /**
         * 顾问名片
         */
        public static Integer BUSINESS_CARD = 5;
        /**
         * 照片
         */
        public static Integer PHOTO = 6;
        /**
         * 快速回复
         */
        public static Integer QUICK_REPLY = 7;
    }

    /**
     * 审核状态
     */
    public static class AuditStatus {

        /**
         * 审核中
         */
        public static Integer AUDIT = 1;
        /**
         * 审核成功
         */
        public static Integer SUCCESS = 2;
        /**
         * 审核失败
         */
        public static Integer FAIL = 3;
    }

    /**
     * 角色
     */
    public static class Role {

        /**
         * 超级管理员
         */
        public static Long SYSTEM = 1L;
        /**
         * 系统管理员
         */
        public static Long ADMIN_SYSTEM = 2L;
        /**
         * 企业管理员
         */
        public static Long FIRM_SYSTEM = 3L;
        /**
         * AI顾问
         */
        public static Long AI = 4L;
        /**
         * 置业顾问
         */
        public static Long HOME = 5L;
    }

}

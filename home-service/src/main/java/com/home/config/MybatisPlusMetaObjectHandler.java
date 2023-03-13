package com.home.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.home.common.utils.BeanEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动补充插入或更新时的值，起始版本 3.3.0(推荐使用)
 * @author xiewei
 */
@Slf4j
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    private final static String CREATE_TIME = "createTime";
    private final static String IS_DELETE = "isDelete";
    private final static String UPDATE_TIME = "updateTime";

    /**
     * 插入填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("==========start insert fill ....");
        this.strictInsertFill(metaObject, CREATE_TIME, Date.class, new Date());
        this.strictInsertFill(metaObject, IS_DELETE, Integer.class, 1);
        this.strictInsertFill(metaObject, UPDATE_TIME, Date.class, new Date());
    }

    /**
     * 更新填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("=============start update fill ....");
        if (metaObject.getValue(UPDATE_TIME) != null) {
            metaObject.setValue(UPDATE_TIME, null);
        }
        this.strictUpdateFill(metaObject, UPDATE_TIME, Date.class, new Date());
    }
}

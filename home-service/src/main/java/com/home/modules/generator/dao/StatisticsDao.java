package com.home.modules.generator.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiewei
 * @date 2020/8/5 16:09
 */
@Mapper
public interface StatisticsDao {

    /**
     * 查询在线人数
     * @param type day/month/year 日、月、年
     * @return
     */
    Integer getOnlineCount(String type);

    /**
     * 查询转发数
     * @param type day/month/year 日、月、年
     * @return
     */
    Integer getForwardCount(String type);

    /**
     * 查询点赞数
     * @param type day/month/year 日、月、年
     * @return
     */
    Integer getLikeCount(String type);

    /**
     * 查询评论数
     * @param type day/month/year 日、月、年
     * @return
     */
    Integer getCommentCount(String type);

    /**
     * 查询收藏数
     * @param type day/month/year 日、月、年
     * @return
     */
    Integer getCollectCount(String type);

    /**
     * 查询发布购房需求数
     * @param type day/month/year 日、月、年
     * @return
     */
    Integer getDemandCount(String type);

    /**
     * 查询测评数
     * @param type day/month/year 日、月、年
     * @return
     */
    Integer getEvaluationCount(String type);

    /**
     * 查询快讯数
     * @param type day/month/year 日、月、年
     * @return
     */
    Integer getNewsCount(String type);
}

package com.home.modules.job.task;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务(演示Demo，可删除)
 *
 * testTask为spring bean的名称
 *
 * @author xw
 */
@Slf4j
@Component("testTask")
public class TestTask implements ITask {

	@Override
	public void run(String params){
		log.info("TestTask定时任务正在执行，参数为：{}", params);
	}
}

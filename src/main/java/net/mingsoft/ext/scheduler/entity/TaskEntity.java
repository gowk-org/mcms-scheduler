package net.mingsoft.ext.scheduler.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.mingsoft.base.entity.BaseEntity;
import java.util.Date;
/**
 * 任务实体
 *
 * @author z
 * 创建日期：2020-4-2 17:12:52
 * 历史修订：
 * @version $Id: $Id
 */
@Data
public class TaskEntity extends BaseEntity {

private static final long serialVersionUID = 1585818772214L;

	/**
	* 任务名称
	*/
	private String jobName;
	/**
	* 组名称
	*/
	private String jobGroup;
	/**
	* CRON表达式
	*/
	private String jobCron;
	/**
	* 是否启用
	*/
	private Integer jobState;

}

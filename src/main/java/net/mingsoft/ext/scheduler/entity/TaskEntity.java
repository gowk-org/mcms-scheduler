package net.mingsoft.ext.scheduler.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.mingsoft.base.entity.BaseEntity;
import java.util.Date;
/**
* 任务实体
* @author z
* 创建日期：2020-4-2 17:12:52<br/>
* 历史修订：<br/>
*/
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


	/**
	* 设置任务名称
	*/
	public void setJobName(String jobName) {
	this.jobName = jobName;
	}

	/**
	* 获取任务名称
	*/
	public String getJobName() {
	return this.jobName;
	}
	/**
	* 设置组名称
	*/
	public void setJobGroup(String jobGroup) {
	this.jobGroup = jobGroup;
	}

	/**
	* 获取组名称
	*/
	public String getJobGroup() {
	return this.jobGroup;
	}
	/**
	* 设置CRON表达式
	*/
	public void setJobCron(String jobCron) {
	this.jobCron = jobCron;
	}

	/**
	* 获取CRON表达式
	*/
	public String getJobCron() {
	return this.jobCron;
	}
	/**
	* 设置是否启用
	*/
	public void setJobState(Integer jobState) {
	this.jobState = jobState;
	}

	/**
	* 获取是否启用
	*/
	public Integer getJobState() {
	return this.jobState;
	}
}
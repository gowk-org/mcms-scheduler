package net.mingsoft.ext.scheduler.action;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.hutool.core.util.ObjectUtil;
import java.util.*;

import com.alibaba.fastjson.JSON;
import net.mingsoft.base.entity.ResultData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.*;
import net.mingsoft.ext.scheduler.biz.ITaskBiz;
import net.mingsoft.ext.scheduler.entity.TaskEntity;
import net.mingsoft.base.util.JSONObject;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.base.filter.DateValueFilter;
import net.mingsoft.base.filter.DoubleValueFilter;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
/**
 * 任务管理控制层
 *
 * @author z
 * 创建日期：2020-4-2 17:12:52
 * 历史修订：
 * @version $Id: $Id
 */
@Api(value = "任务接口")
@Controller("schedulerTaskAction")
@RequestMapping("/${ms.manager.path}/scheduler/task")
public class TaskAction extends net.mingsoft.ext.scheduler.action.BaseAction{


	/**
	 * 注入任务业务层
	 */
	@Autowired
	private ITaskBiz taskBiz;

	/**
	 * 返回主界面index
	 *
	 * @param response a {@link javax.servlet.http.HttpServletResponse} object.
	 * @param request a {@link javax.servlet.http.HttpServletRequest} object.
	 * @return a {@link java.lang.String} object.
	 */
	@GetMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request){
		return "/scheduler/task/index";
	}

	/**
	 * 查询任务列表
	 *
	 * @param task 任务实体
	 * @param response a {@link javax.servlet.http.HttpServletResponse} object.
	 * @param request a {@link javax.servlet.http.HttpServletRequest} object.
	 * @param model a {@link org.springframework.ui.ModelMap} object.
	 * @param result a {@link org.springframework.validation.BindingResult} object.
	 * @return a {@link net.mingsoft.base.entity.ResultData} object.
	 */
	@ApiOperation(value = "查询任务列表接口")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "jobName", value = "任务名称", required =false,paramType="query"),
    	@ApiImplicitParam(name = "jobGroup", value = "组名称", required =false,paramType="query"),
    	@ApiImplicitParam(name = "jobCron", value = "CRON表达式", required =false,paramType="query"),
    	@ApiImplicitParam(name = "jobState", value = "是否启用", required =false,paramType="query"),
    	@ApiImplicitParam(name = "createBy", value = "创建人", required =false,paramType="query"),
    	@ApiImplicitParam(name = "createDate", value = "创建时间", required =false,paramType="query"),
    	@ApiImplicitParam(name = "updateBy", value = "修改人", required =false,paramType="query"),
    	@ApiImplicitParam(name = "updateDate", value = "修改时间", required =false,paramType="query"),
    	@ApiImplicitParam(name = "del", value = "删除标记", required =false,paramType="query"),
    	@ApiImplicitParam(name = "id", value = "编号", required =false,paramType="query"),
    })
	@RequestMapping(value ="/list",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ResultData list(@ModelAttribute @ApiIgnore TaskEntity task,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model,BindingResult result) {
		BasicUtil.startPage();
		List taskList = taskBiz.query(task);
		return ResultData.build().success(new EUListBean(taskList,(int)BasicUtil.endPage(taskList).getTotal()));
	}

	/**
	 * 返回编辑界面task_form
	 *
	 * @param task a {@link net.mingsoft.ext.scheduler.entity.TaskEntity} object.
	 * @param response a {@link javax.servlet.http.HttpServletResponse} object.
	 * @param request a {@link javax.servlet.http.HttpServletRequest} object.
	 * @param model a {@link org.springframework.ui.ModelMap} object.
	 * @return a {@link java.lang.String} object.
	 */
	@GetMapping("/form")
	public String form(@ModelAttribute TaskEntity task,HttpServletResponse response,HttpServletRequest request,ModelMap model){
		if(task.getId()!=null){
			BaseEntity taskEntity = taskBiz.getEntity(Integer.parseInt(task.getId()));
			model.addAttribute("taskEntity",taskEntity);
		}
		return "/scheduler/task/form";
	}
	/**
	 * 获取任务
	 *
	 * @param task 任务实体
	 * @param response a {@link javax.servlet.http.HttpServletResponse} object.
	 * @param request a {@link javax.servlet.http.HttpServletRequest} object.
	 * @param model a {@link org.springframework.ui.ModelMap} object.
	 * @return a {@link net.mingsoft.base.entity.ResultData} object.
	 */
	@ApiOperation(value = "获取任务列表接口")
    @ApiImplicitParam(name = "id", value = "编号", required =true,paramType="query")
	@GetMapping("/get")
	@ResponseBody
	public ResultData get(@ModelAttribute @ApiIgnore TaskEntity task,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
		if(task.getId()==null) {
			return ResultData.build().error();
		}
		TaskEntity _task = (TaskEntity)taskBiz.getEntity(Integer.parseInt(task.getId()));
		return ResultData.build().success(_task);
	}

	@ApiOperation(value = "保存任务列表接口")
	 @ApiImplicitParams({
		@ApiImplicitParam(name = "jobName", value = "任务名称", required =false,paramType="query"),
		@ApiImplicitParam(name = "jobGroup", value = "组名称", required =false,paramType="query"),
		@ApiImplicitParam(name = "jobCron", value = "CRON表达式", required =false,paramType="query"),
		@ApiImplicitParam(name = "jobState", value = "是否启用", required =false,paramType="query"),
		@ApiImplicitParam(name = "createBy", value = "创建人", required =false,paramType="query"),
		@ApiImplicitParam(name = "createDate", value = "创建时间", required =false,paramType="query"),
		@ApiImplicitParam(name = "updateBy", value = "修改人", required =false,paramType="query"),
		@ApiImplicitParam(name = "updateDate", value = "修改时间", required =false,paramType="query"),
		@ApiImplicitParam(name = "del", value = "删除标记", required =false,paramType="query"),
		@ApiImplicitParam(name = "id", value = "编号", required =false,paramType="query"),
	})

	/**
	 * 保存任务
	 *
	 * @param task 任务实体
	 * @param response a {@link javax.servlet.http.HttpServletResponse} object.
	 * @param request a {@link javax.servlet.http.HttpServletRequest} object.
	 * @return a {@link net.mingsoft.base.entity.ResultData} object.
	 */
	@PostMapping("/save")
	@ResponseBody
	@LogAnn(title = "保存任务", businessType = BusinessTypeEnum.INSERT)
	@RequiresPermissions("scheduler:task:save")
	public ResultData save(@ModelAttribute @ApiIgnore TaskEntity task, HttpServletResponse response, HttpServletRequest request) {
		if(!StringUtil.checkLength(task.getJobName()+"", 1, 10)){
			return ResultData.build().error(getResString("err.length", this.getResString("job.name"), "1", "10"));
		}
		if(!StringUtil.checkLength(task.getJobGroup()+"", 1, 10)){
			return ResultData.build().error(getResString("err.length", this.getResString("job.group"), "1", "10"));
		}
		if(!StringUtil.checkLength(task.getJobCron()+"", 1, 10)){
			return ResultData.build().error(getResString("err.length", this.getResString("job.cron"), "1", "10"));
		}
		taskBiz.saveEntity(task);
		return ResultData.build().success(task);
	}

	/**
	 * <p>delete.</p>
	 *
	 * @param tasks a {@link java.util.List} object.
	 * @param response a {@link javax.servlet.http.HttpServletResponse} object.
	 * @param request a {@link javax.servlet.http.HttpServletRequest} object.
	 * @return a {@link net.mingsoft.base.entity.ResultData} object.
	 */
	@ApiOperation(value = "批量删除任务列表接口")
	@PostMapping("/delete")
	@ResponseBody
	@LogAnn(title = "删除任务", businessType = BusinessTypeEnum.DELETE)
	@RequiresPermissions("scheduler:task:del")
	public ResultData delete(@RequestBody List<TaskEntity> tasks,HttpServletResponse response, HttpServletRequest request) {
		int[] ids = new int[tasks.size()];
		for(int i = 0;i<tasks.size();i++){
			ids[i] =Integer.parseInt(tasks.get(i).getId()) ;
		}
		taskBiz.delete(ids);
		return ResultData.build().success();
	}

	/**
	 *	更新任务列表
	 *
	 * @param task 任务实体
	 * @param response a {@link javax.servlet.http.HttpServletResponse} object.
	 * @param request a {@link javax.servlet.http.HttpServletRequest} object.
	 * @return a {@link net.mingsoft.base.entity.ResultData} object.
	 */
	 @ApiOperation(value = "更新任务列表接口")
	 @ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "编号", required =true,paramType="query"),
		@ApiImplicitParam(name = "jobName", value = "任务名称", required =false,paramType="query"),
		@ApiImplicitParam(name = "jobGroup", value = "组名称", required =false,paramType="query"),
		@ApiImplicitParam(name = "jobCron", value = "CRON表达式", required =false,paramType="query"),
		@ApiImplicitParam(name = "jobState", value = "是否启用", required =false,paramType="query"),
		@ApiImplicitParam(name = "createBy", value = "创建人", required =false,paramType="query"),
		@ApiImplicitParam(name = "createDate", value = "创建时间", required =false,paramType="query"),
		@ApiImplicitParam(name = "updateBy", value = "修改人", required =false,paramType="query"),
		@ApiImplicitParam(name = "updateDate", value = "修改时间", required =false,paramType="query"),
		@ApiImplicitParam(name = "del", value = "删除标记", required =false,paramType="query"),
		@ApiImplicitParam(name = "id", value = "编号", required =false,paramType="query"),
	})
	@PostMapping("/update")
	@ResponseBody
	@LogAnn(title = "更新任务", businessType = BusinessTypeEnum.UPDATE)
	@RequiresPermissions("scheduler:task:update")
	public ResultData update(@ModelAttribute @ApiIgnore TaskEntity task, HttpServletResponse response,
			HttpServletRequest request) {
		if(!StringUtil.checkLength(task.getJobName()+"", 1, 10)){
			return ResultData.build().error(getResString("err.length", this.getResString("job.name"), "1", "10"));
		}
		if(!StringUtil.checkLength(task.getJobGroup()+"", 1, 10)){
			return ResultData.build().error(getResString("err.length", this.getResString("job.group"), "1", "10"));
		}
		if(!StringUtil.checkLength(task.getJobCron()+"", 1, 10)){
			return ResultData.build().error(getResString("err.length", this.getResString("job.cron"), "1", "10"));
		}
		taskBiz.updateEntity(task);
		return ResultData.build().success(task);
	}

}

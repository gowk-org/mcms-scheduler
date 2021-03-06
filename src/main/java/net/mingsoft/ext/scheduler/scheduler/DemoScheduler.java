package net.mingsoft.ext.scheduler.scheduler;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import net.mingsoft.ext.scheduler.biz.ITaskBiz;
import net.mingsoft.ext.scheduler.entity.TaskEntity;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * <p>DemoScheduler class.</p>
 *
 * @author user1
 * @version $Id: $Id
 */
@Component
public class DemoScheduler implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DemoScheduler.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    @Autowired
    ITaskBiz taskBiz;


    /** {@inheritDoc} */
    @Override
    public void run(String...args) throws Exception {

        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.clear();
        Map<JobDetail, Trigger> jobTriggerMap = getJobDetailTriggerMap();

        jobTriggerMap.forEach((job, trigger) -> {
            try {
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                LOG.info(e.getMessage());
            }
        });
    }

    private Map<JobDetail, Trigger> getJobDetailTriggerMap() {
        Map<JobDetail, Trigger> jobTriggerMap = new HashMap<>();
TaskEntity taskEntity=new TaskEntity();
taskEntity.setJobState(1);

        Map<String,String> wheres=new HashMap<>();
        wheres.put("job_state","1");
        List<String> fields=new ArrayList<>();
        fields.add("job_name");
        fields.add("job_cron");
        fields.add("job_group");
        List<Map> list = taskBiz.queryBySQL("ext_task",fields,wheres);
        for(Map map:list){
            Class aClass = null;
            try {
                String json=JSONUtils.toJSONString(map);
                TaskEntity task=JSON.parseObject(json,TaskEntity.class);
                aClass = Class.forName(task.getJobName());
                String jobName=task.getJobGroup()+"/"+task.getJobName();
                jobTriggerMap.putAll(createJobWithTrigger(aClass,jobName,new HashMap<>(),jobName,task.getJobCron()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
       /* jobTriggerMap.putAll(createJobWithTrigger(HelloJob.class,
                "JobGroup/Job1", ImmutableMap.of("message", "A"),
                "TriggerGroup/Trigger1", "0/5 * * * * ?"));*/
/*        jobTriggerMap.putAll(createJobWithTrigger(HelloJob.class,
                "JobGroup/Job2", ImmutableMap.of("message", "B"),
                "TriggerGroup/Trigger2", "0/10 * * * * ?"));
        jobTriggerMap.putAll(createJobWithTrigger(HelloJob.class,
                "JobGroup/Job3", ImmutableMap.of("message", "C"),
                "TriggerGroup/Trigger3", "0/15 * * * * ?"));
        jobTriggerMap.putAll(createJobWithTrigger(HelloJob.class,
                "JobGroup/Job4", ImmutableMap.of("message", "D"),
                "TriggerGroup/Trigger4", "0/20 * * * * ?"));
        jobTriggerMap.putAll(createJobWithTrigger(HelloJob.class,
                "JobGroup/Job5", ImmutableMap.of("message", "E"),
                "TriggerGroup/Trigger5", "0/25 * * * * ?"));*/

        return jobTriggerMap;
    }

    private Map<JobDetail,Trigger> createJobWithTrigger(Class <? extends Job> jobClass, String jobName,
                                                         Map<String, String> jobData, String triggerName,
                                                         String scheduleTime) {
        JobBuilder jobBuilder = newJob(jobClass).withIdentity(jobName.split("/")[1], jobName.split("/")[0]);
        jobData.forEach((k, v) -> jobBuilder.usingJobData(k, v) );
        JobDetail job = jobBuilder.build();
        Trigger trigger = newTrigger().withIdentity(triggerName.split("/")[1], triggerName.split("/")[0])
                .withSchedule(cronSchedule(scheduleTime)).build();
        return ImmutableMap.of(job, trigger);
    }

}

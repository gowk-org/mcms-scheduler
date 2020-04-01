package net.mingsoft.ext.scheduler.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class HelloJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(HelloJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        String groupName = context.getJobDetail().getKey().getGroup();
        String jobName = context.getJobDetail().getKey().getName();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String message = dataMap.containsKey("message") ? dataMap.getString("message") : "NIL";
        Date now = new Date();
        LOG.info("Hello [" + groupName + ":" + jobName + "] " + message + " -> " + now.toString());
    }
}

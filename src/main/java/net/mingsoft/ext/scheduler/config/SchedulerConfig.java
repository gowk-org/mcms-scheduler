package net.mingsoft.ext.scheduler.config;

import javax.sql.DataSource;

import net.mingsoft.ext.scheduler.job.HelloJob;
import net.mingsoft.ext.scheduler.spring.AutowiringSpringBeanJobFactory;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>SchedulerConfig class.</p>
 *
 * @author user1
 * @version $Id: $Id
 */
@Configuration
//@ConditionalOnProperty(name = "quartz.enabled")
public class SchedulerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerConfig.class);

    /**
     * <p>jobFactory.</p>
     *
     * @param applicationContext a {@link org.springframework.context.ApplicationContext} object.
     * @return a {@link org.quartz.spi.JobFactory} object.
     */
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * <p>schedulerFactoryBean.</p>
     *
     * @param dataSource a {@link javax.sql.DataSource} object.
     * @param jobFactory a {@link org.quartz.spi.JobFactory} object.
     * @param simpleJobTrigger a {@link org.quartz.Trigger} object.
     * @return a {@link org.springframework.scheduling.quartz.SchedulerFactoryBean} object.
     * @throws java.io.IOException if any.
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory,
                                                     @Qualifier("simpleJobTrigger") Trigger simpleJobTrigger)
            throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());

        return factory;
    }

    /**
     * <p>quartzProperties.</p>
     *
     * @return a {@link java.util.Properties} object.
     * @throws java.io.IOException if any.
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     * <p>simpleJobDetail.</p>
     *
     * @return a {@link org.springframework.scheduling.quartz.JobDetailFactoryBean} object.
     */
    @Bean
    public JobDetailFactoryBean simpleJobDetail() {
        return createJobDetail(HelloJob.class);
    }

    /**
     * <p>simpleJobTrigger.</p>
     *
     * @param jobDetail a {@link org.quartz.JobDetail} object.
     * @return a {@link org.springframework.scheduling.quartz.SimpleTriggerFactoryBean} object.
     */
    @Bean(name = "simpleJobTrigger")
    public SimpleTriggerFactoryBean simpleJobTrigger(@Qualifier("simpleJobDetail") JobDetail jobDetail) {
        long frequency=2000;
        return createTrigger(jobDetail, frequency);
    }

    private static JobDetailFactoryBean createJobDetail(Class jobClass) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        // job has to be durable to be stored in DB:
        factoryBean.setDurability(true);
        return factoryBean;
    }

    private static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, long pollFrequencyMs) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        // in case of misfire, ignore all missed triggers and continue :
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }

    // Use this method for creating cron triggers instead of simple triggers:
    private static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        return factoryBean;
    }

}

package net.mingsoft.ext.scheduler;

import net.mingsoft.ext.scheduler.config.SchedulerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SchedulerConfig.class})
public class QuartzSpringbootMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuartzSpringbootMysqlApplication.class, args);
	}
}

package net.mingsoft.ext.scheduler;

import net.mingsoft.ext.scheduler.config.SchedulerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * <p>QuartzSpringbootMysqlApplication class.</p>
 *
 * @author user1
 * @version $Id: $Id
 */
@SpringBootApplication
@Import({SchedulerConfig.class})
public class QuartzSpringbootMysqlApplication {

	/**
	 * <p>main.</p>
	 *
	 * @param args an array of {@link java.lang.String} objects.
	 */
	public static void main(String[] args) {
		SpringApplication.run(QuartzSpringbootMysqlApplication.class, args);
	}
}

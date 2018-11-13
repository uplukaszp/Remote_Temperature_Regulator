package pl.uplukaszp.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//https://dzone.com/articles/spring-and-quartz-integration-that-works-together
@Configuration
public class QuartzConfig {

	@Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(@Autowired DataSource dataSource, @Autowired JobFactory jobFactory)
			throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setOverwriteExistingJobs(true);
		factory.setAutoStartup(true);
		factory.setDataSource(dataSource);
		// This is the place where we will wire Quartz and Spring together
		factory.setJobFactory(jobFactory);
		// factory.setTriggers(triggers);
		return factory;
	}
}

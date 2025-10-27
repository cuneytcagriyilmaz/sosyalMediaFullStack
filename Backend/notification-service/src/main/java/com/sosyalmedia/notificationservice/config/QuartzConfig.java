package com.sosyalmedia.notificationservice.config;

import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

    /**
     * Quartz Job Factory (Spring bean injection için)
     */
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * Scheduler Factory Bean
     * (Quartz properties artık application.yml'den alınıyor)
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
            DataSource dataSource,
            JobFactory jobFactory
    ) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        // Properties Config Server'dan otomatik geliyor
        return factory;
    }
}
package nl.craftsmen.quartzdemo;

import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.Collections;
import java.util.HashSet;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class QuartzSchedule {

    private Scheduler scheduler;

    @PostConstruct
    public void schedule() throws SchedulerException {
        Trigger trigger;
        SchedulerFactory stdSchedulerFactory = new org.quartz.impl.StdSchedulerFactory();
        scheduler = stdSchedulerFactory.getScheduler();
        JobDetail job;
        // define the job and tie it to our HelloJob class
        job = newJob(HelloJob.class)
                .withIdentity("myHelloJob", "group1")
                .build();

        // Trigger the job to run now, and then every 4 seconds
        trigger = newTrigger()
                .withIdentity("myHelloTrigger", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(4)
                        .repeatForever())
                .build();
        scheduler.scheduleJob(job, new HashSet<Trigger>(Collections.singleton(trigger)), true);
        // Tell quartz to schedule the job using our trigger
        scheduler.start();


    }

    @PreDestroy
    public void stop() throws SchedulerException {
        scheduler.shutdown();
    }
}

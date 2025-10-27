package com.sosyalmedia.notificationservice.service.impl;

import com.sosyalmedia.notificationservice.exception.SchedulerException;
import com.sosyalmedia.notificationservice.service.JobManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobManagementServiceImpl implements JobManagementService {

    private final Scheduler scheduler;

    @Override
    public List<Map<String, Object>> getAllJobs() {
        try {
            List<Map<String, Object>> jobsList = new ArrayList<>();

            // Tüm job gruplarını al
            for (String groupName : scheduler.getJobGroupNames()) {
                // Gruptaki tüm job'ları al
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

                    Map<String, Object> jobInfo = new HashMap<>();
                    jobInfo.put("jobName", jobKey.getName());
                    jobInfo.put("jobGroup", jobKey.getGroup());
                    jobInfo.put("jobClass", jobDetail.getJobClass().getSimpleName());
                    jobInfo.put("description", jobDetail.getDescription());

                    if (!triggers.isEmpty()) {
                        Trigger trigger = triggers.get(0);
                        jobInfo.put("triggerState", scheduler.getTriggerState(trigger.getKey()).name());

                        if (trigger instanceof CronTrigger) {
                            jobInfo.put("cronExpression", ((CronTrigger) trigger).getCronExpression());
                        }

                        jobInfo.put("nextFireTime", trigger.getNextFireTime());
                        jobInfo.put("previousFireTime", trigger.getPreviousFireTime());
                    }

                    jobsList.add(jobInfo);
                }
            }

            return jobsList;

        } catch (org.quartz.SchedulerException e) {
            log.error("❌ Job listesi alınamadı: {}", e.getMessage(), e);
            throw new SchedulerException("Job listesi alınamadı", e);
        }
    }

    @Override
    public void triggerJobManually(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);

            if (!scheduler.checkExists(jobKey)) {
                throw new SchedulerException("Job bulunamadı: " + jobName);
            }

            scheduler.triggerJob(jobKey);
            log.info("▶️ Job manuel olarak tetiklendi: {}", jobName);

        } catch (org.quartz.SchedulerException e) {
            log.error("❌ Job tetiklenemedi: {}", e.getMessage(), e);
            throw new SchedulerException("Job tetiklenemedi: " + jobName, e);
        }
    }

    @Override
    public void pauseJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);

            if (!scheduler.checkExists(jobKey)) {
                throw new SchedulerException("Job bulunamadı: " + jobName);
            }

            scheduler.pauseJob(jobKey);
            log.info("⏸️ Job duraklatıldı: {}", jobName);

        } catch (org.quartz.SchedulerException e) {
            log.error("❌ Job duraklatılamadı: {}", e.getMessage(), e);
            throw new SchedulerException("Job duraklatılamadı: " + jobName, e);
        }
    }

    @Override
    public void resumeJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);

            if (!scheduler.checkExists(jobKey)) {
                throw new SchedulerException("Job bulunamadı: " + jobName);
            }

            scheduler.resumeJob(jobKey);
            log.info("▶️ Job devam ettirildi: {}", jobName);

        } catch (org.quartz.SchedulerException e) {
            log.error("❌ Job devam ettirilemedi: {}", e.getMessage(), e);
            throw new SchedulerException("Job devam ettirilemedi: " + jobName, e);
        }
    }

    @Override
    public void deleteJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);

            if (!scheduler.checkExists(jobKey)) {
                throw new SchedulerException("Job bulunamadı: " + jobName);
            }

            scheduler.deleteJob(jobKey);
            log.info("🗑️ Job silindi: {}", jobName);

        } catch (org.quartz.SchedulerException e) {
            log.error("❌ Job silinemedi: {}", e.getMessage(), e);
            throw new SchedulerException("Job silinemedi: " + jobName, e);
        }
    }

    @Override
    public Map<String, Object> getJobStatus(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);

            if (!scheduler.checkExists(jobKey)) {
                throw new SchedulerException("Job bulunamadı: " + jobName);
            }

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

            Map<String, Object> status = new HashMap<>();
            status.put("jobName", jobKey.getName());
            status.put("jobGroup", jobKey.getGroup());
            status.put("jobClass", jobDetail.getJobClass().getSimpleName());
            status.put("description", jobDetail.getDescription());

            if (!triggers.isEmpty()) {
                Trigger trigger = triggers.get(0);
                status.put("triggerState", scheduler.getTriggerState(trigger.getKey()).name());

                if (trigger instanceof CronTrigger) {
                    status.put("cronExpression", ((CronTrigger) trigger).getCronExpression());
                }

                status.put("nextFireTime", trigger.getNextFireTime());
                status.put("previousFireTime", trigger.getPreviousFireTime());
            }

            return status;

        } catch (org.quartz.SchedulerException e) {
            log.error("❌ Job durumu alınamadı: {}", e.getMessage(), e);
            throw new SchedulerException("Job durumu alınamadı: " + jobName, e);
        }
    }
}
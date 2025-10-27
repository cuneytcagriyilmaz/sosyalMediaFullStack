package com.sosyalmedia.notificationservice.service;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

import java.util.List;
import java.util.Map;

public interface JobManagementService {

    /**
     * Tüm job'ları listele
     */
    List<Map<String, Object>> getAllJobs();

    /**
     * Job'u manuel çalıştır
     */
    void triggerJobManually(String jobName, String jobGroup);

    /**
     * Job'u duraklat
     */
    void pauseJob(String jobName, String jobGroup);

    /**
     * Job'u devam ettir
     */
    void resumeJob(String jobName, String jobGroup);

    /**
     * Job'u sil
     */
    void deleteJob(String jobName, String jobGroup);

    /**
     * Job durumunu getir
     */
    Map<String, Object> getJobStatus(String jobName, String jobGroup);
}
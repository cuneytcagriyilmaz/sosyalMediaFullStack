package com.sosyalmedia.notificationservice.config;


 import com.sosyalmedia.notificationservice.model.SchedulerSettings;
import com.sosyalmedia.notificationservice.repository.SchedulerSettingsRepository;
import com.sosyalmedia.notificationservice.service.job.CheckOverduePostsJob;
import com.sosyalmedia.notificationservice.service.job.CheckSpecialDatesJob;
import com.sosyalmedia.notificationservice.service.job.CheckUpcomingPostsJob;
 import com.sosyalmedia.notificationservice.service.job.SendEmailNotificationsJob;
 import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobSchedulerConfig {

    private final Scheduler scheduler;
    private final SchedulerSettingsRepository schedulerSettingsRepository;

    @PostConstruct
    public void scheduleJobs() {
        log.info("⚙️ Quartz Job'lar kaydediliyor...");

        try {
            // 1. CheckUpcomingPostsJob
            scheduleJob(
                    CheckUpcomingPostsJob.class,
                    "checkUpcomingPostsJob",
                    "notificationJobs",
                    "CHECK_UPCOMING_POSTS",
                    "0 0 9 * * ?" // Default: Her gün 09:00
            );

            // 2. CheckOverduePostsJob
            scheduleJob(
                    CheckOverduePostsJob.class,
                    "checkOverduePostsJob",
                    "notificationJobs",
                    "CHECK_OVERDUE_POSTS",
                    "0 0 10 * * ?" // Default: Her gün 10:00
            );

            // 3. CheckSpecialDatesJob
            scheduleJob(
                    CheckSpecialDatesJob.class,
                    "checkSpecialDatesJob",
                    "notificationJobs",
                    "CHECK_SPECIAL_DATES",
                    "0 0 8 * * ?" // Default: Her gün 08:00
            );

            // 4. SendEmailNotificationsJob
            scheduleJob(
                    SendEmailNotificationsJob.class,
                    "sendEmailNotificationsJob",
                    "notificationJobs",
                    "SEND_EMAIL_NOTIFICATIONS",
                    "0 */30 * * * ?" // Default: Her 30 dakikada bir
            );

            log.info("✅ Tüm Job'lar başarıyla kaydedildi");

        } catch (Exception e) {
            log.error("❌ Job kayıt hatası: {}", e.getMessage(), e);
        }
    }

    /**
     * Job'u schedule et
     */
    private void scheduleJob(
            Class<? extends Job> jobClass,
            String jobName,
            String jobGroup,
            String settingKey,
            String defaultCronExpression
    ) throws SchedulerException {

        // SchedulerSettings'den cron expression al
        Optional<SchedulerSettings> settingOpt = schedulerSettingsRepository.findBySettingKey(settingKey);

        String cronExpression = defaultCronExpression;
        boolean isActive = true;

        if (settingOpt.isPresent()) {
            SchedulerSettings setting = settingOpt.get();
            cronExpression = setting.getCronExpression();
            isActive = setting.getIsActive();

            log.info("📅 {} - Cron: {} - Aktif: {}", jobName, cronExpression, isActive);
        } else {
            log.warn("⚠️ {} için ayar bulunamadı, default cron kullanılıyor: {}",
                    settingKey, defaultCronExpression);
        }

        // Job aktif değilse kaydetme
        if (!isActive) {
            log.info("⏸️ {} pasif, kaydedilmedi", jobName);
            return;
        }

        // JobDetail oluştur
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .storeDurably()
                .build();

        // Trigger oluştur
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + "Trigger", jobGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        // Job'u schedule et
        if (scheduler.checkExists(jobDetail.getKey())) {
            log.info("🔄 {} zaten mevcut, yeniden planlanıyor...", jobName);
            scheduler.rescheduleJob(trigger.getKey(), trigger);
        } else {
            log.info("➕ {} ekleniyor...", jobName);
            scheduler.scheduleJob(jobDetail, trigger);
        }

        log.info("✅ {} başarıyla kaydedildi", jobName);
    }
}
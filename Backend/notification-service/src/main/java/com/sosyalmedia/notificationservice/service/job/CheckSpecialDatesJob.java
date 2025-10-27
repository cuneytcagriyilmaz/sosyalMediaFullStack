package com.sosyalmedia.notificationservice.service.job; // ✅ DOĞRU PACKAGE

import com.sosyalmedia.notificationservice.client.AnalyticsServiceClient; // ✅ DÜZELT
import com.sosyalmedia.notificationservice.client.CustomerServiceClient;
import com.sosyalmedia.notificationservice.dto.external.ActivityDTO;
import com.sosyalmedia.notificationservice.dto.external.CustomerDTO;
import com.sosyalmedia.notificationservice.model.GlobalSpecialDate;
import com.sosyalmedia.notificationservice.model.Notification;
import com.sosyalmedia.notificationservice.model.NotificationSettings;
import com.sosyalmedia.notificationservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckSpecialDatesJob implements Job {

    private final CalendarificService calendarificService;
    private final NotificationService notificationService;
    private final NotificationSettingsService notificationSettingsService;
    private final EmailService emailService;
    private final CustomerServiceClient customerServiceClient;
    private final AnalyticsServiceClient analyticsServiceClient; // ✅ DÜZELT

    @Override
    public void execute(JobExecutionContext context) {
        log.info("⏰ CheckSpecialDatesJob çalışıyor...");

        try {
            NotificationSettings settings = notificationSettingsService
                    .getSettingByKey("SPECIAL_DATE_REMINDERS");

            if (!settings.getIsActive()) {
                log.info("⚠️ Özel gün hatırlatmaları devre dışı");
                return;
            }

            List<Integer> reminderDays = settings.getReminderDays();
            int totalNotifications = 0;

            List<CustomerDTO> customers = customerServiceClient.getCustomersWithSpecialDayPostsEnabled();

            log.info("📅 {} müşteri için özel gün kontrolü yapılıyor", customers.size());

            for (Integer days : reminderDays) {
                LocalDate targetDate = LocalDate.now().plusDays(days);

                List<GlobalSpecialDate> specialDates = calendarificService
                        .getUpcomingHolidays(days + 1)
                        .stream()
                        .filter(sd -> sd.getDateValue().equals(targetDate))
                        .collect(Collectors.toList());

                log.info("📅 {} gün sonrası için {} özel gün bulundu", days, specialDates.size());

                for (GlobalSpecialDate specialDate : specialDates) {
                    for (CustomerDTO customer : customers) {
                        boolean hasSelectedThisDate = customer.getSpecialDayPreferences() != null
                                && customer.getSpecialDayPreferences().stream()
                                .anyMatch(pref -> pref.getSpecialDateId().equals(specialDate.getId())
                                        && pref.getIsEnabled());

                        if (!hasSelectedThisDate) {
                            continue;
                        }

                        Notification notification = createNotification(specialDate, customer, days);
                        notificationService.createNotification(notification);

                        sendEmailNotification(specialDate, customer, days);
                        saveActivity(customer.getId(), specialDate, days);

                        totalNotifications++;
                    }
                }
            }

            log.info("✅ CheckSpecialDatesJob tamamlandı - {} bildirim gönderildi", totalNotifications);

        } catch (Exception e) {
            log.error("❌ CheckSpecialDatesJob hatası: {}", e.getMessage(), e);
        }
    }

    private Notification createNotification(GlobalSpecialDate specialDate, CustomerDTO customer, int daysRemaining) {
        String title = String.format("🎉 [%s] %d Gün Sonra: %s",
                customer.getCompanyName(), daysRemaining, specialDate.getDateName());

        String message = String.format(
                "%s %d gün sonra (%s). %s müşteriniz için özel içerik hazırlamayı unutmayın!",
                specialDate.getDateName(),
                daysRemaining,
                specialDate.getDateValue(),
                customer.getCompanyName()
        );

        return Notification.builder()
                .customerId(customer.getId())
                .specialDateId(specialDate.getId())
                .notificationType("SPECIAL_DATE_REMINDER")
                .title(title)
                .message(message)
                .icon(specialDate.getIcon())
                .severity("INFO")
                .isRead(false)
                .emailSent(false)
                .emailStatus("PENDING")
                .build();
    }

    private void sendEmailNotification(GlobalSpecialDate specialDate, CustomerDTO customer, int daysRemaining) {
        try {
            String recipientEmail = customer.getPrimaryContact().getEmail();

            Map<String, String> variables = new HashMap<>();
            variables.put("companyName", customer.getCompanyName());
            variables.put("contactName", customer.getPrimaryContact().getFullName());
            variables.put("specialDateName", specialDate.getDateName());
            variables.put("specialDate", specialDate.getDateValue().toString());
            variables.put("daysRemaining", String.valueOf(daysRemaining));
            variables.put("icon", specialDate.getIcon());
            variables.put("description", specialDate.getDescription() != null ? specialDate.getDescription() : "");
            variables.put("customerList", customer.getCompanyName());

            emailService.sendTemplatedEmail(recipientEmail, "SPECIAL_DATE_REMINDER", variables);

            log.info("📧 Özel gün e-postası gönderildi: {} - {}",
                    customer.getCompanyName(), specialDate.getDateName());

        } catch (Exception e) {
            log.error("❌ E-posta gönderilemedi: {}", e.getMessage());
        }
    }

    private void saveActivity(Long customerId, GlobalSpecialDate specialDate, int daysRemaining) {
        try {
            ActivityDTO activity = ActivityDTO.builder()
                    .customerId(customerId)
                    .activityType("SPECIAL_DATE_REMINDER")
                    .message(String.format("Özel gün hatırlatması: %s (%d gün kaldı)",
                            specialDate.getDateName(), daysRemaining))
                    .icon(specialDate.getIcon())
                    .activityDate(LocalDateTime.now())
                    .build();

            analyticsServiceClient.createActivity(activity); // ✅ DÜZELT
        } catch (Exception e) {
            log.warn("⚠️ Activity kaydedilemedi: {}", e.getMessage());
        }
    }
}
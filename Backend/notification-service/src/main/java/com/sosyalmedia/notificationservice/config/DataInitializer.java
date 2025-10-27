package com.sosyalmedia.notificationservice.config;

import com.sosyalmedia.notificationservice.model.EmailTemplate;
import com.sosyalmedia.notificationservice.model.NotificationSettings;
import com.sosyalmedia.notificationservice.model.SchedulerSettings;
import com.sosyalmedia.notificationservice.repository.EmailTemplateRepository;
import com.sosyalmedia.notificationservice.repository.NotificationSettingsRepository;
import com.sosyalmedia.notificationservice.repository.SchedulerSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final SchedulerSettingsRepository schedulerSettingsRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    public void run(String... args) {
        log.info("🔄 DataInitializer başlatılıyor...");

        initializeSchedulerSettings();
        initializeNotificationSettings();
        initializeEmailTemplates();

        log.info("✅ DataInitializer tamamlandı!");
    }

    private void initializeSchedulerSettings() {
        log.info("📅 Scheduler ayarları kontrol ediliyor...");

        List<SchedulerSettings> defaultSettings = Arrays.asList(
                SchedulerSettings.builder()
                        .settingKey("CHECK_UPCOMING_POSTS")
                        .settingName("Yaklaşan Postları Kontrol Et")
                        .description("Önümüzdeki günlerdeki postları kontrol eder ve bildirim gönderir")
                        .cronExpression("0 0 9 * * ?")
                        .isActive(true)
                        .build(),

                SchedulerSettings.builder()
                        .settingKey("CHECK_OVERDUE_POSTS")
                        .settingName("Gecikmiş Postları Kontrol Et")
                        .description("Tarihi geçmiş ama yayınlanmamış postları kontrol eder")
                        .cronExpression("0 0 10 * * ?")
                        .isActive(true)
                        .build(),

                SchedulerSettings.builder()
                        .settingKey("CHECK_SPECIAL_DATES")
                        .settingName("Özel Günleri Kontrol Et")
                        .description("Yaklaşan özel günleri kontrol eder ve hatırlatma gönderir")
                        .cronExpression("0 0 8 * * ?")
                        .isActive(true)
                        .build(),

                SchedulerSettings.builder()
                        .settingKey("SYNC_HOLIDAYS")
                        .settingName("Tatilleri Senkronize Et")
                        .description("Calendarific API'den yeni yıl tatillerini çeker")
                        .cronExpression("0 0 2 1 1 ?")
                        .isActive(true)
                        .build()
        );

        for (SchedulerSettings setting : defaultSettings) {
            if (!schedulerSettingsRepository.existsBySettingKey(setting.getSettingKey())) {
                schedulerSettingsRepository.save(setting);
                log.info("➕ Scheduler ayarı eklendi: {}", setting.getSettingName());
            }
        }
    }

    private void initializeNotificationSettings() {
        log.info("🔔 Bildirim ayarları kontrol ediliyor...");

        List<NotificationSettings> defaultSettings = Arrays.asList(
                NotificationSettings.builder()
                        .settingKey("UPCOMING_POST_REMINDERS")
                        .settingName("Yaklaşan Post Hatırlatmaları")
                        .description("Post tarihinden kaç gün önce hatırlatma gönderilecek")
                        .reminderDays(Arrays.asList(14, 7, 3, 1))
                        .emailTemplateKey("UPCOMING_POST_REMINDER")
                        .isActive(true)
                        .build(),

                NotificationSettings.builder()
                        .settingKey("SPECIAL_DATE_REMINDERS")
                        .settingName("Özel Gün Hatırlatmaları")
                        .description("Özel günlerden kaç gün önce hatırlatma gönderilecek")
                        .reminderDays(Arrays.asList(14, 7))
                        .emailTemplateKey("SPECIAL_DATE_REMINDER")
                        .isActive(true)
                        .build()
        );

        for (NotificationSettings setting : defaultSettings) {
            if (!notificationSettingsRepository.existsBySettingKey(setting.getSettingKey())) {
                notificationSettingsRepository.save(setting);
                log.info("➕ Bildirim ayarı eklendi: {}", setting.getSettingName());
            }
        }
    }

    private void initializeEmailTemplates() {
        log.info("📧 E-posta şablonları kontrol ediliyor...");

        List<EmailTemplate> defaultTemplates = Arrays.asList(
                // 1. Normal Post Hatırlatma
                EmailTemplate.builder()
                        .templateKey("UPCOMING_POST_REMINDER")
                        .templateName("Yaklaşan Post Hatırlatması")
                        .subjectTemplate("{{companyName}} - {{daysRemaining}} Gün Sonra Post")
                        .bodyTemplate("""
                    <html>
                    <body style="font-family: Arial, sans-serif; padding: 20px;">
                        <h2 style="color: #2563eb;">📅 Post Hatırlatması</h2>
                        <p>Merhaba,</p>
                        <p><strong>{{companyName}}</strong> için <strong>{{daysRemaining}} gün</strong> sonra post yayınlanacak.</p>
                        <div style="background-color: #f3f4f6; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <p><strong>📅 Post Tarihi:</strong> {{postDate}}</p>
                            <p><strong>🕐 Post Saati:</strong> {{postTime}}</p>
                            <p><strong>📱 Platform:</strong> {{platforms}}</p>
                        </div>
                        <p>Lütfen içeriği hazırlamaya başlayın.</p>
                    </body>
                    </html>
                    """)
                        .templateType("UPCOMING_POST")
                        .variables(Arrays.asList("companyName", "daysRemaining", "postDate", "postTime", "platforms")) // ✅ DÜZELT
                        .isActive(true)
                        .build(),

                // 2. Kritik Uyarı
                EmailTemplate.builder()
                        .templateKey("CRITICAL_POST_ALERT")
                        .templateName("Kritik Post Uyarısı")
                        .subjectTemplate("⚠️ ACİL: {{companyName}} - {{daysRemaining}} Gün Kaldı!")
                        .bodyTemplate("""
                    <html>
                    <body style="font-family: Arial, sans-serif; padding: 20px;">
                        <div style="background-color: #fef3c7; border-left: 4px solid #f59e0b; padding: 15px;">
                            <h2 style="color: #92400e;">⚠️ ACİL DURUM</h2>
                        </div>
                        <p><strong>{{companyName}}</strong> için sadece <strong style="color: #dc2626;">{{daysRemaining}} gün</strong> kaldı!</p>
                        <div style="background-color: #fee2e2; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <p><strong>📅 Post Tarihi:</strong> {{postDate}}</p>
                            <p><strong>🕐 Post Saati:</strong> {{postTime}}</p>
                            <p><strong>⚠️ Durum:</strong> İçerik hazır değil!</p>
                        </div>
                        <p style="color: #dc2626; font-weight: bold;">Lütfen acilen içeriği hazırlayın!</p>
                    </body>
                    </html>
                    """)
                        .templateType("CRITICAL_ALERT")
                        .variables(Arrays.asList("companyName", "daysRemaining", "postDate", "postTime")) // ✅ DÜZELT
                        .isActive(true)
                        .build(),

                // 3. Özel Gün Hatırlatma
                EmailTemplate.builder()
                        .templateKey("SPECIAL_DATE_REMINDER")
                        .templateName("Özel Gün Hatırlatması")
                        .subjectTemplate("🎉 Yaklaşan Özel Gün: {{specialDateName}}")
                        .bodyTemplate("""
                    <html>
                    <body style="font-family: Arial, sans-serif; padding: 20px;">
                        <h2 style="color: #7c3aed;">🎉 Özel Gün Hatırlatması</h2>
                        <p><strong>{{specialDateName}}</strong> yaklaşıyor!</p>
                        <div style="background-color: #f3e8ff; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <p><strong>📅 Tarih:</strong> {{specialDate}}</p>
                            <p><strong>⏰ Kalan Gün:</strong> {{daysRemaining}} gün</p>
                            <p><strong>{{icon}}</strong> {{description}}</p>
                        </div>
                        <p>Müşterileriniz için özel içerik hazırlamayı unutmayın!</p>
                    </body>
                    </html>
                    """)
                        .templateType("SPECIAL_DATE")
                        .variables(Arrays.asList("specialDateName", "specialDate", "daysRemaining", "icon", "description")) // ✅ DÜZELT
                        .isActive(true)
                        .build(),

                // 4. Gecikmiş Post Uyarısı
                EmailTemplate.builder()
                        .templateKey("OVERDUE_POST_ALERT")
                        .templateName("Gecikmiş Post Uyarısı")
                        .subjectTemplate("🚨 {{companyName}} - Post Tarihi Geçti!")
                        .bodyTemplate("""
                    <html>
                    <body style="font-family: Arial, sans-serif; padding: 20px;">
                        <div style="background-color: #fee2e2; border-left: 4px solid #dc2626; padding: 15px;">
                            <h2 style="color: #991b1b;">🚨 GECİKME UYARISI</h2>
                        </div>
                        <p><strong>{{companyName}}</strong> postu <strong style="color: #dc2626;">tarihi geçti!</strong></p>
                        <div style="background-color: #fef2f2; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <p><strong>📅 Planlanan Tarih:</strong> {{postDate}}</p>
                            <p><strong>⏰ Gecikme:</strong> {{daysOverdue}} gün</p>
                        </div>
                        <p style="color: #dc2626; font-weight: bold;">Lütfen acilen postu yayınlayın!</p>
                    </body>
                    </html>
                    """)
                        .templateType("OVERDUE")
                        .variables(Arrays.asList("companyName", "postDate", "daysOverdue")) // ✅ DÜZELT
                        .isActive(true)
                        .build(),

                // 5. Hoş Geldin
                EmailTemplate.builder()
                        .templateKey("WELCOME_EMAIL")
                        .templateName("Hoş Geldin E-postası")
                        .subjectTemplate("🎉 Hoş Geldiniz - {{companyName}}")
                        .bodyTemplate("""
                    <html>
                    <body style="font-family: Arial, sans-serif; padding: 20px;">
                        <h2 style="color: #16a34a;">🎉 Hoş Geldiniz!</h2>
                        <p>Merhaba <strong>{{companyName}}</strong>,</p>
                        <div style="background-color: #f0fdf4; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <h3>📅 Post Planınız Oluşturuldu</h3>
                            <p><strong>İlk Post:</strong> {{firstPostDate}}</p>
                            <p><strong>Toplam Post:</strong> {{totalPosts}} adet</p>
                            <p><strong>Sıklık:</strong> Haftada {{postFrequency}} gün</p>
                        </div>
                    </body>
                    </html>
                    """)
                        .templateType("WELCOME")
                        .variables(Arrays.asList("companyName", "firstPostDate", "totalPosts", "postFrequency")) // ✅ DÜZELT
                        .isActive(true)
                        .build()
        );

        for (EmailTemplate template : defaultTemplates) {
            if (!emailTemplateRepository.existsByTemplateKey(template.getTemplateKey())) {
                emailTemplateRepository.save(template);
                log.info("➕ E-posta şablonu eklendi: {}", template.getTemplateName());
            }
        }
    }
}
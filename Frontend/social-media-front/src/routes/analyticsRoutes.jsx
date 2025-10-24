// src/routes/analyticsRoutes.jsx

import DashboardPage from '../modules/analytics-service/pages/DashboardPage';
import CustomerAnalyticsWrapper from '../modules/analytics-service/pages/CustomerAnalyticsWrapper';
import AITaskListPage from '../modules/analytics-service/pages/AITaskListPage';
import OnboardingListPage from '../modules/analytics-service/pages/OnboardingListPage';
import ActivityHistoryPage from '../modules/analytics-service/pages/ActivityHistoryPage';
import CustomerNotesPage from '../modules/analytics-service/pages/CustomerNotesPage';
 
export const analyticsRoutes = {
  // 📈 Genel Bakış
  dashboard: {
    component: DashboardPage
  },
  
  // 📋 Süreç Yönetimi (eski Müşteri Analizi)
  surecYonetimi: {
    component: CustomerAnalyticsWrapper
  },
  
  // 🤖 AI İçerik Takibi
  aiTaskList: {
    component: AITaskListPage
  },
  
  // 🚀 Onboarding Takibi
  onboardingList: {
    component: OnboardingListPage
  },
  
  // 📜 Aktivite Geçmişi
  activityHistory: {
    component: ActivityHistoryPage
  },

  // ✅ YENİ: 📝 Müşteri Notları
  customerNotes: {
    component: CustomerNotesPage
  }
};
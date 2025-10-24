// src/modules/analytics-service/services/analyticsService.js

import httpClient from './api/httpClient';
import { API_ENDPOINTS } from './api/config';

const analyticsService = {
    // ============================================
    // DASHBOARD SERVICE
    // ============================================

    /**
     * Dashboard istatistiklerini getir
     */
    getDashboardStats: async () => {
        try {
            const response = await httpClient.get(API_ENDPOINTS.DASHBOARD_STATS);
            return response;
        } catch (error) {
            console.error('❌ Failed to fetch dashboard stats:', error);
            return { success: false, error: error.error || 'Veriler yüklenemedi' };
        }
    },

    /**
     * Platform istatistiklerini getir
     */
    getPlatformStats: async () => {
        try {
            const response = await httpClient.get(API_ENDPOINTS.PLATFORM_STATS);
            return response;
        } catch (error) {
            console.error('❌ Failed to fetch platform stats:', error);
            return { success: false, error: error.error || 'Veriler yüklenemedi' };
        }
    },

    /**
     * Son aktiviteleri getir (Dashboard için)
     */
    getRecentActivities: async (limit = 10) => {
        try {
            const response = await httpClient.get(
                `${API_ENDPOINTS.ACTIVITIES}?limit=${limit}`
            );
            return response;
        } catch (error) {
            console.error('❌ Failed to fetch activities:', error);
            return { success: false, error: error.error || 'Veriler yüklenemedi' };
        }
    },

 
    /**
     * Yeni aktivite ekle
     */
    createActivity: async (activityData) => {
        try {
            const response = await httpClient.post(API_ENDPOINTS.ACTIVITIES, activityData);
            return response;
        } catch (error) {
            console.error('❌ Failed to create activity:', error);
            return { success: false, error: error.error || 'Aktivite eklenemedi' };
        }
    },

    /**
   * Aktivite güncelle (✅ YENİ)
   */
    updateActivity: async (activityId, activityData) => {
        try {
            const response = await httpClient.put(
                `${API_ENDPOINTS.ACTIVITIES}/${activityId}`,
                activityData
            );
            return response;
        } catch (error) {
            console.error('❌ Failed to update activity:', error);
            return { success: false, error: error.error || 'Aktivite güncellenemedi' };
        }
    },

    /**
     * Toplu aktivite ekle
     */
    createActivitiesBulk: async (activities) => {
        try {
            const response = await httpClient.post(API_ENDPOINTS.ACTIVITIES_BULK, activities);
            return response;
        } catch (error) {
            console.error('❌ Failed to create bulk activities:', error);
            return { success: false, error: error.error || 'Aktiviteler eklenemedi' };
        }
    },

    /**
     * Aktiviteyi ID ile getir
     */
    getActivityById: async (id) => {
        try {
            const response = await httpClient.get(API_ENDPOINTS.ACTIVITY_BY_ID(id));
            return response;
        } catch (error) {
            console.error(`❌ Failed to fetch activity ${id}:`, error);
            return { success: false, error: error.error || 'Aktivite bulunamadı' };
        }
    },

    /**
     * Tipe göre aktiviteleri getir
     */
    getActivitiesByType: async (activityType, limit = 50) => {
        try {
            const response = await httpClient.get(
                `${API_ENDPOINTS.ACTIVITIES_BY_TYPE(activityType)}?limit=${limit}`
            );
            return response;
        } catch (error) {
            console.error(`❌ Failed to fetch activities by type ${activityType}:`, error);
            return { success: false, error: error.error || 'Aktiviteler getirilemedi' };
        }
    },

    /**
     * Müşteri + Tip kombinasyonu ile aktiviteleri getir
     */
    getCustomerActivitiesByType: async (customerId, activityType, limit = 20) => {
        try {
            const response = await httpClient.get(
                `${API_ENDPOINTS.ACTIVITIES}/customer/${customerId}/type/${activityType}?limit=${limit}`
            );
            return response;
        } catch (error) {
            console.error(`❌ Failed to fetch customer activities:`, error);
            return { success: false, error: error.error || 'Aktiviteler getirilemedi' };
        }
    },

    /**
     * Tarih aralığına göre aktiviteleri getir
     */
    getActivitiesByDateRange: async (startDate, endDate, limit = 100) => {
        try {
            const response = await httpClient.get(API_ENDPOINTS.ACTIVITIES_RANGE, {
                params: { startDate, endDate, limit }
            });
            return response;
        } catch (error) {
            console.error('❌ Failed to fetch activities by date range:', error);
            return { success: false, error: error.error || 'Aktiviteler getirilemedi' };
        }
    },

    /**
     * Aktivite istatistikleri
     */
    getActivityStats: async () => {
        try {
            const response = await httpClient.get(API_ENDPOINTS.ACTIVITIES_STATS);
            return response;
        } catch (error) {
            console.error('❌ Failed to fetch activity stats:', error);
            return { success: false, error: error.error || 'İstatistikler getirilemedi' };
        }
    },

    /**
     * Aktivite sil
     */
    deleteActivity: async (id) => {
        try {
            const response = await httpClient.delete(API_ENDPOINTS.ACTIVITY_BY_ID(id));
            return response;
        } catch (error) {
            console.error(`❌ Failed to delete activity ${id}:`, error);
            return { success: false, error: error.error || 'Aktivite silinemedi' };
        }
    },

    // ============================================
    // CUSTOMER DETAIL SERVICE
    // ============================================

    /**
     * Müşteri detay bilgilerini getir
     */
    getCustomerDetail: async (customerId) => {
        try {
            const response = await httpClient.get(API_ENDPOINTS.CUSTOMER_DETAIL(customerId));
            return response;
        } catch (error) {
            console.error(`❌ Failed to fetch customer ${customerId}:`, error);
            return { success: false, error: error.error || 'Müşteri bulunamadı' };
        }
    },

    /**
     * Müşteri post istatistiklerini getir
     */
    getCustomerPostStats: async (customerId) => {
        try {
            const response = await httpClient.get(API_ENDPOINTS.CUSTOMER_POST_STATS(customerId));
            return response;
        } catch (error) {
            console.error(`❌ Failed to fetch post stats for customer ${customerId}:`, error);
            return { success: false, error: error.error || 'Veriler yüklenemedi' };
        }
    },

    /**
     * Müşteri yaklaşan postlarını getir
     */
    getCustomerUpcomingPosts: async (customerId, limit = 5) => {
        try {
            const response = await httpClient.get(
                `${API_ENDPOINTS.CUSTOMER_UPCOMING_POSTS(customerId)}?limit=${limit}`
            );
            return response;
        } catch (error) {
            console.error(`❌ Failed to fetch upcoming posts for customer ${customerId}:`, error);
            return { success: false, error: error.error || 'Veriler yüklenemedi' };
        }
    },

    /**
     * Müşteri aktivitelerini getir
     */
    getCustomerActivities: async (customerId, limit = 10) => {
        try {
            const response = await httpClient.get(
                `${API_ENDPOINTS.CUSTOMER_ACTIVITIES(customerId)}?limit=${limit}`
            );
            return response;
        } catch (error) {
            console.error(`❌ Failed to fetch activities for customer ${customerId}:`, error);
            return { success: false, error: error.error || 'Veriler yüklenemedi' };
        }
    },

    /**
     * Müşteri notlarını getir
     */
    getCustomerNotes: async (customerId) => {
        try {
            const response = await httpClient.get(API_ENDPOINTS.CUSTOMER_NOTES(customerId));
            return response;
        } catch (error) {
            console.error(`❌ Failed to fetch notes for customer ${customerId}:`, error);
            return { success: false, error: error.error || 'Veriler yüklenemedi' };
        }
    },

    addCustomerNote: async (customerId, noteText, createdBy = 'Admin') => {
        try {
            const response = await httpClient.post(
                `${API_ENDPOINTS.CUSTOMER_NOTES(customerId)}`,
                {
                    note: noteText,
                    createdBy: createdBy
                }
            );
            return response;
        } catch (error) {
            console.error(`❌ Failed to add note for customer ${customerId}:`, error);
            return { success: false, error: error.error || 'Not eklenemedi' };
        }
    },

    /**
     * Müşteri notunu güncelle 
     */
    updateCustomerNote: async (noteId, noteData) => {
        try {
            const response = await httpClient.put(
                `${API_ENDPOINTS.NOTE_BY_ID(noteId)}`,
                noteData
            );
            return response;
        } catch (error) {
            console.error(`❌ Failed to update note ${noteId}:`, error);
            return { success: false, error: error.error || 'Not güncellenemedi' };
        }
    },

    /**
     * Müşteri notunu sil  
     */
    deleteCustomerNote: async (noteId) => {
        try {
            const response = await httpClient.delete(`${API_ENDPOINTS.NOTE_BY_ID(noteId)}`);
            return response;
        } catch (error) {
            console.error(`❌ Failed to delete note ${noteId}:`, error);
            return { success: false, error: error.error || 'Not silinemedi' };
        }
    },


    // ============================================
    // AI CONTENT TASKS SERVICE
    // ============================================

    /**
     * Müşterinin AI içerik görevlerini getir
     */
    getAIContentTasks: async (customerId) => {
        try {
            const response = await httpClient.get(API_ENDPOINTS.AI_TASKS_BY_CUSTOMER(customerId));
            return response;
        } catch (error) {
            console.error(`❌ Failed to fetch AI tasks for customer ${customerId}:`, error);
            return { success: false, error: error.error || 'Veriler yüklenemedi' };
        }
    },

    /**
     * AI içerik görevi güncelle
     */
    updateAIContentTask: async (taskId, updateData) => {
        try {
            const response = await httpClient.put(
                API_ENDPOINTS.AI_TASK_BY_ID(taskId),
                updateData
            );
            return response;
        } catch (error) {
            console.error(`❌ Failed to update AI task ${taskId}:`, error);
            return { success: false, error: error.error || 'Güncelleme başarısız' };
        }
    },

    /**
     * Yeni AI içerik görevi ekle
     */
    addAIContentTask: async (customerId, taskData) => {
        try {
            const response = await httpClient.post(
                API_ENDPOINTS.AI_TASKS,
                {
                    customerId,
                    ...taskData
                }
            );
            return response;
        } catch (error) {
            console.error('❌ Failed to add AI task:', error);
            return { success: false, error: error.error || 'Görev eklenemedi' };
        }
    },

    /**
     * AI içerik görevini sil
     */
    deleteAIContentTask: async (taskId) => {
        try {
            const response = await httpClient.delete(API_ENDPOINTS.AI_TASK_BY_ID(taskId));
            return response;
        } catch (error) {
            console.error(`❌ Failed to delete AI task ${taskId}:`, error);
            return { success: false, error: error.error || 'Silme başarısız' };
        }
    },

    // ============================================
    // ONBOARDING TASKS SERVICE
    // ============================================

    /**
     * Müşterinin onboarding görevlerini getir
     */
    getOnboardingTasks: async (customerId) => {
        try {
            const response = await httpClient.get(API_ENDPOINTS.ONBOARDING_TASKS_BY_CUSTOMER(customerId));
            return response;
        } catch (error) {
            console.error(`❌ Failed to fetch onboarding tasks for customer ${customerId}:`, error);
            return { success: false, error: error.error || 'Veriler yüklenemedi' };
        }
    },

    /**
     * Onboarding görevini güncelle
     */
    updateOnboardingTask: async (taskId, updateData) => {
        try {
            const response = await httpClient.put(
                API_ENDPOINTS.ONBOARDING_TASK_BY_ID(taskId),
                updateData
            );
            return response;
        } catch (error) {
            console.error(`❌ Failed to update onboarding task ${taskId}:`, error);
            return { success: false, error: error.error || 'Güncelleme başarısız' };
        }
    },

    /**
     * Yeni onboarding görevi ekle
     */
    addOnboardingTask: async (customerId, taskData) => {
        try {
            const response = await httpClient.post(
                API_ENDPOINTS.ONBOARDING_TASKS,
                {
                    customerId,
                    ...taskData
                }
            );
            return response;
        } catch (error) {
            console.error('❌ Failed to add onboarding task:', error);
            return { success: false, error: error.error || 'Görev eklenemedi' };
        }
    },

    /**
     * Onboarding görevini sil
     */
    deleteOnboardingTask: async (taskId) => {
        try {
            const response = await httpClient.delete(API_ENDPOINTS.ONBOARDING_TASK_BY_ID(taskId));
            return response;
        } catch (error) {
            console.error(`❌ Failed to delete onboarding task via API:`, error);
            return { success: false, error: error.error || 'Silme başarısız' };
        }
    },
};

// Default export
export default analyticsService;
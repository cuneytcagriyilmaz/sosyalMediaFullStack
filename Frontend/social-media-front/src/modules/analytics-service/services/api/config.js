// src/modules/analytics-service/services/api/config.js

const API_BASE_URL = 'http://localhost:8080'; // API Gateway
const ANALYTICS_BASE = `${API_BASE_URL}/api/analytics`;

export const API_ENDPOINTS = {
  // ==================== DASHBOARD ====================
  DASHBOARD_STATS: `${ANALYTICS_BASE}/dashboard`,
  PLATFORM_STATS: `${ANALYTICS_BASE}/dashboard/platform-stats`,
  RECENT_ACTIVITIES: `${ANALYTICS_BASE}/dashboard/activities`,

  // ==================== CUSTOMER ANALYTICS ====================
  CUSTOMER_DETAIL: (customerId) => `${ANALYTICS_BASE}/customers/${customerId}/detail`,
  CUSTOMER_POST_STATS: (customerId) => `${ANALYTICS_BASE}/customers/${customerId}/post-stats`,
  CUSTOMER_UPCOMING_POSTS: (customerId) => `${ANALYTICS_BASE}/customers/${customerId}/upcoming-posts`,
  CUSTOMER_ACTIVITIES: (customerId) => `${ANALYTICS_BASE}/customers/${customerId}/activities`,
  CUSTOMER_NOTES: (customerId) => `${ANALYTICS_BASE}/customers/${customerId}/notes`,

  // ==================== NOTES ====================
  NOTE_BY_ID: (noteId) => `${ANALYTICS_BASE}/notes/${noteId}`,
  NOTE_COUNT: (customerId) => `${ANALYTICS_BASE}/notes/customer/${customerId}/count`,

  // ==================== ACTIVITIES ====================
  ACTIVITIES: `${ANALYTICS_BASE}/activities`,
  ACTIVITY_BY_ID: (activityId) => `${ANALYTICS_BASE}/activities/${activityId}`,
  ACTIVITIES_BY_CUSTOMER: (customerId) => `${ANALYTICS_BASE}/activities/customer/${customerId}`,
  ACTIVITIES_BY_TYPE: (type) => `${ANALYTICS_BASE}/activities/type/${type}`,
  ACTIVITIES_STATS: `${ANALYTICS_BASE}/activities/stats`, // ✅ YENİ
  ACTIVITIES_BULK: `${ANALYTICS_BASE}/activities/bulk`, // ✅ YENİ
  ACTIVITIES_RANGE: `${ANALYTICS_BASE}/activities/range`, // ✅ YENİ

  // ==================== AI TASKS ====================
  AI_TASKS: `${ANALYTICS_BASE}/ai-tasks`,
  AI_TASK_BY_ID: (taskId) => `${ANALYTICS_BASE}/ai-tasks/${taskId}`,
  AI_TASKS_BY_CUSTOMER: (customerId) => `${ANALYTICS_BASE}/ai-tasks/customer/${customerId}`,
  AI_TASKS_BY_STATUS: (status) => `${ANALYTICS_BASE}/ai-tasks/status/${status}`,
  AI_TASKS_COUNT: (customerId) => `${ANALYTICS_BASE}/ai-tasks/customer/${customerId}/count`,

  // ==================== ONBOARDING TASKS ====================
  ONBOARDING_TASKS: `${ANALYTICS_BASE}/onboarding-tasks`,
  ONBOARDING_TASK_BY_ID: (taskId) => `${ANALYTICS_BASE}/onboarding-tasks/${taskId}`,
  ONBOARDING_TASKS_BY_CUSTOMER: (customerId) => `${ANALYTICS_BASE}/onboarding-tasks/customer/${customerId}`,
  ONBOARDING_TASKS_BY_PLATFORM: (customerId, platform) => 
    `${ANALYTICS_BASE}/onboarding-tasks/customer/${customerId}/platform/${platform}`,
  ONBOARDING_TASKS_COMPLETED_COUNT: (customerId) => 
    `${ANALYTICS_BASE}/onboarding-tasks/customer/${customerId}/completed-count`,
};

export default API_BASE_URL;
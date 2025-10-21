// src/modules/analytics-service/services/analyticsService.js

import {
  mockDashboardStats,
  mockCustomerDetails,
  mockAIContentTasks,
  mockOnboardingTasks
} from '../data/mockData';

// Simüle edilmiş API gecikme süresi
const API_DELAY = 500; // ms

// API delay simülasyonu için helper
const delay = (ms = API_DELAY) => new Promise(resolve => setTimeout(resolve, ms));

// ============================================
// DASHBOARD SERVICE
// ============================================

/**
 * Dashboard istatistiklerini getir
 */
export const getDashboardStats = async () => {
  await delay();
  return {
    success: true,
    data: mockDashboardStats
  };
};

/**
 * Platform istatistiklerini getir
 */
export const getPlatformStats = async () => {
  await delay();
  return {
    success: true,
    data: mockDashboardStats.platformStats
  };
};

/**
 * Son aktiviteleri getir
 */
export const getRecentActivities = async (limit = 10) => {
  await delay();
  const activities = mockDashboardStats.recentActivities.slice(0, limit);
  return {
    success: true,
    data: activities
  };
};

// ============================================
// CUSTOMER DETAIL SERVICE
// ============================================

/**
 * Müşteri detay bilgilerini getir
 */
export const getCustomerDetail = async (customerId) => {
  await delay();
  
  const customerDetail = mockCustomerDetails[customerId];
  
  if (!customerDetail) {
    return {
      success: false,
      error: 'Müşteri bulunamadı'
    };
  }
  
  return {
    success: true,
    data: customerDetail
  };
};

/**
 * Müşteri post istatistiklerini getir
 */
export const getCustomerPostStats = async (customerId) => {
  await delay();
  
  const customerDetail = mockCustomerDetails[customerId];
  
  if (!customerDetail) {
    return {
      success: false,
      error: 'Müşteri bulunamadı'
    };
  }
  
  return {
    success: true,
    data: customerDetail.postStats
  };
};

/**
 * Müşteri yaklaşan postlarını getir
 */
export const getCustomerUpcomingPosts = async (customerId, limit = 5) => {
  await delay();
  
  const customerDetail = mockCustomerDetails[customerId];
  
  if (!customerDetail) {
    return {
      success: false,
      error: 'Müşteri bulunamadı'
    };
  }
  
  const posts = customerDetail.upcomingPosts.slice(0, limit);
  
  return {
    success: true,
    data: posts
  };
};

/**
 * Müşteri aktivitelerini getir
 */
export const getCustomerActivities = async (customerId, limit = 10) => {
  await delay();
  
  const customerDetail = mockCustomerDetails[customerId];
  
  if (!customerDetail) {
    return {
      success: false,
      error: 'Müşteri bulunamadı'
    };
  }
  
  const activities = customerDetail.recentActivities.slice(0, limit);
  
  return {
    success: true,
    data: activities
  };
};

/**
 * Müşteri notlarını getir
 */
export const getCustomerNotes = async (customerId) => {
  await delay();
  
  const customerDetail = mockCustomerDetails[customerId];
  
  if (!customerDetail) {
    return {
      success: false,
      error: 'Müşteri bulunamadı'
    };
  }
  
  return {
    success: true,
    data: customerDetail.notes || []
  };
};

/**
 * Müşteri notu ekle
 */
export const addCustomerNote = async (customerId, noteText) => {
  await delay();
  
  const customerDetail = mockCustomerDetails[customerId];
  
  if (!customerDetail) {
    return {
      success: false,
      error: 'Müşteri bulunamadı'
    };
  }
  
  const newNote = {
    id: Date.now(),
    text: noteText,
    createdAt: new Date().toISOString(),
    createdBy: 'Admin'
  };
  
  customerDetail.notes.unshift(newNote);
  
  return {
    success: true,
    data: newNote
  };
};

// ============================================
// AI CONTENT TASKS SERVICE
// ============================================

/**
 * Müşterinin AI içerik görevlerini getir
 */
export const getAIContentTasks = async (customerId) => {
  await delay();
  
  const tasks = mockAIContentTasks[customerId];
  
  if (!tasks) {
    return {
      success: true,
      data: []
    };
  }
  
  return {
    success: true,
    data: tasks
  };
};

/**
 * AI içerik görevi güncelle
 */
export const updateAIContentTask = async (taskId, updateData) => {
  await delay();
  
  // Tüm müşterilerdeki görevleri ara
  for (const customerId in mockAIContentTasks) {
    const tasks = mockAIContentTasks[customerId];
    const taskIndex = tasks.findIndex(t => t.id === taskId);
    
    if (taskIndex !== -1) {
      // Görevi güncelle
      mockAIContentTasks[customerId][taskIndex] = {
        ...mockAIContentTasks[customerId][taskIndex],
        ...updateData,
        updatedAt: new Date().toISOString()
      };
      
      return {
        success: true,
        data: mockAIContentTasks[customerId][taskIndex]
      };
    }
  }
  
  return {
    success: false,
    error: 'Görev bulunamadı'
  };
};

/**
 * Yeni AI içerik görevi ekle
 */
export const addAIContentTask = async (customerId, taskData) => {
  await delay();
  
  if (!mockAIContentTasks[customerId]) {
    mockAIContentTasks[customerId] = [];
  }
  
  const newTask = {
    id: Date.now(),
    customerId,
    ...taskData,
    createdAt: new Date().toISOString()
  };
  
  mockAIContentTasks[customerId].push(newTask);
  
  return {
    success: true,
    data: newTask
  };
};

/**
 * AI içerik görevini sil
 */
export const deleteAIContentTask = async (taskId) => {
  await delay();
  
  // Tüm müşterilerdeki görevleri ara
  for (const customerId in mockAIContentTasks) {
    const tasks = mockAIContentTasks[customerId];
    const taskIndex = tasks.findIndex(t => t.id === taskId);
    
    if (taskIndex !== -1) {
      mockAIContentTasks[customerId].splice(taskIndex, 1);
      
      return {
        success: true,
        message: 'Görev silindi'
      };
    }
  }
  
  return {
    success: false,
    error: 'Görev bulunamadı'
  };
};

// ============================================
// ONBOARDING TASKS SERVICE
// ============================================

/**
 * Müşterinin onboarding görevlerini getir
 */
export const getOnboardingTasks = async (customerId) => {
  await delay();
  
  const tasks = mockOnboardingTasks[customerId];
  
  if (!tasks) {
    return {
      success: true,
      data: []
    };
  }
  
  return {
    success: true,
    data: tasks
  };
};

/**
 * Onboarding görevini güncelle
 */
export const updateOnboardingTask = async (taskId, updateData) => {
  await delay();
  
  // Tüm müşterilerdeki görevleri ara
  for (const customerId in mockOnboardingTasks) {
    const tasks = mockOnboardingTasks[customerId];
    const taskIndex = tasks.findIndex(t => t.id === taskId);
    
    if (taskIndex !== -1) {
      // Görevi güncelle
      mockOnboardingTasks[customerId][taskIndex] = {
        ...mockOnboardingTasks[customerId][taskIndex],
        ...updateData,
        updatedAt: new Date().toISOString()
      };
      
      return {
        success: true,
        data: mockOnboardingTasks[customerId][taskIndex]
      };
    }
  }
  
  return {
    success: false,
    error: 'Görev bulunamadı'
  };
};

/**
 * Yeni onboarding görevi ekle
 */
export const addOnboardingTask = async (customerId, taskData) => {
  await delay();
  
  if (!mockOnboardingTasks[customerId]) {
    mockOnboardingTasks[customerId] = [];
  }
  
  const newTask = {
    id: Date.now(),
    customerId,
    ...taskData,
    createdAt: new Date().toISOString()
  };
  
  mockOnboardingTasks[customerId].push(newTask);
  
  return {
    success: true,
    data: newTask
  };
};

/**
 * Onboarding görevini sil
 */
export const deleteOnboardingTask = async (taskId) => {
  await delay();
  
  // Tüm müşterilerdeki görevleri ara
  for (const customerId in mockOnboardingTasks) {
    const tasks = mockOnboardingTasks[customerId];
    const taskIndex = tasks.findIndex(t => t.id === taskId);
    
    if (taskIndex !== -1) {
      mockOnboardingTasks[customerId].splice(taskIndex, 1);
      
      return {
        success: true,
        message: 'Görev silindi'
      };
    }
  }
  
  return {
    success: false,
    error: 'Görev bulunamadı'
  };
};

// Default export
export default {
  // Dashboard
  getDashboardStats,
  getPlatformStats,
  getRecentActivities,
  
  // Customer Detail
  getCustomerDetail,
  getCustomerPostStats,
  getCustomerUpcomingPosts,
  getCustomerActivities,
  getCustomerNotes,
  addCustomerNote,
  
  // AI Content Tasks
  getAIContentTasks,
  updateAIContentTask,
  addAIContentTask,
  deleteAIContentTask,
  
  // Onboarding Tasks
  getOnboardingTasks,
  updateOnboardingTask,
  addOnboardingTask,
  deleteOnboardingTask
};
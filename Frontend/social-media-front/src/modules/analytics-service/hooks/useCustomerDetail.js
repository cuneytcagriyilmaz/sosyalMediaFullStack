// src/modules/analytics-service/hooks/useCustomerDetail.js

import { useState, useEffect } from 'react';
import analyticsService from '../services/analyticsService';
import { useToast } from '../../../shared/context/ToastContext';

export default function useCustomerDetail(customerId) {
  const [customer, setCustomer] = useState(null);
  const [aiTasks, setAiTasks] = useState([]);
  const [onboardingTasks, setOnboardingTasks] = useState([]);
  const [upcomingPosts, setUpcomingPosts] = useState([]);
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const { toast } = useToast();

  useEffect(() => {
    if (customerId) {
      fetchCustomerDetail();
    }
  }, [customerId]);

  const fetchCustomerDetail = async () => {
    setLoading(true);
    setError(null);
    
    try {
      // Paralel olarak tüm verileri çek
      const [
        customerRes,
        aiTasksRes,
        onboardingRes,
        postsRes,
        activitiesRes
      ] = await Promise.all([
        analyticsService.getCustomerDetail(customerId),
        analyticsService.getAIContentTasks(customerId),
        analyticsService.getOnboardingTasks(customerId),
        analyticsService.getCustomerUpcomingPosts(customerId, 5),
        analyticsService.getCustomerActivities(customerId, 5)
      ]);

      if (customerRes.success) {
        setCustomer(customerRes.data);
      } else {
        setError(customerRes.error);
      }

      if (aiTasksRes.success) {
        setAiTasks(aiTasksRes.data);
      }

      if (onboardingRes.success) {
        setOnboardingTasks(onboardingRes.data);
      }

      if (postsRes.success) {
        setUpcomingPosts(postsRes.data);
      }

      if (activitiesRes.success) {
        setActivities(activitiesRes.data);
      }
    } catch (err) {
      console.error('Müşteri detay hatası:', err);
      setError('Veriler yüklenirken hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  // AI Task güncelleme
  const updateAITask = async (taskId, updateData) => {
    try {
      const response = await analyticsService.updateAIContentTask(taskId, updateData);
      
      if (response.success) {
        // Local state'i güncelle
        setAiTasks(prev =>
          prev.map(task =>
            task.id === taskId ? response.data : task
          )
        );
        toast.success('Görev güncellendi');
        return { success: true };
      } else {
        toast.error(response.error || 'Güncelleme başarısız');
        return { success: false };
      }
    } catch (err) {
      console.error('AI task güncelleme hatası:', err);
      toast.error('Bir hata oluştu');
      return { success: false };
    }
  };

  // Onboarding Task güncelleme
  const updateOnboardingTask = async (taskId, updateData) => {
    try {
      const response = await analyticsService.updateOnboardingTask(taskId, updateData);
      
      if (response.success) {
        // Local state'i güncelle
        setOnboardingTasks(prev =>
          prev.map(task =>
            task.id === taskId ? response.data : task
          )
        );
        toast.success('Görev güncellendi');
        return { success: true };
      } else {
        toast.error(response.error || 'Güncelleme başarısız');
        return { success: false };
      }
    } catch (err) {
      console.error('Onboarding task güncelleme hatası:', err);
      toast.error('Bir hata oluştu');
      return { success: false };
    }
  };

  // Not ekleme
  const addNote = async (noteText) => {
    try {
      const response = await analyticsService.addCustomerNote(customerId, noteText);
      
      if (response.success) {
        // Local state'i güncelle
        setCustomer(prev => ({
          ...prev,
          notes: [response.data, ...(prev.notes || [])]
        }));
        toast.success('Not eklendi');
        return { success: true };
      } else {
        toast.error(response.error || 'Not eklenemedi');
        return { success: false };
      }
    } catch (err) {
      console.error('Not ekleme hatası:', err);
      toast.error('Bir hata oluştu');
      return { success: false };
    }
  };

  const refresh = () => {
    fetchCustomerDetail();
  };

  return {
    customer,
    aiTasks,
    onboardingTasks,
    upcomingPosts,
    activities,
    loading,
    error,
    updateAITask,
    updateOnboardingTask,
    addNote,
    refresh
  };
}
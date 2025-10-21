// src/modules/analytics-service/hooks/useDashboard.js

import { useState, useEffect } from 'react';
import analyticsService from '../services/analyticsService';

export default function useDashboard() {
  const [stats, setStats] = useState(null);
  const [platformStats, setPlatformStats] = useState(null);
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    setLoading(true);
    setError(null);
    
    try {
      // Paralel olarak tüm verileri çek
      const [statsRes, platformRes, activitiesRes] = await Promise.all([
        analyticsService.getDashboardStats(),
        analyticsService.getPlatformStats(),
        analyticsService.getRecentActivities(5)
      ]);

      if (statsRes.success) {
        setStats(statsRes.data);
      }

      if (platformRes.success) {
        setPlatformStats(platformRes.data);
      }

      if (activitiesRes.success) {
        setActivities(activitiesRes.data);
      }
    } catch (err) {
      console.error('Dashboard veri hatası:', err);
      setError('Veriler yüklenirken hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  const refresh = () => {
    fetchDashboardData();
  };

  return {
    stats,
    platformStats,
    activities,
    loading,
    error,
    refresh
  };
}
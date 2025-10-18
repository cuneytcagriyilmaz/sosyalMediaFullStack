// shared/components/HomePage/hooks/useHomePageStats.js

import { useState, useEffect } from 'react';
// import customerService from '../../../services/customerService'; // TODO: Backend hazır olunca

export const useHomePageStats = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    setLoading(true);
    try {
      // TODO: Gerçek API çağrısı
      // const data = await customerService.getHomePageStats();
      
      // Şimdilik mock veri
      await new Promise(resolve => setTimeout(resolve, 500)); // Fake delay
      
      const mockStats = {
        totalCustomers: 24,
        activeCampaigns: 8,
        monthlyPosts: 156,
        pendingTasks: 5
      };
      
      setStats(mockStats);
    } catch (err) {
      console.error('İstatistikler yüklenemedi:', err);
      setError('İstatistikler yüklenemedi');
    } finally {
      setLoading(false);
    }
  };

  return {
    stats,
    loading,
    error,
    refetch: fetchStats
  };
};
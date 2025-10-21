// src/modules/analytics-service/pages/DashboardPage.jsx

import { useEffect } from 'react';
import useDashboard from '../hooks/useDashboard';
import StatsCard from '../components/Dashboard/StatsCard';
import PlatformStats from '../components/Dashboard/PlatformStats';
import RecentActivities from '../components/Dashboard/RecentActivities';
 
export default function DashboardPage() {
  const { stats, platformStats, activities, loading, error, refresh } = useDashboard();

  // Loading state
  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
        <div className="max-w-7xl mx-auto">
          <div className="flex justify-center items-center py-20">
            <div className="text-center">
              <div className="inline-block animate-spin rounded-full h-12 w-12 border-4 border-indigo-500 border-t-transparent"></div>
              <p className="text-indigo-600 mt-4 font-medium">Yükleniyor...</p>
            </div>
          </div>
        </div>
      </div>
    );
  }

  // Error state
  if (error) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
        <div className="max-w-7xl mx-auto">
          <div className="bg-white rounded-2xl shadow-lg p-8 text-center">
            <span className="text-6xl">⚠️</span>
            <h2 className="text-xl font-bold text-gray-800 mt-4">Bir Hata Oluştu</h2>
            <p className="text-gray-600 mt-2">{error}</p>
            <button
              onClick={refresh}
              className="mt-6 px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
            >
              Tekrar Dene
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-800 flex items-center gap-3">
              📊 Dashboard
            </h1>
            <p className="text-gray-600 mt-1">
              Hoşgeldiniz! İşte sistemin genel durumu
            </p>
          </div>
          <button
            onClick={refresh}
            className="px-4 py-2 bg-white text-indigo-600 border border-indigo-200 rounded-lg hover:bg-indigo-50 transition-all duration-200 flex items-center gap-2 shadow-sm"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            Yenile
          </button>
        </div>

        {/* Genel İstatistikler */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <StatsCard
            title="Toplam Müşteri"
            value={stats?.totalCustomers || 0}
            icon="👥"
            subtitle={`${stats?.activeCustomers || 0} aktif`}
            color="indigo"
          />
          <StatsCard
            title="Üretilen Post"
            value={stats?.totalPostsGenerated || 0}
            icon="🤖"
            subtitle={`${stats?.thisMonthPosts || 0} bu ay`}
            color="purple"
          />
          <StatsCard
            title="Gönderilen Post"
            value={stats?.totalPostsSent || 0}
            icon="📤"
            subtitle={`${stats?.totalPostsScheduled || 0} zamanlandı`}
            color="green"
          />
          <StatsCard
            title="Hazır Post"
            value={stats?.totalPostsReady || 0}
            icon="✅"
            subtitle={`Yayına hazır`}
            color="blue"
          />
        </div>

        {/* Platform İstatistikleri */}
        <div>
          <h2 className="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
            📱 Platform İstatistikleri
          </h2>
          <PlatformStats platformStats={platformStats} />
        </div>

        {/* Son Aktiviteler */}
        <div>
          <RecentActivities activities={activities} />
        </div>

        {/* Hızlı Aksiyonlar */}
        <div className="bg-white rounded-xl shadow-lg p-6">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">
            ⚡ Hızlı Aksiyonlar
          </h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
            <button className="p-4 border-2 border-indigo-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-400 transition-all duration-200 text-left">
              <div className="text-2xl mb-2">➕</div>
              <div className="font-medium text-gray-800">Yeni Müşteri Ekle</div>
              <div className="text-xs text-gray-500 mt-1">Hızlı müşteri oluştur</div>
            </button>
            
            <button className="p-4 border-2 border-purple-200 rounded-lg hover:bg-purple-50 hover:border-purple-400 transition-all duration-200 text-left">
              <div className="text-2xl mb-2">📝</div>
              <div className="font-medium text-gray-800">Yeni Post Oluştur</div>
              <div className="text-xs text-gray-500 mt-1">Manuel post ekle</div>
            </button>
            
            <button className="p-4 border-2 border-blue-200 rounded-lg hover:bg-blue-50 hover:border-blue-400 transition-all duration-200 text-left">
              <div className="text-2xl mb-2">📅</div>
              <div className="font-medium text-gray-800">Takvimi Görüntüle</div>
              <div className="text-xs text-gray-500 mt-1">Planlanan postlar</div>
            </button>
            
            <button className="p-4 border-2 border-green-200 rounded-lg hover:bg-green-50 hover:border-green-400 transition-all duration-200 text-left">
              <div className="text-2xl mb-2">📊</div>
              <div className="font-medium text-gray-800">Raporlar</div>
              <div className="text-xs text-gray-500 mt-1">Detaylı analizler</div>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
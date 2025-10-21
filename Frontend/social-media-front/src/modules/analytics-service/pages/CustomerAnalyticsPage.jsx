// src/modules/analytics-service/pages/CustomerAnalyticsPage.jsx

import { useEffect } from 'react';
import useCustomerDetail from '../hooks/useCustomerDetail';
import CustomerInfoSummary from '../components/CustomerAnalytics/CustomerInfoSummary';
import PostProgressCard from '../components/CustomerAnalytics/PostProgressCard';
import NotesCard from '../components/CustomerAnalytics/NotesCard';
import AIProcessBoard from '../components/CustomerAnalytics/AIProcessBoard';
import OnboardingProgressBoard from '../components/CustomerAnalytics/OnboardingProgressBoard';
import ScheduledPostsCard from '../components/CustomerAnalytics/ScheduledPostsCard';
import ActivityTimelineCard from '../components/CustomerAnalytics/ActivityTimelineCard';

export default function CustomerAnalyticsPage({ customerId, onNavigate }) {
  const {
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
  } = useCustomerDetail(customerId);

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
            <div className="flex gap-3 justify-center mt-6">
              <button
                onClick={refresh}
                className="px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
              >
                Tekrar Dene
              </button>
              <button
                onClick={() => onNavigate('musteriListesi')}
                className="px-6 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition"
              >
                Listeye Dön
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  // No customer
  if (!customer) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
        <div className="max-w-7xl mx-auto">
          <div className="bg-white rounded-2xl shadow-lg p-8 text-center">
            <span className="text-6xl">🤷</span>
            <h2 className="text-xl font-bold text-gray-800 mt-4">Müşteri Bulunamadı</h2>
            <p className="text-gray-600 mt-2">Bu müşteriye ait veri bulunamadı.</p>
            <button
              onClick={() => onNavigate('musteriListesi')}
              className="mt-6 px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
            >
              Listeye Dön
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
              📊 Müşteri Analizi
            </h1>
            <p className="text-gray-600 mt-1">
              {customer.companyName} - Süreç Takibi
            </p>
          </div>
          <div className="flex gap-2">
            <button
              onClick={() => onNavigate('musteriListesi')}
              className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-all duration-200 flex items-center gap-2"
            >
              ← Geri
            </button>
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
        </div>

        {/* Üst Kartlar - Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <CustomerInfoSummary customer={customer} />
          <PostProgressCard postStats={customer.postStats} />
          <NotesCard customer={customer} onAddNote={addNote} />
        </div>

        {/* AI İçerik Süreci Board */}
        <AIProcessBoard tasks={aiTasks} onUpdateTask={updateAITask} />

        {/* Onboarding Board */}
        <OnboardingProgressBoard tasks={onboardingTasks} onUpdateTask={updateOnboardingTask} />

        {/* Alt Kartlar - Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <ScheduledPostsCard posts={upcomingPosts} />
          <ActivityTimelineCard activities={activities} />
        </div>
      </div>
    </div>
  );
}
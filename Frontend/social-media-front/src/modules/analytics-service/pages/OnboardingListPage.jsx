// src/modules/analytics-service/pages/OnboardingListPage.jsx

import { useState, useEffect } from 'react';
import analyticsService from '../services/analyticsService';
import { getStatusLabel, getStatusColor, getPlatformIcon } from '../data/mockHelpers';
import CustomerSelector from '../components/CustomerAnalytics/CustomerSelector';
 
export default function OnboardingListPage() {
  const [selectedCustomerId, setSelectedCustomerId] = useState(null);
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [filterStatus, setFilterStatus] = useState('ALL');

  useEffect(() => {
    if (selectedCustomerId) {
      fetchOnboardingTasks();
    }
  }, [selectedCustomerId]);

  const fetchOnboardingTasks = async () => {
    if (!selectedCustomerId) return;
    
    setLoading(true);
    setError(null);

    try {
      const response = await analyticsService.getOnboardingTasks(selectedCustomerId);

      if (response.success) {
        setTasks(response.data || []);
      } else {
        setError(response.error);
      }
    } catch (err) {
      setError('Veriler yüklenirken hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  const statusOptions = [
    { value: 'ALL', label: 'Tümü', count: tasks.length },
    { value: 'NOT_STARTED', label: 'Başlanmadı', count: tasks.filter(t => t.status === 'NOT_STARTED').length },
    { value: 'IN_PROGRESS', label: 'Devam Ediyor', count: tasks.filter(t => t.status === 'IN_PROGRESS').length },
    { value: 'COMPLETED', label: 'Tamamlandı', count: tasks.filter(t => t.status === 'COMPLETED').length }
  ];

  const filteredTasks = filterStatus === 'ALL'
    ? tasks
    : tasks.filter(t => t.status === filterStatus);

  const completedCount = tasks.filter(t => t.status === 'COMPLETED').length;
  const completionRate = tasks.length > 0 ? Math.round((completedCount / tasks.length) * 100) : 0;

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-800 flex items-center gap-3">
              🚀 Onboarding Takibi
            </h1>
            <p className="text-gray-600 mt-1">
              Müşteri kurulum sürecini takip edin
            </p>
          </div>
          {selectedCustomerId && (
            <button
              onClick={fetchOnboardingTasks}
              className="px-4 py-2 bg-white text-green-600 border border-green-200 rounded-lg hover:bg-green-50 transition-all duration-200 flex items-center gap-2 shadow-sm"
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
              Yenile
            </button>
          )}
        </div>

        {/* Customer Selector */}
        <CustomerSelector
          selectedCustomerId={selectedCustomerId}
          onCustomerSelect={setSelectedCustomerId}
        />

        {/* Content */}
        {!selectedCustomerId ? (
          <div className="bg-white rounded-xl shadow-lg p-12 text-center">
            <span className="text-6xl">👆</span>
            <p className="text-gray-700 mt-4 text-lg font-medium">Lütfen yukarıdan bir müşteri seçin</p>
            <p className="text-gray-500 text-sm mt-2">Seçtiğiniz müşterinin onboarding görevleri görüntülenecek</p>
          </div>
        ) : loading ? (
          <div className="bg-white rounded-xl shadow-lg p-12 text-center">
            <div className="inline-block animate-spin rounded-full h-12 w-12 border-4 border-green-500 border-t-transparent"></div>
            <p className="text-gray-600 mt-4 font-medium">Yükleniyor...</p>
          </div>
        ) : error ? (
          <div className="bg-white rounded-2xl shadow-lg p-8 text-center">
            <span className="text-6xl">⚠️</span>
            <h2 className="text-xl font-bold text-gray-800 mt-4">Bir Hata Oluştu</h2>
            <p className="text-gray-600 mt-2">{error}</p>
            <button
              onClick={fetchOnboardingTasks}
              className="mt-6 px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition"
            >
              Tekrar Dene
            </button>
          </div>
        ) : (
          <>
            {/* Progress Summary */}
            <div className="bg-white rounded-xl shadow-lg p-6">
              <div className="flex items-center justify-between mb-4">
                <h3 className="text-lg font-semibold text-gray-800">Genel İlerleme</h3>
                <span className="text-2xl font-bold text-green-600">{completionRate}%</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-3">
                <div
                  className="bg-green-500 h-3 rounded-full transition-all duration-300"
                  style={{ width: `${completionRate}%` }}
                ></div>
              </div>
              <p className="text-sm text-gray-700 mt-2 font-medium">
                {completedCount} / {tasks.length} görev tamamlandı
              </p>
            </div>

            {/* Filter Tabs */}
            <div className="bg-white rounded-xl shadow-lg p-4">
              <div className="flex flex-wrap gap-2">
                {statusOptions.map(option => (
                  <button
                    key={option.value}
                    onClick={() => setFilterStatus(option.value)}
                    className={`px-4 py-2 rounded-lg transition-all duration-200 font-medium ${
                      filterStatus === option.value
                        ? 'bg-green-600 text-white shadow-md'
                        : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                    }`}
                  >
                    {option.label} ({option.count})
                  </button>
                ))}
              </div>
            </div>

            {/* Tasks List */}
            {filteredTasks.length === 0 ? (
              <div className="bg-white rounded-xl shadow-lg p-12 text-center">
                <span className="text-6xl">📭</span>
                <p className="text-gray-700 mt-4 font-medium">Görev bulunamadı</p>
              </div>
            ) : (
              <div className="bg-white rounded-xl shadow-lg overflow-hidden">
                <div className="divide-y divide-gray-100">
                  {filteredTasks.map(task => (
                    <div
                      key={task.id}
                      className="p-6 hover:bg-gray-50 transition-colors duration-200"
                    >
                      <div className="flex items-start justify-between">
                        <div className="flex-1">
                          <div className="flex items-center gap-3 mb-2">
                            {task.platform && (
                              <span className="text-2xl">{getPlatformIcon(task.platform)}</span>
                            )}
                            <h3 className="text-lg font-semibold text-gray-800">{task.taskName}</h3>
                          </div>
                          {task.notes && (
                            <p className="text-sm text-gray-600 mb-3">{task.notes}</p>
                          )}
                          {task.connectionDate && (
                            <p className="text-xs text-gray-500">
                              🔗 Bağlantı Tarihi: {new Date(task.connectionDate).toLocaleDateString('tr-TR', {
                                day: 'numeric',
                                month: 'long',
                                year: 'numeric'
                              })}
                            </p>
                          )}
                        </div>
                        <span className={`px-4 py-2 rounded-full text-sm font-medium border whitespace-nowrap ${getStatusColor(task.status)}`}>
                          {task.status === 'COMPLETED' && '✅ '}
                          {task.status === 'NOT_STARTED' && '⏸️ '}
                          {task.status === 'IN_PROGRESS' && '🔄 '}
                          {getStatusLabel(task.status)}
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}
// src/modules/analytics-service/pages/AITaskListPage.jsx

import { useState, useEffect } from 'react';
import analyticsService from '../services/analyticsService';
import { getStatusLabel, getStatusColor, calculateProgress } from '../data/mockHelpers';
import CustomerSelector from '../components/CustomerAnalytics/CustomerSelector';
 

export default function AITaskListPage() {
  const [selectedCustomerId, setSelectedCustomerId] = useState(null);
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [filterStatus, setFilterStatus] = useState('ALL');

  useEffect(() => {
    if (selectedCustomerId) {
      fetchAITasks();
    }
  }, [selectedCustomerId]);

  const fetchAITasks = async () => {
    if (!selectedCustomerId) return;
    
    setLoading(true);
    setError(null);

    try {
      const response = await analyticsService.getAIContentTasks(selectedCustomerId);

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
    { value: 'COMPLETED', label: 'Tamamlandı', count: tasks.filter(t => t.status === 'COMPLETED').length },
    { value: 'PENDING', label: 'Bekliyor', count: tasks.filter(t => t.status === 'PENDING').length }
  ];

  const filteredTasks = filterStatus === 'ALL' 
    ? tasks 
    : tasks.filter(t => t.status === filterStatus);

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-800 flex items-center gap-3">
              🤖 AI İçerik Takibi
            </h1>
            <p className="text-gray-600 mt-1">
              Müşterinin AI içerik görevlerini takip edin
            </p>
          </div>
          {selectedCustomerId && (
            <button
              onClick={fetchAITasks}
              className="px-4 py-2 bg-white text-indigo-600 border border-indigo-200 rounded-lg hover:bg-indigo-50 transition-all duration-200 flex items-center gap-2 shadow-sm"
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

        {/* Content - Sadece müşteri seçildiyse göster */}
        {!selectedCustomerId ? (
          <div className="bg-white rounded-xl shadow-lg p-12 text-center">
            <span className="text-6xl">👆</span>
            <p className="text-gray-700 mt-4 text-lg font-medium">Lütfen yukarıdan bir müşteri seçin</p>
            <p className="text-gray-500 text-sm mt-2">Seçtiğiniz müşterinin AI içerik görevleri görüntülenecek</p>
          </div>
        ) : loading ? (
          <div className="bg-white rounded-xl shadow-lg p-12 text-center">
            <div className="inline-block animate-spin rounded-full h-12 w-12 border-4 border-indigo-500 border-t-transparent"></div>
            <p className="text-gray-600 mt-4 font-medium">Yükleniyor...</p>
          </div>
        ) : error ? (
          <div className="bg-white rounded-2xl shadow-lg p-8 text-center">
            <span className="text-6xl">⚠️</span>
            <h2 className="text-xl font-bold text-gray-800 mt-4">Bir Hata Oluştu</h2>
            <p className="text-gray-600 mt-2">{error}</p>
            <button
              onClick={fetchAITasks}
              className="mt-6 px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
            >
              Tekrar Dene
            </button>
          </div>
        ) : (
          <>
            {/* Filter Tabs */}
            <div className="bg-white rounded-xl shadow-lg p-4">
              <div className="flex flex-wrap gap-2">
                {statusOptions.map(option => (
                  <button
                    key={option.value}
                    onClick={() => setFilterStatus(option.value)}
                    className={`px-4 py-2 rounded-lg transition-all duration-200 font-medium ${
                      filterStatus === option.value
                        ? 'bg-indigo-600 text-white shadow-md'
                        : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                    }`}
                  >
                    {option.label} ({option.count})
                  </button>
                ))}
              </div>
            </div>

            {/* Tasks Grid */}
            {filteredTasks.length === 0 ? (
              <div className="bg-white rounded-xl shadow-lg p-12 text-center">
                <span className="text-6xl">📭</span>
                <p className="text-gray-700 mt-4 font-medium">Görev bulunamadı</p>
                <p className="text-gray-500 text-sm mt-2">Bu filtre için görev bulunmuyor</p>
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {filteredTasks.map(task => {
                  const progress = task.progressTotal > 0
                    ? calculateProgress(task.progressCurrent, task.progressTotal)
                    : 0;

                  return (
                    <div
                      key={task.id}
                      className="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-all duration-200 hover:scale-[1.02]"
                    >
                      {/* Header */}
                      <div className="bg-gradient-to-r from-purple-600 to-indigo-600 p-4">
                        <h3 className="text-white font-semibold">{task.taskName}</h3>
                        <p className="text-purple-100 text-sm mt-1">{task.taskType}</p>
                      </div>

                      {/* Content */}
                      <div className="p-4 space-y-3">
                        {/* Status */}
                        <div className="flex items-center justify-between">
                          <span className="text-sm text-gray-700 font-medium">Durum:</span>
                          <span className={`px-3 py-1 rounded-full text-xs font-medium border ${getStatusColor(task.status)}`}>
                            {getStatusLabel(task.status)}
                          </span>
                        </div>

                        {/* Progress */}
                        {task.progressTotal > 0 && (
                          <div>
                            <div className="flex justify-between text-xs text-gray-700 mb-1">
                              <span className="font-medium">İlerleme</span>
                              <span className="font-semibold">{task.progressCurrent}/{task.progressTotal} ({progress}%)</span>
                            </div>
                            <div className="w-full bg-gray-200 rounded-full h-2">
                              <div
                                className={`h-2 rounded-full transition-all duration-300 ${
                                  progress === 100 ? 'bg-green-500' :
                                  progress >= 50 ? 'bg-blue-500' : 'bg-yellow-500'
                                }`}
                                style={{ width: `${progress}%` }}
                              ></div>
                            </div>
                          </div>
                        )}

                        {/* Notes */}
                        {task.notes && (
                          <p className="text-xs text-gray-600 line-clamp-2">{task.notes}</p>
                        )}

                        {/* Dates */}
                        <div className="flex items-center justify-between text-xs text-gray-600 pt-2 border-t">
                          {task.startedAt && (
                            <span>🚀 {new Date(task.startedAt).toLocaleDateString('tr-TR')}</span>
                          )}
                          {task.completedAt && (
                            <span>✅ {new Date(task.completedAt).toLocaleDateString('tr-TR')}</span>
                          )}
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}
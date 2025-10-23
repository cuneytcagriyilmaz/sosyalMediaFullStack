// src/modules/analytics-service/components/Modals/AddOnboardingTaskModal.jsx

import { useState } from 'react';

export default function AddOnboardingTaskModal({ customerId, onClose, onSave }) {
  const [formData, setFormData] = useState({
    taskName: '',
    taskType: 'SOCIAL_MEDIA_CONNECT',
    platform: 'INSTAGRAM',
    status: 'NOT_STARTED',
    connectionStatus: false,
    notes: ''
  });
  const [loading, setLoading] = useState(false);

  const platforms = [
    { value: 'INSTAGRAM', label: '📷 Instagram' },
    { value: 'TIKTOK', label: '🎵 TikTok' },
    { value: 'FACEBOOK', label: '👍 Facebook' },
    { value: 'YOUTUBE', label: '▶️ YouTube' }
  ];

  const statusOptions = [
    { value: 'NOT_STARTED', label: 'Başlanmadı' },
    { value: 'IN_PROGRESS', label: 'Devam Ediyor' },
    { value: 'COMPLETED', label: 'Tamamlandı' }
  ];

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    const result = await onSave({
      customerId,
      ...formData,
      connectionDate: formData.connectionStatus ? new Date().toISOString() : null
    });

    setLoading(false);
    
    if (result.success) {
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 animate-fade-in">
      {/* Overlay */}
      <div 
        className="absolute inset-0 bg-black/50 backdrop-blur-sm"
        onClick={!loading ? onClose : undefined}
      />

      {/* Modal */}
      <div className="relative bg-white rounded-2xl shadow-2xl max-w-xl w-full max-h-[90vh] overflow-y-auto animate-scale-in">
        <form onSubmit={handleSubmit}>
          {/* Header */}
          <div className="bg-gradient-to-r from-green-600 to-teal-600 p-6 rounded-t-2xl">
            <h3 className="text-xl font-bold text-white">
              🚀 Yeni Onboarding Görevi Ekle
            </h3>
            <p className="text-green-100 text-sm mt-1">
              Müşteri için yeni bir kurulum görevi oluşturun
            </p>
          </div>

          {/* Content */}
          <div className="p-6 space-y-4">
            {/* Görev Adı */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Görev Adı *
              </label>
              <input
                type="text"
                required
                value={formData.taskName}
                onChange={(e) => setFormData({ ...formData, taskName: e.target.value })}
                placeholder="Örn: Instagram hesabı bağla"
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 text-gray-900 bg-white"
              />
            </div>

            {/* Platform */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Platform *
              </label>
              <select
                required
                value={formData.platform}
                onChange={(e) => setFormData({ ...formData, platform: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 text-gray-900 bg-white"
              >
                {platforms.map(platform => (
                  <option key={platform.value} value={platform.value}>
                    {platform.label}
                  </option>
                ))}
              </select>
            </div>

            {/* Durum */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Durum *
              </label>
              <select
                required
                value={formData.status}
                onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 text-gray-900 bg-white"
              >
                {statusOptions.map(status => (
                  <option key={status.value} value={status.value}>
                    {status.label}
                  </option>
                ))}
              </select>
            </div>

            {/* Bağlantı Durumu */}
            <div className="flex items-center gap-3 p-4 bg-gray-50 rounded-lg">
              <input
                type="checkbox"
                id="connectionStatus"
                checked={formData.connectionStatus}
                onChange={(e) => setFormData({ ...formData, connectionStatus: e.target.checked })}
                className="w-5 h-5 text-green-600 rounded focus:ring-2 focus:ring-green-500"
              />
              <label htmlFor="connectionStatus" className="text-sm font-medium text-gray-700 cursor-pointer">
                Bağlantı başarıyla kuruldu
              </label>
            </div>

            {/* Notlar */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Notlar
              </label>
              <textarea
                value={formData.notes}
                onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                rows="3"
                placeholder="Görev hakkında notlar..."
                className="w-full px-3 py-2 border border-gray-300 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-green-500 text-gray-900 bg-white"
              />
            </div>
          </div>

          {/* Footer */}
          <div className="flex gap-3 p-6 pt-0">
            <button
              type="button"
              onClick={onClose}
              disabled={loading}
              className="flex-1 px-4 py-3 bg-gray-200 text-gray-800 rounded-lg font-medium hover:bg-gray-300 transition disabled:opacity-50"
            >
              İptal
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 px-4 py-3 bg-green-600 text-white rounded-lg font-medium hover:bg-green-700 transition disabled:opacity-50"
            >
              {loading ? 'Ekleniyor...' : '✓ Görev Ekle'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
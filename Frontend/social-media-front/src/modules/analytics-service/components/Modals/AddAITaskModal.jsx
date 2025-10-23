// src/modules/analytics-service/components/Modals/AddAITaskModal.jsx

import { useState } from 'react';

export default function AddAITaskModal({ customerId, onClose, onSave }) {
  const [formData, setFormData] = useState({
    taskName: '',
    taskType: 'AI_GENERATION',
    status: 'NOT_STARTED',
    quantity: 10,
    progressCurrent: 0,
    progressTotal: 10,
    notes: ''
  });
  const [loading, setLoading] = useState(false);

  const taskTypes = [
    { value: 'HASHTAG_ANALYSIS', label: 'Hashtag Analizi' },
    { value: 'PHOTO_UPLOAD', label: 'Fotoğraf Yükleme' },
    { value: 'AI_GENERATION', label: 'AI Post Üretimi' },
    { value: 'CONTENT_REVIEW', label: 'İçerik İncelemesi' },
    { value: 'CUSTOMER_APPROVAL', label: 'Müşteri Onayı' }
  ];

  const statusOptions = [
    { value: 'NOT_STARTED', label: 'Başlanmadı' },
    { value: 'IN_PROGRESS', label: 'Devam Ediyor' },
    { value: 'COMPLETED', label: 'Tamamlandı' },
    { value: 'PENDING', label: 'Bekliyor' }
  ];

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    const result = await onSave({
      customerId,
      ...formData,
      quantity: parseInt(formData.quantity) || 0,
      progressCurrent: parseInt(formData.progressCurrent) || 0,
      progressTotal: parseInt(formData.progressTotal) || 0
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
      <div className="relative bg-white rounded-2xl shadow-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto animate-scale-in">
        <form onSubmit={handleSubmit}>
          {/* Header */}
          <div className="bg-gradient-to-r from-purple-600 to-indigo-600 p-6 rounded-t-2xl sticky top-0 z-10">
            <h3 className="text-xl font-bold text-white">
              🤖 Yeni AI İçerik Görevi Ekle
            </h3>
            <p className="text-purple-100 text-sm mt-1">
              Müşteri için yeni bir AI içerik görevi oluşturun
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
                placeholder="Örn: Instagram için 100 post üret"
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
              />
            </div>

            {/* Görev Tipi */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Görev Tipi *
              </label>
              <select
                required
                value={formData.taskType}
                onChange={(e) => setFormData({ ...formData, taskType: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
              >
                {taskTypes.map(type => (
                  <option key={type.value} value={type.value}>
                    {type.label}
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
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
              >
                {statusOptions.map(status => (
                  <option key={status.value} value={status.value}>
                    {status.label}
                  </option>
                ))}
              </select>
            </div>

            {/* İlerleme */}
            <div className="grid grid-cols-3 gap-3">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Miktar
                </label>
                <input
                  type="number"
                  min="0"
                  value={formData.quantity}
                  onChange={(e) => setFormData({ ...formData, quantity: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Tamamlanan
                </label>
                <input
                  type="number"
                  min="0"
                  value={formData.progressCurrent}
                  onChange={(e) => setFormData({ ...formData, progressCurrent: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Hedef
                </label>
                <input
                  type="number"
                  min="0"
                  value={formData.progressTotal}
                  onChange={(e) => setFormData({ ...formData, progressTotal: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
                />
              </div>
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
                className="w-full px-3 py-2 border border-gray-300 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
              />
            </div>
          </div>

          {/* Footer */}
          <div className="flex gap-3 p-6 pt-0 sticky bottom-0 bg-white">
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
              className="flex-1 px-4 py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition disabled:opacity-50"
            >
              {loading ? 'Ekleniyor...' : '✓ Görev Ekle'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
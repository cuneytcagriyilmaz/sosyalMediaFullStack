// src/modules/analytics-service/components/CustomerAnalytics/TaskEditModal.jsx

import { useState, useEffect } from 'react';
import { getStatusLabel } from '../../data/mockHelpers';

export default function TaskEditModal({ task, onClose, onSave, title }) {
  const [formData, setFormData] = useState({
    status: task.status || 'NOT_STARTED',
    progressCurrent: task.progressCurrent || 0,
    progressTotal: task.progressTotal || 0,
    notes: task.notes || '',
    completedAt: task.completedAt || ''
  });
  const [loading, setLoading] = useState(false);

  // ESC tuşu ile kapatma
  useEffect(() => {
    const handleEsc = (e) => {
      if (e.key === 'Escape' && !loading) {
        onClose();
      }
    };
    window.addEventListener('keydown', handleEsc);
    return () => window.removeEventListener('keydown', handleEsc);
  }, [loading, onClose]);

  // Body scroll kilitle
  useEffect(() => {
    document.body.style.overflow = 'hidden';
    return () => {
      document.body.style.overflow = 'unset';
    };
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    // Eğer COMPLETED seçildiyse, completedAt'i şimdi olarak set et
    const dataToSave = {
      ...formData,
      completedAt: formData.status === 'COMPLETED' 
        ? (formData.completedAt || new Date().toISOString())
        : null
    };

    await onSave(task.id, dataToSave);
    setLoading(false);
  };

  const statusOptions = [
    { value: 'NOT_STARTED', label: 'Başlanmadı' },
    { value: 'IN_PROGRESS', label: 'Devam Ediyor' },
    { value: 'COMPLETED', label: 'Tamamlandı' },
    { value: 'PENDING', label: 'Bekliyor' },
    { value: 'CANCELLED', label: 'İptal Edildi' }
  ];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 animate-fade-in">
      {/* Overlay */}
      <div 
        className="absolute inset-0 bg-black/50 backdrop-blur-sm"
        onClick={!loading ? onClose : undefined}
      />

      {/* Modal */}
      <div className="relative bg-white rounded-2xl shadow-2xl max-w-md w-full animate-scale-in">
        <form onSubmit={handleSubmit}>
          {/* Header */}
          <div className="bg-gradient-to-r from-indigo-600 to-purple-600 p-6 rounded-t-2xl">
            <h3 className="text-xl font-bold text-white">
              {title}
            </h3>
            <p className="text-indigo-100 text-sm mt-1">{task.taskName}</p>
          </div>

          {/* Content */}
          <div className="p-6 space-y-4">
            {/* Durum */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Durum
              </label>
              <select
                value={formData.status}
                onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
                disabled={loading}
              >
                {statusOptions.map(option => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>

            {/* İlerleme (eğer progressTotal varsa) */}
            {task.progressTotal > 0 && (
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  İlerleme
                </label>
                <div className="flex gap-2 items-center">
                  <input
                    type="number"
                    min="0"
                    max={formData.progressTotal}
                    value={formData.progressCurrent}
                    onChange={(e) => setFormData({ ...formData, progressCurrent: parseInt(e.target.value) || 0 })}
                    className="flex-1 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
                    disabled={loading}
                  />
                  <span className="text-gray-600">/</span>
                  <input
                    type="number"
                    min="0"
                    value={formData.progressTotal}
                    onChange={(e) => setFormData({ ...formData, progressTotal: parseInt(e.target.value) || 0 })}
                    className="flex-1 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
                    disabled={loading}
                  />
                </div>
              </div>
            )}

            {/* Notlar */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Notlar
              </label>
              <textarea
                value={formData.notes}
                onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                rows="3"
                className="w-full px-3 py-2 border border-gray-300 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white"
                placeholder="Görev hakkında notlar..."
                disabled={loading}
              />
            </div>
          </div>

          {/* Footer */}
          <div className="flex gap-3 p-6 pt-0">
            <button
              type="button"
              onClick={onClose}
              disabled={loading}
              className="flex-1 px-4 py-3 bg-gray-200 text-gray-800 rounded-lg font-medium hover:bg-gray-300 transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
              İptal
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 px-4 py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Kaydediliyor...' : 'Kaydet'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
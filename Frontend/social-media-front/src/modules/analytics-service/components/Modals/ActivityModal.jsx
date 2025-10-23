// src/modules/analytics-service/components/ActivityModal.jsx

import { useState, useEffect } from 'react';
import { X, Plus, Trash2 } from 'lucide-react';

const ACTIVITY_TYPES = [
  { value: 'POST_SENT', label: '📤 Post Gönderildi', icon: '📤' },
  { value: 'POST_READY', label: '✅ Post Hazır', icon: '✅' },
  { value: 'AI_COMPLETED', label: '🤖 AI Tamamlandı', icon: '🤖' },
  { value: 'NEW_CUSTOMER', label: '👤 Yeni Müşteri', icon: '👤' },
  { value: 'CUSTOMER_UPDATED', label: '✏️ Müşteri Güncellendi', icon: '✏️' },
  { value: 'CONTENT_APPROVED', label: '👍 İçerik Onaylandı', icon: '👍' },
  { value: 'MEDIA_UPLOADED', label: '📸 Medya Yüklendi', icon: '📸' },
  { value: 'DEADLINE_APPROACHING', label: '⏰ Deadline Yaklaşıyor', icon: '⏰' },
];

const createEmptyActivity = () => ({
  id: Date.now() + Math.random(), // Geçici ID
  customerId: '',
  activityType: 'POST_SENT',
  message: '',
  icon: '📤'
});

export default function ActivityModal({ 
  isOpen, 
  onClose, 
  onSave, 
  activity = null,
  customers = [] 
}) {
  const [activities, setActivities] = useState([createEmptyActivity()]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (activity) {
      // Edit mode - tek aktivite
      setActivities([{
        id: activity.id,
        customerId: activity.customerId || '',
        activityType: activity.activityType || 'POST_SENT',
        message: activity.message || '',
        icon: activity.icon || '📤'
      }]);
    } else {
      // Create mode - boş form
      setActivities([createEmptyActivity()]);
    }
    setError(null);
  }, [activity, isOpen]);

  const handleTypeChange = (index, type) => {
    const selectedType = ACTIVITY_TYPES.find(t => t.value === type);
    const newActivities = [...activities];
    newActivities[index] = {
      ...newActivities[index],
      activityType: type,
      icon: selectedType?.icon || '📤'
    };
    setActivities(newActivities);
  };

  const handleFieldChange = (index, field, value) => {
    const newActivities = [...activities];
    newActivities[index] = {
      ...newActivities[index],
      [field]: value
    };
    setActivities(newActivities);
  };

  const handleAddActivity = () => {
    setActivities([...activities, createEmptyActivity()]);
  };

  const handleRemoveActivity = (index) => {
    if (activities.length === 1) {
      alert('En az bir aktivite olmalı!');
      return;
    }
    const newActivities = activities.filter((_, i) => i !== index);
    setActivities(newActivities);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // Validation
      for (let i = 0; i < activities.length; i++) {
        if (!activities[i].activityType || !activities[i].message) {
          setError(`Aktivite ${i + 1}: Lütfen tüm zorunlu alanları doldurun`);
          setLoading(false);
          return;
        }
      }

      // Prepare data
      const dataToSend = activities.map(a => ({
        customerId: a.customerId ? parseInt(a.customerId) : null,
        activityType: a.activityType,
        message: a.message,
        icon: a.icon
      }));

      if (activity) {
        // Edit mode - tek aktivite güncelle
        await onSave(dataToSend[0]);
      } else {
        // Create mode - tek veya çoklu ekle
        if (dataToSend.length === 1) {
          await onSave(dataToSend[0]);
        } else {
          await onSave(dataToSend); // Bulk save
        }
      }

      onClose();
    } catch (err) {
      console.error('Form submit error:', err);
      setError(err.message || 'Bir hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="bg-gradient-to-r from-blue-600 to-indigo-600 p-6 rounded-t-2xl sticky top-0 z-10">
          <div className="flex items-center justify-between">
            <h2 className="text-2xl font-bold text-white flex items-center gap-3">
              {activity ? '✏️ Aktivite Düzenle' : '➕ Aktivite Ekle'}
              {!activity && activities.length > 1 && (
                <span className="text-sm font-normal bg-white/20 px-3 py-1 rounded-full">
                  {activities.length} Aktivite
                </span>
              )}
            </h2>
            <button
              onClick={onClose}
              className="text-white hover:bg-white/20 rounded-lg p-2 transition"
            >
              <X size={24} />
            </button>
          </div>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          {/* Error Message */}
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-4 flex items-start gap-3">
              <span className="text-2xl">⚠️</span>
              <div className="flex-1">
                <p className="text-red-800 font-medium">Hata</p>
                <p className="text-red-600 text-sm">{error}</p>
              </div>
            </div>
          )}

          {/* Activities List */}
          <div className="space-y-6">
            {activities.map((activityItem, index) => (
              <div
                key={activityItem.id}
                className="border border-gray-200 rounded-xl p-4 bg-gray-50 relative"
              >
                {/* Remove Button */}
                {!activity && activities.length > 1 && (
                  <button
                    type="button"
                    onClick={() => handleRemoveActivity(index)}
                    className="absolute top-4 right-4 p-2 text-red-600 hover:bg-red-100 rounded-lg transition"
                    title="Kaldır"
                  >
                    <Trash2 size={18} />
                  </button>
                )}

                {/* Activity Number */}
                {!activity && activities.length > 1 && (
                  <div className="mb-4">
                    <span className="inline-block px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-sm font-medium">
                      Aktivite {index + 1}
                    </span>
                  </div>
                )}

                <div className="space-y-4">
                  {/* Customer Select */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Müşteri (Opsiyonel)
                    </label>
                    <select
                      value={activityItem.customerId}
                      onChange={(e) => handleFieldChange(index, 'customerId', e.target.value)}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white text-gray-900"
                    >
                      <option value="">Sistem Geneli (Müşteri Yok)</option>
                      {customers.map(customer => (
                        <option key={customer.id} value={customer.id}>
                          {customer.companyName || customer.company_name || `Müşteri #${customer.id}`}
                        </option>
                      ))}
                    </select>
                  </div>

                  {/* Activity Type */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Aktivite Tipi <span className="text-red-500">*</span>
                    </label>
                    <select
                      value={activityItem.activityType}
                      onChange={(e) => handleTypeChange(index, e.target.value)}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white text-gray-900"
                      required
                    >
                      {ACTIVITY_TYPES.map(type => (
                        <option key={type.value} value={type.value}>
                          {type.label}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                    {/* Icon */}
                    <div className="md:col-span-1">
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        İkon
                      </label>
                      <input
                        type="text"
                        value={activityItem.icon}
                        onChange={(e) => handleFieldChange(index, 'icon', e.target.value)}
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-2xl text-center bg-white"
                        placeholder="📤"
                        maxLength={2}
                      />
                    </div>

                    {/* Message */}
                    <div className="md:col-span-3">
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Mesaj <span className="text-red-500">*</span>
                      </label>
                      <input
                        type="text"
                        value={activityItem.message}
                        onChange={(e) => handleFieldChange(index, 'message', e.target.value)}
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white text-gray-900"
                        placeholder="Örn: Instagram'da 5 post paylaşıldı"
                        required
                      />
                    </div>
                  </div>

                  {/* Preview */}
                  <div className="bg-white rounded-lg p-3 border border-gray-200">
                    <div className="flex items-start gap-3">
                      <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center text-xl flex-shrink-0">
                        {activityItem.icon}
                      </div>
                      <div className="flex-1 min-w-0">
                        <p className="text-sm text-gray-800 break-words">
                          {activityItem.message || 'Mesajınız burada görünecek...'}
                        </p>
                        <p className="text-xs text-gray-500 mt-1">Şimdi</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* Add More Button */}
          {!activity && (
            <button
              type="button"
              onClick={handleAddActivity}
              className="w-full py-3 border-2 border-dashed border-blue-300 rounded-lg text-blue-600 font-medium hover:bg-blue-50 hover:border-blue-400 transition flex items-center justify-center gap-2"
            >
              <Plus size={20} />
              <span>Yeni Aktivite Ekle</span>
            </button>
          )}

          {/* Action Buttons */}
          <div className="flex gap-3 pt-4 border-t">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-6 py-3 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-gray-50 transition"
              disabled={loading}
            >
              İptal
            </button>
            <button
              type="submit"
              className="flex-1 px-6 py-3 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
              disabled={loading}
            >
              {loading ? (
                <>
                  <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                  <span>Kaydediliyor...</span>
                </>
              ) : (
                <>
                  {activity ? (
                    <>💾 Güncelle</>
                  ) : (
                    <>
                      ➕ {activities.length === 1 ? 'Ekle' : `${activities.length} Aktivite Ekle`}
                    </>
                  )}
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
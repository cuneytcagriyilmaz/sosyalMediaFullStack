// src/modules/analytics-service/pages/ActivityHistoryPage.jsx

import { useState, useEffect } from 'react';
import analyticsService from '../services/analyticsService';
import { getRelativeTime, getNotificationIcon } from '../data/mockHelpers';
import ActivityModal from '../components/Modals/ActivityModal';
import customerService from '../../customer-service/services/customerService';

export default function ActivityHistoryPage() {
    const [activities, setActivities] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [filterType, setFilterType] = useState('ALL');
    const [limit, setLimit] = useState(50);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingActivity, setEditingActivity] = useState(null);

    useEffect(() => {
        fetchActivities();
        fetchCustomers();
    }, [limit, filterType]);

    const fetchActivities = async () => {
        setLoading(true);
        setError(null);

        try {
            let response;

            if (filterType === 'ALL') {
                response = await analyticsService.getRecentActivities(limit);
            } else {
                response = await analyticsService.getActivitiesByType(filterType, limit);
            }

            if (response.success) {
                // ✅ Müşteri bilgilerini backend'den getir ve aktivitelere ekle
                const activitiesWithCustomers = await enrichActivitiesWithCustomers(response.data || []);
                setActivities(activitiesWithCustomers);
            } else {
                setError(response.error);
            }
        } catch (err) {
            console.error('Aktiviteler yüklenemedi:', err);
            setError('Veriler yüklenirken hata oluştu');
        } finally {
            setLoading(false);
        }
    };

    /**
     * Aktivitelere müşteri bilgilerini ekle (✅ YENİ)
     */
    const enrichActivitiesWithCustomers = async (activities) => {
        // Müşteri ID'lerini topla
        const customerIds = [...new Set(
            activities
                .filter(a => a.customerId)
                .map(a => a.customerId)
        )];

        if (customerIds.length === 0) {
            return activities;
        }

        // Müşterileri getir
        const customerMap = {};
        for (const customerId of customerIds) {
            try {
                const response = await customerService.getCustomerById(customerId);
                customerMap[customerId] = response;
            } catch (err) {
                console.error(`Müşteri ${customerId} alınamadı:`, err);
            }
        }

        // Aktivitelere müşteri bilgilerini ekle
        return activities.map(activity => ({
            ...activity,
            customerName: activity.customerId && customerMap[activity.customerId]
                ? (customerMap[activity.customerId].companyName ||
                    customerMap[activity.customerId].company_name ||
                    `Müşteri #${activity.customerId}`)
                : null
        }));
    };

    const fetchCustomers = async () => {
        try {
            console.log('🔄 Müşteriler yükleniyor...');

            const response = await customerService.getAllCustomers();

            console.log('✅ Customers response:', response);

            if (response.success && response.data) {
                setCustomers(response.data);
                console.log('✅ Customers set:', response.data.length, 'müşteri');
            } else {
                setCustomers([]);
                console.warn('⚠️ Müşteri verisi bulunamadı');
            }
        } catch (err) {
            console.error('❌ Müşteriler yüklenemedi:', err);
            setCustomers([]);
        }
    };

    /**
     * Yeni aktivite ekle (tek veya çoklu)
     */
    const handleAddActivity = async (activityData) => {
        // Eğer array ise bulk, değilse single
        if (Array.isArray(activityData)) {
            // Bulk insert
            const response = await analyticsService.createActivitiesBulk(activityData);

            if (response.success) {
                await fetchActivities();
                alert(`✅ ${activityData.length} aktivite başarıyla eklendi!`);
            } else {
                alert('❌ Hata: ' + response.error);
                throw new Error(response.error);
            }
        } else {
            // Single insert
            const response = await analyticsService.createActivity(activityData);

            if (response.success) {
                await fetchActivities();
                alert('✅ Aktivite başarıyla eklendi!');
            } else {
                alert('❌ Hata: ' + response.error);
                throw new Error(response.error);
            }
        }
    };

    /**
     * Aktivite güncelle
     */
    const handleEditActivity = async (activityData) => {
        if (!editingActivity) {
            alert('⚠️ Düzenlenecek aktivite seçilmedi!');
            return;
        }

        const response = await analyticsService.updateActivity(editingActivity.id, activityData);

        if (response.success) {
            await fetchActivities();
            alert('✅ Aktivite başarıyla güncellendi!');
        } else {
            alert('❌ Hata: ' + response.error);
            throw new Error(response.error);
        }
    };

    /**
     * Aktivite sil
     */
    const handleDeleteActivity = async (activityId) => {
        if (!confirm('Bu aktiviteyi silmek istediğinize emin misiniz?')) {
            return;
        }

        const response = await analyticsService.deleteActivity(activityId);

        if (response.success) {
            await fetchActivities();
            alert('✅ Aktivite başarıyla silindi!');
        } else {
            alert('❌ Hata: ' + response.error);
        }
    };



    const activityTypes = [
        { value: 'ALL', label: 'Tümü' },
        { value: 'POST_SENT', label: '📤 Post Gönderildi' },
        { value: 'POST_READY', label: '✅ Post Hazır' },
        { value: 'AI_COMPLETED', label: '🤖 AI Tamamlandı' },
        { value: 'NEW_CUSTOMER', label: '👤 Yeni Müşteri' },
        { value: 'CUSTOMER_UPDATED', label: '✏️ Müşteri Güncellendi' },
        { value: 'CONTENT_APPROVED', label: '👍 İçerik Onaylandı' },
        { value: 'MEDIA_UPLOADED', label: '📸 Medya Yüklendi' },
        { value: 'DEADLINE_APPROACHING', label: '⏰ Deadline Yaklaşıyor' }
    ];

    const filteredActivities = activities;

    const stats = {
        total: activities.length,
        postSent: activities.filter(a => a.activityType === 'POST_SENT').length,
        aiCompleted: activities.filter(a => a.activityType === 'AI_COMPLETED').length,
        newCustomer: activities.filter(a => a.activityType === 'NEW_CUSTOMER').length
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
                <div className="max-w-7xl mx-auto">
                    <div className="flex justify-center items-center py-20">
                        <div className="text-center">
                            <div className="inline-block animate-spin rounded-full h-12 w-12 border-4 border-blue-500 border-t-transparent"></div>
                            <p className="text-blue-600 mt-4 font-medium">Aktiviteler yükleniyor...</p>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
                <div className="max-w-7xl mx-auto">
                    <div className="bg-white rounded-2xl shadow-lg p-8 text-center">
                        <span className="text-6xl">⚠️</span>
                        <h2 className="text-xl font-bold text-gray-800 mt-4">Bir Hata Oluştu</h2>
                        <p className="text-gray-600 mt-2">{error}</p>
                        <button
                            onClick={fetchActivities}
                            className="mt-6 px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
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
                            📜 Aktivite Geçmişi
                        </h1>
                        <p className="text-gray-600 mt-1">
                            Sistemdeki tüm aktiviteleri görüntüleyin
                        </p>
                    </div>
                    <div className="flex gap-2 flex-wrap">
                        <button
                            onClick={() => {
                                setEditingActivity(null);
                                setIsModalOpen(true);
                            }}
                            className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-all duration-200 flex items-center gap-2 shadow-md"
                        >
                            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                            </svg>
                            Yeni Aktivite
                        </button>

                        <button
                            onClick={fetchActivities}
                            className="px-4 py-2 bg-white text-blue-600 border border-blue-200 rounded-lg hover:bg-blue-50 transition-all duration-200 flex items-center gap-2 shadow-sm"
                        >
                            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                            </svg>
                            Yenile
                        </button>
                    </div>
                </div>

                {/* Stats Cards */}
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                    <div className="bg-white rounded-xl shadow-lg p-4">
                        <div className="text-3xl mb-2">📊</div>
                        <div className="text-2xl font-bold text-gray-800">{stats.total}</div>
                        <div className="text-sm text-gray-600">Toplam Aktivite</div>
                    </div>
                    <div className="bg-white rounded-xl shadow-lg p-4">
                        <div className="text-3xl mb-2">📤</div>
                        <div className="text-2xl font-bold text-gray-800">{stats.postSent}</div>
                        <div className="text-sm text-gray-600">Post Gönderildi</div>
                    </div>
                    <div className="bg-white rounded-xl shadow-lg p-4">
                        <div className="text-3xl mb-2">🤖</div>
                        <div className="text-2xl font-bold text-gray-800">{stats.aiCompleted}</div>
                        <div className="text-sm text-gray-600">AI Tamamlandı</div>
                    </div>
                    <div className="bg-white rounded-xl shadow-lg p-4">
                        <div className="text-3xl mb-2">👤</div>
                        <div className="text-2xl font-bold text-gray-800">{stats.newCustomer}</div>
                        <div className="text-sm text-gray-600">Yeni Müşteri</div>
                    </div>
                </div>

                {/* Filter & Limit Controls */}
                <div className="bg-white rounded-xl shadow-lg p-4">
                    <div className="flex flex-col md:flex-row gap-4">
                        <div className="flex-1">
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Filtrele
                            </label>
                            <select
                                value={filterType}
                                onChange={(e) => setFilterType(e.target.value)}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-900 bg-white"
                            >
                                {activityTypes.map(type => (
                                    <option key={type.value} value={type.value}>
                                        {type.label}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="md:w-48">
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Gösterim Sayısı
                            </label>
                            <select
                                value={limit}
                                onChange={(e) => setLimit(parseInt(e.target.value))}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-900 bg-white"
                            >
                                <option value={10}>Son 10</option>
                                <option value={20}>Son 20</option>
                                <option value={50}>Son 50</option>
                                <option value={100}>Son 100</option>
                            </select>
                        </div>
                    </div>
                </div>

                {/* Activities Timeline */}
                {filteredActivities.length === 0 ? (
                    <div className="bg-white rounded-xl shadow-lg p-12 text-center">
                        <span className="text-6xl">📭</span>
                        <p className="text-gray-700 mt-4 font-medium">Aktivite bulunamadı</p>
                        <p className="text-sm text-gray-500 mt-2">
                            {filterType === 'ALL'
                                ? 'Henüz hiç aktivite kaydedilmemiş'
                                : 'Bu filtre için aktivite bulunmuyor'}
                        </p>
                        <button
                            onClick={() => setIsModalOpen(true)}
                            className="mt-6 px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
                        >
                            İlk Aktiviteyi Ekle
                        </button>
                    </div>
                ) : (
                    <div className="bg-white rounded-xl shadow-lg overflow-hidden">
                        <div className="bg-gradient-to-r from-blue-600 to-indigo-600 p-4">
                            <h3 className="text-lg font-semibold text-white">
                                {filteredActivities.length} Aktivite Gösteriliyor
                            </h3>
                        </div>

                        <div className="p-6">
                            <div className="relative">
                                <div className="absolute left-6 top-0 bottom-0 w-0.5 bg-gray-200"></div>

                                <div className="space-y-8">
                                    {filteredActivities.map((activity) => (
                                        <div key={activity.id} className="relative pl-14">
                                            <div className="absolute left-0 w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center text-2xl z-10 border-4 border-white shadow-md">
                                                {activity.icon || getNotificationIcon(activity.activityType)}
                                            </div>

                                            <div className="bg-gray-50 p-4 rounded-lg border border-gray-200 hover:shadow-md transition-shadow duration-200">
                                                <div className="flex items-start justify-between">
                                                    <div className="flex-1">
                                                        <p className="text-sm font-medium text-gray-800">
                                                            {activity.message}
                                                        </p>
                                                        {activity.customerName && (
                                                            <p className="text-xs text-blue-600 mt-1 flex items-center gap-1">
                                                                <span>👤</span>
                                                                {activity.customerName}
                                                            </p>
                                                        )}
                                                    </div>
                                                    <div className="flex flex-col items-end gap-2 ml-4">
                                                        <span className="text-xs text-gray-500">
                                                            {getRelativeTime(activity.timestamp || activity.createdAt)}
                                                        </span>
                                                        <div className="flex gap-2">
                                                            {/* ✅ Düzenle Butonu */}
                                                            <button
                                                                onClick={() => {
                                                                    setEditingActivity(activity);
                                                                    setIsModalOpen(true);
                                                                }}
                                                                className="px-3 py-1 bg-blue-100 text-blue-600 rounded text-xs hover:bg-blue-200 transition"
                                                            >
                                                                ✏️ Düzenle
                                                            </button>
                                                            <button
                                                                onClick={() => handleDeleteActivity(activity.id)}
                                                                className="px-3 py-1 bg-red-100 text-red-600 rounded text-xs hover:bg-red-200 transition"
                                                            >
                                                                🗑️ Sil
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>

                        <div className="bg-gray-50 px-6 py-4 border-t">
                            <p className="text-sm text-gray-600 text-center">
                                {filteredActivities.length} aktivite görüntüleniyor
                                {filterType !== 'ALL' && ` (${filterType} filtresi uygulandı)`}
                            </p>
                        </div>
                    </div>
                )}
            </div>

            {/* Activity Modal */}
            <ActivityModal
                isOpen={isModalOpen}
                onClose={() => {
                    setIsModalOpen(false);
                    setEditingActivity(null);
                }}
                onSave={editingActivity ? handleEditActivity : handleAddActivity}
                activity={editingActivity}
                customers={customers}
            />
        </div>
    );
}
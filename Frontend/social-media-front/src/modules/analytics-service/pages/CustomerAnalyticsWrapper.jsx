// src/modules/analytics-service/pages/CustomerAnalyticsWrapper.jsx

import { useState } from 'react';
import CustomerAnalyticsPage from './CustomerAnalyticsPage';
import CustomerSelector from '../components/CustomerAnalytics/CustomerSelector';


export default function CustomerAnalyticsWrapper({ onNavigate }) {
    const [selectedCustomerId, setSelectedCustomerId] = useState(null);

    // Müşteri seçilmemişse selector göster
    if (!selectedCustomerId) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
                <div className="max-w-7xl mx-auto space-y-6">
                    {/* Header */}
                    <div>
                        <h1 className="text-3xl font-bold text-gray-800 flex items-center gap-3">
                            📋 Süreç Yönetimi
                        </h1>
                        <p className="text-gray-600 mt-1">
                            Detaylı süreç takibi ve yönetim için bir müşteri seçin
                        </p>
                    </div>

                    {/* Customer Selector */}
                    <CustomerSelector
                        selectedCustomerId={selectedCustomerId}
                        onCustomerSelect={setSelectedCustomerId}
                    />

                    {/* Placeholder */}
                    <div className="bg-white rounded-xl shadow-lg p-12 text-center">
                        <span className="text-6xl">👆</span>
                        <p className="text-gray-700 mt-4 text-lg font-medium">Lütfen yukarıdan bir müşteri seçin</p>
                        <p className="text-gray-500 text-sm mt-2">Seçtiğiniz müşterinin detaylı analizi görüntülenecek</p>
                    </div>
                </div>
            </div>
        );
    }

    // Müşteri seçildiyse CustomerAnalyticsPage'i göster
    return (
        <CustomerAnalyticsPage
            customerId={selectedCustomerId}
            onNavigate={onNavigate}
            onBackToSelector={() => setSelectedCustomerId(null)}
        />
    );
}
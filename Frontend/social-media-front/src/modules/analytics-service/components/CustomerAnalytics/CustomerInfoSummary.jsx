// src/modules/analytics-service/components/CustomerAnalytics/CustomerInfoSummary.jsx

import { getStatusLabel, getStatusColor, getPackageBadge, formatShortDate } from '../../data/mockHelpers';

export default function CustomerInfoSummary({ customer }) {
  if (!customer) return null;

  return (
    <div className="bg-white rounded-xl shadow-lg overflow-hidden">
      {/* Header */}
      <div className="bg-gradient-to-r from-indigo-600 to-purple-600 p-6">
        <h3 className="text-xl font-bold text-white flex items-center gap-2">
          🏢 {customer.companyName}
        </h3>
        <p className="text-indigo-100 text-sm mt-1">{customer.sector}</p>
      </div>

      {/* Content */}
      <div className="p-6 space-y-4">
        {/* Status & Package */}
        <div className="flex flex-wrap gap-2">
          <span className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium border ${getStatusColor(customer.status)}`}>
            {getStatusLabel(customer.status)}
          </span>
          <span className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium border ${getPackageBadge(customer.membershipPackage)}`}>
            💎 {customer.membershipPackage}
          </span>
        </div>

        {/* Info Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 pt-4 border-t">
          <div>
            <p className="text-xs text-gray-500 mb-1">Başlangıç Tarihi</p>
            <p className="text-sm font-medium text-gray-800">{formatShortDate(customer.createdAt)}</p>
          </div>
          <div>
            <p className="text-xs text-gray-500 mb-1">Hashtag Sayısı</p>
            <p className="text-sm font-medium text-gray-800">{customer.hashtags?.length || 0} adet</p>
          </div>
          <div>
            <p className="text-xs text-gray-500 mb-1">Yüklenen Fotoğraf</p>
            <p className="text-sm font-medium text-gray-800">{customer.photosUploaded || 0} adet</p>
          </div>
          <div>
            <p className="text-xs text-gray-500 mb-1">İletişim Kişisi</p>
            <p className="text-sm font-medium text-gray-800">{customer.contactPerson || '-'}</p>
          </div>
        </div>

        {/* Social Media */}
        <div className="pt-4 border-t">
          <p className="text-xs text-gray-500 mb-3">Bağlı Platformlar</p>
          <div className="flex flex-wrap gap-2">
            {customer.socialMediaConnected?.instagram && (
              <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-pink-100 text-pink-700 border border-pink-300">
                📷 Instagram
              </span>
            )}
            {customer.socialMediaConnected?.tiktok && (
              <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-gray-900 text-white border border-gray-700">
                🎵 TikTok
              </span>
            )}
            {customer.socialMediaConnected?.facebook && (
              <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-700 border border-blue-300">
                👍 Facebook
              </span>
            )}
            {customer.socialMediaConnected?.youtube && (
              <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-red-100 text-red-700 border border-red-300">
                ▶️ YouTube
              </span>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
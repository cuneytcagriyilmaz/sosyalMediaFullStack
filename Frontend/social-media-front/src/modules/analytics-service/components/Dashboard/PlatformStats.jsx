// src/modules/analytics-service/components/Dashboard/PlatformStats.jsx

import { getPlatformIcon } from '../../data/mockHelpers';

export default function PlatformStats({ platformStats }) {
  if (!platformStats) return null;

  const platforms = [
    { key: 'instagram', name: 'Instagram', color: 'pink' },
    { key: 'tiktok', name: 'TikTok', color: 'gray' },
    { key: 'facebook', name: 'Facebook', color: 'blue' },
    { key: 'youtube', name: 'YouTube', color: 'red' }
  ];

  const colorClasses = {
    pink: 'from-pink-500 to-pink-600',
    gray: 'from-gray-700 to-gray-900',
    blue: 'from-blue-500 to-blue-600',
    red: 'from-red-500 to-red-600'
  };

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      {platforms.map(({ key, name, color }) => {
        const stats = platformStats[key];
        
        return (
          <div 
            key={key}
            className="bg-white rounded-xl shadow-lg overflow-hidden transform hover:scale-[1.02] transition-all duration-200"
          >
            {/* Header */}
            <div className={`bg-gradient-to-r ${colorClasses[color]} p-4`}>
              <div className="flex items-center justify-between text-white">
                <div>
                  <p className="text-sm opacity-90 mb-1">{name}</p>
                  <p className="text-2xl font-bold">{stats.connectedCustomers}</p>
                  <p className="text-xs opacity-80">Bağlı Müşteri</p>
                </div>
                <div className="text-4xl opacity-80">
                  {getPlatformIcon(key.toUpperCase())}
                </div>
              </div>
            </div>

            {/* Stats */}
            <div className="p-4 space-y-2">
              <div className="flex justify-between items-center text-sm">
                <span className="text-gray-600">Toplam Post:</span>
                <span className="font-semibold text-gray-800">{stats.totalPosts}</span>
              </div>
              <div className="flex justify-between items-center text-sm">
                <span className="text-gray-600">Gönderildi:</span>
                <span className="font-semibold text-green-600">{stats.sentPosts}</span>
              </div>
              <div className="flex justify-between items-center text-sm">
                <span className="text-gray-600">Zamanlandı:</span>
                <span className="font-semibold text-indigo-600">{stats.scheduledPosts}</span>
              </div>
              <div className="flex justify-between items-center text-sm">
                <span className="text-gray-600">Hazır:</span>
                <span className="font-semibold text-blue-600">{stats.readyPosts}</span>
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
}
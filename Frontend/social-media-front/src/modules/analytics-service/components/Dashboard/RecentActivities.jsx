// src/modules/analytics-service/components/Dashboard/RecentActivities.jsx

import { getRelativeTime, getNotificationIcon } from '../../data/mockHelpers';

export default function RecentActivities({ activities }) {
  if (!activities || activities.length === 0) {
    return (
      <div className="bg-white rounded-xl shadow-lg p-6">
        <h3 className="text-lg font-semibold text-gray-800 mb-4">
          📋 Son Aktiviteler
        </h3>
        <div className="text-center py-8 text-gray-500">
          <span className="text-4xl">📭</span>
          <p className="mt-2">Henüz aktivite yok</p>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-xl shadow-lg overflow-hidden">
      {/* Header */}
      <div className="bg-gradient-to-r from-indigo-600 to-purple-600 p-4">
        <h3 className="text-lg font-semibold text-white flex items-center gap-2">
          📋 Son Aktiviteler
        </h3>
      </div>

      {/* Activities List */}
      <div className="divide-y divide-gray-100">
        {activities.map((activity) => (
          <div
            key={activity.id}
            className="p-4 hover:bg-gray-50 transition-colors duration-200"
          >
            <div className="flex items-start gap-3">
              {/* Icon */}
              <div className="flex-shrink-0 w-10 h-10 bg-indigo-100 rounded-full flex items-center justify-center text-xl">
                {getNotificationIcon(activity.type)}
              </div>

              {/* Content */}
              <div className="flex-1 min-w-0">
                <p className="text-sm text-gray-800 font-medium">
                  {activity.message}
                </p>
                {activity.customerName && (
                  <p className="text-xs text-indigo-600 mt-1">
                    {activity.customerName}
                  </p>
                )}
                <p className="text-xs text-gray-500 mt-1">
                  {getRelativeTime(activity.timestamp)}
                </p>
              </div>

              {/* Time Badge */}
              <div className="flex-shrink-0">
                <span className="inline-block px-2 py-1 text-xs font-medium text-gray-600 bg-gray-100 rounded-full">
                  {new Date(activity.timestamp).toLocaleTimeString('tr-TR', {
                    hour: '2-digit',
                    minute: '2-digit'
                  })}
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Footer */}
      <div className="bg-gray-50 px-4 py-3">
        <button className="text-sm text-indigo-600 hover:text-indigo-700 font-medium transition">
          Tüm Aktiviteleri Gör →
        </button>
      </div>
    </div>
  );
}
// src/modules/analytics-service/components/CustomerAnalytics/ActivityTimelineCard.jsx

import { getRelativeTime, getNotificationIcon } from '../../data/mockHelpers';

export default function ActivityTimelineCard({ activities }) {
  if (!activities || activities.length === 0) {
    return (
      <div className="bg-white rounded-xl shadow-lg p-6">
        <h3 className="text-lg font-semibold text-gray-800 mb-4">
          🕐 Son Aktiviteler
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
      <div className="bg-gradient-to-r from-blue-600 to-indigo-600 p-4">
        <h3 className="text-lg font-semibold text-white flex items-center gap-2">
          🕐 Son Aktiviteler
        </h3>
      </div>

      {/* Timeline */}
      <div className="p-6">
        <div className="relative">
          {/* Vertical Line */}
          <div className="absolute left-4 top-0 bottom-0 w-0.5 bg-gray-200"></div>

          {/* Activities */}
          <div className="space-y-6">
            {activities.map((activity, index) => (
              <div key={activity.id} className="relative pl-10">
                {/* Icon */}
                <div className="absolute left-0 w-8 h-8 bg-indigo-100 rounded-full flex items-center justify-center text-lg z-10">
                  {getNotificationIcon(activity.type)}
                </div>

                {/* Content */}
                <div className="bg-gray-50 p-3 rounded-lg border border-gray-200">
                  <p className="text-sm text-gray-800 font-medium">
                    {activity.message}
                  </p>
                  <p className="text-xs text-gray-500 mt-1">
                    {getRelativeTime(activity.timestamp)}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
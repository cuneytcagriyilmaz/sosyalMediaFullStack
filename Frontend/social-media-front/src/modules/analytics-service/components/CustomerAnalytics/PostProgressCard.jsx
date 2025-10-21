// src/modules/analytics-service/components/CustomerAnalytics/PostProgressCard.jsx

import { calculateProgress, getProgressColor } from '../../data/mockHelpers';

export default function PostProgressCard({ postStats }) {
  if (!postStats) return null;

  return (
    <div className="bg-white rounded-xl shadow-lg overflow-hidden">
      {/* Header */}
      <div className="bg-gradient-to-r from-purple-600 to-pink-600 p-6">
        <h3 className="text-xl font-bold text-white flex items-center gap-2">
          📊 Post İlerlemesi
        </h3>
      </div>

      {/* Content */}
      <div className="p-6 space-y-4">
        {/* Progress Bars */}
        <div className="space-y-4">
          {/* Toplam Üretilen */}
          <div>
            <div className="flex justify-between text-sm mb-2">
              <span className="text-gray-600">Toplam Üretilen</span>
              <span className="font-semibold text-gray-800">{postStats.totalGenerated}</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div className="bg-purple-500 h-2 rounded-full" style={{ width: '100%' }}></div>
            </div>
          </div>

          {/* Hazır */}
          <div>
            <div className="flex justify-between text-sm mb-2">
              <span className="text-gray-600">Hazır</span>
              <span className="font-semibold text-gray-800">{postStats.ready}</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div 
                className={`h-2 rounded-full ${getProgressColor(calculateProgress(postStats.ready, postStats.totalGenerated))}`}
                style={{ width: `${calculateProgress(postStats.ready, postStats.totalGenerated)}%` }}
              ></div>
            </div>
          </div>

          {/* Gönderilmiş */}
          <div>
            <div className="flex justify-between text-sm mb-2">
              <span className="text-gray-600">Gönderilmiş</span>
              <span className="font-semibold text-gray-800">{postStats.sent}</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div 
                className="bg-green-500 h-2 rounded-full"
                style={{ width: `${calculateProgress(postStats.sent, postStats.totalGenerated)}%` }}
              ></div>
            </div>
          </div>

          {/* Zamanlanmış */}
          <div>
            <div className="flex justify-between text-sm mb-2">
              <span className="text-gray-600">Zamanlanmış</span>
              <span className="font-semibold text-gray-800">{postStats.scheduled}</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div 
                className="bg-indigo-500 h-2 rounded-full"
                style={{ width: `${calculateProgress(postStats.scheduled, postStats.totalGenerated)}%` }}
              ></div>
            </div>
          </div>
        </div>

        {/* Platform Dağılımı */}
        <div className="pt-4 border-t">
          <p className="text-xs text-gray-500 mb-3">Platform Dağılımı</p>
          <div className="grid grid-cols-2 gap-3">
            {postStats.byPlatform && Object.entries(postStats.byPlatform).map(([platform, count]) => (
              <div key={platform} className="flex items-center justify-between p-2 bg-gray-50 rounded-lg">
                <span className="text-sm text-gray-600 capitalize">{platform}</span>
                <span className="text-sm font-semibold text-gray-800">{count}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
// src/modules/analytics-service/components/CustomerAnalytics/ScheduledPostsCard.jsx

import { getPlatformIcon, formatShortDate } from '../../data/mockHelpers';

export default function ScheduledPostsCard({ posts }) {
  if (!posts || posts.length === 0) {
    return (
      <div className="bg-white rounded-xl shadow-lg p-6">
        <h3 className="text-lg font-semibold text-gray-800 mb-4">
          📅 Yaklaşan Postlar
        </h3>
        <div className="text-center py-8 text-gray-500">
          <span className="text-4xl">📭</span>
          <p className="mt-2">Zamanlanmış post yok</p>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-xl shadow-lg overflow-hidden">
      {/* Header */}
      <div className="bg-gradient-to-r from-orange-600 to-red-600 p-4">
        <h3 className="text-lg font-semibold text-white flex items-center gap-2">
          📅 Yaklaşan {posts.length} Post
        </h3>
      </div>

      {/* Posts List */}
      <div className="divide-y divide-gray-100">
        {posts.map((post) => (
          <div
            key={post.id}
            className="p-4 hover:bg-gray-50 transition-colors duration-200"
          >
            <div className="flex items-start gap-3">
              {/* Thumbnail */}
              {post.thumbnail && (
                <img
                  src={post.thumbnail}
                  alt={post.title}
                  className="w-16 h-16 rounded-lg object-cover flex-shrink-0"
                />
              )}

              {/* Content */}
              <div className="flex-1 min-w-0">
                <h4 className="text-sm font-semibold text-gray-800 mb-1 truncate">
                  {post.title}
                </h4>
                <div className="flex items-center gap-2 text-xs text-gray-500">
                  <span className="flex items-center gap-1">
                    {getPlatformIcon(post.platform)}
                    {post.platform}
                  </span>
                  <span>•</span>
                  <span>{formatShortDate(post.scheduledDate)}</span>
                  <span>•</span>
                  <span>
                    {new Date(post.scheduledDate).toLocaleTimeString('tr-TR', {
                      hour: '2-digit',
                      minute: '2-digit'
                    })}
                  </span>
                </div>
              </div>

              {/* Status Badge */}
              <span className="flex-shrink-0 px-2 py-1 bg-indigo-100 text-indigo-700 text-xs font-medium rounded-full">
                Zamanlandı
              </span>
            </div>
          </div>
        ))}
      </div>

      {/* Footer */}
      <div className="bg-gray-50 px-4 py-3">
        <button className="text-sm text-indigo-600 hover:text-indigo-700 font-medium transition">
          Tüm Takvimi Gör →
        </button>
      </div>
    </div>
  );
}
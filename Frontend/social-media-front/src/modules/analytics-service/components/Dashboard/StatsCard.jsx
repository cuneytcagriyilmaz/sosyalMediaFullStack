// src/modules/analytics-service/components/Dashboard/StatsCard.jsx

export default function StatsCard({ title, value, icon, subtitle, color = 'indigo' }) {
  const colorClasses = {
    indigo: 'from-indigo-500 to-indigo-600',
    purple: 'from-purple-500 to-purple-600',
    blue: 'from-blue-500 to-blue-600',
    green: 'from-green-500 to-green-600',
    orange: 'from-orange-500 to-orange-600',
    pink: 'from-pink-500 to-pink-600'
  };

  return (
    <div className="bg-white rounded-xl shadow-lg overflow-hidden transform hover:scale-[1.02] transition-all duration-200">
      <div className={`bg-gradient-to-r ${colorClasses[color]} p-4`}>
        <div className="flex items-center justify-between">
          <div className="text-white">
            <p className="text-sm opacity-90 mb-1">{title}</p>
            <p className="text-3xl font-bold">{value}</p>
            {subtitle && (
              <p className="text-xs opacity-80 mt-1">{subtitle}</p>
            )}
          </div>
          <div className="text-4xl opacity-80">
            {icon}
          </div>
        </div>
      </div>
      <div className="h-1 bg-gradient-to-r from-transparent via-white to-transparent opacity-50"></div>
    </div>
  );
}
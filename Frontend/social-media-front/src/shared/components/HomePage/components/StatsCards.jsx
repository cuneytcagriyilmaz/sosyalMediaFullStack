// shared/components/HomePage/components/StatsCards.jsx

const StatCard = ({ title, value, icon, bgColor, textColor }) => (
  <div className={`${bgColor} p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow`}>
    <div className="flex items-center justify-between">
      <div>
        <p className={`text-sm font-medium ${textColor} opacity-80`}>{title}</p>
        <p className={`text-3xl font-bold ${textColor} mt-2`}>{value}</p>
      </div>
      <div className="text-4xl opacity-50">{icon}</div>
    </div>
  </div>
);

export default function StatsCards({ stats }) {
  const cards = [
    {
      title: 'Toplam Müşteri',
      value: stats?.totalCustomers || '0',
      icon: '👥',
      bgColor: 'bg-indigo-50',
      textColor: 'text-indigo-700'
    },
    {
      title: 'Aktif Kampanya',
      value: stats?.activeCampaigns || '0',
      icon: '📢',
      bgColor: 'bg-green-50',
      textColor: 'text-green-700'
    },
    {
      title: 'Bu Ay Post',
      value: stats?.monthlyPosts || '0',
      icon: '📝',
      bgColor: 'bg-purple-50',
      textColor: 'text-purple-700'
    },
    {
      title: 'Bekleyen Görev',
      value: stats?.pendingTasks || '0',
      icon: '⏰',
      bgColor: 'bg-orange-50',
      textColor: 'text-orange-700'
    }
  ];

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      {cards.map((card, index) => (
        <StatCard key={index} {...card} />
      ))}
    </div>
  );
}
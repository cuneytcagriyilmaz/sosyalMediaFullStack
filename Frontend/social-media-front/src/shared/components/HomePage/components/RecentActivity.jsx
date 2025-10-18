// shared/components/HomePage/components/RecentActivity.jsx

const ActivityItem = ({ icon, title, description, time }) => (
  <div className="flex items-start gap-4 p-3 hover:bg-gray-50 rounded-lg transition">
    <div className="text-2xl flex-shrink-0">{icon}</div>
    <div className="flex-1 min-w-0">
      <p className="font-medium text-gray-800 truncate">{title}</p>
      <p className="text-sm text-gray-600">{description}</p>
    </div>
    <span className="text-xs text-gray-500 whitespace-nowrap">{time}</span>
  </div>
);

export default function RecentActivity({ activities }) {
  // Placeholder veri (gerçek projede API'den gelecek)
  const defaultActivities = [
    {
      icon: '👤',
      title: 'Yeni Müşteri',
      description: 'ABC Şirketi eklendi',
      time: '5dk önce'
    },
    {
      icon: '📝',
      title: 'Post Yayınlandı',
      description: 'Instagram\'da 3 post',
      time: '1s önce'
    },
    {
      icon: '🖼️',
      title: 'Medya Yüklendi',
      description: '12 fotoğraf eklendi',
      time: '2s önce'
    },
    {
      icon: '✏️',
      title: 'Müşteri Güncellendi',
      description: 'XYZ Ltd. bilgileri değişti',
      time: '3s önce'
    }
  ];

  const displayActivities = activities || defaultActivities;

  return (
    <div className="bg-white rounded-xl shadow-md p-6">
      <h2 className="text-xl font-bold text-gray-800 mb-4">🕐 Son Aktiviteler</h2>
      
      {displayActivities.length > 0 ? (
        <div className="space-y-2">
          {displayActivities.map((activity, index) => (
            <ActivityItem key={index} {...activity} />
          ))}
        </div>
      ) : (
        <p className="text-gray-500 text-center py-8">Henüz aktivite yok</p>
      )}
    </div>
  );
}
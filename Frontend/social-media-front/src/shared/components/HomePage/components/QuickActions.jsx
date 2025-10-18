// shared/components/HomePage/components/QuickActions.jsx

const ActionButton = ({ icon, label, description, onClick, color }) => (
  <button
    onClick={onClick}
    className={`${color} p-4 rounded-xl shadow-md hover:shadow-lg transition-all hover:scale-105 text-left w-full`}
  >
    <div className="text-3xl mb-2">{icon}</div>
    <h3 className="font-semibold text-gray-800">{label}</h3>
    <p className="text-sm text-gray-600 mt-1">{description}</p>
  </button>
);

export default function QuickActions({ onActionClick }) {
  const actions = [
    {
      icon: '➕',
      label: 'Yeni Müşteri',
      description: 'Müşteri ekle',
      onClick: () => onActionClick('musteriEkle'),
      color: 'bg-indigo-100 hover:bg-indigo-200'
    },
    {
      icon: '📋',
      label: 'Müşteri Listesi',
      description: 'Tüm müşteriler',
      onClick: () => onActionClick('musteriGoruntule'),
      color: 'bg-blue-100 hover:bg-blue-200'
    },
    {
      icon: '🖼️',
      label: 'Medya Yönetimi',
      description: 'Dosya yükle',
      onClick: () => onActionClick('medyaYonetimi'),
      color: 'bg-purple-100 hover:bg-purple-200'
    },
    {
      icon: '📅',
      label: 'Takvim',
      description: 'Planları gör',
      onClick: () => onActionClick('takvim'),
      color: 'bg-green-100 hover:bg-green-200'
    }
  ];

  return (
    <div className="bg-white rounded-xl shadow-md p-6">
      <h2 className="text-xl font-bold text-gray-800 mb-4">⚡ Hızlı Erişim</h2>
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        {actions.map((action, index) => (
          <ActionButton key={index} {...action} />
        ))}
      </div>
    </div>
  );
}
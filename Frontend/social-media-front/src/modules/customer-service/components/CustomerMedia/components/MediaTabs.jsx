// modules/customer-service/components/CustomerMedia/components/MediaTabs.jsx

const TABS = [
  { key: 'ALL', label: 'Tümü', icon: '📁' },
  { key: 'LOGO', label: 'Logolar', icon: '🏷️' },
  { key: 'PHOTO', label: 'Fotoğraflar', icon: '📸' },
  { key: 'VIDEO', label: 'Videolar', icon: '🎥' },
  { key: 'DOCUMENT', label: 'Dökümanlar', icon: '📄' }
];

export default function MediaTabs({ activeTab, mediaCounts, onTabChange }) {
  return (
    <div className="flex border-b border-gray-200 overflow-x-auto">
      {TABS.map(tab => (
        <button
          key={tab.key}
          type="button"
          onClick={() => onTabChange(tab.key)}
          className={`flex items-center gap-2 px-6 py-3 font-medium text-sm whitespace-nowrap transition ${
            activeTab === tab.key
              ? 'border-b-2 border-indigo-600 text-indigo-600'
              : 'text-gray-600 hover:text-gray-800 hover:bg-gray-50'
          }`}
        >
          <span>{tab.icon}</span>
          <span>{tab.label}</span>
          <span className="ml-1 px-2 py-0.5 bg-gray-100 text-gray-600 rounded-full text-xs">
            {mediaCounts[tab.key] || 0}
          </span>
        </button>
      ))}
    </div>
  );
}
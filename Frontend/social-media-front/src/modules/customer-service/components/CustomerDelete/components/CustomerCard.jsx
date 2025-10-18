// modules/customer-service/components/CustomerDelete/components/CustomerCard.jsx
import { StatusBadge } from '../../../../../shared/components/StatusBadge';

export default function CustomerCard({ customer, isSelected, onSelect }) {
  const getPackageBadgeColor = (packageType) => {
    const colors = {
      'Basic': 'bg-gray-100 text-gray-700 border-gray-300',
      'Gold': 'bg-yellow-100 text-yellow-700 border-yellow-300',
      'Platinum': 'bg-purple-100 text-purple-700 border-purple-300',
      'Premium': 'bg-blue-100 text-blue-700 border-blue-300'
    };
    return colors[packageType] || 'bg-gray-100 text-gray-700 border-gray-300';
  };

  const getPackageIcon = (packageType) => {
    const icons = {
      'Basic': '📦',
      'Gold': '🥇',
      'Platinum': '💎',
      'Premium': '⭐'
    };
    return icons[packageType] || '📦';
  };

  return (
    <div
      onClick={() => onSelect(customer.id)}
      className={`bg-white rounded-xl shadow-lg p-6 cursor-pointer transition-all duration-200 hover:shadow-2xl transform hover:-translate-y-1 ${
        isSelected ? 'ring-4 ring-orange-500 bg-orange-50' : ''
      }`}
    >
      {/* Checkbox */}
      <div className="flex items-start justify-between mb-4">
        <input
          type="checkbox"
          checked={isSelected}
          onChange={() => onSelect(customer.id)}
          onClick={(e) => e.stopPropagation()}
          className="w-5 h-5 text-orange-600 rounded border-gray-300 focus:ring-orange-500 mt-1"
        />
        <div className="flex flex-col gap-2 items-end">
          {/* Package Badge */}
          <span className={`
            inline-flex items-center gap-1.5
            px-3 py-1 rounded-full text-xs font-semibold
            border transition-all duration-200 hover:scale-105
            ${getPackageBadgeColor(customer.membershipPackage)}
          `}>
            <span>{getPackageIcon(customer.membershipPackage)}</span>
            <span>{customer.membershipPackage}</span>
          </span>

          {/* Status Badge - Yeni Component */}
          <StatusBadge status={customer.status} size="sm" />
        </div>
      </div>

      {/* Company Info */}
      <div className="flex items-start gap-3 mb-4">
        <div className="w-12 h-12 bg-gradient-to-br from-indigo-500 to-purple-600 rounded-lg flex items-center justify-center text-white font-bold text-lg shadow-md">
          {customer.companyName.charAt(0).toUpperCase()}
        </div>
        <div className="flex-1 min-w-0">
          <h3 className="text-lg font-bold text-gray-800 mb-1 line-clamp-1">
            {customer.companyName}
          </h3>
          <p className="text-gray-600 text-sm flex items-center gap-1.5">
            <span className="text-gray-400">🏢</span>
            <span className="line-clamp-1">{customer.sector}</span>
          </p>
        </div>
      </div>

      {/* Date */}
      <div className="pt-4 border-t border-gray-200">
        <p className="text-xs text-gray-500 flex items-center gap-1.5">
          <span>📅</span>
          <span>
            {new Date(customer.createdAt).toLocaleDateString('tr-TR', {
              day: '2-digit',
              month: 'long',
              year: 'numeric'
            })}
          </span>
        </p>
      </div>
    </div>
  );
}
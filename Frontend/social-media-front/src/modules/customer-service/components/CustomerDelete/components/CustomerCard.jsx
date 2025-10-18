// modules/customer-service/components/CustomerDelete/components/CustomerCard.jsx
export default function CustomerCard({ customer, isSelected, onSelect }) {
  const getPackageBadgeColor = (packageType) => {
    const colors = {
      'Basic': 'bg-gray-100 text-gray-700',
      'Gold': 'bg-yellow-100 text-yellow-700',
      'Platinum': 'bg-purple-100 text-purple-700',
      'Premium': 'bg-blue-100 text-blue-700'
    };
    return colors[packageType] || 'bg-gray-100 text-gray-700';
  };

  const getStatusBadgeColor = (status) => {
    const colors = {
      'ACTIVE': 'bg-green-100 text-green-700',
      'INACTIVE': 'bg-red-100 text-red-700',
      'PENDING': 'bg-yellow-100 text-yellow-700'
    };
    return colors[status] || 'bg-gray-100 text-gray-700';
  };

  return (
    <div
      onClick={() => onSelect(customer.id)}
      className={`bg-white rounded-xl shadow-lg p-6 cursor-pointer transition-all hover:shadow-xl transform hover:-translate-y-1 ${
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
          <span className={`px-3 py-1 rounded-full text-xs font-semibold ${getPackageBadgeColor(customer.membershipPackage)}`}>
            {customer.membershipPackage}
          </span>
          <span className={`px-3 py-1 rounded-full text-xs font-semibold ${getStatusBadgeColor(customer.status)}`}>
            {customer.status}
          </span>
        </div>
      </div>

      {/* Company Info */}
      <h3 className="text-xl font-bold text-gray-800 mb-2 line-clamp-1">
        {customer.companyName}
      </h3>
      <p className="text-gray-600 mb-4 flex items-center gap-2">
        <span className="text-sm font-medium">Sektör:</span>
        <span className="text-sm">{customer.sector}</span>
      </p>

      {/* Date */}
      <div className="pt-4 border-t border-gray-200">
        <p className="text-xs text-gray-500">
          Oluşturulma: {new Date(customer.createdAt).toLocaleDateString('tr-TR', {
            day: '2-digit',
            month: 'long',
            year: 'numeric'
          })}
        </p>
      </div>
    </div>
  );
}
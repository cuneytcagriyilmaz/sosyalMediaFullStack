// src/modules/customer-service/components/CustomerList/components/Table.jsx

import { StatusBadge } from '../../../../../shared/components/StatusBadge';

export default function Table({ customers, onNavigate }) {
  const getPackageIcon = (packageType) => {
    const icons = {
      'Basic': '📦',
      'Gold': '🥇',
      'Platinum': '💎',
      'Premium': '⭐'
    };
    return icons[packageType] || '📦';
  };

  const getPackageColor = (packageType) => {
    const colors = {
      'Basic': 'bg-gray-100 text-gray-700 border-gray-300',
      'Gold': 'bg-yellow-100 text-yellow-700 border-yellow-300',
      'Platinum': 'bg-purple-100 text-purple-700 border-purple-300',
      'Premium': 'bg-blue-100 text-blue-700 border-blue-300'
    };
    return colors[packageType] || 'bg-gray-100 text-gray-700 border-gray-300';
  };

  return (
    <div className="bg-white rounded-2xl shadow-lg overflow-hidden">
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                Şirket
              </th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                Sektör
              </th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                Paket
              </th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                Durum
              </th>
              <th className="px-6 py-4 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                Oluşturma Tarihi
              </th>
              <th className="px-6 py-4 text-right text-xs font-semibold text-gray-600 uppercase tracking-wider">
                İşlemler
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {customers.map((customer) => (
              <tr
                key={customer.id}
                className="hover:bg-gray-50 transition-colors"
              >
                <td className="px-6 py-4">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 bg-gradient-to-br from-indigo-500 to-purple-600 rounded-lg flex items-center justify-center text-white font-bold shadow-md">
                      {customer.companyName.charAt(0).toUpperCase()}
                    </div>
                    <p className="font-semibold text-gray-800">
                      {customer.companyName}
                    </p>
                  </div>
                </td>
                <td className="px-6 py-4">
                  <p className="text-gray-700">{customer.sector}</p>
                </td>
                <td className="px-6 py-4">
                  <span className={`
                    inline-flex items-center gap-1.5
                    px-3 py-1 rounded-full text-xs font-semibold
                    border transition-all hover:scale-105
                    ${getPackageColor(customer.membershipPackage)}
                  `}>
                    <span>{getPackageIcon(customer.membershipPackage)}</span>
                    <span>{customer.membershipPackage}</span>
                  </span>
                </td>
                <td className="px-6 py-4">
                  <StatusBadge status={customer.status} size="sm" />
                </td>
                <td className="px-6 py-4 text-sm text-gray-600">
                  {new Date(customer.createdAt).toLocaleDateString('tr-TR', {
                    day: '2-digit',
                    month: 'short',
                    year: 'numeric'
                  })}
                </td>
                <td className="px-6 py-4 text-right">
                  <button
                    onClick={() => {
                      localStorage.setItem('selectedCustomerId', customer.id);
                      onNavigate?.('musteriGoruntule');
                    }}
                    className="px-3 py-1.5 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-all text-sm font-medium flex items-center gap-2 justify-center ml-auto"
                  >
                    <span>👁️</span>
                    <span>Detay</span>
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
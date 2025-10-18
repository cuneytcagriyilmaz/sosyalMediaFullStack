//src/modules/customer-service/components/CustomerDetails/components/BasicInfoSection.jsx

import { StatusBadge } from "../../../../../shared/components/StatusBadge";

 

export default function BasicInfoSection({ customer }) {
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
        <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
            <h4 className="text-lg font-semibold text-indigo-600 mb-4 flex items-center justify-between">
                <span className="flex items-center">
                    <span className="mr-2">🏢</span> Temel Bilgiler
                </span>
                {/* Status Badge */}
                {customer.status && (
                    <StatusBadge status={customer.status} size="sm" />
                )}
            </h4>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="bg-gray-50 p-4 rounded-lg">
                    <p className="text-xs text-gray-500 uppercase mb-1">Sektör</p>
                    <p className="font-medium text-gray-800">{customer.sector || '-'}</p>
                </div>
                <div className="bg-gray-50 p-4 rounded-lg">
                    <p className="text-xs text-gray-500 uppercase mb-2">Üyelik Paketi</p>
                    {customer.membershipPackage ? (
                        <span className={`
                            inline-flex items-center gap-1.5
                            px-3 py-1 rounded-full text-xs font-semibold
                            border transition-all
                            ${getPackageColor(customer.membershipPackage)}
                        `}>
                            <span>{getPackageIcon(customer.membershipPackage)}</span>
                            <span>{customer.membershipPackage}</span>
                        </span>
                    ) : (
                        <p className="font-medium text-gray-800">-</p>
                    )}
                </div>
                <div className="bg-gray-50 p-4 rounded-lg md:col-span-2">
                    <p className="text-xs text-gray-500 uppercase mb-1">Adres</p>
                    <p className="font-medium text-gray-800">{customer.address || '-'}</p>
                </div>
            </div>
        </section>
    );
}
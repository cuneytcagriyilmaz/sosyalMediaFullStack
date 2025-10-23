// src/modules/analytics-service/components/CustomerSelector.jsx

import { useState, useEffect } from 'react';

export default function CustomerSelector({ onCustomerSelect, selectedCustomerId }) {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    setLoading(true);
    try {
      // Customer-Service'den müşteri listesini çek
      const response = await fetch('http://localhost:8080/api/customers');
      const data = await response.json();
      
      if (data.success) {
        setCustomers(data.data || []);
      }
    } catch (error) {
      console.error('❌ Failed to fetch customers:', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredCustomers = customers.filter(customer =>
    customer.companyName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    customer.sector.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="bg-white rounded-xl shadow-lg p-6 mb-6">
      <div className="flex items-center gap-4 mb-4">
        <div className="text-3xl">🔍</div>
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-gray-800">Müşteri Seç</h3>
          <p className="text-sm text-gray-600">Analiz görmek istediğiniz müşteriyi seçin</p>
        </div>
      </div>

      {/* Search Input */}
      <div className="mb-4">
        <input
          type="text"
          placeholder="Müşteri ara (şirket adı veya sektör)..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 text-gray-900 bg-white placeholder-gray-500"
        />
      </div>

      {/* Customer Grid */}
      {loading ? (
        <div className="text-center py-8">
          <div className="inline-block animate-spin rounded-full h-8 w-8 border-4 border-indigo-500 border-t-transparent"></div>
          <p className="text-gray-600 mt-2">Müşteriler yükleniyor...</p>
        </div>
      ) : filteredCustomers.length === 0 ? (
        <div className="text-center py-8 text-gray-500">
          <span className="text-4xl">🤷</span>
          <p className="mt-2 text-gray-700 font-medium">Müşteri bulunamadı</p>
          <p className="text-sm text-gray-500 mt-1">Farklı bir arama terimi deneyin</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 max-h-96 overflow-y-auto">
          {filteredCustomers.map((customer) => (
            <button
              key={customer.id}
              onClick={() => onCustomerSelect(customer.id)}
              className={`p-4 rounded-lg border-2 transition-all duration-200 text-left ${
                selectedCustomerId === customer.id
                  ? 'border-indigo-500 bg-indigo-50 shadow-md scale-[1.02]'
                  : 'border-gray-200 hover:border-indigo-300 hover:bg-gray-50'
              }`}
            >
              <div className="flex items-start justify-between mb-2">
                <h4 className="font-semibold text-gray-800 flex-1 line-clamp-1">{customer.companyName}</h4>
                {selectedCustomerId === customer.id && (
                  <span className="text-indigo-600 text-xl ml-2">✓</span>
                )}
              </div>
              <p className="text-sm text-gray-600 line-clamp-1">{customer.sector}</p>
              <div className="flex items-center gap-2 mt-2 flex-wrap">
                <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                  customer.status === 'ACTIVE' 
                    ? 'bg-green-100 text-green-700' 
                    : 'bg-gray-100 text-gray-700'
                }`}>
                  {customer.status}
                </span>
                <span className="text-xs text-gray-600 bg-gray-100 px-2 py-1 rounded-full">{customer.membershipPackage}</span>
              </div>
            </button>
          ))}
        </div>
      )}

      {/* Footer Info */}
      {!loading && filteredCustomers.length > 0 && (
        <div className="mt-4 pt-4 border-t border-gray-200">
          <p className="text-sm text-gray-600 text-center">
            {filteredCustomers.length} müşteri gösteriliyor
            {searchTerm && ` (${customers.length} toplam)`}
          </p>
        </div>
      )}
    </div>
  );
}
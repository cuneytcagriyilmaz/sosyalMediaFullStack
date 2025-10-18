// src/modules/customer-service/components/CustomerList/CustomerListPage.jsx
import { useState, useEffect } from 'react';
import { Search, Users, Filter } from 'lucide-react';
import { StatusBadge } from '../../../../shared/components/StatusBadge';
import customerService from '../../services/customerService';

export default function CustomerListPage({ onNavigate }) {
  const [customers, setCustomers] = useState([]);
  const [filteredCustomers, setFilteredCustomers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [packageFilter, setPackageFilter] = useState('ALL');
  const [sortBy, setSortBy] = useState('name-asc');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCustomers();
  }, []);

  useEffect(() => {
    filterAndSortCustomers();
  }, [customers, searchTerm, statusFilter, packageFilter, sortBy]);

  const fetchCustomers = async () => {
    setLoading(true);
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (error) {
      console.error('Müşteriler yüklenemedi:', error);
    } finally {
      setLoading(false);
    }
  };

  const filterAndSortCustomers = () => {
    let filtered = [...customers];

    // Arama
    if (searchTerm) {
      filtered = filtered.filter(c =>
        c.companyName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        c.sector.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Status filtresi
    if (statusFilter !== 'ALL') {
      filtered = filtered.filter(c => c.status === statusFilter);
    }

    // Paket filtresi
    if (packageFilter !== 'ALL') {
      filtered = filtered.filter(c => c.membershipPackage === packageFilter);
    }

    // Sıralama
    filtered.sort((a, b) => {
      switch (sortBy) {
        case 'name-asc':
          return a.companyName.localeCompare(b.companyName);
        case 'name-desc':
          return b.companyName.localeCompare(a.companyName);
        case 'date-asc':
          return new Date(a.createdAt) - new Date(b.createdAt);
        case 'date-desc':
          return new Date(b.createdAt) - new Date(a.createdAt);
        default:
          return 0;
      }
    });

    setFilteredCustomers(filtered);
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
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
      {/* Header */}
      <div className="max-w-7xl mx-auto mb-6">
        <div className="bg-white rounded-2xl shadow-lg p-6 md:p-8">
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center gap-4">
              <div className="bg-indigo-100 p-3 rounded-xl">
                <Users className="text-indigo-600" size={32} />
              </div>
              <div>
                <h1 className="text-3xl font-bold text-gray-800">Müşteri Listesi</h1>
                <p className="text-gray-600 mt-1">
                  {filteredCustomers.length} müşteri görüntüleniyor
                </p>
              </div>
            </div>
          </div>

          {/* Filters */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            {/* Arama */}
            <div className="relative md:col-span-2">
              <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
              <input
                type="text"
                placeholder="Şirket adı veya sektör ara..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-12 pr-4 py-3 border-2 border-gray-200 rounded-xl text-gray-900 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all outline-none"
              />
            </div>

            {/* Status Filter */}
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="px-4 py-3 border-2 border-gray-200 rounded-xl text-gray-900 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all outline-none"
            >
              <option value="ALL">Tüm Durumlar</option>
              <option value="ACTIVE">✓ Aktif</option>
              <option value="PASSIVE">⏸ Pasif</option>
              <option value="CANCELLED">✕ İptal</option>
            </select>

            {/* Package Filter */}
            <select
              value={packageFilter}
              onChange={(e) => setPackageFilter(e.target.value)}
              className="px-4 py-3 border-2 border-gray-200 rounded-xl text-gray-900 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all outline-none"
            >
              <option value="ALL">Tüm Paketler</option>
              <option value="Basic">📦 Basic</option>
              <option value="Gold">🥇 Gold</option>
              <option value="Platinum">💎 Platinum</option>
              <option value="Premium">⭐ Premium</option>
            </select>
          </div>

          {/* Sort */}
          <div className="flex items-center justify-between mt-4 pt-4 border-t border-gray-200">
            <div className="flex items-center gap-2">
              <Filter size={18} className="text-gray-400" />
              <select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value)}
                className="px-3 py-2 border border-gray-300 rounded-lg text-sm text-gray-900 focus:ring-2 focus:ring-indigo-500"
              >
                <option value="name-asc">İsim (A-Z)</option>
                <option value="name-desc">İsim (Z-A)</option>
                <option value="date-asc">Tarih (Eski→Yeni)</option>
                <option value="date-desc">Tarih (Yeni→Eski)</option>
              </select>
            </div>
          </div>
        </div>
      </div>

      {/* Customer List */}
      <div className="max-w-7xl mx-auto">
        {loading && (
          <div className="flex justify-center items-center py-20">
            <div className="animate-spin rounded-full h-12 w-12 border-4 border-indigo-500 border-t-transparent"></div>
          </div>
        )}

        {!loading && filteredCustomers.length === 0 && (
          <div className="bg-white rounded-2xl shadow-lg p-12 text-center">
            <Search size={64} className="mx-auto text-gray-300 mb-4" />
            <h3 className="text-xl font-semibold text-gray-700 mb-2">
              Müşteri Bulunamadı
            </h3>
            <p className="text-gray-500">
              {searchTerm || statusFilter !== 'ALL' || packageFilter !== 'ALL'
                ? 'Filtrelerinize uygun müşteri bulunamadı.'
                : 'Henüz müşteri eklenmemiş.'}
            </p>
          </div>
        )}

        {!loading && filteredCustomers.length > 0 && (
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
                  {filteredCustomers.map((customer) => (
                    <tr
                      key={customer.id}
                      className="hover:bg-gray-50 transition-colors"
                    >
                      <td className="px-6 py-4">
                        <div className="flex items-center gap-3">
                          <div className="w-10 h-10 bg-gradient-to-br from-indigo-500 to-purple-600 rounded-lg flex items-center justify-center text-white font-bold shadow-md">
                            {customer.companyName.charAt(0).toUpperCase()}
                          </div>
                          <div>
                            <p className="font-semibold text-gray-800">
                              {customer.companyName}
                            </p>
                          </div>
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
                          className="px-3 py-1.5 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-all text-sm font-medium flex items-center gap-2 justify-center"
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
        )}
      </div>
    </div>
  );
}
import { useState, useEffect } from 'react';
import { Search, Trash2, AlertTriangle, CheckCircle2, XCircle } from 'lucide-react';
import customerService from './services/customerService';
 


export default function CustomerDeleteForm() {
  const [customers, setCustomers] = useState([]);
  const [filteredCustomers, setFilteredCustomers] = useState([]);
  const [selectedIds, setSelectedIds] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [notification, setNotification] = useState(null);

  useEffect(() => {
    fetchCustomers();
  }, []);

  useEffect(() => {
    const filtered = customers.filter(customer =>
      customer.companyName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      customer.sector.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredCustomers(filtered);
  }, [searchTerm, customers]);

  const fetchCustomers = async () => {
    setLoading(true);
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
      setFilteredCustomers(data);
    } catch (error) {
      showNotification('Müşteriler yüklenemedi!', 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleSelectAll = (e) => {
    if (e.target.checked) {
      setSelectedIds(filteredCustomers.map(c => c.id));
    } else {
      setSelectedIds([]);
    }
  };

  const handleSelectOne = (id) => {
    setSelectedIds(prev =>
      prev.includes(id)
        ? prev.filter(selectedId => selectedId !== id)
        : [...prev, id]
    );
  };

  const handleDeleteClick = () => {
    if (selectedIds.length === 0) {
      showNotification('Lütfen silinecek müşteri seçin!', 'warning');
      return;
    }
    setShowModal(true);
  };

  const handleConfirmDelete = async () => {
    setShowModal(false);
    setLoading(true);

    try {
      await Promise.all(
        selectedIds.map(id => customerService.deleteCustomer(id))
      );

      showNotification(
        `${selectedIds.length} müşteri başarıyla silindi!`,
        'success'
      );

      setSelectedIds([]);
      await fetchCustomers();
    } catch (error) {
      showNotification('Silme işlemi başarısız!', 'error');
    } finally {
      setLoading(false);
    }
  };

  const showNotification = (message, type) => {
    setNotification({ message, type });
    setTimeout(() => setNotification(null), 4000);
  };

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
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
      {/* Notification */}
      {notification && (
        <div className={`fixed top-4 right-4 z-50 px-6 py-4 rounded-lg shadow-lg flex items-center gap-3 animate-slide-in ${notification.type === 'success' ? 'bg-green-500' :
            notification.type === 'error' ? 'bg-red-500' :
              'bg-yellow-500'
          } text-white`}>
          {notification.type === 'success' ? <CheckCircle2 size={20} /> :
            notification.type === 'error' ? <XCircle size={20} /> :
              <AlertTriangle size={20} />}
          <span className="font-medium">{notification.message}</span>
        </div>
      )}

      {/* Header */}
      <div className="max-w-7xl mx-auto mb-8">
        <div className="bg-white rounded-2xl shadow-lg p-6 md:p-8">
          <div className="flex items-center gap-4 mb-6">
            <div className="bg-red-100 p-3 rounded-xl">
              <Trash2 className="text-red-600" size={32} />
            </div>
            <div>
              <h1 className="text-3xl font-bold text-gray-800">Müşteri Silme</h1>
              <p className="text-gray-600 mt-1">Müşterileri güvenli şekilde silin (Soft Delete)</p>
            </div>
          </div>

          {/* Search & Actions */}
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
              <input
                type="text"
                placeholder="Şirket adı veya sektöre göre ara..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-12 pr-4 py-3 border-2 border-gray-200 rounded-xl focus:border-orange-500 focus:ring-2 focus:ring-orange-200 transition-all outline-none"
              />
            </div>
            <button
              onClick={handleDeleteClick}
              disabled={selectedIds.length === 0 || loading}
              className="px-6 py-3 bg-red-600 text-white rounded-xl font-medium hover:bg-red-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-all flex items-center justify-center gap-2 whitespace-nowrap"
            >
              <Trash2 size={20} />
              Seçili Müşterileri Sil ({selectedIds.length})
            </button>
          </div>

          {/* Info Alert */}
          <div className="mt-6 bg-blue-50 border-l-4 border-blue-500 rounded-lg p-4">
            <div className="flex items-start gap-3">
              <AlertTriangle className="text-blue-600 flex-shrink-0 mt-0.5" size={20} />
              <div className="text-sm">
                <p className="font-semibold text-blue-900 mb-1">Soft Delete Hakkında</p>
                <p className="text-blue-700">
                  Silinen müşteriler pasif duruma getirilir. Tüm veriler korunur ve gerektiğinde geri yüklenebilir.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Customer Grid */}
      <div className="max-w-7xl mx-auto">
        {loading && (
          <div className="flex justify-center items-center py-20">
            <div className="animate-spin rounded-full h-12 w-12 border-4 border-orange-500 border-t-transparent"></div>
          </div>
        )}

        {!loading && filteredCustomers.length === 0 && (
          <div className="bg-white rounded-2xl shadow-lg p-12 text-center">
            <div className="text-gray-400 mb-4">
              <Search size={64} className="mx-auto" />
            </div>
            <h3 className="text-xl font-semibold text-gray-700 mb-2">Müşteri Bulunamadı</h3>
            <p className="text-gray-500">
              {searchTerm ? 'Arama kriterlerinize uygun müşteri bulunamadı.' : 'Henüz müşteri bulunmuyor.'}
            </p>
          </div>
        )}

        {!loading && filteredCustomers.length > 0 && (
          <>
            {/* Select All */}
            <div className="bg-white rounded-xl shadow p-4 mb-4 flex items-center gap-3">
              <input
                type="checkbox"
                checked={selectedIds.length === filteredCustomers.length && filteredCustomers.length > 0}
                onChange={handleSelectAll}
                className="w-5 h-5 text-orange-600 rounded border-gray-300 focus:ring-orange-500"
              />
              <span className="font-medium text-gray-700">
                Tümünü Seç ({filteredCustomers.length} müşteri)
              </span>
            </div>

            {/* Customer Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {filteredCustomers.map((customer) => {
                const isSelected = selectedIds.includes(customer.id);
                return (
                  <div
                    key={customer.id}
                    onClick={() => handleSelectOne(customer.id)}
                    className={`bg-white rounded-xl shadow-lg p-6 cursor-pointer transition-all hover:shadow-xl transform hover:-translate-y-1 ${isSelected ? 'ring-4 ring-orange-500 bg-orange-50' : ''
                      }`}
                  >
                    {/* Checkbox */}
                    <div className="flex items-start justify-between mb-4">
                      <input
                        type="checkbox"
                        checked={isSelected}
                        onChange={() => handleSelectOne(customer.id)}
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
              })}
            </div>
          </>
        )}
      </div>

      {/* Delete Confirmation Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full p-8 animate-scale-in">
            <div className="flex justify-center mb-6">
              <div className="bg-red-100 p-4 rounded-full">
                <AlertTriangle className="text-red-600" size={48} />
              </div>
            </div>

            <h2 className="text-2xl font-bold text-gray-800 text-center mb-4">
              Silme Onayı
            </h2>

            <p className="text-gray-600 text-center mb-6">
              <span className="font-semibold text-red-600">{selectedIds.length} müşteri</span> silinecek.
              Bu işlem geri alınabilir (Soft Delete).
            </p>

            <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
              <p className="text-sm text-yellow-800">
                ⚠️ Müşteriler pasif duruma getirilir ancak verileri korunur.
              </p>
            </div>

            <div className="flex gap-3">
              <button
                onClick={() => setShowModal(false)}
                className="flex-1 px-6 py-3 bg-gray-200 text-gray-700 rounded-xl font-medium hover:bg-gray-300 transition-all"
              >
                İptal
              </button>
              <button
                onClick={handleConfirmDelete}
                className="flex-1 px-6 py-3 bg-red-600 text-white rounded-xl font-medium hover:bg-red-700 transition-all"
              >
                Sil
              </button>
            </div>
          </div>
        </div>
      )}

      <style>{`
        @keyframes slide-in {
          from {
            transform: translateX(100%);
            opacity: 0;
          }
          to {
            transform: translateX(0);
            opacity: 1;
          }
        }
        
        @keyframes scale-in {
          from {
            transform: scale(0.9);
            opacity: 0;
          }
          to {
            transform: scale(1);
            opacity: 1;
          }
        }
        
        .animate-slide-in {
          animation: slide-in 0.3s ease-out;
        }
        
        .animate-scale-in {
          animation: scale-in 0.2s ease-out;
        }
        
        .line-clamp-1 {
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 1;
          -webkit-box-orient: vertical;
        }
      `}</style>
    </div>
  );
}
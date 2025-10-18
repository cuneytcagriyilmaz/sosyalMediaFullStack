import { useState, useEffect } from 'react';
import { RefreshCw, Archive, Trash2, AlertTriangle, CheckCircle2, XCircle, RotateCcw, Flame, Search, Calendar, Building2, Package } from 'lucide-react';
import customerService from './services/customerService';
 
 

export default function DeletedCustomersPage() {
  const [deletedCustomers, setDeletedCustomers] = useState([]);
  const [filteredCustomers, setFilteredCustomers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const [actionLoading, setActionLoading] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [modalData, setModalData] = useState(null);
  const [notification, setNotification] = useState(null);

  useEffect(() => {
    fetchDeletedCustomers();
  }, []);

  useEffect(() => {
    const filtered = deletedCustomers.filter(customer =>
      customer.companyName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      customer.sector.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredCustomers(filtered);
  }, [searchTerm, deletedCustomers]);

  const fetchDeletedCustomers = async () => {
    setLoading(true);
    try {
      const data = await customerService.getAllDeletedCustomers();
      setDeletedCustomers(data);
      setFilteredCustomers(data);
    } catch (error) {
      showNotification('Silinmiş müşteriler yüklenemedi!', 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleRestoreClick = (customer) => {
    setModalData({
      type: 'restore',
      customer,
      title: 'Müşteriyi Geri Yükle',
      message: `${customer.companyName} isimli müşteri geri yüklenecek. Tüm verileri tekrar aktif hale gelecek.`,
      confirmText: 'Geri Yükle',
      confirmColor: 'bg-green-600 hover:bg-green-700'
    });
    setShowModal(true);
  };

  const handleHardDeleteClick = (customer) => {
    setModalData({
      type: 'hardDelete',
      customer,
      title: 'Kalıcı Silme Onayı',
      message: `⚠️ DİKKAT! ${customer.companyName} ve TÜM VERİLERİ kalıcı olarak silinecek. Bu işlem GERİ ALINAMAZ!`,
      confirmText: 'Kalıcı Sil',
      confirmColor: 'bg-red-700 hover:bg-red-800',
      isDangerous: true
    });
    setShowModal(true);
  };

  const handleConfirmAction = async () => {
    if (!modalData) return;

    setShowModal(false);
    setActionLoading(modalData.customer.id);

    try {
      if (modalData.type === 'restore') {
        await customerService.restoreCustomer(modalData.customer.id);
        showNotification(`${modalData.customer.companyName} başarıyla geri yüklendi!`, 'success');
      } else if (modalData.type === 'hardDelete') {
        await customerService.hardDeleteCustomer(modalData.customer.id);
        showNotification(`${modalData.customer.companyName} kalıcı olarak silindi!`, 'success');
      }
      await fetchDeletedCustomers();
    } catch (error) {
      showNotification('İşlem başarısız!', 'error');
    } finally {
      setActionLoading(null);
      setModalData(null);
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

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('tr-TR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
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
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
            <div className="flex items-center gap-4">
              <div className="bg-red-100 p-3 rounded-xl">
                <Archive className="text-red-600" size={32} />
              </div>
              <div>
                <h1 className="text-3xl font-bold text-gray-800">Silinmiş Müşteriler</h1>
                <p className="text-gray-600 mt-1">Geri yükleme veya kalıcı silme işlemleri</p>
              </div>
            </div>
            <button
              onClick={fetchDeletedCustomers}
              disabled={loading}
              className="flex items-center gap-2 px-6 py-3 bg-indigo-600 text-white rounded-xl font-medium hover:bg-indigo-700 disabled:bg-gray-300 transition-all"
            >
              <RefreshCw size={20} className={loading ? 'animate-spin' : ''} />
              Yenile
            </button>
          </div>

          {/* Search */}
          <div className="relative">
            <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
            <input
              type="text"
              placeholder="Şirket adı veya sektöre göre ara..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-12 pr-4 py-3 border-2 border-gray-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all outline-none"
            />
          </div>

          {/* Info Alert */}
          <div className="mt-6 bg-blue-50 border-l-4 border-blue-500 rounded-lg p-4">
            <div className="flex items-start gap-3">
              <AlertTriangle className="text-blue-600 flex-shrink-0 mt-0.5" size={20} />
              <div className="text-sm">
                <p className="font-semibold text-blue-900 mb-1">İşlem Seçenekleri</p>
                <ul className="text-blue-700 space-y-1">
                  <li><strong>Geri Yükle:</strong> Müşteri tekrar aktif hale gelir, tüm verileri geri döner</li>
                  <li><strong>Kalıcı Sil:</strong> Müşteri ve tüm verileri kalıcı olarak silinir (GERİ ALINAMAZ)</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="max-w-7xl mx-auto">
        {loading && (
          <div className="flex justify-center items-center py-20">
            <div className="animate-spin rounded-full h-12 w-12 border-4 border-indigo-500 border-t-transparent"></div>
          </div>
        )}

        {!loading && filteredCustomers.length === 0 && (
          <div className="bg-white rounded-2xl shadow-lg p-12 text-center">
            <div className="text-gray-400 mb-4">
              <CheckCircle2 size={64} className="mx-auto" />
            </div>
            <h3 className="text-xl font-semibold text-gray-700 mb-2">
              {searchTerm ? 'Müşteri Bulunamadı' : 'Silinmiş Müşteri Yok'}
            </h3>
            <p className="text-gray-500">
              {searchTerm
                ? 'Arama kriterlerinize uygun silinmiş müşteri bulunamadı.'
                : 'Harika! Hiç silinmiş müşteri bulunmuyor.'}
            </p>
          </div>
        )}

        {!loading && filteredCustomers.length > 0 && (
          <div className="space-y-4">
            {filteredCustomers.map((customer) => {
              const isLoading = actionLoading === customer.id;
              return (
                <div
                  key={customer.id}
                  className="bg-white rounded-xl shadow-lg p-6 hover:shadow-xl transition-all"
                >
                  <div className="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-6">
                    {/* Left: Customer Info */}
                    <div className="flex-1 space-y-4">
                      {/* Header */}
                      <div className="flex flex-wrap items-start gap-3">
                        <h3 className="text-2xl font-bold text-gray-800 flex-1">
                          {customer.companyName}
                        </h3>
                        <span className={`px-3 py-1 rounded-full text-xs font-semibold ${getPackageBadgeColor(customer.membershipPackage)}`}>
                          {customer.membershipPackage}
                        </span>
                      </div>

                      {/* Details Grid */}
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div className="flex items-center gap-2 text-gray-600">
                          <Building2 size={18} className="text-gray-400" />
                          <span className="text-sm">
                            <strong>Sektör:</strong> {customer.sector}
                          </span>
                        </div>
                        <div className="flex items-center gap-2 text-gray-600">
                          <Package size={18} className="text-gray-400" />
                          <span className="text-sm">
                            <strong>Durum:</strong> <span className="text-red-600 font-semibold">Silinmiş</span>
                          </span>
                        </div>
                      </div>

                      {/* Address */}
                      <p className="text-gray-600 text-sm">
                        <strong>Adres:</strong> {customer.address}
                      </p>

                      {/* Stats */}
                      <div className="flex flex-wrap gap-4 text-sm">
                        {customer.contacts && customer.contacts.length > 0 && (
                          <div className="flex items-center gap-1.5 px-3 py-1.5 bg-blue-50 rounded-lg">
                            <span className="font-medium text-blue-700">📞 {customer.contacts.length} yetkili</span>
                          </div>
                        )}
                        {customer.media && customer.media.length > 0 && (
                          <div className="flex items-center gap-1.5 px-3 py-1.5 bg-purple-50 rounded-lg">
                            <span className="font-medium text-purple-700">🖼️ {customer.media.length} medya</span>
                          </div>
                        )}
                      </div>

                      {/* Dates */}
                      <div className="pt-4 border-t border-gray-200 space-y-1">
                        <div className="flex items-center gap-2 text-xs text-gray-500">
                          <Calendar size={14} />
                          <span><strong>Oluşturulma:</strong> {formatDate(customer.createdAt)}</span>
                        </div>
                        <div className="flex items-center gap-2 text-xs text-red-600 font-medium">
                          <Trash2 size={14} />
                          <span><strong>Silinme:</strong> {formatDate(customer.deletedAt)}</span>
                        </div>
                      </div>
                    </div>

                    {/* Right: Action Buttons */}
                    <div className="flex lg:flex-col gap-3">
                      <button
                        onClick={() => handleRestoreClick(customer)}
                        disabled={isLoading}
                        className="flex-1 lg:flex-initial flex items-center justify-center gap-2 px-6 py-3 bg-green-600 text-white rounded-xl font-medium hover:bg-green-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-all whitespace-nowrap"
                      >
                        {isLoading ? (
                          <>
                            <RefreshCw size={18} className="animate-spin" />
                            İşleniyor...
                          </>
                        ) : (
                          <>
                            <RotateCcw size={18} />
                            Geri Yükle
                          </>
                        )}
                      </button>

                      <button
                        onClick={() => handleHardDeleteClick(customer)}
                        disabled={isLoading}
                        className="flex-1 lg:flex-initial flex items-center justify-center gap-2 px-6 py-3 bg-red-700 text-white rounded-xl font-medium hover:bg-red-800 disabled:bg-gray-300 disabled:cursor-not-allowed transition-all whitespace-nowrap"
                      >
                        {isLoading ? (
                          <>
                            <RefreshCw size={18} className="animate-spin" />
                            İşleniyor...
                          </>
                        ) : (
                          <>
                            <Flame size={18} />
                            Kalıcı Sil
                          </>
                        )}
                      </button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}

        {/* Statistics */}
        {!loading && filteredCustomers.length > 0 && (
          <div className="mt-8 bg-white rounded-xl shadow-lg p-6">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 text-center">
              <div>
                <div className="text-3xl font-bold text-gray-800">{filteredCustomers.length}</div>
                <div className="text-sm text-gray-600 mt-1">Silinmiş Müşteri</div>
              </div>
              <div>
                <div className="text-3xl font-bold text-blue-600">
                  {filteredCustomers.reduce((sum, c) => sum + (c.contacts?.length || 0), 0)}
                </div>
                <div className="text-sm text-gray-600 mt-1">Toplam Yetkili</div>
              </div>
              <div>
                <div className="text-3xl font-bold text-purple-600">
                  {filteredCustomers.reduce((sum, c) => sum + (c.media?.length || 0), 0)}
                </div>
                <div className="text-sm text-gray-600 mt-1">Toplam Medya</div>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Confirmation Modal */}
      {showModal && modalData && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full p-8 animate-scale-in">
            <div className="flex justify-center mb-6">
              <div className={`p-4 rounded-full ${modalData.type === 'restore' ? 'bg-green-100' : 'bg-red-100'
                }`}>
                {modalData.type === 'restore' ? (
                  <RotateCcw className="text-green-600" size={48} />
                ) : (
                  <Flame className="text-red-600" size={48} />
                )}
              </div>
            </div>

            <h2 className="text-2xl font-bold text-gray-800 text-center mb-4">
              {modalData.title}
            </h2>

            <p className="text-gray-600 text-center mb-6">
              {modalData.message}
            </p>

            {modalData.isDangerous && (
              <div className="bg-red-50 border-l-4 border-red-500 rounded-lg p-4 mb-6">
                <p className="text-sm text-red-800 font-medium">
                  ⚠️ Bu işlem geri alınamaz! Müşteri klasörü ve tüm verileri kalıcı olarak silinecektir.
                </p>
              </div>
            )}

            <div className="flex gap-3">
              <button
                onClick={() => {
                  setShowModal(false);
                  setModalData(null);
                }}
                className="flex-1 px-6 py-3 bg-gray-200 text-gray-700 rounded-xl font-medium hover:bg-gray-300 transition-all"
              >
                İptal
              </button>
              <button
                onClick={handleConfirmAction}
                className={`flex-1 px-6 py-3 text-white rounded-xl font-medium transition-all ${modalData.confirmColor}`}
              >
                {modalData.confirmText}
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
      `}</style>
    </div>
  );
}
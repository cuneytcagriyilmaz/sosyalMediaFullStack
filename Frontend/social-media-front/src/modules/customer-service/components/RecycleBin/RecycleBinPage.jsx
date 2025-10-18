//src/modules/customer-service/components/RecycleBin/RecycleBinPage.jsx
import { RefreshCw, Archive, Search, AlertTriangle, CheckCircle2 } from 'lucide-react';
import useRecycleBin from '../../hooks/useRecycleBin';
import { 
  DeletedCustomerCard, 
  ActionModal, 
  Notification, 
  Statistics 
} from './components';

export default function RecycleBinPage() {
  const {
    customers,
    searchTerm,
    setSearchTerm,
    loading,
    actionLoading,
    showModal,
    setShowModal,
    modalData,
    setModalData,
    notification,
    handleRestoreClick,
    handleHardDeleteClick,
    handleConfirmAction,
    handleRefresh
  } = useRecycleBin();

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
      <Notification notification={notification} />

      {/* Header */}
      <div className="max-w-7xl mx-auto mb-8">
        <div className="bg-white rounded-2xl shadow-lg p-6 md:p-8">
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
            <div className="flex items-center gap-4">
              <div className="bg-red-100 p-3 rounded-xl">
                <Archive className="text-red-600" size={32} />
              </div>
              <div>
                <h1 className="text-3xl font-bold text-gray-800">Geri Dönüşüm Kutusu</h1>
                <p className="text-gray-600 mt-1">Geri yükleme veya kalıcı silme işlemleri</p>
              </div>
            </div>
            <button
              onClick={handleRefresh}
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

        {!loading && customers.length === 0 && (
          <div className="bg-white rounded-2xl shadow-lg p-12 text-center">
            <div className="text-gray-400 mb-4">
              <CheckCircle2 size={64} className="mx-auto" />
            </div>
            <h3 className="text-xl font-semibold text-gray-700 mb-2">
              {searchTerm ? 'Müşteri Bulunamadı' : 'Geri Dönüşüm Kutusu Boş'}
            </h3>
            <p className="text-gray-500">
              {searchTerm
                ? 'Arama kriterlerinize uygun silinmiş müşteri bulunamadı.'
                : 'Harika! Geri dönüşüm kutusunda hiç müşteri bulunmuyor.'}
            </p>
          </div>
        )}

        {!loading && customers.length > 0 && (
          <>
            <div className="space-y-4">
              {customers.map((customer) => (
                <DeletedCustomerCard
                  key={customer.id}
                  customer={customer}
                  isLoading={actionLoading === customer.id}
                  onRestore={handleRestoreClick}
                  onHardDelete={handleHardDeleteClick}
                />
              ))}
            </div>

            <Statistics customers={customers} />
          </>
        )}
      </div>

      {/* Action Modal */}
      <ActionModal
        show={showModal}
        data={modalData}
        onConfirm={handleConfirmAction}
        onCancel={() => {
          setShowModal(false);
          setModalData(null);
        }}
      />

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
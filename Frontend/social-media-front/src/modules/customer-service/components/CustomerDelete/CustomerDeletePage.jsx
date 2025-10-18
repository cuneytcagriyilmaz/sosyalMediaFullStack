//src/modules/customer-service/components/CustomerDelete/CustomerDeletePage.jsx
import { Trash2, Search } from 'lucide-react';
import useCustomerDelete from '../../hooks/useCustomerDelete';
import { CustomerCard } from './components';

export default function CustomerDeletePage() {
  const {
    customers,
    selectedIds,
    searchTerm,
    setSearchTerm,
    loading,
    handleSelectAll,
    handleSelectOne,
    handleDeleteClick,
    allSelected
  } = useCustomerDelete();

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
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
              <span className="text-blue-600 text-xl">ℹ️</span>
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

        {!loading && customers.length === 0 && (
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

        {!loading && customers.length > 0 && (
          <>
            {/* Select All */}
            <div className="bg-white rounded-xl shadow p-4 mb-4 flex items-center gap-3">
              <input
                type="checkbox"
                checked={allSelected}
                onChange={handleSelectAll}
                className="w-5 h-5 text-orange-600 rounded border-gray-300 focus:ring-orange-500"
              />
              <span className="font-medium text-gray-700">
                Tümünü Seç ({customers.length} müşteri)
              </span>
            </div>

            {/* Customer Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {customers.map((customer) => (
                <CustomerCard
                  key={customer.id}
                  customer={customer}
                  isSelected={selectedIds.includes(customer.id)}
                  onSelect={handleSelectOne}
                />
              ))}
            </div>
          </>
        )}
      </div>

      <style>{`
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
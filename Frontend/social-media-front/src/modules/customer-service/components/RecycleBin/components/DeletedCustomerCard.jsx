// src/modules/customer-service/components/RecycleBin/components/DeletedCustomerCard.jsx

import { RefreshCw, RotateCcw, Flame, Calendar, Building2, Package, Trash2 } from 'lucide-react';

export default function DeletedCustomerCard({ 
  customer, 
  isLoading, 
  onRestore, 
  onHardDelete 
}) {
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
    if (!dateString) return 'Belirtilmemiş';
    return new Date(dateString).toLocaleDateString('tr-TR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  // ✅ Safe property access
  const companyName = customer.companyName || customer.company_name || 'İsimsiz Şirket';
  const sector = customer.sector || 'Belirtilmemiş';
  const address = customer.address || 'Adres belirtilmemiş';
  const membershipPackage = customer.membershipPackage || customer.membership_package || 'Basic';
  const contacts = customer.contacts || [];
  const media = customer.media || [];

  return (
    <div className="bg-white rounded-xl shadow-lg p-6 hover:shadow-xl transition-all">
      <div className="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-6">
        {/* Left: Customer Info */}
        <div className="flex-1 space-y-4">
          {/* Header */}
          <div className="flex flex-wrap items-start gap-3">
            <h3 className="text-2xl font-bold text-gray-800 flex-1">
              {companyName}
            </h3>
            <span className={`px-3 py-1 rounded-full text-xs font-semibold ${getPackageBadgeColor(membershipPackage)}`}>
              {membershipPackage}
            </span>
          </div>

          {/* Details Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="flex items-center gap-2 text-gray-600">
              <Building2 size={18} className="text-gray-400" />
              <span className="text-sm">
                <strong>Sektör:</strong> {sector}
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
            <strong>Adres:</strong> {address}
          </p>

          {/* Stats */}
          <div className="flex flex-wrap gap-4 text-sm">
            {contacts.length > 0 && (
              <div className="flex items-center gap-1.5 px-3 py-1.5 bg-blue-50 rounded-lg">
                <span className="font-medium text-blue-700">📞 {contacts.length} yetkili</span>
              </div>
            )}
            {media.length > 0 && (
              <div className="flex items-center gap-1.5 px-3 py-1.5 bg-purple-50 rounded-lg">
                <span className="font-medium text-purple-700">🖼️ {media.length} medya</span>
              </div>
            )}
          </div>

          {/* Dates */}
          <div className="pt-4 border-t border-gray-200 space-y-1">
            <div className="flex items-center gap-2 text-xs text-gray-500">
              <Calendar size={14} />
              <span><strong>Oluşturulma:</strong> {formatDate(customer.createdAt || customer.created_at)}</span>
            </div>
            <div className="flex items-center gap-2 text-xs text-red-600 font-medium">
              <Trash2 size={14} />
              <span><strong>Silinme:</strong> {formatDate(customer.deletedAt || customer.deleted_at || customer.updatedAt || customer.updated_at)}</span>
            </div>
          </div>
        </div>

        {/* Right: Action Buttons */}
        <div className="flex lg:flex-col gap-3">
          <button
            onClick={() => onRestore(customer)}
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
            onClick={() => onHardDelete(customer)}
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
}
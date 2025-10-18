// modules/customer-service/components/CustomerForm/UpdateFormSections/UpdateBasicInfoSection.jsx
import { MEMBERSHIP_PACKAGES, CUSTOMER_STATUS } from '../../../constants/formConstants';
import { SaveButton } from '../FormComponents';
import { StatusBadge } from '../../../../../shared/components/StatusBadge';

export default function UpdateBasicInfoSection({ 
  formData, 
  setFormData, 
  onSave, 
  loading,
  inputClass, 
  labelClass 
}) {
  // Paket icon'ları
  const getPackageIcon = (packageValue) => {
    const icons = {
      'Basic': '📦',
      'Gold': '🥇',
      'Platinum': '💎',
      'Premium': '⭐'
    };
    return icons[packageValue] || '📦';
  };

  return (
    <section className="bg-gradient-to-br from-gray-50 to-white p-4 sm:p-6 rounded-xl border border-gray-200 shadow-sm">
      <div className="flex items-center justify-between mb-4 sm:mb-6">
        <h3 className="text-lg sm:text-xl font-semibold text-gray-800 flex items-center">
          <span className="mr-2">🏢</span> Temel Bilgiler
        </h3>
        {/* Mevcut Status Badge */}
        {formData.status && (
          <div className="flex items-center gap-2">
            <span className="text-xs text-gray-500 hidden sm:inline">Mevcut Durum:</span>
            <StatusBadge status={formData.status} size="sm" />
          </div>
        )}
      </div>
      
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
        <div>
          <label className={labelClass}>
            Şirket Adı <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            value={formData.companyName || ""}
            onChange={(e) => setFormData({ ...formData, companyName: e.target.value })}
            required
            className={inputClass}
            placeholder="Örn: Kahve Dünyası"
          />
        </div>

        <div>
          <label className={labelClass}>
            Sektör <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            value={formData.sector || ""}
            onChange={(e) => setFormData({ ...formData, sector: e.target.value })}
            required
            className={inputClass}
            placeholder="Örn: Cafe, Restoran"
          />
        </div>

        <div className="sm:col-span-2">
          <label className={labelClass}>
            Adres <span className="text-red-500">*</span>
          </label>
          <textarea
            value={formData.address || ""}
            onChange={(e) => setFormData({ ...formData, address: e.target.value })}
            required
            className={inputClass}
            rows="2"
            placeholder="Tam adres"
          />
        </div>

        <div>
          <label className={labelClass}>
            Üyelik Paketi <span className="text-red-500">*</span>
          </label>
          <select
            value={formData.membershipPackage || ""}
            onChange={(e) => setFormData({ ...formData, membershipPackage: e.target.value })}
            required
            className={`${inputClass} font-medium`}
          >
            <option value="">Seçiniz</option>
            {MEMBERSHIP_PACKAGES.map(pkg => (
              <option key={pkg.value} value={pkg.value}>
                {getPackageIcon(pkg.value)} {pkg.label}
              </option>
            ))}
          </select>
          {formData.membershipPackage && (
            <p className="text-xs text-gray-500 mt-1 flex items-center gap-1">
              <span>{getPackageIcon(formData.membershipPackage)}</span>
              <span>Seçili: {formData.membershipPackage}</span>
            </p>
          )}
        </div>

        <div>
          <label className={labelClass}>
            Durum <span className="text-red-500">*</span>
          </label>
          <select
            value={formData.status || ""}
            onChange={(e) => setFormData({ ...formData, status: e.target.value })}
            required
            className={`${inputClass} font-medium`}
          >
            {CUSTOMER_STATUS.map(status => (
              <option key={status.value} value={status.value}>
                {status.label}
              </option>
            ))}
          </select>
          {/* Status Preview */}
          {formData.status && (
            <div className="mt-2 flex items-center gap-2">
              <span className="text-xs text-gray-500">Önizleme:</span>
              <StatusBadge status={formData.status} size="sm" />
            </div>
          )}
        </div>
      </div>

      <SaveButton onClick={onSave} loading={loading} />
    </section>
  );
}
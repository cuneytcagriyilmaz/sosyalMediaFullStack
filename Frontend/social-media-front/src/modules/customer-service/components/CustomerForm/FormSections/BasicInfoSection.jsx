// modules/customer-service/components/CustomerForm/FormSections/BasicInfoSection.jsx
import { MEMBERSHIP_PACKAGES, CUSTOMER_STATUS } from '../../../constants/formConstants';

export default function BasicInfoSection({ formData, setFormData, inputClass, labelClass }) {
  return (
    <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
      <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
        <span className="mr-2">🏢</span> Temel Bilgiler
      </h3>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className={labelClass}>
            Şirket Adı <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            value={formData.companyName}
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
            value={formData.sector}
            onChange={(e) => setFormData({ ...formData, sector: e.target.value })}
            required
            className={inputClass}
            placeholder="Örn: Cafe, Restoran, Mağaza"
          />
        </div>

        <div className="md:col-span-2">
          <label className={labelClass}>
            Adres <span className="text-red-500">*</span>
          </label>
          <textarea
            value={formData.address}
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
            value={formData.membershipPackage}
            onChange={(e) => setFormData({ ...formData, membershipPackage: e.target.value })}
            required
            className={inputClass}
          >
            <option value="">Seçiniz</option>
            {MEMBERSHIP_PACKAGES.map(pkg => (
              <option key={pkg.value} value={pkg.value}>{pkg.label}</option>
            ))}
          </select>
        </div>

        <div>
          <label className={labelClass}>
            Durum <span className="text-red-500">*</span>
          </label>
          <select
            value={formData.status}
            onChange={(e) => setFormData({ ...formData, status: e.target.value })}
            required
            className={inputClass}
          >
            {CUSTOMER_STATUS.map(status => (
              <option key={status.value} value={status.value}>{status.label}</option>
            ))}
          </select>
        </div>
      </div>
    </section>
  );
}
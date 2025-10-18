//src/modules/customer-service/components/CustomerDetails/components/SystemInfoSection.jsx
export default function SystemInfoSection({ customer }) {
  return (
    <section className="bg-gray-50 rounded-lg p-4 border border-gray-200">
      <h4 className="text-sm font-semibold text-gray-600 mb-3 uppercase">Sistem Bilgileri</h4>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
        <div>
          <p className="text-xs text-gray-500 mb-1">Müşteri ID</p>
          <p className="font-mono font-medium text-gray-800">#{customer.id}</p>
        </div>
        <div>
          <p className="text-xs text-gray-500 mb-1">Oluşturulma</p>
          <p className="font-medium text-gray-800">
            {new Date(customer.createdAt).toLocaleString('tr-TR')}
          </p>
        </div>
        <div>
          <p className="text-xs text-gray-500 mb-1">Son Güncelleme</p>
          <p className="font-medium text-gray-800">
            {new Date(customer.updatedAt).toLocaleString('tr-TR')}
          </p>
        </div>
      </div>
    </section>
  );
}
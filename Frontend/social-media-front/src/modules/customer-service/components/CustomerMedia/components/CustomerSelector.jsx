// src/modules/customer-service/components/CustomerMedia/components/CustomerSelector.jsx

export default function CustomerSelector({ 
  customers, 
  selectedCustomerId, 
  onSelectCustomer, 
  disabled 
}) {
  // ✅ Array check
  const customerList = Array.isArray(customers) ? customers : [];

  return (
    <div className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
      <label className="block text-sm font-medium text-gray-700 mb-2">
        📋 Müşteri Seç
      </label>
      <select
        value={selectedCustomerId}
        onChange={(e) => onSelectCustomer(e.target.value)}
        disabled={disabled}
        className="w-full border border-gray-300 rounded-lg p-3 bg-white text-black focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
      >
        <option value="">-- Müşteri Seçiniz --</option>
        {customerList.map((customer) => (
          <option key={customer.id} value={customer.id}>
            {customer.companyName || customer.company_name || `Müşteri #${customer.id}`}
          </option>
        ))}
      </select>
    </div>
  );
}
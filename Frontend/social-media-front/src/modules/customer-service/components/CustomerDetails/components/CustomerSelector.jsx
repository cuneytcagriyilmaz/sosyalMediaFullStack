//src/modules/customer-service/components/CustomerDetails/components/CustomerSelector.jsx
export default function CustomerSelector({ customers, loading, onSelect }) {
  return (
    <div className="mb-6 bg-white rounded-lg shadow-md p-4 border border-gray-200">
      <label className="block text-sm font-medium text-gray-700 mb-2">
        Müşteri Seç
      </label>
      <select
        className="w-full border border-gray-300 rounded-lg p-3 bg-white text-gray-900 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition"
        onChange={(e) => onSelect(e.target.value)}
        disabled={loading}
      >
        <option value="">-- Müşteri Seçiniz --</option>
        {customers.map((customer) => (
          <option key={customer.id} value={customer.id} className="text-gray-900">
            {customer.companyName} - {customer.sector}
          </option>
        ))}
      </select>
    </div>
  );
}
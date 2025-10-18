//src/modules/customer-service/components/CustomerDetails/components/CustomerHeader.jsx
export default function CustomerHeader({ customer }) {
  const getStatusColor = (status) => {
    const colors = {
      'ACTIVE': 'bg-green-100 text-green-800',
      'PASSIVE': 'bg-yellow-100 text-yellow-800',
      'CANCELLED': 'bg-red-100 text-red-800'
    };
    return colors[status] || 'bg-gray-100 text-gray-800';
  };

  return (
    <div className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
      <div className="flex justify-between items-start">
        <h3 className="text-2xl font-bold text-indigo-700">
          {customer.companyName}
        </h3>
        <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(customer.status)}`}>
          {customer.status}
        </span>
      </div>
    </div>
  );
}
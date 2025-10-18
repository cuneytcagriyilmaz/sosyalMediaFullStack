//src/modules/customer-service/components/CustomerDetails/components/CustomerHeader.jsx

import { StatusBadge } from "../../../../../shared/components/StatusBadge";

 
export default function CustomerHeader({ customer }) {
  return (
    <div className="bg-gradient-to-r from-indigo-500 to-purple-600 rounded-lg shadow-lg p-6 border border-indigo-300">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div className="flex items-center gap-4">
          {/* Avatar */}
          <div className="w-12 h-12 bg-white rounded-lg flex items-center justify-center text-indigo-600 font-bold text-xl shadow-md">
            {customer.companyName.charAt(0).toUpperCase()}
          </div>
          {/* Company Name */}
          <div>
            <h3 className="text-xl sm:text-2xl font-bold text-white">
              {customer.companyName}
            </h3>
            <p className="text-indigo-100 text-sm mt-1">
              🏢 {customer.sector}
            </p>
          </div>
        </div>
        {/* Status Badge */}
        <div className="bg-white/10 backdrop-blur-sm px-3 py-2 rounded-lg">
          <StatusBadge status={customer.status} size="md" />
        </div>
      </div>
    </div>
  );
}
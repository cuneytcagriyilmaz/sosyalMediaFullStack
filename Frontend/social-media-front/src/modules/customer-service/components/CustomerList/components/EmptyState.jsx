// src/modules/customer-service/components/CustomerList/components/EmptyState.jsx

import { Search } from 'lucide-react';

export default function EmptyState({ hasFilters }) {
  return (
    <div className="bg-white rounded-2xl shadow-lg p-12 text-center">
      <Search size={64} className="mx-auto text-gray-300 mb-4" />
      <h3 className="text-xl font-semibold text-gray-700 mb-2">
        Müşteri Bulunamadı
      </h3>
      <p className="text-gray-500">
        {hasFilters
          ? 'Filtrelerinize uygun müşteri bulunamadı.'
          : 'Henüz müşteri eklenmemiş.'}
      </p>
    </div>
  );
}
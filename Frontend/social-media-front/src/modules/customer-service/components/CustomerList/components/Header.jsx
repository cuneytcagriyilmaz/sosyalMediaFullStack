// src/modules/customer-service/components/CustomerList/components/Header.jsx

import { Users } from 'lucide-react';

export default function Header({ totalCount }) {
  return (
    <div className="flex items-center gap-4">
      <div className="bg-indigo-100 p-3 rounded-xl">
        <Users className="text-indigo-600" size={32} />
      </div>
      <div>
        <h1 className="text-3xl font-bold text-gray-800">Müşteri Listesi</h1>
        <p className="text-gray-600 mt-1">
          {totalCount} müşteri görüntüleniyor
        </p>
      </div>
    </div>
  );
}
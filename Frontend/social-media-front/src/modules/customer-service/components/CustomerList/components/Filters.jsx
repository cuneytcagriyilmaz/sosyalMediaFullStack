// src/modules/customer-service/components/CustomerList/components/Filters.jsx

import { Search, Filter } from 'lucide-react';

export default function Filters({
  searchTerm,
  onSearchChange,
  statusFilter,
  onStatusChange,
  packageFilter,
  onPackageChange,
  sortBy,
  onSortChange
}) {
  return (
    <div className="space-y-4">
      {/* Arama ve Filtreler */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        {/* Arama */}
        <div className="relative md:col-span-2">
          <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
          <input
            type="text"
            placeholder="Şirket adı veya sektör ara..."
            value={searchTerm}
            onChange={(e) => onSearchChange(e.target.value)}
            className="w-full pl-12 pr-4 py-3 border-2 border-gray-200 rounded-xl text-gray-900 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all outline-none"
          />
        </div>

        {/* Status Filter */}
        <select
          value={statusFilter}
          onChange={(e) => onStatusChange(e.target.value)}
          className="px-4 py-3 border-2 border-gray-200 rounded-xl text-gray-900 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all outline-none"
        >
          <option value="ALL">Tüm Durumlar</option>
          <option value="ACTIVE">✓ Aktif</option>
          <option value="PASSIVE">⏸ Pasif</option>
          <option value="CANCELLED">✕ İptal</option>
        </select>

        {/* Package Filter */}
        <select
          value={packageFilter}
          onChange={(e) => onPackageChange(e.target.value)}
          className="px-4 py-3 border-2 border-gray-200 rounded-xl text-gray-900 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all outline-none"
        >
          <option value="ALL">Tüm Paketler</option>
          <option value="Basic">📦 Basic</option>
          <option value="Gold">🥇 Gold</option>
          <option value="Platinum">💎 Platinum</option>
          <option value="Premium">⭐ Premium</option>
        </select>
      </div>

      {/* Sıralama */}
      <div className="flex items-center gap-2 pt-4 border-t border-gray-200">
        <Filter size={18} className="text-gray-400" />
        <select
          value={sortBy}
          onChange={(e) => onSortChange(e.target.value)}
          className="px-3 py-2 border border-gray-300 rounded-lg text-sm text-gray-900 focus:ring-2 focus:ring-indigo-500"
        >
          <option value="name-asc">İsim (A-Z)</option>
          <option value="name-desc">İsim (Z-A)</option>
          <option value="date-asc">Tarih (Eski→Yeni)</option>
          <option value="date-desc">Tarih (Yeni→Eski)</option>
        </select>
      </div>
    </div>
  );
}
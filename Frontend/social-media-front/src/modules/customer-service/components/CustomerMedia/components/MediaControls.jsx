// modules/customer-service/components/CustomerMedia/components/MediaControls.jsx

export default function MediaControls({ 
  searchQuery, 
  sortBy, 
  viewMode,
  onSearchChange,
  onSortChange,
  onViewModeChange,
  isAllSelected,
  onToggleSelectAll,
  totalFiles
}) {
  return (
    <div className="bg-white rounded-lg shadow-md p-4 border border-gray-200">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        {/* Sol: Başlık ve Seçim */}
        <div className="flex items-center gap-4">
          <h3 className="text-xl font-bold text-gray-800 flex items-center">
            <span className="mr-2">🖼️</span>
            Medya Galerisi
            <span className="ml-2 text-sm font-normal text-gray-500">
              ({totalFiles} dosya)
            </span>
          </h3>

          {totalFiles > 0 && (
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="checkbox"
                checked={isAllSelected}
                onChange={onToggleSelectAll}
                className="w-5 h-5 text-indigo-600 rounded"
              />
              <span className="text-sm text-gray-700">Tümünü Seç</span>
            </label>
          )}
        </div>

        {/* Sağ: Arama, Sıralama, Görünüm */}
        <div className="flex items-center gap-3 flex-wrap">
          {/* Arama */}
          <div className="relative">
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => onSearchChange(e.target.value)}
              placeholder="Dosya ara..."
              className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg text-sm text-gray-900 placeholder-gray-400 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
            />
            <span className="absolute left-3 top-2.5 text-gray-400">🔍</span>
          </div>

          {/* Sıralama */}
          <select
            value={sortBy}
            onChange={(e) => onSortChange(e.target.value)}
            className="px-3 py-2 border border-gray-300 rounded-lg text-sm text-gray-900 focus:ring-2 focus:ring-indigo-500"
          >
            <option value="name-asc">İsim (A-Z)</option>
            <option value="name-desc">İsim (Z-A)</option>
            <option value="size-asc">Boyut (Küçük→Büyük)</option>
            <option value="size-desc">Boyut (Büyük→Küçük)</option>
            <option value="date-asc">Tarih (Eski→Yeni)</option>
            <option value="date-desc">Tarih (Yeni→Eski)</option>
          </select>

          {/* Görünüm Toggle */}
          <div className="flex border border-gray-300 rounded-lg overflow-hidden">
            <button
              type="button"
              onClick={() => onViewModeChange('grid')}
              className={`px-3 py-2 text-sm font-medium transition ${
                viewMode === 'grid'
                  ? 'bg-indigo-600 text-white'
                  : 'bg-white text-gray-700 hover:bg-gray-50'
              }`}
            >
              ⊞ Grid
            </button>
            <button
              type="button"
              onClick={() => onViewModeChange('list')}
              className={`px-3 py-2 text-sm font-medium transition ${
                viewMode === 'list'
                  ? 'bg-indigo-600 text-white'
                  : 'bg-white text-gray-700 hover:bg-gray-50'
              }`}
            >
              ☰ List
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
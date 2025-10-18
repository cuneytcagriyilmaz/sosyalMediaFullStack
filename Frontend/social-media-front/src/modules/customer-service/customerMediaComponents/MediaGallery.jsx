// src/components/customerComponents/MediaGallery.jsx
import { useState } from 'react';
import MediaCard from './MediaCard';
import MediaModal from './MediaModal';
import ActionBar from './ActionBar';

export default function MediaGallery({ media, customerId, onMediaUpdate }) {
  // State Management
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [viewMode, setViewMode] = useState('grid'); // 'grid' | 'list'
  const [sortBy, setSortBy] = useState('name-asc');
  const [searchQuery, setSearchQuery] = useState('');
  const [activeTab, setActiveTab] = useState('ALL');
  const [modalOpen, setModalOpen] = useState(false);
  const [currentFileIndex, setCurrentFileIndex] = useState(0);

  // Media kategorilere ayır
  const mediaCounts = {
    ALL: media.length,
    LOGO: media.filter(m => m.mediaType === 'LOGO').length,
    PHOTO: media.filter(m => m.mediaType === 'PHOTO').length,
    VIDEO: media.filter(m => m.mediaType === 'VIDEO').length,
    DOCUMENT: media.filter(m => m.mediaType === 'DOCUMENT').length
  };

  // Filtreleme ve sıralama
  const filteredMedia = media
    .filter(m => activeTab === 'ALL' || m.mediaType === activeTab)
    .filter(m => m.originalFileName.toLowerCase().includes(searchQuery.toLowerCase()))
    .sort((a, b) => {
      switch (sortBy) {
        case 'name-asc': return a.originalFileName.localeCompare(b.originalFileName);
        case 'name-desc': return b.originalFileName.localeCompare(a.originalFileName);
        case 'size-asc': return a.fileSize - b.fileSize;
        case 'size-desc': return b.fileSize - a.fileSize;
        case 'date-asc': return new Date(a.uploadedAt) - new Date(b.uploadedAt);
        case 'date-desc': return new Date(b.uploadedAt) - new Date(a.uploadedAt);
        default: return 0;
      }
    });

  // Seçim fonksiyonları
  const toggleSelectFile = (fileId) => {
    setSelectedFiles(prev =>
      prev.includes(fileId)
        ? prev.filter(id => id !== fileId)
        : [...prev, fileId]
    );
  };

  const toggleSelectAll = () => {
    if (selectedFiles.length === filteredMedia.length) {
      setSelectedFiles([]);
    } else {
      setSelectedFiles(filteredMedia.map(m => m.id));
    }
  };

  const clearSelection = () => setSelectedFiles([]);

  // Modal fonksiyonları
  const openModal = (index) => {
    setCurrentFileIndex(index);
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
  };

  const navigateModal = (direction) => {
    const newIndex = direction === 'next'
      ? (currentFileIndex + 1) % filteredMedia.length
      : (currentFileIndex - 1 + filteredMedia.length) % filteredMedia.length;
    setCurrentFileIndex(newIndex);
  };

  return (
    <div className="w-full space-y-4">
      {/* Üst Kontrol Paneli */}
      <div className="bg-white rounded-lg shadow-md p-4 border border-gray-200">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          {/* Sol: Başlık ve Seçim */}
          <div className="flex items-center gap-4">
            <h3 className="text-xl font-bold text-gray-800 flex items-center">
              <span className="mr-2">🖼️</span>
              Medya Galerisi
              <span className="ml-2 text-sm font-normal text-gray-500">
                ({filteredMedia.length} dosya)
              </span>
            </h3>

            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="checkbox"
                checked={selectedFiles.length === filteredMedia.length && filteredMedia.length > 0}
                onChange={toggleSelectAll}
                className="w-5 h-5 text-indigo-600 rounded"
              />
              <span className="text-sm text-gray-700">Tümünü Seç</span>
            </label>
          </div>

          {/* Sağ: Arama, Sıralama, Görünüm */}
          <div className="flex items-center gap-3">
            {/* Arama */}
            <div className="relative">
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Dosya ara..."
                className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
              />
              <span className="absolute left-3 top-2.5 text-gray-400">🔍</span>
            </div>

            {/* Sıralama */}
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-500"
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
                onClick={() => setViewMode('grid')}
                className={`px-3 py-2 text-sm font-medium transition ${viewMode === 'grid'
                  ? 'bg-indigo-600 text-white'
                  : 'bg-white text-gray-700 hover:bg-gray-50'
                  }`}
              >
                ⊞ Grid
              </button>
              <button
                type="button"
                onClick={() => setViewMode('list')}
                className={`px-3 py-2 text-sm font-medium transition ${viewMode === 'list'
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

      {/* Tab'lar */}
      <div className="bg-white rounded-lg shadow-md border border-gray-200">
        <div className="flex border-b border-gray-200 overflow-x-auto">
          {[
            { key: 'ALL', label: 'Tümü', icon: '📁' },
            { key: 'LOGO', label: 'Logolar', icon: '🏷️' },
            { key: 'PHOTO', label: 'Fotoğraflar', icon: '📸' },
            { key: 'VIDEO', label: 'Videolar', icon: '🎥' },
            { key: 'DOCUMENT', label: 'Dökümanlar', icon: '📄' }
          ].map(tab => (
            <button
              type="button"
              key={tab.key}
              onClick={() => setActiveTab(tab.key)}
              className={`flex items-center gap-2 px-6 py-3 font-medium text-sm whitespace-nowrap transition ${activeTab === tab.key
                ? 'border-b-2 border-indigo-600 text-indigo-600'
                : 'text-gray-600 hover:text-gray-800 hover:bg-gray-50'
                }`}
            >
              <span>{tab.icon}</span>
              <span>{tab.label}</span>
              <span className="ml-1 px-2 py-0.5 bg-gray-100 text-gray-600 rounded-full text-xs">
                {mediaCounts[tab.key]}
              </span>
            </button>
          ))}
        </div>

        {/* Medya Grid/List */}
        <div className="p-6">
          {filteredMedia.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              <p className="text-lg">📭 Dosya bulunamadı</p>
              <p className="text-sm mt-2">Arama kriterlerinizi değiştirin veya yeni dosya yükleyin</p>
            </div>
          ) : viewMode === 'grid' ? (
            // Grid Görünüm - RESPONSIVE BOYUTLAR
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-3">
              {filteredMedia.map((file, index) => (
                <MediaCard
                  key={file.id}
                  file={file}
                  isSelected={selectedFiles.includes(file.id)}
                  onToggleSelect={() => toggleSelectFile(file.id)}
                  onPreview={() => openModal(index)}
                />
              ))}
            </div>
          ) : (
            // List Görünüm
            <div className="space-y-2">
              {filteredMedia.map((file, index) => (
                <MediaCard
                  key={file.id}
                  file={file}
                  isSelected={selectedFiles.includes(file.id)}
                  onToggleSelect={() => toggleSelectFile(file.id)}
                  onPreview={() => openModal(index)}
                  viewMode="list"
                />
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Action Bar (Seçim varsa göster) */}
      {selectedFiles.length > 0 && (
        <ActionBar
          selectedCount={selectedFiles.length}
          selectedFiles={selectedFiles.map(id => filteredMedia.find(m => m.id === id))}
          customerId={customerId}
          onClearSelection={clearSelection}
          onSuccess={() => {
            clearSelection();
            onMediaUpdate();
          }}
        />
      )}

      {/* Preview Modal */}
      {modalOpen && (
        <MediaModal
          file={filteredMedia[currentFileIndex]}
          currentIndex={currentFileIndex}
          totalFiles={filteredMedia.length}
          onClose={closeModal}
          onNavigate={navigateModal}
          onDelete={() => {
            closeModal();
            onMediaUpdate();
          }}
        />
      )}
    </div>
  );
}
// modules/customer-service/components/CustomerMedia/components/MediaGallery.jsx
import { useState } from 'react';
import MediaCard from './MediaCard';
import MediaModal from './MediaModal';
import ActionBar from './ActionBar';
import MediaTabs from './MediaTabs';
import MediaControls from './MediaControls';

export default function MediaGallery({ 
  allMedia,
  customerId, 
  onMediaUpdate,
  onDeleteMedia,
  filters,
  selection
}) {
  const [modalOpen, setModalOpen] = useState(false);
  const [currentFileIndex, setCurrentFileIndex] = useState(0);

  // Güvenlik kontrolü
  if (!filters) {
    console.error('MediaGallery: filters prop is required');
    return null;
  }

  if (!selection) {
    console.error('MediaGallery: selection prop is required');
    return null;
  }

  const openModal = (index) => {
    setCurrentFileIndex(index);
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
  };

  const navigateModal = (direction) => {
    const newIndex = direction === 'next'
      ? (currentFileIndex + 1) % filters.filteredMedia.length
      : (currentFileIndex - 1 + filters.filteredMedia.length) % filters.filteredMedia.length;
    setCurrentFileIndex(newIndex);
  };

  const handleDeleteFromModal = async (mediaId, fileName) => {
    await onDeleteMedia(mediaId, fileName);
  };

  return (
    <div className="w-full space-y-4">
      {/* Kontrol Paneli */}
      <MediaControls
        searchQuery={filters.searchQuery}
        sortBy={filters.sortBy}
        viewMode={filters.viewMode}
        onSearchChange={filters.setSearchQuery}
        onSortChange={filters.setSortBy}
        onViewModeChange={filters.setViewMode}
        isAllSelected={selection.isAllSelected(filters.filteredMedia)}
        onToggleSelectAll={() => selection.toggleSelectAll(filters.filteredMedia)}
        totalFiles={filters.filteredMedia.length}
      />

      {/* Tab'lar */}
      <div className="bg-white rounded-lg shadow-md border border-gray-200">
        <MediaTabs
          activeTab={filters.activeTab}
          mediaCounts={filters.mediaCounts}
          onTabChange={filters.setActiveTab}
        />

        {/* Medya Grid/List */}
        <div className="p-6">
          {filters.filteredMedia.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              <p className="text-lg">📭 Dosya bulunamadı</p>
              <p className="text-sm mt-2">
                Arama kriterlerinizi değiştirin veya yeni dosya yükleyin
              </p>
            </div>
          ) : filters.viewMode === 'grid' ? (
            // Grid Görünüm
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-3">
              {filters.filteredMedia.map((file, index) => (
                <MediaCard
                  key={file.id}
                  file={file}
                  isSelected={selection.isSelected(file.id)}
                  onToggleSelect={() => selection.toggleSelect(file.id)}
                  onPreview={() => openModal(index)}
                />
              ))}
            </div>
          ) : (
            // List Görünüm
            <div className="space-y-2">
              {filters.filteredMedia.map((file, index) => (
                <MediaCard
                  key={file.id}
                  file={file}
                  isSelected={selection.isSelected(file.id)}
                  onToggleSelect={() => selection.toggleSelect(file.id)}
                  onPreview={() => openModal(index)}
                  viewMode="list"
                />
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Action Bar (Seçim varsa göster) */}
      {selection.selectedFiles.length > 0 && (
        <ActionBar
          selectedCount={selection.selectedFiles.length}
          selectedFiles={selection.selectedFiles.map(id => 
            filters.filteredMedia.find(m => m.id === id)
          )}
          customerId={customerId}
          onClearSelection={selection.clearSelection}
          onSuccess={() => {
            selection.clearSelection();
            onMediaUpdate();
          }}
        />
      )}

      {/* Preview Modal */}
      {modalOpen && filters.filteredMedia[currentFileIndex] && (
        <MediaModal
          file={filters.filteredMedia[currentFileIndex]}
          currentIndex={currentFileIndex}
          totalFiles={filters.filteredMedia.length}
          onClose={closeModal}
          onNavigate={navigateModal}
          onDelete={handleDeleteFromModal}
        />
      )}
    </div>
  );
}
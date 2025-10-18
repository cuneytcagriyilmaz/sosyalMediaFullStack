// modules/customer-service/components/CustomerMedia/hooks/customerMediaHooks/useMediaFilters.js
import { useState, useMemo } from 'react';

export const useMediaFilters = (allMedia) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [sortBy, setSortBy] = useState('name-asc');
  const [activeTab, setActiveTab] = useState('ALL');
  const [viewMode, setViewMode] = useState('grid'); // 'grid' | 'list'

  // Media sayıları
  const mediaCounts = useMemo(() => ({
    ALL: allMedia.length,
    LOGO: allMedia.filter(m => m.mediaType === 'LOGO').length,
    PHOTO: allMedia.filter(m => m.mediaType === 'PHOTO').length,
    VIDEO: allMedia.filter(m => m.mediaType === 'VIDEO').length,
    DOCUMENT: allMedia.filter(m => m.mediaType === 'DOCUMENT').length
  }), [allMedia]);

  // Filtrelenmiş ve sıralanmış media
  const filteredMedia = useMemo(() => {
    return allMedia
      .filter(m => activeTab === 'ALL' || m.mediaType === activeTab)
      .filter(m => m.originalFileName.toLowerCase().includes(searchQuery.toLowerCase()))
      .sort((a, b) => {
        switch (sortBy) {
          case 'name-asc': 
            return a.originalFileName.localeCompare(b.originalFileName);
          case 'name-desc': 
            return b.originalFileName.localeCompare(a.originalFileName);
          case 'size-asc': 
            return a.fileSize - b.fileSize;
          case 'size-desc': 
            return b.fileSize - a.fileSize;
          case 'date-asc': 
            return new Date(a.uploadedAt) - new Date(b.uploadedAt);
          case 'date-desc': 
            return new Date(b.uploadedAt) - new Date(a.uploadedAt);
          default: 
            return 0;
        }
      });
  }, [allMedia, activeTab, searchQuery, sortBy]);

  return {
    // State
    searchQuery,
    sortBy,
    activeTab,
    viewMode,
    
    // Setters
    setSearchQuery,
    setSortBy,
    setActiveTab,
    setViewMode,
    
    // Computed
    mediaCounts,
    filteredMedia
  };
};
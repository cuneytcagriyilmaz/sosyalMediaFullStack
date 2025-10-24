// src/modules/customer-service/hooks/customerMediaHooks/useMediaFilters.js

import { useState, useMemo } from 'react';

export const useMediaFilters = (allMedia) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [sortBy, setSortBy] = useState('name-asc');
  const [activeTab, setActiveTab] = useState('ALL');
  const [viewMode, setViewMode] = useState('grid');  

  // ✅ Array check
  const mediaArray = Array.isArray(allMedia) ? allMedia : [];

  // Media sayıları
  const mediaCounts = useMemo(() => ({
    ALL: mediaArray.length,
    LOGO: mediaArray.filter(m => m.mediaType === 'LOGO').length,
    PHOTO: mediaArray.filter(m => m.mediaType === 'PHOTO').length,
    VIDEO: mediaArray.filter(m => m.mediaType === 'VIDEO').length,
    DOCUMENT: mediaArray.filter(m => m.mediaType === 'DOCUMENT').length
  }), [mediaArray]);

  // Filtrelenmiş ve sıralanmış media
  const filteredMedia = useMemo(() => {
    return mediaArray
      .filter(m => activeTab === 'ALL' || m.mediaType === activeTab)
      .filter(m => {
        const fileName = m.originalFileName || m.original_file_name || '';
        return fileName.toLowerCase().includes(searchQuery.toLowerCase());
      })
      .sort((a, b) => {
        switch (sortBy) {
          case 'name-asc': {
            const aName = a.originalFileName || a.original_file_name || '';
            const bName = b.originalFileName || b.original_file_name || '';
            return aName.localeCompare(bName);
          }
          case 'name-desc': {
            const aName = a.originalFileName || a.original_file_name || '';
            const bName = b.originalFileName || b.original_file_name || '';
            return bName.localeCompare(aName);
          }
          case 'size-asc': 
            return (a.fileSize || 0) - (b.fileSize || 0);
          case 'size-desc': 
            return (b.fileSize || 0) - (a.fileSize || 0);
          case 'date-asc': 
            return new Date(a.uploadedAt || 0) - new Date(b.uploadedAt || 0);
          case 'date-desc': 
            return new Date(b.uploadedAt || 0) - new Date(a.uploadedAt || 0);
          default: 
            return 0;
        }
      });
  }, [mediaArray, activeTab, searchQuery, sortBy]);

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
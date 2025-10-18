// modules/customer-service/components/CustomerMedia/hooks/customerMediaHooks/useMediaSelection.js
import { useState } from 'react';

export const useMediaSelection = () => {
  const [selectedFiles, setSelectedFiles] = useState([]);

  const toggleSelect = (fileId) => {
    setSelectedFiles(prev =>
      prev.includes(fileId)
        ? prev.filter(id => id !== fileId)
        : [...prev, fileId]
    );
  };

  const toggleSelectAll = (allFiles) => {
    if (selectedFiles.length === allFiles.length) {
      setSelectedFiles([]);
    } else {
      setSelectedFiles(allFiles.map(f => f.id));
    }
  };

  const clearSelection = () => {
    setSelectedFiles([]);
  };

  const isSelected = (fileId) => {
    return selectedFiles.includes(fileId);
  };

  const isAllSelected = (allFiles) => {
    return selectedFiles.length === allFiles.length && allFiles.length > 0;
  };

  return {
    selectedFiles,
    toggleSelect,
    toggleSelectAll,
    clearSelection,
    isSelected,
    isAllSelected
  };
};
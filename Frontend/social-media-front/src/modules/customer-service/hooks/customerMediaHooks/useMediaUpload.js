// modules/customer-service/components/CustomerMedia/hooks/customerMediaHooks/useMediaUpload.js
import { useState } from 'react';
import customerService from '../../services/customerService';
 

export const useMediaUpload = (customerId, onSuccess) => {
  const [uploading, setUploading] = useState(false);

  const uploadSingle = async (file, mediaType) => {
    if (!file) return;

    setUploading(true);
    try {
      await customerService.uploadMedia(customerId, file, mediaType);
      onSuccess?.();
      return { success: true, message: `✅ ${mediaType} başarıyla yüklendi!` };
    } catch (err) {
      console.error('Upload hatası:', err);
      return { 
        success: false, 
        message: `❌ Hata: ${err.response?.data?.message || err.message}` 
      };
    } finally {
      setUploading(false);
    }
  };

  const uploadMultiple = async (files, mediaType) => {
    if (files.length === 0) return;

    setUploading(true);
    try {
      await customerService.uploadMultipleMedia(customerId, files, mediaType);
      onSuccess?.();
      return { 
        success: true, 
        message: `✅ ${files.length} adet ${mediaType} başarıyla yüklendi!` 
      };
    } catch (err) {
      console.error('Upload hatası:', err);
      return { 
        success: false, 
        message: `❌ Hata: ${err.response?.data?.message || err.message}` 
      };
    } finally {
      setUploading(false);
    }
  };

  return {
    uploading,
    uploadSingle,
    uploadMultiple
  };
};
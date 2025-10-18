// modules/customer-service/components/CustomerMedia/hooks/customerMediaHooks/useCustomerMedia.js
import { useState, useEffect } from 'react';
import customerService from '../../services/customerService';
 
export const useCustomerMedia = (customerId) => {
  const [mediaData, setMediaData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (customerId) {
      fetchMedia();
    } else {
      setMediaData(null);
    }
  }, [customerId]);

  const fetchMedia = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await customerService.getCustomerMedia(customerId);
      setMediaData(data);
    } catch (err) {
      console.error('Media yüklenemedi:', err);
      setError('Media yüklenemedi');
    } finally {
      setLoading(false);
    }
  };

  const deleteMedia = async (mediaId, fileName) => {
    if (!window.confirm(`${fileName} dosyasını silmek istediğinizden emin misiniz?`)) {
      return false;
    }

    try {
      await customerService.deleteMedia(customerId, mediaId);
      await fetchMedia(); // Listeyi yenile
      return true;
    } catch (err) {
      console.error('Silme hatası:', err);
      throw err;
    }
  };

  const getAllMedia = () => {
    if (!mediaData) return [];
    
    return [
      ...(mediaData.logos || []),
      ...(mediaData.photos || []),
      ...(mediaData.videos || []),
      ...(mediaData.documents || [])
    ];
  };

  return {
    mediaData,
    allMedia: getAllMedia(),
    loading,
    error,
    refetch: fetchMedia,
    deleteMedia
  };
};
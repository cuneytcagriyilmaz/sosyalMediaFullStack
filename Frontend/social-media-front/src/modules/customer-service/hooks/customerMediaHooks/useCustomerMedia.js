// src/modules/customer-service/hooks/customerMediaHooks/useCustomerMedia.js

import { useState, useEffect } from 'react';
 
import customerService from '../../services/customerService';
import { useToast } from '../../../../shared/context/ToastContext';
import { useModal } from '../../../../shared/context/ModalContext';
  
export const useCustomerMedia = (customerId) => {
  const [mediaData, setMediaData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const { toast } = useToast();
  const { confirm } = useModal();

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
      const response = await customerService.getCustomerMedia(customerId);
      
      console.log('📥 Media response:', response);

      // ✅ Response formatını handle et
      if (response.success && response.data) {
        setMediaData(response.data);
      } else if (Array.isArray(response)) {
        // Eski format: direkt array
        setMediaData(response);
      } else if (response.logos || response.photos || response.videos || response.documents) {
        // Object format
        setMediaData(response);
      } else {
        console.warn('⚠️ Unexpected media format:', response);
        setMediaData(null);
        setError('Media yüklenemedi');
        toast.error('Medya dosyaları yüklenirken bir hata oluştu!');
      }
    } catch (err) {
      console.error('❌ Media yüklenemedi:', err);
      setMediaData(null);
      setError('Media yüklenemedi');
      toast.error('Medya dosyaları yüklenirken bir hata oluştu!');
    } finally {
      setLoading(false);
    }
  };

  const deleteMedia = async (mediaId, fileName) => {
    // Modal ile onay al
    await confirm({
      title: 'Medya Dosyasını Sil',
      message: `"${fileName}" dosyasını silmek istediğinizden emin misiniz? Bu işlem geri alınamaz.`,
      confirmText: 'Evet, Sil',
      cancelText: 'İptal',
      type: 'danger',
      onConfirm: async () => {
        try {
          await customerService.deleteMedia(customerId, mediaId);
          toast.success(`"${fileName}" başarıyla silindi!`);
          await fetchMedia(); // Listeyi yenile
        } catch (err) {
          console.error('Silme hatası:', err);
          const errorMsg = err.response?.data?.message || err.message;
          toast.error('Silme işlemi başarısız: ' + errorMsg);
          throw err;
        }
      }
    });
  };

  const getAllMedia = () => {
    if (!mediaData) return [];
    
    // ✅ Farklı formatları destekle
    if (Array.isArray(mediaData)) {
      return mediaData;
    }
    
    return [
      ...(Array.isArray(mediaData.logos) ? mediaData.logos : []),
      ...(Array.isArray(mediaData.photos) ? mediaData.photos : []),
      ...(Array.isArray(mediaData.videos) ? mediaData.videos : []),
      ...(Array.isArray(mediaData.documents) ? mediaData.documents : [])
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
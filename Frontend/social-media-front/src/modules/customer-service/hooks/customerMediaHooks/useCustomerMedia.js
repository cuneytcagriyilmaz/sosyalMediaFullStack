// modules/customer-service/hooks/customerMediaHooks/useCustomerMedia.js
import { useState, useEffect } from 'react';
import { useToast } from '../../../../shared/context/ToastContext';
import { useModal } from '../../../../shared/context/ModalContext';
import customerService from '../../services/customerService';
 
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
      const data = await customerService.getCustomerMedia(customerId);
      setMediaData(data);
    } catch (err) {
      console.error('Media yüklenemedi:', err);
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
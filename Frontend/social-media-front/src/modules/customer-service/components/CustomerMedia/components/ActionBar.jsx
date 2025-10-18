// modules/customer-service/components/CustomerMedia/components/ActionBar.jsx
import { useState } from 'react';
import { useToast } from '../../../../../shared/context/ToastContext';
import { useModal } from '../../../../../shared/context/ModalContext';
import customerService from '../../../services/customerService';

export default function ActionBar({ 
  selectedCount, 
  selectedFiles, 
  customerId, 
  onClearSelection, 
  onSuccess 
}) {
  const [loading, setLoading] = useState(false);
  const { toast } = useToast();
  const { confirm } = useModal();

  const handleDownloadZip = async () => {
    setLoading(true);
    try {
      const mediaIds = selectedFiles.map(f => f.id);
      const response = await customerService.downloadMediaAsZip(customerId, mediaIds);

      // Blob oluştur ve indir
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `musteri-${customerId}-medya.zip`);
      document.body.appendChild(link);
      link.click();
      
      // Cleanup - kısa bir gecikme ile
      setTimeout(() => {
        link.remove();
        window.URL.revokeObjectURL(url);
        toast.success(`${selectedCount} dosya ZIP olarak indirildi!`);
        onClearSelection();
      }, 100);

    } catch (error) {
      console.error('ZIP indirme hatası:', error);
      const errorMsg = error.response?.data?.message || error.message;
      toast.error('ZIP indirilemedi: ' + errorMsg);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteSelected = async () => {
    await confirm({
      title: 'Toplu Dosya Silme',
      message: `${selectedCount} dosyayı kalıcı olarak silmek istediğinizden emin misiniz? Bu işlem geri alınamaz!`,
      confirmText: 'Evet, Hepsini Sil',
      cancelText: 'İptal',
      type: 'danger',
      onConfirm: async () => {
        setLoading(true);
        try {
          const mediaIds = selectedFiles.map(f => f.id);
          await Promise.all(
            mediaIds.map(id => customerService.deleteMedia(customerId, id))
          );

          toast.success(`${selectedCount} dosya başarıyla silindi!`);
          onSuccess();
        } catch (error) {
          console.error('Silme hatası:', error);
          const errorMsg = error.response?.data?.message || error.message;
          toast.error('Silme işlemi başarısız: ' + errorMsg);
        } finally {
          setLoading(false);
        }
      }
    });
  };

  return (
    <div className="fixed bottom-0 left-0 right-0 bg-white border-t-2 border-indigo-500 shadow-2xl z-50">
      <div className="max-w-7xl mx-auto px-6 py-4">
        <div className="flex items-center justify-between">
          {/* Sol: Seçim Bilgisi */}
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-2">
              <span className="text-2xl">✅</span>
              <span className="text-lg font-semibold text-gray-800">
                {selectedCount} dosya seçildi
              </span>
            </div>

            <button
              type="button"
              onClick={onClearSelection}
              className="text-sm text-gray-600 hover:text-red-600 transition underline"
              disabled={loading}
            >
              ✕ Seçimi Kaldır
            </button>
          </div>

          {/* Sağ: Aksiyonlar */}
          <div className="flex items-center gap-3">
            {/* Toplu İndirme */}
            <button
              type="button"
              onClick={handleDownloadZip}
              disabled={loading}
              className="flex items-center gap-2 px-6 py-3 bg-green-600 text-white rounded-lg font-medium hover:bg-green-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? (
                <>
                  <span className="animate-spin">⏳</span>
                  <span>Hazırlanıyor...</span>
                </>
              ) : (
                <>
                  <span>📦</span>
                  <span>Seçilenleri İndir (ZIP)</span>
                </>
              )}
            </button>

            {/* Toplu Silme */}
            <button
              type="button"
              onClick={handleDeleteSelected}
              disabled={loading}
              className="flex items-center gap-2 px-6 py-3 bg-red-600 text-white rounded-lg font-medium hover:bg-red-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <span>🗑️</span>
              <span>Seçilenleri Sil</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
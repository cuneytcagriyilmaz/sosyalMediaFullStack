// modules/customer-service/components/CustomerMedia/components/MediaModal.jsx
import { useState, useEffect } from 'react';
import { useToast } from '../../../../../shared/context/ToastContext';
import { useModal } from '../../../../../shared/context/ModalContext';

const formatFileSize = (bytes) => {
  if (bytes < 1024) return bytes + ' B';
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
  return (bytes / 1024 / 1024).toFixed(2) + ' MB';
};

export default function MediaModal({ 
  file, 
  currentIndex, 
  totalFiles, 
  onClose, 
  onNavigate, 
  onDelete 
}) {
  const [zoom, setZoom] = useState(1);
  const { toast } = useToast();
  const { confirm } = useModal();

  // Klavye kontrolleri
  useEffect(() => {
    const handleKeyPress = (e) => {
      if (e.key === 'Escape') onClose();
      if (e.key === 'ArrowLeft') onNavigate('prev');
      if (e.key === 'ArrowRight') onNavigate('next');
    };

    window.addEventListener('keydown', handleKeyPress);
    return () => window.removeEventListener('keydown', handleKeyPress);
  }, [onClose, onNavigate]);

  // Body scroll kilitle
  useEffect(() => {
    document.body.style.overflow = 'hidden';
    return () => {
      document.body.style.overflow = 'unset';
    };
  }, []);

  const copyUrl = () => {
    navigator.clipboard.writeText(file.fullUrl);
    toast.success('URL kopyalandı!');
  };

  const handleDownload = () => {
    window.open(file.fullUrl, '_blank');
    toast.info('Dosya indiriliyor...');
  };

  const handleDelete = async () => {
    await confirm({
      title: 'Dosyayı Sil',
      message: `"${file.originalFileName}" dosyasını kalıcı olarak silmek istediğinizden emin misiniz?`,
      confirmText: 'Evet, Sil',
      cancelText: 'İptal',
      type: 'danger',
      onConfirm: async () => {
        try {
          await onDelete(file.id, file.originalFileName);
          // onDelete içinde zaten toast gösteriliyor (useCustomerMedia hook'unda)
          onClose();
        } catch (error) {
          // Hata yönetimi hook'ta yapılıyor
        }
      }
    });
  };

  const zoomIn = () => setZoom(prev => Math.min(prev + 0.25, 3));
  const zoomOut = () => setZoom(prev => Math.max(prev - 0.25, 0.5));
  const resetZoom = () => setZoom(1);

  return (
    <div className="fixed inset-0 bg-black bg-opacity-95 z-50 flex items-center justify-center animate-fade-in">
      <div className="w-full h-full flex flex-col">
        {/* Üst Bar */}
        <div className="flex items-center justify-between px-6 py-4 bg-black bg-opacity-50">
          <div className="flex items-center gap-4">
            <h3 className="text-white font-medium text-lg">
              {file.originalFileName}
            </h3>
            <span className="text-gray-400 text-sm">
              {currentIndex + 1} / {totalFiles}
            </span>
          </div>

          <button
            onClick={onClose}
            className="text-white hover:text-red-400 transition text-3xl font-bold"
            title="Kapat (ESC)"
          >
            ✕
          </button>
        </div>

        {/* Ana İçerik */}
        <div className="flex-1 flex items-center justify-center relative overflow-hidden">
          {/* Önceki */}
          {totalFiles > 1 && (
            <button
              onClick={() => onNavigate('prev')}
              className="absolute left-4 z-10 p-4 bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white rounded-full transition shadow-2xl flex items-center justify-center w-14 h-14 group"
              title="Önceki (←)"
            >
              <svg className="w-7 h-7 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M15 19l-7-7 7-7" />
              </svg>
            </button>
          )}

          {/* Preview */}
          <div className="max-w-5xl max-h-full p-8 flex items-center justify-center">
            {file.mediaType === 'VIDEO' ? (
              <video
                src={file.fullUrl}
                controls
                autoPlay
                className="max-w-full max-h-full rounded-lg shadow-2xl"
                style={{ transform: `scale(${zoom})` }}
              />
            ) : file.mediaType === 'DOCUMENT' ? (
              <div className="bg-white p-12 rounded-lg shadow-2xl text-center">
                <span className="text-9xl">📄</span>
                <p className="text-2xl font-bold text-gray-800 mt-4">
                  {file.originalFileName}
                </p>
                <p className="text-gray-600 mt-2">
                  Dökümanlar önizlenemiyor
                </p>
                <button
                  onClick={handleDownload}
                  className="mt-6 px-6 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
                >
                  ⬇️ İndir ve Görüntüle
                </button>
              </div>
            ) : (
              <img
                src={file.fullUrl}
                alt={file.originalFileName}
                className="max-w-full max-h-full object-contain rounded-lg shadow-2xl transition-transform duration-200"
                style={{ transform: `scale(${zoom})` }}
              />
            )}
          </div>

          {/* Sonraki */}
          {totalFiles > 1 && (
            <button
              onClick={() => onNavigate('next')}
              className="absolute right-4 z-10 p-4 bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white rounded-full transition shadow-2xl flex items-center justify-center w-14 h-14 group"
              title="Sonraki (→)"
            >
              <svg className="w-7 h-7 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M9 5l7 7-7 7" />
              </svg>
            </button>
          )}

          {/* Zoom Kontrolleri */}
          {(file.mediaType === 'PHOTO' || file.mediaType === 'LOGO') && (
            <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex items-center gap-2 bg-black bg-opacity-50 px-4 py-2 rounded-full">
              <button
                onClick={zoomOut}
                className="text-white hover:text-indigo-400 transition text-xl px-2"
              >
                −
              </button>
              <button
                onClick={resetZoom}
                className="text-white text-sm px-3"
              >
                {Math.round(zoom * 100)}%
              </button>
              <button
                onClick={zoomIn}
                className="text-white hover:text-indigo-400 transition text-xl px-2"
              >
                +
              </button>
            </div>
          )}
        </div>

        {/* Alt Bilgi */}
        <div className="bg-white px-6 py-4 max-h-64 overflow-y-auto">
          <div className="max-w-5xl mx-auto">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
              <div>
                <p className="text-xs text-gray-500 uppercase">Dosya Adı</p>
                <p className="font-medium text-gray-800">{file.originalFileName}</p>
              </div>
              <div>
                <p className="text-xs text-gray-500 uppercase">Boyut</p>
                <p className="font-medium text-gray-800">
                  {formatFileSize(file.fileSize)}
                </p>
              </div>
              <div>
                <p className="text-xs text-gray-500 uppercase">Tür</p>
                <p className="font-medium text-gray-800">{file.mediaType}</p>
              </div>
            </div>

            <div className="flex gap-3 pt-4 border-t">
              <button
                onClick={handleDownload}
                className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition font-medium"
              >
                <span>⬇️</span>
                <span>İndir</span>
              </button>

              <button
                onClick={handleDelete}
                className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 transition font-medium"
              >
                <span>🗑️</span>
                <span>Sil</span>
              </button>

              <button
                onClick={copyUrl}
                className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition font-medium"
              >
                <span>🔗</span>
                <span>URL Kopyala</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
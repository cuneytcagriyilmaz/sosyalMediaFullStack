// src/components/customerComponents/CustomerMediaComponent/MediaModal.jsx
import { useState, useEffect } from 'react';
import customerService from '../services/customerService';

export default function MediaModal({ file, currentIndex, totalFiles, onClose, onNavigate, onDelete }) {
  const [zoom, setZoom] = useState(1);
  const [isFullscreen, setIsFullscreen] = useState(false);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
  const [loading, setLoading] = useState(false);

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

  // Dosya boyutu formatla
  const formatFileSize = (bytes) => {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / 1024 / 1024).toFixed(2) + ' MB';
  };

  // Tarih formatla
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('tr-TR', { 
      day: 'numeric', 
      month: 'long', 
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  // URL kopyala
  const copyUrl = () => {
    navigator.clipboard.writeText(file.fullUrl);
    alert('✅ URL kopyalandı!');
  };

  // İndir
  const handleDownload = () => {
    window.open(file.fullUrl, '_blank');
  };

  // Sil
  const handleDelete = async () => {
    setLoading(true);
    try {
      // customerId'yi file objesinden veya parent'tan al
      // Şimdilik file.fullUrl'den parse edelim
      const urlParts = file.fullUrl.split('/uploads/')[1].split('/');
      const customerFolder = urlParts[0];
      
      await customerService.deleteMedia(file.id);
      
      alert('✅ Dosya silindi!');
      setShowDeleteConfirm(false);
      onDelete();
    } catch (error) {
      console.error('Silme hatası:', error);
      alert('❌ Dosya silinemedi.');
    } finally {
      setLoading(false);
    }
  };

  // Zoom kontrolleri
  const zoomIn = () => setZoom(prev => Math.min(prev + 0.25, 3));
  const zoomOut = () => setZoom(prev => Math.max(prev - 0.25, 0.5));
  const resetZoom = () => setZoom(1);

  return (
    <div className="fixed inset-0 bg-black bg-opacity-95 z-50 flex items-center justify-center animate-fade-in">
      {/* Modal Container */}
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

        {/* Ana İçerik Alanı */}
        <div className="flex-1 flex items-center justify-center relative overflow-hidden">
          {/* Önceki Butonu */}
          <button
            onClick={() => onNavigate('prev')}
            className="absolute left-4 z-10 p-4 bg-white bg-opacity-20 hover:bg-opacity-30 text-white rounded-full transition text-3xl"
            title="Önceki (←)"
          >
            ◄
          </button>

          {/* Dosya Preview */}
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

          {/* Sonraki Butonu */}
          <button
            onClick={() => onNavigate('next')}
            className="absolute right-4 z-10 p-4 bg-white bg-opacity-20 hover:bg-opacity-30 text-white rounded-full transition text-3xl"
            title="Sonraki (→)"
          >
            ►
          </button>

          {/* Zoom Kontrolleri (Sadece resim için) */}
          {(file.mediaType === 'PHOTO' || file.mediaType === 'LOGO') && (
            <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex items-center gap-2 bg-black bg-opacity-50 px-4 py-2 rounded-full">
              <button
                onClick={zoomOut}
                className="text-white hover:text-indigo-400 transition text-xl px-2"
                title="Uzaklaştır"
              >
                −
              </button>
              <button
                onClick={resetZoom}
                className="text-white text-sm px-3"
                title="Sıfırla"
              >
                {Math.round(zoom * 100)}%
              </button>
              <button
                onClick={zoomIn}
                className="text-white hover:text-indigo-400 transition text-xl px-2"
                title="Yakınlaştır"
              >
                +
              </button>
            </div>
          )}
        </div>

        {/* Alt Bilgi Paneli */}
        <div className="bg-white px-6 py-4 max-h-64 overflow-y-auto">
          <div className="max-w-5xl mx-auto">
            {/* Dosya Bilgileri */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
              <div>
                <p className="text-xs text-gray-500 uppercase">Dosya Adı</p>
                <p className="font-medium text-gray-800">{file.originalFileName}</p>
              </div>
              <div>
                <p className="text-xs text-gray-500 uppercase">Boyut</p>
                <p className="font-medium text-gray-800">
                  {formatFileSize(file.fileSize)}
                  <span className="text-gray-500 text-sm ml-2">
                    ({file.fileSize.toLocaleString()} bytes)
                  </span>
                </p>
              </div>
              <div>
                <p className="text-xs text-gray-500 uppercase">Tür</p>
                <p className="font-medium text-gray-800">
                  {file.mediaType}
                  <span className="text-gray-500 text-sm ml-2">
                    ({file.originalFileName.split('.').pop().toUpperCase()})
                  </span>
                </p>
              </div>
            </div>

            <div className="mb-4">
              <p className="text-xs text-gray-500 uppercase">Dosya Yolu</p>
              <div className="flex items-center gap-2 mt-1">
                <p className="font-mono text-sm text-gray-600 bg-gray-100 px-3 py-1 rounded flex-1 truncate">
                  {file.filePath}
                </p>
                <button
                  onClick={copyUrl}
                  className="px-3 py-1 bg-indigo-100 text-indigo-700 rounded hover:bg-indigo-200 transition text-sm"
                >
                  📋 Kopyala
                </button>
              </div>
            </div>

            {/* Aksiyon Butonları */}
            <div className="flex gap-3 pt-4 border-t">
              <button
                onClick={handleDownload}
                className="flex-1 flex items-center justify-center gap-2 px-4 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition font-medium"
              >
                <span>⬇️</span>
                <span>İndir</span>
              </button>

              <button
                onClick={() => setShowDeleteConfirm(true)}
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

      {/* Silme Onay Modal */}
      {showDeleteConfirm && (
        <div className="absolute inset-0 bg-black bg-opacity-70 flex items-center justify-center z-10">
          <div className="bg-white rounded-xl shadow-2xl p-6 max-w-md w-full mx-4">
            <div className="text-center">
              <span className="text-6xl">⚠️</span>
              <h3 className="text-xl font-bold text-gray-800 mt-4">
                Bu dosyayı silmek istediğinize emin misiniz?
              </h3>
              <p className="text-gray-600 mt-2">
                <strong>{file.originalFileName}</strong> kalıcı olarak silinecek.
                Bu işlem geri alınamaz!
              </p>
            </div>

            <div className="flex gap-3 mt-6">
              <button
                onClick={() => setShowDeleteConfirm(false)}
                disabled={loading}
                className="flex-1 px-4 py-3 bg-gray-200 text-gray-800 rounded-lg font-medium hover:bg-gray-300 transition"
              >
                İptal
              </button>
              <button
                onClick={handleDelete}
                disabled={loading}
                className="flex-1 px-4 py-3 bg-red-600 text-white rounded-lg font-medium hover:bg-red-700 transition"
              >
                {loading ? 'Siliniyor...' : 'Evet, Sil'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
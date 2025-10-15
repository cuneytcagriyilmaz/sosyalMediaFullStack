// src/components/customerComponents/CustomerMediaManager.jsx
import { useState, useEffect } from "react";
import customerService from "../../services/customerService";

export default function CustomerMediaManager() {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState("");
  const [mediaData, setMediaData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState(null);

  // Tüm müşterileri getir
  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (err) {
      console.error("Müşteriler yüklenemedi:", err);
      setError("Müşteriler yüklenemedi");
    }
  };

  // Müşteri seçildiğinde media'larını getir
  const handleSelectCustomer = async (customerId) => {
    if (!customerId) {
      setSelectedCustomerId("");
      setMediaData(null);
      return;
    }

    setSelectedCustomerId(customerId);
    setLoading(true);

    try {
      const data = await customerService.getCustomerMedia(customerId);
      setMediaData(data);
    } catch (err) {
      console.error("Media yüklenemedi:", err);
      setError("Media yüklenemedi");
    } finally {
      setLoading(false);
    }
  };

  // Tek dosya yükle
  const handleUpload = async (e, mediaType) => {
    const file = e.target.files[0];
    if (!file) return;

    setUploading(true);

    try {
      await customerService.uploadMedia(selectedCustomerId, file, mediaType);
      alert(`✅ ${mediaType} başarıyla yüklendi!`);
      
      // Media listesini güncelle
      handleSelectCustomer(selectedCustomerId);
      
      // Input'u temizle
      e.target.value = "";

    } catch (err) {
      console.error("Upload hatası:", err);
      alert("❌ Hata: " + (err.response?.data?.message || err.message));
    } finally {
      setUploading(false);
    }
  };

  // Çoklu dosya yükle
  const handleBatchUpload = async (e, mediaType) => {
    const files = Array.from(e.target.files);
    if (files.length === 0) return;

    setUploading(true);

    try {
      await customerService.uploadMultipleMedia(selectedCustomerId, files, mediaType);
      alert(`✅ ${files.length} adet ${mediaType} başarıyla yüklendi!`);
      
      // Media listesini güncelle
      handleSelectCustomer(selectedCustomerId);
      
      // Input'u temizle
      e.target.value = "";

    } catch (err) {
      console.error("Upload hatası:", err);
      alert("❌ Hata: " + (err.response?.data?.message || err.message));
    } finally {
      setUploading(false);
    }
  };

  // Media sil
  const handleDeleteMedia = async (mediaId, fileName) => {
    if (!window.confirm(`${fileName} dosyasını silmek istediğinizden emin misiniz?`)) {
      return;
    }

    setLoading(true);

    try {
      await customerService.deleteMedia(selectedCustomerId, mediaId);
      alert("✅ Media başarıyla silindi!");
      
      // Media listesini güncelle
      handleSelectCustomer(selectedCustomerId);

    } catch (err) {
      console.error("Silme hatası:", err);
      alert("❌ Hata: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        {error}
      </div>
    );
  }

  return (
    <div className="w-full">
      <h2 className="text-2xl font-bold text-indigo-700 mb-6">🖼️ Medya Yönetimi</h2>

      {/* Müşteri Seç */}
      <div className="mb-6">
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Müşteri Seç
        </label>
        <select
          value={selectedCustomerId}
          onChange={(e) => handleSelectCustomer(e.target.value)}
          className="w-full border border-gray-300 rounded-lg p-2 bg-white text-black"
          disabled={loading || uploading}
        >
          <option value="">-- Seçiniz --</option>
          {customers.map((c) => (
            <option key={c.id} value={c.id}>
              {c.companyName}
            </option>
          ))}
        </select>
      </div>

      {selectedCustomerId && (
        <>
          {/* Upload Bölümü */}
          <div className="bg-white border rounded-lg shadow-md p-6 mb-6">
            <h3 className="text-lg font-semibold text-gray-800 mb-4">📤 Dosya Yükle</h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {/* Logo */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Logo Yükle
                </label>
                <input
                  type="file"
                  accept="image/*"
                  onChange={(e) => handleUpload(e, 'LOGO')}
                  disabled={uploading}
                  className="w-full p-2 border rounded-lg"
                />
              </div>

              {/* Fotoğraf */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Fotoğraf Yükle (Çoklu)
                </label>
                <input
                  type="file"
                  accept="image/*"
                  multiple
                  onChange={(e) => handleBatchUpload(e, 'PHOTO')}
                  disabled={uploading}
                  className="w-full p-2 border rounded-lg"
                />
              </div>

              {/* Video */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Video Yükle
                </label>
                <input
                  type="file"
                  accept="video/*"
                  onChange={(e) => handleUpload(e, 'VIDEO')}
                  disabled={uploading}
                  className="w-full p-2 border rounded-lg"
                />
              </div>

              {/* Döküman */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Döküman Yükle
                </label>
                <input
                  type="file"
                  accept=".pdf,.doc,.docx"
                  onChange={(e) => handleUpload(e, 'DOCUMENT')}
                  disabled={uploading}
                  className="w-full p-2 border rounded-lg"
                />
              </div>
            </div>

            {uploading && (
              <p className="text-center text-indigo-600 mt-4">
                📤 Yükleniyor...
              </p>
            )}
          </div>

          {/* Media Listesi */}
          {loading && (
            <div className="text-center py-8">
              <span className="text-indigo-600">Yükleniyor...</span>
            </div>
          )}

          {!loading && mediaData && (
            <div className="space-y-6">
              {/* Logolar */}
              {mediaData.logos && mediaData.logos.length > 0 && (
                <div className="bg-white border rounded-lg shadow-md p-6">
                  <h3 className="text-lg font-semibold text-gray-800 mb-4">
                    🏷️ Logolar ({mediaData.logos.length})
                  </h3>
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                    {mediaData.logos.map((media) => (
                      <div key={media.id} className="border rounded-lg overflow-hidden">
                        <img
                          src={media.fullUrl}
                          alt={media.originalFileName}
                          className="w-full h-32 object-contain bg-gray-50"
                        />
                        <div className="p-2 bg-gray-50">
                          <p className="text-xs text-gray-600 truncate mb-2">
                            {media.originalFileName}
                          </p>
                          <button
                            onClick={() => handleDeleteMedia(media.id, media.originalFileName)}
                            disabled={loading}
                            className="w-full bg-red-500 text-white text-xs py-1 rounded hover:bg-red-600 disabled:bg-gray-400"
                          >
                            🗑️ Sil
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Fotoğraflar */}
              {mediaData.photos && mediaData.photos.length > 0 && (
                <div className="bg-white border rounded-lg shadow-md p-6">
                  <h3 className="text-lg font-semibold text-gray-800 mb-4">
                    📸 Fotoğraflar ({mediaData.photos.length})
                  </h3>
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                    {mediaData.photos.map((media) => (
                      <div key={media.id} className="border rounded-lg overflow-hidden">
                        <img
                          src={media.fullUrl}
                          alt={media.originalFileName}
                          className="w-full h-32 object-cover"
                        />
                        <div className="p-2 bg-gray-50">
                          <p className="text-xs text-gray-600 truncate mb-2">
                            {media.originalFileName}
                          </p>
                          <button
                            onClick={() => handleDeleteMedia(media.id, media.originalFileName)}
                            disabled={loading}
                            className="w-full bg-red-500 text-white text-xs py-1 rounded hover:bg-red-600 disabled:bg-gray-400"
                          >
                            🗑️ Sil
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Videolar */}
              {mediaData.videos && mediaData.videos.length > 0 && (
                <div className="bg-white border rounded-lg shadow-md p-6">
                  <h3 className="text-lg font-semibold text-gray-800 mb-4">
                    🎥 Videolar ({mediaData.videos.length})
                  </h3>
                  <div className="space-y-2">
                    {mediaData.videos.map((media) => (
                      <div
                        key={media.id}
                        className="flex items-center justify-between p-3 border rounded-lg"
                      >
                        <div className="flex-1">
                          <p className="text-sm font-medium">{media.originalFileName}</p>
                          <p className="text-xs text-gray-500">
                            {(media.fileSize / 1024 / 1024).toFixed(2)} MB
                          </p>
                        </div>
                        <div className="flex space-x-2">
                          
                            href={media.fullUrl}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="bg-indigo-500 text-white px-3 py-1 text-sm rounded hover:bg-indigo-600"
                          <a>
                            ▶️ İzle
                          </a>
                          <button
                            onClick={() => handleDeleteMedia(media.id, media.originalFileName)}
                            disabled={loading}
                            className="bg-red-500 text-white px-3 py-1 text-sm rounded hover:bg-red-600 disabled:bg-gray-400"
                          >
                            🗑️ Sil
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Dökümanlar */}
              {mediaData.documents && mediaData.documents.length > 0 && (
                <div className="bg-white border rounded-lg shadow-md p-6">
                  <h3 className="text-lg font-semibold text-gray-800 mb-4">
                    📄 Dökümanlar ({mediaData.documents.length})
                  </h3>
                  <div className="space-y-2">
                    {mediaData.documents.map((media) => (
                      <div
                        key={media.id}
                        className="flex items-center justify-between p-3 border rounded-lg"
                      >
                        <div className="flex-1">
                          <p className="text-sm font-medium">{media.originalFileName}</p>
                          <p className="text-xs text-gray-500">
                            {(media.fileSize / 1024).toFixed(2)} KB
                          </p>
                        </div>
                        <div className="flex space-x-2">
                          
                            href={media.fullUrl}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="bg-indigo-500 text-white px-3 py-1 text-sm rounded hover:bg-indigo-600"
                          <a>
                            📥 İndir
                          </a>
                          <button
                            onClick={() => handleDeleteMedia(media.id, media.originalFileName)}
                            disabled={loading}
                            className="bg-red-500 text-white px-3 py-1 text-sm rounded hover:bg-red-600 disabled:bg-gray-400"
                          >
                            🗑️ Sil
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Toplam */}
              <div className="bg-gray-50 border rounded-lg p-4 text-center">
                <p className="text-sm text-gray-600">
                  📊 <strong>Toplam:</strong> {mediaData.totalFiles} dosya
                </p>
              </div>
            </div>
          )}

          {!loading && mediaData && mediaData.totalFiles === 0 && (
            <div className="bg-gray-50 border rounded-lg p-8 text-center">
              <p className="text-gray-500">📭 Henüz media dosyası yüklenmemiş</p>
            </div>
          )}
        </>
      )}
    </div>
  );
}
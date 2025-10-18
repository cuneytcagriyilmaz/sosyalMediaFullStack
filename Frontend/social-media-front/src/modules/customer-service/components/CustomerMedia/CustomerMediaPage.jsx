// modules/customer-service/components/CustomerMedia/CustomerMediaPage.jsx
import { useState, useEffect } from 'react';
import customerService from '../../services/customerService';
import {
  useCustomerMedia,
  useMediaUpload,
  useMediaSelection,
  useMediaFilters
} from '../../hooks/customerMediaHooks';
import {
  CustomerSelector,
  UploadSection,
  MediaGallery
} from './components';

export default function CustomerMediaPage() {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState('');
  const [error, setError] = useState(null);

  // Custom Hooks
  const { 
    allMedia, 
    loading, 
    refetch, 
    deleteMedia 
  } = useCustomerMedia(selectedCustomerId);

  const { 
    uploading, 
    uploadSingle, 
    uploadMultiple 
  } = useMediaUpload(selectedCustomerId, refetch);

  const selection = useMediaSelection();
  const filters = useMediaFilters(allMedia);

  // Müşterileri getir
  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (err) {
      console.error('Müşteriler yüklenemedi:', err);
      setError('Müşteriler yüklenemedi');
    }
  };

  const handleSelectCustomer = (customerId) => {
    setSelectedCustomerId(customerId);
    selection.clearSelection();
  };

  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        {error}
      </div>
    );
  }

  return (
    <div className="w-full space-y-6">
      {/* Başlık */}
      <div className="bg-gradient-to-r from-indigo-600 to-purple-600 rounded-lg shadow-lg p-6">
        <h2 className="text-3xl font-bold text-white flex items-center gap-3">
          <span>🖼️</span>
          Medya Yönetimi
        </h2>
        <p className="text-indigo-100 mt-2">
          Müşteri medya dosyalarını görüntüleyin, yükleyin ve yönetin
        </p>
      </div>

      {/* Müşteri Seçici */}
      <CustomerSelector
        customers={customers}
        selectedCustomerId={selectedCustomerId}
        onSelectCustomer={handleSelectCustomer}
        disabled={loading || uploading}
      />

      {/* Upload & Galeri (Sadece müşteri seçiliyse) */}
      {selectedCustomerId && (
        <>
          {/* Upload Section */}
          <UploadSection
            uploading={uploading}
            onUploadSingle={uploadSingle}
            onUploadMultiple={uploadMultiple}
          />

          {/* Loading State */}
          {loading && (
            <div className="text-center py-8">
              <span className="text-indigo-600 text-lg">⏳ Yükleniyor...</span>
            </div>
          )}

          {/* Media Gallery */}
          {!loading && allMedia.length > 0 && (
            <MediaGallery
              allMedia={allMedia}
              customerId={selectedCustomerId}
              onMediaUpdate={refetch}
              onDeleteMedia={deleteMedia}
              filters={filters}
              selection={selection}
            />
          )}

          {/* Empty State */}
          {!loading && allMedia.length === 0 && (
            <div className="bg-gray-50 border-2 border-dashed border-gray-300 rounded-lg p-12 text-center">
              <span className="text-6xl">📭</span>
              <p className="text-gray-600 text-lg mt-4 font-medium">
                Henüz media dosyası yüklenmemiş
              </p>
              <p className="text-gray-500 text-sm mt-2">
                Yukarıdaki formdan dosya yükleyebilirsiniz
              </p>
            </div>
          )}
        </>
      )}
    </div>
  );
}
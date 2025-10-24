// src/modules/customer-service/components/CustomerMedia/CustomerMediaPage.jsx

import { useState, useEffect } from 'react';
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
import customerService from '../../services/customerService';

export default function CustomerMediaPage() {
  const [customers, setCustomers] = useState([]); // ✅ Boş array
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
      const response = await customerService.getAllCustomers();
      
      console.log('📥 CustomerMediaPage response:', response);

      // ✅ Response formatını handle et
      if (response.success && response.data) {
        const customerArray = Array.isArray(response.data) 
          ? response.data 
          : [];
        
        console.log('✅ Setting customers:', customerArray.length);
        setCustomers(customerArray);
      } else if (Array.isArray(response)) {
        // Eski format
        console.log('✅ Setting customers (old format):', response.length);
        setCustomers(response);
      } else if (Array.isArray(response.data)) {
        console.log('✅ Setting customers (alt format):', response.data.length);
        setCustomers(response.data);
      } else {
        console.warn('⚠️ Unexpected response format:', response);
        setCustomers([]);
        setError('Müşteriler yüklenemedi');
      }
    } catch (err) {
      console.error('❌ Müşteriler yüklenemedi:', err);
      setCustomers([]); // ✅ Hata durumunda boş array
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
                Yukarıdaki formdan dosya yükleyebilirsinizzzzz
              </p>
            </div>
          )}
        </>
      )}
    </div>
  );
}
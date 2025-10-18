// modules/customer-service/components/CustomerMedia/components/UploadSection.jsx
import { useRef } from 'react';

export default function UploadSection({ uploading, onUploadSingle, onUploadMultiple }) {
  const logoInputRef = useRef(null);
  const photoInputRef = useRef(null);
  const videoInputRef = useRef(null);
  const documentInputRef = useRef(null);

  const handleSingleUpload = async (e, mediaType, inputRef) => {
    const file = e.target.files[0];
    if (!file) return;

    const result = await onUploadSingle(file, mediaType);
    alert(result.message);
    
    if (result.success && inputRef.current) {
      inputRef.current.value = '';
    }
  };

  const handleMultipleUpload = async (e, mediaType, inputRef) => {
    const files = Array.from(e.target.files);
    if (files.length === 0) return;

    const result = await onUploadMultiple(files, mediaType);
    alert(result.message);
    
    if (result.success && inputRef.current) {
      inputRef.current.value = '';
    }
  };

  return (
    <div className="bg-white border rounded-lg shadow-md p-6">
      <h3 className="text-lg font-semibold text-gray-800 mb-4">
        📤 Dosya Yükle
      </h3>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {/* Logo */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Logo Yükle
          </label>
          <input
            ref={logoInputRef}
            type="file"
            accept="image/*"
            onChange={(e) => handleSingleUpload(e, 'LOGO', logoInputRef)}
            disabled={uploading}
            className="w-full p-2 border rounded-lg disabled:bg-gray-100 disabled:cursor-not-allowed"
          />
        </div>

        {/* Fotoğraf */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Fotoğraf Yükle (Çoklu)
          </label>
          <input
            ref={photoInputRef}
            type="file"
            accept="image/*"
            multiple
            onChange={(e) => handleMultipleUpload(e, 'PHOTO', photoInputRef)}
            disabled={uploading}
            className="w-full p-2 border rounded-lg disabled:bg-gray-100 disabled:cursor-not-allowed"
          />
        </div>

        {/* Video */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Video Yükle
          </label>
          <input
            ref={videoInputRef}
            type="file"
            accept="video/*"
            onChange={(e) => handleSingleUpload(e, 'VIDEO', videoInputRef)}
            disabled={uploading}
            className="w-full p-2 border rounded-lg disabled:bg-gray-100 disabled:cursor-not-allowed"
          />
        </div>

        {/* Döküman */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Döküman Yükle
          </label>
          <input
            ref={documentInputRef}
            type="file"
            accept=".pdf,.doc,.docx"
            onChange={(e) => handleSingleUpload(e, 'DOCUMENT', documentInputRef)}
            disabled={uploading}
            className="w-full p-2 border rounded-lg disabled:bg-gray-100 disabled:cursor-not-allowed"
          />
        </div>
      </div>

      {uploading && (
        <p className="text-center text-indigo-600 mt-4">
          📤 Yükleniyor...
        </p>
      )}
    </div>
  );
}
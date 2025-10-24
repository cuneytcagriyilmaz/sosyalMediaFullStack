// src/modules/customer-service/components/CustomerMedia/components/UploadSection.jsx

import { useState } from 'react';
import { Upload, File, Image, Video, FileText } from 'lucide-react';

export default function UploadSection({ uploading, onUploadSingle, onUploadMultiple }) {
  const [uploadMode, setUploadMode] = useState('single'); // 'single' | 'multiple'
  const [selectedType, setSelectedType] = useState('LOGO');
  
  // Single file state
  const [singleFile, setSingleFile] = useState(null);
  
  // Multiple files state
  const [logoFiles, setLogoFiles] = useState([]);
  const [photoFiles, setPhotoFiles] = useState([]);
  const [videoFiles, setVideoFiles] = useState([]);
  const [documentFiles, setDocumentFiles] = useState([]);

  const handleSingleUpload = async () => {
    if (!singleFile) {
      alert('Lütfen bir dosya seçin!');
      return;
    }

    const result = await onUploadSingle(singleFile, selectedType);
    if (result.success) {
      alert(result.message);
      setSingleFile(null);
    } else {
      alert(result.message);
    }
  };

  const handleMultipleUpload = async () => {
    const allFiles = [
      ...logoFiles,
      ...photoFiles,
      ...videoFiles,
      ...documentFiles
    ];

    if (allFiles.length === 0) {
      alert('Lütfen en az bir dosya seçin!');
      return;
    }

    // Her tip için ayrı ayrı yükle
    const promises = [];
    
    if (logoFiles.length > 0) {
      promises.push(onUploadMultiple(logoFiles, 'LOGO'));
    }
    if (photoFiles.length > 0) {
      promises.push(onUploadMultiple(photoFiles, 'PHOTO'));
    }
    if (videoFiles.length > 0) {
      promises.push(onUploadMultiple(videoFiles, 'VIDEO'));
    }
    if (documentFiles.length > 0) {
      promises.push(onUploadMultiple(documentFiles, 'DOCUMENT'));
    }

    const results = await Promise.all(promises);
    const allSuccess = results.every(r => r.success);

    if (allSuccess) {
      alert('✅ Tüm dosyalar başarıyla yüklendi!');
      setLogoFiles([]);
      setPhotoFiles([]);
      setVideoFiles([]);
      setDocumentFiles([]);
    } else {
      alert('⚠️ Bazı dosyalar yüklenemedi!');
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-lg p-6 border border-gray-200">
      <h3 className="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
        <Upload size={24} className="text-indigo-600" />
        Medya Yükleme
      </h3>

      {/* Mode Selector */}
      <div className="flex gap-4 mb-6">
        <button
          onClick={() => setUploadMode('single')}
          className={`flex-1 py-3 rounded-lg font-medium transition ${
            uploadMode === 'single'
              ? 'bg-indigo-600 text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          Tek Dosya
        </button>
        <button
          onClick={() => setUploadMode('multiple')}
          className={`flex-1 py-3 rounded-lg font-medium transition ${
            uploadMode === 'multiple'
              ? 'bg-indigo-600 text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          Çoklu Dosya
        </button>
      </div>

      {/* Single Upload */}
      {uploadMode === 'single' && (
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Dosya Tipi
            </label>
            <select
              value={selectedType}
              onChange={(e) => setSelectedType(e.target.value)}
              className="w-full border border-gray-300 rounded-lg p-3 bg-white text-gray-900 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
              style={{ color: '#111827' }} // ✅ Explicit text color
            >
              <option value="LOGO" style={{ color: '#111827' }}>🎨 Logo</option>
              <option value="PHOTO" style={{ color: '#111827' }}>📸 Fotoğraf</option>
              <option value="VIDEO" style={{ color: '#111827' }}>🎥 Video</option>
              <option value="DOCUMENT" style={{ color: '#111827' }}>📄 Doküman</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Dosya Seç
            </label>
            <input
              type="file"
              onChange={(e) => setSingleFile(e.target.files[0])}
              className="w-full border border-gray-300 rounded-lg p-3 bg-white text-gray-900 file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:bg-indigo-50 file:text-indigo-700 hover:file:bg-indigo-100"
            />
          </div>

          <button
            onClick={handleSingleUpload}
            disabled={uploading || !singleFile}
            className="w-full py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition flex items-center justify-center gap-2"
          >
            {uploading ? (
              <>
                <div className="animate-spin rounded-full h-5 w-5 border-2 border-white border-t-transparent"></div>
                Yükleniyor...
              </>
            ) : (
              <>
                <Upload size={20} />
                Dosyayı Yükle
              </>
            )}
          </button>
        </div>
      )}

      {/* Multiple Upload */}
      {uploadMode === 'multiple' && (
        <div className="space-y-4">
          {/* Logo Files */}
          <div className="border border-gray-200 rounded-lg p-4 bg-gray-50">
            <label className="flex items-center gap-2 text-sm font-medium text-gray-700 mb-2">
              <Image size={18} className="text-purple-600" />
              Logo Dosyaları
            </label>
            <input
              type="file"
              multiple
              accept="image/*"
              onChange={(e) => setLogoFiles(Array.from(e.target.files))}
              className="w-full border border-gray-300 rounded-lg p-2 bg-white text-gray-900 text-sm file:mr-4 file:py-1 file:px-3 file:rounded file:border-0 file:bg-purple-50 file:text-purple-700"
            />
            {logoFiles.length > 0 && (
              <p className="text-xs text-gray-600 mt-2">
                {logoFiles.length} dosya seçildi
              </p>
            )}
          </div>

          {/* Photo Files */}
          <div className="border border-gray-200 rounded-lg p-4 bg-gray-50">
            <label className="flex items-center gap-2 text-sm font-medium text-gray-700 mb-2">
              <Image size={18} className="text-blue-600" />
              Fotoğraf Dosyaları
            </label>
            <input
              type="file"
              multiple
              accept="image/*"
              onChange={(e) => setPhotoFiles(Array.from(e.target.files))}
              className="w-full border border-gray-300 rounded-lg p-2 bg-white text-gray-900 text-sm file:mr-4 file:py-1 file:px-3 file:rounded file:border-0 file:bg-blue-50 file:text-blue-700"
            />
            {photoFiles.length > 0 && (
              <p className="text-xs text-gray-600 mt-2">
                {photoFiles.length} dosya seçildi
              </p>
            )}
          </div>

          {/* Video Files */}
          <div className="border border-gray-200 rounded-lg p-4 bg-gray-50">
            <label className="flex items-center gap-2 text-sm font-medium text-gray-700 mb-2">
              <Video size={18} className="text-red-600" />
              Video Dosyaları
            </label>
            <input
              type="file"
              multiple
              accept="video/*"
              onChange={(e) => setVideoFiles(Array.from(e.target.files))}
              className="w-full border border-gray-300 rounded-lg p-2 bg-white text-gray-900 text-sm file:mr-4 file:py-1 file:px-3 file:rounded file:border-0 file:bg-red-50 file:text-red-700"
            />
            {videoFiles.length > 0 && (
              <p className="text-xs text-gray-600 mt-2">
                {videoFiles.length} dosya seçildi
              </p>
            )}
          </div>

          {/* Document Files */}
          <div className="border border-gray-200 rounded-lg p-4 bg-gray-50">
            <label className="flex items-center gap-2 text-sm font-medium text-gray-700 mb-2">
              <FileText size={18} className="text-green-600" />
              Doküman Dosyaları
            </label>
            <input
              type="file"
              multiple
              accept=".pdf,.doc,.docx,.xls,.xlsx"
              onChange={(e) => setDocumentFiles(Array.from(e.target.files))}
              className="w-full border border-gray-300 rounded-lg p-2 bg-white text-gray-900 text-sm file:mr-4 file:py-1 file:px-3 file:rounded file:border-0 file:bg-green-50 file:text-green-700"
            />
            {documentFiles.length > 0 && (
              <p className="text-xs text-gray-600 mt-2">
                {documentFiles.length} dosya seçildi
              </p>
            )}
          </div>

          <button
            onClick={handleMultipleUpload}
            disabled={uploading}
            className="w-full py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition flex items-center justify-center gap-2"
          >
            {uploading ? (
              <>
                <div className="animate-spin rounded-full h-5 w-5 border-2 border-white border-t-transparent"></div>
                Yükleniyor...
              </>
            ) : (
              <>
                <Upload size={20} />
                Tüm Dosyaları Yükle
              </>
            )}
          </button>
        </div>
      )}
    </div>
  );
}
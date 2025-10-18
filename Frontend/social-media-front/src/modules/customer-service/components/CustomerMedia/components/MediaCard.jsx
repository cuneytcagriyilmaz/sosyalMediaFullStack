// modules/customer-service/components/CustomerMedia/components/MediaCard.jsx
import { useState } from 'react';

const formatFileSize = (bytes) => {
  if (bytes < 1024) return bytes + ' B';
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
  return (bytes / 1024 / 1024).toFixed(2) + ' MB';
};

const getFileIcon = (mediaType) => {
  const icons = {
    LOGO: '🏷️',
    PHOTO: '📸',
    VIDEO: '🎥',
    DOCUMENT: '📄'
  };
  return icons[mediaType] || '📁';
};

export default function MediaCard({ 
  file, 
  isSelected, 
  onToggleSelect, 
  onPreview, 
  viewMode = 'grid' 
}) {
  const [isHovered, setIsHovered] = useState(false);

  const handleDownload = (e) => {
    e.stopPropagation();
    window.open(file.fullUrl, '_blank');
  };

  if (viewMode === 'list') {
    return (
      <div
        className={`flex items-center gap-4 p-3 rounded-lg border transition ${
          isSelected 
            ? 'bg-indigo-50 border-indigo-300' 
            : 'bg-white border-gray-200 hover:border-gray-300 hover:shadow-sm'
        }`}
      >
        {/* Checkbox */}
        <input
          type="checkbox"
          checked={isSelected}
          onChange={onToggleSelect}
          className="w-5 h-5 text-indigo-600 rounded"
          onClick={(e) => e.stopPropagation()}
        />

        {/* Icon */}
        <span className="text-2xl">{getFileIcon(file.mediaType)}</span>

        {/* Dosya Adı */}
        <div className="flex-1 min-w-0">
          <p className="text-sm font-medium text-gray-800 truncate">
            {file.originalFileName}
          </p>
        </div>

        {/* Format */}
        <span className="text-xs text-gray-500 uppercase">
          {file.originalFileName.split('.').pop()}
        </span>

        {/* Boyut */}
        <span className="text-sm text-gray-600 w-20 text-right">
          {formatFileSize(file.fileSize)}
        </span>

        {/* Aksiyonlar */}
        <div className="flex gap-2">
          <button
            onClick={onPreview}
            className="p-2 text-gray-600 hover:text-indigo-600 hover:bg-indigo-50 rounded transition"
            title="Görüntüle"
          >
            👁️
          </button>
          <button
            onClick={handleDownload}
            className="p-2 text-gray-600 hover:text-green-600 hover:bg-green-50 rounded transition"
            title="İndir"
          >
            ⬇️
          </button>
        </div>
      </div>
    );
  }

  // Grid Görünüm
  return (
    <div
      className={`relative rounded-lg border overflow-hidden transition-all cursor-pointer ${
        isSelected 
          ? 'ring-2 ring-indigo-500 border-indigo-500' 
          : 'border-gray-200 hover:border-gray-300 hover:shadow-lg'
      }`}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      {/* Checkbox */}
      <div className="absolute top-2 left-2 z-10">
        <input
          type="checkbox"
          checked={isSelected}
          onChange={onToggleSelect}
          className="w-5 h-5 text-indigo-600 rounded shadow-sm"
          onClick={(e) => e.stopPropagation()}
        />
      </div>

      {/* Thumbnail */}
      <div 
        className="aspect-square w-full bg-gray-100 flex items-center justify-center overflow-hidden relative"
        onClick={onPreview}
      >
        {file.mediaType === 'VIDEO' ? (
          <>
            <video 
              src={file.fullUrl} 
              className="w-full h-full object-cover"
            />
            <div className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-30">
              <span className="text-white text-3xl">▶️</span>
            </div>
          </>
        ) : file.mediaType === 'DOCUMENT' ? (
          <div className="text-center">
            <span className="text-4xl">📄</span>
            <p className="text-xs text-gray-500 mt-2 uppercase">
              {file.originalFileName.split('.').pop()}
            </p>
          </div>
        ) : (
          <img 
            src={file.fullUrl} 
            alt={file.originalFileName}
            className="w-full h-full object-cover"
          />
        )}

        {/* Hover Overlay */}
        {isHovered && (
          <div className="absolute inset-0 bg-black bg-opacity-50 flex items-center justify-center gap-2 transition-opacity">
            <button
              onClick={(e) => {
                e.stopPropagation();
                onPreview();
              }}
              className="p-2 bg-white rounded-full hover:bg-gray-100 transition text-sm"
              title="Görüntüle"
            >
              👁️
            </button>
            <button
              onClick={handleDownload}
              className="p-2 bg-white rounded-full hover:bg-gray-100 transition text-sm"
              title="İndir"
            >
              ⬇️
            </button>
          </div>
        )}
      </div>

      {/* Dosya Bilgisi */}
      <div className="p-2 bg-white border-t border-gray-200">
        <p className="text-xs font-medium text-gray-800 truncate" title={file.originalFileName}>
          {file.originalFileName}
        </p>
        <div className="flex items-center justify-between mt-0.5">
          <span className="text-xs text-gray-500">
            {formatFileSize(file.fileSize)}
          </span>
          <span className="text-xs text-gray-400">
            {getFileIcon(file.mediaType)}
          </span>
        </div>
      </div>
    </div>
  );
}
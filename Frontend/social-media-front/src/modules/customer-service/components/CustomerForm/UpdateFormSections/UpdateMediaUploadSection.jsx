// modules/customer-service/components/CustomerForm/UpdateFormSections/UpdateMediaUploadSection.jsx
export default function UpdateMediaUploadSection({ 
  logoFiles,
  setLogoFiles,
  photoFiles,
  setPhotoFiles,
  videoFiles,
  setVideoFiles,
  documentFiles,
  setDocumentFiles,
  onUpload,
  loading,
  inputClass,
  labelClass 
}) {
  return (
    <section className="bg-gradient-to-r from-purple-50 to-indigo-50 p-4 sm:p-6 rounded-lg border-2 border-purple-200">
      <h3 className="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4 flex items-center">
        <span className="mr-2">📤</span> Yeni Medya Ekle
      </h3>
      
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4 mb-4">
        <div>
          <label className={labelClass}>Logo(lar)</label>
          <input
            type="file"
            multiple
            accept="image/*"
            onChange={(e) => setLogoFiles(Array.from(e.target.files))}
            className={inputClass}
          />
          {logoFiles.length > 0 && (
            <p className="text-xs sm:text-sm text-green-600 mt-1 font-medium">
              ✅ {logoFiles.length} dosya seçildi
            </p>
          )}
        </div>

        <div>
          <label className={labelClass}>Fotoğraf(lar)</label>
          <input
            type="file"
            multiple
            accept="image/*"
            onChange={(e) => setPhotoFiles(Array.from(e.target.files))}
            className={inputClass}
          />
          {photoFiles.length > 0 && (
            <p className="text-xs sm:text-sm text-green-600 mt-1 font-medium">
              ✅ {photoFiles.length} dosya seçildi
            </p>
          )}
        </div>

        <div>
          <label className={labelClass}>Video(lar)</label>
          <input
            type="file"
            multiple
            accept="video/*"
            onChange={(e) => setVideoFiles(Array.from(e.target.files))}
            className={inputClass}
          />
          {videoFiles.length > 0 && (
            <p className="text-xs sm:text-sm text-green-600 mt-1 font-medium">
              ✅ {videoFiles.length} dosya seçildi
            </p>
          )}
        </div>

        <div>
          <label className={labelClass}>Döküman(lar)</label>
          <input
            type="file"
            multiple
            accept=".pdf,.doc,.docx,.xls,.xlsx"
            onChange={(e) => setDocumentFiles(Array.from(e.target.files))}
            className={inputClass}
          />
          {documentFiles.length > 0 && (
            <p className="text-xs sm:text-sm text-green-600 mt-1 font-medium">
              ✅ {documentFiles.length} dosya seçildi
            </p>
          )}
        </div>
      </div>

      <button
        type="button"
        onClick={onUpload}
        disabled={loading}
        className="w-full bg-purple-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-purple-700 transition disabled:bg-gray-400 flex items-center justify-center space-x-2"
      >
        {loading ? (
          <>
            <span className="animate-spin">⏳</span>
            <span className="text-sm sm:text-base">Yükleniyor...</span>
          </>
        ) : (
          <>
            <span>📤</span>
            <span className="text-sm sm:text-base">Medya Yükle</span>
          </>
        )}
      </button>
    </section>
  );
}
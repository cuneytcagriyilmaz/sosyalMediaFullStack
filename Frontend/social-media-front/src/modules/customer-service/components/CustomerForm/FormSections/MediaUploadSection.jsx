// modules/customer-service/components/CustomerForm/FormSections/MediaUploadSection.jsx
export default function MediaUploadSection({ 
  logoFiles,
  setLogoFiles,
  photoFiles,
  setPhotoFiles,
  videoFiles,
  setVideoFiles,
  documentFiles,
  setDocumentFiles,
  inputClass,
  labelClass 
}) {
  return (
    <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
      <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
        <span className="mr-2">🖼️</span> Medya Dosyaları
      </h3>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
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
            <p className="text-sm text-green-600 mt-1 font-medium">
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
            <p className="text-sm text-green-600 mt-1 font-medium">
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
            <p className="text-sm text-green-600 mt-1 font-medium">
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
            <p className="text-sm text-green-600 mt-1 font-medium">
              ✅ {documentFiles.length} dosya seçildi
            </p>
          )}
        </div>
      </div>
    </section>
  );
}
// modules/customer-service/components/CustomerForm/FormSections/SocialMediaSection.jsx
export default function SocialMediaSection({ socialMedia, updateNested, inputClass, labelClass }) {
  return (
    <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
      <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
        <span className="mr-2">📱</span> Sosyal Medya Hesapları
      </h3>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label className={labelClass}>Instagram</label>
          <input
            type="text"
            value={socialMedia.instagram}
            onChange={(e) => updateNested('socialMedia', 'instagram', e.target.value)}
            className={inputClass}
            placeholder="@kullaniciadi"
          />
        </div>

        <div>
          <label className={labelClass}>Facebook</label>
          <input
            type="text"
            value={socialMedia.facebook}
            onChange={(e) => updateNested('socialMedia', 'facebook', e.target.value)}
            className={inputClass}
            placeholder="Sayfa adı"
          />
        </div>

        <div>
          <label className={labelClass}>TikTok</label>
          <input
            type="text"
            value={socialMedia.tiktok}
            onChange={(e) => updateNested('socialMedia', 'tiktok', e.target.value)}
            className={inputClass}
            placeholder="@kullaniciadi"
          />
        </div>
      </div>
    </section>
  );
}
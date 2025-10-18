// modules/customer-service/components/CustomerForm/FormSections/SeoSection.jsx
export default function SeoSection({ seo, updateNested, inputClass, labelClass }) {
  return (
    <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
      <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
        <span className="mr-2">🔍</span> SEO Bilgileri
      </h3>
      
      <div className="space-y-4">
        <div>
          <label className={labelClass}>Google Search Console Email</label>
          <input
            type="email"
            value={seo.googleConsoleEmail}
            onChange={(e) => updateNested('seo', 'googleConsoleEmail', e.target.value)}
            className={inputClass}
            placeholder="console@example.com"
          />
        </div>

        <div>
          <label className={labelClass}>SEO Başlık Önerileri</label>
          <textarea
            value={seo.titleSuggestions}
            onChange={(e) => updateNested('seo', 'titleSuggestions', e.target.value)}
            className={inputClass}
            rows="2"
            placeholder="Örn: Antalya'nın En İyi Cafe'si - Kahve Dünyası"
          />
        </div>

        <div>
          <label className={labelClass}>SEO İçerik Önerileri</label>
          <textarea
            value={seo.contentSuggestions}
            onChange={(e) => updateNested('seo', 'contentSuggestions', e.target.value)}
            className={inputClass}
            rows="3"
            placeholder="SEO için içerik açıklaması..."
          />
        </div>
      </div>
    </section>
  );
}


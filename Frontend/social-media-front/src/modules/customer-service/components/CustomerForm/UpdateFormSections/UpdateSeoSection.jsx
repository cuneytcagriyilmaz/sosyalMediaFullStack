// modules/customer-service/components/CustomerForm/UpdateFormSections/UpdateSeoSection.jsx
import { SaveButton } from '../FormComponents';

export default function UpdateSeoSection({ 
  seo, 
  updateNested, 
  onSave,
  loading,
  inputClass, 
  labelClass 
}) {
  return (
    <section className="bg-gray-50 p-4 sm:p-6 rounded-lg border border-gray-200">
      <h3 className="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4 flex items-center">
        <span className="mr-2">🔍</span> SEO
      </h3>
      
      <div className="space-y-3 sm:space-y-4">
        <div>
          <label className={labelClass}>Google Console Email</label>
          <input
            type="email"
            value={seo.googleConsoleEmail || ""}
            onChange={(e) => updateNested('seo', 'googleConsoleEmail', e.target.value)}
            className={inputClass}
            placeholder="console@example.com"
          />
        </div>

        <div>
          <label className={labelClass}>SEO Başlık Önerileri</label>
          <textarea
            value={seo.titleSuggestions || ""}
            onChange={(e) => updateNested('seo', 'titleSuggestions', e.target.value)}
            className={inputClass}
            rows="2"
            placeholder="SEO başlık önerileri..."
          />
        </div>

        <div>
          <label className={labelClass}>SEO İçerik Önerileri</label>
          <textarea
            value={seo.contentSuggestions || ""}
            onChange={(e) => updateNested('seo', 'contentSuggestions', e.target.value)}
            className={inputClass}
            rows="3"
            placeholder="SEO içerik önerileri..."
          />
        </div>
      </div>

      <SaveButton onClick={onSave} loading={loading} />
    </section>
  );
}
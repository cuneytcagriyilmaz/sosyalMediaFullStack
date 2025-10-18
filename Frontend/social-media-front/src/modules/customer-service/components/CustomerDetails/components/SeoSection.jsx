//src/modules/customer-service/components/CustomerDetails/components/SeoSection.jsx
export default function SeoSection({ seo }) {
  if (!seo) return null;

  const hasContent = seo.googleConsoleEmail || seo.titleSuggestions || seo.contentSuggestions;
  if (!hasContent) return null;

  return (
    <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
      <h4 className="text-lg font-semibold text-indigo-600 mb-4 flex items-center">
        <span className="mr-2">🔍</span> SEO Bilgileri
      </h4>
      <div className="space-y-4">
        {seo.googleConsoleEmail && (
          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-xs text-gray-500 uppercase mb-1">Google Console Email</p>
            <p className="font-medium text-gray-800">{seo.googleConsoleEmail}</p>
          </div>
        )}
        {seo.titleSuggestions && (
          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-xs text-gray-500 uppercase mb-2">SEO Başlık Önerileri</p>
            <p className="text-sm text-gray-700">{seo.titleSuggestions}</p>
          </div>
        )}
        {seo.contentSuggestions && (
          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-xs text-gray-500 uppercase mb-2">SEO İçerik Önerileri</p>
            <p className="text-sm text-gray-700">{seo.contentSuggestions}</p>
          </div>
        )}
      </div>
    </section>
  );
}
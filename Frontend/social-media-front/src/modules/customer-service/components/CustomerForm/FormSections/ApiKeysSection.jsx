// modules/customer-service/components/CustomerForm/FormSections/ApiKeysSection.jsx
export default function ApiKeysSection({ apiKeys, updateNested, inputClass, labelClass }) {
  return (
    <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
      <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
        <span className="mr-2">🔑</span> API Anahtarları
      </h3>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className={labelClass}>Instagram API Key</label>
          <input
            type="text"
            value={apiKeys.instagramApiKey}
            onChange={(e) => updateNested('apiKeys', 'instagramApiKey', e.target.value)}
            className={inputClass}
            placeholder="Instagram API anahtarı"
          />
        </div>

        <div>
          <label className={labelClass}>Facebook API Key</label>
          <input
            type="text"
            value={apiKeys.facebookApiKey}
            onChange={(e) => updateNested('apiKeys', 'facebookApiKey', e.target.value)}
            className={inputClass}
            placeholder="Facebook API anahtarı"
          />
        </div>

        <div>
          <label className={labelClass}>TikTok API Key</label>
          <input
            type="text"
            value={apiKeys.tiktokApiKey}
            onChange={(e) => updateNested('apiKeys', 'tiktokApiKey', e.target.value)}
            className={inputClass}
            placeholder="TikTok API anahtarı"
          />
        </div>

        <div>
          <label className={labelClass}>Google API Key</label>
          <input
            type="text"
            value={apiKeys.googleApiKey}
            onChange={(e) => updateNested('apiKeys', 'googleApiKey', e.target.value)}
            className={inputClass}
            placeholder="Google API anahtarı"
          />
        </div>
      </div>
    </section>
  );
}
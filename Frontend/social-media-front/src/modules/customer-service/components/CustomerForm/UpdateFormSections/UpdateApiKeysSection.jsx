// modules/customer-service/components/CustomerForm/UpdateFormSections/UpdateApiKeysSection.jsx
import { SaveButton } from '../FormComponents';

export default function UpdateApiKeysSection({ 
  apiKeys, 
  updateNested, 
  onSave,
  loading,
  inputClass, 
  labelClass 
}) {
  return (
    <section className="bg-gray-50 p-4 sm:p-6 rounded-lg border border-gray-200">
      <h3 className="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4 flex items-center">
        <span className="mr-2">🔑</span> API Anahtarları
      </h3>
      
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
        <div>
          <label className={labelClass}>Instagram API</label>
          <input
            type="text"
            value={apiKeys.instagramApiKey || ""}
            onChange={(e) => updateNested('apiKeys', 'instagramApiKey', e.target.value)}
            className={inputClass}
            placeholder="Instagram API"
          />
        </div>

        <div>
          <label className={labelClass}>Facebook API</label>
          <input
            type="text"
            value={apiKeys.facebookApiKey || ""}
            onChange={(e) => updateNested('apiKeys', 'facebookApiKey', e.target.value)}
            className={inputClass}
            placeholder="Facebook API"
          />
        </div>

        <div>
          <label className={labelClass}>TikTok API</label>
          <input
            type="text"
            value={apiKeys.tiktokApiKey || ""}
            onChange={(e) => updateNested('apiKeys', 'tiktokApiKey', e.target.value)}
            className={inputClass}
            placeholder="TikTok API"
          />
        </div>

        <div>
          <label className={labelClass}>Google API</label>
          <input
            type="text"
            value={apiKeys.googleApiKey || ""}
            onChange={(e) => updateNested('apiKeys', 'googleApiKey', e.target.value)}
            className={inputClass}
            placeholder="Google API"
          />
        </div>
      </div>

      <SaveButton onClick={onSave} loading={loading} />
    </section>
  );
}
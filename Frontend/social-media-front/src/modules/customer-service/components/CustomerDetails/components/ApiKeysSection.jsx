//src/modules/customer-service/components/CustomerDetails/components/ApiKeysSection.jsx
export default function ApiKeysSection({ apiKeys }) {
  if (!apiKeys) return null;

  const hasKeys = apiKeys.instagramApiKey || apiKeys.facebookApiKey || 
                  apiKeys.tiktokApiKey || apiKeys.googleApiKey;
  if (!hasKeys) return null;

  return (
    <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
      <h4 className="text-lg font-semibold text-indigo-600 mb-4 flex items-center">
        <span className="mr-2">🔑</span> API Anahtarları
      </h4>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {apiKeys.instagramApiKey && (
          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-xs text-gray-500 uppercase mb-1">Instagram API Key</p>
            <p className="font-mono text-xs break-all text-gray-700">{apiKeys.instagramApiKey}</p>
          </div>
        )}
        {apiKeys.facebookApiKey && (
          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-xs text-gray-500 uppercase mb-1">Facebook API Key</p>
            <p className="font-mono text-xs break-all text-gray-700">{apiKeys.facebookApiKey}</p>
          </div>
        )}
        {apiKeys.tiktokApiKey && (
          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-xs text-gray-500 uppercase mb-1">TikTok API Key</p>
            <p className="font-mono text-xs break-all text-gray-700">{apiKeys.tiktokApiKey}</p>
          </div>
        )}
        {apiKeys.googleApiKey && (
          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-xs text-gray-500 uppercase mb-1">Google API Key</p>
            <p className="font-mono text-xs break-all text-gray-700">{apiKeys.googleApiKey}</p>
          </div>
        )}
      </div>
    </section>
  );
}
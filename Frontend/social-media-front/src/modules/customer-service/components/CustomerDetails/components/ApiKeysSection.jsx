//src/modules/customer-service/components/CustomerDetails/components/ApiKeysSection.jsx

import CopyButton from "../../../../../shared/components/CopyButton/CopyButton";

 
export default function ApiKeysSection({ apiKeys }) {
  if (!apiKeys) return null;

  const hasKeys = apiKeys.instagramApiKey || apiKeys.facebookApiKey || 
                  apiKeys.tiktokApiKey || apiKeys.googleApiKey;
  if (!hasKeys) return null;

  const ApiKeyCard = ({ platform, apiKey, icon }) => (
    <div className="bg-gradient-to-br from-gray-50 to-gray-100 p-5 rounded-lg border border-gray-200 hover:shadow-md transition-shadow">
      <div className="flex items-center justify-between mb-3">
        <p className="text-sm text-gray-600 font-semibold flex items-center gap-2">
          <span className="text-lg">{icon}</span>
          <span>{platform} API Key</span>
        </p>
        <CopyButton 
          text={apiKey} 
          size="md" 
          variant="outline"
          showText={false}
        />
      </div>
      <div className="bg-white p-3 rounded border border-gray-300 shadow-sm">
        <p className="font-mono text-sm break-all text-gray-800">
          {apiKey}
        </p>
      </div>
    </div>
  );

  return (
    <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
      <h4 className="text-lg font-semibold text-indigo-600 mb-4 flex items-center">
        <span className="mr-2">🔑</span> API Anahtarları
      </h4>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {apiKeys.instagramApiKey && (
          <ApiKeyCard 
            platform="Instagram" 
            apiKey={apiKeys.instagramApiKey}
            icon="📸"
          />
        )}
        {apiKeys.facebookApiKey && (
          <ApiKeyCard 
            platform="Facebook" 
            apiKey={apiKeys.facebookApiKey}
            icon="👤"
          />
        )}
        {apiKeys.tiktokApiKey && (
          <ApiKeyCard 
            platform="TikTok" 
            apiKey={apiKeys.tiktokApiKey}
            icon="🎵"
          />
        )}
        {apiKeys.googleApiKey && (
          <ApiKeyCard 
            platform="Google" 
            apiKey={apiKeys.googleApiKey}
            icon="🔍"
          />
        )}
      </div>
    </section>
  );
}
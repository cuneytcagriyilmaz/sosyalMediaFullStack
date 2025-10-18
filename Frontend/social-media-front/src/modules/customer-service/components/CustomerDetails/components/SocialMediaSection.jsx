//src/modules/customer-service/components/CustomerDetails/components/SocialMediaSection.jsx

export default function SocialMediaSection({ socialMedia }) {
  if (!socialMedia) return null;

  return (
    <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
      <h4 className="text-lg font-semibold text-indigo-600 mb-4 flex items-center">
        <span className="mr-2">📱</span> Sosyal Medya
      </h4>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-gray-50 p-4 rounded-lg">
          <p className="text-xs text-gray-500 uppercase mb-1">Instagram</p>
          <p className="font-medium text-gray-800">{socialMedia.instagram || '-'}</p>
        </div>
        <div className="bg-gray-50 p-4 rounded-lg">
          <p className="text-xs text-gray-500 uppercase mb-1">Facebook</p>
          <p className="font-medium text-gray-800">{socialMedia.facebook || '-'}</p>
        </div>
        <div className="bg-gray-50 p-4 rounded-lg">
          <p className="text-xs text-gray-500 uppercase mb-1">TikTok</p>
          <p className="font-medium text-gray-800">{socialMedia.tiktok || '-'}</p>
        </div>
      </div>
    </section>
  );
}
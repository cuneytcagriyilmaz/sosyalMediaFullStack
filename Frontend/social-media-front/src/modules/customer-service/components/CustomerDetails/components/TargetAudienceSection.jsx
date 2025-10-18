//src/modules/customer-service/components/CustomerDetails/components/TargetAudienceSection.jsx
export default function TargetAudienceSection({ targetAudience }) {
    if (!targetAudience) return null;

    return (
        <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
            <h4 className="text-lg font-semibold text-indigo-600 mb-4 flex items-center">
                <span className="mr-2">🎯</span> Hedef Kitle ve İçerik Stratejisi
            </h4>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <div className="bg-gray-50 p-4 rounded-lg">
                    <p className="text-xs text-gray-500 uppercase mb-1">Post Türü</p>
                    <p className="font-medium text-gray-800 capitalize">{targetAudience.postType || '-'}</p>
                </div>
                <div className="bg-gray-50 p-4 rounded-lg">
                    <p className="text-xs text-gray-500 uppercase mb-1">Haftalık Post Sıklığı</p>
                    <p className="font-medium text-gray-800">{targetAudience.postFrequency || '-'}</p>
                </div>
                <div className="bg-gray-50 p-4 rounded-lg">
                    <p className="text-xs text-gray-500 uppercase mb-1">Post Tonu</p>
                    <p className="font-medium text-gray-800 capitalize">{targetAudience.postTone || '-'}</p>
                </div>
                <div className="bg-gray-50 p-4 rounded-lg">
                    <p className="text-xs text-gray-500 uppercase mb-1">Hedef Bölge</p>
                    <p className="font-medium text-gray-800">{targetAudience.targetRegion || '-'}</p>
                </div>
                <div className="bg-gray-50 p-4 rounded-lg">
                    <p className="text-xs text-gray-500 uppercase mb-1">Hedef Yaş Aralığı</p>
                    <p className="font-medium text-gray-800">{targetAudience.audienceAge || '-'}</p>
                </div>
                <div className="bg-gray-50 p-4 rounded-lg">
                    <p className="text-xs text-gray-500 uppercase mb-1">Özel Günler</p>
                    <p className="font-medium text-gray-800">
                        {targetAudience.specialDates ? '✅ Aktif' : '❌ Pasif'}
                    </p>
                </div>
                {targetAudience.customerHashtags && (
                    <div className="bg-gray-50 p-4 rounded-lg md:col-span-2 lg:col-span-3">
                        <p className="text-xs text-gray-500 uppercase mb-1">Hashtagler</p>
                        <p className="font-medium text-sm text-gray-800">{targetAudience.customerHashtags}</p>
                    </div>
                )}
                {targetAudience.audienceInterests && (
                    <div className="bg-gray-50 p-4 rounded-lg md:col-span-2 lg:col-span-3">
                        <p className="text-xs text-gray-500 uppercase mb-1">İlgi Alanları</p>
                        <p className="font-medium text-sm text-gray-800">{targetAudience.audienceInterests}</p>
                    </div>
                )}
            </div>
        </section>
    );
}
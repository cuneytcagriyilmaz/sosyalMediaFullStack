// src/components/customerComponents/CustomerView.jsx
import { useState, useEffect } from "react";
import customerService from "../../services/customerService";

export default function CustomerView() {
    const [customers, setCustomers] = useState([]);
    const [selectedCustomer, setSelectedCustomer] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchCustomers();
    }, []);

    const fetchCustomers = async () => {
        setLoading(true);
        try {
            const data = await customerService.getAllCustomers();
            setCustomers(data);
        } catch (err) {
            console.error("Müşteriler yüklenemedi:", err);
            setError("Müşteriler yüklenemedi");
        } finally {
            setLoading(false);
        }
    };

    const handleSelectCustomer = async (customerId) => {
        if (!customerId) {
            setSelectedCustomer(null);
            return;
        }

        setLoading(true);
        try {
            const customer = await customerService.getCustomerById(customerId);
            setSelectedCustomer(customer);
        } catch (err) {
            console.error("Müşteri detayı yüklenemedi:", err);
            setError("Müşteri detayı yüklenemedi");
        } finally {
            setLoading(false);
        }
    };

    if (error) {
        return (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
                {error}
            </div>
        );
    }

    return (
        <div className="w-full">
            {/* Müşteri Seç */}
            <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">
                    Müşteri Seç
                </label>
                <select
                    className="w-full border border-gray-300 rounded-lg p-2 bg-white text-black"
                    onChange={(e) => handleSelectCustomer(e.target.value)}
                    disabled={loading}
                >
                    <option value="">-- Seçiniz --</option>
                    {customers.map((customer) => (
                        <option key={customer.id} value={customer.id} className="text-black">
                            {customer.companyName}
                        </option>
                    ))}
                </select>
            </div>

            {loading && (
                <div className="text-center py-4">
                    <span className="text-indigo-600">Yükleniyor...</span>
                </div>
            )}

            {selectedCustomer && !loading && (
                <div className="bg-white border rounded-lg shadow-md p-6 space-y-6 text-gray-800">
                    {/* Başlık ve Durum */}
                    <div className="flex justify-between items-start">
                        <h3 className="text-2xl font-bold text-indigo-700">
                            {selectedCustomer.companyName}
                        </h3>
                        <span className={`px-3 py-1 rounded-full text-sm font-medium ${selectedCustomer.status === 'ACTIVE' ? 'bg-green-100 text-green-800' :
                                selectedCustomer.status === 'PASSIVE' ? 'bg-yellow-100 text-yellow-800' :
                                    'bg-red-100 text-red-800'
                            }`}>
                            {selectedCustomer.status}
                        </span>
                    </div>

                    {/* Temel Bilgiler */}
                    <section className="border-b pb-4">
                        <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                            <span className="mr-2">🏢</span> Temel Bilgiler
                        </h4>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div className="bg-gray-50 p-3 rounded">
                                <p className="text-xs text-gray-500 uppercase">Sektör</p>
                                <p className="font-medium">{selectedCustomer.sector || '-'}</p>
                            </div>
                            <div className="bg-gray-50 p-3 rounded">
                                <p className="text-xs text-gray-500 uppercase">Üyelik Paketi</p>
                                <p className="font-medium">{selectedCustomer.membershipPackage || '-'}</p>
                            </div>
                            <div className="bg-gray-50 p-3 rounded md:col-span-2">
                                <p className="text-xs text-gray-500 uppercase">Adres</p>
                                <p className="font-medium">{selectedCustomer.address || '-'}</p>
                            </div>
                        </div>
                    </section>

                    {/* İletişim Kişileri */}
                    {selectedCustomer.contacts && selectedCustomer.contacts.length > 0 && (
                        <section className="border-b pb-4">
                            <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                                <span className="mr-2">👥</span> İletişim Kişileri ({selectedCustomer.contacts.length})
                            </h4>
                            <div className="space-y-3">
                                {selectedCustomer.contacts
                                    .sort((a, b) => a.priority - b.priority)
                                    .map((contact, index) => (
                                        <div key={contact.id || index} className="bg-gray-50 p-4 rounded-lg border-l-4 border-indigo-500">
                                            <div className="flex justify-between items-start mb-2">
                                                <p className="font-semibold text-lg">
                                                    {contact.name} {contact.surname}
                                                </p>
                                                <span className="text-xs bg-indigo-100 text-indigo-700 px-2 py-1 rounded">
                                                    Öncelik: {contact.priority}
                                                </span>
                                            </div>
                                            <div className="grid grid-cols-1 md:grid-cols-2 gap-2 text-sm">
                                                <p className="text-gray-600">
                                                    <span className="font-medium">📧 Email:</span> {contact.email || '-'}
                                                </p>
                                                <p className="text-gray-600">
                                                    <span className="font-medium">📱 Telefon:</span> {contact.phone || '-'}
                                                </p>
                                            </div>
                                        </div>
                                    ))}
                            </div>
                        </section>
                    )}

                    {/* Sosyal Medya */}
                    {selectedCustomer.socialMedia && (
                        <section className="border-b pb-4">
                            <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                                <span className="mr-2">📱</span> Sosyal Medya
                            </h4>
                            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                <div className="bg-gray-50 p-3 rounded">
                                    <p className="text-xs text-gray-500 uppercase">Instagram</p>
                                    <p className="font-medium">{selectedCustomer.socialMedia.instagram || '-'}</p>
                                </div>
                                <div className="bg-gray-50 p-3 rounded">
                                    <p className="text-xs text-gray-500 uppercase">Facebook</p>
                                    <p className="font-medium">{selectedCustomer.socialMedia.facebook || '-'}</p>
                                </div>
                                <div className="bg-gray-50 p-3 rounded">
                                    <p className="text-xs text-gray-500 uppercase">TikTok</p>
                                    <p className="font-medium">{selectedCustomer.socialMedia.tiktok || '-'}</p>
                                </div>
                            </div>
                        </section>
                    )}

                    {/* Hedef Kitle */}
                    {selectedCustomer.targetAudience && (
                        <section className="border-b pb-4">
                            <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                                <span className="mr-2">🎯</span> Hedef Kitle ve İçerik Stratejisi
                            </h4>
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                                <div className="bg-gray-50 p-3 rounded">
                                    <p className="text-xs text-gray-500 uppercase">Post Türü</p>
                                    <p className="font-medium capitalize">{selectedCustomer.targetAudience.postType || '-'}</p>
                                </div>
                                <div className="bg-gray-50 p-3 rounded">
                                    <p className="text-xs text-gray-500 uppercase">Haftalık Post Sıklığı</p>
                                    <p className="font-medium">{selectedCustomer.targetAudience.postFrequency || '-'}</p>
                                </div>
                                <div className="bg-gray-50 p-3 rounded">
                                    <p className="text-xs text-gray-500 uppercase">Post Tonu</p>
                                    <p className="font-medium capitalize">{selectedCustomer.targetAudience.postTone || '-'}</p>
                                </div>
                                <div className="bg-gray-50 p-3 rounded">
                                    <p className="text-xs text-gray-500 uppercase">Hedef Bölge</p>
                                    <p className="font-medium">{selectedCustomer.targetAudience.targetRegion || '-'}</p>
                                </div>
                                <div className="bg-gray-50 p-3 rounded">
                                    <p className="text-xs text-gray-500 uppercase">Hedef Yaş Aralığı</p>
                                    <p className="font-medium">{selectedCustomer.targetAudience.audienceAge || '-'}</p>
                                </div>
                                <div className="bg-gray-50 p-3 rounded">
                                    <p className="text-xs text-gray-500 uppercase">Özel Günler</p>
                                    <p className="font-medium">
                                        {selectedCustomer.targetAudience.specialDates ? '✅ Aktif' : '❌ Pasif'}
                                    </p>
                                </div>
                                {selectedCustomer.targetAudience.customerHashtags && (
                                    <div className="bg-gray-50 p-3 rounded md:col-span-2 lg:col-span-3">
                                        <p className="text-xs text-gray-500 uppercase">Hashtagler</p>
                                        <p className="font-medium text-sm">{selectedCustomer.targetAudience.customerHashtags}</p>
                                    </div>
                                )}
                                {selectedCustomer.targetAudience.audienceInterests && (
                                    <div className="bg-gray-50 p-3 rounded md:col-span-2 lg:col-span-3">
                                        <p className="text-xs text-gray-500 uppercase">İlgi Alanları</p>
                                        <p className="font-medium text-sm">{selectedCustomer.targetAudience.audienceInterests}</p>
                                    </div>
                                )}
                            </div>
                        </section>
                    )}

                    {/* SEO */}
                    {selectedCustomer.seo && (
                        <section className="border-b pb-4">
                            <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                                <span className="mr-2">🔍</span> SEO Bilgileri
                            </h4>
                            <div className="space-y-3">
                                {selectedCustomer.seo.googleConsoleEmail && (
                                    <div className="bg-gray-50 p-3 rounded">
                                        <p className="text-xs text-gray-500 uppercase">Google Console Email</p>
                                        <p className="font-medium">{selectedCustomer.seo.googleConsoleEmail}</p>
                                    </div>
                                )}
                                {selectedCustomer.seo.titleSuggestions && (
                                    <div className="bg-gray-50 p-3 rounded">
                                        <p className="text-xs text-gray-500 uppercase mb-1">SEO Başlık Önerileri</p>
                                        <p className="text-sm text-gray-700">{selectedCustomer.seo.titleSuggestions}</p>
                                    </div>
                                )}
                                {selectedCustomer.seo.contentSuggestions && (
                                    <div className="bg-gray-50 p-3 rounded">
                                        <p className="text-xs text-gray-500 uppercase mb-1">SEO İçerik Önerileri</p>
                                        <p className="text-sm text-gray-700">{selectedCustomer.seo.contentSuggestions}</p>
                                    </div>
                                )}
                            </div>
                        </section>
                    )}

                    {/* API Keys */}
                    {selectedCustomer.apiKeys && (
                        <section className="border-b pb-4">
                            <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                                <span className="mr-2">🔑</span> API Anahtarları
                            </h4>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                {selectedCustomer.apiKeys.instagramApiKey && (
                                    <div className="bg-gray-50 p-3 rounded">
                                        <p className="text-xs text-gray-500 uppercase">Instagram API Key</p>
                                        <p className="font-mono text-xs break-all">{selectedCustomer.apiKeys.instagramApiKey}</p>
                                    </div>
                                )}
                                {selectedCustomer.apiKeys.facebookApiKey && (
                                    <div className="bg-gray-50 p-3 rounded">
                                        <p className="text-xs text-gray-500 uppercase">Facebook API Key</p>
                                        <p className="font-mono text-xs break-all">{selectedCustomer.apiKeys.facebookApiKey}</p>
                                    </div>
                                )}
                                {selectedCustomer.apiKeys.tiktokApiKey && (
                                    <div className="bg-gray-50 p-3 rounded">
                                        <p className="text-xs text-gray-500 uppercase">TikTok API Key</p>
                                        <p className="font-mono text-xs break-all">{selectedCustomer.apiKeys.tiktokApiKey}</p>
                                    </div>
                                )}
                                {selectedCustomer.apiKeys.googleApiKey && (
                                    <div className="bg-gray-50 p-3 rounded">
                                        <p className="text-xs text-gray-500 uppercase">Google API Key</p>
                                        <p className="font-mono text-xs break-all">{selectedCustomer.apiKeys.googleApiKey}</p>
                                    </div>
                                )}
                            </div>
                        </section>
                    )}

                    {/* Medya Dosyaları */}
                    {selectedCustomer.media && selectedCustomer.media.length > 0 && (
                        <section className="border-b pb-4">
                            <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                                <span className="mr-2">🖼️</span> Medya Dosyaları ({selectedCustomer.media.length})
                            </h4>

                            {/* Logolar */}
                            {selectedCustomer.media.filter(m => m.mediaType === 'LOGO').length > 0 && (
                                <div className="mb-6">
                                    <h5 className="text-sm font-semibold text-gray-700 mb-3 uppercase">
                                        Logolar ({selectedCustomer.media.filter(m => m.mediaType === 'LOGO').length})
                                    </h5>
                                    <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
                                        {selectedCustomer.media
                                            .filter(m => m.mediaType === 'LOGO')
                                            .map((media) => (
                                                <div key={media.id} className="border rounded-lg overflow-hidden bg-white shadow-sm hover:shadow-md transition">
                                                    <div className="aspect-square bg-gray-50 flex items-center justify-center p-2">
                                                        <img
                                                            src={media.fullUrl}
                                                            alt={media.originalFileName}
                                                            className="max-w-full max-h-full object-contain"
                                                        />
                                                    </div>
                                                    <div className="p-2 bg-gray-50 border-t">
                                                        <p className="text-xs text-gray-600 truncate" title={media.originalFileName}>
                                                            {media.originalFileName}
                                                        </p>
                                                        <p className="text-xs text-gray-400">
                                                            {(media.fileSize / 1024).toFixed(0)} KB
                                                        </p>
                                                    </div>
                                                </div>
                                            ))}
                                    </div>
                                </div>
                            )}

                            {/* Fotoğraflar */}
                            {selectedCustomer.media.filter(m => m.mediaType === 'PHOTO').length > 0 && (
                                <div className="mb-6">
                                    <h5 className="text-sm font-semibold text-gray-700 mb-3 uppercase">
                                        Fotoğraflar ({selectedCustomer.media.filter(m => m.mediaType === 'PHOTO').length})
                                    </h5>
                                    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                                        {selectedCustomer.media
                                            .filter(m => m.mediaType === 'PHOTO')
                                            .map((media) => (
                                                <div key={media.id} className="border rounded-lg overflow-hidden bg-white shadow-sm hover:shadow-md transition">
                                                    <div className="aspect-video bg-gray-100">
                                                        <img
                                                            src={media.fullUrl}
                                                            alt={media.originalFileName}
                                                            className="w-full h-full object-cover"
                                                        />
                                                    </div>
                                                    <div className="p-2 bg-gray-50 border-t">
                                                        <p className="text-xs text-gray-600 truncate" title={media.originalFileName}>
                                                            {media.originalFileName}
                                                        </p>
                                                        <p className="text-xs text-gray-400">
                                                            {(media.fileSize / 1024).toFixed(0)} KB
                                                        </p>
                                                    </div>
                                                </div>
                                            ))}
                                    </div>
                                </div>
                            )}

                            {/* Videolar */}
                            {selectedCustomer.media.filter(m => m.mediaType === 'VIDEO').length > 0 && (
                                <div className="mb-6">
                                    <h5 className="text-sm font-semibold text-gray-700 mb-3 uppercase">
                                        Videolar ({selectedCustomer.media.filter(m => m.mediaType === 'VIDEO').length})
                                    </h5>
                                    <div className="space-y-2">
                                        {selectedCustomer.media
                                            .filter(m => m.mediaType === 'VIDEO')
                                            .map((media) => (
                                                <div key={media.id} className="flex items-center justify-between p-3 bg-gray-50 border rounded-lg hover:bg-gray-100 transition">
                                                    <div className="flex-1">
                                                        <p className="text-sm font-medium text-gray-800">{media.originalFileName}</p>
                                                        <p className="text-xs text-gray-500">
                                                            {(media.fileSize / 1024 / 1024).toFixed(2)} MB
                                                        </p>
                                                    </div>
                                                    <a
                                                        href={media.fullUrl}
                                                        target="_blank"
                                                        rel="noopener noreferrer"
                                                        className="ml-4 bg-indigo-600 text-white px-4 py-2 rounded text-sm hover:bg-indigo-700 transition" >

                                                        ▶️ İzle
                                                    </a>
                                                </div>
                                            ))}
                                    </div>
                                </div>
                            )}

                            {/* Dökümanlar */}
                            {selectedCustomer.media.filter(m => m.mediaType === 'DOCUMENT').length > 0 && (
                                <div>
                                    <h5 className="text-sm font-semibold text-gray-700 mb-3 uppercase">
                                        Dökümanlar ({selectedCustomer.media.filter(m => m.mediaType === 'DOCUMENT').length})
                                    </h5>
                                    <div className="space-y-2">
                                        {selectedCustomer.media
                                            .filter(m => m.mediaType === 'DOCUMENT')
                                            .map((media) => (
                                                <div key={media.id} className="flex items-center justify-between p-3 bg-gray-50 border rounded-lg hover:bg-gray-100 transition">
                                                    <div className="flex-1">
                                                        <p className="text-sm font-medium text-gray-800">{media.originalFileName}</p>
                                                        <p className="text-xs text-gray-500">
                                                            {(media.fileSize / 1024).toFixed(0)} KB
                                                        </p>
                                                    </div>
                                                    <a
                                                        href={media.fullUrl}
                                                        target="_blank"
                                                        rel="noopener noreferrer"
                                                        className="ml-4 bg-gray-600 text-white px-4 py-2 rounded text-sm hover:bg-gray-700 transition" >

                                                        📥 İndir
                                                    </a>
                                                </div>
                                            ))}
                                    </div>
                                </div>
                            )}
                        </section>
                    )}

                    {/* Tarih Bilgileri */}
                    <section className="pt-4 border-t bg-gray-50 -mx-6 -mb-6 px-6 py-4 rounded-b-lg">
                        <h4 className="text-sm font-semibold text-gray-600 mb-2 uppercase">Sistem Bilgileri</h4>
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                            <div>
                                <p className="text-xs text-gray-500">Müşteri ID</p>
                                <p className="font-mono font-medium">#{selectedCustomer.id}</p>
                            </div>
                            <div>
                                <p className="text-xs text-gray-500">Oluşturulma</p>
                                <p className="font-medium">
                                    {new Date(selectedCustomer.createdAt).toLocaleString('tr-TR')}
                                </p>
                            </div>
                            <div>
                                <p className="text-xs text-gray-500">Son Güncelleme</p>
                                <p className="font-medium">
                                    {new Date(selectedCustomer.updatedAt).toLocaleString('tr-TR')}
                                </p>
                            </div>
                        </div>
                    </section>
                </div>
            )}
        </div>
    );
}
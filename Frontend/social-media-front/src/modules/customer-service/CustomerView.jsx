// src/components/customerComponents/CustomerView.jsx
import { useState, useEffect } from "react";
import customerService from "./services/customerService";
import MediaGallery from "./customerMediaComponents/MediaGallery";
 
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

  // Medya güncellendiğinde müşteriyi yeniden yükle
  const handleMediaUpdate = async () => {
    if (selectedCustomer) {
      await handleSelectCustomer(selectedCustomer.id);
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
          className="w-full border border-gray-300 rounded-lg p-2 bg-white text-gray-900"
          onChange={(e) => handleSelectCustomer(e.target.value)}
          disabled={loading}
        >
          <option value="">-- Seçiniz --</option>
          {customers.map((customer) => (
            <option key={customer.id} value={customer.id} className="text-gray-900">
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
        <div className="space-y-6">
          {/* Başlık ve Durum */}
          <div className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
            <div className="flex justify-between items-start">
              <h3 className="text-2xl font-bold text-indigo-700">
                {selectedCustomer.companyName}
              </h3>
              <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                selectedCustomer.status === 'ACTIVE' ? 'bg-green-100 text-green-800' :
                selectedCustomer.status === 'PASSIVE' ? 'bg-yellow-100 text-yellow-800' :
                'bg-red-100 text-red-800'
              }`}>
                {selectedCustomer.status}
              </span>
            </div>
          </div>

          {/* Temel Bilgiler */}
          <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
            <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
              <span className="mr-2">🏢</span> Temel Bilgiler
            </h4>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="bg-gray-50 p-3 rounded">
                <p className="text-xs text-gray-500 uppercase">Sektör</p>
                <p className="font-medium text-gray-800">{selectedCustomer.sector || '-'}</p>
              </div>
              <div className="bg-gray-50 p-3 rounded">
                <p className="text-xs text-gray-500 uppercase">Üyelik Paketi</p>
                <p className="font-medium text-gray-800">{selectedCustomer.membershipPackage || '-'}</p>
              </div>
              <div className="bg-gray-50 p-3 rounded md:col-span-2">
                <p className="text-xs text-gray-500 uppercase">Adres</p>
                <p className="font-medium text-gray-800">{selectedCustomer.address || '-'}</p>
              </div>
            </div>
          </section>

          {/* İletişim Kişileri */}
          {selectedCustomer.contacts && selectedCustomer.contacts.length > 0 && (
            <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
              <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                <span className="mr-2">👥</span> İletişim Kişileri ({selectedCustomer.contacts.length})
              </h4>
              <div className="space-y-3">
                {selectedCustomer.contacts
                  .sort((a, b) => a.priority - b.priority)
                  .map((contact, index) => (
                    <div key={contact.id || index} className="bg-gray-50 p-4 rounded-lg border-l-4 border-indigo-500">
                      <div className="flex justify-between items-start mb-2">
                        <p className="font-semibold text-lg text-gray-800">
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
            <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
              <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                <span className="mr-2">📱</span> Sosyal Medya
              </h4>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="bg-gray-50 p-3 rounded">
                  <p className="text-xs text-gray-500 uppercase">Instagram</p>
                  <p className="font-medium text-gray-800">{selectedCustomer.socialMedia.instagram || '-'}</p>
                </div>
                <div className="bg-gray-50 p-3 rounded">
                  <p className="text-xs text-gray-500 uppercase">Facebook</p>
                  <p className="font-medium text-gray-800">{selectedCustomer.socialMedia.facebook || '-'}</p>
                </div>
                <div className="bg-gray-50 p-3 rounded">
                  <p className="text-xs text-gray-500 uppercase">TikTok</p>
                  <p className="font-medium text-gray-800">{selectedCustomer.socialMedia.tiktok || '-'}</p>
                </div>
              </div>
            </section>
          )}

          {/* Hedef Kitle */}
          {selectedCustomer.targetAudience && (
            <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
              <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                <span className="mr-2">🎯</span> Hedef Kitle ve İçerik Stratejisi
              </h4>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <div className="bg-gray-50 p-3 rounded">
                  <p className="text-xs text-gray-500 uppercase">Post Türü</p>
                  <p className="font-medium text-gray-800 capitalize">{selectedCustomer.targetAudience.postType || '-'}</p>
                </div>
                <div className="bg-gray-50 p-3 rounded">
                  <p className="text-xs text-gray-500 uppercase">Haftalık Post Sıklığı</p>
                  <p className="font-medium text-gray-800">{selectedCustomer.targetAudience.postFrequency || '-'}</p>
                </div>
                <div className="bg-gray-50 p-3 rounded">
                  <p className="text-xs text-gray-500 uppercase">Post Tonu</p>
                  <p className="font-medium text-gray-800 capitalize">{selectedCustomer.targetAudience.postTone || '-'}</p>
                </div>
                <div className="bg-gray-50 p-3 rounded">
                  <p className="text-xs text-gray-500 uppercase">Hedef Bölge</p>
                  <p className="font-medium text-gray-800">{selectedCustomer.targetAudience.targetRegion || '-'}</p>
                </div>
                <div className="bg-gray-50 p-3 rounded">
                  <p className="text-xs text-gray-500 uppercase">Hedef Yaş Aralığı</p>
                  <p className="font-medium text-gray-800">{selectedCustomer.targetAudience.audienceAge || '-'}</p>
                </div>
                <div className="bg-gray-50 p-3 rounded">
                  <p className="text-xs text-gray-500 uppercase">Özel Günler</p>
                  <p className="font-medium text-gray-800">
                    {selectedCustomer.targetAudience.specialDates ? '✅ Aktif' : '❌ Pasif'}
                  </p>
                </div>
                {selectedCustomer.targetAudience.customerHashtags && (
                  <div className="bg-gray-50 p-3 rounded md:col-span-2 lg:col-span-3">
                    <p className="text-xs text-gray-500 uppercase">Hashtagler</p>
                    <p className="font-medium text-sm text-gray-800">{selectedCustomer.targetAudience.customerHashtags}</p>
                  </div>
                )}
                {selectedCustomer.targetAudience.audienceInterests && (
                  <div className="bg-gray-50 p-3 rounded md:col-span-2 lg:col-span-3">
                    <p className="text-xs text-gray-500 uppercase">İlgi Alanları</p>
                    <p className="font-medium text-sm text-gray-800">{selectedCustomer.targetAudience.audienceInterests}</p>
                  </div>
                )}
              </div>
            </section>
          )}

          {/* SEO */}
          {selectedCustomer.seo && (
            <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
              <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                <span className="mr-2">🔍</span> SEO Bilgileri
              </h4>
              <div className="space-y-3">
                {selectedCustomer.seo.googleConsoleEmail && (
                  <div className="bg-gray-50 p-3 rounded">
                    <p className="text-xs text-gray-500 uppercase">Google Console Email</p>
                    <p className="font-medium text-gray-800">{selectedCustomer.seo.googleConsoleEmail}</p>
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
            <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
              <h4 className="text-lg font-semibold text-indigo-600 mb-3 flex items-center">
                <span className="mr-2">🔑</span> API Anahtarları
              </h4>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {selectedCustomer.apiKeys.instagramApiKey && (
                  <div className="bg-gray-50 p-3 rounded">
                    <p className="text-xs text-gray-500 uppercase">Instagram API Key</p>
                    <p className="font-mono text-xs break-all text-gray-700">{selectedCustomer.apiKeys.instagramApiKey}</p>
                  </div>
                )}
                {selectedCustomer.apiKeys.facebookApiKey && (
                  <div className="bg-gray-50 p-3 rounded">
                    <p className="text-xs text-gray-500 uppercase">Facebook API Key</p>
                    <p className="font-mono text-xs break-all text-gray-700">{selectedCustomer.apiKeys.facebookApiKey}</p>
                  </div>
                )}
                {selectedCustomer.apiKeys.tiktokApiKey && (
                  <div className="bg-gray-50 p-3 rounded">
                    <p className="text-xs text-gray-500 uppercase">TikTok API Key</p>
                    <p className="font-mono text-xs break-all text-gray-700">{selectedCustomer.apiKeys.tiktokApiKey}</p>
                  </div>
                )}
                {selectedCustomer.apiKeys.googleApiKey && (
                  <div className="bg-gray-50 p-3 rounded">
                    <p className="text-xs text-gray-500 uppercase">Google API Key</p>
                    <p className="font-mono text-xs break-all text-gray-700">{selectedCustomer.apiKeys.googleApiKey}</p>
                  </div>
                )}
              </div>
            </section>
          )}

          {/* Medya Galerisi */}
          {selectedCustomer.media && selectedCustomer.media.length > 0 && (
            <MediaGallery 
              media={selectedCustomer.media}
              customerId={selectedCustomer.id}
              onMediaUpdate={handleMediaUpdate}
            />
          )}

          {/* Tarih Bilgileri */}
          <section className="bg-gray-50 rounded-lg p-4 border border-gray-200">
            <h4 className="text-sm font-semibold text-gray-600 mb-2 uppercase">Sistem Bilgileri</h4>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
              <div>
                <p className="text-xs text-gray-500">Müşteri ID</p>
                <p className="font-mono font-medium text-gray-800">#{selectedCustomer.id}</p>
              </div>
              <div>
                <p className="text-xs text-gray-500">Oluşturulma</p>
                <p className="font-medium text-gray-800">
                  {new Date(selectedCustomer.createdAt).toLocaleString('tr-TR')}
                </p>
              </div>
              <div>
                <p className="text-xs text-gray-500">Son Güncelleme</p>
                <p className="font-medium text-gray-800">
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
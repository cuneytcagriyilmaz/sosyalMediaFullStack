// src/components/customerComponents/CustomerAddForm.jsx
import { useState } from "react";
import customerService from "./services/customerService";

export default function CustomerAddForm() {
  const [formData, setFormData] = useState({
    companyName: "",
    sector: "",
    address: "",
    membershipPackage: "",
    status: "ACTIVE",
    
    targetAudience: {
      specialDates: false,
      targetRegion: "",
      customerHashtags: "",
      postType: "gorsel",
      postFrequency: "5",
      postTone: "samimi",
      audienceAge: "",
      audienceInterests: ""
    },
    
    contacts: [
      { name: "", surname: "", email: "", phone: "", priority: 1 }
    ],
    
    socialMedia: {
      instagram: "",
      facebook: "",
      tiktok: ""
    },
    
    seo: {
      googleConsoleEmail: "",
      titleSuggestions: "",
      contentSuggestions: ""
    },
    
    apiKeys: {
      instagramApiKey: "",
      facebookApiKey: "",
      tiktokApiKey: "",
      googleApiKey: ""
    }
  });

  const [logoFiles, setLogoFiles] = useState([]);
  const [photoFiles, setPhotoFiles] = useState([]);
  const [videoFiles, setVideoFiles] = useState([]);
  const [documentFiles, setDocumentFiles] = useState([]);
  const [loading, setLoading] = useState(false);

  const addContact = () => {
    setFormData({
      ...formData,
      contacts: [
        ...formData.contacts,
        { name: "", surname: "", email: "", phone: "", priority: formData.contacts.length + 1 }
      ]
    });
  };

  const removeContact = (index) => {
    const newContacts = formData.contacts.filter((_, i) => i !== index);
    setFormData({ ...formData, contacts: newContacts });
  };

  const updateContact = (index, field, value) => {
    const newContacts = [...formData.contacts];
    newContacts[index][field] = value;
    setFormData({ ...formData, contacts: newContacts });
  };

  const updateNested = (parent, field, value) => {
    setFormData({
      ...formData,
      [parent]: {
        ...formData[parent],
        [field]: value
      }
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      // 1. Müşteri oluştur
      const customer = await customerService.createCustomer(formData);
      console.log("Müşteri oluşturuldu:", customer);

      // 2. Logo yükle
      if (logoFiles.length > 0) {
        await customerService.uploadMultipleMedia(customer.id, logoFiles, 'LOGO');
        console.log("Logolar yüklendi");
      }

      // 3. Fotoğraf yükle
      if (photoFiles.length > 0) {
        await customerService.uploadMultipleMedia(customer.id, photoFiles, 'PHOTO');
        console.log("Fotoğraflar yüklendi");
      }

      // 4. Video yükle
      if (videoFiles.length > 0) {
        await customerService.uploadMultipleMedia(customer.id, videoFiles, 'VIDEO');
        console.log("Videolar yüklendi");
      }

      // 5. Döküman yükle
      if (documentFiles.length > 0) {
        await customerService.uploadMultipleMedia(customer.id, documentFiles, 'DOCUMENT');
        console.log("Dökümanlar yüklendi");
      }

      alert("✅ Müşteri başarıyla eklendi!");
      
      // Form'u sıfırla
      window.location.reload();

    } catch (error) {
      console.error("Hata:", error);
      alert("❌ Hata: " + (error.response?.data?.message || error.message));
    } finally {
      setLoading(false);
    }
  };

  const inputClass = "w-full p-2 border border-gray-300 rounded-lg bg-white text-gray-900 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500";
  const labelClass = "block text-sm font-medium text-gray-700 mb-1";

  return (
    <div className="max-w-6xl mx-auto p-6 bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl shadow-lg">
      <div className="bg-white rounded-lg p-6">
        <h2 className="text-3xl font-bold text-indigo-700 mb-6 flex items-center">
          <span className="mr-3">👥</span> Yeni Müşteri Ekle
        </h2>
        
        <form onSubmit={handleSubmit} className="space-y-8">
          {/* Temel Bilgiler */}
          <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
            <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
              <span className="mr-2">🏢</span> Temel Bilgiler
            </h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className={labelClass}>
                  Şirket Adı <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  value={formData.companyName}
                  onChange={(e) => setFormData({ ...formData, companyName: e.target.value })}
                  required
                  className={inputClass}
                  placeholder="Örn: Kahve Dünyası"
                />
              </div>

              <div>
                <label className={labelClass}>
                  Sektör <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  value={formData.sector}
                  onChange={(e) => setFormData({ ...formData, sector: e.target.value })}
                  required
                  className={inputClass}
                  placeholder="Örn: Cafe, Restoran, Mağaza"
                />
              </div>

              <div className="md:col-span-2">
                <label className={labelClass}>
                  Adres <span className="text-red-500">*</span>
                </label>
                <textarea
                  value={formData.address}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                  required
                  className={inputClass}
                  rows="2"
                  placeholder="Tam adres"
                />
              </div>

              <div>
                <label className={labelClass}>
                  Üyelik Paketi <span className="text-red-500">*</span>
                </label>
                <select
                  value={formData.membershipPackage}
                  onChange={(e) => setFormData({ ...formData, membershipPackage: e.target.value })}
                  required
                  className={inputClass}
                >
                  <option value="">Seçiniz</option>
                  <option value="Basic">Basic - Temel Paket</option>
                  <option value="Gold">Gold - Altın Paket</option>
                  <option value="Platinum">Platinum - Platin Paket</option>
                  <option value="Premium">Premium - Premium Paket</option>
                </select>
              </div>

              <div>
                <label className={labelClass}>
                  Durum <span className="text-red-500">*</span>
                </label>
                <select
                  value={formData.status}
                  onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                  required
                  className={inputClass}
                >
                  <option value="ACTIVE">Aktif</option>
                  <option value="PASSIVE">Pasif</option>
                  <option value="CANCELLED">İptal</option>
                </select>
              </div>
            </div>
          </section>

          {/* İletişim Kişileri */}
          <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-xl font-semibold text-gray-800 flex items-center">
                <span className="mr-2">📞</span> İletişim Kişileri
              </h3>
              <button
                type="button"
                onClick={addContact}
                className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition text-sm font-medium"
              >
                + Yeni Kişi Ekle
              </button>
            </div>

            {formData.contacts.map((contact, index) => (
              <div key={index} className="bg-white p-4 rounded-lg border border-gray-300 mb-4">
                <div className="flex justify-between items-center mb-3">
                  <p className="font-medium text-gray-700">Yetkili #{index + 1}</p>
                  {index > 0 && (
                    <button
                      type="button"
                      onClick={() => removeContact(index)}
                      className="bg-red-500 text-white px-3 py-1 rounded text-sm hover:bg-red-600"
                    >
                      🗑️ Sil
                    </button>
                  )}
                </div>
                
                <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                  <div>
                    <label className={labelClass}>Ad</label>
                    <input
                      type="text"
                      value={contact.name}
                      onChange={(e) => updateContact(index, 'name', e.target.value)}
                      required
                      className={inputClass}
                      placeholder="Adı"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>Soyad</label>
                    <input
                      type="text"
                      value={contact.surname}
                      onChange={(e) => updateContact(index, 'surname', e.target.value)}
                      required
                      className={inputClass}
                      placeholder="Soyadı"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>Email</label>
                    <input
                      type="email"
                      value={contact.email}
                      onChange={(e) => updateContact(index, 'email', e.target.value)}
                      required
                      className={inputClass}
                      placeholder="email@example.com"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>Telefon</label>
                    <input
                      type="text"
                      value={contact.phone}
                      onChange={(e) => updateContact(index, 'phone', e.target.value)}
                      required
                      className={inputClass}
                      placeholder="5XX XXX XX XX"
                    />
                  </div>
                </div>
              </div>
            ))}
          </section>

          {/* Sosyal Medya */}
          <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
            <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
              <span className="mr-2">📱</span> Sosyal Medya Hesapları
            </h3>
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label className={labelClass}>Instagram</label>
                <input
                  type="text"
                  value={formData.socialMedia.instagram}
                  onChange={(e) => updateNested('socialMedia', 'instagram', e.target.value)}
                  className={inputClass}
                  placeholder="@kullaniciadi"
                />
              </div>

              <div>
                <label className={labelClass}>Facebook</label>
                <input
                  type="text"
                  value={formData.socialMedia.facebook}
                  onChange={(e) => updateNested('socialMedia', 'facebook', e.target.value)}
                  className={inputClass}
                  placeholder="Sayfa adı"
                />
              </div>

              <div>
                <label className={labelClass}>TikTok</label>
                <input
                  type="text"
                  value={formData.socialMedia.tiktok}
                  onChange={(e) => updateNested('socialMedia', 'tiktok', e.target.value)}
                  className={inputClass}
                  placeholder="@kullaniciadi"
                />
              </div>
            </div>
          </section>

          {/* Hedef Kitle ve İçerik Stratejisi */}
          <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
            <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
              <span className="mr-2">🎯</span> Hedef Kitle ve İçerik Stratejisi
            </h3>
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
              <div>
                <label className={labelClass}>
                  Post Türü <span className="text-red-500">*</span>
                </label>
                <select
                  value={formData.targetAudience.postType}
                  onChange={(e) => updateNested('targetAudience', 'postType', e.target.value)}
                  required
                  className={inputClass}
                >
                  <option value="gorsel">📸 Görsel</option>
                  <option value="video">🎥 Video</option>
                  <option value="story">📱 Story</option>
                </select>
              </div>

              <div>
                <label className={labelClass}>
                  Haftalık Post Sıklığı <span className="text-red-500">*</span>
                </label>
                <input
                  type="number"
                  min="1"
                  max="7"
                  value={formData.targetAudience.postFrequency}
                  onChange={(e) => updateNested('targetAudience', 'postFrequency', e.target.value)}
                  required
                  className={inputClass}
                  placeholder="1-7 arası"
                />
              </div>

              <div>
                <label className={labelClass}>
                  Post Tonu <span className="text-red-500">*</span>
                </label>
                <select
                  value={formData.targetAudience.postTone}
                  onChange={(e) => updateNested('targetAudience', 'postTone', e.target.value)}
                  required
                  className={inputClass}
                >
                  <option value="samimi">😊 Samimi</option>
                  <option value="resmi">👔 Resmi</option>
                  <option value="mizahi">😄 Mizahi</option>
                  <option value="ciddi">🎩 Ciddi</option>
                </select>
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className={labelClass}>Hedef Bölge</label>
                <input
                  type="text"
                  value={formData.targetAudience.targetRegion}
                  onChange={(e) => updateNested('targetAudience', 'targetRegion', e.target.value)}
                  className={inputClass}
                  placeholder="Örn: Antalya, Lara, Muratpaşa"
                />
              </div>

              <div>
                <label className={labelClass}>Hedef Yaş Aralığı</label>
                <input
                  type="text"
                  value={formData.targetAudience.audienceAge}
                  onChange={(e) => updateNested('targetAudience', 'audienceAge', e.target.value)}
                  className={inputClass}
                  placeholder="Örn: 25-45"
                />
              </div>

              <div className="md:col-span-2">
                <label className={labelClass}>Hashtagler</label>
                <input
                  type="text"
                  value={formData.targetAudience.customerHashtags}
                  onChange={(e) => updateNested('targetAudience', 'customerHashtags', e.target.value)}
                  className={inputClass}
                  placeholder="#kahve #antalya #cafe #lara"
                />
              </div>

              <div className="md:col-span-2">
                <label className={labelClass}>Hedef Kitle İlgi Alanları</label>
                <input
                  type="text"
                  value={formData.targetAudience.audienceInterests}
                  onChange={(e) => updateNested('targetAudience', 'audienceInterests', e.target.value)}
                  className={inputClass}
                  placeholder="Örn: Kahve, Deniz, Fotograf, Sosyalleşme"
                />
              </div>
            </div>

            <div className="mt-4">
              <label className="flex items-center space-x-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={formData.targetAudience.specialDates}
                  onChange={(e) => updateNested('targetAudience', 'specialDates', e.target.checked)}
                  className="w-5 h-5 text-indigo-600 rounded"
                />
                <span className="text-gray-700 font-medium">🎉 Özel günlerde otomatik post yapılsın (Bayramlar, Anneler Günü vb.)</span>
              </label>
            </div>
          </section>

          {/* SEO Bilgileri */}
          <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
            <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
              <span className="mr-2">🔍</span> SEO Bilgileri
            </h3>
            
            <div className="space-y-4">
              <div>
                <label className={labelClass}>Google Search Console Email</label>
                <input
                  type="email"
                  value={formData.seo.googleConsoleEmail}
                  onChange={(e) => updateNested('seo', 'googleConsoleEmail', e.target.value)}
                  className={inputClass}
                  placeholder="console@example.com"
                />
              </div>

              <div>
                <label className={labelClass}>SEO Başlık Önerileri</label>
                <textarea
                  value={formData.seo.titleSuggestions}
                  onChange={(e) => updateNested('seo', 'titleSuggestions', e.target.value)}
                  className={inputClass}
                  rows="2"
                  placeholder="Örn: Antalya'nın En İyi Cafe'si - Kahve Dünyası"
                />
              </div>

              <div>
                <label className={labelClass}>SEO İçerik Önerileri</label>
                <textarea
                  value={formData.seo.contentSuggestions}
                  onChange={(e) => updateNested('seo', 'contentSuggestions', e.target.value)}
                  className={inputClass}
                  rows="3"
                  placeholder="SEO için içerik açıklaması..."
                />
              </div>
            </div>
          </section>

          {/* API Anahtarları */}
          <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
            <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
              <span className="mr-2">🔑</span> API Anahtarları
            </h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className={labelClass}>Instagram API Key</label>
                <input
                  type="text"
                  value={formData.apiKeys.instagramApiKey}
                  onChange={(e) => updateNested('apiKeys', 'instagramApiKey', e.target.value)}
                  className={inputClass}
                  placeholder="Instagram API anahtarı"
                />
              </div>

              <div>
                <label className={labelClass}>Facebook API Key</label>
                <input
                  type="text"
                  value={formData.apiKeys.facebookApiKey}
                  onChange={(e) => updateNested('apiKeys', 'facebookApiKey', e.target.value)}
                  className={inputClass}
                  placeholder="Facebook API anahtarı"
                />
              </div>

              <div>
                <label className={labelClass}>TikTok API Key</label>
                <input
                  type="text"
                  value={formData.apiKeys.tiktokApiKey}
                  onChange={(e) => updateNested('apiKeys', 'tiktokApiKey', e.target.value)}
                  className={inputClass}
                  placeholder="TikTok API anahtarı"
                />
              </div>

              <div>
                <label className={labelClass}>Google API Key</label>
                <input
                  type="text"
                  value={formData.apiKeys.googleApiKey}
                  onChange={(e) => updateNested('apiKeys', 'googleApiKey', e.target.value)}
                  className={inputClass}
                  placeholder="Google API anahtarı"
                />
              </div>
            </div>
          </section>

          {/* Medya Yükleme */}
          <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
            <h3 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
              <span className="mr-2">🖼️</span> Medya Dosyaları
            </h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className={labelClass}>Logo(lar)</label>
                <input
                  type="file"
                  multiple
                  accept="image/*"
                  onChange={(e) => setLogoFiles(Array.from(e.target.files))}
                  className={inputClass}
                />
                {logoFiles.length > 0 && (
                  <p className="text-sm text-green-600 mt-1 font-medium">✅ {logoFiles.length} dosya seçildi</p>
                )}
              </div>

              <div>
                <label className={labelClass}>Fotoğraf(lar)</label>
                <input
                  type="file"
                  multiple
                  accept="image/*"
                  onChange={(e) => setPhotoFiles(Array.from(e.target.files))}
                  className={inputClass}
                />
                {photoFiles.length > 0 && (
                  <p className="text-sm text-green-600 mt-1 font-medium">✅ {photoFiles.length} dosya seçildi</p>
                )}
              </div>

              <div>
                <label className={labelClass}>Video(lar)</label>
                <input
                  type="file"
                  multiple
                  accept="video/*"
                  onChange={(e) => setVideoFiles(Array.from(e.target.files))}
                  className={inputClass}
                />
                {videoFiles.length > 0 && (
                  <p className="text-sm text-green-600 mt-1 font-medium">✅ {videoFiles.length} dosya seçildi</p>
                )}
              </div>

              <div>
                <label className={labelClass}>Döküman(lar)</label>
                <input
                  type="file"
                  multiple
                  accept=".pdf,.doc,.docx,.xls,.xlsx"
                  onChange={(e) => setDocumentFiles(Array.from(e.target.files))}
                  className={inputClass}
                />
                {documentFiles.length > 0 && (
                  <p className="text-sm text-green-600 mt-1 font-medium">✅ {documentFiles.length} dosya seçildi</p>
                )}
              </div>
            </div>
          </section>

          {/* Submit Button */}
          <div className="flex justify-end space-x-4 pt-6 border-t">
            <button
              type="button"
              onClick={() => window.location.reload()}
              disabled={loading}
              className="px-6 py-3 bg-gray-300 text-gray-700 rounded-lg font-medium hover:bg-gray-400 transition disabled:opacity-50"
            >
              ↩️ İptal
            </button>
            <button
              type="submit"
              disabled={loading}
              className="px-8 py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition disabled:bg-gray-400 disabled:cursor-not-allowed flex items-center space-x-2"
            >
              {loading ? (
                <>
                  <span className="animate-spin">⏳</span>
                  <span>Kaydediliyor...</span>
                </>
              ) : (
                <>
                  <span>✅</span>
                  <span>Müşteriyi Kaydet</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
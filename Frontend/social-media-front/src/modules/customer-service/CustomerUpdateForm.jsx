// src/components/customerComponents/CustomerUpdateForm.jsx
import { useState, useEffect } from "react";
import customerService from "./services/customerService";
import MediaGallery from "./customerMediaComponents/MediaGallery";

export default function CustomerUpdateForm() {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState("");
  const [formData, setFormData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Her section için ayrı loading state'leri
  const [loadingBasicInfo, setLoadingBasicInfo] = useState(false);
  const [loadingContacts, setLoadingContacts] = useState(false);
  const [loadingSocialMedia, setLoadingSocialMedia] = useState(false);
  const [loadingTargetAudience, setLoadingTargetAudience] = useState(false);
  const [loadingSeo, setLoadingSeo] = useState(false);
  const [loadingApiKeys, setLoadingApiKeys] = useState(false);

  // Yeni medya yükleme için state'ler
  const [logoFiles, setLogoFiles] = useState([]);
  const [photoFiles, setPhotoFiles] = useState([]);
  const [videoFiles, setVideoFiles] = useState([]);
  const [documentFiles, setDocumentFiles] = useState([]);
  const [uploadingMedia, setUploadingMedia] = useState(false);

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (err) {
      console.error("Müşteriler yüklenemedi:", err);
      setError("Müşteriler yüklenemedi");
    }
  };

  const handleSelectCustomer = async (customerId) => {
    if (!customerId) {
      setSelectedCustomerId("");
      setFormData(null);
      return;
    }

    setSelectedCustomerId(customerId);
    setLoading(true);

    try {
      const customer = await customerService.getCustomerById(customerId);
      setFormData(customer);
    } catch (err) {
      console.error("Müşteri detayı yüklenemedi:", err);
      setError("Müşteri detayı yüklenemedi");
    } finally {
      setLoading(false);
    }
  };

  // ========== CONTACT İŞLEMLERİ ==========

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

  // ========== NESTED ALAN GÜNCELLEME ==========

  const updateNested = (parent, field, value) => {
    setFormData({
      ...formData,
      [parent]: {
        ...formData[parent],
        [field]: value
      }
    });
  };

  // ========== BÖLÜM BAZLI KAYDETME FONKSİYONLARI ==========

  const handleSaveBasicInfo = async () => {
    setLoadingBasicInfo(true);
    try {
      const data = {
        companyName: formData.companyName,
        sector: formData.sector,
        address: formData.address,
        membershipPackage: formData.membershipPackage,
        status: formData.status
      };

      await customerService.updateBasicInfo(selectedCustomerId, data);
      alert("✅ Temel bilgiler başarıyla güncellendi!");
      
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      alert("❌ Hata: " + (err.response?.data?.message || err.message));
    } finally {
      setLoadingBasicInfo(false);
    }
  };

  const handleSaveContacts = async () => {
    setLoadingContacts(true);
    try {
      await customerService.updateContacts(selectedCustomerId, formData.contacts);
      alert("✅ İletişim kişileri başarıyla güncellendi!");
      
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      const errorMsg = err.response?.data?.message || err.message;
      
      if (errorMsg.includes("Duplicate")) {
        alert("❌ Hata: Aynı email veya telefon numarası birden fazla kişide kullanılamaz!\n\n" + errorMsg);
      } else {
        alert("❌ Hata: " + errorMsg);
      }
    } finally {
      setLoadingContacts(false);
    }
  };

  const handleSaveSocialMedia = async () => {
    setLoadingSocialMedia(true);
    try {
      await customerService.updateSocialMedia(selectedCustomerId, formData.socialMedia);
      alert("✅ Sosyal medya bilgileri başarıyla güncellendi!");
      
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      alert("❌ Hata: " + (err.response?.data?.message || err.message));
    } finally {
      setLoadingSocialMedia(false);
    }
  };

  const handleSaveTargetAudience = async () => {
    setLoadingTargetAudience(true);
    try {
      await customerService.updateTargetAudience(selectedCustomerId, formData.targetAudience);
      alert("✅ Hedef kitle bilgileri başarıyla güncellendi!");
      
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      alert("❌ Hata: " + (err.response?.data?.message || err.message));
    } finally {
      setLoadingTargetAudience(false);
    }
  };

  const handleSaveSeo = async () => {
    setLoadingSeo(true);
    try {
      await customerService.updateSeo(selectedCustomerId, formData.seo);
      alert("✅ SEO bilgileri başarıyla güncellendi!");
      
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      alert("❌ Hata: " + (err.response?.data?.message || err.message));
    } finally {
      setLoadingSeo(false);
    }
  };

  const handleSaveApiKeys = async () => {
    setLoadingApiKeys(true);
    try {
      await customerService.updateApiKeys(selectedCustomerId, formData.apiKeys);
      alert("✅ API anahtarları başarıyla güncellendi!");
      
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      alert("❌ Hata: " + (err.response?.data?.message || err.message));
    } finally {
      setLoadingApiKeys(false);
    }
  };

  // ========== MEDYA İŞLEMLERİ ==========

  const handleMediaUpdate = async () => {
    if (selectedCustomerId) {
      await handleSelectCustomer(selectedCustomerId);
    }
  };

  const handleUploadNewMedia = async () => {
    if (logoFiles.length === 0 && photoFiles.length === 0 && 
        videoFiles.length === 0 && documentFiles.length === 0) {
      alert("❌ Lütfen en az bir dosya seçin!");
      return;
    }

    setUploadingMedia(true);

    try {
      if (logoFiles.length > 0) {
        await customerService.uploadMultipleMedia(selectedCustomerId, logoFiles, 'LOGO');
      }
      if (photoFiles.length > 0) {
        await customerService.uploadMultipleMedia(selectedCustomerId, photoFiles, 'PHOTO');
      }
      if (videoFiles.length > 0) {
        await customerService.uploadMultipleMedia(selectedCustomerId, videoFiles, 'VIDEO');
      }
      if (documentFiles.length > 0) {
        await customerService.uploadMultipleMedia(selectedCustomerId, documentFiles, 'DOCUMENT');
      }

      alert("✅ Medya dosyaları başarıyla yüklendi!");

      setLogoFiles([]);
      setPhotoFiles([]);
      setVideoFiles([]);
      setDocumentFiles([]);

      await handleSelectCustomer(selectedCustomerId);
    } catch (error) {
      console.error("Medya yükleme hatası:", error);
      alert("❌ Medya yüklenemedi: " + (error.response?.data?.message || error.message));
    } finally {
      setUploadingMedia(false);
    }
  };

  const inputClass = "w-full px-3 py-2 border border-gray-300 rounded-lg bg-white text-gray-900 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500";
  const labelClass = "block text-sm font-medium text-gray-700 mb-1";
  const buttonClass = "w-full sm:w-auto px-4 sm:px-6 py-2.5 rounded-lg font-medium transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center space-x-2";

  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        {error}
      </div>
    );
  }

  return (
    <div className="w-full max-w-6xl mx-auto p-3 sm:p-6 bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl shadow-lg">
      <div className="bg-white rounded-lg p-4 sm:p-6">
        <h2 className="text-2xl sm:text-3xl font-bold text-indigo-700 mb-4 sm:mb-6 flex items-center">
          <span className="mr-2 sm:mr-3">✏️</span> Müşteri Güncelle
        </h2>

        {/* Müşteri Seç */}
        <div className="mb-4 sm:mb-6 bg-gray-50 p-3 sm:p-4 rounded-lg border border-gray-200">
          <label className={labelClass}>
            Güncellenecek Müşteriyi Seçin <span className="text-red-500">*</span>
          </label>
          <select
            value={selectedCustomerId}
            onChange={(e) => handleSelectCustomer(e.target.value)}
            className={inputClass}
          >
            <option value="">-- Bir müşteri seçiniz --</option>
            {customers.map(c => (
              <option key={c.id} value={c.id} className="text-gray-900">
                {c.companyName} ({c.sector})
              </option>
            ))}
          </select>
        </div>

        {loading && (
          <div className="text-center py-8">
            <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
            <p className="text-indigo-600 mt-4">Yükleniyor...</p>
          </div>
        )}

        {selectedCustomerId && formData && !loading && (
          <div className="space-y-6 sm:space-y-8">
            
            {/* 1. TEMEL BİLGİLER */}
            <section className="bg-gray-50 p-4 sm:p-6 rounded-lg border border-gray-200">
              <h3 className="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4 flex items-center">
                <span className="mr-2">🏢</span> Temel Bilgiler
              </h3>
              
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4 mb-4">
                <div>
                  <label className={labelClass}>Şirket Adı <span className="text-red-500">*</span></label>
                  <input
                    type="text"
                    value={formData.companyName || ""}
                    onChange={(e) => setFormData({ ...formData, companyName: e.target.value })}
                    required
                    className={inputClass}
                    placeholder="Örn: Kahve Dünyası"
                  />
                </div>

                <div>
                  <label className={labelClass}>Sektör <span className="text-red-500">*</span></label>
                  <input
                    type="text"
                    value={formData.sector || ""}
                    onChange={(e) => setFormData({ ...formData, sector: e.target.value })}
                    required
                    className={inputClass}
                    placeholder="Örn: Cafe, Restoran"
                  />
                </div>

                <div className="sm:col-span-2">
                  <label className={labelClass}>Adres <span className="text-red-500">*</span></label>
                  <textarea
                    value={formData.address || ""}
                    onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                    required
                    className={inputClass}
                    rows="2"
                    placeholder="Tam adres"
                  />
                </div>

                <div>
                  <label className={labelClass}>Üyelik Paketi <span className="text-red-500">*</span></label>
                  <select
                    value={formData.membershipPackage || ""}
                    onChange={(e) => setFormData({ ...formData, membershipPackage: e.target.value })}
                    required
                    className={inputClass}
                  >
                    <option value="">Seçiniz</option>
                    <option value="Basic">Basic</option>
                    <option value="Gold">Gold</option>
                    <option value="Platinum">Platinum</option>
                    <option value="Premium">Premium</option>
                  </select>
                </div>

                <div>
                  <label className={labelClass}>Durum <span className="text-red-500">*</span></label>
                  <select
                    value={formData.status || ""}
                    onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                    required
                    className={inputClass}
                  >
                    <option value="ACTIVE">✅ Aktif</option>
                    <option value="PASSIVE">⏸️ Pasif</option>
                    <option value="CANCELLED">❌ İptal</option>
                  </select>
                </div>
              </div>

              {/* Kaydet Butonu */}
              <div className="flex justify-end">
                <button
                  type="button"
                  onClick={handleSaveBasicInfo}
                  disabled={loadingBasicInfo}
                  className={`${buttonClass} bg-indigo-600 text-white hover:bg-indigo-700`}
                >
                  {loadingBasicInfo ? (
                    <>
                      <span className="animate-spin">⏳</span>
                      <span className="text-sm sm:text-base">Kaydediliyor...</span>
                    </>
                  ) : (
                    <>
                      <span>💾</span>
                      <span className="text-sm sm:text-base">Kaydet</span>
                    </>
                  )}
                </button>
              </div>
            </section>

            {/* 2. İLETİŞİM KİŞİLERİ */}
            {formData.contacts && (
              <section className="bg-gray-50 p-4 sm:p-6 rounded-lg border border-gray-200">
                <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 mb-4">
                  <h3 className="text-lg sm:text-xl font-semibold text-gray-800 flex items-center">
                    <span className="mr-2">📞</span> İletişim Kişileri
                  </h3>
                  <button
                    type="button"
                    onClick={addContact}
                    className="w-full sm:w-auto bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition text-sm font-medium"
                  >
                    + Yeni Kişi
                  </button>
                </div>

                {formData.contacts.map((contact, index) => (
                  <div key={index} className="bg-white p-3 sm:p-4 rounded-lg border border-gray-300 mb-3 sm:mb-4">
                    <div className="flex justify-between items-center mb-3">
                      <p className="font-medium text-gray-700 text-sm sm:text-base">Yetkili #{index + 1}</p>
                      <button
                        type="button"
                        onClick={() => removeContact(index)}
                        className="bg-red-500 text-white px-2 sm:px-3 py-1 rounded text-xs sm:text-sm hover:bg-red-600"
                      >
                        🗑️ Sil
                      </button>
                    </div>
                    
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3 sm:gap-4">
                      <div>
                        <label className={labelClass}>Ad <span className="text-red-500">*</span></label>
                        <input
                          type="text"
                          value={contact.name || ""}
                          onChange={(e) => updateContact(index, 'name', e.target.value)}
                          required
                          className={inputClass}
                          placeholder="Adı"
                        />
                      </div>

                      <div>
                        <label className={labelClass}>Soyad <span className="text-red-500">*</span></label>
                        <input
                          type="text"
                          value={contact.surname || ""}
                          onChange={(e) => updateContact(index, 'surname', e.target.value)}
                          required
                          className={inputClass}
                          placeholder="Soyadı"
                        />
                      </div>

                      <div>
                        <label className={labelClass}>Email <span className="text-red-500">*</span></label>
                        <input
                          type="email"
                          value={contact.email || ""}
                          onChange={(e) => updateContact(index, 'email', e.target.value)}
                          required
                          className={inputClass}
                          placeholder="email@example.com"
                        />
                      </div>

                      <div>
                        <label className={labelClass}>Telefon <span className="text-red-500">*</span></label>
                        <input
                          type="text"
                          value={contact.phone || ""}
                          onChange={(e) => updateContact(index, 'phone', e.target.value)}
                          required
                          className={inputClass}
                          placeholder="5XX XXX XX XX"
                        />
                      </div>
                    </div>
                  </div>
                ))}

                {/* Kaydet Butonu */}
                <div className="flex justify-end mt-4">
                  <button
                    type="button"
                    onClick={handleSaveContacts}
                    disabled={loadingContacts}
                    className={`${buttonClass} bg-indigo-600 text-white hover:bg-indigo-700`}
                  >
                    {loadingContacts ? (
                      <>
                        <span className="animate-spin">⏳</span>
                        <span className="text-sm sm:text-base">Kaydediliyor...</span>
                      </>
                    ) : (
                      <>
                        <span>💾</span>
                        <span className="text-sm sm:text-base">Kaydet</span>
                      </>
                    )}
                  </button>
                </div>
              </section>
            )}

            {/* 3. SOSYAL MEDYA */}
            {formData.socialMedia && (
              <section className="bg-gray-50 p-4 sm:p-6 rounded-lg border border-gray-200">
                <h3 className="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4 flex items-center">
                  <span className="mr-2">📱</span> Sosyal Medya
                </h3>
                
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-3 sm:gap-4 mb-4">
                  <div>
                    <label className={labelClass}>Instagram</label>
                    <input
                      type="text"
                      value={formData.socialMedia.instagram || ""}
                      onChange={(e) => updateNested('socialMedia', 'instagram', e.target.value)}
                      className={inputClass}
                      placeholder="@kullaniciadi"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>Facebook</label>
                    <input
                      type="text"
                      value={formData.socialMedia.facebook || ""}
                      onChange={(e) => updateNested('socialMedia', 'facebook', e.target.value)}
                      className={inputClass}
                      placeholder="Sayfa adı"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>TikTok</label>
                    <input
                      type="text"
                      value={formData.socialMedia.tiktok || ""}
                      onChange={(e) => updateNested('socialMedia', 'tiktok', e.target.value)}
                      className={inputClass}
                      placeholder="@kullaniciadi"
                    />
                  </div>
                </div>

                {/* Kaydet Butonu */}
                <div className="flex justify-end">
                  <button
                    type="button"
                    onClick={handleSaveSocialMedia}
                    disabled={loadingSocialMedia}
                    className={`${buttonClass} bg-indigo-600 text-white hover:bg-indigo-700`}
                  >
                    {loadingSocialMedia ? (
                      <>
                        <span className="animate-spin">⏳</span>
                        <span className="text-sm sm:text-base">Kaydediliyor...</span>
                      </>
                    ) : (
                      <>
                        <span>💾</span>
                        <span className="text-sm sm:text-base">Kaydet</span>
                      </>
                    )}
                  </button>
                </div>
              </section>
            )}

            {/* 4. HEDEF KİTLE */}
            {formData.targetAudience && (
              <section className="bg-gray-50 p-4 sm:p-6 rounded-lg border border-gray-200">
                <h3 className="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4 flex items-center">
                  <span className="mr-2">🎯</span> Hedef Kitle
                </h3>
                
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4 mb-4">
                  <div>
                    <label className={labelClass}>Post Türü <span className="text-red-500">*</span></label>
                    <select
                      value={formData.targetAudience.postType || ""}
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
                    <label className={labelClass}>Haftalık Post Sıklığı <span className="text-red-500">*</span></label>
                    <input
                      type="number"
                      min="1"
                      max="7"
                      value={formData.targetAudience.postFrequency || ""}
                      onChange={(e) => updateNested('targetAudience', 'postFrequency', e.target.value)}
                      required
                      className={inputClass}
                      placeholder="1-7"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>Post Tonu <span className="text-red-500">*</span></label>
                    <select
                      value={formData.targetAudience.postTone || ""}
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

                  <div>
                    <label className={labelClass}>Hedef Bölge</label>
                    <input
                      type="text"
                      value={formData.targetAudience.targetRegion || ""}
                      onChange={(e) => updateNested('targetAudience', 'targetRegion', e.target.value)}
                      className={inputClass}
                      placeholder="Örn: Antalya"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>Hedef Yaş</label>
                    <input
                      type="text"
                      value={formData.targetAudience.audienceAge || ""}
                      onChange={(e) => updateNested('targetAudience', 'audienceAge', e.target.value)}
                      className={inputClass}
                      placeholder="Örn: 25-45"
                    />
                  </div>

                  <div className="sm:col-span-2 lg:col-span-3">
                    <label className={labelClass}>Hashtagler</label>
                    <input
                      type="text"
                      value={formData.targetAudience.customerHashtags || ""}
                      onChange={(e) => updateNested('targetAudience', 'customerHashtags', e.target.value)}
                      className={inputClass}
                      placeholder="#kahve #antalya"
                    />
                  </div>

                  <div className="sm:col-span-2 lg:col-span-3">
                    <label className={labelClass}>İlgi Alanları</label>
                    <input
                      type="text"
                      value={formData.targetAudience.audienceInterests || ""}
                      onChange={(e) => updateNested('targetAudience', 'audienceInterests', e.target.value)}
                      className={inputClass}
                      placeholder="Örn: Kahve, Deniz"
                    />
                  </div>
                </div>

                <div className="mb-4">
                  <label className="flex items-center space-x-2 cursor-pointer">
                    <input
                      type="checkbox"
                      checked={formData.targetAudience.specialDates || false}
                      onChange={(e) => updateNested('targetAudience', 'specialDates', e.target.checked)}
                      className="w-5 h-5 text-indigo-600 rounded"
                    />
                    <span className="text-gray-700 font-medium text-sm sm:text-base">🎉 Özel günlerde post</span>
                  </label>
                </div>

                {/* Kaydet Butonu */}
                <div className="flex justify-end">
                  <button
                    type="button"
                    onClick={handleSaveTargetAudience}
                    disabled={loadingTargetAudience}
                    className={`${buttonClass} bg-indigo-600 text-white hover:bg-indigo-700`}
                  >
                    {loadingTargetAudience ? (
                      <>
                        <span className="animate-spin">⏳</span>
                        <span className="text-sm sm:text-base">Kaydediliyor...</span>
                      </>
                    ) : (
                      <>
                        <span>💾</span>
                        <span className="text-sm sm:text-base">Kaydet</span>
                      </>
                    )}
                  </button>
                </div>
              </section>
            )}

            {/* 5. SEO */}
            {formData.seo && (
              <section className="bg-gray-50 p-4 sm:p-6 rounded-lg border border-gray-200">
                <h3 className="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4 flex items-center">
                  <span className="mr-2">🔍</span> SEO
                </h3>
                
                <div className="space-y-3 sm:space-y-4 mb-4">
                  <div>
                    <label className={labelClass}>Google Console Email</label>
                    <input
                      type="email"
                      value={formData.seo.googleConsoleEmail || ""}
                      onChange={(e) => updateNested('seo', 'googleConsoleEmail', e.target.value)}
                      className={inputClass}
                      placeholder="console@example.com"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>SEO Başlık Önerileri</label>
                    <textarea
                      value={formData.seo.titleSuggestions || ""}
                      onChange={(e) => updateNested('seo', 'titleSuggestions', e.target.value)}
                      className={inputClass}
                      rows="2"
                      placeholder="SEO başlık önerileri..."
                    />
                  </div>

                  <div>
                    <label className={labelClass}>SEO İçerik Önerileri</label>
                    <textarea
                      value={formData.seo.contentSuggestions || ""}
                      onChange={(e) => updateNested('seo', 'contentSuggestions', e.target.value)}
                      className={inputClass}
                      rows="3"
                      placeholder="SEO içerik önerileri..."
                    />
                  </div>
                </div>

                {/* Kaydet Butonu */}
                <div className="flex justify-end">
                  <button
                    type="button"
                    onClick={handleSaveSeo}
                    disabled={loadingSeo}
                    className={`${buttonClass} bg-indigo-600 text-white hover:bg-indigo-700`}
                  >
                    {loadingSeo ? (
                      <>
                        <span className="animate-spin">⏳</span>
                        <span className="text-sm sm:text-base">Kaydediliyor...</span>
                      </>
                    ) : (
                      <>
                        <span>💾</span>
                        <span className="text-sm sm:text-base">Kaydet</span>
                      </>
                    )}
                  </button>
                </div>
              </section>
            )}

            {/* 6. API ANAHTARLARI */}
            {formData.apiKeys && (
              <section className="bg-gray-50 p-4 sm:p-6 rounded-lg border border-gray-200">
                <h3 className="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4 flex items-center">
                  <span className="mr-2">🔑</span> API Anahtarları
                </h3>
                
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4 mb-4">
                  <div>
                    <label className={labelClass}>Instagram API</label>
                    <input
                      type="text"
                      value={formData.apiKeys.instagramApiKey || ""}
                      onChange={(e) => updateNested('apiKeys', 'instagramApiKey', e.target.value)}
                      className={inputClass}
                      placeholder="Instagram API"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>Facebook API</label>
                    <input
                      type="text"
                      value={formData.apiKeys.facebookApiKey || ""}
                      onChange={(e) => updateNested('apiKeys', 'facebookApiKey', e.target.value)}
                      className={inputClass}
                      placeholder="Facebook API"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>TikTok API</label>
                    <input
                      type="text"
                      value={formData.apiKeys.tiktokApiKey || ""}
                      onChange={(e) => updateNested('apiKeys', 'tiktokApiKey', e.target.value)}
                      className={inputClass}
                      placeholder="TikTok API"
                    />
                  </div>

                  <div>
                    <label className={labelClass}>Google API</label>
                    <input
                      type="text"
                      value={formData.apiKeys.googleApiKey || ""}
                      onChange={(e) => updateNested('apiKeys', 'googleApiKey', e.target.value)}
                      className={inputClass}
                      placeholder="Google API"
                    />
                  </div>
                </div>

                {/* Kaydet Butonu */}
                <div className="flex justify-end">
                  <button
                    type="button"
                    onClick={handleSaveApiKeys}
                    disabled={loadingApiKeys}
                    className={`${buttonClass} bg-indigo-600 text-white hover:bg-indigo-700`}
                  >
                    {loadingApiKeys ? (
                      <>
                        <span className="animate-spin">⏳</span>
                        <span className="text-sm sm:text-base">Kaydediliyor...</span>
                      </>
                    ) : (
                      <>
                        <span>💾</span>
                        <span className="text-sm sm:text-base">Kaydet</span>
                      </>
                    )}
                  </button>
                </div>
              </section>
            )}

            {/* 7. YENİ MEDYA YÜKLEME */}
            <section className="bg-gradient-to-r from-purple-50 to-indigo-50 p-4 sm:p-6 rounded-lg border-2 border-purple-200">
              <h3 className="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4 flex items-center">
                <span className="mr-2">📤</span> Yeni Medya Ekle
              </h3>
              
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4 mb-4">
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
                    <p className="text-xs sm:text-sm text-green-600 mt-1 font-medium">✅ {logoFiles.length} dosya seçildi</p>
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
                    <p className="text-xs sm:text-sm text-green-600 mt-1 font-medium">✅ {photoFiles.length} dosya seçildi</p>
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
                    <p className="text-xs sm:text-sm text-green-600 mt-1 font-medium">✅ {videoFiles.length} dosya seçildi</p>
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
                    <p className="text-xs sm:text-sm text-green-600 mt-1 font-medium">✅ {documentFiles.length} dosya seçildi</p>
                  )}
                </div>
              </div>

              <button
                type="button"
                onClick={handleUploadNewMedia}
                disabled={uploadingMedia}
                className="w-full bg-purple-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-purple-700 transition disabled:bg-gray-400 flex items-center justify-center space-x-2"
              >
                {uploadingMedia ? (
                  <>
                    <span className="animate-spin">⏳</span>
                    <span className="text-sm sm:text-base">Yükleniyor...</span>
                  </>
                ) : (
                  <>
                    <span>📤</span>
                    <span className="text-sm sm:text-base">Medya Yükle</span>
                  </>
                )}
              </button>
            </section>

            {/* 8. MEVCUT MEDYA GALERİSİ */}
            {formData.media && formData.media.length > 0 && (
              <MediaGallery 
                media={formData.media}
                customerId={selectedCustomerId}
                onMediaUpdate={handleMediaUpdate}
              />
            )}

          </div>
        )}
      </div>
    </div>
  );
}
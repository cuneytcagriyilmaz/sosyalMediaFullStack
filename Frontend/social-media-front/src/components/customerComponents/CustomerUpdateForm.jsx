// src/components/customerComponents/CustomerUpdateForm.jsx
import { useState, useEffect } from "react";
import customerService from "../../services/customerService";

export default function CustomerUpdateForm() {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState("");
  const [formData, setFormData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Tüm müşterileri getir
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

  // Müşteri seçildiğinde detaylarını getir
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

  // Contact ekle
  const addContact = () => {
    setFormData({
      ...formData,
      contacts: [
        ...formData.contacts,
        { name: "", surname: "", email: "", phone: "", priority: formData.contacts.length + 1 }
      ]
    });
  };

  // Contact sil
  const removeContact = (index) => {
    const newContacts = formData.contacts.filter((_, i) => i !== index);
    setFormData({ ...formData, contacts: newContacts });
  };

  // Contact güncelle
  const updateContact = (index, field, value) => {
    const newContacts = [...formData.contacts];
    newContacts[index][field] = value;
    setFormData({ ...formData, contacts: newContacts });
  };

  // Nested alan güncelle
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
      // PATCH isteği - sadece değişen alanları gönder
      const updateData = {
        companyName: formData.companyName,
        sector: formData.sector,
        address: formData.address,
        membershipPackage: formData.membershipPackage,
        status: formData.status,
        targetAudience: formData.targetAudience,
        contacts: formData.contacts,
        socialMedia: formData.socialMedia,
        seo: formData.seo,
        apiKeys: formData.apiKeys
      };

      const updated = await customerService.updateCustomer(selectedCustomerId, updateData);
      console.log("Müşteri güncellendi:", updated);

      alert("✅ Müşteri başarıyla güncellendi!");
      
      // Güncel veriyi tekrar getir
      handleSelectCustomer(selectedCustomerId);

    } catch (err) {
      console.error("Güncelleme hatası:", err);
      alert("❌ Hata: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const inputClass = "w-full mt-1 px-3 py-2 border border-gray-300 rounded-lg bg-white text-black focus:ring-2 focus:ring-orange-500 focus:border-orange-500";

  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        {error}
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto p-6 bg-white rounded-xl shadow-lg">
      <h2 className="text-2xl font-bold text-indigo-700 mb-6">👥 Müşteri Güncelle</h2>

      {/* Müşteri Seç */}
      <div className="mb-4">
        <label className="block text-sm font-medium text-gray-700">Müşteri Seç</label>
        <select
          value={selectedCustomerId}
          onChange={(e) => handleSelectCustomer(e.target.value)}
          className={inputClass}
        >
          <option value="">Seçiniz</option>
          {customers.map(c => (
            <option key={c.id} value={c.id}>{c.companyName}</option>
          ))}
        </select>
      </div>

      {loading && (
        <div className="text-center py-4">
          <span className="text-indigo-600">Yükleniyor...</span>
        </div>
      )}

      {/* Form */}
      {selectedCustomerId && formData && !loading && (
        <form onSubmit={handleSubmit} className="space-y-6">

          {/* Temel Bilgiler */}
          <section>
            <h3 className="text-lg font-semibold mb-4">Temel Bilgiler</h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Şirket Adı</label>
                <input
                  type="text"
                  value={formData.companyName || ""}
                  onChange={(e) => setFormData({ ...formData, companyName: e.target.value })}
                  className={inputClass}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Sektör</label>
                <input
                  type="text"
                  value={formData.sector || ""}
                  onChange={(e) => setFormData({ ...formData, sector: e.target.value })}
                  className={inputClass}
                />
              </div>

              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-gray-700">Adres</label>
                <textarea
                  value={formData.address || ""}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                  className={inputClass}
                  rows="2"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Üyelik Paketi</label>
                <select
                  value={formData.membershipPackage || ""}
                  onChange={(e) => setFormData({ ...formData, membershipPackage: e.target.value })}
                  className={inputClass}
                >
                  <option value="">Seçiniz</option>
                  <option value="Basic">Basic</option>
                  <option value="Gold">Gold</option>
                  <option value="Platinum">Platinum</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Durum</label>
                <select
                  value={formData.status || ""}
                  onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                  className={inputClass}
                >
                  <option value="ACTIVE">Aktif</option>
                  <option value="PASSIVE">Pasif</option>
                  <option value="CANCELLED">İptal</option>
                </select>
              </div>
            </div>
          </section>

          {/* Sosyal Medya */}
          {formData.socialMedia && (
            <section>
              <h3 className="text-lg font-semibold mb-4">Sosyal Medya</h3>
              
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Instagram</label>
                  <input
                    type="text"
                    value={formData.socialMedia.instagram || ""}
                    onChange={(e) => updateNested('socialMedia', 'instagram', e.target.value)}
                    className={inputClass}
                    placeholder="@kullaniciadi"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">Facebook</label>
                  <input
                    type="text"
                    value={formData.socialMedia.facebook || ""}
                    onChange={(e) => updateNested('socialMedia', 'facebook', e.target.value)}
                    className={inputClass}
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">TikTok</label>
                  <input
                    type="text"
                    value={formData.socialMedia.tiktok || ""}
                    onChange={(e) => updateNested('socialMedia', 'tiktok', e.target.value)}
                    className={inputClass}
                    placeholder="@kullaniciadi"
                  />
                </div>
              </div>
            </section>
          )}

          {/* Hedef Kitle */}
          {formData.targetAudience && (
            <section>
              <h3 className="text-lg font-semibold mb-4">Hedef Kitle ve İçerik</h3>
              
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Post Türü</label>
                  <select
                    value={formData.targetAudience.postType || ""}
                    onChange={(e) => updateNested('targetAudience', 'postType', e.target.value)}
                    className={inputClass}
                  >
                    <option value="gorsel">Görsel</option>
                    <option value="video">Video</option>
                    <option value="story">Story</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">Haftalık Post Sıklığı</label>
                  <input
                    type="number"
                    min="1"
                    max="7"
                    value={formData.targetAudience.postFrequency || ""}
                    onChange={(e) => updateNested('targetAudience', 'postFrequency', e.target.value)}
                    className={inputClass}
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">Post Tonu</label>
                  <select
                    value={formData.targetAudience.postTone || ""}
                    onChange={(e) => updateNested('targetAudience', 'postTone', e.target.value)}
                    className={inputClass}
                  >
                    <option value="samimi">Samimi</option>
                    <option value="resmi">Resmi</option>
                    <option value="mizahi">Mizahi</option>
                    <option value="ciddi">Ciddi</option>
                  </select>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Hedef Bölge</label>
                  <input
                    type="text"
                    value={formData.targetAudience.targetRegion || ""}
                    onChange={(e) => updateNested('targetAudience', 'targetRegion', e.target.value)}
                    className={inputClass}
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">Hedef Yaş Aralığı</label>
                  <input
                    type="text"
                    value={formData.targetAudience.audienceAge || ""}
                    onChange={(e) => updateNested('targetAudience', 'audienceAge', e.target.value)}
                    className={inputClass}
                  />
                </div>

                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700">Hashtagler</label>
                  <input
                    type="text"
                    value={formData.targetAudience.customerHashtags || ""}
                    onChange={(e) => updateNested('targetAudience', 'customerHashtags', e.target.value)}
                    className={inputClass}
                  />
                </div>

                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700">İlgi Alanları</label>
                  <input
                    type="text"
                    value={formData.targetAudience.audienceInterests || ""}
                    onChange={(e) => updateNested('targetAudience', 'audienceInterests', e.target.value)}
                    className={inputClass}
                  />
                </div>
              </div>

              <div className="mt-4">
                <label className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    checked={formData.targetAudience.specialDates || false}
                    onChange={(e) => updateNested('targetAudience', 'specialDates', e.target.checked)}
                    className="w-5 h-5"
                  />
                  <span>Özel günlerde post yapılsın</span>
                </label>
              </div>
            </section>
          )}

          {/* SEO */}
          {formData.seo && (
            <section>
              <h3 className="text-lg font-semibold mb-4">SEO Bilgileri</h3>
              
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Google Console Email</label>
                  <input
                    type="email"
                    value={formData.seo.googleConsoleEmail || ""}
                    onChange={(e) => updateNested('seo', 'googleConsoleEmail', e.target.value)}
                    className={inputClass}
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">SEO Başlık Önerileri</label>
                  <textarea
                    value={formData.seo.titleSuggestions || ""}
                    onChange={(e) => updateNested('seo', 'titleSuggestions', e.target.value)}
                    className={inputClass}
                    rows="2"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">SEO İçerik Önerileri</label>
                  <textarea
                    value={formData.seo.contentSuggestions || ""}
                    onChange={(e) => updateNested('seo', 'contentSuggestions', e.target.value)}
                    className={inputClass}
                    rows="3"
                  />
                </div>
              </div>
            </section>
          )}

          {/* İletişim Kişileri */}
          {formData.contacts && (
            <section>
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold">İletişim Kişileri</h3>
                <button
                  type="button"
                  onClick={addContact}
                  className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                >
                  + Ekle
                </button>
              </div>

              {formData.contacts.map((contact, index) => (
                <div key={index} className="grid grid-cols-1 md:grid-cols-5 gap-4 mb-4 p-4 border rounded-lg">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Ad</label>
                    <input
                      type="text"
                      value={contact.name || ""}
                      onChange={(e) => updateContact(index, 'name', e.target.value)}
                      className={inputClass}
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Soyad</label>
                    <input
                      type="text"
                      value={contact.surname || ""}
                      onChange={(e) => updateContact(index, 'surname', e.target.value)}
                      className={inputClass}
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Email</label>
                    <input
                      type="email"
                      value={contact.email || ""}
                      onChange={(e) => updateContact(index, 'email', e.target.value)}
                      className={inputClass}
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Telefon</label>
                    <input
                      type="text"
                      value={contact.phone || ""}
                      onChange={(e) => updateContact(index, 'phone', e.target.value)}
                      className={inputClass}
                    />
                  </div>

                  <div className="flex items-end">
                    <button
                      type="button"
                      onClick={() => removeContact(index)}
                      className="bg-red-500 text-white px-3 py-2 rounded hover:bg-red-600 w-full"
                    >
                      Sil
                    </button>
                  </div>
                </div>
              ))}
            </section>
          )}

          {/* Submit Button */}
          <button
            type="submit"
            disabled={loading}
            className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-indigo-700 transition disabled:bg-gray-400"
          >
            {loading ? "Güncelleniyor..." : "Müşteriyi Güncelle"}
          </button>
        </form>
      )}
    </div>
  );
}
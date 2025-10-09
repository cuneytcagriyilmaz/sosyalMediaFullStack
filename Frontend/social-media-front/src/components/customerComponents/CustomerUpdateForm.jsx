import { useState, useEffect } from "react";
import fakeCustomers from "../../data/fakeCustomers";

export default function CustomerUpdateForm() {
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [selectedCustomerId, setSelectedCustomerId] = useState("");
  const [formData, setFormData] = useState({});
  const [showOther, setShowOther] = useState({
    sector: false,
    postFrequency: false,
    postType: false,
    postTone: false,
  });

  useEffect(() => {
    if (selectedCustomerId) {
      const customer = fakeCustomers.find(c => c.id === parseInt(selectedCustomerId));
      if (customer) {
        setFormData({ ...customer });
        setShowOther({
          sector: !["cafe", "restaurant", "store"].includes(customer.sector),
          postFrequency: !["1","2","3"].includes(customer.postFrequency),
          postType: !["gorsel","video","story","genel"].includes(customer.postType),
          postTone: !["samimi","resmi","mizahi","ciddi"].includes(customer.postTone),
        });
      }
    } else {
      setFormData({});
      setShowOther({ sector:false, postFrequency:false, postType:false, postTone:false });
    }
  }, [selectedCustomerId]);

  const handleChange = (e) => {
    const { name, value, type, checked, files } = e.target;

    if (type === "checkbox") {
      setFormData({ ...formData, [name]: checked });
      return;
    }

    if (type === "file") {
      setFormData({ ...formData, [name]: Array.from(files) });
      return;
    }

    if (["sector", "postFrequency", "postType", "postTone"].includes(name)) {
      setFormData({ ...formData, [name]: value });
      if (name === "sector") setShowOther({ ...showOther, sector: value === "other" || !["cafe","restaurant","store"].includes(value) });
      if (name === "postFrequency") setShowOther({ ...showOther, postFrequency: value === "ozel" || !["1","2","3"].includes(value) });
      if (name === "postType") setShowOther({ ...showOther, postType: value === "other" || !["gorsel","video","story","genel"].includes(value) });
      if (name === "postTone") setShowOther({ ...showOther, postTone: value === "other" || !["samimi","resmi","mizahi","ciddi"].includes(value) });
      return;
    }

    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Güncellenmiş Müşteri Verisi:", formData);
    alert("Müşteri başarıyla güncellendi!");
    // TODO: Backend API çağrısı yapılacak
  };

  const inputClass = "w-full mt-1 px-3 py-2 border border-gray-300 rounded-lg bg-white text-black focus:ring-2 focus:ring-orange-500 focus:border-orange-500";

  return (
    <div className="max-w-5xl mx-auto p-6 bg-white rounded-xl shadow-lg">
      <h2 className="text-2xl font-bold text-indigo-700 mb-6">👥 Müşteri Güncelle</h2>

      <div className="mb-4">
        <label className="block text-sm font-medium text-gray-700">Müşteri Seç</label>
        <select
          value={selectedCustomerId}
          onChange={(e) => setSelectedCustomerId(e.target.value)}
          className={inputClass}
        >
          <option value="">Seçiniz</option>
          {fakeCustomers.map(c => (
            <option key={c.id} value={c.id}>{c.companyName}</option>
          ))}
        </select>
      </div>

      {selectedCustomerId && (
        <form onSubmit={handleSubmit} className="space-y-4">

          {/* Şirket Bilgileri */}
          <div>
            <label className="block text-sm font-medium text-gray-700">Şirket / Dükkan Adı</label>
            <input type="text" name="companyName" value={formData.companyName || ""} onChange={handleChange} className={inputClass} />
          </div>

          {/* Sektör */}
          <div>
            <label className="block text-sm font-medium text-gray-700">Sektör</label>
            <select name="sector" value={showOther.sector ? "other" : formData.sector || ""} onChange={handleChange} className={inputClass}>
              <option value="">Seçiniz</option>
              <option value="cafe">Cafe</option>
              <option value="restaurant">Restoran</option>
              <option value="store">Mağaza</option>
              <option value="other">Diğer</option>
            </select>
            {showOther.sector && <input type="text" name="sector" value={formData.sector} onChange={handleChange} className={`${inputClass} mt-2`} />}
          </div>

          {/* Adres */}
          <div>
            <label className="block text-sm font-medium text-gray-700">Adres</label>
            <input type="text" name="address" value={formData.address || ""} onChange={handleChange} className={inputClass} />
          </div>

          {/* Sosyal Medya */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {["instagram", "facebook", "tiktok"].map(platform => (
              <div key={platform}>
                <label className="block text-sm font-medium text-gray-700">{platform}</label>
                <input type="text" name={platform} value={formData[platform] || ""} onChange={handleChange} className={inputClass} />
              </div>
            ))}
          </div>

          {/* Post Planlama */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {/* Haftalık Post Sıklığı */}
            <div>
              <label className="block text-sm font-medium text-gray-700">Haftalık Post Sıklığı</label>
              <select name="postFrequency" value={showOther.postFrequency ? "other" : formData.postFrequency || ""} onChange={handleChange} className={inputClass}>
                <option value="1">Haftada 1</option>
                <option value="2">Haftada 2</option>
                <option value="3">Haftada 3</option>
                <option value="other">Özel</option>
              </select>
              {showOther.postFrequency && <input type="text" name="postFrequency" value={formData.postFrequency} onChange={handleChange} className={`${inputClass} mt-2`} />}
            </div>

            {/* Post Türü */}
            <div>
              <label className="block text-sm font-medium text-gray-700">Post Türü</label>
              <select name="postType" value={showOther.postType ? "other" : formData.postType || ""} onChange={handleChange} className={inputClass}>
                <option value="gorsel">Görsel</option>
                <option value="video">Video</option>
                <option value="story">Story</option>
                <option value="genel">Genel</option>
                <option value="other">Diğer</option>
              </select>
              {showOther.postType && <input type="text" name="postType" value={formData.postType} onChange={handleChange} className={`${inputClass} mt-2`} />}
            </div>

            {/* Post Tonu */}
            <div>
              <label className="block text-sm font-medium text-gray-700">Post Tonu</label>
              <select name="postTone" value={showOther.postTone ? "other" : formData.postTone || ""} onChange={handleChange} className={inputClass}>
                <option value="samimi">Samimi</option>
                <option value="resmi">Resmi</option>
                <option value="mizahi">Mizahi</option>
                <option value="ciddi">Ciddi</option>
                <option value="other">Diğer</option>
              </select>
              {showOther.postTone && <input type="text" name="postTone" value={formData.postTone} onChange={handleChange} className={`${inputClass} mt-2`} />}
            </div>
          </div>

          {/* Diğer Alanlar (hashtag, SEO, target, contacts vb.) */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Müşteriden Tavsiyeler: Hashtagler</label>
              <input type="text" name="customerHashtags" value={formData.customerHashtags || ""} onChange={handleChange} className={inputClass} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">SEO Başlık Önerileri</label>
              <textarea name="seoTitleSuggestions" value={formData.seoTitleSuggestions || ""} onChange={handleChange} className={`${inputClass} resize-none h-20`} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">SEO İçerik Önerileri</label>
              <textarea name="seoContentSuggestions" value={formData.seoContentSuggestions || ""} onChange={handleChange} className={`${inputClass} resize-none h-20`} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Hedef Bölge / Lokasyon</label>
              <input type="text" name="targetRegion" value={formData.targetRegion || ""} onChange={handleChange} className={inputClass} />
            </div>
          </div>

          {/* Yetkili Kişiler */}
          {[1,2,3].map(i => (
            <div key={i} className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Yetkili {i} Adı</label>
                <input type="text" name={`customerContact${i}Name`} value={formData[`customerContact${i}Name`] || ""} onChange={handleChange} className={inputClass} />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Yetkili {i} Email</label>
                <input type="email" name={`customerContact${i}Email`} value={formData[`customerContact${i}Email`] || ""} onChange={handleChange} className={inputClass} />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Yetkili {i} Telefon</label>
                <input type="text" name={`customerContact${i}Phone`} value={formData[`customerContact${i}Phone`] || ""} onChange={handleChange} className={inputClass} />
              </div>
            </div>
          ))}

          {/* Özel Tarihler ve Audience */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="flex items-center space-x-2">
                <input type="checkbox" name="specialDates" checked={formData.specialDates || false} onChange={handleChange} />
                <span className="text-gray-700">Özel Günler</span>
              </label>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Hedef Kitle Yaş</label>
              <input type="text" name="audienceAge" value={formData.audienceAge || ""} onChange={handleChange} className={inputClass} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Hedef Kitle İlgi Alanları</label>
              <input type="text" name="audienceInterests" value={formData.audienceInterests || ""} onChange={handleChange} className={inputClass} />
            </div>
          </div>

          {/* API Anahtarları */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            {["instagramApiKey","facebookApiKey","tiktokApiKey","googleApiKey"].map(key => (
              <div key={key}>
                <label className="block text-sm font-medium text-gray-700">{key}</label>
                <input type="text" name={key} value={formData[key] || ""} onChange={handleChange} className={inputClass} />
              </div>
            ))}
          </div>

          {/* Paket ve Dosyalar */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Üyelik Paketi</label>
              <input type="text" name="membershipPackage" value={formData.membershipPackage || ""} onChange={handleChange} className={inputClass} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Logo Yükle</label>
              <input type="file" name="customerLogos" multiple onChange={handleChange} className={inputClass} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Fotoğraf Yükle</label>
              <input type="file" name="customerPhotos" multiple onChange={handleChange} className={inputClass} />
            </div>
          </div>

          <button type="submit" className="w-full bg-indigo-600 text-white py-2 px-4 rounded-lg font-medium hover:bg-indigo-700 transition">
            Müşteriyi Güncelle
          </button>
        </form>
      )}
    </div>
  );
}
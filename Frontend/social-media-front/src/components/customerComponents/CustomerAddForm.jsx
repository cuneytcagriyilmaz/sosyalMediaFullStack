import { useState } from "react";

export default function CustomerAddForm() {
  const [formData, setFormData] = useState({
    companyName: "",
    sector: "",
    address: "",
    instagram: "",
    facebook: "",
    tiktok: "",
    postFrequency: "2",
    postType: "gorsel",
    postTone: "samimi",
    customerHashtags: "",
    googleConsoleEmail: "",
    seoTitleSuggestions: "",
    seoContentSuggestions: "",
    targetRegion: "",
    customerContact1Name: "",
    customerContact1Email: "",
    customerContact1Phone: "",
    customerContact2Name: "",
    customerContact2Email: "",
    customerContact2Phone: "",
    customerContact3Name: "",
    customerContact3Email: "",
    customerContact3Phone: "",
    specialDates: false,
    audienceAge: "",
    audienceInterests: "",
    instagramApiKey: "",
    facebookApiKey: "",
    tiktokApiKey: "",
    googleApiKey: "",
    membershipPackage: "",
    customerLogos: [],
    customerPhotos: []
  });

  const [showOther, setShowOther] = useState({
    sector: false,
    postFrequency: false,
    postType: false,
    postTone: false
  });

  const [otherValues, setOtherValues] = useState({
    otherSector: "",
    otherPostFrequency: "",
    otherPostType: "",
    otherPostTone: ""
  });

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
      setShowOther({ ...showOther, [name]: value === "other" || value === "ozel" });
      return;
    }

    if (["otherSector", "otherPostFrequency", "otherPostType", "otherPostTone"].includes(name)) {
      setOtherValues({ ...otherValues, [name]: value });
      return;
    }

    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const finalData = { ...formData };

    // Other input değerlerini formData'ya geçir
    if (showOther.sector) finalData.sector = otherValues.otherSector;
    if (showOther.postFrequency) finalData.postFrequency = otherValues.otherPostFrequency;
    if (showOther.postType) finalData.postType = otherValues.otherPostType;
    if (showOther.postTone) finalData.postTone = otherValues.otherPostTone;

    console.log("Müşteri verisi:", finalData);
    alert("Müşteri başarıyla eklendi!");
    // TODO: Backend API çağrısı yapılacak
  };

  const inputClass =
    "w-full mt-1 px-3 py-2 border border-gray-300 rounded-lg bg-white text-black focus:ring-2 focus:ring-orange-500 focus:border-orange-500";

  return (
    <div className="max-w-5xl mx-auto p-6 bg-white rounded-xl shadow-lg">
      <h2 className="text-2xl font-bold text-indigo-700 mb-6">👥 Müşteri Ekle</h2>
      <form className="space-y-4" onSubmit={handleSubmit}>
        {/* Şirket Bilgileri */}
        <div className="relative">
          <label className="block text-sm font-medium text-gray-700">
            Şirket / Dükkan Adı <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            name="companyName"
            value={formData.companyName}
            onChange={handleChange}
            required
            className={inputClass}
          />
        </div>

        <div className="relative">
          <label className="block text-sm font-medium text-gray-700">
            Sektör <span className="text-red-500">*</span>
          </label>
          <select
            name="sector"
            value={formData.sector}
            onChange={handleChange}
            required
            className={`${inputClass} text-black`}
          >
            <option value="">Seçiniz</option>
            <option value="cafe">Cafe</option>
            <option value="restaurant">Restoran</option>
            <option value="store">Mağaza</option>
            <option value="other">Diğer</option>
          </select>
          {showOther.sector && (
            <input
              type="text"
              name="otherSector"
              value={otherValues.otherSector}
              onChange={handleChange}
              placeholder="Diğer sektör"
              className={`${inputClass} mt-2`}
            />
          )}
        </div>

        <div className="relative">
          <label className="block text-sm font-medium text-gray-700">
            Adres / Lokasyon <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            name="address"
            value={formData.address}
            onChange={handleChange}
            required
            className={inputClass}
          />
        </div>

        {/* Sosyal Medya */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {["instagram", "facebook", "tiktok"].map((platform) => (
            <div key={platform} className="relative">
              <label className="block text-sm font-medium text-gray-700">
                {platform.charAt(0).toUpperCase() + platform.slice(1)} Hesabı
              </label>
              <input
                type="text"
                name={platform}
                value={formData[platform]}
                onChange={handleChange}
                className={inputClass}
              />
            </div>
          ))}
        </div>

        {/* Post Planlama */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="relative">
            <label className="block text-sm font-medium text-gray-700">
              Haftalık Post Sıklığı <span className="text-red-500">*</span>
            </label>
            <select
              name="postFrequency"
              value={formData.postFrequency}
              onChange={handleChange}
              required
              className={`${inputClass} text-black`}
            >
              <option value="1">Haftada 1</option>
              <option value="2">Haftada 2</option>
              <option value="3">Haftada 3</option>
              <option value="ozel">Özel</option>
            </select>
            {showOther.postFrequency && (
              <input
                type="text"
                name="otherPostFrequency"
                value={otherValues.otherPostFrequency}
                onChange={handleChange}
                placeholder="Özel sıklık"
                className={`${inputClass} mt-2`}
              />
            )}
          </div>

          <div className="relative">
            <label className="block text-sm font-medium text-gray-700">
              Post Türü <span className="text-red-500">*</span>
            </label>
            <select
              name="postType"
              value={formData.postType}
              onChange={handleChange}
              required
              className={`${inputClass} text-black`}
            >
              <option value="gorsel">Görsel</option>
              <option value="video">Video</option>
              <option value="story">Story</option>
              <option value="genel">Genel</option>
              <option value="other">Diğer</option>
            </select>
            {showOther.postType && (
              <input
                type="text"
                name="otherPostType"
                value={otherValues.otherPostType}
                onChange={handleChange}
                placeholder="Diğer post türü"
                className={`${inputClass} mt-2`}
              />
            )}
          </div>

          <div className="relative">
            <label className="block text-sm font-medium text-gray-700">
              Post Tonu / Stili <span className="text-red-500">*</span>
            </label>
            <select
              name="postTone"
              value={formData.postTone}
              onChange={handleChange}
              required
              className={`${inputClass} text-black`}
            >
              <option value="samimi">Samimi</option>
              <option value="resmi">Resmi</option>
              <option value="mizahi">Mizahi</option>
              <option value="ciddi">Ciddi</option>
              <option value="other">Diğer</option>
            </select>
            {showOther.postTone && (
              <input
                type="text"
                name="otherPostTone"
                value={otherValues.otherPostTone}
                onChange={handleChange}
                placeholder="Diğer ton/stil"
                className={`${inputClass} mt-2`}
              />
            )}
          </div>
        </div>

        {/* Müşteri Tavsiyeleri */}
        <div className="relative">
          <label className="block text-sm font-medium text-gray-700">
            Müşteriden Tavsiyeler: Hashtagler
          </label>
          <input
            type="text"
            name="customerHashtags"
            value={formData.customerHashtags}
            onChange={handleChange}
            className={inputClass}
          />
        </div>

        {/* SEO / Hedef Kitle */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="relative">
            <label className="block text-sm font-medium text-gray-700">
              SEO Başlık Önerileri
            </label>
            <textarea
              name="seoTitleSuggestions"
              value={formData.seoTitleSuggestions}
              onChange={handleChange}
              className={`${inputClass} resize-none h-20`}
            />
          </div>
          <div className="relative">
            <label className="block text-sm font-medium text-gray-700">
              SEO İçerik Önerileri
            </label>
            <textarea
              name="seoContentSuggestions"
              value={formData.seoContentSuggestions}
              onChange={handleChange}
              className={`${inputClass} resize-none h-20`}
            />
          </div>
        </div>

        <div className="relative">
          <label className="block text-sm font-medium text-gray-700">
            Hedef Bölge / Lokasyon
          </label>
          <input
            type="text"
            name="targetRegion"
            value={formData.targetRegion}
            onChange={handleChange}
            className={inputClass}
          />
        </div>

        {/* Yetkili Kişiler */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {["customerContact1Name", "customerContact1Email", "customerContact1Phone"].map((field, idx) => (
            <div key={idx} className="relative">
              <label className="block text-sm font-medium text-gray-700">
                {field === "customerContact1Name" ? "Yetkili Adı *" : field === "customerContact1Email" ? "Yetkili Email *" : "Yetkili Telefon *"}
              </label>
              <input
                type={field.includes("Email") ? "email" : "text"}
                name={field}
                value={formData[field]}
                onChange={handleChange}
                required
                className={inputClass}
              />
            </div>
          ))}
        </div>

        {/* Özel Günler */}
        <div className="relative flex items-center space-x-2">
          <input
            type="checkbox"
            name="specialDates"
            checked={formData.specialDates}
            onChange={handleChange}
            className="h-5 w-5 text-orange-500 border-gray-300 rounded focus:ring-2 focus:ring-orange-500"
          />
          <label className="block text-sm font-medium text-gray-700">
            Özel Günler Postları
          </label>
        </div>

        {/* Hedef Kitle */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="relative">
            <label className="block text-sm font-medium text-gray-700">Hedef Yaş Aralığı</label>
            <input
              type="text"
              name="audienceAge"
              value={formData.audienceAge}
              onChange={handleChange}
              className={inputClass}
            />
          </div>
          <div className="relative">
            <label className="block text-sm font-medium text-gray-700">Hedef İlgi Alanları</label>
            <input
              type="text"
              name="audienceInterests"
              value={formData.audienceInterests}
              onChange={handleChange}
              className={inputClass}
            />
          </div>
        </div>

        {/* API Keyler */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          {["instagramApiKey", "facebookApiKey", "tiktokApiKey", "googleApiKey"].map((key) => (
            <input
              key={key}
              type="text"
              name={key}
              value={formData[key]}
              onChange={handleChange}
              placeholder={`API Key ${key.replace("ApiKey", "")}`}
              className={inputClass}
            />
          ))}
        </div>

        {/* Üyelik Paketi */}
        <div className="relative">
          <label className="block text-sm font-medium text-gray-700">
            Üyelik Paketi <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            name="membershipPackage"
            value={formData.membershipPackage}
            onChange={handleChange}
            required
            className={inputClass}
          />
        </div>

        {/* Müşteri Logoları */}
        <div className="relative">
          <label className="block text-sm font-medium text-gray-700">Müşteri Logoları</label>
          <input
            type="file"
            name="customerLogos"
            multiple
            onChange={handleChange}
            className={inputClass}
          />
        </div>

        {/* Müşteri Fotoğrafları */}
        <div className="relative">
          <label className="block text-sm font-medium text-gray-700">Müşteri Fotoğrafları</label>
          <input
            type="file"
            name="customerPhotos"
            multiple
            onChange={handleChange}
            className={inputClass}
          />
        </div>

        {/* Submit */}
        <div className="pt-4">
          <button
            type="submit"
            className="w-full bg-indigo-600 text-white py-2 px-4 rounded-lg font-medium hover:bg-indigo-700 transition"
          >
            Müşteriyi Ekle
          </button>
        </div>
      </form>
    </div>
  );
}

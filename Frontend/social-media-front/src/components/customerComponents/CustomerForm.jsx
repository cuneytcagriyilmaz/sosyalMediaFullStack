import { useState } from "react";

export default function CustomerForm() {
  const [formData, setFormData] = useState({
    name: "",
    address: "",
    taxNo: "",
    sector: "",
    contact1: "",
    contact2: "",
    contact3: "",
    website: "",
    email: "",
    logo: null,
    photos: []
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleLogoUpload = (e) => {
    setFormData((prev) => ({ ...prev, logo: e.target.files[0] }));
  };

  const handlePhotosUpload = (e) => {
    setFormData((prev) => ({
      ...prev,
      photos: [...prev.photos, ...Array.from(e.target.files)]
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Form Data:", formData);
  };

  return (
    <div className="min-h-screen bg-gray-100 p-6 flex justify-center items-center">
      <div className="bg-white shadow-lg rounded-2xl w-full max-w-2xl p-8">
        {/* Başlık */}
        <h2 className="text-3xl font-bold text-center mb-6 text-gray-700">
          🏢 Müşteri Kayıt Formu
        </h2>

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Genel Bilgiler */}
          <section>
            <h3 className="text-xl font-semibold text-gray-600 mb-4">Genel Bilgiler</h3>

            <div className="space-y-4">
              <div>
                <label className="block font-medium mb-1">Şirket İsmi</label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  className="w-full p-2 border rounded-lg"
                  placeholder="Örn: Kahve Dünyası"
                  required
                />
              </div>

              <div>
                <label className="block font-medium mb-1">Sektör</label>
                <input
                  type="text"
                  name="sector"
                  value={formData.sector}
                  onChange={handleChange}
                  className="w-full p-2 border rounded-lg"
                  placeholder="Örn: Cafe / Restoran"
                />
              </div>

              <div>
                <label className="block font-medium mb-1">Vergi Numarası</label>
                <input
                  type="text"
                  name="taxNo"
                  value={formData.taxNo}
                  onChange={handleChange}
                  className="w-full p-2 border rounded-lg"
                  placeholder="1234567890"
                />
              </div>

              <div>
                <label className="block font-medium mb-1">Adres</label>
                <textarea
                  name="address"
                  rows="3"
                  value={formData.address}
                  onChange={handleChange}
                  className="w-full p-2 border rounded-lg"
                  placeholder="İstanbul, Beşiktaş..."
                ></textarea>
              </div>
            </div>
          </section>

          {/* İletişim */}
          <section>
            <h3 className="text-xl font-semibold text-gray-600 mb-4">İletişim Bilgileri</h3>

            <div className="space-y-4">
              <div>
                <label className="block font-medium mb-1">Telefon 1</label>
                <input
                  type="text"
                  name="contact1"
                  value={formData.contact1}
                  onChange={handleChange}
                  className="w-full p-2 border rounded-lg"
                  placeholder="+90 555 123 45 67"
                />
              </div>

              <div>
                <label className="block font-medium mb-1">Telefon 2</label>
                <input
                  type="text"
                  name="contact2"
                  value={formData.contact2}
                  onChange={handleChange}
                  className="w-full p-2 border rounded-lg"
                  placeholder="Opsiyonel"
                />
              </div>

              <div>
                <label className="block font-medium mb-1">Telefon 3</label>
                <input
                  type="text"
                  name="contact3"
                  value={formData.contact3}
                  onChange={handleChange}
                  className="w-full p-2 border rounded-lg"
                  placeholder="Opsiyonel"
                />
              </div>

              <div>
                <label className="block font-medium mb-1">E-posta</label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  className="w-full p-2 border rounded-lg"
                  placeholder="info@firma.com"
                />
              </div>

              <div>
                <label className="block font-medium mb-1">Web Sitesi</label>
                <input
                  type="text"
                  name="website"
                  value={formData.website}
                  onChange={handleChange}
                  className="w-full p-2 border rounded-lg"
                  placeholder="https://firma.com"
                />
              </div>
            </div>
          </section>

          {/* Medya */}
          <section>
            <h3 className="text-xl font-semibold text-gray-600 mb-4">Medya</h3>

            <div className="space-y-4">
              <div>
                <label className="block font-medium mb-1">Logo</label>
                <input type="file" accept="image/*" onChange={handleLogoUpload} />
                {formData.logo && (
                  <img
                    src={URL.createObjectURL(formData.logo)}
                    alt="Logo Preview"
                    className="mt-2 w-24 h-24 object-cover rounded-lg border"
                  />
                )}
              </div>

              <div>
                <label className="block font-medium mb-1">Şirket Fotoğrafları</label>
                <input type="file" accept="image/*" multiple onChange={handlePhotosUpload} />
                <div className="flex flex-wrap gap-2 mt-2">
                  {formData.photos.map((photo, i) => (
                    <img
                      key={i}
                      src={URL.createObjectURL(photo)}
                      alt={`photo-${i}`}
                      className="w-24 h-24 object-cover rounded-lg border"
                    />
                  ))}
                </div>
              </div>
            </div>
          </section>

          {/* Kaydet Butonu */}
          <div>
            <button
              type="submit"
              className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition"
            >
              Kaydet
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

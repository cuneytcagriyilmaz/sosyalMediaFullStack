// src/components/customerComponents/CustomerDeleteForm.jsx
import { useState, useEffect } from "react";
import customerService from "../../services/customerService";

export default function CustomerDeleteForm() {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState("");
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Tüm müşterileri getir
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

  // Müşteri seçildiğinde detaylarını göster
  const handleSelectChange = async (customerId) => {
    if (!customerId) {
      setSelectedCustomerId("");
      setSelectedCustomer(null);
      return;
    }

    setSelectedCustomerId(customerId);
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

  // Soft Delete
  const handleSoftDelete = async () => {
    if (!selectedCustomerId) {
      alert("Önce silinecek müşteriyi seçin!");
      return;
    }

    if (!window.confirm(`${selectedCustomer.companyName} isimli müşteri silinsin mi? (Soft Delete - Geri alınabilir)`)) {
      return;
    }

    setLoading(true);

    try {
      await customerService.deleteCustomer(selectedCustomerId);
      alert("✅ Müşteri başarıyla silindi! (Soft Delete)");
      
      // Listeyi güncelle ve seçimi temizle
      setSelectedCustomerId("");
      setSelectedCustomer(null);
      fetchCustomers();

    } catch (err) {
      console.error("Silme hatası:", err);
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
    <div className="max-w-3xl mx-auto p-6 bg-white rounded-xl shadow-lg">
      <h2 className="text-2xl font-bold text-red-600 mb-6">❌ Müşteri Sil</h2>

      {/* Müşteri Seç */}
      <div className="relative mb-6">
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Müşteri Seçiniz
        </label>
        <select
          value={selectedCustomerId}
          onChange={(e) => handleSelectChange(e.target.value)}
          className={inputClass}
          disabled={loading}
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

      {/* Seçili Müşteri Bilgileri */}
      {selectedCustomer && !loading && (
        <div className="bg-gray-50 border rounded-lg p-4 mb-6">
          <h3 className="font-semibold text-lg mb-3 text-gray-800">
            Seçili Müşteri Bilgileri
          </h3>
          
          <div className="space-y-2 text-sm">
            <p><strong>Şirket Adı:</strong> {selectedCustomer.companyName}</p>
            <p><strong>Sektör:</strong> {selectedCustomer.sector}</p>
            <p><strong>Adres:</strong> {selectedCustomer.address}</p>
            <p><strong>Üyelik Paketi:</strong> {selectedCustomer.membershipPackage}</p>
            <p><strong>Durum:</strong> {selectedCustomer.status}</p>
            
            {selectedCustomer.contacts && selectedCustomer.contacts.length > 0 && (
              <div className="mt-3">
                <strong>İletişim Kişileri:</strong>
                <ul className="list-disc pl-5 mt-1">
                  {selectedCustomer.contacts
                    .sort((a, b) => a.priority - b.priority)
                    .map((contact, index) => (
                      <li key={index}>
                        {contact.name} {contact.surname} - {contact.email}
                      </li>
                    ))}
                </ul>
              </div>
            )}

            {selectedCustomer.media && selectedCustomer.media.length > 0 && (
              <p className="mt-3">
                <strong>Medya Dosyaları:</strong> {selectedCustomer.media.length} adet
              </p>
            )}

            <p className="mt-3 text-xs text-gray-500">
              <strong>Oluşturulma:</strong> {new Date(selectedCustomer.createdAt).toLocaleDateString('tr-TR')}
            </p>
          </div>
        </div>
      )}

      {/* Uyarı */}
      {selectedCustomer && (
        <div className="bg-yellow-50 border border-yellow-300 rounded-lg p-4 mb-6">
          <p className="text-yellow-800 text-sm">
            ⚠️ <strong>Dikkat:</strong> Bu işlem "Soft Delete" olarak gerçekleştirilir. 
            Müşteri pasif duruma getirilir ancak verileri korunur ve daha sonra geri yüklenebilir.
          </p>
        </div>
      )}

      {/* Sil Butonu */}
      <button
        onClick={handleSoftDelete}
        disabled={!selectedCustomerId || loading}
        className="w-full bg-red-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-red-700 transition disabled:bg-gray-400 disabled:cursor-not-allowed"
      >
        {loading ? "Siliniyor..." : "Müşteriyi Sil (Soft Delete)"}
      </button>

      {/* Bilgilendirme */}
      <div className="mt-6 p-4 bg-blue-50 border border-blue-200 rounded-lg">
        <h4 className="font-semibold text-blue-800 mb-2">ℹ️ Bilgilendirme</h4>
        <ul className="text-sm text-blue-700 space-y-1 list-disc pl-5">
          <li>Silinen müşteri <strong>silinmiş müşteriler</strong> listesine taşınır</li>
          <li>Müşteri verileri ve dosyaları <strong>korunur</strong></li>
          <li>Silinen müşteri gerektiğinde <strong>geri yüklenebilir</strong></li>
          <li>Müşteriye ait klasör <strong>"silinmis-musteriler"</strong> dizinine taşınır</li>
        </ul>
      </div>
    </div>
  );
}
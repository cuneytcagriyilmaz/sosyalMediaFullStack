// src/components/customerComponents/CustomerRestore.jsx
import { useState, useEffect } from "react";
import customerService from "../../services/customerService";

export default function CustomerRestore() {
  const [deletedCustomers, setDeletedCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Sayfa yüklendiğinde silinmiş müşterileri getir
  useEffect(() => {
    fetchDeletedCustomers();
  }, []);

  const fetchDeletedCustomers = async () => {
    setLoading(true);
    try {
      // Backend'de /customers/deleted endpoint'ini kullan
      const response = await fetch('http://localhost:8080/api/customers/deleted');
      const result = await response.json();
      setDeletedCustomers(result.data);
    } catch (err) {
      console.error("Silinmiş müşteriler yüklenemedi:", err);
      setError("Silinmiş müşteriler yüklenemedi");
    } finally {
      setLoading(false);
    }
  };

  const handleRestore = async (customerId, companyName) => {
    if (!window.confirm(`${companyName} isimli müşteri geri yüklensin mi?`)) {
      return;
    }

    setLoading(true);

    try {
      // Backend'de PUT /customers/{id}/restore endpoint'ini kullan
      await fetch(`http://localhost:8080/api/customers/${customerId}/restore`, {
        method: 'PUT'
      });

      alert("✅ Müşteri başarıyla geri yüklendi!");
      
      // Listeyi güncelle
      fetchDeletedCustomers();

    } catch (err) {
      console.error("Geri yükleme hatası:", err);
      alert("❌ Hata: " + (err.message || "Geri yükleme başarısız"));
    } finally {
      setLoading(false);
    }
  };

  const handleHardDelete = async (customerId, companyName) => {
    if (!window.confirm(`⚠️ DİKKAT! ${companyName} isimli müşteri KALICI olarak silinecek. Bu işlem GERİ ALINAMAZ! Onaylıyor musunuz?`)) {
      return;
    }

    // İkinci onay
    if (!window.confirm(`Son kez soruyoruz: ${companyName} VE TÜM VERİLERİ kalıcı olarak silinsin mi?`)) {
      return;
    }

    setLoading(true);

    try {
      // Backend'de DELETE /customers/{id}/hard endpoint'ini kullan
      await fetch(`http://localhost:8080/api/customers/${customerId}/hard`, {
        method: 'DELETE'
      });

      alert("✅ Müşteri kalıcı olarak silindi!");
      
      // Listeyi güncelle
      fetchDeletedCustomers();

    } catch (err) {
      console.error("Kalıcı silme hatası:", err);
      alert("❌ Hata: " + (err.message || "Silme başarısız"));
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
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-700">🗑️ Silinmiş Müşteriler</h2>
        <button
          onClick={fetchDeletedCustomers}
          disabled={loading}
          className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition disabled:bg-gray-400"
        >
          🔄 Yenile
        </button>
      </div>

      {loading && (
        <div className="text-center py-8">
          <span className="text-indigo-600">Yükleniyor...</span>
        </div>
      )}

      {!loading && deletedCustomers.length === 0 && (
        <div className="bg-gray-50 border rounded-lg p-8 text-center">
          <p className="text-gray-500 text-lg">✅ Silinmiş müşteri bulunmuyor</p>
        </div>
      )}

      {!loading && deletedCustomers.length > 0 && (
        <div className="space-y-4">
          {deletedCustomers.map((customer) => (
            <div key={customer.id} className="bg-white border rounded-lg shadow-md p-4">
              <div className="flex justify-between items-start">
                {/* Müşteri Bilgileri */}
                <div className="flex-1">
                  <h3 className="text-lg font-semibold text-gray-800 mb-2">
                    {customer.companyName}
                  </h3>
                  
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-2 text-sm text-gray-600">
                    <p><strong>Sektör:</strong> {customer.sector}</p>
                    <p><strong>Üyelik:</strong> {customer.membershipPackage}</p>
                    <p><strong>Adres:</strong> {customer.address}</p>
                    <p><strong>Durum:</strong> <span className="text-red-600 font-medium">Silinmiş</span></p>
                  </div>

                  {customer.contacts && customer.contacts.length > 0 && (
                    <p className="text-sm text-gray-500 mt-2">
                      📞 {customer.contacts.length} yetkili kişi
                    </p>
                  )}

                  {customer.media && customer.media.length > 0 && (
                    <p className="text-sm text-gray-500">
                      🖼️ {customer.media.length} medya dosyası
                    </p>
                  )}

                  <p className="text-xs text-gray-400 mt-2">
                    🗑️ Silinme Tarihi: {customer.deletedAt ? new Date(customer.deletedAt).toLocaleString('tr-TR') : 'N/A'}
                  </p>
                </div>

                {/* Aksiyon Butonları */}
                <div className="flex flex-col space-y-2 ml-4">
                  <button
                    onClick={() => handleRestore(customer.id, customer.companyName)}
                    disabled={loading}
                    className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition text-sm whitespace-nowrap disabled:bg-gray-400"
                  >
                    ♻️ Geri Yükle
                  </button>

                  <button
                    onClick={() => handleHardDelete(customer.id, customer.companyName)}
                    disabled={loading}
                    className="bg-red-700 text-white px-4 py-2 rounded-lg hover:bg-red-800 transition text-sm whitespace-nowrap disabled:bg-gray-400"
                  >
                    🔥 Kalıcı Sil
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Bilgilendirme */}
      <div className="mt-6 p-4 bg-blue-50 border border-blue-200 rounded-lg">
        <h4 className="font-semibold text-blue-800 mb-2">ℹ️ Bilgilendirme</h4>
        <ul className="text-sm text-blue-700 space-y-1 list-disc pl-5">
          <li><strong>Geri Yükle:</strong> Müşteri tekrar aktif hale gelir, tüm verileri geri gelir</li>
          <li><strong>Kalıcı Sil:</strong> Müşteri ve TÜM verileri kalıcı olarak silinir (GERİ ALINAMAZ!)</li>
          <li>Kalıcı silme işleminde müşteri klasörü de tamamen silinir</li>
        </ul>
      </div>
    </div>
  );
}
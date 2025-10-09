import { useState } from "react";
import fakeCustomers from "../../data/fakeCustomers";

export default function CustomerDeleteForm() {
  const [selectedCustomerId, setSelectedCustomerId] = useState("");

  const handleSelectChange = (e) => {
    setSelectedCustomerId(parseInt(e.target.value));
  };

  const handleDelete = () => {
    if (!selectedCustomerId) {
      alert("Önce silinecek müşteriyi seçin!");
      return;
    }

    const customer = fakeCustomers.find(c => c.id === selectedCustomerId);
    if (!customer) return;

    if (window.confirm(`${customer.companyName} isimli müşteri silinsin mi?`)) {
      // Sahte veri listesinden sil
      const index = fakeCustomers.findIndex(c => c.id === selectedCustomerId);
      if (index > -1) {
        fakeCustomers.splice(index, 1);
        alert(`${customer.companyName} başarıyla silindi!`);
        setSelectedCustomerId("");
      }
    }
  };

  const inputClass =
    "w-full mt-1 px-3 py-2 border border-gray-300 rounded-lg bg-white text-black focus:ring-2 focus:ring-orange-500 focus:border-orange-500";

  return (
    <div className="max-w-3xl mx-auto p-6 bg-white rounded-xl shadow-lg">
      <h2 className="text-2xl font-bold text-red-600 mb-6">❌ Müşteri Sil</h2>

      <div className="relative mb-4">
        <label className="block text-sm font-medium text-gray-700">Müşteri Seçiniz</label>
        <select
          value={selectedCustomerId}
          onChange={handleSelectChange}
          className={inputClass}
        >
          <option value="">Seçiniz</option>
          {fakeCustomers.map(c => (
            <option key={c.id} value={c.id}>{c.companyName}</option>
          ))}
        </select>
      </div>

      <button
        onClick={handleDelete}
        className="w-full bg-red-600 text-white py-2 px-4 rounded-lg font-medium hover:bg-red-700 transition"
      >
        Müşteriyi Sil
      </button>
    </div>
  );
}

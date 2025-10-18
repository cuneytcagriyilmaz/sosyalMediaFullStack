// src/shared/layouts/AppLayout/AppSidebar.jsx

import { useState } from "react";

const SINGLE_ITEMS = [
  { id: "silinimisMusteriler", label: "Geri Dönüşüm Kutusu", icon: "🗑️" },
  { id: "medyaYonetimi", label: "Medya Yönetimi", icon: "🖼️" },
  { id: "takvim", label: "Takvim", icon: "📅" },
  { id: "post", label: "Postlar", icon: "📝" },
  { id: "ayarlar", label: "Ayarlar", icon: "⚙️" }
];

export default function AppSidebar({ activeMenu, onMenuChange }) {
  const [customerMenuOpen, setCustomerMenuOpen] = useState(true);
  const [customerCRUDOpen, setCustomerCRUDOpen] = useState(false);

  return (
    <aside className="w-64 bg-white border-r shadow-md p-4 hidden md:block">
      <nav className="space-y-2">
        {/* Müşteri İşlemleri Dropdown */}
        <div>
          <button
            onClick={() => setCustomerMenuOpen(!customerMenuOpen)}
            className="flex items-center w-full p-2 rounded-lg hover:bg-indigo-50 transition text-gray-800"
          >
            <span className="text-lg mr-2">👥</span> Müşteri İşlemleri
            <span className="ml-auto">{customerMenuOpen ? "▲" : "▼"}</span>
          </button>

          {customerMenuOpen && (
            <div className="pl-4 mt-1 space-y-1">
              <button
                onClick={() => onMenuChange("musteriGoruntule")}
                className={`block p-2 rounded-lg hover:bg-indigo-100 w-full text-left ${
                  activeMenu === "musteriGoruntule" ? "bg-indigo-100 text-indigo-700 font-medium" : "text-gray-700"
                }`}
              >
                Müşteri Görüntüleme
              </button>

              {/* CRUD İşlemleri Dropdown */}
              <div>
                <button
                  onClick={() => setCustomerCRUDOpen(!customerCRUDOpen)}
                  className="block p-2 rounded-lg hover:bg-indigo-100 text-gray-700 w-full text-left"
                >
                  CRUD İşlemleri {customerCRUDOpen ? "▲" : "▼"}
                </button>

                {customerCRUDOpen && (
                  <div className="pl-4 mt-1 space-y-1">
                    <button
                      onClick={() => onMenuChange("musteriEkle")}
                      className={`block p-2 rounded-lg hover:bg-indigo-200 w-full text-left ${
                        activeMenu === "musteriEkle" ? "bg-indigo-200 text-indigo-700 font-medium" : "text-gray-700"
                      }`}
                    >
                      Müşteri Ekle
                    </button>
                    <button
                      onClick={() => onMenuChange("musteriSil")}
                      className={`block p-2 rounded-lg hover:bg-indigo-200 w-full text-left ${
                        activeMenu === "musteriSil" ? "bg-indigo-200 text-indigo-700 font-medium" : "text-gray-700"
                      }`}
                    >
                      Müşteri Sil
                    </button>
                    <button
                      onClick={() => onMenuChange("musteriGuncelle")}
                      className={`block p-2 rounded-lg hover:bg-indigo-200 w-full text-left ${
                        activeMenu === "musteriGuncelle" ? "bg-indigo-200 text-indigo-700 font-medium" : "text-gray-700"
                      }`}
                    >
                      Müşteri Güncelle
                    </button>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>

        {/* Diğer Menü Öğeleri */}
        {SINGLE_ITEMS.map((item) => (
          <button
            key={item.id}
            onClick={() => onMenuChange(item.id)}
            className={`flex items-center w-full p-2 rounded-lg hover:bg-indigo-50 transition ${
              activeMenu === item.id ? "bg-indigo-100 text-indigo-700 font-medium" : "text-gray-800"
            }`}
          >
            <span className="text-lg mr-2">{item.icon}</span> {item.label}
          </button>
        ))}
      </nav>
    </aside>
  );
}
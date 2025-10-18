// Sidebar.jsx
import { useState } from "react";

export default function Sidebar({ activeMenu, setActiveMenu }) {
  const [customerMenuOpen, setCustomerMenuOpen] = useState(false);
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
                onClick={() => setActiveMenu("musteriGoruntule")}
                className="block p-2 rounded-lg hover:bg-indigo-100 text-gray-700 w-full text-left"
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
                      onClick={() => setActiveMenu("musteriEkle")}
                      className="block p-2 rounded-lg hover:bg-indigo-200 text-gray-700 w-full text-left"
                    >
                      Müşteri Ekle
                    </button>
                    <button
                      onClick={() => setActiveMenu("musteriSil")}
                      className="block p-2 rounded-lg hover:bg-indigo-200 text-gray-700 w-full text-left"
                    >
                      Müşteri Sil
                    </button>
                    <button
                      onClick={() => setActiveMenu("musteriGuncelle")}
                      className="block p-2 rounded-lg hover:bg-indigo-200 text-gray-700 w-full text-left"
                    >
                      Müşteri Güncelle
                    </button>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>

        <button
          onClick={() => setActiveMenu("silinimisMusteriler")}
          className="block p-2 rounded-lg hover:bg-indigo-200 text-gray-700 w-full text-left"
        >
          Silinmiş Müşteriler
        </button>

        <button
          onClick={() => setActiveMenu("medyaYonetimi")}
          className="block p-2 rounded-lg hover:bg-indigo-200 text-gray-700 w-full text-left"
        >
          Medya Yönetimi
        </button>
        <button
          onClick={() => setActiveMenu("takvim")}
          className="flex items-center w-full p-2 rounded-lg hover:bg-indigo-50 transition text-gray-800"
        >
          <span className="text-lg mr-2">📅</span> Takvim
        </button>

        <button
          onClick={() => setActiveMenu("post")}
          className="flex items-center w-full p-2 rounded-lg hover:bg-indigo-50 transition text-gray-800"
        >
          <span className="text-lg mr-2">📝</span> Postlar
        </button>


        <button
          onClick={() => setActiveMenu("ayarlar")}
          className="flex items-center w-full p-2 rounded-lg hover:bg-indigo-50 transition text-gray-800"
        >
          <span className="text-lg mr-2">⚙️</span> Ayarlar
        </button>
      </nav>
    </aside>
  );
}

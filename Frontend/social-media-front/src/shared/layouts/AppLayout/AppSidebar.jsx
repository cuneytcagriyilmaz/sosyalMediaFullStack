// src/shared/layouts/AppLayout/AppSidebar.jsx

import { useState, useEffect } from "react";

export default function AppSidebar({ activeMenu, onMenuChange, isOpen, onClose }) {
  const [customerMenuOpen, setCustomerMenuOpen] = useState(true);
  const [dashboardMenuOpen, setDashboardMenuOpen] = useState(true);

  useEffect(() => {
    const handleEsc = (e) => {
      if (e.key === 'Escape' && isOpen) {
        onClose();
      }
    };
    window.addEventListener('keydown', handleEsc);
    return () => window.removeEventListener('keydown', handleEsc);
  }, [isOpen, onClose]);

  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'unset';
    }
    return () => {
      document.body.style.overflow = 'unset';
    };
  }, [isOpen]);

  return (
    <>
      {isOpen && (
        <div
          className="fixed inset-0 bg-black/30 backdrop-blur-sm z-30 md:hidden animate-fade-in"
          onClick={onClose}
        />
      )}

      <aside
        className={`
          fixed md:relative top-0 left-0 
          h-screen md:min-h-full
          w-64 bg-white border-r shadow-md z-40
          flex flex-col
          transition-transform duration-300 ease-in-out
          md:transition-none
          ${isOpen ? 'translate-x-0' : '-translate-x-full md:translate-x-0'}
        `}
      >
        <div className="md:hidden flex justify-between items-center p-4 pb-4 border-b flex-shrink-0">
          <h2 className="font-bold text-gray-800">Menü</h2>
          <button
            onClick={onClose}
            className="p-2 hover:bg-gray-100 rounded-lg transition"
            aria-label="Menüyü Kapat"
          >
            <svg
              className="w-5 h-5 text-gray-600"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>

        <nav className="flex-1 overflow-y-auto p-4 space-y-2">
          {/* ============================================ */}
          {/* 📊 DASHBOARD (Alt Menü ile) */}
          {/* ============================================ */}
          <div>
            <button
              onClick={() => setDashboardMenuOpen(!dashboardMenuOpen)}
              className="flex items-center justify-between w-full p-3 rounded-lg hover:bg-indigo-50 transition-all duration-200 text-gray-800 font-medium hover:scale-[1.01]"
            >
              <div className="flex items-center">
                <span className="text-lg mr-3">📊</span>
                <span>Dashboard</span>
              </div>
              <span className={`text-gray-500 transition-transform duration-200 ${dashboardMenuOpen ? 'rotate-180' : ''}`}>
                ▼
              </span>
            </button>

            {/* Dashboard Alt Menüsü */}
            <div
              className={`
                ml-4 border-l-2 border-indigo-100 pl-3 space-y-1
                transition-all duration-300 ease-in-out origin-top
                ${dashboardMenuOpen ? 'mt-1 max-h-96 opacity-100' : 'max-h-0 opacity-0 overflow-hidden'}
              `}
            >
              {/* Genel Bakış */}
              <button
                onClick={() => onMenuChange("dashboard")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "dashboard"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">📈</span>
                <span>Genel Bakış</span>
              </button>

              {/* ✅ YENİ: Süreç Yönetimi (eski Müşteri Analizi) */}
              <button
                onClick={() => onMenuChange("surecYonetimi")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "surecYonetimi"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">📋</span>
                <span>Süreç Yönetimi</span>
              </button>

              {/* AI İçerik Takibi */}
              <button
                onClick={() => onMenuChange("aiTaskList")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "aiTaskList"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">🤖</span>
                <span>AI İçerik Takibi</span>
              </button>

              {/* Onboarding Takibi */}
              <button
                onClick={() => onMenuChange("onboardingList")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "onboardingList"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">🚀</span>
                <span>Onboarding Takibi</span>
              </button>

              {/* Aktivite Geçmişi */}
              <button
                onClick={() => onMenuChange("activityHistory")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "activityHistory"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">📜</span>
                <span>Aktivite Geçmişi</span>
              </button>
            </div>
          </div>

          {/* ============================================ */}
          {/* 👥 MÜŞTERİ YÖNETİMİ */}
          {/* ============================================ */}
          <div className="pt-2">
            <button
              onClick={() => setCustomerMenuOpen(!customerMenuOpen)}
              className="flex items-center justify-between w-full p-3 rounded-lg hover:bg-indigo-50 transition-all duration-200 text-gray-800 font-medium hover:scale-[1.01]"
            >
              <div className="flex items-center">
                <span className="text-lg mr-3">👥</span>
                <span>Müşteri Yönetimi</span>
              </div>
              <span className={`text-gray-500 transition-transform duration-200 ${customerMenuOpen ? 'rotate-180' : ''}`}>
                ▼
              </span>
            </button>

            <div
              className={`
                ml-4 border-l-2 border-indigo-100 pl-3 space-y-1
                transition-all duration-300 ease-in-out origin-top
                ${customerMenuOpen ? 'mt-1 max-h-96 opacity-100' : 'max-h-0 opacity-0 overflow-hidden'}
              `}
            >
              <button
                onClick={() => onMenuChange("musteriListesi")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "musteriListesi"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">📋</span>
                <span>Müşteri Listesi</span>
              </button>

              <button
                onClick={() => onMenuChange("musteriGoruntule")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "musteriGoruntule"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">👁️</span>
                <span>Müşteri Detayları</span>
              </button>

              <button
                onClick={() => onMenuChange("musteriEkle")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "musteriEkle"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">➕</span>
                <span>Müşteri Ekle</span>
              </button>

              <button
                onClick={() => onMenuChange("musteriGuncelle")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "musteriGuncelle"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">✏️</span>
                <span>Müşteri Düzenle</span>
              </button>

              <button
                onClick={() => onMenuChange("musteriSil")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "musteriSil"
                    ? "bg-red-100 text-red-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-red-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">🗑️</span>
                <span>Müşteri Sil</span>
              </button>

              <button
                onClick={() => onMenuChange("medyaYonetimi")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "medyaYonetimi"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">🖼️</span>
                <span>Medya Yönetimi</span>
              </button>

              <button
                onClick={() => onMenuChange("silinimisMusteriler")}
                className={`flex items-center w-full p-2 rounded-lg transition-all duration-200 text-sm ${
                  activeMenu === "silinimisMusteriler"
                    ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                    : "text-gray-700 hover:bg-indigo-50 hover:scale-[1.01]"
                }`}
              >
                <span className="text-base mr-2">♻️</span>
                <span>Geri Dönüşüm Kutusu</span>
              </button>
            </div>
          </div>

          {/* ============================================ */}
          {/* 📝 İÇERİK YÖNETİMİ */}
          {/* ============================================ */}
          <div className="pt-4 border-t border-gray-200">
            <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider px-3 mb-2">
              İçerik Yönetimi
            </p>

            <button
              onClick={() => onMenuChange("post")}
              className={`flex items-center w-full p-3 rounded-lg transition-all duration-200 ${
                activeMenu === "post"
                  ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                  : "text-gray-800 hover:bg-indigo-50 hover:scale-[1.01]"
              }`}
            >
              <span className="text-lg mr-3">📝</span>
              <span>Postlar</span>
            </button>

            <button
              onClick={() => onMenuChange("takvim")}
              className={`flex items-center w-full p-3 rounded-lg transition-all duration-200 ${
                activeMenu === "takvim"
                  ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                  : "text-gray-800 hover:bg-indigo-50 hover:scale-[1.01]"
              }`}
            >
              <span className="text-lg mr-3">📅</span>
              <span>Takvim</span>
            </button>
          </div>

          {/* ============================================ */}
          {/* ⚙️ AYARLAR */}
          {/* ============================================ */}
          <div className="pt-4 border-t border-gray-200">
            <button
              onClick={() => onMenuChange("ayarlar")}
              className={`flex items-center w-full p-3 rounded-lg transition-all duration-200 ${
                activeMenu === "ayarlar"
                  ? "bg-indigo-100 text-indigo-700 font-medium transform scale-[1.02]"
                  : "text-gray-800 hover:bg-indigo-50 hover:scale-[1.01]"
              }`}
            >
              <span className="text-lg mr-3">⚙️</span>
              <span>Ayarlar</span>
            </button>
          </div>
        </nav>

        <div className="flex-shrink-0 h-4 bg-white"></div>
      </aside>
    </>
  );
}
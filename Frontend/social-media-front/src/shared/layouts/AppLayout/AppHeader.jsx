// src/shared/layouts/AppLayout/AppHeader.jsx

import LogoutButton from "../../../modules/auth-service/components/LogoutButton";

export default function AppHeader({ onToggleSidebar }) {
  return (
    <header className="bg-gradient-to-r from-indigo-600 to-purple-600 text-white p-4 shadow flex justify-between items-center sticky top-0 z-40">
      <div className="flex items-center gap-4">
        {/* Hamburger Menü - Mobilde görünür */}
        <button
          onClick={onToggleSidebar}
          className="md:hidden p-2 hover:bg-white/10 rounded-lg transition"
          aria-label="Menüyü Aç"
        >
          <svg 
            className="w-6 h-6" 
            fill="none" 
            stroke="currentColor" 
            viewBox="0 0 24 24"
          >
            <path 
              strokeLinecap="round" 
              strokeLinejoin="round" 
              strokeWidth={2} 
              d="M4 6h16M4 12h16M4 18h16" 
            />
          </svg>
        </button>

        <h1 className="text-xl font-bold tracking-wide">📊 SosyalMedyaApp</h1>
      </div>

      <div className="flex items-center space-x-4">
        <span className="hidden sm:block">Hoşgeldin, Çağrı</span>
        <LogoutButton />
      </div>
    </header>
  );
}
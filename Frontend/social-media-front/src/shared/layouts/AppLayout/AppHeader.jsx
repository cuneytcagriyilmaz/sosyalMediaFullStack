// src/shared/layouts/AppLayout/AppHeader.jsx
import LogoutButton from "../../../modules/auth-service/components/LogoutButton";

export default function AppHeader() {
  return (
    <header className="bg-gradient-to-r from-indigo-600 to-purple-600 text-white p-4 shadow flex justify-between items-center">
      <h1 className="text-xl font-bold tracking-wide">📊 SosyalMedyaApp</h1>
      <div className="flex items-center space-x-4">
        <span className="hidden sm:block">Hoşgeldin, Çağrı</span>
        <LogoutButton />
      </div>
    </header>
  );
}
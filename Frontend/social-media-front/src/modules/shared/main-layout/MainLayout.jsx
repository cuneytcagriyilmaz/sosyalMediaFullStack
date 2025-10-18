import { useState } from "react";
import Sidebar from "../main-layout/components/Sidebar";
import MainContent from "../main-layout/components/MainContent";
import LogoutButton from "../../auth-service/components/LogoutButton";

export default function MainLayout() {
  const [activeMenu, setActiveMenu] = useState("takvim");

  return (
    <div className="min-h-screen flex flex-col">
      {/* Header */}
      <header className="bg-gradient-to-r from-indigo-600 to-purple-600 text-white p-4 shadow flex justify-between items-center">
        <h1 className="text-xl font-bold tracking-wide">📊 SosyalMedyaApp</h1>
        <div className="flex items-center space-x-4">
          <span className="hidden sm:block">Hoşgeldin, Çağrı</span>
          <LogoutButton />
        </div>
      </header>

      <div className="flex flex-1">
        <Sidebar activeMenu={activeMenu} setActiveMenu={setActiveMenu} />
        <MainContent activeMenu={activeMenu} />
      </div>
    </div>
  );
}

// src/shared/layouts/AppLayout/AppLayout.jsx

import { useState } from "react";
import AppHeader from "./AppHeader";
import AppSidebar from "./AppSidebar";
import AppContent from "./AppContent";

export default function AppLayout() {
  const [activeMenu, setActiveMenu] = useState("anasayfa");
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleMenuChange = (menu) => {
    setActiveMenu(menu);
    setSidebarOpen(false); // Mobilde menü kapansın
  };

  return (
    <div className="min-h-screen flex flex-col">
      <AppHeader 
        onToggleSidebar={() => setSidebarOpen(!sidebarOpen)} 
      />
      
      <div className="flex flex-1 overflow-hidden relative bg-gray-50 min-h-0">
        <AppSidebar 
          activeMenu={activeMenu} 
          onMenuChange={handleMenuChange}
          isOpen={sidebarOpen}
          onClose={() => setSidebarOpen(false)}
        />
        <AppContent activeMenu={activeMenu} onNavigate={setActiveMenu} />
      </div>
    </div>
  );
}
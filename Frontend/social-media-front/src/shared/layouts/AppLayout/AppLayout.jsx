// src/shared/layouts/AppLayout/AppLayout.jsx

import { useState } from "react";
import AppHeader from "./AppHeader";
import AppSidebar from "./AppSidebar";
import AppContent from "./AppContent";

export default function AppLayout() {
  const [activeMenu, setActiveMenu] = useState("takvim");

  return (
    <div className="min-h-screen flex flex-col">
      <AppHeader />
      
      <div className="flex flex-1">
        <AppSidebar 
          activeMenu={activeMenu} 
          onMenuChange={setActiveMenu} 
        />
        <AppContent activeMenu={activeMenu} />
      </div>
    </div>
  );
}
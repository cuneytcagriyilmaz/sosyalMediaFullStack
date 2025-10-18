// src/App.jsx

import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./modules/auth-service/pages/LoginPage";
import AppLayout from "./shared/layouts/AppLayout/AppLayout";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Login ekranı */}
        <Route path="/login" element={<LoginPage />} />

        {/* Ana uygulama */}
        <Route path="/*" element={<AppLayout />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
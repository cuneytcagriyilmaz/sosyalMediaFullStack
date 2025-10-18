// src/App.jsx

import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
 import LoginPage from "./modules/auth-service/pages/LoginPage";
import AppLayout from "./shared/layouts/AppLayout/AppLayout";
import { AuthProvider } from "./modules/auth-service/context/AuthContext";
import ProtectedRoute from "./modules/auth-service/context/ProtectedRoute";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Ana sayfa - Login'e yönlendir */}
          <Route path="/" element={<Navigate to="/login" replace />} />

          {/* Login ekranı - Public */}
          <Route path="/login" element={<LoginPage />} />

          {/* Uygulama içi sayfalar - Protected */}
          <Route 
            path="/*" 
            element={
              <ProtectedRoute>
                <AppLayout />
              </ProtectedRoute>
            } 
          />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
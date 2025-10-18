// src/App.jsx

import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./modules/auth-service/context/AuthContext";
 
import { ToastContainer } from "./shared/components/Toast";
import ProtectedRoute from "./modules/auth-service/context/ProtectedRoute";
import LoginPage from "./modules/auth-service/pages/LoginPage";
import AppLayout from "./shared/layouts/AppLayout/AppLayout";
import { ToastProvider } from "./shared/context/ToastContext";
import { ModalProvider } from "./shared/context/ModalContext";

function App() {
  return (
    <AuthProvider>
      <ToastProvider>
        <ModalProvider>
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

            {/* Toast Container - Global */}
            <ToastContainer />
          </BrowserRouter>
        </ModalProvider>
      </ToastProvider>
    </AuthProvider>
  );
}

export default App;
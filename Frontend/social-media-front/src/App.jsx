import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./modules/auth-service/pages/LoginPage";
import MainLayout from "./modules/shared/main-layout/MainLayout";



function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Login ekranı */}
        <Route path="/" element={<LoginPage  />} />

        {/* Dashboard ekranı */}
        <Route path="/dashboard" element={<MainLayout />} />

        {/* 404 fallback */}
        <Route
          path="*"
          element={
            <h1 className="text-center mt-20 text-xl text-red-500">
              Sayfa bulunamadı
            </h1>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

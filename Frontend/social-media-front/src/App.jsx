import { BrowserRouter, Routes, Route } from "react-router-dom";
 import LoginForm from "./components/authComponents/LoginForm";
import Dashboard from "./pages/Dashboard";


function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Login ekranı */}
        <Route path="/" element={<LoginForm  />} />

        {/* Dashboard ekranı */}
        <Route path="/dashboard" element={<Dashboard />} />

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

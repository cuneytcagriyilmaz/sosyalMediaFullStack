import { useNavigate } from "react-router-dom";

export default function LogoutButton() {
  const navigate = useNavigate();

  const handleLogout = () => {
    // Eğer localStorage veya sessionStorage kullanıyorsan temizle
    // localStorage.removeItem("token");
    // localStorage.removeItem("user");

    navigate("/"); // login sayfasına yönlendir
  };

  return (
    <button
      onClick={handleLogout}
      className="bg-red-500 px-3 py-1 rounded-lg hover:bg-red-600 transition"
    >
      Çıkış
    </button>
  );
}

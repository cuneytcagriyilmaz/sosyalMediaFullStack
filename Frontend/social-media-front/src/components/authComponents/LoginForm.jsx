import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function LoginForm() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        // TODO: Backend API'ye login isteği atılacak
        // Şimdilik sahte kontrol:
        if (email === "cagri@cagri.com" && password === "123456") {
            console.log("Login başarılı!");
            navigate("/dashboard"); // giriş başarılı → dashboard sayfasına git
        } else {
            alert("Hatalı kullanıcı adı veya şifre!");
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100 p-4">
            <div className="w-full max-w-sm bg-white shadow-md rounded-2xl p-6">
                {/* Logo */}
                <div className="text-center mb-6">
                    <h1 className="text-2xl font-bold text-gray-800">SosyalMedyaApp</h1>
                    <p className="text-sm text-gray-500">Şirket hesabınızla giriş yapın</p>
                </div>

                {/* Form */}
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">E-posta</label>
                        <input
                            type="email"
                            className="w-full mt-1 px-3 py-2 border border-gray-300 rounded-lg shadow-sm 
             focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 
             text-sm text-gray-900"
                            placeholder="calisan@sirket.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Şifre</label>
                        <input
                            type="password"
                            className="w-full mt-1 px-3 py-2 border border-gray-300 rounded-lg shadow-sm 
             focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 
             text-sm text-gray-900"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-indigo-600 text-white py-2 px-4 rounded-lg font-medium hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    >
                        Giriş Yap
                    </button>
                </form>
            </div>
        </div>
    );
}

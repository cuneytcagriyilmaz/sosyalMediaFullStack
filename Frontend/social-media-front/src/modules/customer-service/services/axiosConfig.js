// src/services/axiosConfig.js
import axios from 'axios';

// ✅ Axios instance oluştur
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true, // CORS için gerekli
  headers: {
    'Accept': 'application/json',
  },
});

// ✅ Hata yakalama interceptoru
axiosInstance.interceptors.response.use(
  (response) => response, // başarılıysa direkt geri döndür
  (error) => {
    // Konsolda daha okunaklı hata logu
    if (error.response) {
      console.error(
        `API Error [${error.response.status}]:`,
        error.response.data?.message || error.response.data || error.message
      );
    } else if (error.request) {
      console.error('API Error: Sunucu yanıt vermedi.', error.message);
    } else {
      console.error('API Error:', error.message);
    }

    // Hata fırlat — service tarafı  try/catch ile yakalar
    return Promise.reject(error);
  }
);

export default axiosInstance;

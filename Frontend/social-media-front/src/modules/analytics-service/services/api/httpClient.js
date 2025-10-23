// src/modules/analytics-service/services/api/httpClient.js

import axios from 'axios';
import API_BASE_URL from './config';

const httpClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// ==================== REQUEST INTERCEPTOR ====================
httpClient.interceptors.request.use(
  (config) => {
    // İleride JWT token eklenebilir
    // const token = localStorage.getItem('authToken');
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`;
    // }
    
    console.log('🔄 API Request:', config.method.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    console.error('❌ Request Error:', error);
    return Promise.reject(error);
  }
);

// ==================== RESPONSE INTERCEPTOR ====================
httpClient.interceptors.response.use(
  (response) => {
    console.log('✅ API Response:', response.status, response.config.url);
    
    // Backend ApiResponse formatı: { success, message, data, timestamp }
    if (response.data && response.data.success !== undefined) {
      // Backend response'u standart formata dönüştür
      return {
        success: response.data.success,
        data: response.data.data,
        message: response.data.message,
        timestamp: response.data.timestamp
      };
    }
    
    // Eğer farklı format gelirse olduğu gibi dön
    return response.data;
  },
  (error) => {
    console.error('❌ API Error:', error);
    
    // Hata yönetimi
    if (error.response) {
      // Backend'den gelen hata
      const { status, data } = error.response;
      
      let errorMessage = 'Bir hata oluştu';
      
      switch (status) {
        case 400:
          errorMessage = data.message || 'Geçersiz istek';
          break;
        case 404:
          errorMessage = data.message || 'Kayıt bulunamadı';
          break;
        case 500:
          errorMessage = 'Sunucu hatası. Lütfen daha sonra tekrar deneyin.';
          break;
        case 503:
          errorMessage = 'Servis şu anda kullanılamıyor';
          break;
        default:
          errorMessage = data.message || 'Bir hata oluştu';
      }
      
      return Promise.reject({
        success: false,
        error: errorMessage,
        status: status,
        data: data
      });
      
    } else if (error.request) {
      // İstek gönderildi ama yanıt alınamadı
      return Promise.reject({
        success: false,
        error: 'Sunucuya ulaşılamıyor. İnternet bağlantınızı kontrol edin.'
      });
      
    } else {
      // İstek oluşturulurken hata
      return Promise.reject({
        success: false,
        error: error.message || 'Beklenmeyen bir hata oluştu'
      });
    }
  }
);

export default httpClient;
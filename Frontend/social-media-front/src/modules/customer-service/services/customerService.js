// src/services/customerService.js

import axios from './axiosConfig';

const customerService = {
  // ✅ Tüm müşterileri getir (İYİLEŞTİRİLMİŞ)
  getAllCustomers: async () => {
    try {
      const response = await axios.get('/customers');
      
      console.log('🔍 Raw axios response:', response);
      console.log('🔍 Response data:', response.data);
      
      // Backend'den gelen data yapısı
      let customers = [];
      
      // Format 1: { data: [...] }
      if (response.data && Array.isArray(response.data.data)) {
        customers = response.data.data;
      }
      // Format 2: { data: { customers: [...] } }
      else if (response.data && response.data.data && Array.isArray(response.data.data.customers)) {
        customers = response.data.data.customers;
      }
      // Format 3: Direct array
      else if (Array.isArray(response.data)) {
        customers = response.data;
      }
      // Format 4: { success: true, data: [...] }
      else if (response.data && response.data.success && Array.isArray(response.data.data)) {
        customers = response.data.data;
      }
      
      console.log('✅ Parsed customers:', customers.length, 'items');
      
      return {
        success: true,
        data: customers
      };
    } catch (error) {
      console.error('❌ getAllCustomers error:', error);
      return {
        success: false,
        data: [],
        error: error.response?.data?.message || 'Müşteriler getirilemedi'
      };
    }
  },

  // ID ile müşteri getir
  getCustomerById: async (id) => {
    try {
      const response = await axios.get(`/customers/${id}`);
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ getCustomerById error:', error);
      return {
        success: false,
        data: null,
        error: error.response?.data?.message || 'Müşteri bulunamadı'
      };
    }
  },

  // Yeni müşteri oluştur
  createCustomer: async (customerData) => {
    try {
      const response = await axios.post('/customers', customerData);
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ createCustomer error:', error);
      return {
        success: false,
        data: null,
        error: error.response?.data?.message || 'Müşteri oluşturulamadı'
      };
    }
  },

  // Müşteri güncelle
  updateCustomer: async (id, customerData) => {
    try {
      const response = await axios.patch(`/customers/${id}`, customerData);
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ updateCustomer error:', error);
      return {
        success: false,
        data: null,
        error: error.response?.data?.message || 'Müşteri güncellenemedi'
      };
    }
  },

  // Tam güncelleme (PUT - contacts'ları replace eder)
  fullUpdateCustomer: async (id, customerData) => {
    try {
      const response = await axios.put(`/customers/${id}`, customerData);
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ fullUpdateCustomer error:', error);
      return {
        success: false,
        data: null,
        error: error.response?.data?.message || 'Müşteri güncellenemedi'
      };
    }
  },

  // Müşteri sil (soft delete)
  deleteCustomer: async (id) => {
    try {
      const response = await axios.delete(`/customers/${id}`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      console.error('❌ deleteCustomer error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'Müşteri silinemedi'
      };
    }
  },

  // Silinmiş müşterileri listele
  getAllDeletedCustomers: async () => {
    try {
      const response = await axios.get('/customers/deleted');
      
      let customers = [];
      if (Array.isArray(response.data.data)) {
        customers = response.data.data;
      } else if (Array.isArray(response.data)) {
        customers = response.data;
      }
      
      return {
        success: true,
        data: customers
      };
    } catch (error) {
      console.error('❌ getAllDeletedCustomers error:', error);
      return {
        success: false,
        data: [],
        error: error.response?.data?.message || 'Silinmiş müşteriler getirilemedi'
      };
    }
  },

  // Müşteriyi geri yükle
  restoreCustomer: async (id) => {
    try {
      const response = await axios.put(`/customers/${id}/restore`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      console.error('❌ restoreCustomer error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'Müşteri geri yüklenemedi'
      };
    }
  },

  // Müşteriyi kalıcı sil (hard delete)
  hardDeleteCustomer: async (id) => {
    try {
      const response = await axios.delete(`/customers/${id}/hard`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      console.error('❌ hardDeleteCustomer error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'Müşteri kalıcı olarak silinemedi'
      };
    }
  },

  // Tek media yükle
  uploadMedia: async (customerId, file, mediaType) => {
    try {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('mediaType', mediaType);

      const response = await axios.post(
        `/customers/${customerId}/media/upload`,
        formData,
        { headers: { 'Content-Type': 'multipart/form-data' } }
      );
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ uploadMedia error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'Media yüklenemedi'
      };
    }
  },

  // Çoklu media yükle
  uploadMultipleMedia: async (customerId, files, mediaType) => {
    try {
      const formData = new FormData();
      files.forEach(file => formData.append('files', file));
      formData.append('mediaType', mediaType);

      const response = await axios.post(
        `/customers/${customerId}/media/upload-batch`,
        formData,
        { headers: { 'Content-Type': 'multipart/form-data' } }
      );
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ uploadMultipleMedia error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'Medyalar yüklenemedi'
      };
    }
  },

  // Media sil
  deleteMedia: async (customerId, mediaId) => {
    try {
      const response = await axios.delete(`/customers/${customerId}/media/${mediaId}`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      console.error('❌ deleteMedia error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'Media silinemedi'
      };
    }
  },

  // Media listele
  getCustomerMedia: async (customerId) => {
    try {
      const response = await axios.get(`/customers/${customerId}/media`);
      
      let media = [];
      if (Array.isArray(response.data.data)) {
        media = response.data.data;
      } else if (Array.isArray(response.data)) {
        media = response.data;
      }
      
      return {
        success: true,
        data: media
      };
    } catch (error) {
      console.error('❌ getCustomerMedia error:', error);
      return {
        success: false,
        data: [],
        error: error.response?.data?.message || 'Medyalar getirilemedi'
      };
    }
  },

  // Seçili medyaları ZIP olarak indir
  downloadMediaAsZip: async (customerId, mediaIds) => {
    try {
      const response = await axios.post(
        `/customers/${customerId}/media/download-zip`,
        mediaIds,
        {
          responseType: 'blob'
        }
      );
      return response;
    } catch (error) {
      console.error('❌ downloadMediaAsZip error:', error);
      throw error;
    }
  },

  // ========== BÖLÜM BAZLI GÜNCELLEME ==========

  updateBasicInfo: async (customerId, data) => {
    try {
      const response = await axios.put(`/customers/${customerId}/update/basic-info`, data);
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ updateBasicInfo error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'Temel bilgiler güncellenemedi'
      };
    }
  },

  updateContacts: async (customerId, contacts) => {
    try {
      const response = await axios.put(`/customers/${customerId}/update/contacts`, contacts);
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ updateContacts error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'İletişim bilgileri güncellenemedi'
      };
    }
  },

  updateSocialMedia: async (customerId, socialMedia) => {
    try {
      const response = await axios.put(`/customers/${customerId}/update/social-media`, socialMedia);
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ updateSocialMedia error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'Sosyal medya bilgileri güncellenemedi'
      };
    }
  },

  updateTargetAudience: async (customerId, targetAudience) => {
    try {
      const response = await axios.put(`/customers/${customerId}/update/target-audience`, targetAudience);
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ updateTargetAudience error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'Hedef kitle bilgileri güncellenemedi'
      };
    }
  },

  updateSeo: async (customerId, seo) => {
    try {
      const response = await axios.put(`/customers/${customerId}/update/seo`, seo);
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ updateSeo error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'SEO bilgileri güncellenemedi'
      };
    }
  },

  updateApiKeys: async (customerId, apiKeys) => {
    try {
      const response = await axios.put(`/customers/${customerId}/update/api-keys`, apiKeys);
      return {
        success: true,
        data: response.data.data || response.data
      };
    } catch (error) {
      console.error('❌ updateApiKeys error:', error);
      return {
        success: false,
        error: error.response?.data?.message || 'API anahtarları güncellenemedi'
      };
    }
  }
};

export default customerService;
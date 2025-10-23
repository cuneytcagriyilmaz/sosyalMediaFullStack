// src/services/customerService.js
import axios from './axiosConfig';

const customerService = {
  // ✅ Tüm müşterileri getir (WRAPPER)
  getAllCustomers: async () => {
    try {
      const response = await axios.get('/customers');
      return {
        success: true,
        data: response.data.data || response.data || []
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
    const response = await axios.get(`/customers/${id}`);
    return response.data.data;
  },

  // Yeni müşteri oluştur
  createCustomer: async (customerData) => {
    const response = await axios.post('/customers', customerData);
    return response.data.data;
  },

  // Müşteri güncelle
  updateCustomer: async (id, customerData) => {
    const response = await axios.patch(`/customers/${id}`, customerData);
    return response.data.data;
  },

  // Tam güncelleme (PUT - contacts'ları replace eder)
  fullUpdateCustomer: async (id, customerData) => {
    const response = await axios.put(`/customers/${id}`, customerData);
    return response.data.data;
  },

  // Müşteri sil (soft delete)
  deleteCustomer: async (id) => {
    const response = await axios.delete(`/customers/${id}`);
    return response.data;
  },

  // Silinmiş müşterileri listele
  getAllDeletedCustomers: async () => {
    const response = await axios.get('/customers/deleted');
    return response.data.data;
  },

  // Müşteriyi geri yükle
  restoreCustomer: async (id) => {
    const response = await axios.put(`/customers/${id}/restore`);
    return response.data;
  },

  // Müşteriyi kalıcı sil (hard delete)
  hardDeleteCustomer: async (id) => {
    const response = await axios.delete(`/customers/${id}/hard`);
    return response.data;
  },

  // Tek media yükle
  uploadMedia: async (customerId, file, mediaType) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('mediaType', mediaType);

    const response = await axios.post(
      `/customers/${customerId}/media/upload`,
      formData,
      { headers: { 'Content-Type': 'multipart/form-data' } }
    );
    return response.data.data;
  },

  // Çoklu media yükle
  uploadMultipleMedia: async (customerId, files, mediaType) => {
    const formData = new FormData();
    files.forEach(file => formData.append('files', file));
    formData.append('mediaType', mediaType);

    const response = await axios.post(
      `/customers/${customerId}/media/upload-batch`,
      formData,
      { headers: { 'Content-Type': 'multipart/form-data' } }
    );
    return response.data.data;
  },

  // Media sil
  deleteMedia: async (customerId, mediaId) => {
    const response = await axios.delete(`/customers/${customerId}/media/${mediaId}`);
    return response.data;
  },

  // Media listele
  getCustomerMedia: async (customerId) => {
    const response = await axios.get(`/customers/${customerId}/media`);
    return response.data.data;
  },

  // Seçili medyaları ZIP olarak indir
  downloadMediaAsZip: async (customerId, mediaIds) => {
    const response = await axios.post(
      `/customers/${customerId}/media/download-zip`,
      mediaIds,
      {
        responseType: 'blob'
      }
    );
    return response;
  },

  // ========== BÖLÜM BAZLI GÜNCELLEME ==========

  updateBasicInfo: async (customerId, data) => {
    const response = await axios.put(`/customers/${customerId}/update/basic-info`, data);
    return response.data.data;
  },

  updateContacts: async (customerId, contacts) => {
    const response = await axios.put(`/customers/${customerId}/update/contacts`, contacts);
    return response.data.data;
  },

  updateSocialMedia: async (customerId, socialMedia) => {
    const response = await axios.put(`/customers/${customerId}/update/social-media`, socialMedia);
    return response.data.data;
  },

  updateTargetAudience: async (customerId, targetAudience) => {
    const response = await axios.put(`/customers/${customerId}/update/target-audience`, targetAudience);
    return response.data.data;
  },

  updateSeo: async (customerId, seo) => {
    const response = await axios.put(`/customers/${customerId}/update/seo`, seo);
    return response.data.data;
  },

  updateApiKeys: async (customerId, apiKeys) => {
    const response = await axios.put(`/customers/${customerId}/update/api-keys`, apiKeys);
    return response.data.data;
  }
};

export default customerService;
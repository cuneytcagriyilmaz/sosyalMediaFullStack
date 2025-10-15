// src/services/customerService.js
import axios from './axiosConfig'; // özel axios instance

const customerService = {
  // Tüm müşterileri getir
  getAllCustomers: async () => {
    const response = await axios.get('/customers');
    return response.data.data;
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

  // Müşteri sil (soft delete)
  deleteCustomer: async (id) => {
    const response = await axios.delete(`/customers/${id}`);
    return response.data;
  },

  // Media yükle
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
  }
};

export default customerService;

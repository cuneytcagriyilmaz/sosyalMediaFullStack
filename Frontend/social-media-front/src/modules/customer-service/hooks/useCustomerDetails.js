// src/modules/customer-service/hooks/useCustomerDetails.js

import { useState, useEffect } from 'react';
 import customerService from '../services/customerService';
import { useToast } from '../../../shared/context/ToastContext';
 
export default function useCustomerDetails() {
  const [customers, setCustomers] = useState([]); // ✅ Boş array
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const { toast } = useToast();

  useEffect(() => {
    fetchCustomers();
  }, []);

  // ✅ localStorage'dan customer ID'yi oku ve otomatik seç
  useEffect(() => {
    const storedCustomerId = localStorage.getItem('selectedCustomerId');
    if (storedCustomerId && customers.length > 0) {
      handleSelectCustomer(storedCustomerId);
      // Kullanıldıktan sonra temizle
      localStorage.removeItem('selectedCustomerId');
    }
  }, [customers]);

  const fetchCustomers = async () => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await customerService.getAllCustomers();
      
      console.log('📥 useCustomerDetails response:', response);

      // ✅ Response formatını handle et
      if (response.success && response.data) {
        // Backend: { success: true, data: [...] }
        const customerArray = Array.isArray(response.data) 
          ? response.data 
          : [];
        
        console.log('✅ Setting customers:', customerArray.length);
        setCustomers(customerArray);
      } else if (Array.isArray(response)) {
        // Eski format: direkt array döndürüyorsa
        console.log('✅ Setting customers (old format):', response.length);
        setCustomers(response);
      } else if (Array.isArray(response.data)) {
        // Başka bir format
        console.log('✅ Setting customers (alt format):', response.data.length);
        setCustomers(response.data);
      } else {
        console.warn('⚠️ Unexpected response format:', response);
        setCustomers([]);
        setError("Müşteri listesi formatı beklenmedik");
        toast.error("Müşteri listesi yüklenemedi!");
      }
    } catch (err) {
      console.error("❌ Müşteriler yüklenemedi:", err);
      setCustomers([]); // ✅ Hata durumunda boş array
      setError("Müşteriler yüklenemedi");
      toast.error("Müşteriler yüklenemedi!");
    } finally {
      setLoading(false);
    }
  };

  const handleSelectCustomer = async (customerId) => {
    if (!customerId) {
      setSelectedCustomer(null);
      return;
    }

    setLoading(true);
    setError(null);
    
    try {
      const response = await customerService.getCustomerById(customerId);
      
      console.log('📥 Selected customer response:', response);

      // ✅ Response formatını handle et
      if (response.success && response.data) {
        // Backend: { success: true, data: {...} }
        setSelectedCustomer(response.data);
      } else if (response.id) {
        // Eski format: direkt customer object döndürüyorsa
        setSelectedCustomer(response);
      } else if (response.data && response.data.id) {
        // Başka bir format
        setSelectedCustomer(response.data);
      } else {
        console.warn('⚠️ Unexpected customer format:', response);
        setError("Müşteri detayı yüklenemedi");
        toast.error("Müşteri detayı yüklenemedi!");
      }
    } catch (err) {
      console.error("❌ Müşteri detayı yüklenemedi:", err);
      setError("Müşteri detayı yüklenemedi");
      toast.error("Müşteri detayı yüklenemedi!");
    } finally {
      setLoading(false);
    }
  };

  const handleMediaUpdate = async () => {
    if (selectedCustomer) {
      await handleSelectCustomer(selectedCustomer.id);
    }
  };

  return {
    customers,
    selectedCustomer,
    loading,
    error,
    handleSelectCustomer,
    handleMediaUpdate,
    refresh: fetchCustomers // ✅ Yenileme fonksiyonu
  };
}
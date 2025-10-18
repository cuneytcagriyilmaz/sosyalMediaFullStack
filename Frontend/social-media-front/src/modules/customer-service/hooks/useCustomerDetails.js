import { useState, useEffect } from 'react';
import customerService from '../services/customerService';

export default function useCustomerDetails() {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    setLoading(true);
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (err) {
      console.error("Müşteriler yüklenemedi:", err);
      setError("Müşteriler yüklenemedi");
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
    try {
      const customer = await customerService.getCustomerById(customerId);
      setSelectedCustomer(customer);
    } catch (err) {
      console.error("Müşteri detayı yüklenemedi:", err);
      setError("Müşteri detayı yüklenemedi");
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
    handleMediaUpdate
  };
}
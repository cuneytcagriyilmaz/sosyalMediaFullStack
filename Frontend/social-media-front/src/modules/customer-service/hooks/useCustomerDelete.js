// modules/customer-service/hooks/useCustomerDelete.js
import { useState, useEffect } from 'react';
import customerService from '../services/customerService';

export default function useCustomerDelete() {
  const [customers, setCustomers] = useState([]);
  const [filteredCustomers, setFilteredCustomers] = useState([]);
  const [selectedIds, setSelectedIds] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [notification, setNotification] = useState(null);

  useEffect(() => {
    fetchCustomers();
  }, []);

  useEffect(() => {
    const filtered = customers.filter(customer =>
      customer.companyName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      customer.sector.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredCustomers(filtered);
  }, [searchTerm, customers]);

  const fetchCustomers = async () => {
    setLoading(true);
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
      setFilteredCustomers(data);
    } catch (error) {
      showNotification('Müşteriler yüklenemedi!', 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleSelectAll = (e) => {
    if (e.target.checked) {
      setSelectedIds(filteredCustomers.map(c => c.id));
    } else {
      setSelectedIds([]);
    }
  };

  const handleSelectOne = (id) => {
    setSelectedIds(prev =>
      prev.includes(id)
        ? prev.filter(selectedId => selectedId !== id)
        : [...prev, id]
    );
  };

  const handleDeleteClick = () => {
    if (selectedIds.length === 0) {
      showNotification('Lütfen silinecek müşteri seçin!', 'warning');
      return;
    }
    setShowModal(true);
  };

  const handleConfirmDelete = async () => {
    setShowModal(false);
    setLoading(true);

    try {
      await Promise.all(
        selectedIds.map(id => customerService.deleteCustomer(id))
      );

      showNotification(
        `${selectedIds.length} müşteri başarıyla silindi!`,
        'success'
      );

      setSelectedIds([]);
      await fetchCustomers();
    } catch (error) {
      showNotification('Silme işlemi başarısız!', 'error');
    } finally {
      setLoading(false);
    }
  };

  const showNotification = (message, type) => {
    setNotification({ message, type });
    setTimeout(() => setNotification(null), 4000);
  };

  return {
    customers: filteredCustomers,
    selectedIds,
    searchTerm,
    setSearchTerm,
    loading,
    showModal,
    setShowModal,
    notification,
    handleSelectAll,
    handleSelectOne,
    handleDeleteClick,
    handleConfirmDelete,
    allSelected: selectedIds.length === filteredCustomers.length && filteredCustomers.length > 0
  };
}
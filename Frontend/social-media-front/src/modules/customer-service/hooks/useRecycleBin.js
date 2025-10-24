// src/modules/customer-service/hooks/useRecycleBin.js

import { useState, useEffect } from 'react';
import customerService from '../services/customerService';

export default function useRecycleBin() {
  const [deletedCustomers, setDeletedCustomers] = useState([]);  
  const [filteredCustomers, setFilteredCustomers] = useState([]);  
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const [actionLoading, setActionLoading] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [modalData, setModalData] = useState(null);
  const [notification, setNotification] = useState(null);

  useEffect(() => {
    fetchDeletedCustomers();
  }, []);

  useEffect(() => {
    // ✅ Array check
    if (!Array.isArray(deletedCustomers) || deletedCustomers.length === 0) {
      setFilteredCustomers([]);
      return;
    }

    const filtered = deletedCustomers.filter(customer => {
      const companyName = customer.companyName || customer.company_name || '';
      const sector = customer.sector || '';
      const searchLower = searchTerm.toLowerCase();
      
      return (
        companyName.toLowerCase().includes(searchLower) ||
        sector.toLowerCase().includes(searchLower)
      );
    });
    
    setFilteredCustomers(filtered);
  }, [searchTerm, deletedCustomers]);

  const fetchDeletedCustomers = async () => {
    setLoading(true);
    try {
      const response = await customerService.getAllDeletedCustomers();
      
      console.log('📥 useRecycleBin response:', response);

      // ✅ Response formatını handle et
      if (response.success && response.data) {
        const customerArray = Array.isArray(response.data) 
          ? response.data 
          : [];
        
        console.log('✅ Setting deleted customers:', customerArray.length);
        setDeletedCustomers(customerArray);
        setFilteredCustomers(customerArray);
      } else if (Array.isArray(response)) {
        // Eski format
        console.log('✅ Setting deleted customers (old format):', response.length);
        setDeletedCustomers(response);
        setFilteredCustomers(response);
      } else if (Array.isArray(response.data)) {
        console.log('✅ Setting deleted customers (alt format):', response.data.length);
        setDeletedCustomers(response.data);
        setFilteredCustomers(response.data);
      } else {
        console.warn('⚠️ Unexpected response format:', response);
        setDeletedCustomers([]);
        setFilteredCustomers([]);
        showNotification('Silinmiş müşteriler yüklenemedi!', 'error');
      }
    } catch (error) {
      console.error('❌ Silinmiş müşteriler yüklenemedi:', error);
      setDeletedCustomers([]); // ✅ Hata durumunda boş array
      setFilteredCustomers([]);
      showNotification('Silinmiş müşteriler yüklenemedi!', 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleRestoreClick = (customer) => {
    setModalData({
      type: 'restore',
      customer,
      title: 'Müşteriyi Geri Yükle',
      message: `${customer.companyName || customer.company_name} isimli müşteri geri yüklenecek. Tüm verileri tekrar aktif hale gelecek.`,
      confirmText: 'Geri Yükle',
      confirmColor: 'bg-green-600 hover:bg-green-700'
    });
    setShowModal(true);
  };

  const handleHardDeleteClick = (customer) => {
    setModalData({
      type: 'hardDelete',
      customer,
      title: 'Kalıcı Silme Onayı',
      message: `⚠️ DİKKAT! ${customer.companyName || customer.company_name} ve TÜM VERİLERİ kalıcı olarak silinecek. Bu işlem GERİ ALINAMAZ!`,
      confirmText: 'Kalıcı Sil',
      confirmColor: 'bg-red-700 hover:bg-red-800',
      isDangerous: true
    });
    setShowModal(true);
  };

  const handleConfirmAction = async () => {
    if (!modalData) return;

    setShowModal(false);
    setActionLoading(modalData.customer.id);

    try {
      if (modalData.type === 'restore') {
        const response = await customerService.restoreCustomer(modalData.customer.id);
        
        if (response.success) {
          showNotification(
            `${modalData.customer.companyName || modalData.customer.company_name} başarıyla geri yüklendi!`, 
            'success'
          );
        } else {
          showNotification('Geri yükleme başarısız!', 'error');
        }
      } else if (modalData.type === 'hardDelete') {
        const response = await customerService.hardDeleteCustomer(modalData.customer.id);
        
        if (response.success) {
          showNotification(
            `${modalData.customer.companyName || modalData.customer.company_name} kalıcı olarak silindi!`, 
            'success'
          );
        } else {
          showNotification('Silme işlemi başarısız!', 'error');
        }
      }
      
      await fetchDeletedCustomers();
    } catch (error) {
      console.error('❌ İşlem başarısız:', error);
      const errorMsg = error.response?.data?.message || error.message;
      showNotification(`İşlem başarısız: ${errorMsg}`, 'error');
    } finally {
      setActionLoading(null);
      setModalData(null);
    }
  };

  const showNotification = (message, type) => {
    setNotification({ message, type });
    setTimeout(() => setNotification(null), 4000);
  };

  return {
    customers: filteredCustomers,
    allCustomers: deletedCustomers,
    searchTerm,
    setSearchTerm,
    loading,
    actionLoading,
    showModal,
    setShowModal,
    modalData,
    setModalData,
    notification,
    handleRestoreClick,
    handleHardDeleteClick,
    handleConfirmAction,
    handleRefresh: fetchDeletedCustomers
  };
}
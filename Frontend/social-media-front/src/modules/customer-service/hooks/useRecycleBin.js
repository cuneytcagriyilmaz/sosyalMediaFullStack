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
    const filtered = deletedCustomers.filter(customer =>
      customer.companyName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      customer.sector.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredCustomers(filtered);
  }, [searchTerm, deletedCustomers]);

  const fetchDeletedCustomers = async () => {
    setLoading(true);
    try {
      const data = await customerService.getAllDeletedCustomers();
      setDeletedCustomers(data);
      setFilteredCustomers(data);
    } catch (error) {
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
      message: `${customer.companyName} isimli müşteri geri yüklenecek. Tüm verileri tekrar aktif hale gelecek.`,
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
      message: `⚠️ DİKKAT! ${customer.companyName} ve TÜM VERİLERİ kalıcı olarak silinecek. Bu işlem GERİ ALINAMAZ!`,
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
        await customerService.restoreCustomer(modalData.customer.id);
        showNotification(`${modalData.customer.companyName} başarıyla geri yüklendi!`, 'success');
      } else if (modalData.type === 'hardDelete') {
        await customerService.hardDeleteCustomer(modalData.customer.id);
        showNotification(`${modalData.customer.companyName} kalıcı olarak silindi!`, 'success');
      }
      await fetchDeletedCustomers();
    } catch (error) {
      showNotification('İşlem başarısız!', 'error');
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
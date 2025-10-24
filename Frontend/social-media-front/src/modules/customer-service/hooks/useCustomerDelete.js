// src/modules/customer-service/hooks/useCustomerDelete.js

import { useState, useEffect } from 'react';
 import { useToast } from '../../../shared/context/ToastContext';
import { useModal } from '../../../shared/context/ModalContext';
import customerService from '../services/customerService';

export default function useCustomerDelete() {
  const [customers, setCustomers] = useState([]); // ✅ Boş array
  const [filteredCustomers, setFilteredCustomers] = useState([]); // ✅ Boş array
  const [selectedIds, setSelectedIds] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);

  const { toast } = useToast();
  const { confirm } = useModal();

  useEffect(() => {
    fetchCustomers();
  }, []);

  useEffect(() => {
    // ✅ Array check
    if (!Array.isArray(customers) || customers.length === 0) {
      setFilteredCustomers([]);
      return;
    }

    const filtered = customers.filter(customer => {
      const companyName = customer.companyName || customer.company_name || '';
      const sector = customer.sector || '';
      const searchLower = searchTerm.toLowerCase();
      
      return (
        companyName.toLowerCase().includes(searchLower) ||
        sector.toLowerCase().includes(searchLower)
      );
    });
    
    setFilteredCustomers(filtered);
  }, [searchTerm, customers]);

  const fetchCustomers = async () => {
    setLoading(true);
    try {
      const response = await customerService.getAllCustomers();
      
      console.log('📥 useCustomerDelete response:', response);

      // ✅ Response formatını handle et
      if (response.success && response.data) {
        const customerArray = Array.isArray(response.data) 
          ? response.data 
          : [];
        
        console.log('✅ Setting customers:', customerArray.length);
        setCustomers(customerArray);
        setFilteredCustomers(customerArray);
      } else if (Array.isArray(response)) {
        // Eski format
        console.log('✅ Setting customers (old format):', response.length);
        setCustomers(response);
        setFilteredCustomers(response);
      } else if (Array.isArray(response.data)) {
        console.log('✅ Setting customers (alt format):', response.data.length);
        setCustomers(response.data);
        setFilteredCustomers(response.data);
      } else {
        console.warn('⚠️ Unexpected response format:', response);
        setCustomers([]);
        setFilteredCustomers([]);
        toast.error('Müşteriler yüklenirken bir hata oluştu!');
      }
    } catch (error) {
      console.error('❌ Müşteriler yüklenemedi:', error);
      setCustomers([]); // ✅ Hata durumunda boş array
      setFilteredCustomers([]);
      toast.error('Müşteriler yüklenirken bir hata oluştu!');
    } finally {
      setLoading(false);
    }
  };

  const handleSelectAll = (e) => {
    if (e.target.checked) {
      const ids = Array.isArray(filteredCustomers) 
        ? filteredCustomers.map(c => c.id) 
        : [];
      setSelectedIds(ids);
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

  const handleDeleteClick = async () => {
    if (selectedIds.length === 0) {
      toast.warning('Lütfen en az bir müşteri seçin!');
      return;
    }

    // Modal ile onay al
    await confirm({
      title: 'Müşterileri Sil',
      message: `${selectedIds.length} müşteri silinecek. Bu işlem geri alınabilir (Soft Delete). Devam etmek istiyor musunuz?`,
      confirmText: 'Evet, Sil',
      cancelText: 'İptal',
      type: 'danger',
      onConfirm: async () => {
        await handleConfirmDelete();
      }
    });
  };

  const handleConfirmDelete = async () => {
    setLoading(true);

    try {
      // Tüm seçili müşterileri sil
      const deletePromises = selectedIds.map(id => customerService.deleteCustomer(id));
      await Promise.all(deletePromises);

      // Başarı mesajı
      toast.success(`${selectedIds.length} müşteri başarıyla silindi!`);

      // Seçimleri temizle ve listeyi yenile
      setSelectedIds([]);
      await fetchCustomers();

    } catch (error) {
      console.error('❌ Silme hatası:', error);
      const errorMsg = error.response?.data?.message || error.message;
      toast.error('Silme işlemi başarısız: ' + errorMsg);
    } finally {
      setLoading(false);
    }
  };

  return {
    customers: filteredCustomers,
    allCustomers: customers,
    selectedIds,
    searchTerm,
    setSearchTerm,
    loading,
    handleSelectAll,
    handleSelectOne,
    handleDeleteClick,
    allSelected: selectedIds.length === filteredCustomers.length && filteredCustomers.length > 0,
    refresh: fetchCustomers // ✅ Yenileme fonksiyonu
  };
}
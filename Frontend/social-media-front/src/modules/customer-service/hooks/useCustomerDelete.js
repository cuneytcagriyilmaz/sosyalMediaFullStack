// modules/customer-service/hooks/useCustomerDelete.js
import { useState, useEffect } from 'react';
import { useToast } from '../../../shared/context/ToastContext';
import { useModal } from '../../../shared/context/ModalContext';
import customerService from '../services/customerService';

export default function useCustomerDelete() {
  const [customers, setCustomers] = useState([]);
  const [filteredCustomers, setFilteredCustomers] = useState([]);
  const [selectedIds, setSelectedIds] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);

  const { toast } = useToast();
  const { confirm } = useModal();

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
      console.error('Müşteriler yüklenemedi:', error);
      toast.error('Müşteriler yüklenirken bir hata oluştu!');
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
      await Promise.all(
        selectedIds.map(id => customerService.deleteCustomer(id))
      );

      // Başarı mesajı
      toast.success(`${selectedIds.length} müşteri başarıyla silindi!`);

      // Seçimleri temizle ve listeyi yenile
      setSelectedIds([]);
      await fetchCustomers();

    } catch (error) {
      console.error('Silme hatası:', error);
      const errorMsg = error.response?.data?.message || error.message;
      toast.error('Silme işlemi başarısız: ' + errorMsg);
    } finally {
      setLoading(false);
    }
  };

  return {
    customers: filteredCustomers,
    selectedIds,
    searchTerm,
    setSearchTerm,
    loading,
    handleSelectAll,
    handleSelectOne,
    handleDeleteClick,
    allSelected: selectedIds.length === filteredCustomers.length && filteredCustomers.length > 0
  };
}
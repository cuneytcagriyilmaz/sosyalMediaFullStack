// src/modules/customer-service/components/CustomerList/hooks/useCustomerList.js

import { useState, useEffect } from 'react';
import customerService from '../services/customerService';
 
export default function useCustomerList() {
  const [customers, setCustomers] = useState([]);
  const [filteredCustomers, setFilteredCustomers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [packageFilter, setPackageFilter] = useState('ALL');
  const [sortBy, setSortBy] = useState('name-asc');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchCustomers();
  }, []);

  useEffect(() => {
    filterAndSortCustomers();
  }, [customers, searchTerm, statusFilter, packageFilter, sortBy]);

  const fetchCustomers = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await customerService.getAllCustomers();
      
      console.log('📥 useCustomerList response:', response);

      // ✅ Response formatını kontrol et
      if (response.success && response.data) {
        const customerArray = Array.isArray(response.data) 
          ? response.data 
          : [];
        
        console.log('✅ Setting customers:', customerArray.length);
        setCustomers(customerArray);
      } else {
        console.warn('⚠️ Invalid response format:', response);
        setCustomers([]);
        setError('Müşteriler yüklenemedi');
      }
    } catch (error) {
      console.error('❌ Müşteriler yüklenemedi:', error);
      setCustomers([]);
      setError('Bir hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  const filterAndSortCustomers = () => {
    console.log('🔍 Filtering customers:', customers);

    // ✅ Array kontrolü
    if (!Array.isArray(customers)) {
      console.warn('⚠️ customers is not an array:', customers);
      setFilteredCustomers([]);
      return;
    }

    let filtered = [...customers];

    // Arama
    if (searchTerm) {
      filtered = filtered.filter(c => {
        const companyName = c.companyName || c.company_name || '';
        const sector = c.sector || '';
        const searchLower = searchTerm.toLowerCase();
        
        return (
          companyName.toLowerCase().includes(searchLower) ||
          sector.toLowerCase().includes(searchLower)
        );
      });
    }

    // Status filtresi
    if (statusFilter !== 'ALL') {
      filtered = filtered.filter(c => c.status === statusFilter);
    }

    // Paket filtresi
    if (packageFilter !== 'ALL') {
      filtered = filtered.filter(c => 
        (c.membershipPackage || c.membership_package) === packageFilter
      );
    }

    // Sıralama
    filtered.sort((a, b) => {
      switch (sortBy) {
        case 'name-asc':
          return (a.companyName || a.company_name || '').localeCompare(
            b.companyName || b.company_name || '', 'tr'
          );
        case 'name-desc':
          return (b.companyName || b.company_name || '').localeCompare(
            a.companyName || a.company_name || '', 'tr'
          );
        case 'date-asc':
          return new Date(a.createdAt || a.created_at) - new Date(b.createdAt || b.created_at);
        case 'date-desc':
          return new Date(b.createdAt || b.created_at) - new Date(a.createdAt || a.created_at);
        default:
          return 0;
      }
    });

    console.log('✅ Filtered customers:', filtered.length);
    setFilteredCustomers(filtered);
  };

  return {
    customers: filteredCustomers,
    allCustomers: customers,
    loading,
    error,
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    packageFilter,
    setPackageFilter,
    sortBy,
    setSortBy,
    refresh: fetchCustomers
  };
}
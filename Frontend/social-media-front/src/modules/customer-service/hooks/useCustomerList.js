// src/modules/customer-service/components/CustomerList/hooks/useCustomerList.js

import { useState, useEffect } from 'react';
import customerService from "../services/customerService";

 
export default function useCustomerList() {
  const [customers, setCustomers] = useState([]);
  const [filteredCustomers, setFilteredCustomers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [packageFilter, setPackageFilter] = useState('ALL');
  const [sortBy, setSortBy] = useState('name-asc');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCustomers();
  }, []);

  useEffect(() => {
    filterAndSortCustomers();
  }, [customers, searchTerm, statusFilter, packageFilter, sortBy]);

  const fetchCustomers = async () => {
    setLoading(true);
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (error) {
      console.error('Müşteriler yüklenemedi:', error);
    } finally {
      setLoading(false);
    }
  };

  const filterAndSortCustomers = () => {
    let filtered = [...customers];

    // Arama
    if (searchTerm) {
      filtered = filtered.filter(c =>
        c.companyName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        c.sector.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Status filtresi
    if (statusFilter !== 'ALL') {
      filtered = filtered.filter(c => c.status === statusFilter);
    }

    // Paket filtresi
    if (packageFilter !== 'ALL') {
      filtered = filtered.filter(c => c.membershipPackage === packageFilter);
    }

    // Sıralama
    filtered.sort((a, b) => {
      switch (sortBy) {
        case 'name-asc':
          return a.companyName.localeCompare(b.companyName);
        case 'name-desc':
          return b.companyName.localeCompare(a.companyName);
        case 'date-asc':
          return new Date(a.createdAt) - new Date(b.createdAt);
        case 'date-desc':
          return new Date(b.createdAt) - new Date(a.createdAt);
        default:
          return 0;
      }
    });

    setFilteredCustomers(filtered);
  };

  return {
    customers: filteredCustomers,
    loading,
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    packageFilter,
    setPackageFilter,
    sortBy,
    setSortBy
  };
}
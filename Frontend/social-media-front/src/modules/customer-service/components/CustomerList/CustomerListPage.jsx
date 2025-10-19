// src/modules/customer-service/components/CustomerList/CustomerListPage.jsx

 import useCustomerList from '../../hooks/useCustomerList';
import { Header, Filters, Table, EmptyState } from './components';

export default function CustomerListPage({ onNavigate }) {
  const {
    customers,
    loading,
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    packageFilter,
    setPackageFilter,
    sortBy,
    setSortBy
  } = useCustomerList();

  const hasFilters = searchTerm || statusFilter !== 'ALL' || packageFilter !== 'ALL';

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
      {/* Header Section */}
      <div className="max-w-7xl mx-auto mb-6">
        <div className="bg-white rounded-2xl shadow-lg p-6 md:p-8">
          <Header totalCount={customers.length} />
          <div className="mt-6">
            <Filters
              searchTerm={searchTerm}
              onSearchChange={setSearchTerm}
              statusFilter={statusFilter}
              onStatusChange={setStatusFilter}
              packageFilter={packageFilter}
              onPackageChange={setPackageFilter}
              sortBy={sortBy}
              onSortChange={setSortBy}
            />
          </div>
        </div>
      </div>

      {/* Content Section */}
      <div className="max-w-7xl mx-auto">
        {loading && (
          <div className="flex justify-center items-center py-20">
            <div className="animate-spin rounded-full h-12 w-12 border-4 border-indigo-500 border-t-transparent"></div>
          </div>
        )}

        {!loading && customers.length === 0 && (
          <EmptyState hasFilters={hasFilters} />
        )}

        {!loading && customers.length > 0 && (
          <Table customers={customers} onNavigate={onNavigate} />
        )}
      </div>
    </div>
  );
}
// src/routes/analyticsRoutes.jsx

import DashboardPage from '../modules/analytics-service/pages/DashboardPage';
import CustomerAnalyticsPage from '../modules/analytics-service/pages/CustomerAnalyticsPage';

// ✅ Dinamik Customer ID Wrapper
const CustomerAnalyticsWrapper = ({ onNavigate }) => {
  // localStorage'dan customer ID'yi al, yoksa default 1 kullan (mock data için)
  const storedId = localStorage.getItem('selectedCustomerId');
  const customerId = storedId ? parseInt(storedId) : 1; // Default: 1 (mock data'da var)
  
  return <CustomerAnalyticsPage customerId={customerId} onNavigate={onNavigate} />;
};

export const analyticsRoutes = {
  dashboard: {
    component: DashboardPage
  },
  musteriAnaliz: {
    component: CustomerAnalyticsWrapper
  }
};
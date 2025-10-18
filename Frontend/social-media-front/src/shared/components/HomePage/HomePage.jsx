// shared/components/HomePage/HomePage.jsx

import { useHomePageStats } from './hooks/useHomePageStats';
import {
  WelcomeSection,
  StatsCards,
  QuickActions,
  RecentActivity
} from './components';

export default function HomePage({ onNavigate }) {
  const { stats, loading } = useHomePageStats();

  const handleQuickAction = (menuKey) => {
    if (onNavigate) {
      onNavigate(menuKey);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
          <p className="text-indigo-600 mt-4">Yükleniyor...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Hoşgeldin Mesajı */}
      <WelcomeSection />

      {/* İstatistik Kartları */}
      <StatsCards stats={stats} />

      {/* İki Kolonlu Layout */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Sol: Hızlı Erişim (2 kolon) */}
        <div className="lg:col-span-2">
          <QuickActions onActionClick={handleQuickAction} />
        </div>

        {/* Sağ: Son Aktiviteler (1 kolon) */}
        <div className="lg:col-span-1">
          <RecentActivity />
        </div>
      </div>
    </div>
  );
}
// shared/components/HomePage/HomePage.jsx

import { useHomePageStats } from './hooks/useHomePageStats';
 
import {
  WelcomeSection,
  StatsCards,
  QuickActions,
  RecentActivity
} from './components';
import { useToast } from '../../context/ToastContext';
import { useModal } from '../../context/ModalContext';

export default function HomePage({ onNavigate }) {
  const { stats, loading } = useHomePageStats();
  const { toast } = useToast();
  const { confirm } = useModal();

  const handleQuickAction = (menuKey) => {
    if (onNavigate) {
      onNavigate(menuKey);
    }
  };

  // TEST BUTONLARI
  const testToasts = () => {
    toast.success('✅ İşlem başarıyla tamamlandı!');
    setTimeout(() => toast.error('❌ Bir hata oluştu!'), 500);
    setTimeout(() => toast.warning('⚠️ Dikkat! Bu önemli bir uyarı.'), 1000);
    setTimeout(() => toast.info('ℹ️ Bilgi: Yeni özellikler eklendi.'), 1500);
  };

  const testModal = async () => {
    await confirm({
      title: 'Silme İşlemi',
      message: 'Bu müşteriyi silmek istediğinize emin misiniz? Bu işlem geri alınamaz!',
      confirmText: 'Evet, Sil',
      cancelText: 'İptal',
      type: 'danger',
      onConfirm: async () => {
        // Fake API call
        await new Promise(resolve => setTimeout(resolve, 2000));
        toast.success('Müşteri başarıyla silindi!');
      }
    });
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
      {/* TEST BUTONU - GEÇİCİ */}
      <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
        <p className="text-sm text-yellow-800 mb-2">🧪 Toast Test Alanı:</p>
        <button
          onClick={testToasts}
          className="px-4 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600 transition"
        >
          Tüm Toast Tiplerini Test Et
        </button>
      </div>

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
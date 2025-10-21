// src/modules/analytics-service/data/mockHelpers.js

// ============================================
// HELPER FUNCTIONS
// ============================================

// Kalan gün sayısını hesapla
export const calculateDaysRemaining = (deadline) => {
  const now = new Date();
  const deadlineDate = new Date(deadline);
  const diffTime = deadlineDate - now;
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  return diffDays > 0 ? diffDays : 0;
};

// Renk kodlaması (gün sayısına göre)
export const getPriorityColor = (daysRemaining) => {
  if (daysRemaining <= 2) {
    return { 
      bg: 'bg-red-100', 
      text: 'text-red-700', 
      badge: 'bg-red-500',
      border: 'border-red-300'
    };
  }
  if (daysRemaining <= 5) {
    return { 
      bg: 'bg-yellow-100', 
      text: 'text-yellow-700', 
      badge: 'bg-yellow-500',
      border: 'border-yellow-300'
    };
  }
  return { 
    bg: 'bg-green-100', 
    text: 'text-green-700', 
    badge: 'bg-green-500',
    border: 'border-green-300'
  };
};

// Status'e göre Türkçe label
export const getStatusLabel = (status) => {
  const statusMap = {
    'NOT_STARTED': 'Başlanmadı',
    'IN_PROGRESS': 'Hazırlanıyor',
    'READY': 'Hazırlandı',
    'COMPLETED': 'Tamamlandı',
    'SENT': 'Gönderildi',
    'SCHEDULED': 'Zamanlandı',
    'FAILED': 'Başarısız',
    'CANCELLED': 'İptal Edildi',
    'PENDING': 'Bekliyor',
    'ACTIVE': 'Aktif'
  };
  return statusMap[status] || status;
};

// Status'e göre renk
export const getStatusColor = (status) => {
  const colorMap = {
    'NOT_STARTED': 'bg-gray-200 text-gray-700 border-gray-300',
    'IN_PROGRESS': 'bg-blue-100 text-blue-700 border-blue-300',
    'READY': 'bg-green-100 text-green-700 border-green-300',
    'COMPLETED': 'bg-green-200 text-green-800 border-green-400',
    'SENT': 'bg-purple-100 text-purple-700 border-purple-300',
    'SCHEDULED': 'bg-indigo-100 text-indigo-700 border-indigo-300',
    'FAILED': 'bg-red-100 text-red-700 border-red-300',
    'CANCELLED': 'bg-gray-300 text-gray-800 border-gray-400',
    'PENDING': 'bg-yellow-100 text-yellow-700 border-yellow-300',
    'ACTIVE': 'bg-green-100 text-green-700 border-green-300'
  };
  return colorMap[status] || 'bg-gray-200 text-gray-700 border-gray-300';
};

// Platform icon
export const getPlatformIcon = (platform) => {
  const iconMap = {
    'INSTAGRAM': '📷',
    'TIKTOK': '🎵',
    'FACEBOOK': '👍',
    'YOUTUBE': '▶️'
  };
  return iconMap[platform] || '📱';
};

// Platform renk
export const getPlatformColor = (platform) => {
  const colorMap = {
    'INSTAGRAM': 'bg-pink-100 text-pink-700 border-pink-300',
    'TIKTOK': 'bg-gray-900 text-white border-gray-700',
    'FACEBOOK': 'bg-blue-100 text-blue-700 border-blue-300',
    'YOUTUBE': 'bg-red-100 text-red-700 border-red-300'
  };
  return colorMap[platform] || 'bg-gray-100 text-gray-700 border-gray-300';
};

// Bildirim tipi icon
export const getNotificationIcon = (type) => {
  const iconMap = {
    'POST_READY': '✅',
    'POST_SENT': '📤',
    'AI_COMPLETED': '🤖',
    'DEADLINE_APPROACHING': '⏰',
    'NEW_CUSTOMER': '👤',
    'CUSTOMER_UPDATED': '✏️',
    'CONTENT_APPROVED': '👍',
    'MEDIA_UPLOADED': '📸'
  };
  return iconMap[type] || '🔔';
};

// Tarih formatla (Türkçe)
export const formatDate = (dateString) => {
  if (!dateString) return '-';
  
  const date = new Date(dateString);
  const options = { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  };
  
  return date.toLocaleDateString('tr-TR', options);
};

// Kısa tarih formatı
export const formatShortDate = (dateString) => {
  if (!dateString) return '-';
  
  const date = new Date(dateString);
  const options = { 
    day: 'numeric',
    month: 'short',
    year: 'numeric'
  };
  
  return date.toLocaleDateString('tr-TR', options);
};

// Göreceli zaman (2 saat önce, 3 gün önce vs.)
export const getRelativeTime = (dateString) => {
  if (!dateString) return '-';
  
  const now = new Date();
  const date = new Date(dateString);
  const diffMs = now - date;
  const diffMins = Math.floor(diffMs / 60000);
  const diffHours = Math.floor(diffMs / 3600000);
  const diffDays = Math.floor(diffMs / 86400000);
  
  if (diffMins < 1) return 'Az önce';
  if (diffMins < 60) return `${diffMins} dakika önce`;
  if (diffHours < 24) return `${diffHours} saat önce`;
  if (diffDays < 7) return `${diffDays} gün önce`;
  if (diffDays < 30) return `${Math.floor(diffDays / 7)} hafta önce`;
  if (diffDays < 365) return `${Math.floor(diffDays / 30)} ay önce`;
  return `${Math.floor(diffDays / 365)} yıl önce`;
};

// Paket badge rengi
export const getPackageBadge = (packageType) => {
  const badgeMap = {
    'BASIC': 'bg-gray-100 text-gray-700 border-gray-300',
    'STANDARD': 'bg-blue-100 text-blue-700 border-blue-300',
    'PREMIUM': 'bg-purple-100 text-purple-700 border-purple-300',
    'ENTERPRISE': 'bg-indigo-100 text-indigo-700 border-indigo-300'
  };
  return badgeMap[packageType] || 'bg-gray-100 text-gray-700 border-gray-300';
};

// İlerleme yüzdesi hesapla
export const calculateProgress = (current, total) => {
  if (!total || total === 0) return 0;
  return Math.round((current / total) * 100);
};

// İlerleme bar rengi
export const getProgressColor = (percentage) => {
  if (percentage < 30) return 'bg-red-500';
  if (percentage < 70) return 'bg-yellow-500';
  return 'bg-green-500';
};
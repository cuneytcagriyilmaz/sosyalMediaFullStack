// src/modules/analytics-service/data/mockData.js

// ============================================
// DASHBOARD STATISTICS
// ============================================
export const mockDashboardStats = {
  // Genel istatistikler
  totalCustomers: 12,
  activeCustomers: 9,
  pendingCustomers: 3,
  inactiveCustomers: 0,
  
  // Post istatistikleri
  totalPostsGenerated: 1200,
  totalPostsSent: 850,
  totalPostsScheduled: 180,
  totalPostsReady: 170,
  
  // Bu ay
  thisMonthPosts: 95,
  thisMonthNewCustomers: 2,
  
  // Platform istatistikleri
  platformStats: {
    instagram: {
      connectedCustomers: 10,
      totalPosts: 480,
      sentPosts: 380,
      scheduledPosts: 60,
      readyPosts: 40,
      color: 'pink'
    },
    tiktok: {
      connectedCustomers: 7,
      totalPosts: 340,
      sentPosts: 260,
      scheduledPosts: 50,
      readyPosts: 30,
      color: 'gray'
    },
    facebook: {
      connectedCustomers: 5,
      totalPosts: 240,
      sentPosts: 150,
      scheduledPosts: 40,
      readyPosts: 50,
      color: 'blue'
    },
    youtube: {
      connectedCustomers: 3,
      totalPosts: 140,
      sentPosts: 60,
      scheduledPosts: 30,
      readyPosts: 50,
      color: 'red'
    }
  },
  
  // Son aktiviteler
  recentActivities: [
    {
      id: 1,
      type: 'POST_SENT',
      message: 'TechVision Solutions için Instagram\'da 5 post paylaşıldı',
      timestamp: '2025-10-21T14:30:00Z',
      icon: '📤',
      customerName: 'TechVision Solutions'
    },
    {
      id: 2,
      type: 'AI_COMPLETED',
      message: 'Café Delight için AI 100 post oluşturdu',
      timestamp: '2025-10-21T12:15:00Z',
      icon: '🤖',
      customerName: 'Café Delight'
    },
    {
      id: 3,
      type: 'NEW_CUSTOMER',
      message: 'FitLife Gym sisteme eklendi',
      timestamp: '2025-10-20T09:00:00Z',
      icon: '👤',
      customerName: 'FitLife Gym'
    },
    {
      id: 4,
      type: 'CONTENT_APPROVED',
      message: 'EcoStore için 20 post müşteri tarafından onaylandı',
      timestamp: '2025-10-20T08:45:00Z',
      icon: '👍',
      customerName: 'EcoStore'
    },
    {
      id: 5,
      type: 'POST_SENT',
      message: 'BeautyZone için TikTok\'ta 3 video paylaşıldı',
      timestamp: '2025-10-19T16:20:00Z',
      icon: '📤',
      customerName: 'BeautyZone'
    }
  ]
};

// ============================================
// CUSTOMER DETAIL DATA
// ============================================
export const mockCustomerDetails = {
  1: {
    // Genel bilgiler
    id: 1,
    companyName: 'TechVision Solutions',
    sector: 'Teknoloji',
    email: 'info@techvision.com',
    phone: '+90 532 123 4567',
    contactPerson: 'Ahmet Yılmaz',
    status: 'ACTIVE',
    membershipPackage: 'PREMIUM',
    createdAt: '2024-01-15T10:30:00Z',
    
    // Sosyal medya bağlantıları
    socialMediaConnected: {
      instagram: true,
      tiktok: true,
      facebook: false,
      youtube: false
    },
    
    // İçerik bilgileri
    hashtags: ['#teknoloji', '#yazılım', '#innovation', '#ai', '#digitaltransformation'],
    photosUploaded: 45,
    
    // Post istatistikleri
    postStats: {
      totalGenerated: 100,
      ready: 85,
      sent: 60,
      scheduled: 25,
      byPlatform: {
        instagram: 35,
        tiktok: 15,
        facebook: 5,
        youtube: 5
      }
    },
    
    // Notlar
    notes: [
      {
        id: 1,
        text: 'Müşteri AI\'ın ürettiği içerikleri çok beğeniyor, hızlı onay veriyor',
        createdAt: '2024-10-15T10:00:00Z',
        createdBy: 'Admin'
      },
      {
        id: 2,
        text: 'Facebook bağlantısı ertelendi, şimdilik sadece Instagram ve TikTok kullanacak',
        createdAt: '2024-10-10T14:30:00Z',
        createdBy: 'Admin'
      },
      {
        id: 3,
        text: 'Her Pazartesi saat 10:00\'da haftalık toplantı',
        createdAt: '2024-09-20T09:00:00Z',
        createdBy: 'Admin'
      }
    ],
    
    // Yaklaşan postlar
    upcomingPosts: [
      {
        id: 101,
        title: 'AI Teknolojileri 2024 Trendi',
        scheduledDate: '2025-10-22T10:00:00Z',
        platform: 'INSTAGRAM',
        status: 'SCHEDULED',
        thumbnail: 'https://picsum.photos/200/200?random=1'
      },
      {
        id: 102,
        title: 'Yazılım Geliştirme İpuçları',
        scheduledDate: '2025-10-23T14:30:00Z',
        platform: 'TIKTOK',
        status: 'SCHEDULED',
        thumbnail: 'https://picsum.photos/200/200?random=2'
      },
      {
        id: 103,
        title: 'Dijital Dönüşüm Rehberi',
        scheduledDate: '2025-10-24T16:00:00Z',
        platform: 'INSTAGRAM',
        status: 'SCHEDULED',
        thumbnail: 'https://picsum.photos/200/200?random=3'
      },
      {
        id: 104,
        title: 'Startup Ekosistemi Türkiye',
        scheduledDate: '2025-10-25T09:00:00Z',
        platform: 'TIKTOK',
        status: 'SCHEDULED',
        thumbnail: 'https://picsum.photos/200/200?random=4'
      },
      {
        id: 105,
        title: 'Makine Öğrenmesi Temelleri',
        scheduledDate: '2025-10-26T20:00:00Z',
        platform: 'INSTAGRAM',
        status: 'SCHEDULED',
        thumbnail: 'https://picsum.photos/200/200?random=5'
      }
    ],
    
    // Son aktiviteler (müşteriye özel)
    recentActivities: [
      {
        id: 1,
        type: 'POST_SENT',
        message: 'Instagram\'da 5 post paylaşıldı',
        timestamp: '2025-10-21T14:30:00Z'
      },
      {
        id: 2,
        type: 'CONTENT_APPROVED',
        message: 'İçerik incelemesi tamamlandı (85/100)',
        timestamp: '2025-10-21T09:00:00Z'
      },
      {
        id: 3,
        type: 'AI_COMPLETED',
        message: 'AI 20 yeni post üretti',
        timestamp: '2025-10-20T16:45:00Z'
      },
      {
        id: 4,
        type: 'MEDIA_UPLOADED',
        message: '10 yeni fotoğraf yüklendi',
        timestamp: '2025-10-19T11:20:00Z'
      },
      {
        id: 5,
        type: 'CUSTOMER_UPDATED',
        message: 'TikTok hesabı bağlandı',
        timestamp: '2025-10-15T10:30:00Z'
      }
    ]
  },
  
  // Daha fazla müşteri eklenebilir...
  2: {
    id: 2,
    companyName: 'Café Delight',
    sector: 'Yiyecek & İçecek',
    email: 'hello@cafedelight.com',
    phone: '+90 533 234 5678',
    contactPerson: 'Ayşe Kaya',
    status: 'ACTIVE',
    membershipPackage: 'STANDARD',
    createdAt: '2024-02-20T14:20:00Z',
    socialMediaConnected: {
      instagram: true,
      tiktok: false,
      facebook: true,
      youtube: false
    },
    hashtags: ['#kahve', '#cafe', '#istanbul', '#breakfast'],
    photosUploaded: 30,
    postStats: {
      totalGenerated: 100,
      ready: 70,
      sent: 45,
      scheduled: 25,
      byPlatform: {
        instagram: 30,
        tiktok: 0,
        facebook: 10,
        youtube: 5
      }
    },
    notes: [
      {
        id: 1,
        text: 'Kahve fotoğrafları çok başarılı, daha fazla benzer içerik istiyorlar',
        createdAt: '2024-10-18T15:00:00Z',
        createdBy: 'Admin'
      }
    ],
    upcomingPosts: [
      {
        id: 201,
        title: 'Günün Kahve Önerisi',
        scheduledDate: '2025-10-22T08:00:00Z',
        platform: 'INSTAGRAM',
        status: 'SCHEDULED',
        thumbnail: 'https://picsum.photos/200/200?random=10'
      }
    ],
    recentActivities: [
      {
        id: 1,
        type: 'AI_COMPLETED',
        message: 'AI 100 post oluşturdu',
        timestamp: '2025-10-21T12:15:00Z'
      }
    ]
  }
};

// ============================================
// AI CONTENT TASKS
// ============================================
export const mockAIContentTasks = {
  1: [ // Customer ID 1
    {
      id: 1,
      customerId: 1,
      taskName: 'Hashtag Analizi',
      taskType: 'HASHTAG_ANALYSIS',
      status: 'COMPLETED',
      quantity: 5,
      progressCurrent: 5,
      progressTotal: 5,
      notes: 'Instagram ve TikTok için trend hashtagler belirlendi',
      startedAt: '2024-01-15T10:00:00Z',
      completedAt: '2024-01-15T11:30:00Z',
      createdAt: '2024-01-15T10:00:00Z'
    },
    {
      id: 2,
      customerId: 1,
      taskName: 'Fotoğraf Yükleme',
      taskType: 'PHOTO_UPLOAD',
      status: 'COMPLETED',
      quantity: 45,
      progressCurrent: 45,
      progressTotal: 45,
      notes: 'Tüm ürün ve ofis fotoğrafları sisteme yüklendi',
      startedAt: '2024-01-16T09:00:00Z',
      completedAt: '2024-01-16T14:00:00Z',
      createdAt: '2024-01-16T09:00:00Z'
    },
    {
      id: 3,
      customerId: 1,
      taskName: 'AI ile Post Üretimi',
      taskType: 'AI_GENERATION',
      status: 'COMPLETED',
      quantity: 100,
      progressCurrent: 100,
      progressTotal: 100,
      notes: 'Tüm içerikler başarıyla oluşturuldu',
      startedAt: '2024-01-17T10:00:00Z',
      completedAt: '2024-01-20T16:45:00Z',
      createdAt: '2024-01-17T10:00:00Z'
    },
    {
      id: 4,
      customerId: 1,
      taskName: 'İçerik İncelemesi',
      taskType: 'CONTENT_REVIEW',
      status: 'IN_PROGRESS',
      quantity: 100,
      progressCurrent: 85,
      progressTotal: 100,
      notes: 'Görseller ve caption\'lar kontrol ediliyor',
      startedAt: '2024-01-21T09:00:00Z',
      completedAt: null,
      createdAt: '2024-01-21T09:00:00Z'
    },
    {
      id: 5,
      customerId: 1,
      taskName: 'Müşteri Onayı',
      taskType: 'CUSTOMER_APPROVAL',
      status: 'PENDING',
      quantity: 100,
      progressCurrent: 0,
      progressTotal: 100,
      notes: 'İnceleme tamamlandıktan sonra müşteriye sunulacak',
      startedAt: null,
      completedAt: null,
      createdAt: '2024-01-21T09:00:00Z'
    }
  ],
  2: [ // Customer ID 2
    {
      id: 6,
      customerId: 2,
      taskName: 'Hashtag Analizi',
      taskType: 'HASHTAG_ANALYSIS',
      status: 'COMPLETED',
      quantity: 4,
      progressCurrent: 4,
      progressTotal: 4,
      notes: 'Kahve ve cafe hashtagleri belirlendi',
      startedAt: '2024-02-20T14:00:00Z',
      completedAt: '2024-02-20T15:00:00Z',
      createdAt: '2024-02-20T14:00:00Z'
    },
    {
      id: 7,
      customerId: 2,
      taskName: 'Fotoğraf Yükleme',
      taskType: 'PHOTO_UPLOAD',
      status: 'COMPLETED',
      quantity: 30,
      progressCurrent: 30,
      progressTotal: 30,
      notes: 'Kahve ve tatlı fotoğrafları yüklendi',
      startedAt: '2024-02-21T10:00:00Z',
      completedAt: '2024-02-21T12:00:00Z',
      createdAt: '2024-02-21T10:00:00Z'
    },
    {
      id: 8,
      customerId: 2,
      taskName: 'AI ile Post Üretimi',
      taskType: 'AI_GENERATION',
      status: 'COMPLETED',
      quantity: 100,
      progressCurrent: 100,
      progressTotal: 100,
      notes: 'Tüm içerikler oluşturuldu',
      startedAt: '2024-02-22T09:00:00Z',
      completedAt: '2024-02-25T14:00:00Z',
      createdAt: '2024-02-22T09:00:00Z'
    },
    {
      id: 9,
      customerId: 2,
      taskName: 'İçerik İncelemesi',
      taskType: 'CONTENT_REVIEW',
      status: 'COMPLETED',
      quantity: 100,
      progressCurrent: 100,
      progressTotal: 100,
      notes: 'Tüm içerikler incelendi',
      startedAt: '2024-02-26T10:00:00Z',
      completedAt: '2024-02-28T16:00:00Z',
      createdAt: '2024-02-26T10:00:00Z'
    },
    {
      id: 10,
      customerId: 2,
      taskName: 'Müşteri Onayı',
      taskType: 'CUSTOMER_APPROVAL',
      status: 'IN_PROGRESS',
      quantity: 100,
      progressCurrent: 70,
      progressTotal: 100,
      notes: 'Müşteri postları onaylıyor',
      startedAt: '2024-03-01T09:00:00Z',
      completedAt: null,
      createdAt: '2024-03-01T09:00:00Z'
    }
  ]
};

// ============================================
// ONBOARDING TASKS
// ============================================
export const mockOnboardingTasks = {
  1: [ // Customer ID 1
    {
      id: 1,
      customerId: 1,
      taskName: 'Instagram Hesabı Bağlantısı',
      platform: 'INSTAGRAM',
      status: 'COMPLETED',
      connectionDate: '2024-01-15T11:00:00Z',
      notes: 'Bağlantı başarılı, API erişimi aktif',
      createdAt: '2024-01-15T10:00:00Z'
    },
    {
      id: 2,
      customerId: 1,
      taskName: 'TikTok Hesabı Bağlantısı',
      platform: 'TIKTOK',
      status: 'COMPLETED',
      connectionDate: '2024-01-15T11:30:00Z',
      notes: 'Bağlantı başarılı',
      createdAt: '2024-01-15T10:00:00Z'
    },
    {
      id: 3,
      customerId: 1,
      taskName: 'Facebook Hesabı Bağlantısı',
      platform: 'FACEBOOK',
      status: 'NOT_STARTED',
      connectionDate: null,
      notes: 'Müşteri şimdilik Facebook kullanmayacak',
      createdAt: '2024-01-15T10:00:00Z'
    },
    {
      id: 4,
      customerId: 1,
      taskName: 'YouTube Hesabı Bağlantısı',
      platform: 'YOUTUBE',
      status: 'NOT_STARTED',
      connectionDate: null,
      notes: 'İleri bir tarihte eklenecek',
      createdAt: '2024-01-15T10:00:00Z'
    },
    {
      id: 5,
      customerId: 1,
      taskName: 'İlk Post Gönderimi',
      platform: 'INSTAGRAM',
      status: 'COMPLETED',
      connectionDate: '2024-01-18T09:00:00Z',
      notes: 'İlk post başarıyla paylaşıldı',
      createdAt: '2024-01-15T10:00:00Z'
    },
    {
      id: 6,
      customerId: 1,
      taskName: 'Platform Ayarları',
      platform: null,
      status: 'COMPLETED',
      connectionDate: '2024-01-19T10:00:00Z',
      notes: 'Yayın saatleri ve içerik stratejisi belirlendi',
      createdAt: '2024-01-15T10:00:00Z'
    }
  ],
  2: [ // Customer ID 2
    {
      id: 7,
      customerId: 2,
      taskName: 'Instagram Hesabı Bağlantısı',
      platform: 'INSTAGRAM',
      status: 'COMPLETED',
      connectionDate: '2024-02-20T15:00:00Z',
      notes: 'Bağlantı başarılı',
      createdAt: '2024-02-20T14:00:00Z'
    },
    {
      id: 8,
      customerId: 2,
      taskName: 'Facebook Hesabı Bağlantısı',
      platform: 'FACEBOOK',
      status: 'COMPLETED',
      connectionDate: '2024-02-20T15:30:00Z',
      notes: 'Bağlantı başarılı',
      createdAt: '2024-02-20T14:00:00Z'
    },
    {
      id: 9,
      customerId: 2,
      taskName: 'TikTok Hesabı Bağlantısı',
      platform: 'TIKTOK',
      status: 'NOT_STARTED',
      connectionDate: null,
      notes: 'Müşteri henüz TikTok kullanmayacak',
      createdAt: '2024-02-20T14:00:00Z'
    },
    {
      id: 10,
      customerId: 2,
      taskName: 'İlk Post Gönderimi',
      platform: 'INSTAGRAM',
      status: 'COMPLETED',
      connectionDate: '2024-02-21T08:00:00Z',
      notes: 'İlk kahve postu paylaşıldı',
      createdAt: '2024-02-20T14:00:00Z'
    }
  ]
};
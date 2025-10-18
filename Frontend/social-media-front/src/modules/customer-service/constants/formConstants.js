// modules/customer-service/constants/formConstants.js

export const INPUT_CLASS = "w-full p-2 border border-gray-300 rounded-lg bg-white text-gray-900 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500";

export const LABEL_CLASS = "block text-sm font-medium text-gray-700 mb-1";

export const MEMBERSHIP_PACKAGES = [
  { value: "Basic", label: "Basic - Temel Paket" },
  { value: "Gold", label: "Gold - Altın Paket" },
  { value: "Platinum", label: "Platinum - Platin Paket" },
  { value: "Premium", label: "Premium - Premium Paket" }
];

export const CUSTOMER_STATUS = [
  { value: "ACTIVE", label: "Aktif" },
  { value: "PASSIVE", label: "Pasif" },
  { value: "CANCELLED", label: "İptal" }
];

export const POST_TYPES = [
  { value: "gorsel", label: "📸 Görsel" },
  { value: "video", label: "🎥 Video" },
  { value: "story", label: "📱 Story" }
];

export const POST_TONES = [
  { value: "samimi", label: "😊 Samimi" },
  { value: "resmi", label: "👔 Resmi" },
  { value: "mizahi", label: "😄 Mizahi" },
  { value: "ciddi", label: "🎩 Ciddi" }
];

export const INITIAL_FORM_DATA = {
  companyName: "",
  sector: "",
  address: "",
  membershipPackage: "",
  status: "ACTIVE",
  targetAudience: {
    specialDates: false,
    targetRegion: "",
    customerHashtags: "",
    postType: "gorsel",
    postFrequency: "5",
    postTone: "samimi",
    audienceAge: "",
    audienceInterests: ""
  },
  contacts: [
    { name: "", surname: "", email: "", phone: "", priority: 1 }
  ],
  socialMedia: {
    instagram: "",
    facebook: "",
    tiktok: ""
  },
  seo: {
    googleConsoleEmail: "",
    titleSuggestions: "",
    contentSuggestions: ""
  },
  apiKeys: {
    instagramApiKey: "",
    facebookApiKey: "",
    tiktokApiKey: "",
    googleApiKey: ""
  }
};
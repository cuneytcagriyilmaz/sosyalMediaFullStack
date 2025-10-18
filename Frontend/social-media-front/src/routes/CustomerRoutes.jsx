// src/routes/customerRoutes.jsx

import { 
  CustomerAddPage, 
  CustomerUpdatePage 
} from '../modules/customer-service/components/CustomerForm';
import CustomerDeletePage from '../modules/customer-service/components/CustomerDelete';
import RecycleBinPage from '../modules/customer-service/components/RecycleBin';
import CustomerDetailsPage from '../modules/customer-service/components/CustomerDetails';
import CustomerMediaPage from '../modules/customer-service/components/CustomerMedia';
import CustomerListPage from '../modules/customer-service/components/CustomerList/CustomerListPage'; // ✅ YENİ
import HomePage from '../shared/components/HomePage';

export const customerRoutes = {
  anasayfa: {
    component: HomePage
  },
  // ✅ YENİ - Müşteri Listesi (musteriListesi olarak eklendi)
  musteriListesi: {
    component: CustomerListPage
  },
  musteriGoruntule: {
    component: CustomerDetailsPage,
    wrapper: { title: "👥 Müşteri Detayları" }
  },
  musteriEkle: {
    component: CustomerAddPage,
    wrapper: { title: "👥 Müşteri Ekle" }
  },
  musteriSil: {
    component: CustomerDeletePage,
    wrapper: true
  },
  musteriGuncelle: {
    component: CustomerUpdatePage,
    wrapper: { title: "👥 Müşteri Güncelle" }
  },
  silinimisMusteriler: {
    component: RecycleBinPage,
    wrapper: true
  },
  medyaYonetimi: {
    component: CustomerMediaPage,
    wrapper: true
  }
};
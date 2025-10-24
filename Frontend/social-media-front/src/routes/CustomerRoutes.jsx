// src/routes/customerRoutes.jsx

import { 
  CustomerAddPage, 
  CustomerUpdatePage 
} from '../modules/customer-service/components/CustomerForm';
import CustomerDeletePage from '../modules/customer-service/components/CustomerDelete';
import RecycleBinPage from '../modules/customer-service/components/RecycleBin';
import CustomerDetailsPage from '../modules/customer-service/components/CustomerDetails';
import CustomerMediaPage from '../modules/customer-service/components/CustomerMedia';
import CustomerListPage from '../modules/customer-service/components/CustomerList/CustomerListPage';
import HomePage from '../shared/components/HomePage';

export const customerRoutes = {
  anasayfa: {
    component: HomePage
  },
  
  // ZORUNLU: musteriListesi tanımlı olmalı
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
  
  musteriGuncelle: {
    component: CustomerUpdatePage,
    wrapper: { title: "👥 Müşteri Güncelle" }
  },
  
  musteriSil: {
    component: CustomerDeletePage,
    wrapper: true
  },
  
  medyaYonetimi: {
    component: CustomerMediaPage,
    wrapper: true
  },
  
  silinimisMusteriler: {
    component: RecycleBinPage,
    wrapper: true
  }
};
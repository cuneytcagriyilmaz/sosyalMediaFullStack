// src/modules/customer-service/hooks/useCustomerForm.js

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useToast } from "../../../shared/context/ToastContext";
import customerService from "../services/customerService";
import { INITIAL_FORM_DATA } from "../constants/formConstants";

export default function useCustomerForm() {
  const [formData, setFormData] = useState(INITIAL_FORM_DATA);
  const [logoFiles, setLogoFiles] = useState([]);
  const [photoFiles, setPhotoFiles] = useState([]);
  const [videoFiles, setVideoFiles] = useState([]);
  const [documentFiles, setDocumentFiles] = useState([]);
  const [loading, setLoading] = useState(false);
  
  const navigate = useNavigate();
  const { toast } = useToast();

  const addContact = () => {
    setFormData({
      ...formData,
      contacts: [
        ...formData.contacts,
        { name: "", surname: "", email: "", phone: "", priority: formData.contacts.length + 1 }
      ]
    });
  };

  const removeContact = (index) => {
    const newContacts = formData.contacts.filter((_, i) => i !== index);
    setFormData({ ...formData, contacts: newContacts });
  };

  const updateContact = (index, field, value) => {
    const newContacts = [...formData.contacts];
    newContacts[index][field] = value;
    setFormData({ ...formData, contacts: newContacts });
  };

  const updateNested = (parent, field, value) => {
    setFormData({
      ...formData,
      [parent]: {
        ...formData[parent],
        [field]: value
      }
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      // ===== 1. MÜŞTERİ OLUŞTUR =====
      console.log('📤 Form Data:', formData);
      
      const response = await customerService.createCustomer(formData);
      console.log('📥 Customer response:', response);

      // ✅ Response kontrolü
      if (!response || !response.success || !response.data) {
        console.error('❌ Müşteri oluşturulamadı:', response);
        const errorMsg = response?.error || 'Müşteri oluşturulamadı';
        toast.error(errorMsg);
        setLoading(false);
        return; // ✅ DURDUR - Media upload yapma!
      }

      const customer = response.data;
      const customerId = customer.id;

      // ✅ ID kontrolü
      if (!customerId) {
        console.error('❌ Customer ID bulunamadı:', customer);
        toast.error('Müşteri ID alınamadı!');
        setLoading(false);
        return;
      }

      console.log('✅ Müşteri başarıyla oluşturuldu:', customerId);

      // ===== 2. MEDYA YÜKLE (Sadece müşteri başarıyla oluşturulduysa) =====
      const uploadPromises = [];
      
      if (logoFiles.length > 0) {
        console.log(`📤 ${logoFiles.length} logo yükleniyor...`);
        uploadPromises.push(
          customerService.uploadMultipleMedia(customerId, logoFiles, 'LOGO')
            .then((res) => {
              console.log('✅ Logolar yüklendi:', res);
              return res;
            })
            .catch(err => {
              console.error('❌ Logo upload hatası:', err);
              // Devam et, diğer upload'ları engelleme
              return null;
            })
        );
      }

      if (photoFiles.length > 0) {
        console.log(`📤 ${photoFiles.length} fotoğraf yükleniyor...`);
        uploadPromises.push(
          customerService.uploadMultipleMedia(customerId, photoFiles, 'PHOTO')
            .then((res) => {
              console.log('✅ Fotoğraflar yüklendi:', res);
              return res;
            })
            .catch(err => {
              console.error('❌ Fotoğraf upload hatası:', err);
              return null;
            })
        );
      }

      if (videoFiles.length > 0) {
        console.log(`📤 ${videoFiles.length} video yükleniyor...`);
        uploadPromises.push(
          customerService.uploadMultipleMedia(customerId, videoFiles, 'VIDEO')
            .then((res) => {
              console.log('✅ Videolar yüklendi:', res);
              return res;
            })
            .catch(err => {
              console.error('❌ Video upload hatası:', err);
              return null;
            })
        );
      }

      if (documentFiles.length > 0) {
        console.log(`📤 ${documentFiles.length} döküman yükleniyor...`);
        uploadPromises.push(
          customerService.uploadMultipleMedia(customerId, documentFiles, 'DOCUMENT')
            .then((res) => {
              console.log('✅ Dökümanlar yüklendi:', res);
              return res;
            })
            .catch(err => {
              console.error('❌ Döküman upload hatası:', err);
              return null;
            })
        );
      }

      // ✅ Tüm medya yüklemelerini bekle (hata olsa bile devam eder)
      if (uploadPromises.length > 0) {
        console.log(`⏳ ${uploadPromises.length} media upload bekleniyor...`);
        const results = await Promise.allSettled(uploadPromises);
        
        const successCount = results.filter(r => r.status === 'fulfilled' && r.value).length;
        const failCount = results.filter(r => r.status === 'rejected' || !r.value).length;
        
        console.log(`✅ Media upload sonuçları: ${successCount} başarılı, ${failCount} başarısız`);
        
        if (failCount > 0) {
          toast.warning(`Müşteri eklendi ancak ${failCount} medya dosyası yüklenemedi.`);
        } else {
          toast.success("Müşteri ve medya dosyaları başarıyla eklendi!");
        }
      } else {
        toast.success("Müşteri başarıyla eklendi!");
      }

      // ===== 3. YÖNLENDİRME =====
      setTimeout(() => {
        navigate("/"); // Ana sayfaya yönlendir
      }, 1500);

    } catch (error) {
      console.error("❌ Form submit hatası:", error);
      
      // Hata mesajını oku
      const errorMessage = error.response?.data?.message 
        || error.message 
        || "Müşteri eklenirken bir hata oluştu!";
      
      toast.error(errorMessage);
      
    } finally {
      setLoading(false);
    }
  };

  return {
    formData,
    setFormData,
    logoFiles,
    setLogoFiles,
    photoFiles,
    setPhotoFiles,
    videoFiles,
    setVideoFiles,
    documentFiles,
    setDocumentFiles,
    loading,
    addContact,
    removeContact,
    updateContact,
    updateNested,
    handleSubmit
  };
}
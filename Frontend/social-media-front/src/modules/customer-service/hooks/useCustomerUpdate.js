// modules/customer-service/hooks/useCustomerUpdate.js
import { useState, useEffect } from "react";
import { useToast } from "../../../shared/context/ToastContext";
import customerService from "../services/customerService";

export default function useCustomerUpdate() {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState("");
  const [formData, setFormData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Section loading states
  const [loadingBasicInfo, setLoadingBasicInfo] = useState(false);
  const [loadingContacts, setLoadingContacts] = useState(false);
  const [loadingSocialMedia, setLoadingSocialMedia] = useState(false);
  const [loadingTargetAudience, setLoadingTargetAudience] = useState(false);
  const [loadingSeo, setLoadingSeo] = useState(false);
  const [loadingApiKeys, setLoadingApiKeys] = useState(false);

  // Media upload states
  const [logoFiles, setLogoFiles] = useState([]);
  const [photoFiles, setPhotoFiles] = useState([]);
  const [videoFiles, setVideoFiles] = useState([]);
  const [documentFiles, setDocumentFiles] = useState([]);
  const [uploadingMedia, setUploadingMedia] = useState(false);

  const { toast } = useToast();

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (err) {
      console.error("Müşteriler yüklenemedi:", err);
      setError("Müşteriler yüklenemedi");
      toast.error("Müşteriler yüklenemedi!");
    }
  };

  const handleSelectCustomer = async (customerId) => {
    if (!customerId) {
      setSelectedCustomerId("");
      setFormData(null);
      return;
    }

    setSelectedCustomerId(customerId);
    setLoading(true);

    try {
      const customer = await customerService.getCustomerById(customerId);
      setFormData(customer);
    } catch (err) {
      console.error("Müşteri detayı yüklenemedi:", err);
      setError("Müşteri detayı yüklenemedi");
      toast.error("Müşteri detayı yüklenemedi!");
    } finally {
      setLoading(false);
    }
  };

  // Contact operations
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

  // Nested field update
  const updateNested = (parent, field, value) => {
    setFormData({
      ...formData,
      [parent]: {
        ...formData[parent],
        [field]: value
      }
    });
  };

  // Section save functions
  const handleSaveBasicInfo = async () => {
    setLoadingBasicInfo(true);
    try {
      const data = {
        companyName: formData.companyName,
        sector: formData.sector,
        address: formData.address,
        membershipPackage: formData.membershipPackage,
        status: formData.status
      };

      await customerService.updateBasicInfo(selectedCustomerId, data);
      toast.success("Temel bilgiler başarıyla güncellendi!");
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      const errorMsg = err.response?.data?.message || err.message;
      toast.error("Hata: " + errorMsg);
    } finally {
      setLoadingBasicInfo(false);
    }
  };

  const handleSaveContacts = async () => {
    setLoadingContacts(true);
    try {
      await customerService.updateContacts(selectedCustomerId, formData.contacts);
      toast.success("İletişim kişileri başarıyla güncellendi!");
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      const errorMsg = err.response?.data?.message || err.message;
      
      if (errorMsg.includes("Duplicate")) {
        toast.error("Aynı email veya telefon numarası birden fazla kişide kullanılamaz!");
      } else {
        toast.error("Hata: " + errorMsg);
      }
    } finally {
      setLoadingContacts(false);
    }
  };

  const handleSaveSocialMedia = async () => {
    setLoadingSocialMedia(true);
    try {
      await customerService.updateSocialMedia(selectedCustomerId, formData.socialMedia);
      toast.success("Sosyal medya bilgileri başarıyla güncellendi!");
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      const errorMsg = err.response?.data?.message || err.message;
      toast.error("Hata: " + errorMsg);
    } finally {
      setLoadingSocialMedia(false);
    }
  };

  const handleSaveTargetAudience = async () => {
    setLoadingTargetAudience(true);
    try {
      await customerService.updateTargetAudience(selectedCustomerId, formData.targetAudience);
      toast.success("Hedef kitle bilgileri başarıyla güncellendi!");
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      const errorMsg = err.response?.data?.message || err.message;
      toast.error("Hata: " + errorMsg);
    } finally {
      setLoadingTargetAudience(false);
    }
  };

  const handleSaveSeo = async () => {
    setLoadingSeo(true);
    try {
      await customerService.updateSeo(selectedCustomerId, formData.seo);
      toast.success("SEO bilgileri başarıyla güncellendi!");
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      const errorMsg = err.response?.data?.message || err.message;
      toast.error("Hata: " + errorMsg);
    } finally {
      setLoadingSeo(false);
    }
  };

  const handleSaveApiKeys = async () => {
    setLoadingApiKeys(true);
    try {
      await customerService.updateApiKeys(selectedCustomerId, formData.apiKeys);
      toast.success("API anahtarları başarıyla güncellendi!");
      await handleSelectCustomer(selectedCustomerId);
    } catch (err) {
      console.error("Güncelleme hatası:", err);
      const errorMsg = err.response?.data?.message || err.message;
      toast.error("Hata: " + errorMsg);
    } finally {
      setLoadingApiKeys(false);
    }
  };

  // Media operations
  const handleMediaUpdate = async () => {
    if (selectedCustomerId) {
      await handleSelectCustomer(selectedCustomerId);
    }
  };

  const handleUploadNewMedia = async () => {
    if (logoFiles.length === 0 && photoFiles.length === 0 && 
        videoFiles.length === 0 && documentFiles.length === 0) {
      toast.warning("Lütfen en az bir dosya seçin!");
      return;
    }

    setUploadingMedia(true);

    try {
      const uploadPromises = [];

      if (logoFiles.length > 0) {
        uploadPromises.push(
          customerService.uploadMultipleMedia(selectedCustomerId, logoFiles, 'LOGO')
        );
      }
      if (photoFiles.length > 0) {
        uploadPromises.push(
          customerService.uploadMultipleMedia(selectedCustomerId, photoFiles, 'PHOTO')
        );
      }
      if (videoFiles.length > 0) {
        uploadPromises.push(
          customerService.uploadMultipleMedia(selectedCustomerId, videoFiles, 'VIDEO')
        );
      }
      if (documentFiles.length > 0) {
        uploadPromises.push(
          customerService.uploadMultipleMedia(selectedCustomerId, documentFiles, 'DOCUMENT')
        );
      }

      await Promise.all(uploadPromises);
      toast.success("Medya dosyaları başarıyla yüklendi!");

      // Clear files
      setLogoFiles([]);
      setPhotoFiles([]);
      setVideoFiles([]);
      setDocumentFiles([]);

      await handleSelectCustomer(selectedCustomerId);
    } catch (error) {
      console.error("Medya yükleme hatası:", error);
      const errorMsg = error.response?.data?.message || error.message;
      toast.error("Medya yüklenemedi: " + errorMsg);
    } finally {
      setUploadingMedia(false);
    }
  };

  return {
    customers,
    selectedCustomerId,
    formData,
    setFormData,
    loading,
    error,
    loadingBasicInfo,
    loadingContacts,
    loadingSocialMedia,
    loadingTargetAudience,
    loadingSeo,
    loadingApiKeys,
    logoFiles,
    setLogoFiles,
    photoFiles,
    setPhotoFiles,
    videoFiles,
    setVideoFiles,
    documentFiles,
    setDocumentFiles,
    uploadingMedia,
    handleSelectCustomer,
    addContact,
    removeContact,
    updateContact,
    updateNested,
    handleSaveBasicInfo,
    handleSaveContacts,
    handleSaveSocialMedia,
    handleSaveTargetAudience,
    handleSaveSeo,
    handleSaveApiKeys,
    handleMediaUpdate,
    handleUploadNewMedia
  };
}
// modules/customer-service/hooks/useCustomerForm.js

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
      // Müşteri oluştur
      const customer = await customerService.createCustomer(formData);
      console.log("Müşteri oluşturuldu:", customer);

      // Medya yüklemeleri
      const uploadPromises = [];
      
      if (logoFiles.length > 0) {
        uploadPromises.push(
          customerService.uploadMultipleMedia(customer.id, logoFiles, 'LOGO')
            .then(() => console.log("Logolar yüklendi"))
        );
      }
      if (photoFiles.length > 0) {
        uploadPromises.push(
          customerService.uploadMultipleMedia(customer.id, photoFiles, 'PHOTO')
            .then(() => console.log("Fotoğraflar yüklendi"))
        );
      }
      if (videoFiles.length > 0) {
        uploadPromises.push(
          customerService.uploadMultipleMedia(customer.id, videoFiles, 'VIDEO')
            .then(() => console.log("Videolar yüklendi"))
        );
      }
      if (documentFiles.length > 0) {
        uploadPromises.push(
          customerService.uploadMultipleMedia(customer.id, documentFiles, 'DOCUMENT')
            .then(() => console.log("Dökümanlar yüklendi"))
        );
      }

      // Tüm medya yüklemelerini bekle
      if (uploadPromises.length > 0) {
        await Promise.all(uploadPromises);
        toast.success("Müşteri ve medya dosyaları başarıyla eklendi!");
      } else {
        toast.success("Müşteri başarıyla eklendi!");
      }

      // Başarılı olunca listeye yönlendir
      setTimeout(() => {
        navigate("/musteriler"); // veya "/anasayfa" nereye gitmek istiyorsan
      }, 1000);

    } catch (error) {
      console.error("Hata:", error);
      
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
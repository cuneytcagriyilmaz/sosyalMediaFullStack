// modules/customer-service/hooks/useCustomerForm.js

import { useState } from "react";
import customerService from "../services/customerService";
import { INITIAL_FORM_DATA } from "../constants/formConstants";

export default function useCustomerForm() {
  const [formData, setFormData] = useState(INITIAL_FORM_DATA);
  const [logoFiles, setLogoFiles] = useState([]);
  const [photoFiles, setPhotoFiles] = useState([]);
  const [videoFiles, setVideoFiles] = useState([]);
  const [documentFiles, setDocumentFiles] = useState([]);
  const [loading, setLoading] = useState(false);

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
      const customer = await customerService.createCustomer(formData);
      console.log("Müşteri oluşturuldu:", customer);

      // Media uploads
      if (logoFiles.length > 0) {
        await customerService.uploadMultipleMedia(customer.id, logoFiles, 'LOGO');
        console.log("Logolar yüklendi");
      }
      if (photoFiles.length > 0) {
        await customerService.uploadMultipleMedia(customer.id, photoFiles, 'PHOTO');
        console.log("Fotoğraflar yüklendi");
      }
      if (videoFiles.length > 0) {
        await customerService.uploadMultipleMedia(customer.id, videoFiles, 'VIDEO');
        console.log("Videolar yüklendi");
      }
      if (documentFiles.length > 0) {
        await customerService.uploadMultipleMedia(customer.id, documentFiles, 'DOCUMENT');
        console.log("Dökümanlar yüklendi");
      }

      alert("✅ Müşteri başarıyla eklendi!");
      window.location.reload();

    } catch (error) {
      console.error("Hata:", error);
      alert("❌ Hata: " + (error.response?.data?.message || error.message));
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


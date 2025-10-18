// modules/customer-service/components/CustomerForm/CustomerAddPage.jsx
import useCustomerForm from '../../hooks/useCustomerForm';
import { INPUT_CLASS, LABEL_CLASS } from '../../constants/formConstants';

// Section imports
import {
  BasicInfoSection,
  ContactsSection,
  SocialMediaSection,
  TargetAudienceSection,
  SeoSection,
  ApiKeysSection,
  MediaUploadSection
} from './FormSections';

// Component imports
import { FormActions } from './FormComponents';

export default function CustomerAddPage() {
  const {
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
  } = useCustomerForm();

  return (
    <div className="max-w-6xl mx-auto p-6 bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl shadow-lg">
      <div className="bg-white rounded-lg p-6">
        <h2 className="text-3xl font-bold text-indigo-700 mb-6 flex items-center">
          <span className="mr-3">👥</span> Yeni Müşteri Ekle
        </h2>

        <form onSubmit={handleSubmit} className="space-y-8">

          {/* Temel Bilgiler */}
          <BasicInfoSection
            formData={formData}
            setFormData={setFormData}
            inputClass={INPUT_CLASS}
            labelClass={LABEL_CLASS}
          />

          {/* İletişim Kişileri */}
          <ContactsSection
            contacts={formData.contacts}
            addContact={addContact}
            removeContact={removeContact}
            updateContact={updateContact}
            inputClass={INPUT_CLASS}
            labelClass={LABEL_CLASS}
          />

          {/* Sosyal Medya */}
          <SocialMediaSection
            socialMedia={formData.socialMedia}
            updateNested={updateNested}
            inputClass={INPUT_CLASS}
            labelClass={LABEL_CLASS}
          />

          {/* Hedef Kitle ve İçerik Stratejisi */}
          <TargetAudienceSection
            targetAudience={formData.targetAudience}
            updateNested={updateNested}
            inputClass={INPUT_CLASS}
            labelClass={LABEL_CLASS}
          />

          {/* SEO Bilgileri */}
          <SeoSection
            seo={formData.seo}
            updateNested={updateNested}
            inputClass={INPUT_CLASS}
            labelClass={LABEL_CLASS}
          />

          {/* API Anahtarları */}
          <ApiKeysSection
            apiKeys={formData.apiKeys}
            updateNested={updateNested}
            inputClass={INPUT_CLASS}
            labelClass={LABEL_CLASS}
          />

          {/* Medya Yükleme */}
          <MediaUploadSection
            logoFiles={logoFiles}
            setLogoFiles={setLogoFiles}
            photoFiles={photoFiles}
            setPhotoFiles={setPhotoFiles}
            videoFiles={videoFiles}
            setVideoFiles={setVideoFiles}
            documentFiles={documentFiles}
            setDocumentFiles={setDocumentFiles}
            inputClass={INPUT_CLASS}
            labelClass={LABEL_CLASS}
          />

          {/* Submit Butonları */}
          <FormActions loading={loading} />

        </form>
      </div>
    </div>
  );
}
// modules/customer-service/components/CustomerForm/CustomerUpdatePage.jsx
import useCustomerUpdate from '../../hooks/useCustomerUpdate';
import { INPUT_CLASS, LABEL_CLASS } from '../../constants/formConstants';
import {
    useMediaSelection,
    useMediaFilters
} from '../../hooks/customerMediaHooks';

// Update Section imports
import {
    UpdateBasicInfoSection,
    UpdateContactsSection,
    UpdateSocialMediaSection,
    UpdateTargetAudienceSection,
    UpdateSeoSection,
    UpdateApiKeysSection,
    UpdateMediaUploadSection
} from './UpdateFormSections';
import { MediaGallery } from '../CustomerMedia/components';
// src/modules/customer-service/components/CustomerForm/CustomerUpdatePage.jsx

// ... (imports aynı)

export default function CustomerUpdatePage() {
    const {
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
    } = useCustomerUpdate();

    // ✅ Array check
    const customerList = Array.isArray(customers) ? customers : [];

    // Media için hook'lar
    const allMedia = formData?.media || [];
    const selection = useMediaSelection();
    const filters = useMediaFilters(allMedia);

    // Media silme fonksiyonu
    const handleDeleteMedia = async (mediaId, fileName) => {
        try {
            await handleMediaUpdate();
        } catch (error) {
            console.error('Media silinemedi:', error);
            throw error;
        }
    };

    if (error) {
        return (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
                {error}
            </div>
        );
    }

    return (
        <div className="w-full max-w-6xl mx-auto p-3 sm:p-6 bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl shadow-lg">
            <div className="bg-white rounded-lg p-4 sm:p-6">
                <h2 className="text-2xl sm:text-3xl font-bold text-indigo-700 mb-4 sm:mb-6 flex items-center">
                    <span className="mr-2 sm:mr-3">✏️</span> Müşteri Güncelle
                </h2>

                {/* Müşteri Seç */}
                <div className="mb-4 sm:mb-6 bg-gray-50 p-3 sm:p-4 rounded-lg border border-gray-200">
                    <label className={LABEL_CLASS}>
                        Güncellenecek Müşteriyi Seçin <span className="text-red-500">*</span>
                    </label>
                    <select
                        value={selectedCustomerId}
                        onChange={(e) => handleSelectCustomer(e.target.value)}
                        className={INPUT_CLASS}
                    >
                        <option value="">-- Bir müşteri seçiniz --</option>
                        {/* ✅ Safe mapping */}
                        {customerList.map(c => (
                            <option key={c.id} value={c.id} className="text-gray-900">
                                {c.companyName || c.company_name || 'İsimsiz'} ({c.sector || 'Sektör yok'})
                            </option>
                        ))}
                    </select>
                </div>

                {loading && (
                    <div className="text-center py-8">
                        <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
                        <p className="text-indigo-600 mt-4">Yükleniyor...</p>
                    </div>
                )}

                {selectedCustomerId && formData && !loading && (
                    <div className="space-y-6 sm:space-y-8">
                        {/* ... (sections aynı kalacak) */}

                        <UpdateBasicInfoSection
                            formData={formData}
                            setFormData={setFormData}
                            onSave={handleSaveBasicInfo}
                            loading={loadingBasicInfo}
                            inputClass={INPUT_CLASS}
                            labelClass={LABEL_CLASS}
                        />

                        {formData.contacts && (
                            <UpdateContactsSection
                                contacts={formData.contacts}
                                addContact={addContact}
                                removeContact={removeContact}
                                updateContact={updateContact}
                                onSave={handleSaveContacts}
                                loading={loadingContacts}
                                inputClass={INPUT_CLASS}
                                labelClass={LABEL_CLASS}
                            />
                        )}

                        {formData.socialMedia && (
                            <UpdateSocialMediaSection
                                socialMedia={formData.socialMedia}
                                updateNested={updateNested}
                                onSave={handleSaveSocialMedia}
                                loading={loadingSocialMedia}
                                inputClass={INPUT_CLASS}
                                labelClass={LABEL_CLASS}
                            />
                        )}

                        {formData.targetAudience && (
                            <UpdateTargetAudienceSection
                                targetAudience={formData.targetAudience}
                                updateNested={updateNested}
                                onSave={handleSaveTargetAudience}
                                loading={loadingTargetAudience}
                                inputClass={INPUT_CLASS}
                                labelClass={LABEL_CLASS}
                            />
                        )}

                        {formData.seo && (
                            <UpdateSeoSection
                                seo={formData.seo}
                                updateNested={updateNested}
                                onSave={handleSaveSeo}
                                loading={loadingSeo}
                                inputClass={INPUT_CLASS}
                                labelClass={LABEL_CLASS}
                            />
                        )}

                        {formData.apiKeys && (
                            <UpdateApiKeysSection
                                apiKeys={formData.apiKeys}
                                updateNested={updateNested}
                                onSave={handleSaveApiKeys}
                                loading={loadingApiKeys}
                                inputClass={INPUT_CLASS}
                                labelClass={LABEL_CLASS}
                            />
                        )}

                        <UpdateMediaUploadSection
                            logoFiles={logoFiles}
                            setLogoFiles={setLogoFiles}
                            photoFiles={photoFiles}
                            setPhotoFiles={setPhotoFiles}
                            videoFiles={videoFiles}
                            setVideoFiles={setVideoFiles}
                            documentFiles={documentFiles}
                            setDocumentFiles={setDocumentFiles}
                            onUpload={handleUploadNewMedia}
                            loading={uploadingMedia}
                            inputClass={INPUT_CLASS}
                            labelClass={LABEL_CLASS}
                        />

                        {allMedia.length > 0 && (
                            <MediaGallery
                                allMedia={allMedia}
                                customerId={selectedCustomerId}
                                onMediaUpdate={handleMediaUpdate}
                                onDeleteMedia={handleDeleteMedia}
                                filters={filters}
                                selection={selection}
                            />
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}
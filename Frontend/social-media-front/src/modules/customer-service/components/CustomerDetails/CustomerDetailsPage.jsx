//src/modules/customer-service/components/CustomerDetails/CustomerDetailsPage.jsx
import useCustomerDetails from '../../hooks/useCustomerDetails';
import { MediaGallery } from '../CustomerMedia/components';
import {
  useMediaSelection,
  useMediaFilters
} from '../../hooks/customerMediaHooks';
import {  
  CustomerSelector,
  CustomerHeader,
  BasicInfoSection,
  ContactsSection,
  SocialMediaSection,
  TargetAudienceSection,
  SeoSection,
  ApiKeysSection,
  SystemInfoSection
} from './components';

export default function CustomerDetailsPage() {
  const {
    customers,
    selectedCustomer,
    loading,
    error,
    handleSelectCustomer,
    handleMediaUpdate
  } = useCustomerDetails();

  // Media için hook'lar
  const allMedia = selectedCustomer?.media || [];
  const selection = useMediaSelection();
  const filters = useMediaFilters(allMedia);

  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        {error}
      </div>
    );
  }

  return (
    <div className="w-full">
      {/* Müşteri Seç */}
      <CustomerSelector
        customers={customers}
        loading={loading}
        onSelect={handleSelectCustomer}
      />

      {loading && (
        <div className="text-center py-8">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
          <p className="text-indigo-600 mt-4">Yükleniyor...</p>
        </div>
      )}

      {selectedCustomer && !loading && (
        <div className="space-y-6">
          {/* Header */}
          <CustomerHeader customer={selectedCustomer} />

          {/* Temel Bilgiler */}
          <BasicInfoSection customer={selectedCustomer} />

          {/* İletişim Kişileri */}
          <ContactsSection contacts={selectedCustomer.contacts} />

          {/* Sosyal Medya */}
          <SocialMediaSection socialMedia={selectedCustomer.socialMedia} />

          {/* Hedef Kitle */}
          <TargetAudienceSection targetAudience={selectedCustomer.targetAudience} />

          {/* SEO */}
          <SeoSection seo={selectedCustomer.seo} />

          {/* API Keys */}
          <ApiKeysSection apiKeys={selectedCustomer.apiKeys} />

          {/* Medya Galerisi */}
          {allMedia.length > 0 && (
            <MediaGallery 
              allMedia={allMedia}
              customerId={selectedCustomer.id}
              onMediaUpdate={handleMediaUpdate}
              onDeleteMedia={async (mediaId, fileName) => {
                // Silme işlemi varsa burada implement et
                console.log('Delete media:', mediaId, fileName);
                await handleMediaUpdate();
              }}
              filters={filters}
              selection={selection}
            />
          )}

          {/* Sistem Bilgileri */}
          <SystemInfoSection customer={selectedCustomer} />
        </div>
      )}
    </div>
  );
}
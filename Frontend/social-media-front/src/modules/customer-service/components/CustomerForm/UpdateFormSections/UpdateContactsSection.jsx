// modules/customer-service/components/CustomerForm/UpdateFormSections/UpdateContactsSection.jsx
import { ContactItem, SaveButton } from '../FormComponents';

export default function UpdateContactsSection({ 
  contacts,
  addContact,
  removeContact,
  updateContact,
  onSave,
  loading,
  inputClass,
  labelClass 
}) {
  return (
    <section className="bg-gray-50 p-4 sm:p-6 rounded-lg border border-gray-200">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 mb-4">
        <h3 className="text-lg sm:text-xl font-semibold text-gray-800 flex items-center">
          <span className="mr-2">📞</span> İletişim Kişileri
        </h3>
        <button
          type="button"
          onClick={addContact}
          className="w-full sm:w-auto bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition text-sm font-medium"
        >
          + Yeni Kişi
        </button>
      </div>

      {contacts.map((contact, index) => (
        <ContactItem
          key={index}
          contact={contact}
          index={index}
          updateContact={updateContact}
          removeContact={removeContact}
          canRemove={true}
          inputClass={inputClass}
          labelClass={labelClass}
        />
      ))}

      <SaveButton onClick={onSave} loading={loading} />
    </section>
  );
}
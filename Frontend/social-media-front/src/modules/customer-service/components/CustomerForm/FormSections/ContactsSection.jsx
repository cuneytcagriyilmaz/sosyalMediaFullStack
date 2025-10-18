// modules/customer-service/components/CustomerForm/FormSections/ContactsSection.jsx
import { ContactItem } from '../FormComponents';

export default function ContactsSection({ 
  contacts, 
  addContact, 
  removeContact, 
  updateContact,
  inputClass,
  labelClass 
}) {
  return (
    <section className="bg-gray-50 p-6 rounded-lg border border-gray-200">
      <div className="flex justify-between items-center mb-4">
        <h3 className="text-xl font-semibold text-gray-800 flex items-center">
          <span className="mr-2">📞</span> İletişim Kişileri
        </h3>
        <button
          type="button"
          onClick={addContact}
          className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition text-sm font-medium"
        >
          + Yeni Kişi Ekle
        </button>
      </div>

      {contacts.map((contact, index) => (
        <ContactItem
          key={index}
          contact={contact}
          index={index}
          updateContact={updateContact}
          removeContact={removeContact}
          canRemove={index > 0}
          inputClass={inputClass}
          labelClass={labelClass}
        />
      ))}
    </section>
  );
}
// modules/customer-service/components/CustomerForm/FormComponents/ContactItem.jsx

export default function ContactItem({ 
  contact, 
  index, 
  updateContact, 
  removeContact, 
  canRemove,
  inputClass,
  labelClass 
}) {
  return (
    <div className="bg-white p-4 rounded-lg border border-gray-300 mb-4">
      <div className="flex justify-between items-center mb-3">
        <p className="font-medium text-gray-700">Yetkili #{index + 1}</p>
        {canRemove && (
          <button
            type="button"
            onClick={() => removeContact(index)}
            className="bg-red-500 text-white px-3 py-1 rounded text-sm hover:bg-red-600 transition"
          >
            🗑️ Sil
          </button>
        )}
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div>
          <label className={labelClass}>Ad</label>
          <input
            type="text"
            value={contact.name}
            onChange={(e) => updateContact(index, 'name', e.target.value)}
            required
            className={inputClass}
            placeholder="Adı"
          />
        </div>

        <div>
          <label className={labelClass}>Soyad</label>
          <input
            type="text"
            value={contact.surname}
            onChange={(e) => updateContact(index, 'surname', e.target.value)}
            required
            className={inputClass}
            placeholder="Soyadı"
          />
        </div>

        <div>
          <label className={labelClass}>Email</label>
          <input
            type="email"
            value={contact.email}
            onChange={(e) => updateContact(index, 'email', e.target.value)}
            required
            className={inputClass}
            placeholder="email@example.com"
          />
        </div>

        <div>
          <label className={labelClass}>Telefon</label>
          <input
            type="text"
            value={contact.phone}
            onChange={(e) => updateContact(index, 'phone', e.target.value)}
            required
            className={inputClass}
            placeholder="5XX XXX XX XX"
          />
        </div>
      </div>
    </div>
  );
}

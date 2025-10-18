//src/modules/customer-service/components/CustomerDetails/components/ContactsSection.jsx
export default function ContactsSection({ contacts }) {
  if (!contacts || contacts.length === 0) return null;

  return (
    <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
      <h4 className="text-lg font-semibold text-indigo-600 mb-4 flex items-center">
        <span className="mr-2">👥</span> İletişim Kişileri ({contacts.length})
      </h4>
      <div className="space-y-3">
        {contacts
          .sort((a, b) => a.priority - b.priority)
          .map((contact, index) => (
            <div key={contact.id || index} className="bg-gray-50 p-4 rounded-lg border-l-4 border-indigo-500">
              <div className="flex justify-between items-start mb-2">
                <p className="font-semibold text-lg text-gray-800">
                  {contact.name} {contact.surname}
                </p>
                <span className="text-xs bg-indigo-100 text-indigo-700 px-2 py-1 rounded">
                  Öncelik: {contact.priority}
                </span>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-2 text-sm">
                <p className="text-gray-600">
                  <span className="font-medium">📧 Email:</span> {contact.email || '-'}
                </p>
                <p className="text-gray-600">
                  <span className="font-medium">📱 Telefon:</span> {contact.phone || '-'}
                </p>
              </div>
            </div>
          ))}
      </div>
    </section>
  );
}
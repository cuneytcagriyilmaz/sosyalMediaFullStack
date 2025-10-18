//src/modules/customer-service/components/CustomerDetails/components/BasicInfoSection.jsx

export default function BasicInfoSection({ customer }) {
    return (
        <section className="bg-white rounded-lg shadow-md p-6 border border-gray-200">
            <h4 className="text-lg font-semibold text-indigo-600 mb-4 flex items-center">
                <span className="mr-2">🏢</span> Temel Bilgiler
            </h4>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="bg-gray-50 p-4 rounded-lg">
                    <p className="text-xs text-gray-500 uppercase mb-1">Sektör</p>
                    <p className="font-medium text-gray-800">{customer.sector || '-'}</p>
                </div>
                <div className="bg-gray-50 p-4 rounded-lg">
                    <p className="text-xs text-gray-500 uppercase mb-1">Üyelik Paketi</p>
                    <p className="font-medium text-gray-800">{customer.membershipPackage || '-'}</p>
                </div>
                <div className="bg-gray-50 p-4 rounded-lg md:col-span-2">
                    <p className="text-xs text-gray-500 uppercase mb-1">Adres</p>
                    <p className="font-medium text-gray-800">{customer.address || '-'}</p>
                </div>
            </div>
        </section>
    );
}
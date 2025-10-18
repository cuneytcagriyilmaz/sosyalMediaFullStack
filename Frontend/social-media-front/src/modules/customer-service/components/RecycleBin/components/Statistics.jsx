// src/modules/customer-service/components/RecycleBin/components/Statistics.jsx
export default function Statistics({ customers }) {
    const totalContacts = customers.reduce((sum, c) => sum + (c.contacts?.length || 0), 0);
    const totalMedia = customers.reduce((sum, c) => sum + (c.media?.length || 0), 0);

    return (
        <div className="mt-8 bg-white rounded-xl shadow-lg p-6">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 text-center">
                <div>
                    <div className="text-3xl font-bold text-gray-800">{customers.length}</div>
                    <div className="text-sm text-gray-600 mt-1">Silinmiş Müşteri</div>
                </div>
                <div>
                    <div className="text-3xl font-bold text-blue-600">{totalContacts}</div>
                    <div className="text-sm text-gray-600 mt-1">Toplam Yetkili</div>
                </div>
                <div>
                    <div className="text-3xl font-bold text-purple-600">{totalMedia}</div>
                    <div className="text-sm text-gray-600 mt-1">Toplam Medya</div>
                </div>
            </div>
        </div>
    );
}
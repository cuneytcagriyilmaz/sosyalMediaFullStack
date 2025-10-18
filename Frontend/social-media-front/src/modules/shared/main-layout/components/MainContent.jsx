// MainContent.jsx
import CustomerAddForm from "../../../customer-service/CustomerAddForm";
import CustomerDeleteForm from "../../../customer-service/CustomerDeleteForm";
import CustomerUpdateForm from "../../../customer-service/CustomerUpdateForm";
import CustomerView from "../../../customer-service/CustomerView";
import CustomerRestore from "../../../customer-service/CustomerRestore";



export default function MainContent({ activeMenu }) {
    return (
        <main className="flex-1 p-6 bg-gray-50">

            {/* Takvim */}
            {activeMenu === "takvim" && (
                <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
                    <h2 className="text-lg font-semibold text-indigo-700 mb-2">📅 Takvim</h2>
                    <div className="h-64 flex items-center justify-center text-gray-800">
                        Takvim bileşeni buraya gelecek
                    </div>
                </section>
            )}

            {/* Post Yönetimi */}
            {activeMenu === "post" && (
                <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
                    <h2 className="text-lg font-semibold text-indigo-700 mb-2">📝 Post Yönetimi</h2>
                    <div className="h-64 flex items-center justify-center text-gray-800">
                        Post listesi / oluşturma ekranı buraya gelecek
                    </div>
                </section>
            )}

            {/* Müşteri Görüntüleme */}
            {activeMenu === "musteriGoruntule" && (
                <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
                    <h2 className="text-lg font-semibold text-indigo-700 mb-2">👥 Müşteri Görüntüleme</h2>

                    <CustomerView />
                </section>
            )}

            {/* Müşteri Ekle */}
            {activeMenu === "musteriEkle" && (
                <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
                    <h2 className="text-lg font-semibold text-indigo-700 mb-4">👥 Müşteri Ekle</h2>
                    <CustomerAddForm />
                </section>
            )}


            {/* Müşteri Sil */}
            {activeMenu === "musteriSil" && (
                <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
                    <h2 className="text-lg font-semibold text-indigo-700 mb-4">👥 Müşteri Sil</h2>
                    <CustomerDeleteForm />
                </section>
            )}




            {/* Müşteri Güncelle */}
            {activeMenu === "musteriGuncelle" && (
                <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
                    <h2 className="text-lg font-semibold text-indigo-700 mb-4">👥 Müşteri Güncelle</h2>
                    <CustomerUpdateForm />
                </section>
            )}

            {activeMenu === "silinimisMusteriler" && (
                <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
                    <CustomerRestore />
                </section>
            )}

            {activeMenu === "medyaYonetimi" && (
                <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
                    <CustomerMediaManager />
                </section>
            )}

            {/* Ayarlar */}
            {activeMenu === "ayarlar" && (
                <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
                    <h2 className="text-lg font-semibold text-indigo-700 mb-2">⚙️ Ayarlar</h2>
                    <div className="h-64 flex items-center justify-center text-gray-800">
                        Ayarlar bileşeni buraya gelecek
                    </div>
                </section>
            )}

        </main>
    );
}

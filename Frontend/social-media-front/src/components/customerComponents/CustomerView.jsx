import { useState } from "react";
import fakeCustomers from "../../data/fakeCustomers";

export default function CustomerView() {
    const [selectedCustomer, setSelectedCustomer] = useState(null);

    return (
        <div className="w-full">
            {/* Müşteri seçme dropdown */}
            <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">
                    Müşteri Seç
                </label>
                <select
                    className="w-full border border-gray-300 rounded-lg p-2 bg-white text-black"
                    onChange={(e) =>
                        setSelectedCustomer(
                            fakeCustomers.find((c) => c.id === Number(e.target.value))
                        )
                    }
                >
                    <option value="">-- Seçiniz --</option>
                    {fakeCustomers.map((customer) => (
                        <option key={customer.id} value={customer.id} className="text-black">
                            {customer.companyName}
                        </option>
                    ))}
                </select>
            </div>


            {/* Müşteri bilgileri */}
            {selectedCustomer && (
                <div className="bg-white border rounded-lg shadow-md p-4 space-y-4 text-gray-800">
                    <h3 className="text-xl font-semibold text-indigo-700">
                        {selectedCustomer.companyName}
                    </h3>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <p><strong>Sektör:</strong> {selectedCustomer.sector}</p>
                        <p><strong>Adres:</strong> {selectedCustomer.address}</p>
                        <p><strong>Instagram:</strong> {selectedCustomer.instagram}</p>
                        <p><strong>Facebook:</strong> {selectedCustomer.facebook}</p>
                        <p><strong>TikTok:</strong> {selectedCustomer.tiktok}</p>
                        <p><strong>Post Sıklığı:</strong> {selectedCustomer.postFrequency}</p>
                        <p><strong>Post Türü:</strong> {selectedCustomer.postType}</p>
                        <p><strong>Post Tonu:</strong> {selectedCustomer.postTone}</p>
                        <p><strong>Hashtagler:</strong> {selectedCustomer.customerHashtags}</p>
                        <p><strong>Google Console Email:</strong> {selectedCustomer.googleConsoleEmail}</p>
                        <p><strong>SEO Başlık Önerisi:</strong> {selectedCustomer.seoTitleSuggestions}</p>
                        <p><strong>SEO İçerik Önerisi:</strong> {selectedCustomer.seoContentSuggestions}</p>
                        <p><strong>Hedef Bölge:</strong> {selectedCustomer.targetRegion}</p>
                        <p><strong>Özel Günler:</strong> {selectedCustomer.specialDates ? "Var" : "Yok"}</p>
                        <p><strong>Hedef Yaş:</strong> {selectedCustomer.audienceAge}</p>
                        <p><strong>Hedef İlgi Alanları:</strong> {selectedCustomer.audienceInterests}</p>
                        <p><strong>Instagram API Key:</strong> {selectedCustomer.instagramApiKey}</p>
                        <p><strong>Facebook API Key:</strong> {selectedCustomer.facebookApiKey}</p>
                        <p><strong>TikTok API Key:</strong> {selectedCustomer.tiktokApiKey}</p>
                        <p><strong>Google API Key:</strong> {selectedCustomer.googleApiKey}</p>
                        <p><strong>Üyelik Paketi:</strong> {selectedCustomer.membershipPackage}</p>
                    </div>

                    {/* İletişim Kişileri */}
                    <div className="mt-4">
                        <h4 className="font-semibold text-indigo-600">İletişim Kişileri</h4>
                        <ul className="list-disc pl-6">
                            <li>
                                {selectedCustomer.customerContact1Name} -{" "}
                                {selectedCustomer.customerContact1Email} -{" "}
                                {selectedCustomer.customerContact1Phone}
                            </li>
                            <li>
                                {selectedCustomer.customerContact2Name} -{" "}
                                {selectedCustomer.customerContact2Email} -{" "}
                                {selectedCustomer.customerContact2Phone}
                            </li>
                            <li>
                                {selectedCustomer.customerContact3Name} -{" "}
                                {selectedCustomer.customerContact3Email} -{" "}
                                {selectedCustomer.customerContact3Phone}
                            </li>
                        </ul>
                    </div>

                    {/* Logolar */}
                    <div className="mt-4">
                        <h4 className="font-semibold text-indigo-600">Müşteri Logoları</h4>
                        <div className="flex gap-2">
                            {selectedCustomer.customerLogos.map((logo, idx) => (
                                <img
                                    key={idx}
                                    src={logo}
                                    alt={`Logo ${idx + 1}`}
                                    className="w-16 h-16 object-contain border rounded"
                                />
                            ))}
                        </div>
                    </div>

                    {/* Fotoğraflar */}
                    <div className="mt-4">
                        <h4 className="font-semibold text-indigo-600">Müşteri Fotoğrafları</h4>
                        <div className="grid grid-cols-2 md:grid-cols-3 gap-2">
                            {selectedCustomer.customerPhotos.map((photo, idx) => (
                                <img
                                    key={idx}
                                    src={photo}
                                    alt={`Foto ${idx + 1}`}
                                    className="w-full h-32 object-cover border rounded"
                                />
                            ))}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

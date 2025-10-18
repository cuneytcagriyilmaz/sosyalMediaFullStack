// src/components/customerComponents/CustomerMediaComponent/ActionBar.jsx
import { useState } from 'react';
import customerService from '../../customer-service/services/customerService';

export default function ActionBar({ selectedCount, selectedFiles, customerId, onClearSelection, onSuccess }) {
    const [loading, setLoading] = useState(false);
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

    // ZIP olarak toplu indirme
    const handleDownloadZip = async () => {
        setLoading(true);
        try {
            const mediaIds = selectedFiles.map(f => f.id);

            // Backend'den ZIP al
            const response = await customerService.downloadMediaAsZip(customerId, mediaIds);

            // ZIP dosyasını indir
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `musteri-${customerId}-medya.zip`);
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);

            alert(`✅ ${selectedCount} dosya ZIP olarak indirildi!`);
            onClearSelection();
        } catch (error) {
            console.error('ZIP indirme hatası:', error);
            alert('❌ ZIP indirilemedi. Tek tek indirmeyi deneyin.');
        } finally {
            setLoading(false);
        }
    };

    // Toplu silme
    const handleDeleteSelected = async () => {
        setLoading(true);
        try {
            const mediaIds = selectedFiles.map(f => f.id);

            // Her dosyayı sil
            await Promise.all(
                mediaIds.map(id => customerService.deleteMedia(customerId, id))
            );

            alert(`✅ ${selectedCount} dosya silindi!`);
            setShowDeleteConfirm(false);
            onSuccess();
        } catch (error) {
            console.error('Silme hatası:', error);
            alert('❌ Bazı dosyalar silinemedi.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            {/* Fixed Action Bar */}
            <div className="fixed bottom-0 left-0 right-0 bg-white border-t-2 border-indigo-500 shadow-2xl z-50 animate-slide-up">
                <div className="max-w-7xl mx-auto px-6 py-4">
                    <div className="flex items-center justify-between">
                        {/* Sol: Seçim Bilgisi */}
                        <div className="flex items-center gap-4">
                            <div className="flex items-center gap-2">
                                <span className="text-2xl">✅</span>
                                <span className="text-lg font-semibold text-gray-800">
                                    {selectedCount} dosya seçildi
                                </span>
                            </div>

                            <button
                                type="button"
                                onClick={onClearSelection}
                                className="text-sm text-gray-600 hover:text-red-600 transition underline"
                                disabled={loading}
                            >
                                ✕ Seçimi Kaldır
                            </button>
                        </div>

                        {/* Sağ: Aksiyonlar */}
                        <div className="flex items-center gap-3">
                            {/* Toplu İndirme */}
                            <button
                                type="button"
                                onClick={handleDownloadZip}
                                disabled={loading}
                                className="flex items-center gap-2 px-6 py-3 bg-green-600 text-white rounded-lg font-medium hover:bg-green-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                {loading ? (
                                    <>
                                        <span className="animate-spin">⏳</span>
                                        <span>Hazırlanıyor...</span>
                                    </>
                                ) : (
                                    <>
                                        <span>📦</span>
                                        <span>Seçilenleri İndir (ZIP)</span>
                                    </>
                                )}
                            </button>

                            {/* Toplu Silme */}
                            <button
                                type="button"
                                onClick={() => setShowDeleteConfirm(true)}
                                disabled={loading}
                                className="flex items-center gap-2 px-6 py-3 bg-red-600 text-white rounded-lg font-medium hover:bg-red-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                <span>🗑️</span>
                                <span>Seçilenleri Sil</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            {/* Silme Onay Modal */}
            {showDeleteConfirm && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-[60] animate-fade-in">
                    <div className="bg-white rounded-xl shadow-2xl p-6 max-w-md w-full mx-4 animate-scale-in">
                        <div className="text-center">
                            <span className="text-6xl">⚠️</span>
                            <h3 className="text-xl font-bold text-gray-800 mt-4">
                                Emin misiniz?
                            </h3>
                            <p className="text-gray-600 mt-2">
                                <strong>{selectedCount} dosyayı</strong> kalıcı olarak silmek üzeresiniz.
                                Bu işlem geri alınamaz!
                            </p>
                        </div>

                        <div className="flex gap-3 mt-6">
                            <button
                                type="button"
                                onClick={() => setShowDeleteConfirm(false)}
                                disabled={loading}
                                className="flex-1 px-4 py-3 bg-gray-200 text-gray-800 rounded-lg font-medium hover:bg-gray-300 transition disabled:opacity-50"
                            >
                                İptal
                            </button>
                            <button
                                type="button"
                                onClick={handleDeleteSelected}
                                disabled={loading}
                                className="flex-1 px-4 py-3 bg-red-600 text-white rounded-lg font-medium hover:bg-red-700 transition disabled:opacity-50"
                            >
                                {loading ? 'Siliniyor...' : 'Evet, Sil'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}
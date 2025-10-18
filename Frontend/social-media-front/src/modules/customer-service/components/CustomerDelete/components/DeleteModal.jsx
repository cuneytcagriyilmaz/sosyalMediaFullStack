// modules/customer-service/components/CustomerDelete/components/DeleteModal.jsx
import { AlertTriangle } from 'lucide-react';

export default function DeleteModal({ show, count, onConfirm, onCancel }) {
  if (!show) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full p-8 animate-scale-in">
        <div className="flex justify-center mb-6">
          <div className="bg-red-100 p-4 rounded-full">
            <AlertTriangle className="text-red-600" size={48} />
          </div>
        </div>
        
        <h2 className="text-2xl font-bold text-gray-800 text-center mb-4">
          Silme Onayı
        </h2>
        
        <p className="text-gray-600 text-center mb-6">
          <span className="font-semibold text-red-600">{count} müşteri</span> silinecek.
          Bu işlem geri alınabilir (Soft Delete).
        </p>

        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
          <p className="text-sm text-yellow-800">
            ⚠️ Müşteriler pasif duruma getirilir ancak verileri korunur.
          </p>
        </div>

        <div className="flex gap-3">
          <button
            onClick={onCancel}
            className="flex-1 px-6 py-3 bg-gray-200 text-gray-700 rounded-xl font-medium hover:bg-gray-300 transition-all"
          >
            İptal
          </button>
          <button
            onClick={onConfirm}
            className="flex-1 px-6 py-3 bg-red-600 text-white rounded-xl font-medium hover:bg-red-700 transition-all"
          >
            Sil
          </button>
        </div>
      </div>
    </div>
  );
}
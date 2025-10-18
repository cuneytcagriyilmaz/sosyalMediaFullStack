//src/modules/customer-service/components/RecycleBin/components/ActionModal.jsx

import { RotateCcw, Flame } from 'lucide-react';

export default function ActionModal({ show, data, onConfirm, onCancel }) {
  if (!show || !data) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full p-8 animate-scale-in">
        <div className="flex justify-center mb-6">
          <div className={`p-4 rounded-full ${
            data.type === 'restore' ? 'bg-green-100' : 'bg-red-100'
          }`}>
            {data.type === 'restore' ? (
              <RotateCcw className="text-green-600" size={48} />
            ) : (
              <Flame className="text-red-600" size={48} />
            )}
          </div>
        </div>

        <h2 className="text-2xl font-bold text-gray-800 text-center mb-4">
          {data.title}
        </h2>

        <p className="text-gray-600 text-center mb-6">
          {data.message}
        </p>

        {data.isDangerous && (
          <div className="bg-red-50 border-l-4 border-red-500 rounded-lg p-4 mb-6">
            <p className="text-sm text-red-800 font-medium">
              ⚠️ Bu işlem geri alınamaz! Müşteri klasörü ve tüm verileri kalıcı olarak silinecektir.
            </p>
          </div>
        )}

        <div className="flex gap-3">
          <button
            onClick={onCancel}
            className="flex-1 px-6 py-3 bg-gray-200 text-gray-700 rounded-xl font-medium hover:bg-gray-300 transition-all"
          >
            İptal
          </button>
          <button
            onClick={onConfirm}
            className={`flex-1 px-6 py-3 text-white rounded-xl font-medium transition-all ${data.confirmColor}`}
          >
            {data.confirmText}
          </button>
        </div>
      </div>
    </div>
  );
}
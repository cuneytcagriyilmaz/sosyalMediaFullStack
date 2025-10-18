// modules/customer-service/components/CustomerForm/FormComponents/FormActions.jsx
export default function FormActions({ loading }) {
  return (
    <div className="flex justify-end space-x-4 pt-6 border-t">
      <button
        type="button"
        onClick={() => window.location.reload()}
        disabled={loading}
        className="px-6 py-3 bg-gray-300 text-gray-700 rounded-lg font-medium hover:bg-gray-400 transition disabled:opacity-50"
      >
        ↩️ İptal
      </button>
      <button
        type="submit"
        disabled={loading}
        className="px-8 py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition disabled:bg-gray-400 disabled:cursor-not-allowed flex items-center space-x-2"
      >
        {loading ? (
          <>
            <span className="animate-spin">⏳</span>
            <span>Kaydediliyor...</span>
          </>
        ) : (
          <>
            <span>✅</span>
            <span>Müşteriyi Kaydet</span>
          </>
        )}
      </button>
    </div>
  );
}
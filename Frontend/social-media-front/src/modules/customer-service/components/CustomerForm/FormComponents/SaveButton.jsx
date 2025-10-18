// modules/customer-service/components/CustomerForm/FormComponents/SaveButton.jsx
export default function SaveButton({ onClick, loading, text = "Kaydet" }) {
  return (
    <div className="flex justify-end mt-4">
      <button
        type="button"
        onClick={onClick}
        disabled={loading}
        className="w-full sm:w-auto px-4 sm:px-6 py-2.5 rounded-lg font-medium transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center space-x-2 bg-indigo-600 text-white hover:bg-indigo-700"
      >
        {loading ? (
          <>
            <span className="animate-spin">⏳</span>
            <span className="text-sm sm:text-base">Kaydediliyor...</span>
          </>
        ) : (
          <>
            <span>💾</span>
            <span className="text-sm sm:text-base">{text}</span>
          </>
        )}
      </button>
    </div>
  );
}
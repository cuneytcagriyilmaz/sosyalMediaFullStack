// src/modules/analytics-service/components/CustomerNoteModal.jsx

import { useState, useEffect } from 'react';
import { X, Plus, Trash2 } from 'lucide-react';

const createEmptyNote = () => ({
  id: Date.now() + Math.random(),
  customerId: '',
  note: '',
  createdBy: 'Admin'
});

export default function CustomerNoteModal({ 
  isOpen, 
  onClose, 
  onSave, 
  note = null,
  customers = [] 
}) {
  const [notes, setNotes] = useState([createEmptyNote()]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (note) {
      // Edit mode - tek not
      setNotes([{
        id: note.id,
        customerId: note.customerId || '',
        note: note.note || note.text || '',
        createdBy: note.createdBy || 'Admin'
      }]);
    } else {
      // Create mode - boş form
      setNotes([createEmptyNote()]);
    }
    setError(null);
  }, [note, isOpen]);

  const handleFieldChange = (index, field, value) => {
    const newNotes = [...notes];
    newNotes[index] = {
      ...newNotes[index],
      [field]: value
    };
    setNotes(newNotes);
  };

  const handleAddNote = () => {
    setNotes([...notes, createEmptyNote()]);
  };

  const handleRemoveNote = (index) => {
    if (notes.length === 1) {
      alert('En az bir not olmalı!');
      return;
    }
    const newNotes = notes.filter((_, i) => i !== index);
    setNotes(newNotes);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // Validation
      for (let i = 0; i < notes.length; i++) {
        if (!notes[i].customerId || !notes[i].note) {
          setError(`Not ${i + 1}: Lütfen tüm zorunlu alanları doldurun`);
          setLoading(false);
          return;
        }
      }

      if (note) {
        // Edit mode - tek not güncelle
        await onSave(notes[0]);
      } else {
        // Create mode
        for (const noteData of notes) {
          await onSave(noteData);
        }
      }

      onClose();
    } catch (err) {
      console.error('Form submit error:', err);
      setError(err.message || 'Bir hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl shadow-2xl max-w-3xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="bg-gradient-to-r from-blue-600 to-indigo-600 p-6 rounded-t-2xl sticky top-0 z-10">
          <div className="flex items-center justify-between">
            <h2 className="text-2xl font-bold text-white flex items-center gap-3">
              {note ? '✏️ Not Düzenle' : '📝 Not Ekle'}
              {!note && notes.length > 1 && (
                <span className="text-sm font-normal bg-white/20 px-3 py-1 rounded-full">
                  {notes.length} Not
                </span>
              )}
            </h2>
            <button
              onClick={onClose}
              className="text-white hover:bg-white/20 rounded-lg p-2 transition"
            >
              <X size={24} />
            </button>
          </div>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          {/* Error Message */}
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-4 flex items-start gap-3">
              <span className="text-2xl">⚠️</span>
              <div className="flex-1">
                <p className="text-red-800 font-medium">Hata</p>
                <p className="text-red-600 text-sm">{error}</p>
              </div>
            </div>
          )}

          {/* Notes List */}
          <div className="space-y-6">
            {notes.map((noteItem, index) => (
              <div
                key={noteItem.id}
                className="border border-gray-200 rounded-xl p-4 bg-gray-50 relative"
              >
                {/* Remove Button */}
                {!note && notes.length > 1 && (
                  <button
                    type="button"
                    onClick={() => handleRemoveNote(index)}
                    className="absolute top-4 right-4 p-2 text-red-600 hover:bg-red-100 rounded-lg transition"
                    title="Kaldır"
                  >
                    <Trash2 size={18} />
                  </button>
                )}

                {/* Note Number */}
                {!note && notes.length > 1 && (
                  <div className="mb-4">
                    <span className="inline-block px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-sm font-medium">
                      Not {index + 1}
                    </span>
                  </div>
                )}

                <div className="space-y-4">
                  {/* Customer Select */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Müşteri <span className="text-red-500">*</span>
                    </label>
                    <select
                      value={noteItem.customerId}
                      onChange={(e) => handleFieldChange(index, 'customerId', e.target.value)}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white text-gray-900"
                      required
                      disabled={!!note} // Edit modunda değiştirme
                    >
                      <option value="">Müşteri Seçin</option>
                      {customers.map(customer => (
                        <option key={customer.id} value={customer.id}>
                          {customer.companyName || customer.company_name || `Müşteri #${customer.id}`}
                        </option>
                      ))}
                    </select>
                  </div>

                  {/* Note Text */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Not <span className="text-red-500">*</span>
                    </label>
                    <textarea
                      value={noteItem.note}
                      onChange={(e) => handleFieldChange(index, 'note', e.target.value)}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none bg-white text-gray-900"
                      rows={5}
                      placeholder="Not metnini buraya yazın..."
                      required
                    />
                    <p className="text-xs text-gray-500 mt-1">
                      {noteItem.note.length} karakter
                    </p>
                  </div>

                  {/* Created By */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Yazan Kişi
                    </label>
                    <input
                      type="text"
                      value={noteItem.createdBy}
                      onChange={(e) => handleFieldChange(index, 'createdBy', e.target.value)}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white text-gray-900"
                      placeholder="Admin"
                    />
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* Add More Button */}
          {!note && (
            <button
              type="button"
              onClick={handleAddNote}
              className="w-full py-3 border-2 border-dashed border-blue-300 rounded-lg text-blue-600 font-medium hover:bg-blue-50 hover:border-blue-400 transition flex items-center justify-center gap-2"
            >
              <Plus size={20} />
              <span>Yeni Not Ekle</span>
            </button>
          )}

          {/* Action Buttons */}
          <div className="flex gap-3 pt-4 border-t">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-6 py-3 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-gray-50 transition"
              disabled={loading}
            >
              İptal
            </button>
            <button
              type="submit"
              className="flex-1 px-6 py-3 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
              disabled={loading}
            >
              {loading ? (
                <>
                  <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                  <span>Kaydediliyor...</span>
                </>
              ) : (
                <>
                  {note ? (
                    <>💾 Güncelle</>
                  ) : (
                    <>
                      📝 {notes.length === 1 ? 'Kaydet' : `${notes.length} Not Kaydet`}
                    </>
                  )}
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
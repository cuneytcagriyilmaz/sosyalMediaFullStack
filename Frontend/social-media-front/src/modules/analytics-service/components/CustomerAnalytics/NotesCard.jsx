// src/modules/analytics-service/components/CustomerAnalytics/NotesCard.jsx

import { useState } from 'react';
import { formatShortDate } from '../../data/mockHelpers';

export default function NotesCard({ customer, onAddNote }) {
  const [isAdding, setIsAdding] = useState(false);
  const [noteText, setNoteText] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!noteText.trim()) return;

    setLoading(true);
    const result = await onAddNote(noteText);
    
    if (result.success) {
      setNoteText('');
      setIsAdding(false);
    }
    setLoading(false);
  };

  return (
    <div className="bg-white rounded-xl shadow-lg overflow-hidden">
      {/* Header */}
      <div className="bg-gradient-to-r from-blue-600 to-cyan-600 p-6">
        <div className="flex items-center justify-between">
          <h3 className="text-xl font-bold text-white flex items-center gap-2">
            💬 Notlar
          </h3>
          {!isAdding && (
            <button
              onClick={() => setIsAdding(true)}
              className="px-3 py-1 bg-white/20 hover:bg-white/30 text-white text-sm rounded-lg transition"
            >
              + Not Ekle
            </button>
          )}
        </div>
      </div>

      {/* Content */}
      <div className="p-6">
        {/* Add Note Form */}
        {isAdding && (
          <form onSubmit={handleSubmit} className="mb-4 p-4 bg-blue-50 rounded-lg border border-blue-200">
            <textarea
              value={noteText}
              onChange={(e) => setNoteText(e.target.value)}
              placeholder="Not yazın..."
              className="w-full px-3 py-2 border border-blue-300 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-blue-500"
              rows="3"
              disabled={loading}
            />
            <div className="flex gap-2 mt-2">
              <button
                type="submit"
                disabled={loading || !noteText.trim()}
                className="px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? 'Kaydediliyor...' : 'Kaydet'}
              </button>
              <button
                type="button"
                onClick={() => {
                  setIsAdding(false);
                  setNoteText('');
                }}
                disabled={loading}
                className="px-4 py-2 bg-gray-200 text-gray-700 text-sm rounded-lg hover:bg-gray-300 transition disabled:opacity-50"
              >
                İptal
              </button>
            </div>
          </form>
        )}

        {/* Notes List */}
        {customer?.notes && customer.notes.length > 0 ? (
          <div className="space-y-3">
            {customer.notes.map((note) => (
              <div key={note.id} className="p-4 bg-gray-50 rounded-lg border border-gray-200">
                <p className="text-sm text-gray-800 mb-2">{note.text}</p>
                <div className="flex items-center justify-between text-xs text-gray-500">
                  <span>{note.createdBy}</span>
                  <span>{formatShortDate(note.createdAt)}</span>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="text-center py-8 text-gray-500">
            <span className="text-4xl">📝</span>
            <p className="mt-2 text-sm">Henüz not eklenmemiş</p>
          </div>
        )}
      </div>
    </div>
  );
}
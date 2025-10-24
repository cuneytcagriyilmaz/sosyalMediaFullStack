// src/modules/analytics-service/components/CustomerAnalytics/NotesCard.jsx

import { useState, useEffect } from 'react';
import { Plus, Edit, Trash2, StickyNote } from 'lucide-react';
import analyticsService from '../../services/analyticsService';

export default function NotesCard({ customer, onAddNote }) {
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingNote, setEditingNote] = useState(null);
  const [noteText, setNoteText] = useState('');
  const [createdBy, setCreatedBy] = useState('Admin');

  // Component mount olduğunda notları getir
  useEffect(() => {
    if (customer?.id) {
      fetchNotes();
    }
  }, [customer]);

  /**
   * Müşterinin notlarını getir
   */
  const fetchNotes = async () => {
    try {
      setLoading(true);
      const response = await analyticsService.getCustomerNotes(customer.id);
      if (response.success) {
        setNotes(response.data || []);
      }
    } catch (error) {
      console.error('Notlar yüklenemedi:', error);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Modal aç (yeni not veya düzenleme)
   */
  const handleOpenModal = (note = null) => {
    if (note) {
      setEditingNote(note);
      setNoteText(note.note || note.text || '');
      setCreatedBy(note.createdBy || 'Admin');
    } else {
      setEditingNote(null);
      setNoteText('');
      setCreatedBy('Admin');
    }
    setShowModal(true);
  };

  /**
   * Modal kapat
   */
  const handleCloseModal = () => {
    setShowModal(false);
    setEditingNote(null);
    setNoteText('');
    setCreatedBy('Admin');
  };

  /**
   * Not kaydet (yeni veya güncelleme)
   */
  const handleSaveNote = async () => {
    if (!noteText.trim()) {
      alert('Not boş olamaz!');
      return;
    }

    try {
      if (editingNote) {
        // UPDATE
        const response = await analyticsService.updateCustomerNote(editingNote.id, {
          note: noteText,
          createdBy: createdBy
        });

        if (response.success) {
          await fetchNotes();
          handleCloseModal();
          alert('✅ Not güncellendi!');
        } else {
          alert('❌ Hata: ' + response.error);
        }
      } else {
        // CREATE
        const response = await analyticsService.addCustomerNote(
          customer.id,
          noteText,
          createdBy
        );

        if (response.success) {
          await fetchNotes();
          handleCloseModal();
          if (onAddNote) onAddNote(); // Parent'ı bilgilendir
          alert('✅ Not eklendi!');
        } else {
          alert('❌ Hata: ' + response.error);
        }
      }
    } catch (error) {
      console.error('Not kaydedilemedi:', error);
      alert('❌ Bir hata oluştu!');
    }
  };

  /**
   * Not sil
   */
  const handleDeleteNote = async (noteId) => {
    if (!confirm('Bu notu silmek istediğinize emin misiniz?')) {
      return;
    }

    try {
      const response = await analyticsService.deleteCustomerNote(noteId);
      if (response.success) {
        await fetchNotes();
        alert('✅ Not silindi!');
      } else {
        alert('❌ Hata: ' + response.error);
      }
    } catch (error) {
      console.error('Not silinemedi:', error);
      alert('❌ Bir hata oluştu!');
    }
  };

  return (
    <>
      {/* Card */}
      <div className="bg-white rounded-xl shadow-lg overflow-hidden">
        {/* Header */}
        <div className="bg-gradient-to-r from-yellow-400 to-orange-400 p-4 flex items-center justify-between">
          <h3 className="text-lg font-semibold text-white flex items-center gap-2">
            <StickyNote size={20} />
            Müşteri Notları
          </h3>
          <button
            onClick={() => handleOpenModal()}
            className="p-2 bg-white/20 hover:bg-white/30 rounded-lg transition"
            title="Yeni Not"
          >
            <Plus size={18} className="text-white" />
          </button>
        </div>

        {/* Body */}
        <div className="p-4">
          {loading ? (
            <div className="text-center py-8">
              <div className="inline-block animate-spin rounded-full h-8 w-8 border-4 border-orange-500 border-t-transparent"></div>
              <p className="text-gray-600 text-sm mt-2">Yükleniyor...</p>
            </div>
          ) : notes.length === 0 ? (
            <div className="text-center py-8">
              <StickyNote className="mx-auto text-gray-300" size={48} />
              <p className="text-gray-500 text-sm mt-2">Henüz not eklenmemiş</p>
              <button
                onClick={() => handleOpenModal()}
                className="mt-4 px-4 py-2 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition text-sm"
              >
                İlk Notu Ekle
              </button>
            </div>
          ) : (
            <div className="space-y-3 max-h-80 overflow-y-auto">
              {notes.map((note) => (
                <div
                  key={note.id}
                  className="bg-yellow-50 border border-yellow-200 rounded-lg p-3 hover:shadow-md transition"
                >
                  <div className="flex items-start justify-between gap-2">
                    <p className="text-sm text-gray-700 flex-1 whitespace-pre-wrap">
                      {note.note || note.text}
                    </p>
                    <div className="flex gap-1 flex-shrink-0">
                      <button
                        onClick={() => handleOpenModal(note)}
                        className="p-1 text-blue-600 hover:bg-blue-100 rounded transition"
                        title="Düzenle"
                      >
                        <Edit size={14} />
                      </button>
                      <button
                        onClick={() => handleDeleteNote(note.id)}
                        className="p-1 text-red-600 hover:bg-red-100 rounded transition"
                        title="Sil"
                      >
                        <Trash2 size={14} />
                      </button>
                    </div>
                  </div>
                  <div className="flex items-center justify-between mt-2 pt-2 border-t border-yellow-200">
                    <span className="text-xs text-gray-500">
                      📝 {note.createdBy || 'Admin'}
                    </span>
                    <span className="text-xs text-gray-500">
                      {new Date(note.createdAt).toLocaleDateString('tr-TR')}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Footer */}
        {notes.length > 0 && (
          <div className="bg-gray-50 px-4 py-3 border-t text-center">
            <p className="text-xs text-gray-600">
              Toplam {notes.length} not
            </p>
          </div>
        )}
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl shadow-2xl max-w-2xl w-full">
            {/* Modal Header */}
            <div className="bg-gradient-to-r from-yellow-400 to-orange-400 p-6 rounded-t-2xl">
              <h2 className="text-2xl font-bold text-white">
                {editingNote ? '✏️ Not Düzenle' : '📝 Yeni Not'}
              </h2>
            </div>

            {/* Modal Body */}
            <div className="p-6 space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Not <span className="text-red-500">*</span>
                </label>
                <textarea
                  value={noteText}
                  onChange={(e) => setNoteText(e.target.value)}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent resize-none bg-white text-gray-900"
                  rows={6}
                  placeholder="Not metnini buraya yazın..."
                  autoFocus
                />
                <p className="text-xs text-gray-500 mt-1">
                  {noteText.length} karakter
                </p>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Yazan Kişi
                </label>
                <input
                  type="text"
                  value={createdBy}
                  onChange={(e) => setCreatedBy(e.target.value)}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent bg-white text-gray-900"
                  placeholder="Admin"
                />
              </div>
            </div>

            {/* Modal Footer */}
            <div className="flex gap-3 p-6 border-t">
              <button
                onClick={handleCloseModal}
                className="flex-1 px-6 py-3 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-gray-50 transition"
              >
                İptal
              </button>
              <button
                onClick={handleSaveNote}
                className="flex-1 px-6 py-3 bg-orange-500 text-white rounded-lg font-medium hover:bg-orange-600 transition"
              >
                {editingNote ? '💾 Güncelle' : '📝 Kaydet'}
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
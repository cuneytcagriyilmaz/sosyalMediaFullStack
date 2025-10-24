// src/modules/analytics-service/pages/CustomerNotesPage.jsx

import { useState, useEffect } from 'react';
import analyticsService from '../services/analyticsService';
 import { getRelativeTime } from '../data/mockHelpers';
 import { Plus, Edit, Trash2, FileText, User } from 'lucide-react';
import customerService from '../../customer-service/services/customerService';
import CustomerNoteModal from '../components/Modals/CustomerNoteModal';

export default function CustomerNotesPage() {
  const [notes, setNotes] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedCustomer, setSelectedCustomer] = useState('ALL');

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingNote, setEditingNote] = useState(null);

  useEffect(() => {
    fetchCustomers();
  }, []);

  useEffect(() => {
    if (customers.length > 0) {
      fetchNotes();
    }
  }, [selectedCustomer, customers]);

  const fetchCustomers = async () => {
    try {
      const response = await customerService.getAllCustomers();
      if (response.success && response.data) {
        setCustomers(response.data);
        console.log('✅ Customers loaded:', response.data.length);
      }
    } catch (err) {
      console.error('❌ Müşteriler yüklenemedi:', err);
    }
  };

  const fetchNotes = async () => {
    setLoading(true);
    setError(null);

    try {
      let allNotes = [];

      if (selectedCustomer === 'ALL') {
        // Tüm müşterilerin notlarını getir
        for (const customer of customers) {
          const response = await analyticsService.getCustomerNotes(customer.id);
          if (response.success && response.data) {
            // Müşteri bilgisini ekle
            const notesWithCustomer = response.data.map(note => ({
              ...note,
              customerName: customer.companyName || customer.company_name || `Müşteri #${customer.id}`,
              customerId: customer.id
            }));
            allNotes = [...allNotes, ...notesWithCustomer];
          }
        }
      } else {
        // Tek müşterinin notlarını getir
        const response = await analyticsService.getCustomerNotes(parseInt(selectedCustomer));
        if (response.success && response.data) {
          const customer = customers.find(c => c.id === parseInt(selectedCustomer));
          allNotes = response.data.map(note => ({
            ...note,
            customerName: customer?.companyName || customer?.company_name || `Müşteri #${selectedCustomer}`,
            customerId: parseInt(selectedCustomer)
          }));
        }
      }

      // Tarihe göre sırala (yeniden eskiye)
      allNotes.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      
      setNotes(allNotes);
    } catch (err) {
      console.error('Notlar yüklenemedi:', err);
      setError('Veriler yüklenirken hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  const handleAddNote = async (noteData) => {
    const response = await analyticsService.addCustomerNote(
      noteData.customerId,
      noteData.note,
      noteData.createdBy || 'Admin'
    );
    
    if (response.success) {
      await fetchNotes();
      alert('✅ Not başarıyla eklendi!');
    } else {
      alert('❌ Hata: ' + response.error);
      throw new Error(response.error);
    }
  };

  const handleEditNote = async (noteData) => {
    if (!editingNote) {
      alert('⚠️ Düzenlenecek not seçilmedi!');
      return;
    }

    const response = await analyticsService.updateCustomerNote(editingNote.id, {
      note: noteData.note,
      createdBy: noteData.createdBy || 'Admin'
    });
    
    if (response.success) {
      await fetchNotes();
      alert('✅ Not başarıyla güncellendi!');
    } else {
      alert('❌ Hata: ' + response.error);
      throw new Error(response.error);
    }
  };

  const handleDeleteNote = async (noteId) => {
    if (!confirm('Bu notu silmek istediğinize emin misiniz?')) {
      return;
    }

    const response = await analyticsService.deleteCustomerNote(noteId);
    
    if (response.success) {
      await fetchNotes();
      alert('✅ Not başarıyla silindi!');
    } else {
      alert('❌ Hata: ' + response.error);
    }
  };

  const stats = {
    total: notes.length,
    customers: new Set(notes.map(n => n.customerId)).size,
    thisWeek: notes.filter(n => {
      const noteDate = new Date(n.createdAt);
      const weekAgo = new Date();
      weekAgo.setDate(weekAgo.getDate() - 7);
      return noteDate >= weekAgo;
    }).length
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
        <div className="max-w-7xl mx-auto">
          <div className="flex justify-center items-center py-20">
            <div className="text-center">
              <div className="inline-block animate-spin rounded-full h-12 w-12 border-4 border-blue-500 border-t-transparent"></div>
              <p className="text-blue-600 mt-4 font-medium">Notlar yükleniyor...</p>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
        <div className="max-w-7xl mx-auto">
          <div className="bg-white rounded-2xl shadow-lg p-8 text-center">
            <span className="text-6xl">⚠️</span>
            <h2 className="text-xl font-bold text-gray-800 mt-4">Bir Hata Oluştu</h2>
            <p className="text-gray-600 mt-2">{error}</p>
            <button
              onClick={fetchNotes}
              className="mt-6 px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
            >
              Tekrar Dene
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 p-4 md:p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-800 flex items-center gap-3">
              <FileText className="text-blue-600" size={32} />
              Müşteri Notları
            </h1>
            <p className="text-gray-600 mt-1">
              Müşterileriniz hakkında notlar tutun
            </p>
          </div>
          <div className="flex gap-2 flex-wrap">
            <button
              onClick={() => {
                setEditingNote(null);
                setIsModalOpen(true);
              }}
              className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-all duration-200 flex items-center gap-2 shadow-md"
            >
              <Plus size={20} />
              Yeni Not
            </button>
            <button
              onClick={fetchNotes}
              className="px-4 py-2 bg-white text-blue-600 border border-blue-200 rounded-lg hover:bg-blue-50 transition-all duration-200 flex items-center gap-2 shadow-sm"
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
              Yenile
            </button>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="bg-white rounded-xl shadow-lg p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm">Toplam Not</p>
                <p className="text-3xl font-bold text-gray-800 mt-1">{stats.total}</p>
              </div>
              <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                <FileText className="text-blue-600" size={24} />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm">Müşteri Sayısı</p>
                <p className="text-3xl font-bold text-gray-800 mt-1">{stats.customers}</p>
              </div>
              <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                <User className="text-green-600" size={24} />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-lg p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm">Bu Hafta</p>
                <p className="text-3xl font-bold text-gray-800 mt-1">{stats.thisWeek}</p>
              </div>
              <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center">
                <span className="text-2xl">📅</span>
              </div>
            </div>
          </div>
        </div>

        {/* Filter */}
        <div className="bg-white rounded-xl shadow-lg p-4">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Müşteri Filtresi
          </label>
          <select
            value={selectedCustomer}
            onChange={(e) => setSelectedCustomer(e.target.value)}
            className="w-full md:w-96 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-900 bg-white"
          >
            <option value="ALL">Tüm Müşteriler</option>
            {customers.map(customer => (
              <option key={customer.id} value={customer.id}>
                {customer.companyName || customer.company_name || `Müşteri #${customer.id}`}
              </option>
            ))}
          </select>
        </div>

        {/* Notes List */}
        {notes.length === 0 ? (
          <div className="bg-white rounded-xl shadow-lg p-12 text-center">
            <FileText className="mx-auto text-gray-400" size={64} />
            <p className="text-gray-700 mt-4 font-medium">Not bulunamadı</p>
            <p className="text-sm text-gray-500 mt-2">
              {selectedCustomer === 'ALL' 
                ? 'Henüz hiç not eklenmemiş' 
                : 'Bu müşteri için not bulunmuyor'}
            </p>
            <button
              onClick={() => setIsModalOpen(true)}
              className="mt-6 px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
            >
              İlk Notu Ekle
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {notes.map((note) => (
              <div
                key={note.id}
                className="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-200 overflow-hidden"
              >
                {/* Card Header */}
                <div className="bg-gradient-to-r from-blue-50 to-indigo-50 p-4 border-b">
                  <div className="flex items-start justify-between">
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2">
                        <User className="text-blue-600 flex-shrink-0" size={16} />
                        <p className="text-sm font-medium text-gray-900 truncate">
                          {note.customerName}
                        </p>
                      </div>
                      <p className="text-xs text-gray-500 mt-1">
                        {getRelativeTime(note.createdAt)}
                      </p>
                    </div>
                    <div className="flex gap-1 ml-2">
                      <button
                        onClick={() => {
                          setEditingNote(note);
                          setIsModalOpen(true);
                        }}
                        className="p-2 text-blue-600 hover:bg-blue-100 rounded-lg transition"
                        title="Düzenle"
                      >
                        <Edit size={16} />
                      </button>
                      <button
                        onClick={() => handleDeleteNote(note.id)}
                        className="p-2 text-red-600 hover:bg-red-100 rounded-lg transition"
                        title="Sil"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>
                </div>

                {/* Card Body */}
                <div className="p-4">
                  <p className="text-gray-700 text-sm leading-relaxed whitespace-pre-wrap">
                    {note.note || note.text}
                  </p>
                </div>

                {/* Card Footer */}
                <div className="px-4 py-3 bg-gray-50 border-t">
                  <div className="flex items-center justify-between text-xs text-gray-500">
                    <span>📝 {note.createdBy || 'Admin'}</span>
                    <span>{new Date(note.createdAt).toLocaleDateString('tr-TR')}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Note Modal */}
      <CustomerNoteModal
        isOpen={isModalOpen}
        onClose={() => {
          setIsModalOpen(false);
          setEditingNote(null);
        }}
        onSave={editingNote ? handleEditNote : handleAddNote}
        note={editingNote}
        customers={customers}
      />
    </div>
  );
}
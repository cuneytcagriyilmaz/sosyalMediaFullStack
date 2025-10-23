// src/modules/analytics-service/components/CustomerAnalytics/OnboardingProgressBoard.jsx

import { useState } from 'react';
import { getStatusLabel, getStatusColor, getPlatformIcon } from '../../data/mockHelpers';
import TaskEditModal from './TaskEditModal';
import AddOnboardingTaskModal from '../Modals/AddOnboardingTaskModal';
import analyticsService from '../../services/analyticsService';

export default function OnboardingProgressBoard({ tasks, onUpdateTask, customerId, onRefresh }) {
  const [selectedTask, setSelectedTask] = useState(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);

  const handleEditTask = (task, e) => {
    e.stopPropagation();
    setSelectedTask(task);
    setIsEditModalOpen(true);
  };

  const handleDeleteTask = async (taskId, e) => {
    e.stopPropagation();
    
    if (!window.confirm('Bu görevi silmek istediğinize emin misiniz?')) {
      return;
    }

    try {
      const result = await analyticsService.deleteOnboardingTask(taskId);
      if (result.success) {
        onRefresh();
      }
    } catch (error) {
      console.error('Task silinemedi:', error);
    }
  };

  const handleSaveTask = async (taskId, updateData) => {
    const result = await onUpdateTask(taskId, updateData);
    if (result.success) {
      setIsEditModalOpen(false);
      onRefresh();
    }
  };

  const handleAddTask = async (taskData) => {
    try {
      const result = await analyticsService.addOnboardingTask(customerId, taskData);
      if (result.success) {
        setIsAddModalOpen(false);
        onRefresh();
      }
      return result;
    } catch (error) {
      return { success: false, error: 'Görev eklenemedi' };
    }
  };

  return (
    <>
      <div className="bg-white rounded-xl shadow-lg overflow-hidden">
        {/* Header */}
        <div className="bg-gradient-to-r from-green-600 to-teal-600 p-6">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-xl font-bold text-white flex items-center gap-2">
                🚀 Onboarding Durumu
              </h3>
              <p className="text-green-100 text-sm mt-1">
                Müşteri kurulum sürecini takip edin
              </p>
            </div>
            {/* Ekle Butonu */}
            <button
              onClick={() => setIsAddModalOpen(true)}
              className="px-4 py-2 bg-white text-green-600 rounded-lg hover:bg-green-50 transition-all duration-200 flex items-center gap-2 font-medium shadow-md hover:shadow-lg"
            >
              <span className="text-xl">+</span>
              Yeni Görev
            </button>
          </div>
        </div>

        {/* Tasks List */}
        <div className="p-6 space-y-3">
          {!tasks || tasks.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              <span className="text-6xl">📋</span>
              <p className="mt-4 font-medium text-gray-700">Henüz görev eklenmemiş</p>
              <p className="text-sm text-gray-500 mt-2">Yukarıdaki "Yeni Görev" butonunu kullanarak görev ekleyin</p>
              <button
                onClick={() => setIsAddModalOpen(true)}
                className="mt-4 px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition"
              >
                + İlk Görev Ekle
              </button>
            </div>
          ) : (
            tasks.map((task) => (
              <div
                key={task.id}
                className="p-4 border-2 border-gray-200 rounded-lg hover:border-green-300 hover:shadow-md transition-all duration-200 group"
              >
                <div className="flex items-start justify-between mb-2">
                  <div className="flex-1">
                    <h4 className="font-semibold text-gray-800 flex items-center gap-2">
                      {task.platform && getPlatformIcon(task.platform)}
                      {task.status === 'COMPLETED' && '✅'}
                      {task.status === 'NOT_STARTED' && '⏸️'}
                      {task.taskName}
                    </h4>
                    {task.notes && (
                      <p className="text-xs text-gray-500 mt-1">{task.notes}</p>
                    )}
                  </div>
                  
                  {/* Aksiyon Butonları */}
                  <div className="flex items-center gap-2 ml-4">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium border ${getStatusColor(task.status)}`}>
                      {getStatusLabel(task.status)}
                    </span>
                    
                    {/* Düzenle Butonu */}
                    <button
                      onClick={(e) => handleEditTask(task, e)}
                      className="p-2 text-blue-600 hover:bg-blue-50 rounded-lg transition opacity-0 group-hover:opacity-100"
                      title="Düzenle"
                    >
                      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                      </svg>
                    </button>
                    
                    {/* Sil Butonu */}
                    <button
                      onClick={(e) => handleDeleteTask(task.id, e)}
                      className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition opacity-0 group-hover:opacity-100"
                      title="Sil"
                    >
                      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                      </svg>
                    </button>
                  </div>
                </div>

                {/* Connection Date */}
                {task.connectionDate && (
                  <div className="text-xs text-gray-500 mt-2">
                    🔗 Bağlantı Tarihi: {new Date(task.connectionDate).toLocaleDateString('tr-TR', {
                      day: 'numeric',
                      month: 'long',
                      year: 'numeric'
                    })}
                  </div>
                )}
              </div>
            ))
          )}
        </div>

        {/* Progress Summary */}
        {tasks && tasks.length > 0 && (
          <div className="bg-gray-50 px-6 py-4 border-t">
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-700 font-medium">Tamamlanan Görevler</span>
              <span className="text-sm font-semibold text-gray-800">
                {tasks.filter(t => t.status === 'COMPLETED').length} / {tasks.length}
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2 mt-2">
              <div
                className="bg-green-500 h-2 rounded-full transition-all duration-300"
                style={{ 
                  width: `${(tasks.filter(t => t.status === 'COMPLETED').length / tasks.length) * 100}%` 
                }}
              ></div>
            </div>
          </div>
        )}
      </div>

      {/* Edit Modal */}
      {isEditModalOpen && selectedTask && (
        <TaskEditModal
          task={selectedTask}
          onClose={() => setIsEditModalOpen(false)}
          onSave={handleSaveTask}
          title="Onboarding Görevi Düzenle"
        />
      )}

      {/* Add Modal */}
      {isAddModalOpen && (
        <AddOnboardingTaskModal
          customerId={customerId}
          onClose={() => setIsAddModalOpen(false)}
          onSave={handleAddTask}
        />
      )}
    </>
  );
}
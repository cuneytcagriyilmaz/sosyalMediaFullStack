// src/modules/analytics-service/components/CustomerAnalytics/OnboardingProgressBoard.jsx

import { useState } from 'react';
import { getStatusLabel, getStatusColor, getPlatformIcon } from '../../data/mockHelpers';
import TaskEditModal from './TaskEditModal';

export default function OnboardingProgressBoard({ tasks, onUpdateTask }) {
  const [selectedTask, setSelectedTask] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleEditTask = (task) => {
    setSelectedTask(task);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedTask(null);
    setIsModalOpen(false);
  };

  const handleSaveTask = async (taskId, updateData) => {
    const result = await onUpdateTask(taskId, updateData);
    if (result.success) {
      handleCloseModal();
    }
  };

  if (!tasks || tasks.length === 0) {
    return (
      <div className="bg-white rounded-xl shadow-lg p-6">
        <h3 className="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
          🚀 Onboarding Durumu
        </h3>
        <div className="text-center py-8 text-gray-500">
          <span className="text-4xl">📋</span>
          <p className="mt-2">Henüz görev eklenmemiş</p>
        </div>
      </div>
    );
  }

  return (
    <>
      <div className="bg-white rounded-xl shadow-lg overflow-hidden">
        {/* Header */}
        <div className="bg-gradient-to-r from-green-600 to-teal-600 p-6">
          <h3 className="text-xl font-bold text-white flex items-center gap-2">
            🚀 Onboarding Durumu
          </h3>
          <p className="text-green-100 text-sm mt-1">
            Müşteri kurulum sürecini takip edin
          </p>
        </div>

        {/* Tasks List */}
        <div className="p-6 space-y-3">
          {tasks.map((task) => (
            <div
              key={task.id}
              className="p-4 border-2 border-gray-200 rounded-lg hover:border-green-300 hover:shadow-md transition-all duration-200 cursor-pointer"
              onClick={() => handleEditTask(task)}
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
                <span className={`px-3 py-1 rounded-full text-xs font-medium border ${getStatusColor(task.status)}`}>
                  {getStatusLabel(task.status)}
                </span>
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
          ))}
        </div>

        {/* Progress Summary */}
        <div className="bg-gray-50 px-6 py-4 border-t">
          <div className="flex items-center justify-between">
            <span className="text-sm text-gray-600">Tamamlanan Görevler</span>
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
      </div>

      {/* Edit Modal */}
      {isModalOpen && selectedTask && (
        <TaskEditModal
          task={selectedTask}
          onClose={handleCloseModal}
          onSave={handleSaveTask}
          title="Onboarding Görevi Düzenle"
        />
      )}
    </>
  );
}
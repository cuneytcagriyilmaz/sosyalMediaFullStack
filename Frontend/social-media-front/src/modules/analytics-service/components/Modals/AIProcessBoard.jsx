// src/modules/analytics-service/components/CustomerAnalytics/AIProcessBoard.jsx

import { useState } from 'react';
import { getStatusLabel, getStatusColor, calculateProgress } from '../../data/mockHelpers';
import TaskEditModal from './TaskEditModal';
import AddAITaskModal from '../Modals/AddAITaskModal'; // ✅ YENİ

export default function AIProcessBoard({ tasks, onUpdateTask, onAddTask, customerId }) { // ✅ onAddTask prop
  const [selectedTask, setSelectedTask] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false); // ✅ YENİ

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

  // ✅ YENİ: Task ekleme
  const handleAddTask = async (taskData) => {
    const result = await onAddTask(taskData);
    if (result.success) {
      setIsAddModalOpen(false);
    }
    return result;
  };

  return (
    <>
      <div className="bg-white rounded-xl shadow-lg overflow-hidden">
        {/* Header */}
        <div className="bg-gradient-to-r from-purple-600 to-indigo-600 p-6">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-xl font-bold text-white flex items-center gap-2">
                🤖 AI İçerik Süreci
              </h3>
              <p className="text-purple-100 text-sm mt-1">
                İçerik üretim aşamalarını takip edin
              </p>
            </div>
            {/* ✅ YENİ: Ekle Butonu */}
            <button
              onClick={() => setIsAddModalOpen(true)}
              className="px-4 py-2 bg-white/20 hover:bg-white/30 text-white rounded-lg transition-all duration-200 flex items-center gap-2 font-medium"
            >
              <span className="text-xl">+</span>
              Görev Ekle
            </button>
          </div>
        </div>

        {/* Tasks List */}
        <div className="p-6 space-y-3">
          {!tasks || tasks.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              <span className="text-6xl">📋</span>
              <p className="mt-4 font-medium text-gray-700">Henüz görev eklenmemiş</p>
              <p className="text-sm text-gray-500 mt-2">Yukarıdaki "Görev Ekle" butonunu kullanarak yeni görev oluşturun</p>
            </div>
          ) : (
            tasks.map((task) => {
              const progress = task.progressTotal > 0 
                ? calculateProgress(task.progressCurrent, task.progressTotal)
                : 0;

              return (
                <div
                  key={task.id}
                  className="p-4 border-2 border-gray-200 rounded-lg hover:border-indigo-300 hover:shadow-md transition-all duration-200 cursor-pointer"
                  onClick={() => handleEditTask(task)}
                >
                  <div className="flex items-start justify-between mb-3">
                    <div className="flex-1">
                      <h4 className="font-semibold text-gray-800 flex items-center gap-2">
                        {task.status === 'COMPLETED' && '✅'}
                        {task.status === 'IN_PROGRESS' && '🔄'}
                        {task.status === 'PENDING' && '⏳'}
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

                  {/* Progress Bar */}
                  {task.progressTotal > 0 && (
                    <div className="mb-2">
                      <div className="flex justify-between text-xs text-gray-600 mb-1">
                        <span>İlerleme</span>
                        <span className="font-semibold">{task.progressCurrent}/{task.progressTotal} ({progress}%)</span>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-2">
                        <div
                          className={`h-2 rounded-full transition-all duration-300 ${
                            progress === 100 ? 'bg-green-500' : 
                            progress >= 50 ? 'bg-blue-500' : 'bg-yellow-500'
                          }`}
                          style={{ width: `${progress}%` }}
                        ></div>
                      </div>
                    </div>
                  )}

                  {/* Dates */}
                  <div className="flex items-center gap-4 text-xs text-gray-500 mt-2">
                    {task.startedAt && (
                      <span>🚀 {new Date(task.startedAt).toLocaleDateString('tr-TR')}</span>
                    )}
                    {task.completedAt && (
                      <span>✅ {new Date(task.completedAt).toLocaleDateString('tr-TR')}</span>
                    )}
                  </div>
                </div>
              );
            })
          )}
        </div>
      </div>

      {/* Edit Modal */}
      {isModalOpen && selectedTask && (
        <TaskEditModal
          task={selectedTask}
          onClose={handleCloseModal}
          onSave={handleSaveTask}
          title="AI İçerik Görevi Düzenle"
        />
      )}

      {/* ✅ Add Modal */}
      {isAddModalOpen && (
        <AddAITaskModal
          customerId={customerId}
          onClose={() => setIsAddModalOpen(false)}
          onSave={handleAddTask}
        />
      )}
    </>
  );
}
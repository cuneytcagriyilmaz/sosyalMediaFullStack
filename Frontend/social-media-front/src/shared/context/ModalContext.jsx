// src/shared/contexts/ModalContext.jsx

import { createContext, useContext, useState, useCallback } from 'react';
import { ConfirmationModal } from '../Modal';
 
const ModalContext = createContext(null);

export const ModalProvider = ({ children }) => {
  const [modalState, setModalState] = useState({
    isOpen: false,
    title: '',
    message: '',
    confirmText: 'Evet',
    cancelText: 'İptal',
    type: 'danger',
    onConfirm: null,
    loading: false
  });

  const confirm = useCallback((options) => {
    return new Promise((resolve) => {
      setModalState({
        isOpen: true,
        title: options.title || 'Emin misiniz?',
        message: options.message || 'Bu işlemi gerçekleştirmek istediğinize emin misiniz?',
        confirmText: options.confirmText || 'Evet',
        cancelText: options.cancelText || 'İptal',
        type: options.type || 'danger',
        loading: false,
        onConfirm: async () => {
          setModalState(prev => ({ ...prev, loading: true }));
          
          if (options.onConfirm) {
            await options.onConfirm();
          }
          
          setModalState(prev => ({ ...prev, isOpen: false, loading: false }));
          resolve(true);
        }
      });
    });
  }, []);

  const closeModal = useCallback(() => {
    setModalState(prev => ({ ...prev, isOpen: false, loading: false }));
  }, []);

  return (
    <ModalContext.Provider value={{ confirm }}>
      {children}
      <ConfirmationModal
        isOpen={modalState.isOpen}
        onClose={closeModal}
        onConfirm={modalState.onConfirm}
        title={modalState.title}
        message={modalState.message}
        confirmText={modalState.confirmText}
        cancelText={modalState.cancelText}
        type={modalState.type}
        loading={modalState.loading}
      />
    </ModalContext.Provider>
  );
};

export const useModal = () => {
  const context = useContext(ModalContext);
  if (!context) {
    throw new Error('useModal must be used within ModalProvider');
  }
  return context;
};
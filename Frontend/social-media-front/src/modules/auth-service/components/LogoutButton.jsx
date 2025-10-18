import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../../../shared/context/ToastContext';
import { useModal } from '../../../shared/context/ModalContext';
 

export default function LogoutButton() {
  const navigate = useNavigate();
  const { logout } = useAuth();
  const { toast } = useToast();
  const { confirm } = useModal();

  const handleLogout = async () => {
    await confirm({
      title: 'Çıkış Yap',
      message: 'Çıkış yapmak istediğinize emin misiniz?',
      confirmText: 'Evet, Çıkış Yap',
      cancelText: 'İptal',
      type: 'warning',
      onConfirm: async () => {
        logout();
        toast.success('Başarıyla çıkış yapıldı!');
        setTimeout(() => {
          navigate('/login');
        }, 500);
      }
    });
  };

  return (
    <button
      onClick={handleLogout}
      className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg text-sm font-medium transition"
    >
      Çıkış Yap
    </button>
  );
}
// modules/customer-service/components/CustomerDelete/components/Notification.jsx
import { CheckCircle2, XCircle, AlertTriangle } from 'lucide-react';

export default function Notification({ notification }) {
  if (!notification) return null;

  return (
    <div className={`fixed top-4 right-4 z-50 px-6 py-4 rounded-lg shadow-lg flex items-center gap-3 animate-slide-in ${
      notification.type === 'success' ? 'bg-green-500' :
      notification.type === 'error' ? 'bg-red-500' :
      'bg-yellow-500'
    } text-white`}>
      {notification.type === 'success' ? <CheckCircle2 size={20} /> :
       notification.type === 'error' ? <XCircle size={20} /> :
       <AlertTriangle size={20} />}
      <span className="font-medium">{notification.message}</span>
    </div>
  );
}
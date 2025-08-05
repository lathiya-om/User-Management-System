import React from 'react';
import { CheckCircle, AlertCircle, X, Info } from 'lucide-react';

const Notification = ({ message, type, onClose }) => {
  const getNotificationStyles = () => {
    switch (type) {
      case 'success':
        return {
          bg: 'bg-green-50 border-green-200',
          text: 'text-green-800',
          icon: <CheckCircle size={20} className="text-green-600" />
        };
      case 'error':
        return {
          bg: 'bg-red-50 border-red-200',
          text: 'text-red-800',
          icon: <AlertCircle size={20} className="text-red-600" />
        };
      default:
        return {
          bg: 'bg-blue-50 border-blue-200',
          text: 'text-blue-800',
          icon: <Info size={20} className="text-blue-600" />
        };
    }
  };

  const styles = getNotificationStyles();

  return (
    <div className={`fixed top-4 right-4 z-50 animate-fade-in-down`}>
      <div className={`${styles.bg} border rounded-2xl shadow-xl p-4 max-w-sm backdrop-blur-sm`}>
        <div className="flex items-start space-x-3">
          {styles.icon}
          <div className="flex-1">
            <p className={`text-sm font-medium ${styles.text}`}>{message}</p>
          </div>
          <button 
            onClick={onClose} 
            className="text-gray-400 hover:text-gray-600 transition-colors p-1 rounded-full hover:bg-white/50"
          >
            <X size={16} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default Notification;

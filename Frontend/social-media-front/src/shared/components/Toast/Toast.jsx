// src/shared/components/Toast/Toast.jsx

import { useEffect, useState } from 'react';

const TOAST_ICONS = {
  success: '✅',
  error: '❌',
  warning: '⚠️',
  info: 'ℹ️'
};

const TOAST_STYLES = {
  success: 'bg-green-500 border-green-600',
  error: 'bg-red-500 border-red-600',
  warning: 'bg-orange-500 border-orange-600',
  info: 'bg-blue-500 border-blue-600'
};

export default function Toast({ id, message, type, onRemove }) {
  const [isExiting, setIsExiting] = useState(false);

  const handleRemove = () => {
    setIsExiting(true);
    setTimeout(() => {
      onRemove(id);
    }, 300); // Animation duration
  };

  return (
    <div
      className={`
        ${TOAST_STYLES[type]}
        text-white px-5 py-4 rounded-lg shadow-2xl border-l-4
        flex items-center gap-3 min-w-[300px] max-w-md
        transform transition-all duration-300 ease-out
        ${isExiting 
          ? 'translate-x-[400px] opacity-0' 
          : 'translate-x-0 opacity-100'
        }
        animate-slide-in-right
      `}
    >
      {/* Icon */}
      <span className="text-2xl flex-shrink-0">
        {TOAST_ICONS[type]}
      </span>

      {/* Message */}
      <p className="flex-1 font-medium text-sm leading-snug">
        {message}
      </p>

      {/* Close Button */}
      <button
        onClick={handleRemove}
        className="flex-shrink-0 hover:bg-white/20 rounded p-1 transition"
        aria-label="Kapat"
      >
        <svg 
          className="w-4 h-4" 
          fill="none" 
          stroke="currentColor" 
          viewBox="0 0 24 24"
        >
          <path 
            strokeLinecap="round" 
            strokeLinejoin="round" 
            strokeWidth={2} 
            d="M6 18L18 6M6 6l12 12" 
          />
        </svg>
      </button>
    </div>
  );
}
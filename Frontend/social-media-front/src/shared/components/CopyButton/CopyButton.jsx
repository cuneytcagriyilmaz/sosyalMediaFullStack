// src/shared/components/CopyButton/CopyButton.jsx

import { useState } from 'react';
import { useToast } from '../../context/ToastContext';

export default function CopyButton({ 
  text, 
  label = 'Kopyala',
  size = 'md', // 'sm' | 'md' | 'lg'
  variant = 'primary', // 'primary' | 'ghost' | 'outline'
  showText = true
}) {
  const [copied, setCopied] = useState(false);
  const { toast } = useToast();

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(text);
      setCopied(true);
      toast.success('Panoya kopyalandı!');
      
      // 2 saniye sonra eski haline dön
      setTimeout(() => {
        setCopied(false);
      }, 2000);
    } catch (error) {
      console.error('Kopyalama hatası:', error);
      toast.error('Kopyalama başarısız!');
    }
  };

  // Size variants
  const sizeClasses = {
    sm: 'px-2 py-1 text-xs',
    md: 'px-3 py-1.5 text-sm',
    lg: 'px-4 py-2 text-base'
  };

  // Variant styles
  const variantClasses = {
    primary: 'bg-indigo-600 text-white hover:bg-indigo-700',
    ghost: 'bg-transparent text-gray-600 hover:bg-gray-100 hover:text-indigo-600',
    outline: 'bg-white text-indigo-600 border border-indigo-300 hover:bg-indigo-50 hover:border-indigo-500'
  };

  const iconSize = {
    sm: 'w-3 h-3',
    md: 'w-4 h-4',
    lg: 'w-5 h-5'
  };

  return (
    <button
      onClick={handleCopy}
      disabled={copied}
      className={`
        ${sizeClasses[size]}
        ${variantClasses[variant]}
        rounded-lg font-medium
        transition-all duration-200
        flex items-center gap-2
        disabled:opacity-50 disabled:cursor-not-allowed
        ${copied ? 'scale-95' : 'hover:scale-105'}
      `}
      title={copied ? 'Kopyalandı!' : 'Kopyala'}
    >
      {/* Icon */}
      {copied ? (
        <svg 
          className={iconSize[size]} 
          fill="none" 
          stroke="currentColor" 
          viewBox="0 0 24 24"
        >
          <path 
            strokeLinecap="round" 
            strokeLinejoin="round" 
            strokeWidth={2} 
            d="M5 13l4 4L19 7" 
          />
        </svg>
      ) : (
        <svg 
          className={iconSize[size]} 
          fill="none" 
          stroke="currentColor" 
          viewBox="0 0 24 24"
        >
          <path 
            strokeLinecap="round" 
            strokeLinejoin="round" 
            strokeWidth={2} 
            d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" 
          />
        </svg>
      )}
      
      {/* Text */}
      {showText && (
        <span>
          {copied ? 'Kopyalandı!' : label}
        </span>
      )}
    </button>
  );
}
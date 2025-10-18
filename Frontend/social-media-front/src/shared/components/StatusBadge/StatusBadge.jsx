// src/shared/components/StatusBadge/StatusBadge.jsx

const STATUS_CONFIG = {
  ACTIVE: {
    bg: 'bg-green-100',
    text: 'text-green-800',
    border: 'border-green-300',
    icon: '✓',
    label: 'Aktif'
  },
  PASSIVE: {
    bg: 'bg-yellow-100',
    text: 'text-yellow-800',
    border: 'border-yellow-300',
    icon: '⏸',
    label: 'Pasif'
  },
  CANCELLED: {
    bg: 'bg-red-100',
    text: 'text-red-800',
    border: 'border-red-300',
    icon: '✕',
    label: 'İptal'
  },
  PENDING: {
    bg: 'bg-blue-100',
    text: 'text-blue-800',
    border: 'border-blue-300',
    icon: '⏳',
    label: 'Bekliyor'
  }
};

export default function StatusBadge({ 
  status, 
  showIcon = true,
  size = 'md' // 'sm' | 'md' | 'lg'
}) {
  const config = STATUS_CONFIG[status];

  if (!config) {
    console.warn(`StatusBadge: Unknown status "${status}"`);
    return (
      <span className="px-2 py-1 bg-gray-100 text-gray-600 rounded-full text-xs font-medium">
        {status}
      </span>
    );
  }

  // Size variants
  const sizeClasses = {
    sm: 'px-2 py-0.5 text-xs',
    md: 'px-3 py-1 text-sm',
    lg: 'px-4 py-1.5 text-base'
  };

  return (
    <span
      className={`
        inline-flex items-center gap-1.5
        ${config.bg} ${config.text} 
        ${sizeClasses[size]}
        rounded-full font-semibold
        border ${config.border}
        transition-all duration-200
        hover:scale-105
      `}
    >
      {showIcon && <span className="text-sm">{config.icon}</span>}
      <span>{config.label}</span>
    </span>
  );
}
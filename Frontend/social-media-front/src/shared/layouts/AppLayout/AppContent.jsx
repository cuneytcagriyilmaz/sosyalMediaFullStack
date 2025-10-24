// src/shared/layouts/AppLayout/AppContent.jsx

import { customerRoutes } from "../../../routes/customerRoutes";
import { analyticsRoutes } from "../../../routes/analyticsRoutes";

// Placeholder Component
const PagePlaceholder = ({ title, icon }) => (
  <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
    <h2 className="text-lg font-semibold text-indigo-700 mb-2">
      {icon} {title}
    </h2>
    <div className="h-64 flex items-center justify-center text-gray-800">
      Bu bileşen yakında eklenecek...
    </div>
  </section>
);

// Diğer Route'lar
const otherRoutes = {
  takvim: {
    component: () => <PagePlaceholder title="Takvim" icon="📅" />
  },
  post: {
    component: () => <PagePlaceholder title="Post Yönetimi" icon="📝" />
  },
  ayarlar: {
    component: () => <PagePlaceholder title="Ayarlar" icon="⚙️" />
  }
};

// ✅ DÜZELTME: customerRoutes en başta olmalı
const ROUTES = {
  ...customerRoutes,    // ✅ Customer routes önce (musteriListesi vs.)
  ...analyticsRoutes,   // ✅ Analytics routes sonra (dashboard, customerNotes vs.)
  ...otherRoutes        // ✅ Diğer routes en sonda
};

export default function AppContent({ activeMenu, onNavigate }) {
  const route = ROUTES[activeMenu];

  // Debug için (geliştirme aşamasında)
  console.log('Active Menu:', activeMenu);
  console.log('Route Found:', !!route);

  if (!route) {
    return (
      <main className="flex-1 p-6 bg-gray-50">
        <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
          <div className="h-64 flex items-center justify-center text-gray-500">
            <div className="text-center">
              <span className="text-6xl">🤷</span>
              <p className="mt-4 text-lg">Sayfa bulunamadı</p>
              <p className="text-sm text-gray-400 mt-2">Menu: {activeMenu}</p>
              <button
                onClick={() => onNavigate('dashboard')}
                className="mt-4 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
              >
                Dashboard'a Dön
              </button>
            </div>
          </div>
        </section>
      </main>
    );
  }

  const Component = route.component;

  return (
    <main className="flex-1 p-3 sm:p-4 md:p-6 bg-gray-50 overflow-y-auto">
      {route.wrapper ? (
        <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
          {route.wrapper.title && (
            <h2 className="text-lg font-semibold text-indigo-700 mb-4">
              {route.wrapper.title}
            </h2>
          )}
          <Component onNavigate={onNavigate} />
        </section>
      ) : (
        <Component onNavigate={onNavigate} />
      )}
    </main>
  );
}
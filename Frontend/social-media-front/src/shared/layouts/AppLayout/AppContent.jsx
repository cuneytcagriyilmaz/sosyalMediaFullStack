// src/shared/layouts/AppLayout/AppContent.jsx

import { customerRoutes } from "../../../routes/CustomerRoutes";

 
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

// Tüm Route'ları birleştir
const ROUTES = {
  ...customerRoutes,
  ...otherRoutes
};

export default function AppContent({ activeMenu, onNavigate }) {
  const route = ROUTES[activeMenu];

  if (!route) {
    return (
      <main className="flex-1 p-6 bg-gray-50">
        <section className="bg-white shadow-lg rounded-xl overflow-hidden p-4">
          <div className="h-64 flex items-center justify-center text-gray-500">
            <div className="text-center">
              <span className="text-6xl">🤷</span>
              <p className="mt-4 text-lg">Sayfa bulunamadı</p>
              <p className="text-sm text-gray-400 mt-2">Menu: {activeMenu}</p>
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
        <Component />
      )}
    </main>
  );
}
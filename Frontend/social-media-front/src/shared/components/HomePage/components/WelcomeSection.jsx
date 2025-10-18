// shared/components/HomePage/components/WelcomeSection.jsx

export default function WelcomeSection() {
  const currentHour = new Date().getHours();
  const greeting = currentHour < 12 ? 'Günaydın' : currentHour < 18 ? 'İyi günler' : 'İyi akşamlar';
  const userName = 'Çağrı'; // TODO: Auth'dan gelecek

  return (
    <div className="bg-gradient-to-r from-indigo-600 to-purple-600 rounded-xl shadow-lg p-6 text-white">
      <h1 className="text-3xl font-bold mb-2">
        {greeting}, {userName}! 👋
      </h1>
      <p className="text-indigo-100">
        {new Date().toLocaleDateString('tr-TR', { 
          weekday: 'long', 
          year: 'numeric', 
          month: 'long', 
          day: 'numeric' 
        })}
      </p>
    </div>
  );
}
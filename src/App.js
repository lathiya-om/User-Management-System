import React, { useState, useEffect, useContext } from 'react';
import { AuthProvider, AuthContext } from './contexts/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import ProfilePage from './pages/ProfilePage';
import EditProfilePage from './pages/EditProfilePage';
import LoadingSpinner from './components/common/LoadingSpinner';

function App() {
  const [path, setPath] = useState(window.location.pathname);
  const { authToken, loadingAuth } = useContext(AuthContext);

  // Simple client-side routing
  const navigate = (newPath) => {
    console.log('Navigating to:', newPath);
    window.history.pushState({}, '', newPath);
    setPath(newPath);
  };

  useEffect(() => {
    const handlePopState = () => setPath(window.location.pathname);
    window.addEventListener('popstate', handlePopState);
    return () => window.removeEventListener('popstate', handlePopState);
  }, []);

  console.log('Current path:', path);
  console.log('Auth token:', authToken);
  console.log('Loading auth:', loadingAuth);

  let ComponentToRender;

  if (loadingAuth) {
    ComponentToRender = <LoadingSpinner />;
  } else if (authToken) {
    // User is authenticated
    switch (path) {
      case '/dashboard':
        ComponentToRender = <DashboardPage navigate={navigate} />;
        break;
      case '/profile':
        console.log('Rendering ProfilePage');
        ComponentToRender = <ProfilePage navigate={navigate} />;
        break;
      case '/profile/edit':
        console.log('Rendering EditProfilePage');
        ComponentToRender = <EditProfilePage navigate={navigate} />;
        break;
      case '/login':
      case '/register':
        ComponentToRender = <DashboardPage navigate={navigate} />;
        navigate('/dashboard');
        break;
      default:
        ComponentToRender = <DashboardPage navigate={navigate} />;
    }
  } else {
    // User is not authenticated
    switch (path) {
      case '/register':
        ComponentToRender = <RegisterPage navigate={navigate} />;
        break;
      case '/login':
      default:
        ComponentToRender = <LoginPage navigate={navigate} />;
        break;
    }
  }

  return (
    <div className="min-h-screen font-sans antialiased">
      <div className="relative z-10">
        {ComponentToRender}
      </div>
    </div>
  );
}

export default App;

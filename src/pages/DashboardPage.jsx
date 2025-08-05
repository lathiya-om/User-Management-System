import React, { useContext } from 'react';
import { LogOut, User, Shield } from 'lucide-react';
import { AuthContext } from '../contexts/AuthContext';

const DashboardPage = ({ navigate }) => {
  const { user, logout } = useContext(AuthContext);

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="bg-white rounded-lg p-8 text-center shadow-lg">
          <p className="text-xl text-gray-700">Please log in to view this page.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center space-x-4">
              <div className="w-10 h-10 bg-gradient-to-r from-blue-600 to-purple-600 rounded-xl flex items-center justify-center">
                <Shield className="text-white" size={24} />
              </div>
              <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
            </div>
            
            <div className="flex items-center space-x-4">
              <div className="flex items-center space-x-3">
                <div className="w-8 h-8 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full flex items-center justify-center">
                  <User className="text-white" size={16} />
                </div>
                <span className="text-sm font-medium text-gray-700">{user.username}</span>
              </div>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Welcome Section */}
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-2">
            Welcome back, {user.username || 'User'}! 👋
          </h2>
          <p className="text-gray-600">Manage your account and profile settings.</p>
        </div>

        {/* Main Content */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Account Management */}
          <div className="bg-white rounded-lg p-6 shadow-sm">
            <h3 className="text-xl font-semibold text-gray-900 mb-4">Account Management</h3>
            <div className="space-y-3">
              <button 
                onClick={() => navigate('/profile')}
                className="w-full text-left p-3 rounded-lg hover:bg-gray-50 transition-colors flex items-center"
              >
                <User size={20} className="mr-3 text-gray-600" />
                <span className="text-gray-700">Profile Settings</span>
              </button>
              <button 
                onClick={handleLogout}
                className="w-full text-left p-3 rounded-lg hover:bg-red-50 transition-colors flex items-center text-red-600"
              >
                <LogOut size={20} className="mr-3" />
                <span>Sign Out</span>
              </button>
            </div>
          </div>

          {/* Quick Info */}
          <div className="bg-white rounded-lg p-6 shadow-sm">
            <h3 className="text-xl font-semibold text-gray-900 mb-4">Account Information</h3>
            <div className="space-y-3">
              <div className="flex justify-between">
                <span className="text-gray-600">Username:</span>
                <span className="font-medium">{user.username}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Status:</span>
                <span className="text-green-600 font-medium">Active</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Role:</span>
                <span className="font-medium">{user.roles?.join(', ') || 'User'}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;

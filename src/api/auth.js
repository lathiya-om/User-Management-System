// Base URL for your backend. IMPORTANT: Replace with your actual backend URL.
const API_BASE_URL = 'http://localhost:8080/api/auth';
const USER_API_BASE_URL = 'http://localhost:8080/api/users';

const api = {
  async post(path, data, token = null) {
    const headers = { 
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    
    const url = `${API_BASE_URL}${path}`;
    
    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(data),
        mode: 'cors',
      });
      
      return response;
    } catch (error) {
      throw new Error(`Network error: ${error.message}`);
    }
  },

  // Generic request function for user profile operations
  async makeRequest(url, method, data = null, token = null) {
    const headers = { 
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    
    try {
      const requestOptions = {
        method: method,
        headers: headers,
        mode: 'cors',
      };
      
      if (data && method !== 'GET') {
        requestOptions.body = JSON.stringify(data);
      }
      
      const response = await fetch(url, requestOptions);
      return response;
    } catch (error) {
      throw new Error(`Network error: ${error.message}`);
    }
  },

  async loginUser(username, password) {
    const response = await api.post('/login', { username, password });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Login failed');
    }
    const data = await response.json();
    return data.data;
  },

  async registerUser(username, email, password) {
    const response = await api.post('/register', { username, email, password });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Registration failed');
    }
    const data = await response.json();
    return data;
  },

  async refreshToken(token) {
    const response = await fetch(`${API_BASE_URL}/refresh?refreshToken=${token}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
    });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Token refresh failed');
    }
    const data = await response.json();
    return data.data;
  },

  async logoutUser(token) {
    const response = await fetch(`${API_BASE_URL}/logout?refreshToken=${token}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
    });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Logout failed');
    }
    const data = await response.json();
    return data;
  },

  // User Profile Management Functions
  async getProfile(token) {
    const response = await api.makeRequest(`${USER_API_BASE_URL}/me`, 'GET', null, token);
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to fetch profile');
    }
    const data = await response.json();
    return data.data;
  },

  async updateProfile(updateData, token) {
    const response = await api.makeRequest(`${USER_API_BASE_URL}/me`, 'PUT', updateData, token);
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to update profile');
    }
    const data = await response.json();
    return data;
  },

  async deleteAccount(token) {
    const response = await api.makeRequest(`${USER_API_BASE_URL}/me`, 'DELETE', null, token);
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to delete account');
    }
    const data = await response.json();
    return data;
  },

  async disableAccount(token) {
    const response = await api.makeRequest(`${USER_API_BASE_URL}/me/disable`, 'PUT', {}, token);
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to disable account');
    }
    const data = await response.json();
    return data;
  },

  async enableAccount(token) {
    const response = await api.makeRequest(`${USER_API_BASE_URL}/me/enable`, 'PUT', {}, token);
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to enable account');
    }
    const data = await response.json();
    return data;
  },
};

export default api;

import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('pms_user');
    return savedUser ? JSON.parse(savedUser) : null;
  });
  const [token, setToken] = useState(() => localStorage.getItem('pms_token'));
  const [loading, setLoading] = useState(false);

  const login = async (email, password) => {
    setLoading(true);
    try {
      const response = await api.post('/auth/login', { email, password });
      // Backend returns a flat AuthResponse: { token, id, fullName, email, role }
      const { token: jwtToken, id, fullName, role } = response.data;
      const userData = { id, fullName, email, role };

      localStorage.setItem('pms_token', jwtToken);
      localStorage.setItem('pms_user', JSON.stringify(userData));

      setToken(jwtToken);
      setUser(userData);
      return role;
    } catch (error) {
      throw error.response?.data?.message || 'Login failed';
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('pms_token');
    localStorage.removeItem('pms_user');
    setToken(null);
    setUser(null);
    window.location.href = '/login';
  };

  const value = {
    user,
    token,
    loading,
    isAuthenticated: !!token,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
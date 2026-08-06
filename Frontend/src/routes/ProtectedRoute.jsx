import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute({ allowedRoles }) {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(user?.role)) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-100 p-4 font-sans">
        <div className="bg-white p-8 rounded-xl shadow-md text-center max-w-md w-full">
          <h2 className="text-xl font-bold text-red-600 mb-2">403 - Unauthorized Access</h2>
          <p className="text-xs text-slate-600 mb-4">
            Your role (<strong className="text-slate-800">{user?.role}</strong>) does not have permission to access this portal.
          </p>
          <button
            onClick={() => window.history.back()}
            className="px-4 py-2 bg-slate-800 text-white text-xs font-semibold rounded-lg hover:bg-slate-700"
          >
            Go Back
          </button>
        </div>
      </div>
    );
  }

  return <Outlet />;
}
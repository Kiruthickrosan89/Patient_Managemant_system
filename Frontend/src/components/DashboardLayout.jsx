import { useAuth } from '../context/AuthContext';
import { Activity, LogOut, User, Bell } from 'lucide-react';

const ROLE_COLORS = {
  ADMIN:       'bg-purple-600',
  DOCTOR:      'bg-blue-600',
  PATIENT:     'bg-teal-600',
  RECEPTIONIST:'bg-orange-500',
  PHARMACY:    'bg-green-600',
  LAB_XRAY:   'bg-rose-600',
  LAB_BLOOD:  'bg-red-600',
  LAB_SUGAR:  'bg-yellow-600',
};

const ROLE_LABELS = {
  ADMIN:       'Admin Portal',
  DOCTOR:      'Doctor Portal',
  PATIENT:     'Patient Portal',
  RECEPTIONIST:'Reception Portal',
  PHARMACY:    'Pharmacy Portal',
  LAB_XRAY:   'X-Ray Lab Portal',
  LAB_BLOOD:  'Blood Lab Portal',
  LAB_SUGAR:  'Sugar Lab Portal',
};

export default function DashboardLayout({ children, title }) {
  const { user, logout } = useAuth();
  const roleBadge = ROLE_COLORS[user?.role] ?? 'bg-slate-600';
  const roleLabel = ROLE_LABELS[user?.role] ?? user?.role;

  return (
    <div className="min-h-screen bg-slate-100 font-sans">
      {/* Top Nav */}
      <header className="bg-slate-900 border-b border-slate-800 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 py-3 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="flex items-center gap-2 text-white">
              <Activity className="w-5 h-5 text-blue-400" />
              <span className="font-bold text-sm">MediCare</span>
            </div>
            <span className="text-slate-600 text-xs">|</span>
            <span className={`text-xs font-semibold text-white px-2 py-0.5 rounded-full ${roleBadge}`}>
              {roleLabel}
            </span>
          </div>

          <div className="flex items-center gap-4">
            <button className="text-slate-400 hover:text-white transition">
              <Bell className="w-4 h-4" />
            </button>
            <div className="flex items-center gap-2 text-slate-300">
              <div className="w-7 h-7 rounded-full bg-slate-700 flex items-center justify-center">
                <User className="w-3.5 h-3.5" />
              </div>
              <span className="text-xs font-medium hidden sm:block">{user?.fullName}</span>
            </div>
            <button
              onClick={logout}
              className="flex items-center gap-1.5 text-xs text-slate-400 hover:text-red-400 transition"
            >
              <LogOut className="w-3.5 h-3.5" />
              <span className="hidden sm:block">Logout</span>
            </button>
          </div>
        </div>
      </header>

      {/* Page Title */}
      <div className="bg-white border-b border-slate-200">
        <div className="max-w-7xl mx-auto px-4 py-4">
          <h1 className="text-lg font-bold text-slate-800">{title}</h1>
        </div>
      </div>

      {/* Content */}
      <main className="max-w-7xl mx-auto px-4 py-6">
        {children}
      </main>
    </div>
  );
}

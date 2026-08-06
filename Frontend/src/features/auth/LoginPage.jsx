import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Lock, 
  Mail, 
  Activity, 
  AlertCircle, 
  User, 
  Stethoscope, 
  Scan, 
  Droplet, 
  TestTube, 
  Pill, 
  Shield, 
  ClipboardList 
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const DEPARTMENTS = [
  { id: 'PATIENT', label: 'Patient', icon: User, path: '/patient/dashboard' },
  { id: 'DOCTOR', label: 'Doctor', icon: Stethoscope, path: '/doctor/dashboard' },
  { id: 'LAB_XRAY', label: 'X-Ray Lab', icon: Scan, path: '/lab/xray/dashboard' },
  { id: 'LAB_BLOOD', label: 'Blood Lab', icon: Droplet, path: '/lab/blood/dashboard' },
  { id: 'LAB_SUGAR', label: 'Sugar Lab', icon: TestTube, path: '/lab/sugar/dashboard' },
  { id: 'PHARMACY', label: 'Pharmacy', icon: Pill, path: '/pharmacy/dashboard' },
  { id: 'RECEPTIONIST', label: 'Reception', icon: ClipboardList, path: '/reception/dashboard' },
  { id: 'ADMIN', label: 'Admin', icon: Shield, path: '/admin/dashboard' },
];

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [selectedDept, setSelectedDept] = useState(DEPARTMENTS[0]);
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setIsSubmitting(true);

    try {
      // Perform authentication request via AuthContext
      const actualRole = await login(email, password);

      // Verify that user logged into the correct portal for their role
      if (actualRole !== selectedDept.id && actualRole !== 'ADMIN') {
        setError(`Access Denied: Your account role is '${actualRole}', not '${selectedDept.label}'.`);
        setIsSubmitting(false);
        return;
      }

      // Redirect to the matching department dashboard
      const targetDept = DEPARTMENTS.find((dept) => dept.id === actualRole) || selectedDept;
      navigate(targetDept.path, { replace: true });
    } catch (err) {
      setError(typeof err === 'string' ? err : 'Invalid credentials. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const SelectedIcon = selectedDept.icon;

  return (
    <div className="min-h-screen bg-slate-950 flex items-center justify-center p-4 font-sans relative overflow-hidden">
      {/* Background Decorative Glows */}
      <div className="absolute -top-40 -left-40 w-96 h-96 bg-blue-600/20 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute -bottom-40 -right-40 w-96 h-96 bg-teal-500/10 rounded-full blur-3xl pointer-events-none" />

      <div className="bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl max-w-lg w-full p-6 sm:p-8 space-y-6 relative z-10 backdrop-blur-xl">
        {/* Header Header */}
        <div className="text-center space-y-2">
          <div className="inline-flex items-center justify-center w-12 h-12 rounded-xl bg-blue-600/10 border border-blue-500/20 text-blue-400 mb-1">
            <Activity className="w-6 h-6" />
          </div>
          <h1 className="text-2xl font-bold text-white tracking-tight">MediCare Portal</h1>
          <p className="text-xs text-slate-400">Select your portal department to access system</p>
        </div>

        {/* Department Role Selector Grid */}
        <div className="space-y-1.5">
          <label className="block text-[11px] font-semibold uppercase tracking-wider text-slate-400">
            Select Department Portal
          </label>
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-1.5 bg-slate-950/60 p-1.5 rounded-xl border border-slate-800">
            {DEPARTMENTS.map((dept) => {
              const IconComponent = dept.icon;
              const isSelected = selectedDept.id === dept.id;
              return (
                <button
                  key={dept.id}
                  type="button"
                  onClick={() => setSelectedDept(dept)}
                  className={`flex flex-col items-center justify-center p-2 rounded-lg text-[10px] font-medium transition-all ${
                    isSelected
                      ? 'bg-blue-600 text-white shadow-md shadow-blue-600/30'
                      : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/50'
                  }`}
                >
                  <IconComponent className="w-4 h-4 mb-1" />
                  <span className="truncate w-full text-center">{dept.label}</span>
                </button>
              );
            })}
          </div>
        </div>

        {/* Error Alert */}
        {error && (
          <div className="flex items-start gap-2.5 p-3.5 bg-red-500/10 border border-red-500/20 rounded-xl text-red-400 text-xs">
            <AlertCircle className="w-4 h-4 shrink-0 mt-0.5" />
            <div className="font-medium">{error}</div>
          </div>
        )}

        {/* Login Form */}
        <form onSubmit={handleLogin} className="space-y-4">
          <div className="space-y-1">
            <label className="block text-xs font-semibold text-slate-300">
              {selectedDept.label} Email
            </label>
            <div className="relative">
              <Mail className="w-4 h-4 text-slate-500 absolute left-3 top-3" />
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder={`${selectedDept.id.toLowerCase()}@hospital.com`}
                className="w-full bg-slate-950 border border-slate-800 text-slate-200 text-xs rounded-xl pl-9 pr-3 py-2.5 outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all placeholder:text-slate-600"
              />
            </div>
          </div>

          <div className="space-y-1">
            <div className="flex items-center justify-between">
              <label className="block text-xs font-semibold text-slate-300">Password</label>
              <a href="#forgot" className="text-[11px] text-blue-400 hover:underline">Forgot password?</a>
            </div>
            <div className="relative">
              <Lock className="w-4 h-4 text-slate-500 absolute left-3 top-3" />
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full bg-slate-950 border border-slate-800 text-slate-200 text-xs rounded-xl pl-9 pr-3 py-2.5 outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all placeholder:text-slate-600"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-500 hover:to-blue-600 text-white font-semibold text-xs py-3 rounded-xl transition shadow-lg shadow-blue-600/20 flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {isSubmitting ? (
              <span>Authenticating...</span>
            ) : (
              <>
                <SelectedIcon className="w-4 h-4" />
                <span>Sign In as {selectedDept.label}</span>
              </>
            )}
          </button>
        </form>

        {/* Footer info */}
        <div className="pt-2 text-center text-[11px] text-slate-500 border-t border-slate-800/60">
          Encrypted Medical Portal • HIPAA Compliant Environment
        </div>
      </div>
    </div>
  );
}
import DashboardLayout from '../../components/DashboardLayout';
import { Users, Activity, FileText, TrendingUp, UserPlus, Heart, Calendar, DollarSign } from 'lucide-react';

export default function AdminDashboard() {
  const stats = [
    { label: 'Total Patients', value: '1,247', icon: Users, color: 'text-teal-600', bg: 'bg-teal-50' },
    { label: 'Active Doctors', value: '43', icon: Activity, color: 'text-blue-600', bg: 'bg-blue-50' },
    { label: 'Appointments Today', value: '89', icon: Calendar, color: 'text-orange-600', bg: 'bg-orange-50' },
    { label: 'Revenue This Month', value: '₹4.2M', icon: DollarSign, color: 'text-green-600', bg: 'bg-green-50' },
  ];

  return (
    <DashboardLayout title="Admin Dashboard">
      <div className="space-y-6">
        {/* Stats Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {stats.map((stat, idx) => (
            <div key={idx} className="bg-white p-4 rounded-xl border border-slate-200 shadow-sm">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-xs text-slate-500 uppercase tracking-wide">{stat.label}</p>
                  <p className="text-2xl font-bold text-slate-800 mt-1">{stat.value}</p>
                </div>
                <div className={`w-12 h-12 rounded-lg ${stat.bg} flex items-center justify-center`}>
                  <stat.icon className={`w-6 h-6 ${stat.color}`} />
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Quick Actions */}
        <div className="bg-white p-6 rounded-xl border border-slate-200">
          <h2 className="text-sm font-bold text-slate-800 mb-4">Quick Actions</h2>
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
            <button className="flex flex-col items-center gap-2 p-4 rounded-lg border border-slate-200 hover:border-blue-500 hover:bg-blue-50 transition text-xs">
              <UserPlus className="w-5 h-5 text-blue-600" />
              <span className="text-slate-700 font-medium">Add Patient</span>
            </button>
            <button className="flex flex-col items-center gap-2 p-4 rounded-lg border border-slate-200 hover:border-teal-500 hover:bg-teal-50 transition text-xs">
              <Heart className="w-5 h-5 text-teal-600" />
              <span className="text-slate-700 font-medium">Add Doctor</span>
            </button>
            <button className="flex flex-col items-center gap-2 p-4 rounded-lg border border-slate-200 hover:border-orange-500 hover:bg-orange-50 transition text-xs">
              <Calendar className="w-5 h-5 text-orange-600" />
              <span className="text-slate-700 font-medium">View Schedule</span>
            </button>
            <button className="flex flex-col items-center gap-2 p-4 rounded-lg border border-slate-200 hover:border-green-500 hover:bg-green-50 transition text-xs">
              <FileText className="w-5 h-5 text-green-600" />
              <span className="text-slate-700 font-medium">Generate Report</span>
            </button>
          </div>
        </div>

        {/* System Overview */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
          <div className="bg-white p-6 rounded-xl border border-slate-200">
            <h2 className="text-sm font-bold text-slate-800 mb-3">Service Status</h2>
            <div className="space-y-2 text-xs">
              {['Auth Service', 'Patient Service', 'Doctor Service', 'Lab Service', 'Pharmacy Service'].map((service) => (
                <div key={service} className="flex items-center justify-between py-2 border-b border-slate-100">
                  <span className="text-slate-700">{service}</span>
                  <span className="inline-flex items-center gap-1 text-green-600 font-semibold">
                    <span className="w-2 h-2 rounded-full bg-green-500"></span>
                    Online
                  </span>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-white p-6 rounded-xl border border-slate-200">
            <h2 className="text-sm font-bold text-slate-800 mb-3">Recent Activity</h2>
            <div className="space-y-3 text-xs">
              <div className="flex gap-3">
                <div className="w-1.5 bg-blue-500 rounded-full"></div>
                <div>
                  <p className="text-slate-700 font-medium">New patient registered</p>
                  <p className="text-slate-400 text-[10px]">2 minutes ago</p>
                </div>
              </div>
              <div className="flex gap-3">
                <div className="w-1.5 bg-teal-500 rounded-full"></div>
                <div>
                  <p className="text-slate-700 font-medium">Lab report completed</p>
                  <p className="text-slate-400 text-[10px]">15 minutes ago</p>
                </div>
              </div>
              <div className="flex gap-3">
                <div className="w-1.5 bg-orange-500 rounded-full"></div>
                <div>
                  <p className="text-slate-700 font-medium">Appointment scheduled</p>
                  <p className="text-slate-400 text-[10px]">1 hour ago</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}

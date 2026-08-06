import DashboardLayout from '../../components/DashboardLayout';
import { UserPlus, Calendar, Users, Search, CheckCircle, Clock } from 'lucide-react';
import { useState } from 'react';

export default function ReceptionDashboard() {
  const [searchQuery, setSearchQuery] = useState('');

  const todayAppointments = [
    { id: '#A001', patient: 'Sarah Wilson', doctor: 'Dr. Priya Sharma', time: '09:00 AM', status: 'completed' },
    { id: '#A002', patient: 'Tom Hardy', doctor: 'Dr. Raj Kumar', time: '09:30 AM', status: 'waiting' },
    { id: '#A003', patient: 'Emma Stone', doctor: 'Dr. Vijay Rajan', time: '10:00 AM', status: 'waiting' },
    { id: '#A004', patient: 'Chris Brown', doctor: 'Dr. Meena Pillai', time: '11:00 AM', status: 'scheduled' },
  ];

  const statusBadge = {
    completed: 'bg-green-100 text-green-700',
    waiting:   'bg-orange-100 text-orange-700',
    scheduled: 'bg-blue-100 text-blue-700',
  };

  return (
    <DashboardLayout title="Reception Dashboard">
      <div className="space-y-6">
        {/* Stats */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div className="bg-white p-4 rounded-xl border border-slate-200 flex items-center gap-4">
            <div className="bg-orange-50 rounded-xl p-3">
              <Clock className="w-6 h-6 text-orange-600" />
            </div>
            <div>
              <p className="text-xs text-slate-500">Waiting</p>
              <p className="text-2xl font-bold text-slate-800">7</p>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200 flex items-center gap-4">
            <div className="bg-teal-50 rounded-xl p-3">
              <Users className="w-6 h-6 text-teal-600" />
            </div>
            <div>
              <p className="text-xs text-slate-500">Today's Appointments</p>
              <p className="text-2xl font-bold text-slate-800">24</p>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200 flex items-center gap-4">
            <div className="bg-blue-50 rounded-xl p-3">
              <CheckCircle className="w-6 h-6 text-blue-600" />
            </div>
            <div>
              <p className="text-xs text-slate-500">Completed</p>
              <p className="text-2xl font-bold text-slate-800">11</p>
            </div>
          </div>
        </div>

        {/* Actions */}
        <div className="flex flex-wrap gap-3">
          <button className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg text-xs font-semibold hover:bg-blue-700 transition">
            <UserPlus className="w-4 h-4" />
            Register New Patient
          </button>
          <button className="flex items-center gap-2 px-4 py-2 bg-white text-slate-700 border border-slate-200 rounded-lg text-xs font-semibold hover:bg-slate-50 transition">
            <Calendar className="w-4 h-4" />
            Book Appointment
          </button>
        </div>

        {/* Patient Search */}
        <div className="bg-white p-4 rounded-xl border border-slate-200">
          <div className="relative">
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
            <input
              type="text"
              placeholder="Search patients by name or ID..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-xs bg-slate-50 border border-slate-200 rounded-lg focus:outline-none focus:border-blue-500"
            />
          </div>
        </div>

        {/* Today's Appointments */}
        <div className="bg-white p-6 rounded-xl border border-slate-200">
          <h2 className="text-sm font-bold text-slate-800 mb-4">Today's Appointments</h2>
          <div className="overflow-x-auto">
            <table className="w-full text-xs">
              <thead>
                <tr className="border-b border-slate-200">
                  <th className="text-left py-2 text-slate-500 font-medium">Token</th>
                  <th className="text-left py-2 text-slate-500 font-medium">Patient</th>
                  <th className="text-left py-2 text-slate-500 font-medium">Doctor</th>
                  <th className="text-left py-2 text-slate-500 font-medium">Time</th>
                  <th className="text-left py-2 text-slate-500 font-medium">Status</th>
                </tr>
              </thead>
              <tbody>
                {todayAppointments.map((a) => (
                  <tr key={a.id} className="border-b border-slate-100 last:border-0">
                    <td className="py-2.5 font-mono text-slate-700">{a.id}</td>
                    <td className="py-2.5 font-medium text-slate-800">{a.patient}</td>
                    <td className="py-2.5 text-slate-600">{a.doctor}</td>
                    <td className="py-2.5 text-slate-600">{a.time}</td>
                    <td className="py-2.5">
                      <span className={`px-2 py-0.5 rounded-full font-semibold capitalize ${statusBadge[a.status]}`}>
                        {a.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}

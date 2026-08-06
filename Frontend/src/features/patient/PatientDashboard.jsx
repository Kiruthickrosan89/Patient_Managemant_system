import DashboardLayout from '../../components/DashboardLayout';
import { Calendar, FileText, FlaskConical, Pill, Heart, Clock } from 'lucide-react';

export default function PatientDashboard() {
  const upcomingAppts = [
    { doctor: 'Dr. Priya Sharma', specialty: 'Cardiology', date: 'Aug 8, 2026', time: '10:00 AM' },
    { doctor: 'Dr. Raj Kumar', specialty: 'General Practice', date: 'Aug 14, 2026', time: '11:30 AM' },
  ];

  const recentRecords = [
    { title: 'Blood Pressure Check', date: 'Jul 28, 2026', doctor: 'Dr. Priya Sharma' },
    { title: 'Chest X-Ray', date: 'Jul 20, 2026', doctor: 'Dr. Vijay Rajan' },
    { title: 'CBC Test', date: 'Jul 15, 2026', doctor: 'Dr. Raj Kumar' },
  ];

  return (
    <DashboardLayout title="My Health Dashboard">
      <div className="space-y-6">
        {/* Health Overview Cards */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
          <div className="bg-white p-4 rounded-xl border border-slate-200 text-center">
            <Heart className="w-6 h-6 text-red-500 mx-auto mb-1" />
            <p className="text-xs text-slate-500">Blood Group</p>
            <p className="text-lg font-bold text-slate-800 mt-0.5">O+</p>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200 text-center">
            <FileText className="w-6 h-6 text-blue-500 mx-auto mb-1" />
            <p className="text-xs text-slate-500">Prescriptions</p>
            <p className="text-lg font-bold text-slate-800 mt-0.5">4</p>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200 text-center">
            <FlaskConical className="w-6 h-6 text-teal-500 mx-auto mb-1" />
            <p className="text-xs text-slate-500">Lab Reports</p>
            <p className="text-lg font-bold text-slate-800 mt-0.5">6</p>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200 text-center">
            <Calendar className="w-6 h-6 text-purple-500 mx-auto mb-1" />
            <p className="text-xs text-slate-500">Upcoming Appts</p>
            <p className="text-lg font-bold text-slate-800 mt-0.5">2</p>
          </div>
        </div>

        {/* Upcoming Appointments */}
        <div className="bg-white p-6 rounded-xl border border-slate-200">
          <h2 className="text-sm font-bold text-slate-800 mb-4">Upcoming Appointments</h2>
          {upcomingAppts.map((appt, idx) => (
            <div key={idx} className="flex items-center justify-between p-3 bg-slate-50 rounded-lg border border-slate-100 mb-2">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center">
                  <Clock className="w-5 h-5 text-blue-600" />
                </div>
                <div>
                  <p className="text-sm font-semibold text-slate-800">{appt.doctor}</p>
                  <p className="text-xs text-slate-500">{appt.specialty}</p>
                </div>
              </div>
              <div className="text-right">
                <p className="text-xs font-semibold text-slate-700">{appt.date}</p>
                <p className="text-xs text-slate-400">{appt.time}</p>
              </div>
            </div>
          ))}
        </div>

        {/* Recent Medical Records */}
        <div className="bg-white p-6 rounded-xl border border-slate-200">
          <h2 className="text-sm font-bold text-slate-800 mb-4">Recent Medical Records</h2>
          <div className="space-y-2">
            {recentRecords.map((r, idx) => (
              <div key={idx} className="flex items-center justify-between py-2 border-b border-slate-100 text-xs">
                <div>
                  <p className="font-medium text-slate-700">{r.title}</p>
                  <p className="text-slate-400">{r.doctor}</p>
                </div>
                <span className="text-slate-500">{r.date}</span>
              </div>
            ))}
          </div>
        </div>

        {/* Active Medications */}
        <div className="bg-white p-6 rounded-xl border border-slate-200">
          <h2 className="text-sm font-bold text-slate-800 mb-4">Active Medications</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            {['Metformin 500mg — Twice daily', 'Amlodipine 5mg — Once daily', 'Vitamin D3 1000IU — Once daily'].map((med, idx) => (
              <div key={idx} className="flex items-center gap-2 p-3 bg-green-50 border border-green-200 rounded-lg text-xs">
                <Pill className="w-4 h-4 text-green-600 shrink-0" />
                <span className="text-slate-700">{med}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}

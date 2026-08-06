import DashboardLayout from '../../components/DashboardLayout';
import { Calendar, Clock, Users, FileText, CheckCircle, AlertCircle } from 'lucide-react';

export default function DoctorDashboard() {
  const appointments = [
    { patient: 'John Doe', time: '09:00 AM', reason: 'Follow-up', status: 'scheduled' },
    { patient: 'Jane Smith', time: '10:30 AM', reason: 'Consultation', status: 'in_progress' },
    { patient: 'Mike Johnson', time: '11:30 AM', reason: 'Check-up', status: 'scheduled' },
  ];

  return (
    <DashboardLayout title="Doctor Dashboard">
      <div className="space-y-6">
        {/* Stats */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-xs text-slate-500">Today's Appointments</p>
                <p className="text-2xl font-bold text-slate-800 mt-1">12</p>
              </div>
              <Calendar className="w-8 h-8 text-blue-600" />
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-xs text-slate-500">Pending Prescriptions</p>
                <p className="text-2xl font-bold text-slate-800 mt-1">5</p>
              </div>
              <FileText className="w-8 h-8 text-teal-600" />
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-xs text-slate-500">Total Patients</p>
                <p className="text-2xl font-bold text-slate-800 mt-1">147</p>
              </div>
              <Users className="w-8 h-8 text-purple-600" />
            </div>
          </div>
        </div>

        {/* Today's Schedule */}
        <div className="bg-white p-6 rounded-xl border border-slate-200">
          <h2 className="text-sm font-bold text-slate-800 mb-4">Today's Schedule</h2>
          <div className="space-y-3">
            {appointments.map((appt, idx) => (
              <div key={idx} className="flex items-center justify-between p-3 bg-slate-50 rounded-lg border border-slate-200">
                <div className="flex items-center gap-3">
                  <div className="flex items-center justify-center w-10 h-10 rounded-full bg-blue-100">
                    <Clock className="w-5 h-5 text-blue-600" />
                  </div>
                  <div>
                    <p className="text-sm font-semibold text-slate-800">{appt.patient}</p>
                    <p className="text-xs text-slate-500">{appt.reason} • {appt.time}</p>
                  </div>
                </div>
                {appt.status === 'in_progress' ? (
                  <span className="text-xs font-semibold text-orange-600 flex items-center gap-1">
                    <AlertCircle className="w-3 h-3" />
                    In Progress
                  </span>
                ) : (
                  <span className="text-xs font-semibold text-green-600 flex items-center gap-1">
                    <CheckCircle className="w-3 h-3" />
                    Scheduled
                  </span>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Quick Actions */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
          <button className="flex flex-col items-center gap-2 p-4 bg-white rounded-lg border border-slate-200 hover:border-blue-500 hover:bg-blue-50 transition text-xs">
            <FileText className="w-5 h-5 text-blue-600" />
            <span className="text-slate-700 font-medium">New Prescription</span>
          </button>
          <button className="flex flex-col items-center gap-2 p-4 bg-white rounded-lg border border-slate-200 hover:border-teal-500 hover:bg-teal-50 transition text-xs">
            <Users className="w-5 h-5 text-teal-600" />
            <span className="text-slate-700 font-medium">Patient List</span>
          </button>
          <button className="flex flex-col items-center gap-2 p-4 bg-white rounded-lg border border-slate-200 hover:border-purple-500 hover:bg-purple-50 transition text-xs">
            <Calendar className="w-5 h-5 text-purple-600" />
            <span className="text-slate-700 font-medium">My Schedule</span>
          </button>
          <button className="flex flex-col items-center gap-2 p-4 bg-white rounded-lg border border-slate-200 hover:border-orange-500 hover:bg-orange-50 transition text-xs">
            <FileText className="w-5 h-5 text-orange-600" />
            <span className="text-slate-700 font-medium">Medical Records</span>
          </button>
        </div>
      </div>
    </DashboardLayout>
  );
}

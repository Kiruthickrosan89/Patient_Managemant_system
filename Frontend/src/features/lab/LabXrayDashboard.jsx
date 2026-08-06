import DashboardLayout from '../../components/DashboardLayout';
import { Scan, Clock, CheckCircle, Upload, AlertCircle } from 'lucide-react';

export default function LabXrayDashboard() {
  const orders = [
    { id: 'XR-2041', patient: 'Sarah Wilson', part: 'Chest', view: 'PA', priority: 'URGENT', status: 'PENDING', doctor: 'Dr. Priya Sharma', time: '08:30 AM' },
    { id: 'XR-2042', patient: 'Tom Hardy', part: 'Spine', view: 'Lateral', priority: 'ROUTINE', status: 'IN_PROGRESS', doctor: 'Dr. Raj Kumar', time: '09:15 AM' },
    { id: 'XR-2043', patient: 'Emma Stone', part: 'Knee', view: 'AP', priority: 'ROUTINE', status: 'COMPLETED', doctor: 'Dr. Vijay Rajan', time: '10:00 AM' },
    { id: 'XR-2044', patient: 'Mike Johnson', part: 'Shoulder', view: 'AP', priority: 'STAT', status: 'PENDING', doctor: 'Dr. Meena Pillai', time: '10:45 AM' },
  ];

  const statusBadge = {
    PENDING:     { cls: 'bg-yellow-100 text-yellow-700', icon: <Clock className="w-3 h-3" /> },
    IN_PROGRESS: { cls: 'bg-blue-100 text-blue-700',   icon: <AlertCircle className="w-3 h-3" /> },
    COMPLETED:   { cls: 'bg-green-100 text-green-700', icon: <CheckCircle className="w-3 h-3" /> },
  };
  const priorityBadge = {
    ROUTINE: 'bg-slate-100 text-slate-600',
    URGENT:  'bg-orange-100 text-orange-700',
    STAT:    'bg-red-100 text-red-700',
  };

  return (
    <DashboardLayout title="X-Ray Lab Dashboard">
      <div className="space-y-6">
        {/* Stats */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-yellow-50 rounded-lg"><Clock className="w-5 h-5 text-yellow-600" /></div>
              <div>
                <p className="text-[11px] text-slate-500">Pending</p>
                <p className="text-xl font-bold text-slate-800">8</p>
              </div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-blue-50 rounded-lg"><Scan className="w-5 h-5 text-blue-600" /></div>
              <div>
                <p className="text-[11px] text-slate-500">In Progress</p>
                <p className="text-xl font-bold text-slate-800">2</p>
              </div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-green-50 rounded-lg"><CheckCircle className="w-5 h-5 text-green-600" /></div>
              <div>
                <p className="text-[11px] text-slate-500">Completed Today</p>
                <p className="text-xl font-bold text-slate-800">14</p>
              </div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-red-50 rounded-lg"><AlertCircle className="w-5 h-5 text-red-600" /></div>
              <div>
                <p className="text-[11px] text-slate-500">STAT Orders</p>
                <p className="text-xl font-bold text-red-700">1</p>
              </div>
            </div>
          </div>
        </div>

        {/* Work Queue */}
        <div className="bg-white p-6 rounded-xl border border-slate-200">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-sm font-bold text-slate-800">X-Ray Work Queue</h2>
            <button className="flex items-center gap-1.5 text-xs bg-rose-600 text-white px-3 py-1.5 rounded-lg hover:bg-rose-700">
              <Upload className="w-3.5 h-3.5" />
              Upload Results
            </button>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-xs">
              <thead>
                <tr className="border-b border-slate-200 text-slate-500 font-medium">
                  <th className="text-left pb-2">Order ID</th>
                  <th className="text-left pb-2">Patient</th>
                  <th className="text-left pb-2">Body Part</th>
                  <th className="text-left pb-2">View</th>
                  <th className="text-left pb-2">Priority</th>
                  <th className="text-left pb-2">Doctor</th>
                  <th className="text-left pb-2">Time</th>
                  <th className="text-left pb-2">Status</th>
                  <th className="text-left pb-2">Action</th>
                </tr>
              </thead>
              <tbody>
                {orders.map((o) => {
                  const badge = statusBadge[o.status];
                  return (
                    <tr key={o.id} className="border-b border-slate-100 last:border-0">
                      <td className="py-3 font-mono text-slate-700">{o.id}</td>
                      <td className="py-3 font-medium text-slate-800">{o.patient}</td>
                      <td className="py-3 text-slate-600">{o.part}</td>
                      <td className="py-3 text-slate-600">{o.view}</td>
                      <td className="py-3">
                        <span className={`px-2 py-0.5 rounded-full font-semibold ${priorityBadge[o.priority]}`}>{o.priority}</span>
                      </td>
                      <td className="py-3 text-slate-600">{o.doctor}</td>
                      <td className="py-3 text-slate-500">{o.time}</td>
                      <td className="py-3">
                        <span className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full font-semibold ${badge.cls}`}>
                          {badge.icon} {o.status.replace('_', ' ')}
                        </span>
                      </td>
                      <td className="py-3">
                        {o.status !== 'COMPLETED' && (
                          <button className="px-2.5 py-1 bg-rose-600 text-white rounded font-semibold hover:bg-rose-700">
                            {o.status === 'PENDING' ? 'Start' : 'Upload'}
                          </button>
                        )}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}

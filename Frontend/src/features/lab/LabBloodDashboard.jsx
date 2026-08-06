import DashboardLayout from '../../components/DashboardLayout';
import { Droplet, Clock, CheckCircle, Upload, AlertCircle, FlaskConical } from 'lucide-react';

export default function LabBloodDashboard() {
  const orders = [
    { id: 'BL-3041', patient: 'Sarah Wilson', test: 'CBC', priority: 'URGENT', status: 'PENDING', doctor: 'Dr. Priya Sharma', time: '08:00 AM' },
    { id: 'BL-3042', patient: 'Tom Hardy', test: 'LFT + RFT', priority: 'ROUTINE', status: 'IN_PROGRESS', doctor: 'Dr. Raj Kumar', time: '09:00 AM' },
    { id: 'BL-3043', patient: 'Emma Stone', test: 'Lipid Profile', priority: 'ROUTINE', status: 'COMPLETED', doctor: 'Dr. Vijay Rajan', time: '09:30 AM' },
    { id: 'BL-3044', patient: 'Lisa Ray', test: 'CBC + CRP', priority: 'STAT', status: 'PENDING', doctor: 'Dr. Meena Pillai', time: '10:15 AM' },
  ];

  const testCategories = [
    { name: 'Haematology', count: 8, color: 'text-red-600', bg: 'bg-red-50' },
    { name: 'Biochemistry', count: 5, color: 'text-blue-600', bg: 'bg-blue-50' },
    { name: 'Serology', count: 3, color: 'text-teal-600', bg: 'bg-teal-50' },
    { name: 'Hormone Panel', count: 2, color: 'text-purple-600', bg: 'bg-purple-50' },
  ];

  const statusBadge = {
    PENDING:     'bg-yellow-100 text-yellow-700',
    IN_PROGRESS: 'bg-blue-100 text-blue-700',
    COMPLETED:   'bg-green-100 text-green-700',
  };
  const priorityBadge = {
    ROUTINE: 'bg-slate-100 text-slate-600',
    URGENT:  'bg-orange-100 text-orange-700',
    STAT:    'bg-red-100 text-red-700',
  };

  return (
    <DashboardLayout title="Blood Lab Dashboard">
      <div className="space-y-6">
        {/* Stats Row */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-yellow-50 rounded-lg"><Clock className="w-5 h-5 text-yellow-600" /></div>
              <div><p className="text-[11px] text-slate-500">Pending</p><p className="text-xl font-bold text-slate-800">11</p></div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-blue-50 rounded-lg"><FlaskConical className="w-5 h-5 text-blue-600" /></div>
              <div><p className="text-[11px] text-slate-500">Processing</p><p className="text-xl font-bold text-slate-800">4</p></div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-green-50 rounded-lg"><CheckCircle className="w-5 h-5 text-green-600" /></div>
              <div><p className="text-[11px] text-slate-500">Completed Today</p><p className="text-xl font-bold text-slate-800">23</p></div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-red-50 rounded-lg"><AlertCircle className="w-5 h-5 text-red-600" /></div>
              <div><p className="text-[11px] text-slate-500">STAT Orders</p><p className="text-xl font-bold text-red-700">2</p></div>
            </div>
          </div>
        </div>

        {/* Test Categories */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
          {testCategories.map((cat) => (
            <div key={cat.name} className={`${cat.bg} p-3 rounded-xl border border-slate-200 text-center`}>
              <Droplet className={`w-5 h-5 ${cat.color} mx-auto mb-1`} />
              <p className="text-[11px] text-slate-500">{cat.name}</p>
              <p className={`text-lg font-bold ${cat.color}`}>{cat.count}</p>
            </div>
          ))}
        </div>

        {/* Work Queue */}
        <div className="bg-white p-6 rounded-xl border border-slate-200">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-sm font-bold text-slate-800">Blood Test Queue</h2>
            <button className="flex items-center gap-1.5 text-xs bg-red-600 text-white px-3 py-1.5 rounded-lg hover:bg-red-700">
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
                  <th className="text-left pb-2">Test</th>
                  <th className="text-left pb-2">Priority</th>
                  <th className="text-left pb-2">Doctor</th>
                  <th className="text-left pb-2">Time</th>
                  <th className="text-left pb-2">Status</th>
                  <th className="text-left pb-2">Action</th>
                </tr>
              </thead>
              <tbody>
                {orders.map((o) => (
                  <tr key={o.id} className="border-b border-slate-100 last:border-0">
                    <td className="py-3 font-mono text-slate-700">{o.id}</td>
                    <td className="py-3 font-medium text-slate-800">{o.patient}</td>
                    <td className="py-3 text-slate-700">{o.test}</td>
                    <td className="py-3"><span className={`px-2 py-0.5 rounded-full font-semibold ${priorityBadge[o.priority]}`}>{o.priority}</span></td>
                    <td className="py-3 text-slate-600">{o.doctor}</td>
                    <td className="py-3 text-slate-500">{o.time}</td>
                    <td className="py-3"><span className={`px-2 py-0.5 rounded-full font-semibold ${statusBadge[o.status]}`}>{o.status.replace('_', ' ')}</span></td>
                    <td className="py-3">
                      {o.status !== 'COMPLETED' && (
                        <button className="px-2.5 py-1 bg-red-600 text-white rounded font-semibold hover:bg-red-700">
                          {o.status === 'PENDING' ? 'Start' : 'Upload'}
                        </button>
                      )}
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

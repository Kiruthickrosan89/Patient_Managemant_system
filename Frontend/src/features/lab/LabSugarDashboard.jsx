import DashboardLayout from '../../components/DashboardLayout';
import { TestTube, Clock, CheckCircle, Upload, AlertCircle, TrendingUp } from 'lucide-react';

export default function LabSugarDashboard() {
  const orders = [
    { id: 'SG-4041', patient: 'David Lee', test: 'Fasting Blood Sugar', priority: 'ROUTINE', status: 'PENDING', doctor: 'Dr. Raj Kumar', time: '07:30 AM' },
    { id: 'SG-4042', patient: 'Priya Nair', test: 'HbA1c', priority: 'URGENT', status: 'IN_PROGRESS', doctor: 'Dr. Priya Sharma', time: '08:45 AM' },
    { id: 'SG-4043', patient: 'Ravi Menon', test: 'PP Blood Sugar', priority: 'ROUTINE', status: 'COMPLETED', doctor: 'Dr. Vijay Rajan', time: '09:15 AM' },
    { id: 'SG-4044', patient: 'Kavya Reddy', test: 'GTT', priority: 'ROUTINE', status: 'PENDING', doctor: 'Dr. Meena Pillai', time: '10:00 AM' },
  ];

  const normalRanges = [
    { test: 'Fasting Blood Sugar', normal: '70–99 mg/dL', prediabetes: '100–125 mg/dL', diabetic: '≥126 mg/dL' },
    { test: 'PP Blood Sugar', normal: '<140 mg/dL', prediabetes: '140–199 mg/dL', diabetic: '≥200 mg/dL' },
    { test: 'HbA1c', normal: '<5.7%', prediabetes: '5.7–6.4%', diabetic: '≥6.5%' },
    { test: 'Random Blood Sugar', normal: '<140 mg/dL', prediabetes: '140–200 mg/dL', diabetic: '≥200 mg/dL' },
  ];

  const statusBadge = {
    PENDING:     'bg-yellow-100 text-yellow-700',
    IN_PROGRESS: 'bg-blue-100 text-blue-700',
    COMPLETED:   'bg-green-100 text-green-700',
  };

  return (
    <DashboardLayout title="Sugar Lab Dashboard">
      <div className="space-y-6">
        {/* Stats */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-yellow-50 rounded-lg"><Clock className="w-5 h-5 text-yellow-600" /></div>
              <div><p className="text-[11px] text-slate-500">Pending</p><p className="text-xl font-bold text-slate-800">9</p></div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-yellow-100 rounded-lg"><TestTube className="w-5 h-5 text-yellow-600" /></div>
              <div><p className="text-[11px] text-slate-500">Processing</p><p className="text-xl font-bold text-slate-800">3</p></div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-green-50 rounded-lg"><CheckCircle className="w-5 h-5 text-green-600" /></div>
              <div><p className="text-[11px] text-slate-500">Done Today</p><p className="text-xl font-bold text-slate-800">18</p></div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-red-50 rounded-lg"><TrendingUp className="w-5 h-5 text-red-600" /></div>
              <div><p className="text-[11px] text-slate-500">High Results</p><p className="text-xl font-bold text-red-700">4</p></div>
            </div>
          </div>
        </div>

        {/* Work Queue */}
        <div className="bg-white p-6 rounded-xl border border-slate-200">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-sm font-bold text-slate-800">Sugar Test Queue</h2>
            <button className="flex items-center gap-1.5 text-xs bg-yellow-600 text-white px-3 py-1.5 rounded-lg hover:bg-yellow-700">
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
                  <th className="text-left pb-2">Test Type</th>
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
                    <td className="py-3 text-slate-600">{o.doctor}</td>
                    <td className="py-3 text-slate-500">{o.time}</td>
                    <td className="py-3"><span className={`px-2 py-0.5 rounded-full font-semibold ${statusBadge[o.status]}`}>{o.status.replace('_', ' ')}</span></td>
                    <td className="py-3">
                      {o.status !== 'COMPLETED' && (
                        <button className="px-2.5 py-1 bg-yellow-600 text-white rounded font-semibold hover:bg-yellow-700">
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

        {/* Normal Ranges Reference */}
        <div className="bg-white p-6 rounded-xl border border-slate-200">
          <h2 className="text-sm font-bold text-slate-800 mb-4">Blood Sugar Reference Ranges</h2>
          <div className="overflow-x-auto">
            <table className="w-full text-xs">
              <thead>
                <tr className="border-b border-slate-200 text-slate-500 font-medium">
                  <th className="text-left pb-2">Test</th>
                  <th className="text-left pb-2 text-green-700">Normal</th>
                  <th className="text-left pb-2 text-orange-700">Pre-diabetic</th>
                  <th className="text-left pb-2 text-red-700">Diabetic</th>
                </tr>
              </thead>
              <tbody>
                {normalRanges.map((r) => (
                  <tr key={r.test} className="border-b border-slate-100 last:border-0">
                    <td className="py-2.5 font-medium text-slate-700">{r.test}</td>
                    <td className="py-2.5 text-green-700">{r.normal}</td>
                    <td className="py-2.5 text-orange-700">{r.prediabetes}</td>
                    <td className="py-2.5 text-red-700">{r.diabetic}</td>
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

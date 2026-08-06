import DashboardLayout from '../../components/DashboardLayout';
import { Pill, AlertTriangle, CheckCircle, Package, Search, TrendingDown } from 'lucide-react';
import { useState } from 'react';

export default function PharmacyDashboard() {
  const [search, setSearch] = useState('');

  const pendingPrescriptions = [
    { id: 'RX-1041', patient: 'Tom Hardy', doctor: 'Dr. Raj Kumar', items: 3, time: '10 min ago' },
    { id: 'RX-1042', patient: 'Emma Stone', doctor: 'Dr. Vijay Rajan', items: 2, time: '25 min ago' },
    { id: 'RX-1043', patient: 'Chris Brown', doctor: 'Dr. Priya Sharma', items: 4, time: '1 hr ago' },
  ];

  const lowStockDrugs = [
    { name: 'Amoxicillin 250mg', qty: 8, reorderLevel: 30, category: 'Antibiotic' },
    { name: 'Insulin 100IU/mL', qty: 5, reorderLevel: 20, category: 'Antidiabetic' },
    { name: 'Atorvastatin 10mg', qty: 12, reorderLevel: 25, category: 'Cardiovascular' },
  ];

  return (
    <DashboardLayout title="Pharmacy Dashboard">
      <div className="space-y-6">
        {/* Stats */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-orange-50 rounded-lg"><Pill className="w-5 h-5 text-orange-600" /></div>
              <div>
                <p className="text-[11px] text-slate-500">Pending Rx</p>
                <p className="text-xl font-bold text-slate-800">12</p>
              </div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-green-50 rounded-lg"><CheckCircle className="w-5 h-5 text-green-600" /></div>
              <div>
                <p className="text-[11px] text-slate-500">Dispensed Today</p>
                <p className="text-xl font-bold text-slate-800">67</p>
              </div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-blue-50 rounded-lg"><Package className="w-5 h-5 text-blue-600" /></div>
              <div>
                <p className="text-[11px] text-slate-500">Drug Types</p>
                <p className="text-xl font-bold text-slate-800">248</p>
              </div>
            </div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-slate-200">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-red-50 rounded-lg"><AlertTriangle className="w-5 h-5 text-red-600" /></div>
              <div>
                <p className="text-[11px] text-slate-500">Low Stock Alerts</p>
                <p className="text-xl font-bold text-red-700">3</p>
              </div>
            </div>
          </div>
        </div>

        {/* Drug Search */}
        <div className="bg-white p-4 rounded-xl border border-slate-200">
          <div className="flex items-center gap-2">
            <div className="relative flex-1">
              <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
              <input
                type="text"
                placeholder="Search drug inventory..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="w-full pl-9 pr-4 py-2 text-xs bg-slate-50 border border-slate-200 rounded-lg focus:outline-none focus:border-blue-500"
              />
            </div>
            <button className="px-4 py-2 text-xs bg-green-600 text-white font-semibold rounded-lg hover:bg-green-700">
              Add Drug
            </button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
          {/* Pending Prescriptions */}
          <div className="bg-white p-6 rounded-xl border border-slate-200">
            <h2 className="text-sm font-bold text-slate-800 mb-4">Pending Prescriptions</h2>
            <div className="space-y-3">
              {pendingPrescriptions.map((rx) => (
                <div key={rx.id} className="flex items-center justify-between p-3 bg-orange-50 border border-orange-200 rounded-lg">
                  <div>
                    <div className="flex items-center gap-2 mb-0.5">
                      <span className="text-xs font-mono font-bold text-orange-700">{rx.id}</span>
                    </div>
                    <p className="text-xs font-medium text-slate-800">{rx.patient}</p>
                    <p className="text-[11px] text-slate-500">{rx.doctor} • {rx.items} items • {rx.time}</p>
                  </div>
                  <button className="px-3 py-1.5 bg-orange-600 text-white text-xs font-semibold rounded-lg hover:bg-orange-700">
                    Dispense
                  </button>
                </div>
              ))}
            </div>
          </div>

          {/* Low Stock Alerts */}
          <div className="bg-white p-6 rounded-xl border border-slate-200">
            <div className="flex items-center gap-2 mb-4">
              <TrendingDown className="w-4 h-4 text-red-600" />
              <h2 className="text-sm font-bold text-slate-800">Low Stock Alerts</h2>
            </div>
            <div className="space-y-3">
              {lowStockDrugs.map((drug, idx) => (
                <div key={idx} className="p-3 bg-red-50 border border-red-200 rounded-lg">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-xs font-semibold text-slate-800">{drug.name}</p>
                      <p className="text-[11px] text-slate-500">{drug.category}</p>
                    </div>
                    <div className="text-right">
                      <p className="text-xs font-bold text-red-700">{drug.qty} units</p>
                      <p className="text-[11px] text-slate-500">Min: {drug.reorderLevel}</p>
                    </div>
                  </div>
                  <div className="mt-2 bg-red-200 rounded-full h-1.5">
                    <div
                      className="bg-red-600 h-1.5 rounded-full"
                      style={{ width: `${Math.min((drug.qty / drug.reorderLevel) * 100, 100)}%` }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}

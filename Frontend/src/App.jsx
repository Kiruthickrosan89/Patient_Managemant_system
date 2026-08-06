import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './features/auth/LoginPage';
import ProtectedRoute from './routes/ProtectedRoute';

// Dashboards
import AdminDashboard from './features/admin/AdminDashboard';
import DoctorDashboard from './features/doctor/DoctorDashboard';
import PatientDashboard from './features/patient/PatientDashboard';
import ReceptionDashboard from './features/reception/ReceptionDashboard';
import PharmacyDashboard from './features/pharmacy/PharmacyDashboard';
import LabXrayDashboard from './features/lab/LabXrayDashboard';
import LabBloodDashboard from './features/lab/LabBloodDashboard';
import LabSugarDashboard from './features/lab/LabSugarDashboard';

export default function App() {
  return (
    <Routes>
      {/* Public */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/" element={<Navigate to="/login" replace />} />

      {/* Admin */}
      <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
      </Route>

      {/* Doctor */}
      <Route element={<ProtectedRoute allowedRoles={['DOCTOR', 'ADMIN']} />}>
        <Route path="/doctor/dashboard" element={<DoctorDashboard />} />
      </Route>

      {/* Patient */}
      <Route element={<ProtectedRoute allowedRoles={['PATIENT', 'ADMIN']} />}>
        <Route path="/patient/dashboard" element={<PatientDashboard />} />
      </Route>

      {/* Receptionist */}
      <Route element={<ProtectedRoute allowedRoles={['RECEPTIONIST', 'ADMIN']} />}>
        <Route path="/reception/dashboard" element={<ReceptionDashboard />} />
      </Route>

      {/* Pharmacy */}
      <Route element={<ProtectedRoute allowedRoles={['PHARMACY', 'ADMIN']} />}>
        <Route path="/pharmacy/dashboard" element={<PharmacyDashboard />} />
      </Route>

      {/* Labs */}
      <Route element={<ProtectedRoute allowedRoles={['LAB_XRAY', 'ADMIN']} />}>
        <Route path="/lab/xray/dashboard" element={<LabXrayDashboard />} />
      </Route>
      <Route element={<ProtectedRoute allowedRoles={['LAB_BLOOD', 'ADMIN']} />}>
        <Route path="/lab/blood/dashboard" element={<LabBloodDashboard />} />
      </Route>
      <Route element={<ProtectedRoute allowedRoles={['LAB_SUGAR', 'ADMIN']} />}>
        <Route path="/lab/sugar/dashboard" element={<LabSugarDashboard />} />
      </Route>

      {/* Catch-all */}
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}

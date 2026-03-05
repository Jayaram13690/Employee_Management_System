import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Layout from "./components/Layout";
import Login from "./pages/Login";
import AdminDashboard from "./pages/AdminDashboard";
import EmployeeDashboard from "./pages/EmployeeDashboard";
import Employees from "./pages/Employees";
import Departments from "./pages/Departments";
import AttendanceAdmin from "./pages/AttendanceAdmin";
import LeaveAdmin from "./pages/LeaveAdmin";
import MyAttendance from "./pages/MyAttendance";
import MyLeaves from "./pages/MyLeaves";
import Profile from "./pages/Profile";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./index.css";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <ToastContainer position="top-right" autoClose={3000} />
        <Routes>
          {/* Public */}
          <Route path="/login" element={<Login />} />

          {/* Protected Layout */}
          <Route
            element={
              <ProtectedRoute>
                <Layout />
              </ProtectedRoute>
            }
          >
            {/* Admin Pages */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute adminOnly>
                  <AdminDashboard />
                </ProtectedRoute>
              }
            />
            <Route
              path="/employees"
              element={
                <ProtectedRoute adminOnly>
                  <Employees />
                </ProtectedRoute>
              }
            />
            <Route
              path="/departments"
              element={
                <ProtectedRoute adminOnly>
                  <Departments />
                </ProtectedRoute>
              }
            />
            <Route
              path="/attendance-admin"
              element={
                <ProtectedRoute adminOnly>
                  <AttendanceAdmin />
                </ProtectedRoute>
              }
            />
            <Route
              path="/leave-admin"
              element={
                <ProtectedRoute adminOnly>
                  <LeaveAdmin />
                </ProtectedRoute>
              }
            />

            {/* Employee Pages */}
            <Route path="/my-dashboard" element={<EmployeeDashboard />} />
            <Route path="/my-attendance" element={<MyAttendance />} />
            <Route path="/my-leaves" element={<MyLeaves />} />

            {/* Shared */}
            <Route path="/profile" element={<Profile />} />
          </Route>

          {/* Default redirect */}
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;

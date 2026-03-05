import { useEffect, useState } from "react";
import { dashboardAPI } from "../services/api";
import { useAuth } from "../context/AuthContext";
import {
  IconUsers,
  IconUserCheck,
  IconCalendar,
  IconXCircle,
  IconBeach,
  IconClock,
} from "../components/Icons";

const AdminDashboard = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const res = await dashboardAPI.getAdmin();
        setData(res.data.data);
      } catch (err) {
        console.error("Failed to fetch dashboard:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchDashboard();
  }, []);

  if (loading) {
    return (
      <div className="loader">
        <div className="spinner"></div>
      </div>
    );
  }

  const stats = [
    {
      label: "Total Employees",
      value: data?.totalEmployees || 0,
      icon: <IconUsers />,
      color: "#6366f1",
    },
    {
      label: "Active Employees",
      value: data?.activeEmployees || 0,
      icon: <IconUserCheck />,
      color: "#10b981",
    },
    {
      label: "Present Today",
      value: data?.todayPresent || 0,
      icon: <IconCalendar />,
      color: "#3b82f6",
    },
    {
      label: "Absent Today",
      value: data?.todayAbsent || 0,
      icon: <IconXCircle />,
      color: "#ef4444",
    },
    {
      label: "On Leave",
      value: data?.todayOnLeave || 0,
      icon: <IconBeach />,
      color: "#f59e0b",
    },
    {
      label: "Pending Leaves",
      value: data?.pendingLeaveRequests || 0,
      icon: <IconClock />,
      color: "#8b5cf6",
    },
  ];

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Admin Dashboard</h1>
          <p>Welcome back, {user?.firstName}! Here's your overview.</p>
        </div>
      </div>

      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
          gap: "var(--space-lg)",
        }}
      >
        {stats.map((stat, i) => (
          <div key={i} className="stat-card">
            <div
              className="stat-icon"
              style={{
                background: `${stat.color}15`,
                color: stat.color,
                fontSize: "1.5rem",
              }}
            >
              {stat.icon}
            </div>
            <div className="stat-value">{stat.value}</div>
            <div className="stat-label">{stat.label}</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminDashboard;

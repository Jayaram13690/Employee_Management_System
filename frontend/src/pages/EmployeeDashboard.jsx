import { useEffect, useState } from "react";
import { dashboardAPI, attendanceAPI } from "../services/api";
import { useAuth } from "../context/AuthContext";
import {
  IconArrowIn,
  IconArrowOut,
  IconCheckCircle,
} from "../components/Icons";
import { toast } from "react-toastify";

const EmployeeDashboard = () => {
  const [data, setData] = useState(null);
  const [todayAttendance, setTodayAttendance] = useState(null);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);
  const { user } = useAuth();

  const fetchData = async () => {
    try {
      const [dashRes, attendRes] = await Promise.all([
        dashboardAPI.getEmployee(user.employeeId),
        attendanceAPI.getToday(user.employeeId),
      ]);
      setData(dashRes.data.data);
      setTodayAttendance(attendRes.data.data);
    } catch (err) {
      console.error("Failed to fetch dashboard:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [user.employeeId]);

  const handleCheckIn = async () => {
    setActionLoading(true);
    try {
      await attendanceAPI.checkIn(user.employeeId);
      fetchData();
      toast.success("Checked in successfully");
    } catch (err) {
      toast.error(err.response?.data?.message || "Check-in failed");
    } finally {
      setActionLoading(false);
    }
  };

  const handleCheckOut = async () => {
    setActionLoading(true);
    try {
      await attendanceAPI.checkOut(user.employeeId);
      fetchData();
      toast.success("Checked out successfully");
    } catch (err) {
      toast.error(err.response?.data?.message || "Check-out failed");
    } finally {
      setActionLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="loader">
        <div className="spinner"></div>
      </div>
    );
  }

  const hasCheckedIn =
    todayAttendance?.checkIn && todayAttendance?.status !== "ABSENT";
  const hasCheckedOut = todayAttendance?.checkOut;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>My Dashboard</h1>
          <p>Welcome, {user?.firstName}!</p>
        </div>
        <div style={{ display: "flex", gap: "var(--space-sm)" }}>
          <button
            className="btn btn-success btn-lg"
            onClick={handleCheckIn}
            disabled={actionLoading || hasCheckedIn}
          >
            {hasCheckedIn ? (
              <>
                <IconCheckCircle /> Checked In
              </>
            ) : (
              <>
                <IconArrowIn /> Check In
              </>
            )}
          </button>
          <button
            className="btn btn-danger btn-lg"
            onClick={handleCheckOut}
            disabled={actionLoading || !hasCheckedIn || hasCheckedOut}
          >
            {hasCheckedOut ? (
              <>
                <IconCheckCircle /> Checked Out
              </>
            ) : (
              <>
                <IconArrowOut /> Check Out
              </>
            )}
          </button>
        </div>
      </div>

      {/* Today's Status */}
      <div className="card" style={{ marginBottom: "var(--space-lg)" }}>
        <h3 style={{ marginBottom: "var(--space-md)" }}>Today's Status</h3>
        <div
          style={{ display: "flex", gap: "var(--space-xl)", flexWrap: "wrap" }}
        >
          <div>
            <span
              style={{ color: "var(--text-muted)", fontSize: "var(--font-sm)" }}
            >
              Status
            </span>
            <div style={{ fontWeight: 700, fontSize: "var(--font-lg)" }}>
              <span
                className={`badge ${todayAttendance?.status === "PRESENT" ? "badge-success" : todayAttendance?.status === "LATE" ? "badge-warning" : "badge-danger"}`}
              >
                {todayAttendance?.status || "NOT CHECKED IN"}
              </span>
            </div>
          </div>
          <div>
            <span
              style={{ color: "var(--text-muted)", fontSize: "var(--font-sm)" }}
            >
              Check In
            </span>
            <div style={{ fontWeight: 600 }}>
              {todayAttendance?.checkIn || "—"}
            </div>
          </div>
          <div>
            <span
              style={{ color: "var(--text-muted)", fontSize: "var(--font-sm)" }}
            >
              Check Out
            </span>
            <div style={{ fontWeight: 600 }}>
              {todayAttendance?.checkOut || "—"}
            </div>
          </div>
        </div>
      </div>

      {/* Monthly Stats */}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))",
          gap: "var(--space-md)",
        }}
      >
        {[
          {
            label: "Present",
            value: data?.totalPresent || 0,
            color: "#10b981",
          },
          { label: "Late", value: data?.totalLate || 0, color: "#f59e0b" },
          {
            label: "Half Day",
            value: data?.totalHalfDay || 0,
            color: "#3b82f6",
          },
          {
            label: "Leaves Applied",
            value: data?.totalLeavesApplied || 0,
            color: "#8b5cf6",
          },
          {
            label: "Approved",
            value: data?.approvedLeaves || 0,
            color: "#10b981",
          },
          {
            label: "Pending",
            value: data?.pendingLeaves || 0,
            color: "#f59e0b",
          },
        ].map((stat, i) => (
          <div key={i} className="stat-card">
            <div className="stat-value" style={{ color: stat.color }}>
              {stat.value}
            </div>
            <div className="stat-label">{stat.label}</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default EmployeeDashboard;

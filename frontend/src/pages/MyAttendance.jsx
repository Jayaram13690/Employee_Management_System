import { useState, useEffect } from "react";
import { attendanceAPI } from "../services/api";
import { useAuth } from "../context/AuthContext";

const MyAttendance = () => {
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();
  const now = new Date();
  const [month, setMonth] = useState(now.getMonth() + 1);
  const [year, setYear] = useState(now.getFullYear());

  const fetchAttendance = async () => {
    setLoading(true);
    try {
      const res = await attendanceAPI.getMonthly(user.employeeId, month, year);
      // Handle both array and paginated response structures
      const data = res.data.data;
      setRecords(Array.isArray(data) ? data : (data?.content || []));
    } catch (err) {
      console.error("Failed to fetch attendance:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAttendance();
  }, [month, year]);

  const statusBadge = (status) => {
    const map = {
      PRESENT: "badge-success",
      LATE: "badge-warning",
      HALF_DAY: "badge-info",
      ABSENT: "badge-danger",
      ON_LEAVE: "badge-primary",
    };
    return (
      <span className={`badge ${map[status] || "badge-info"}`}>{status}</span>
    );
  };

  const months = [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Oct",
    "Nov",
    "Dec",
  ];

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>My Attendance</h1>
          <p>View your monthly attendance records</p>
        </div>
        <div className="flex gap-sm items-center">
          <select
            className="form-input"
            value={month}
            onChange={(e) => setMonth(parseInt(e.target.value))}
            style={{ width: 120 }}
          >
            {months.map((m, i) => (
              <option key={i} value={i + 1}>
                {m}
              </option>
            ))}
          </select>
          <select
            className="form-input"
            value={year}
            onChange={(e) => setYear(parseInt(e.target.value))}
            style={{ width: 100 }}
          >
            {[2024, 2025, 2026, 2027].map((y) => (
              <option key={y} value={y}>
                {y}
              </option>
            ))}
          </select>
        </div>
      </div>

      {/* Summary cards */}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fit, minmax(140px, 1fr))",
          gap: "var(--space-md)",
          marginBottom: "var(--space-lg)",
        }}
      >
        {[
          {
            label: "Present",
            count: records.filter((r) => r.status === "PRESENT").length,
            color: "var(--success)",
          },
          {
            label: "Late",
            count: records.filter((r) => r.status === "LATE").length,
            color: "var(--warning)",
          },
          {
            label: "Half Day",
            count: records.filter((r) => r.status === "HALF_DAY").length,
            color: "var(--info)",
          },
          {
            label: "On Leave",
            count: records.filter((r) => r.status === "ON_LEAVE").length,
            color: "var(--primary)",
          },
        ].map((s, i) => (
          <div key={i} className="stat-card">
            <div
              className="stat-value"
              style={{ color: s.color, fontSize: "var(--font-2xl)" }}
            >
              {s.count}
            </div>
            <div className="stat-label">{s.label}</div>
          </div>
        ))}
      </div>

      <div className="card" style={{ padding: 0, overflow: "hidden" }}>
        {loading ? (
          <div className="loader">
            <div className="spinner"></div>
          </div>
        ) : (
          <div className="table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Check In</th>
                  <th>Check Out</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {records.length === 0 ? (
                  <tr>
                    <td
                      colSpan="4"
                      style={{
                        textAlign: "center",
                        padding: 40,
                        color: "var(--text-muted)",
                      }}
                    >
                      No records for this month
                    </td>
                  </tr>
                ) : (
                  records.map((r) => (
                    <tr key={r.id}>
                      <td style={{ fontWeight: 600 }}>{r.date}</td>
                      <td>{r.checkIn || "—"}</td>
                      <td>{r.checkOut || "—"}</td>
                      <td>{statusBadge(r.status)}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default MyAttendance;

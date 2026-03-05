import { useState, useEffect } from "react";
import { attendanceAPI } from "../services/api";
import CustomDatePicker from "../components/CustomDatePicker";

const AttendanceAdmin = () => {
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedDate, setSelectedDate] = useState(
    new Date().toISOString().split("T")[0],
  );

  const fetchAttendance = async () => {
    setLoading(true);
    try {
      const res = await attendanceAPI.getByDate(selectedDate);
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
  }, [selectedDate]);

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

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Attendance</h1>
          <p>View attendance records for all employees</p>
        </div>
        <div className="flex gap-md items-center">
          <div style={{ width: 200 }}>
            <CustomDatePicker
              selected={selectedDate ? new Date(selectedDate) : null}
              maxDate={new Date()}
              onChange={(date) =>
                setSelectedDate(date ? date.toISOString().split("T")[0] : "")
              }
            />
          </div>
        </div>
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
                  <th>Employee</th>
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
                      colSpan="5"
                      style={{
                        textAlign: "center",
                        padding: 40,
                        color: "var(--text-muted)",
                      }}
                    >
                      No attendance records for this date
                    </td>
                  </tr>
                ) : (
                  records.map((r) => (
                    <tr key={r.id}>
                      <td style={{ fontWeight: 600 }}>{r.employeeName}</td>
                      <td>{r.date}</td>
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

      <div
        style={{
          marginTop: "var(--space-lg)",
          display: "flex",
          gap: "var(--space-md)",
        }}
      >
        <div className="stat-card" style={{ flex: 1 }}>
          <div className="stat-value" style={{ color: "var(--success)" }}>
            {records.filter((r) => r.status === "PRESENT").length}
          </div>
          <div className="stat-label">Present</div>
        </div>
        <div className="stat-card" style={{ flex: 1 }}>
          <div className="stat-value" style={{ color: "var(--warning)" }}>
            {records.filter((r) => r.status === "LATE").length}
          </div>
          <div className="stat-label">Late</div>
        </div>
        <div className="stat-card" style={{ flex: 1 }}>
          <div className="stat-value" style={{ color: "var(--info)" }}>
            {records.filter((r) => r.status === "HALF_DAY").length}
          </div>
          <div className="stat-label">Half Day</div>
        </div>
        <div className="stat-card" style={{ flex: 1 }}>
          <div className="stat-value" style={{ color: "var(--primary)" }}>
            {records.filter((r) => r.status === "ON_LEAVE").length}
          </div>
          <div className="stat-label">On Leave</div>
        </div>
      </div>
    </div>
  );
};

export default AttendanceAdmin;

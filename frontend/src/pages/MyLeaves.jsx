import { useState, useEffect } from "react";
import { leaveAPI } from "../services/api";
import { useAuth } from "../context/AuthContext";
import { IconPlus } from "../components/Icons";
import { toast } from "react-toastify";
import CustomDatePicker from "../components/CustomDatePicker";
import CustomSelect from "../components/CustomSelect";

const MyLeaves = () => {
  const [leaves, setLeaves] = useState([]);
  const [leaveBalance, setLeaveBalance] = useState({});
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [saving, setSaving] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    leaveType: "CASUAL_LEAVE",
    startDate: "",
    endDate: "",
    reason: "",
  });

  const fetchLeaves = async () => {
    try {
      const [leavesRes, balanceRes] = await Promise.all([
        leaveAPI.getByEmployee(user.employeeId, page, 10),
        leaveAPI.getBalance(user.employeeId),
      ]);
      setLeaves(leavesRes.data.data?.content || []);
      setTotalPages(leavesRes.data.data?.totalPages || 0);
      setLeaveBalance(balanceRes.data.data || {});
    } catch (err) {
      console.error("Failed to fetch leaves:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLeaves();
  }, [page]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await leaveAPI.apply(user.employeeId, formData);
      setShowModal(false);
      setFormData({
        leaveType: "CASUAL_LEAVE",
        startDate: "",
        endDate: "",
        reason: "",
      });
      fetchLeaves();
      toast.success("Leave applied successfully");
    } catch (err) {
      toast.error(err.response?.data?.message || "Failed to apply leave");
    } finally {
      setSaving(false);
    }
  };

  const statusBadge = (status) => {
    const map = {
      PENDING: "badge-warning",
      APPROVED: "badge-success",
      REJECTED: "badge-danger",
    };
    return <span className={`badge ${map[status]}`}>{status}</span>;
  };

  const leaveTypes = [
    "CASUAL_LEAVE",
    "SICK_LEAVE",
    "EARNED_LEAVE",
    "MATERNITY_LEAVE",
    "PATERNITY_LEAVE",
    "UNPAID_LEAVE",
  ];

  if (loading)
    return (
      <div className="loader">
        <div className="spinner"></div>
      </div>
    );

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>My Leaves</h1>
          <p>Apply for leave and track your requests</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>
          <IconPlus /> Apply Leave
        </button>
      </div>

      {/* Leave Balance Section */}
      {Object.keys(leaveBalance).length > 0 && (
        <div style={{ marginBottom: "var(--space-lg)" }}>
          <h2 style={{ marginBottom: "var(--space-md)", fontSize: "var(--font-lg)" }}>
            Available Leave Balance
          </h2>
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fit, minmax(140px, 1fr))",
              gap: "var(--space-md)",
              marginBottom: "var(--space-lg)",
            }}
          >
            {Object.entries(leaveBalance).map(([leaveType, balance]) => {
              const used =
                leaves.filter((l) => l.leaveType === leaveType && l.status === "APPROVED")
                  .length || 0;
              const remaining = balance - used;
              return (
                <div
                  key={leaveType}
                  className="card"
                  style={{
                    padding: "var(--space-md)",
                    backgroundColor:
                      remaining <= 2 ? "rgba(239, 68, 68, 0.1)" : "transparent",
                  }}
                >
                  <div
                    style={{
                      fontSize: "var(--font-xs)",
                      color: "var(--text-muted)",
                      marginBottom: "var(--space-xs)",
                    }}
                  >
                    {leaveType.replace(/_/g, " ")}
                  </div>
                  <div
                    style={{
                      fontSize: "var(--font-xl)",
                      fontWeight: "bold",
                      color:
                        remaining <= 2
                          ? "var(--danger)"
                          : remaining <= 5
                            ? "var(--warning)"
                            : "var(--success)",
                    }}
                  >
                    {remaining}
                  </div>
                  <div
                    style={{
                      fontSize: "var(--font-xs)",
                      color: "var(--text-muted)",
                      marginTop: "var(--space-xs)",
                    }}
                  >
                    of {balance} days
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      )}

      {/* Summary */}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fit, minmax(160px, 1fr))",
          gap: "var(--space-md)",
          marginBottom: "var(--space-lg)",
        }}
      >
        {[
          {
            label: "Total Applied",
            count: leaves.length,
            color: "var(--primary)",
          },
          {
            label: "Approved",
            count: leaves.filter((l) => l.status === "APPROVED").length,
            color: "var(--success)",
          },
          {
            label: "Pending",
            count: leaves.filter((l) => l.status === "PENDING").length,
            color: "var(--warning)",
          },
          {
            label: "Rejected",
            count: leaves.filter((l) => l.status === "REJECTED").length,
            color: "var(--danger)",
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
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Type</th>
                <th>From</th>
                <th>To</th>
                <th>Days</th>
                <th>Reason</th>
                <th>Status</th>
                <th>Remarks</th>
              </tr>
            </thead>
            <tbody>
              {leaves.length === 0 ? (
                <tr>
                  <td
                    colSpan="7"
                    style={{
                      textAlign: "center",
                      padding: 40,
                      color: "var(--text-muted)",
                    }}
                  >
                    No leave requests yet
                  </td>
                </tr>
              ) : (
                leaves.map((leave) => (
                  <tr key={leave.id}>
                    <td>
                      <span className="badge badge-info">
                        {leave.leaveType.replace("_", " ")}
                      </span>
                    </td>
                    <td>{leave.startDate}</td>
                    <td>{leave.endDate}</td>
                    <td>{leave.leaveDays}</td>
                    <td
                      style={{
                        maxWidth: 200,
                        whiteSpace: "nowrap",
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                      }}
                    >
                      {leave.reason}
                    </td>
                    <td>{statusBadge(leave.status)}</td>
                    <td
                      style={{
                        color: "var(--text-muted)",
                        fontSize: "var(--font-xs)",
                      }}
                    >
                      {leave.adminRemarks || "—"}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {totalPages > 1 && (
        <div className="pagination">
          <button disabled={page === 0} onClick={() => setPage((p) => p - 1)}>
            ← Prev
          </button>
          <span>
            Page {page + 1} of {totalPages}
          </span>
          <button
            disabled={page >= totalPages - 1}
            onClick={() => setPage((p) => p + 1)}
          >
            Next →
          </button>
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Apply for Leave</h2>
              <button onClick={() => setShowModal(false)}>×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group">
                  <CustomSelect
                    label="Leave Type"
                    required
                    options={leaveTypes.map((t) => ({
                      id: t,
                      label: t.replace(/_/g, " "),
                    }))}
                    value={formData.leaveType}
                    onChange={(selectedType) =>
                      setFormData((p) => ({
                        ...p,
                        leaveType: selectedType,
                      }))
                    }
                    placeholder="Select leave type"
                  />
                </div>
                <div
                  style={{
                    display: "grid",
                    gridTemplateColumns: "1fr 1fr",
                    gap: "var(--space-md)",
                  }}
                >
                  <div className="form-group">
                    <label>Start Date * (Today or later)</label>
                    <CustomDatePicker
                      required
                      selected={
                        formData.startDate ? new Date(formData.startDate) : null
                      }
                      onChange={(date) =>
                        setFormData((p) => ({
                          ...p,
                          startDate: date
                            ? date.toISOString().split("T")[0]
                            : "",
                        }))
                      }
                      minDate={new Date()} // Disable past dates
                    />
                  </div>
                  <div className="form-group">
                    <label>End Date * (Today or later)</label>
                    <CustomDatePicker
                      required
                      selected={
                        formData.endDate ? new Date(formData.endDate) : null
                      }
                      onChange={(date) =>
                        setFormData((p) => ({
                          ...p,
                          endDate: date ? date.toISOString().split("T")[0] : "",
                        }))
                      }
                      minDate={new Date()} // Disable past dates
                    />
                  </div>
                </div>
                <div className="form-group">
                  <label>Reason *</label>
                  <textarea
                    className="form-input"
                    rows="3"
                    required
                    placeholder="Explain your reason for leave..."
                    value={formData.reason}
                    onChange={(e) =>
                      setFormData((p) => ({ ...p, reason: e.target.value }))
                    }
                    style={{ resize: "vertical" }}
                  />
                </div>
              </div>
              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="btn btn-primary"
                  disabled={saving}
                >
                  {saving ? "Submitting..." : "Apply Leave"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default MyLeaves;

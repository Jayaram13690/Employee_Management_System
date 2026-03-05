import { useState, useEffect } from "react";
import { leaveAPI } from "../services/api";
import { IconClock, IconList, IconCheck, IconX } from "../components/Icons";
import { toast } from "react-toastify";

const LeaveAdmin = () => {
  const [leaves, setLeaves] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState("pending");
  const [actionModal, setActionModal] = useState(null);
  const [remarks, setRemarks] = useState("");
  const [processing, setProcessing] = useState(false);

  const fetchLeaves = async () => {
    setLoading(true);
    try {
      const res =
        filter === "pending"
          ? await leaveAPI.getPending()
          : await leaveAPI.getAll();

      // Handle paginated response
      const data = res.data.data;
      const leavesList = data.content || data || [];
      setLeaves(leavesList);
    } catch (err) {
      console.error("Failed to fetch leaves:", err);
      toast.error("Failed to load leave requests");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLeaves();
  }, [filter]);

  const handleAction = async (action) => {
    setProcessing(true);
    try {
      if (action === "approve") {
        await leaveAPI.approve(actionModal.id, { adminRemarks: remarks });
      } else {
        await leaveAPI.reject(actionModal.id, { adminRemarks: remarks });
      }
      setActionModal(null);
      setRemarks("");
      fetchLeaves();
      toast.success(action === "approve" ? "Leave approved" : "Leave rejected");
    } catch (err) {
      toast.error(err.response?.data?.message || "Action failed");
    } finally {
      setProcessing(false);
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

  const leaveTypeBadge = (type) => {
    return <span className="badge badge-info">{type.replace("_", " ")}</span>;
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Leave Requests</h1>
          <p>Manage employee leave requests</p>
        </div>
        <div className="flex gap-sm">
          <button
            className={`btn ${filter === "pending" ? "btn-primary" : "btn-secondary"}`}
            onClick={() => setFilter("pending")}
          >
            <IconClock /> Pending
          </button>
          <button
            className={`btn ${filter === "all" ? "btn-primary" : "btn-secondary"}`}
            onClick={() => setFilter("all")}
          >
            <IconList /> All
          </button>
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
                  <th>Type</th>
                  <th>From</th>
                  <th>To</th>
                  <th>Days</th>
                  <th>Reason</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {leaves.length === 0 ? (
                  <tr>
                    <td
                      colSpan="8"
                      style={{
                        textAlign: "center",
                        padding: 40,
                        color: "var(--text-muted)",
                      }}
                    >
                      No leave requests found
                    </td>
                  </tr>
                ) : (
                  leaves.map((leave) => (
                    <tr key={leave.id}>
                      <td style={{ fontWeight: 600 }}>{leave.employeeName}</td>
                      <td>{leaveTypeBadge(leave.leaveType)}</td>
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
                      <td>
                        {leave.status === "PENDING" ? (
                          <div className="flex gap-sm">
                            <button
                              className="btn btn-success btn-sm"
                              title="Approve"
                              onClick={() => {
                                setActionModal(leave);
                                setRemarks("");
                              }}
                            >
                              <IconCheck />
                            </button>
                            <button
                              className="btn btn-danger btn-sm"
                              title="Reject"
                              onClick={() => {
                                setActionModal(leave);
                                setRemarks("");
                              }}
                            >
                              <IconX />
                            </button>
                          </div>
                        ) : (
                          <span
                            style={{
                              color: "var(--text-muted)",
                              fontSize: "var(--font-xs)",
                            }}
                          >
                            {leave.adminRemarks || "—"}
                          </span>
                        )}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {actionModal && (
        <div className="modal-overlay" onClick={() => setActionModal(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Review Leave Request</h2>
              <button onClick={() => setActionModal(null)}>×</button>
            </div>
            <div className="modal-body">
              <div
                style={{
                  marginBottom: "var(--space-md)",
                  padding: "var(--space-md)",
                  background: "var(--bg)",
                  borderRadius: "var(--radius-sm)",
                }}
              >
                <p>
                  <strong>{actionModal.employeeName}</strong>
                </p>
                <p
                  style={{
                    fontSize: "var(--font-sm)",
                    color: "var(--text-secondary)",
                  }}
                >
                  {actionModal.leaveType.replace("_", " ")} ·{" "}
                  {actionModal.startDate} to {actionModal.endDate} (
                  {actionModal.leaveDays} days)
                </p>
                <p
                  style={{
                    fontSize: "var(--font-sm)",
                    marginTop: "var(--space-sm)",
                  }}
                >
                  <strong>Reason:</strong> {actionModal.reason}
                </p>
              </div>
              <div className="form-group">
                <label>Admin Remarks</label>
                <textarea
                  className="form-input"
                  rows="3"
                  placeholder="Optional remarks..."
                  value={remarks}
                  onChange={(e) => setRemarks(e.target.value)}
                  style={{ resize: "vertical" }}
                />
              </div>
            </div>
            <div className="modal-footer">
              <button
                className="btn btn-secondary"
                onClick={() => setActionModal(null)}
              >
                Cancel
              </button>
              <button
                className="btn btn-danger"
                onClick={() => handleAction("reject")}
                disabled={processing}
              >
                <IconX /> Reject
              </button>
              <button
                className="btn btn-success"
                onClick={() => handleAction("approve")}
                disabled={processing}
              >
                <IconCheck /> Approve
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default LeaveAdmin;

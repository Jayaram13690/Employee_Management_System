import { useState, useEffect } from "react";
import { departmentAPI } from "../services/api";
import {
  IconBuilding,
  IconEdit,
  IconTrash,
  IconPlus,
} from "../components/Icons";
import { toast } from "react-toastify";

const Departments = () => {
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState(null);
  const [formData, setFormData] = useState({ name: "", description: "" });
  const [saving, setSaving] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState(null);

  const fetchDepartments = async () => {
    try {
      const res = await departmentAPI.getAll();
      setDepartments(res.data.data || []);
    } catch (err) {
      console.error("Failed to fetch departments:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDepartments();
  }, []);

  const openCreate = () => {
    setEditing(null);
    setFormData({ name: "", description: "" });
    setShowModal(true);
  };

  const openEdit = (dept) => {
    setEditing(dept);
    setFormData({ name: dept.name, description: dept.description || "" });
    setShowModal(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editing) {
        await departmentAPI.update(editing.id, formData);
      } else {
        await departmentAPI.create(formData);
      }
      setShowModal(false);
      fetchDepartments();
      toast.success(
        editing
          ? "Department updated successfully"
          : "Department created successfully",
      );
    } catch (err) {
      toast.error(err.response?.data?.message || "Operation failed");
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = (id) => {
    setDeleteConfirm(id);
  };

  const confirmDelete = async () => {
    if (!deleteConfirm) return;
    try {
      await departmentAPI.delete(deleteConfirm);
      setDeleteConfirm(null);
      fetchDepartments();
      toast.success("Department deleted successfully");
    } catch (err) {
      toast.error(err.response?.data?.message || "Delete failed");
      setDeleteConfirm(null);
    }
  };

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
          <h1>Departments</h1>
          <p>Manage organization departments</p>
        </div>
        <button className="btn btn-primary" onClick={openCreate}>
          <IconPlus /> Add Department
        </button>
      </div>

      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
          gap: "var(--space-lg)",
        }}
      >
        {departments.length === 0 ? (
          <div
            className="card"
            style={{ textAlign: "center", padding: "var(--space-2xl)" }}
          >
            <p style={{ color: "var(--text-muted)" }}>
              No departments yet. Create one!
            </p>
          </div>
        ) : (
          departments.map((dept) => (
            <div key={dept.id} className="card">
              <div
                className="flex justify-between items-center"
                style={{ marginBottom: "var(--space-md)" }}
              >
                <div>
                  <h3
                    style={{
                      fontSize: "var(--font-lg)",
                      fontWeight: 700,
                      display: "flex",
                      alignItems: "center",
                      gap: "8px",
                    }}
                  >
                    <IconBuilding style={{ color: "var(--primary)" }} />{" "}
                    {dept.name}
                  </h3>
                  <p
                    style={{
                      fontSize: "var(--font-sm)",
                      color: "var(--text-muted)",
                      marginTop: 4,
                    }}
                  >
                    {dept.description || "No description"}
                  </p>
                </div>
              </div>
              <div className="flex justify-between items-center">
                <span className="badge badge-primary">
                  {dept.employeeCount} employee
                  {dept.employeeCount !== 1 ? "s" : ""}
                </span>
                <div className="flex gap-sm">
                  <button
                    className="btn btn-secondary btn-sm"
                    onClick={() => openEdit(dept)}
                    title="Edit"
                  >
                    <IconEdit /> Edit
                  </button>
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={() => handleDelete(dept.id)}
                    title="Delete"
                  >
                    <IconTrash />
                  </button>
                </div>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Create/Edit Modal */}
      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>{editing ? "Edit Department" : "Add Department"}</h2>
              <button onClick={() => setShowModal(false)}>×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group">
                  <label>Department Name *</label>
                  <input
                    className="form-input"
                    required
                    value={formData.name}
                    onChange={(e) =>
                      setFormData((p) => ({ ...p, name: e.target.value }))
                    }
                  />
                </div>
                <div className="form-group">
                  <label>Description</label>
                  <textarea
                    className="form-input"
                    rows="3"
                    value={formData.description}
                    onChange={(e) =>
                      setFormData((p) => ({
                        ...p,
                        description: e.target.value,
                      }))
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
                  {saving ? "Saving..." : editing ? "Update" : "Create"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Delete Confirmation Modal */}
      {deleteConfirm && (
        <div className="modal-overlay" onClick={() => setDeleteConfirm(null)}>
          <div
            className="modal"
            onClick={(e) => e.stopPropagation()}
            style={{ maxWidth: 400 }}
          >
            <div className="modal-header">
              <h2>Delete Department</h2>
              <button onClick={() => setDeleteConfirm(null)}>×</button>
            </div>
            <div className="modal-body">
              <p>
                Are you sure you want to delete this department? Employees will
                be unassigned.
              </p>
            </div>
            <div className="modal-footer">
              <button
                className="btn btn-secondary"
                onClick={() => setDeleteConfirm(null)}
              >
                Cancel
              </button>
              <button className="btn btn-danger" onClick={confirmDelete}>
                Yes, Delete
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Departments;

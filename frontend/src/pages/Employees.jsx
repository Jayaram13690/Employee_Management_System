import { useState, useEffect } from "react";
import { employeeAPI, departmentAPI } from "../services/api";
import { IconSearch, IconEdit, IconTrash, IconPlus } from "../components/Icons";
import { toast } from "react-toastify";
import CustomDatePicker from "../components/CustomDatePicker";
import PasswordInput from "../components/PasswordInput";
import CustomSelect from "../components/CustomSelect";
import "./Employees.css";

const Employees = () => {
  const [employees, setEmployees] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState(null);
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    phone: "",
    designation: "",
    dateOfJoining: "",
    salary: "",
    departmentId: "",
    active: true,
  });
  const [saving, setSaving] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState(null);

  const fetchEmployees = async () => {
    try {
      const res = await employeeAPI.getAll(page, 10, "id", "asc", search);
      const data = res.data.data;
      setEmployees(data.content || []);
      setTotalPages(data.totalPages || 0);
    } catch (err) {
      console.error("Failed to fetch employees:", err);
    } finally {
      setLoading(false);
    }
  };

  const fetchDepartments = async () => {
    try {
      const res = await departmentAPI.getAll();
      setDepartments(res.data.data || []);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchEmployees();
  }, [page, search]);
  useEffect(() => {
    fetchDepartments();
  }, []);

  const openCreateModal = () => {
    setEditingEmployee(null);
    setFormData({
      email: "",
      password: "",
      firstName: "",
      lastName: "",
      phone: "",
      designation: "",
      dateOfJoining: "",
      salary: "",
      departmentId: "",
      active: true,
    });
    setShowModal(true);
  };

  const openEditModal = (emp) => {
    setEditingEmployee(emp);
    setFormData({
      firstName: emp.firstName,
      lastName: emp.lastName,
      phone: emp.phone || "",
      designation: emp.designation || "",
      dateOfJoining: emp.dateOfJoining || "",
      salary: emp.salary || "",
      departmentId: emp.departmentId || "",
      active: emp.active,
    });
    setShowModal(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editingEmployee) {
        await employeeAPI.update(editingEmployee.id, {
          firstName: formData.firstName,
          lastName: formData.lastName,
          phone: formData.phone,
          designation: formData.designation,
          dateOfJoining: formData.dateOfJoining || null,
          salary: formData.salary ? parseFloat(formData.salary) : null,
          departmentId: formData.departmentId
            ? parseInt(formData.departmentId)
            : null,
          active: formData.active,
        });
      } else {
        await employeeAPI.create({
          ...formData,
          salary: formData.salary ? parseFloat(formData.salary) : null,
          departmentId: formData.departmentId
            ? parseInt(formData.departmentId)
            : null,
        });
      }
      setShowModal(false);
      fetchEmployees();
      toast.success(
        editingEmployee
          ? "Employee updated successfully"
          : "Employee created successfully",
      );
    } catch (err) {
      toast.error(err.response?.data?.message || "Operation failed");
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    setDeleteConfirm(id);
  };

  const confirmDelete = async () => {
    if (!deleteConfirm) return;
    try {
      await employeeAPI.delete(deleteConfirm);
      setDeleteConfirm(null);
      fetchEmployees();
      toast.success("Employee deactivated successfully");
    } catch (err) {
      console.error("Delete failed:", err);
      toast.error(err.response?.data?.message || "Delete failed");
      setDeleteConfirm(null);
    }
  };

  const handleChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
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
          <h1>Employees</h1>
          <p>Manage all employees in the organization</p>
        </div>
        <div className="flex gap-md items-center">
          <div className="search-bar">
            <span className="search-icon">
              <IconSearch />
            </span>
            <input
              placeholder="Search employees..."
              value={search}
              onChange={(e) => {
                setSearch(e.target.value);
                setPage(0);
              }}
            />
          </div>
          <button className="btn btn-primary" onClick={openCreateModal}>
            <IconPlus /> Add Employee
          </button>
        </div>
      </div>

      <div className="card" style={{ padding: 0, overflow: "hidden" }}>
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Employee</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Designation</th>
                <th>Department</th>
                <th>Salary</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {employees.length === 0 ? (
                <tr>
                  <td
                    colSpan="8"
                    style={{
                      textAlign: "center",
                      padding: "40px",
                      color: "var(--text-muted)",
                    }}
                  >
                    No employees found
                  </td>
                </tr>
              ) : (
                employees.map((emp) => (
                  <tr key={emp.id}>
                    <td>
                      <div className="emp-name">
                        <div className="emp-avatar">
                          {emp.firstName?.[0]}
                          {emp.lastName?.[0]}
                        </div>
                        <span>
                          {emp.firstName} {emp.lastName}
                        </span>
                      </div>
                    </td>
                    <td>{emp.email}</td>
                    <td>{emp.phone || "—"}</td>
                    <td>{emp.designation || "—"}</td>
                    <td>{emp.departmentName || "—"}</td>
                    <td>
                      {emp.salary ? `₹${emp.salary.toLocaleString()}` : "—"}
                    </td>
                    <td>
                      <span
                        className={`badge ${emp.active ? "badge-success" : "badge-danger"}`}
                      >
                        {emp.active ? "Active" : "Inactive"}
                      </span>
                    </td>
                    <td>
                      <div className="flex gap-sm">
                        <button
                          className="btn btn-secondary btn-sm"
                          onClick={() => openEditModal(emp)}
                          title="Edit"
                        >
                          <IconEdit />
                        </button>
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => handleDelete(emp.id)}
                          title="Deactivate"
                        >
                          <IconTrash />
                        </button>
                      </div>
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

      {/* Create/Edit Modal */}
      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>{editingEmployee ? "Edit Employee" : "Add Employee"}</h2>
              <button onClick={() => setShowModal(false)}>×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                {!editingEmployee && (
                  <div
                    style={{
                      display: "grid",
                      gridTemplateColumns: "1fr 1fr",
                      gap: "var(--space-md)",
                    }}
                  >
                    <div className="form-group">
                      <label>Email *</label>
                      <input
                        className="form-input"
                        type="email"
                        required
                        value={formData.email}
                        onChange={(e) => handleChange("email", e.target.value)}
                      />
                    </div>
                    <div className="form-group">
                      <label>Password *</label>
                      <PasswordInput
                        id="employee-password"
                        value={formData.password}
                        onChange={(e) =>
                          handleChange("password", e.target.value)
                        }
                        placeholder="Min 8 chars: uppercase, lowercase, digit, special char"
                        required
                      />
                    </div>
                  </div>
                )}
                <div
                  style={{
                    display: "grid",
                    gridTemplateColumns: "1fr 1fr",
                    gap: "var(--space-md)",
                  }}
                >
                  <div className="form-group">
                    <label>First Name *</label>
                    <input
                      className="form-input"
                      required
                      value={formData.firstName}
                      onChange={(e) =>
                        handleChange("firstName", e.target.value)
                      }
                    />
                  </div>
                  <div className="form-group">
                    <label>Last Name *</label>
                    <input
                      className="form-input"
                      required
                      value={formData.lastName}
                      onChange={(e) => handleChange("lastName", e.target.value)}
                    />
                  </div>
                  <div className="form-group">
                    <label>Phone</label>
                    <input
                      className="form-input"
                      value={formData.phone}
                      onChange={(e) => handleChange("phone", e.target.value)}
                    />
                  </div>
                  <div className="form-group">
                    <label>Designation</label>
                    <input
                      className="form-input"
                      value={formData.designation}
                      onChange={(e) =>
                        handleChange("designation", e.target.value)
                      }
                    />
                  </div>
                  <div className="form-group">
                    <label>Date of Joining</label>
                    <CustomDatePicker
                      selected={
                        formData.dateOfJoining
                          ? new Date(formData.dateOfJoining)
                          : null
                      }
                      maxDate={new Date()}
                      onChange={(date) =>
                        handleChange(
                          "dateOfJoining",
                          date ? date.toISOString().split("T")[0] : "",
                        )
                      }
                    />
                  </div>
                  <div className="form-group">
                    <label>Salary</label>
                    <input
                      className="form-input"
                      type="number"
                      value={formData.salary}
                      onChange={(e) => handleChange("salary", e.target.value)}
                    />
                  </div>
                  <div className="form-group">
                    <CustomSelect
                      label="Department"
                      options={[
                        { id: "", label: "No Department" },
                        ...departments.map((d) => ({
                          id: d.id.toString(),
                          label: d.name,
                        })),
                      ]}
                      value={formData.departmentId}
                      onChange={(selectedId) =>
                        handleChange("departmentId", selectedId)
                      }
                      placeholder="Select a department"
                    />
                  </div>
                  <div
                    className="form-group"
                    style={{ justifyContent: "flex-end" }}
                  >
                    <label className="checkbox-label">
                      <input
                        type="checkbox"
                        checked={formData.active}
                        onChange={(e) =>
                          handleChange("active", e.target.checked)
                        }
                      />
                      <span>Active</span>
                    </label>
                  </div>
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
                  {saving ? "Saving..." : editingEmployee ? "Update" : "Create"}
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
              <h2>Deactivate Employee</h2>
              <button onClick={() => setDeleteConfirm(null)}>×</button>
            </div>
            <div className="modal-body">
              <p>
                Are you sure you want to deactivate this employee? They will no
                longer be able to log in.
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
                Yes, Deactivate
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Employees;

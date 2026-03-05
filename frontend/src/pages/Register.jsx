import { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { IconPlus, IconCheck, IconAlertCircle } from "../components/Icons";
import PasswordInput from "../components/PasswordInput";
import { toast } from "react-toastify";
import "./Register.css";

const Register = () => {
  const { register } = useAuth();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    phone: "",
    role: "EMPLOYEE",
  });
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");
  const [fieldErrors, setFieldErrors] = useState({});

  const handleChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    // Clear field error when user starts typing
    if (fieldErrors[field]) {
      setFieldErrors((prev) => ({ ...prev, [field]: "" }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccess("");
    setFieldErrors({});

    try {
      await register(formData);
      setSuccess(
        `User "${formData.email}" registered successfully as ${formData.role}.`,
      );
      toast.success(`✓ Registration successful for ${formData.email}!`);
      setFormData({
        email: "",
        password: "",
        firstName: "",
        lastName: "",
        phone: "",
        role: "EMPLOYEE",
      });
      // Fade out success message after 3 seconds
      setTimeout(() => setSuccess(""), 3000);
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Registration failed. Please try again.";
      const fieldErrs = err.response?.data?.fieldErrors || {};

      setError(errorMessage);
      setFieldErrors(fieldErrs);

      // Show toast with error
      toast.error(
        Object.keys(fieldErrs).length > 0
          ? `⚠ Validation Error: ${Object.values(fieldErrs).join(", ")}`
          : errorMessage
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Register New User</h1>
          <p>Create accounts for employees and admins</p>
        </div>
      </div>

      <div className="register-layout">
        <div className="card register-form-card">
          <h3>Create Account</h3>

          {success && (
            <div className="register-alert register-alert-success">
              <IconCheck /> {success}
            </div>
          )}

          {error && (
            <div className="register-alert register-alert-error">
              <IconAlertCircle /> {error}
            </div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="register-grid">
              <div className="form-group">
                <label>First Name *</label>
                <input
                  className={`form-input ${fieldErrors.firstName ? "input-error" : ""}`}
                  required
                  value={formData.firstName}
                  onChange={(e) => handleChange("firstName", e.target.value)}
                  placeholder="John"
                />
                {fieldErrors.firstName && (
                  <span className="field-error">{fieldErrors.firstName}</span>
                )}
              </div>
              <div className="form-group">
                <label>Last Name *</label>
                <input
                  className={`form-input ${fieldErrors.lastName ? "input-error" : ""}`}
                  required
                  value={formData.lastName}
                  onChange={(e) => handleChange("lastName", e.target.value)}
                  placeholder="Doe"
                />
                {fieldErrors.lastName && (
                  <span className="field-error">{fieldErrors.lastName}</span>
                )}
              </div>
            </div>

            <div className="form-group">
              <label>Email Address *</label>
              <input
                className={`form-input ${fieldErrors.email ? "input-error" : ""}`}
                type="email"
                required
                value={formData.email}
                onChange={(e) => handleChange("email", e.target.value)}
                placeholder="user@company.com"
              />
              {fieldErrors.email && (
                <span className="field-error">{fieldErrors.email}</span>
              )}
            </div>

            <div className="form-group">
              <label>Phone *</label>
              <input
                className={`form-input ${fieldErrors.phone ? "input-error" : ""}`}
                type="tel"
                required
                value={formData.phone}
                onChange={(e) => handleChange("phone", e.target.value)}
                placeholder="10-15 digits, e.g., +1 5551234567"
              />
              {fieldErrors.phone && (
                <span className="field-error">{fieldErrors.phone}</span>
              )}
            </div>

            <div className="form-group">
              <label>Password *</label>
              <PasswordInput
                id="password"
                value={formData.password}
                onChange={(e) => handleChange("password", e.target.value)}
                placeholder="Min 8 chars: uppercase, lowercase, digit, special char"
                required
                error={!!fieldErrors.password}
              />
              {fieldErrors.password && (
                <span className="field-error">{fieldErrors.password}</span>
              )}
            </div>

            <div className="form-group">
              <label>Role *</label>
              <div className="role-selector">
                <label
                  className={`role-option ${formData.role === "EMPLOYEE" ? "selected" : ""}`}
                >
                  <input
                    type="radio"
                    name="role"
                    value="EMPLOYEE"
                    checked={formData.role === "EMPLOYEE"}
                    onChange={(e) => handleChange("role", e.target.value)}
                  />
                  <div className="role-content">
                    <strong>Employee</strong>
                    <span>Standard access — attendance, leave requests</span>
                  </div>
                </label>
                <label
                  className={`role-option ${formData.role === "ADMIN" ? "selected" : ""}`}
                >
                  <input
                    type="radio"
                    name="role"
                    value="ADMIN"
                    checked={formData.role === "ADMIN"}
                    onChange={(e) => handleChange("role", e.target.value)}
                  />
                  <div className="role-content">
                    <strong>Admin</strong>
                    <span>Full access — manage employees, departments</span>
                  </div>
                </label>
              </div>
            </div>

            <button
              type="submit"
              className="btn btn-primary btn-lg btn-full"
              disabled={loading}
              style={{ marginTop: "var(--space-md)" }}
            >
              {loading ? (
                <>
                  <span
                    className="spinner"
                    style={{ width: 18, height: 18, borderWidth: 2 }}
                  ></span>{" "}
                  Creating Account...
                </>
              ) : (
                <>
                  <IconPlus /> Create Account
                </>
              )}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Register;

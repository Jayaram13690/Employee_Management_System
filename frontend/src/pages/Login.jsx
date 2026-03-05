import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { IconUsers, IconShield } from "../components/Icons";
import PasswordInput from "../components/PasswordInput";
import { toast } from "react-toastify";
import "./Login.css";

const Login = () => {
  // Load portal from localStorage, default to "admin"
  const [portal, setPortal] = useState(() => {
    return localStorage.getItem("selectedPortal") || "admin";
  });
  const [mode, setMode] = useState("signin");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phone, setPhone] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const { login, register, logout } = useAuth();
  const navigate = useNavigate();

  const resetForm = () => {
    setEmail("");
    setPassword("");
    setFirstName("");
    setLastName("");
    setPhone("");
    setError("");
    setSuccess("");
  };

  // Just update email without clearing error (error will clear on submit)
  const handleEmailChange = (value) => {
    setEmail(value);
  };

  // Just update password without clearing error (error will clear on submit)
  const handlePasswordChange = (value) => {
    setPassword(value);
  };

  const switchPortal = (p) => {
    setPortal(p);
    localStorage.setItem("selectedPortal", p); // Save to localStorage
    setMode("signin");
    resetForm();
  };
  const switchMode = (m) => {
    // Just switch mode, don't reset form data or error
    // This allows user to see error and try again on same tab
    setMode(m);
  };

  const handleSignIn = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const userData = await login(email, password);

      // Validate role matches the selected portal
      const expectedRole = portal === "admin" ? "ADMIN" : "EMPLOYEE";
      if (userData.role !== expectedRole) {
        logout(); // Clear the invalid session
        const roleError = portal === "admin"
          ? "This account is not an Admin. Please use the Employee portal."
          : "This account is not an Employee. Please use the Admin portal.";
        setError(roleError);
        toast.error(roleError, { autoClose: 5000 });
        setLoading(false);
        return;
      }

      // Clear error on successful login
      setError("");
      toast.success(`✓ Welcome back, ${userData.fullName}!`);
      navigate(userData.role === "ADMIN" ? "/dashboard" : "/my-dashboard");
    } catch (err) {
      const errorMsg = err.response?.data?.message ||
        "Login failed. Please check your credentials.";
      setError(errorMsg);
      // Show error toast for longer (5 seconds instead of default 3)
      toast.error(errorMsg, { autoClose: 5000 });
      // Keep loading state briefly to show the error
      setTimeout(() => setLoading(false), 300);
    }
  };

  const handleSignUp = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    // Validate phone format
    const phoneRegex = /^[+]?[0-9]{10,15}$/;
    if (!phone || !phoneRegex.test(phone)) {
      const phoneError = "Phone must be 10-15 digits (optional +)";
      setError(phoneError);
      toast.error(phoneError);
      return;
    }

    // Validate password strength
    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,}$/;
    if (!passwordRegex.test(password)) {
      const passwordError =
        "Password must be at least 8 characters with uppercase, lowercase, digit, and special character";
      setError(passwordError);
      toast.error(passwordError);
      return;
    }

    setLoading(true);
    try {
      await register({
        email,
        password,
        firstName,
        lastName,
        phone,
        role: "ADMIN",
      });
      setSuccess("Admin account created! You can now sign in.");
      toast.success("✓ Admin account created successfully!");
      setTimeout(() => {
        setMode("signin");
      }, 1500);
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Registration failed. Please try again.";
      const fieldErrs = err.response?.data?.fieldErrors || {};

      setError(errorMessage);

      const errorToShow = Object.keys(fieldErrs).length > 0
        ? `⚠ ${Object.values(fieldErrs).join(", ")}`
        : errorMessage;

      toast.error(errorToShow);
    } finally {
      setLoading(false);
    }
  };

  const isAdmin = portal === "admin";

  return (
    <div className="login-page">
      <div className="login-card">
        {/* ===== Left Panel — Branding ===== */}
        <div
          className={`login-left ${isAdmin ? "admin-theme" : "employee-theme"}`}
        >
          <div className="login-left-shapes">
            <div className="left-shape left-shape-1"></div>
            <div className="left-shape left-shape-2"></div>
            <div className="left-shape left-shape-3"></div>
          </div>
          <div className="login-left-content">
            <div className="login-logo">
              {isAdmin ? (
                <IconShield style={{ width: "3rem", height: "3rem" }} />
              ) : (
                <IconUsers style={{ width: "3rem", height: "3rem" }} />
              )}
            </div>
            <div className="portal-badge">
              {isAdmin ? "ADMIN PORTAL" : "EMPLOYEE PORTAL"}
            </div>
            <h1>EMP Manager</h1>
            <p className="login-tagline">
              Enterprise Employee Management System
            </p>
            <p className="login-desc">
              {isAdmin
                ? "Manage your workforce with powerful tools for employee records, departments, attendance tracking, and leave management."
                : "Access your attendance records, apply for leave, and view your employment details from one dashboard."}
            </p>
            <div className="login-features">
              {isAdmin ? (
                <>
                  <div className="login-feature">
                    ✓ Employee & Department Management
                  </div>
                  <div className="login-feature">
                    ✓ Attendance Tracking & Reporting
                  </div>
                  <div className="login-feature">✓ Leave Request Approvals</div>
                  <div className="login-feature">✓ Full Admin Dashboard</div>
                </>
              ) : (
                <>
                  <div className="login-feature">✓ View Attendance Records</div>
                  <div className="login-feature">✓ Apply for Leave</div>
                  <div className="login-feature">✓ Personal Dashboard</div>
                  <div className="login-feature">✓ Track Leave Status</div>
                </>
              )}
            </div>
          </div>
        </div>

        {/* ===== Right Panel — Form ===== */}
        <div className="login-right">
          {/* Portal Switcher */}
          <div className="portal-switcher">
            <button
              className={`portal-btn ${isAdmin ? "active admin-active" : ""}`}
              onClick={() => switchPortal("admin")}
            >
              <IconShield style={{ width: "0.9em", height: "0.9em" }} /> Admin
            </button>
            <button
              className={`portal-btn ${!isAdmin ? "active employee-active" : ""}`}
              onClick={() => switchPortal("employee")}
            >
              <IconUsers style={{ width: "0.9em", height: "0.9em" }} /> Employee
            </button>
          </div>

          {/* Auth Tabs — only for Admin */}
          {isAdmin && (
            <div className="auth-tabs">
              <button
                className={`auth-tab ${mode === "signin" ? "active" : ""}`}
                onClick={() => switchMode("signin")}
              >
                Sign In
              </button>
              <button
                className={`auth-tab ${mode === "signup" ? "active" : ""}`}
                onClick={() => switchMode("signup")}
              >
                Sign Up
              </button>
            </div>
          )}

          {error && (
            <div className="form-message form-message-error">
              <span className="message-icon">⚠</span>
              <span>{error}</span>
              <button
                type="button"
                className="message-close-btn"
                onClick={() => setError("")}
                title="Dismiss error"
              >
                ×
              </button>
            </div>
          )}
          {success && (
            <div className="form-message form-message-success">
              <span className="message-icon">✓</span>
              <span>{success}</span>
              <button
                type="button"
                className="message-close-btn"
                onClick={() => setSuccess("")}
                title="Dismiss message"
              >
                ×
              </button>
            </div>
          )}

          {/* ===== Admin Sign In ===== */}
          {isAdmin && mode === "signin" && (
            <form onSubmit={handleSignIn}>
              <h2 className="form-title">Admin Sign In</h2>
              <p className="form-subtitle">Sign in to your admin account</p>
              <div className="form-group">
                <label htmlFor="email">Email</label>
                <input
                  id="email"
                  type="email"
                  className="form-input"
                  placeholder="admin@company.com"
                  value={email}
                  onChange={(e) => handleEmailChange(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="password">Password</label>
                <PasswordInput
                  id="password"
                  value={password}
                  onChange={(e) => handlePasswordChange(e.target.value)}
                  placeholder="Enter your password"
                  required
                />
              </div>
              <button
                type="submit"
                className="btn btn-primary btn-full btn-lg"
                disabled={loading}
              >
                {loading ? (
                  <>
                    <span
                      className="spinner"
                      style={{ width: 18, height: 18, borderWidth: 2 }}
                    ></span>{" "}
                    Signing in...
                  </>
                ) : (
                  "Sign In"
                )}
              </button>
              <div className="login-footer">
                <span>Don't have an account?</span>
                <button
                  type="button"
                  className="auth-switch-link"
                  onClick={() => switchMode("signup")}
                >
                  Sign Up
                </button>
              </div>
            </form>
          )}

          {/* ===== Admin Sign Up ===== */}
          {isAdmin && mode === "signup" && (
            <form onSubmit={handleSignUp}>
              <h2 className="form-title">Create Admin Account</h2>
              <p className="form-subtitle">
                Register a new administrator account
              </p>
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="firstName">First Name</label>
                  <input
                    id="firstName"
                    className="form-input"
                    placeholder="John"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="lastName">Last Name</label>
                  <input
                    id="lastName"
                    className="form-input"
                    placeholder="Doe"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    required
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="signupEmail">Email</label>
                  <input
                    id="signupEmail"
                    type="email"
                    className="form-input"
                    placeholder="admin@company.com"
                    value={email}
                    onChange={(e) => handleEmailChange(e.target.value)}
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="signupPhone">Phone</label>
                  <input
                    id="signupPhone"
                    type="tel"
                    className="form-input"
                    placeholder="+1 555-1234567"
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    required
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group" style={{ gridColumn: "1 / -1" }}>
                  <label htmlFor="signupPassword">Password</label>
                  <PasswordInput
                    id="signupPassword"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Min 8 chars: uppercase, lowercase, digit, special char"
                    required
                  />
                </div>
              </div>
              <button
                type="submit"
                className="btn btn-primary btn-full btn-lg"
                disabled={loading}
              >
                {loading ? (
                  <>
                    <span
                      className="spinner"
                      style={{ width: 18, height: 18, borderWidth: 2 }}
                    ></span>{" "}
                    Creating...
                  </>
                ) : (
                  "Sign Up"
                )}
              </button>
              <div className="login-footer">
                <span>Already have an account?</span>
                <button
                  type="button"
                  className="auth-switch-link"
                  onClick={() => switchMode("signin")}
                >
                  Sign In
                </button>
              </div>
            </form>
          )}

          {/* ===== Employee Login ===== */}
          {!isAdmin && (
            <form onSubmit={handleSignIn}>
              <h2 className="form-title">Employee Login</h2>
              <p className="form-subtitle">
                Sign in with your employee credentials
              </p>
              <div className="form-group">
                <label htmlFor="empEmail">Email</label>
                <input
                  id="empEmail"
                  type="email"
                  className="form-input"
                  placeholder="you@company.com"
                  value={email}
                  onChange={(e) => handleEmailChange(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="empPassword">Password</label>
                <PasswordInput
                  id="empPassword"
                  value={password}
                  onChange={(e) => handlePasswordChange(e.target.value)}
                  placeholder="Enter your password"
                  required
                />
              </div>
              <button
                type="submit"
                className="btn btn-primary btn-full btn-lg employee-btn"
                disabled={loading}
              >
                {loading ? (
                  <>
                    <span
                      className="spinner"
                      style={{ width: 18, height: 18, borderWidth: 2 }}
                    ></span>{" "}
                    Signing in...
                  </>
                ) : (
                  "Login"
                )}
              </button>
              <div className="login-footer">
                <span>Are you an admin?</span>
                <button
                  type="button"
                  className="auth-switch-link"
                  onClick={() => switchPortal("admin")}
                >
                  Go to Admin Portal
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

export default Login;

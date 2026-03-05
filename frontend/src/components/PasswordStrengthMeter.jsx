import "./PasswordStrengthMeter.css";

const PasswordStrengthMeter = ({ password = "" }) => {
  const requirements = [
    { label: "At least 8 characters", check: password.length >= 8 },
    {
      label: "Contains uppercase letter (A-Z)",
      check: /[A-Z]/.test(password),
    },
    {
      label: "Contains lowercase letter (a-z)",
      check: /[a-z]/.test(password),
    },
    { label: "Contains number (0-9)", check: /[0-9]/.test(password) },
    {
      label: "Contains special character (!@#$%^&*)",
      check: /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password),
    },
  ];

  const metRequirements = requirements.filter((r) => r.check).length;
  const strength =
    metRequirements === 0
      ? 0
      : metRequirements <= 2
        ? 1
        : metRequirements <= 3
          ? 2
          : metRequirements <= 4
            ? 3
            : 4;

  const strengthLabels = ["None", "Weak", "Fair", "Good", "Strong"];
  const strengthColors = [
    "var(--text-muted)",
    "var(--danger)",
    "var(--warning)",
    "var(--info)",
    "var(--success)",
  ];

  const isComplete = requirements.every((r) => r.check);

  return (
    <div className="password-strength-meter">
      {password && (
        <>
          <div className="strength-bar-container">
            <div
              className="strength-bar"
              style={{
                width: `${(metRequirements / requirements.length) * 100}%`,
                backgroundColor: strengthColors[strength],
              }}
            />
          </div>
          <div
            className="strength-label"
            style={{ color: strengthColors[strength] }}
          >
            Strength: {strengthLabels[strength]}
          </div>
        </>
      )}

      <div className="requirements-list">
        {requirements.map((req, i) => (
          <div
            key={i}
            className={`requirement ${req.check ? "met" : "unmet"}`}
          >
            <span className="check-icon">
              {req.check ? "✓" : "○"}
            </span>
            <span className="requirement-label">{req.label}</span>
          </div>
        ))}
      </div>

      {password && !isComplete && (
        <div className="password-hint">
          Complete all requirements for a strong password
        </div>
      )}
    </div>
  );
};

export default PasswordStrengthMeter;

import { useState } from "react";
import { IconEye, IconEyeOff } from "./Icons";
import "./PasswordInput.css";

const PasswordInput = ({
  id,
  value,
  onChange,
  placeholder = "Enter password",
  required = false,
  error = false
}) => {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="password-input-wrapper">
      <input
        id={id}
        type={showPassword ? "text" : "password"}
        className={`form-input password-input ${error ? "input-error" : ""}`}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
      />
      <button
        type="button"
        className="password-toggle-btn"
        onClick={() => setShowPassword(!showPassword)}
        title={showPassword ? "Hide password" : "Show password"}
        tabIndex="-1"
      >
        {showPassword ? <IconEyeOff /> : <IconEye />}
      </button>
    </div>
  );
};

export default PasswordInput;

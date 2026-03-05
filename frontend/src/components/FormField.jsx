import { useState, useEffect } from "react";
import { IconAlertCircle, IconCheckCircle } from "./Icons";
import "./FormField.css";

/**
 * FormField Component
 * Enhanced form input wrapper with floating labels, validation states, and accessibility
 *
 * @param {string} label - Field label text
 * @param {string} type - Input type (text, email, password, number, tel, url, etc.)
 * @param {string} value - Input value
 * @param {Function} onChange - Change handler
 * @param {string} placeholder - Placeholder text
 * @param {string} error - Error message (shows if provided)
 * @param {string} success - Success message
 * @param {boolean} disabled - Disable the input
 * @param {boolean} required - Show required indicator
 * @param {string} helpText - Helper text below input
 * @param {boolean} isLoading - Show loading state
 * @param {string} id - Input ID
 * @param {string} name - Input name
 * @param {object} inputProps - Additional input props
 * @param {boolean} autoComplete - Enable autocomplete
 * @param {string} className - Additional CSS classes
 */
const FormField = ({
  label,
  type = "text",
  value,
  onChange,
  placeholder,
  error,
  success,
  disabled = false,
  required = false,
  helpText,
  isLoading = false,
  id,
  name,
  inputProps = {},
  autoComplete,
  className = "",
  onBlur,
  onFocus,
}) => {
  const [isFocused, setIsFocused] = useState(false);
  const [hasValue, setHasValue] = useState(!!value);

  // Update hasValue when value changes externally
  useEffect(() => {
    setHasValue(!!value);
  }, [value]);

  const handleChange = (e) => {
    setHasValue(!!e.target.value);
    onChange(e);
  };

  const handleFocus = (e) => {
    setIsFocused(true);
    onFocus?.(e);
  };

  const handleBlur = (e) => {
    setIsFocused(false);
    onBlur?.(e);
  };

  const fieldId = id || `field-${Math.random().toString(36).slice(2, 9)}`;
  const hasError = !!error;
  const hasSuccess = !!success && !hasError;

  return (
    <div
      className={`form-field-wrapper ${className} ${
        hasError ? "has-error" : ""
      } ${hasSuccess ? "has-success" : ""} ${disabled ? "disabled" : ""}`}
    >
      {/* Label - positioned for floating effect */}
      {label && (
        <label
          htmlFor={fieldId}
          className={`form-field-label ${isFocused || hasValue ? "floating" : ""}`}
        >
          {label}
          {required && <span className="required-indicator">*</span>}
        </label>
      )}

      {/* Input container for positioning icons */}
      <div className="form-field-input-wrapper">
        <input
          id={fieldId}
          type={type}
          name={name}
          value={value}
          onChange={handleChange}
          onFocus={handleFocus}
          onBlur={handleBlur}
          placeholder={placeholder}
          disabled={disabled || isLoading}
          required={required}
          autoComplete={autoComplete}
          className={`form-field-input form-input ${
            hasError ? "input-error" : ""
          } ${hasSuccess ? "input-success" : ""} ${isLoading ? "loading" : ""}`}
          aria-invalid={hasError}
          aria-describedby={
            [
              error ? `${fieldId}-error` : "",
              success ? `${fieldId}-success` : "",
              helpText ? `${fieldId}-help` : "",
            ]
              .filter(Boolean)
              .join(" ") || undefined
          }
          aria-required={required}
          {...inputProps}
        />

        {/* Loading spinner overlay */}
        {isLoading && (
          <div className="form-field-loading-spinner" aria-hidden="true" />
        )}

        {/* Status icons - Error or Success */}
        {hasError && (
          <IconAlertCircle
            className="form-field-icon form-field-icon-error"
            aria-hidden="true"
          />
        )}
        {hasSuccess && (
          <IconCheckCircle
            className="form-field-icon form-field-icon-success"
            aria-hidden="true"
          />
        )}
      </div>

      {/* Help text */}
      {helpText && !hasError && !hasSuccess && (
        <p className="form-field-help-text" id={`${fieldId}-help`}>
          {helpText}
        </p>
      )}

      {/* Error message */}
      {hasError && (
        <p className="form-field-error-message" id={`${fieldId}-error`}>
          <IconAlertCircle className="icon-inline" aria-hidden="true" />
          {error}
        </p>
      )}

      {/* Success message */}
      {hasSuccess && (
        <p className="form-field-success-message" id={`${fieldId}-success`}>
          <IconCheckCircle className="icon-inline" aria-hidden="true" />
          {success}
        </p>
      )}
    </div>
  );
};

export default FormField;

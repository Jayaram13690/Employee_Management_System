import { useState, useRef, useEffect } from "react";
import { IconChevronDown } from "./Icons";
import "./CustomSelect.css";

/**
 * CustomSelect Component
 * Modern, accessible dropdown/select replacement with keyboard navigation and ARIA support
 *
 * @param {Array} options - Array of {id, label} objects or strings
 * @param {string|number} value - Currently selected value (id)
 * @param {Function} onChange - Callback when selection changes: onChange(selectedId)
 * @param {string} placeholder - Placeholder text when nothing selected
 * @param {boolean} disabled - Disable the dropdown
 * @param {boolean} isLoading - Show loading state
 * @param {string} error - Error message to display
 * @param {string} label - Field label
 * @param {boolean} required - Show required indicator
 * @param {string} className - Additional CSS classes
 */
const CustomSelect = ({
  options = [],
  value,
  onChange,
  placeholder = "Select an option",
  disabled = false,
  isLoading = false,
  error,
  label,
  required = false,
  className = "",
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [highlightedIndex, setHighlightedIndex] = useState(-1);
  const containerRef = useRef(null);
  const triggerRef = useRef(null);
  const listRef = useRef(null);

  // Normalize options to {id, label} format
  const normalizedOptions = options.map((opt) =>
    typeof opt === "string" ? { id: opt, label: opt } : opt
  );

  // Find selected option
  const selectedOption = normalizedOptions.find((opt) => opt.id === value);
  const selectedLabel = selectedOption?.label || placeholder;

  // Handle click outside to close dropdown
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (containerRef.current && !containerRef.current.contains(e.target)) {
        setIsOpen(false);
      }
    };

    if (isOpen) {
      document.addEventListener("mousedown", handleClickOutside);
      return () => document.removeEventListener("mousedown", handleClickOutside);
    }
  }, [isOpen]);

  // Handle keyboard navigation
  const handleKeyDown = (e) => {
    if (disabled || isLoading) return;

    switch (e.key) {
      case "ArrowDown":
        e.preventDefault();
        if (!isOpen) {
          setIsOpen(true);
        } else {
          setHighlightedIndex((prev) =>
            prev < normalizedOptions.length - 1 ? prev + 1 : 0
          );
        }
        break;

      case "ArrowUp":
        e.preventDefault();
        if (isOpen) {
          setHighlightedIndex((prev) =>
            prev > 0 ? prev - 1 : normalizedOptions.length - 1
          );
        }
        break;

      case "Enter":
      case " ":
        e.preventDefault();
        if (!isOpen) {
          setIsOpen(true);
        } else if (highlightedIndex >= 0) {
          onChange(normalizedOptions[highlightedIndex].id);
          setIsOpen(false);
          setHighlightedIndex(-1);
        }
        break;

      case "Escape":
        e.preventDefault();
        setIsOpen(false);
        setHighlightedIndex(-1);
        break;

      case "Home":
        e.preventDefault();
        if (isOpen) {
          setHighlightedIndex(0);
        }
        break;

      case "End":
        e.preventDefault();
        if (isOpen) {
          setHighlightedIndex(normalizedOptions.length - 1);
        }
        break;

      default:
        // Handle character search (jump to option starting with character)
        if (isOpen && e.key.length === 1 && /[a-zA-Z0-9]/.test(e.key)) {
          e.preventDefault();
          const char = e.key.toLowerCase();
          const startIndex = highlightedIndex + 1;

          for (let i = 0; i < normalizedOptions.length; i++) {
            const index = (startIndex + i) % normalizedOptions.length;
            if (
              normalizedOptions[index].label
                .toLowerCase()
                .startsWith(char)
            ) {
              setHighlightedIndex(index);
              break;
            }
          }
        }
        break;
    }
  };

  // Scroll highlighted option into view
  useEffect(() => {
    if (isOpen && highlightedIndex >= 0 && listRef.current) {
      const highlightedElement = listRef.current.children[highlightedIndex];
      if (highlightedElement) {
        highlightedElement.scrollIntoView({
          block: "nearest",
          behavior: "smooth",
        });
      }
    }
  }, [highlightedIndex, isOpen]);

  // Reset highlighted index when dropdown closes
  useEffect(() => {
    if (!isOpen) {
      setHighlightedIndex(-1);
    }
  }, [isOpen]);

  const handleOptionClick = (optionId) => {
    onChange(optionId);
    setIsOpen(false);
    setHighlightedIndex(-1);
    triggerRef.current?.focus();
  };

  const handleTriggerClick = () => {
    if (!disabled && !isLoading) {
      setIsOpen(!isOpen);
    }
  };

  return (
    <div
      className={`custom-select-wrapper ${className} ${
        error ? "has-error" : ""
      }`}
      ref={containerRef}
    >
      {label && (
        <label className="custom-select-label">
          {label}
          {required && <span className="required-indicator">*</span>}
        </label>
      )}

      <div className="custom-select-container">
        <button
          ref={triggerRef}
          type="button"
          className={`custom-select-trigger form-input ${
            isOpen ? "open" : ""
          } ${disabled ? "disabled" : ""} ${isLoading ? "loading" : ""} ${
            error ? "input-error" : ""
          }`}
          onClick={handleTriggerClick}
          onKeyDown={handleKeyDown}
          disabled={disabled || isLoading}
          aria-haspopup="listbox"
          aria-expanded={isOpen}
          aria-label={label || "Select an option"}
          aria-disabled={disabled || isLoading}
          aria-invalid={!!error}
          aria-describedby={error ? `${label}-error` : undefined}
        >
          <span className="custom-select-value">{selectedLabel}</span>
          <IconChevronDown
            className={`custom-select-icon ${isOpen ? "rotated" : ""}`}
          />
        </button>

        {isLoading && <div className="custom-select-loading-spinner" />}
      </div>

      {isOpen && (
        <ul
          ref={listRef}
          className="custom-select-menu"
          role="listbox"
          aria-label={label || "Options"}
        >
          {normalizedOptions.length === 0 ? (
            <li className="custom-select-empty">No options available</li>
          ) : (
            normalizedOptions.map((option, index) => (
              <li
                key={option.id}
                role="option"
                aria-selected={option.id === value}
                className={`custom-select-option ${
                  option.id === value ? "selected" : ""
                } ${index === highlightedIndex ? "highlighted" : ""}`}
                onClick={() => handleOptionClick(option.id)}
                onMouseEnter={() => setHighlightedIndex(index)}
              >
                <span className="custom-select-option-label">
                  {option.label}
                </span>
                {option.id === value && (
                  <span className="custom-select-checkmark">✓</span>
                )}
              </li>
            ))
          )}
        </ul>
      )}

      {error && (
        <div className="custom-select-error" id={`${label}-error`}>
          {error}
        </div>
      )}
    </div>
  );
};

export default CustomSelect;

import React, { forwardRef, useState, useRef, useEffect } from "react";
import DatePicker from "react-datepicker";
import { format } from "date-fns";
import { IconChevronLeft, IconChevronRight, IconCalendar } from "./Icons";
import "react-datepicker/dist/react-datepicker.css";
import "./CustomDatePicker.css";

const CustomInput = forwardRef(
  ({ value, onClick, onChange, placeholder, required }, ref) => (
    <div className="custom-datepicker-input-wrapper" onClick={onClick}>
      <input
        value={value}
        onChange={onChange}
        ref={ref}
        className="form-input"
        placeholder={placeholder || "Select date"}
        readOnly
        required={required}
        style={{ paddingRight: "40px", cursor: "pointer" }}
      />
      <IconCalendar className="custom-datepicker-icon" />
    </div>
  ),
);

// Custom Dropdown component to replace native <select>
const CustomSelector = ({ value, options, onChange, type }) => {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div className="custom-selector-container" ref={dropdownRef}>
      <button
        type="button"
        className="custom-selector-btn"
        onClick={() => setIsOpen(!isOpen)}
      >
        {value}
      </button>

      {isOpen && (
        <ul className="custom-selector-list">
          {options.map((option) => (
            <li
              key={option}
              className={`custom-selector-item ${
                option === value ? "selected" : ""
              }`}
              onClick={() => {
                onChange(option);
                setIsOpen(false);
              }}
            >
              {option}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

const CustomDatePicker = ({
  selected,
  onChange,
  placeholderText,
  required,
  minDate,
  maxDate,
}) => {
  return (
    <DatePicker
      selected={selected}
      onChange={onChange}
      customInput={
        <CustomInput required={required} placeholder={placeholderText} />
      }
      dateFormat="yyyy-MM-dd"
      minDate={minDate}
      maxDate={maxDate}
      showPopperArrow={false}
      popperPlacement="bottom"
      renderCustomHeader={({
        date,
        changeYear,
        changeMonth,
        decreaseMonth,
        increaseMonth,
        prevMonthButtonDisabled,
        nextMonthButtonDisabled,
      }) => {
        const months = [
          "January",
          "February",
          "March",
          "April",
          "May",
          "June",
          "July",
          "August",
          "September",
          "October",
          "November",
          "December",
        ];

        // Generate a reasonable range of years (e.g. 1950 to current year)
        const currentYear = new Date().getFullYear();
        const years = [];
        for (let i = 1950; i <= currentYear; i++) {
          years.push(i);
        }

        return (
          <div className="custom-datepicker-header">
            <button
              type="button"
              onClick={decreaseMonth}
              disabled={prevMonthButtonDisabled}
              className="custom-datepicker-nav-btn"
            >
              <IconChevronLeft />
            </button>
            <div className="custom-datepicker-selectors">
              <CustomSelector
                value={months[date.getMonth()]}
                options={months}
                onChange={(selectedValue) =>
                  changeMonth(months.indexOf(selectedValue))
                }
              />
              <CustomSelector
                value={date.getFullYear()}
                options={years}
                onChange={(selectedValue) => changeYear(Number(selectedValue))}
              />
            </div>
            <button
              type="button"
              onClick={increaseMonth}
              disabled={nextMonthButtonDisabled}
              className="custom-datepicker-nav-btn"
            >
              <IconChevronRight />
            </button>
          </div>
        );
      }}
    />

  );
};

export default CustomDatePicker;

import { useState } from "react";
import CustomSelect from "./CustomSelect";
import FormField from "./FormField";
import CustomDatePicker from "./CustomDatePicker";
import PasswordInput from "./PasswordInput";
import "./ComponentsDemo.css";

/**
 * ComponentsDemo
 * Comprehensive showcase of all form components and their states
 * Useful for testing accessibility, keyboard navigation, and visual states
 *
 * Not included in production builds - for development/testing only
 */
const ComponentsDemo = () => {
  const [demoState, setDemoState] = useState({
    selectValue: "",
    textValue: "",
    emailValue: "",
    phoneValue: "",
    passwordValue: "",
    dateValue: null,
  });

  const departmentOptions = [
    { id: "1", label: "Engineering" },
    { id: "2", label: "Human Resources" },
    { id: "3", label: "Marketing" },
    { id: "4", label: "Sales" },
    { id: "5", label: "Finance" },
  ];

  const handleChange = (field, value) => {
    setDemoState((prev) => ({ ...prev, [field]: value }));
  };

  return (
    <div className="components-demo">
      <div className="demo-header">
        <h1>Component Demo & Testing</h1>
        <p>
          Testing ground for all form components. Check keyboard navigation,
          accessibility, and visual states.
        </p>
      </div>

      <div className="demo-container">
        {/* ===== CustomSelect Demo ===== */}
        <section className="demo-section">
          <h2>CustomSelect Component</h2>
          <p className="section-description">
            Modern dropdown with keyboard navigation (Arrow keys, Enter,
            Escape), ARIA support, and smooth animations.
          </p>

          <div className="demo-grid">
            <div className="demo-item">
              <h3>Normal State</h3>
              <CustomSelect
                label="Select Department"
                options={departmentOptions}
                value={demoState.selectValue}
                onChange={(val) => handleChange("selectValue", val)}
                placeholder="Choose a department"
                required
              />
              <p className="state-label">
                Selected: {demoState.selectValue || "None"}
              </p>
            </div>

            <div className="demo-item">
              <h3>Disabled State</h3>
              <CustomSelect
                label="Disabled Select"
                options={departmentOptions}
                value="1"
                onChange={() => {}}
                disabled={true}
              />
              <p className="state-label">Disabled (pointer-events: none)</p>
            </div>

            <div className="demo-item">
              <h3>Loading State</h3>
              <CustomSelect
                label="Loading Select"
                options={departmentOptions}
                value=""
                onChange={() => {}}
                isLoading={true}
              />
              <p className="state-label">With animated spinner</p>
            </div>

            <div className="demo-item">
              <h3>Error State</h3>
              <CustomSelect
                label="Department"
                options={departmentOptions}
                value=""
                onChange={() => {}}
                error="Please select a valid department"
              />
              <p className="state-label">Red border + error message</p>
            </div>
          </div>

          <div className="keyboard-nav-info">
            <h4>Keyboard Navigation:</h4>
            <ul>
              <li>
                <kbd>Tab</kbd> - Focus dropdown
              </li>
              <li>
                <kbd>Enter</kbd> or <kbd>Space</kbd> - Open/select
              </li>
              <li>
                <kbd>↑↓</kbd> - Navigate options
              </li>
              <li>
                <kbd>Esc</kbd> - Close dropdown
              </li>
              <li>
                <kbd>Home</kbd> / <kbd>End</kbd> - Jump to first/last
              </li>
              <li>
                <kbd>a-z</kbd> - Jump to option starting with letter
              </li>
            </ul>
          </div>
        </section>

        {/* ===== FormField Demo ===== */}
        <section className="demo-section">
          <h2>FormField Component</h2>
          <p className="section-description">
            Enhanced form input with floating labels, validation feedback, error
            & success states.
          </p>

          <div className="demo-grid">
            <div className="demo-item">
              <h3>Normal State</h3>
              <FormField
                label="Full Name"
                type="text"
                value={demoState.textValue}
                onChange={(e) => handleChange("textValue", e.target.value)}
                placeholder="Enter your full name"
                helpText="Enter your first and last name"
              />
              <p className="state-label">With help text</p>
            </div>

            <div className="demo-item">
              <h3>Error State</h3>
              <FormField
                label="Email"
                type="email"
                value=""
                onChange={() => {}}
                error="Please enter a valid email address"
                required
              />
              <p className="state-label">Red border + error icon + message</p>
            </div>

            <div className="demo-item">
              <h3>Success State</h3>
              <FormField
                label="Confirmation"
                type="text"
                value="Verified"
                onChange={() => {}}
                success="Email verified successfully"
              />
              <p className="state-label">Green border + checkmark + message</p>
            </div>

            <div className="demo-item">
              <h3>Loading State</h3>
              <FormField
                label="Processing"
                type="text"
                value=""
                onChange={() => {}}
                isLoading={true}
              />
              <p className="state-label">With animated spinner</p>
            </div>

            <div className="demo-item">
              <h3>Disabled State</h3>
              <FormField
                label="Read Only"
                type="text"
                value="Locked"
                onChange={() => {}}
                disabled={true}
              />
              <p className="state-label">Faded appearance</p>
            </div>

            <div className="demo-item">
              <h3>Required Field</h3>
              <FormField
                label="Required Field"
                type="text"
                value={demoState.emailValue}
                onChange={(e) => handleChange("emailValue", e.target.value)}
                required
                placeholder="This field is required"
              />
              <p className="state-label">Red asterisk indicator</p>
            </div>
          </div>

          <div className="features-info">
            <h4>Features:</h4>
            <ul>
              <li>Floating label animations</li>
              <li>Inline validation feedback</li>
              <li>Error/Success/Loading states</li>
              <li>Status icons with animations</li>
              <li>Full keyboard accessibility</li>
              <li>ARIA attributes for screen readers</li>
            </ul>
          </div>
        </section>

        {/* ===== PasswordInput Demo ===== */}
        <section className="demo-section">
          <h2>PasswordInput Component</h2>
          <p className="section-description">
            Password input with visibility toggle, show/hide eye icons.
          </p>

          <div className="demo-grid">
            <div className="demo-item">
              <h3>Default State</h3>
              <div className="form-group">
                <label>Password</label>
                <PasswordInput
                  id="password-demo"
                  value={demoState.passwordValue}
                  onChange={(e) =>
                    handleChange("passwordValue", e.target.value)
                  }
                  placeholder="Click eye icon to toggle visibility"
                  required
                />
              </div>
              <p className="state-label">Click eye icon to show/hide</p>
            </div>

            <div className="demo-item">
              <h3>With Error State</h3>
              <div className="form-group">
                <label>Password (Error)</label>
                <PasswordInput
                  id="password-error"
                  value=""
                  onChange={() => {}}
                  error
                />
              </div>
              <p className="state-label">Red border styling</p>
            </div>
          </div>

          <div className="features-info">
            <h4>Features:</h4>
            <ul>
              <li>Eye icon toggle for visibility</li>
              <li>Professional SVG icons</li>
              <li>Smooth transitions</li>
              <li>Error state support</li>
            </ul>
          </div>
        </section>

        {/* ===== CustomDatePicker Demo ===== */}
        <section className="demo-section">
          <h2>CustomDatePicker Component</h2>
          <p className="section-description">
            Date picker with month/year selectors, keyboard navigation.
          </p>

          <div className="demo-grid">
            <div className="demo-item">
              <h3>Normal State</h3>
              <div className="form-group">
                <label>Select Date</label>
                <CustomDatePicker
                  selected={demoState.dateValue}
                  onChange={(date) => handleChange("dateValue", date)}
                  placeholderText="Click to select date"
                  minDate={new Date()}
                />
              </div>
              <p className="state-label">
                Selected: {demoState.dateValue?.toDateString() || "None"}
              </p>
            </div>

            <div className="demo-item">
              <h3>Past Dates Disabled</h3>
              <div className="form-group">
                <label>Future Dates Only</label>
                <CustomDatePicker
                  selected={null}
                  onChange={() => {}}
                  minDate={new Date()}
                />
              </div>
              <p className="state-label">Past dates are disabled</p>
            </div>
          </div>

          <div className="features-info">
            <h4>Features:</h4>
            <ul>
              <li>Custom month/year selectors</li>
              <li>Min/max date restrictions</li>
              <li>Keyboard navigation</li>
              <li>Smooth animations</li>
            </ul>
          </div>
        </section>

        {/* ===== Accessibility Testing ===== */}
        <section className="demo-section">
          <h2>Accessibility Testing Checklist</h2>
          <div className="accessibility-checklist">
            <h3>Keyboard Navigation</h3>
            <ul>
              <li>
                <input type="checkbox" id="tab-nav" />
                <label htmlFor="tab-nav">
                  Tab through all form fields
                </label>
              </li>
              <li>
                <input type="checkbox" id="arrow-nav" />
                <label htmlFor="arrow-nav">
                  Arrow keys navigate dropdown options
                </label>
              </li>
              <li>
                <input type="checkbox" id="enter-nav" />
                <label htmlFor="enter-nav">
                  Enter/Space selects options
                </label>
              </li>
              <li>
                <input type="checkbox" id="esc-nav" />
                <label htmlFor="esc-nav">
                  Escape closes dropdowns
                </label>
              </li>
            </ul>

            <h3>Focus Indicators</h3>
            <ul>
              <li>
                <input type="checkbox" id="focus-visible" />
                <label htmlFor="focus-visible">
                  All inputs have visible focus rings (3px minimum)
                </label>
              </li>
              <li>
                <input type="checkbox" id="contrast" />
                <label htmlFor="contrast">
                  Focus colors have good contrast (WCAG AA)
                </label>
              </li>
            </ul>

            <h3>Screen Reader Support</h3>
            <ul>
              <li>
                <input type="checkbox" id="aria-labels" />
                <label htmlFor="aria-labels">
                  All inputs have associated labels
                </label>
              </li>
              <li>
                <input type="checkbox" id="aria-error" />
                <label htmlFor="aria-error">
                  Error messages linked with aria-describedby
                </label>
              </li>
              <li>
                <input type="checkbox" id="aria-required" />
                <label htmlFor="aria-required">
                  Required fields marked with aria-required
                </label>
              </li>
              <li>
                <input type="checkbox" id="aria-invalid" />
                <label htmlFor="aria-invalid">
                  Error fields marked with aria-invalid
                </label>
              </li>
            </ul>

            <h3>Visual Design</h3>
            <ul>
              <li>
                <input type="checkbox" id="color-contrast" />
                <label htmlFor="color-contrast">
                  Text has 4.5:1 contrast ratio (WCAG AA)
                </label>
              </li>
              <li>
                <input type="checkbox" id="animations" />
                <label htmlFor="animations">
                  Animations respect prefers-reduced-motion
                </label>
              </li>
              <li>
                <input type="checkbox" id="responsive" />
                <label htmlFor="responsive">
                  Forms responsive on mobile devices
                </label>
              </li>
            </ul>
          </div>
        </section>

        {/* ===== Color Palette Reference ===== */}
        <section className="demo-section">
          <h2>Color Palette & Design System</h2>
          <div className="color-grid">
            <div className="color-swatch">
              <div className="swatch primary"></div>
              <p>Primary</p>
              <code>#7c3aed</code>
            </div>
            <div className="color-swatch">
              <div className="swatch accent"></div>
              <p>Accent</p>
              <code>#06b6d4</code>
            </div>
            <div className="color-swatch">
              <div className="swatch success"></div>
              <p>Success</p>
              <code>#10b981</code>
            </div>
            <div className="color-swatch">
              <div className="swatch warning"></div>
              <p>Warning</p>
              <code>#f59e0b</code>
            </div>
            <div className="color-swatch">
              <div className="swatch danger"></div>
              <p>Danger</p>
              <code>#ef4444</code>
            </div>
            <div className="color-swatch">
              <div className="swatch info"></div>
              <p>Info</p>
              <code>#3b82f6</code>
            </div>
          </div>
        </section>
      </div>

      <div className="demo-footer">
        <p>
          💡 <strong>Tip:</strong> This demo page is for testing only. Remove
          from production builds.
        </p>
      </div>
    </div>
  );
};

export default ComponentsDemo;

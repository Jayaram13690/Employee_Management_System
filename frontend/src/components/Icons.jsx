/**
 * Professional SVG icon components for the EMP Management System.
 * All icons are 20×20 by default, using currentColor for easy theming.
 */

const s = {
  width: "1em",
  height: "1em",
  fill: "none",
  stroke: "currentColor",
  strokeWidth: 2,
  strokeLinecap: "round",
  strokeLinejoin: "round",
  verticalAlign: "-0.125em",
};

export const IconDashboard = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <rect x="3" y="3" width="7" height="9" rx="1" />
    <rect x="14" y="3" width="7" height="5" rx="1" />
    <rect x="3" y="16" width="7" height="5" rx="1" />
    <rect x="14" y="12" width="7" height="9" rx="1" />
  </svg>
);

export const IconUsers = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
    <circle cx="9" cy="7" r="4" />
    <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
    <path d="M16 3.13a4 4 0 0 1 0 7.75" />
  </svg>
);

export const IconBuilding = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M3 21h18" />
    <path d="M5 21V7l8-4v18" />
    <path d="M19 21V11l-6-4" />
    <path d="M9 9h1" />
    <path d="M9 13h1" />
    <path d="M9 17h1" />
  </svg>
);

export const IconCalendar = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <rect x="3" y="4" width="18" height="18" rx="2" />
    <line x1="16" y1="2" x2="16" y2="6" />
    <line x1="8" y1="2" x2="8" y2="6" />
    <line x1="3" y1="10" x2="21" y2="10" />
  </svg>
);

export const IconBeach = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M17.5 21H6.5C5.4 21 4.5 20.1 4.5 19V5C4.5 3.9 5.4 3 6.5 3H17.5C18.6 3 19.5 3.9 19.5 5V19C19.5 20.1 18.6 21 17.5 21Z" />
    <line x1="8" y1="7" x2="16" y2="7" />
    <line x1="8" y1="11" x2="16" y2="11" />
    <line x1="8" y1="15" x2="12" y2="15" />
  </svg>
);

export const IconLogout = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
    <polyline points="16 17 21 12 16 7" />
    <line x1="21" y1="12" x2="9" y2="12" />
  </svg>
);

export const IconSearch = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <circle cx="11" cy="11" r="8" />
    <line x1="21" y1="21" x2="16.65" y2="16.65" />
  </svg>
);

export const IconEdit = (props) => (
  <svg
    {...s}
    viewBox="0 0 24 24"
    {...props}
    style={{ ...s, width: "0.85em", height: "0.85em", ...(props?.style || {}) }}
  >
    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
  </svg>
);

export const IconTrash = (props) => (
  <svg
    {...s}
    viewBox="0 0 24 24"
    {...props}
    style={{ ...s, width: "0.85em", height: "0.85em", ...(props?.style || {}) }}
  >
    <polyline points="3 6 5 6 21 6" />
    <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6" />
    <path d="M10 11v6" />
    <path d="M14 11v6" />
    <path d="M9 6V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2" />
  </svg>
);

export const IconPlus = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <line x1="12" y1="5" x2="12" y2="19" />
    <line x1="5" y1="12" x2="19" y2="12" />
  </svg>
);

export const IconCheck = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <polyline points="20 6 9 17 4 12" />
  </svg>
);

export const IconX = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <line x1="18" y1="6" x2="6" y2="18" />
    <line x1="6" y1="6" x2="18" y2="18" />
  </svg>
);

export const IconClock = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <circle cx="12" cy="12" r="10" />
    <polyline points="12 6 12 12 16 14" />
  </svg>
);

export const IconCheckCircle = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
    <polyline points="22 4 12 14.01 9 11.01" />
  </svg>
);

export const IconXCircle = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <circle cx="12" cy="12" r="10" />
    <line x1="15" y1="9" x2="9" y2="15" />
    <line x1="9" y1="9" x2="15" y2="15" />
  </svg>
);

export const IconArrowIn = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <polyline points="15 10 19 6 23 10" />
    <line x1="19" y1="6" x2="19" y2="18" />
    <path d="M3 21h12a2 2 0 0 0 2-2v-3" />
    <path d="M3 3h12a2 2 0 0 1 2 2v3" />
  </svg>
);

export const IconArrowOut = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <polyline points="15 18 19 22 23 18" />
    <line x1="19" y1="22" x2="19" y2="10" />
    <path d="M3 21h12a2 2 0 0 0 2-2v-3" />
    <path d="M3 3h12a2 2 0 0 1 2 2v3" />
  </svg>
);

export const IconList = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <line x1="8" y1="6" x2="21" y2="6" />
    <line x1="8" y1="12" x2="21" y2="12" />
    <line x1="8" y1="18" x2="21" y2="18" />
    <line x1="3" y1="6" x2="3.01" y2="6" />
    <line x1="3" y1="12" x2="3.01" y2="12" />
    <line x1="3" y1="18" x2="3.01" y2="18" />
  </svg>
);

export const IconShield = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
  </svg>
);

export const IconUserCheck = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
    <circle cx="8.5" cy="7" r="4" />
    <polyline points="17 11 19 13 23 9" />
  </svg>
);

export const IconAlertCircle = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <circle cx="12" cy="12" r="10" />
    <line x1="12" y1="8" x2="12" y2="12" />
    <line x1="12" y1="16" x2="12.01" y2="16" />
  </svg>
);

export const IconChevronLeft = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <polyline points="15 18 9 12 15 6" />
  </svg>
);

export const IconChevronRight = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <polyline points="9 18 15 12 9 6" />
  </svg>
);

export const IconSave = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z" />
    <polyline points="17 21 17 13 7 13 7 21" />
    <polyline points="7 3 7 8 15 8" />
  </svg>
);

export const IconCamera = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z" />
    <circle cx="12" cy="13" r="4" />
  </svg>
);

export const IconEye = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
    <circle cx="12" cy="12" r="3" />
  </svg>
);

export const IconEyeOff = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24" />
    <line x1="1" y1="1" x2="23" y2="23" />
  </svg>
);

export const IconChevronDown = (props) => (
  <svg {...s} viewBox="0 0 24 24" {...props}>
    <polyline points="6 9 12 15 18 9" />
  </svg>
);

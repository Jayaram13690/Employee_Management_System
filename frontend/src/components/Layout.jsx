import { NavLink, useNavigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import {
  IconDashboard,
  IconUsers,
  IconBuilding,
  IconCalendar,
  IconBeach,
  IconLogout,
  IconShield,
} from "./Icons";
import "./Layout.css";

const Layout = () => {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const adminLinks = [
    { to: "/dashboard", icon: <IconDashboard />, label: "Dashboard" },
    { to: "/employees", icon: <IconUsers />, label: "Employees" },
    { to: "/departments", icon: <IconBuilding />, label: "Departments" },
    { to: "/attendance-admin", icon: <IconCalendar />, label: "Attendance" },
    { to: "/leave-admin", icon: <IconBeach />, label: "Leave Requests" },
  ];

  const employeeLinks = [
    { to: "/my-dashboard", icon: <IconDashboard />, label: "Dashboard" },
    { to: "/my-attendance", icon: <IconCalendar />, label: "Attendance" },
    { to: "/my-leaves", icon: <IconBeach />, label: "My Leaves" },
  ];

  const links = isAdmin() ? adminLinks : employeeLinks;

  return (
    <div className="app-layout">
      <aside className="sidebar">
        <div className="sidebar-brand">
          <div className="sidebar-logo-icon">
            <IconUsers style={{ width: "1.4em", height: "1.4em" }} />
          </div>
          <div>
            <h2>EMP Manager</h2>
            <span className="sidebar-badge">{user?.role}</span>
          </div>
        </div>

        <nav className="sidebar-nav">
          {links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              className={({ isActive }) =>
                `sidebar-link ${isActive ? "active" : ""}`
              }
            >
              <span className="sidebar-link-icon">{link.icon}</span>
              <span>{link.label}</span>
            </NavLink>
          ))}

          <div className="sidebar-divider" />
          <NavLink
            to="/profile"
            className={({ isActive }) =>
              `sidebar-link ${isActive ? "active" : ""}`
            }
          >
            <span className="sidebar-link-icon">
              <IconShield />
            </span>
            <span>My Profile</span>
          </NavLink>
        </nav>

        <div className="sidebar-footer">
          <div className="sidebar-user">
            <div className="sidebar-avatar">
              {user?.firstName?.[0]}
              {user?.lastName?.[0]}
            </div>
            <div className="sidebar-user-info">
              <span className="sidebar-user-name">
                {user?.firstName} {user?.lastName}
              </span>
              <span className="sidebar-user-email">{user?.email}</span>
            </div>
          </div>
          <button
            className="sidebar-logout"
            onClick={handleLogout}
            title="Logout"
          >
            <IconLogout />
          </button>
        </div>
      </aside>

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
};

export default Layout;

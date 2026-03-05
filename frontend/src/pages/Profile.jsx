import { useState, useRef, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import {
  IconUsers,
  IconShield,
  IconEdit,
  IconSave,
  IconX,
  IconCamera,
  IconBuilding,
} from "../components/Icons";
import PasswordInput from "../components/PasswordInput";
import FormField from "../components/FormField";
import { employeeAPI } from "../services/api";
import { toast } from "react-toastify";
import "./Profile.css";

const Profile = () => {
  const { user, updateUser } = useAuth();

  const [activeTab, setActiveTab] = useState("personal"); // personal, education, employment, security
  const [isEditing, setIsEditing] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Form Data States
  const [formData, setFormData] = useState({
    phone: "",
    bio: "",
    address: "",
    skills: "",
    educationDegree: "",
    educationInstitution: "",
    educationBranch: "",
    educationPassingYear: "",
  });

  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });

  // Sync state with user data on mount or user change
  useEffect(() => {
    const loadProfile = async () => {
      try {
        const response = await employeeAPI.getMyProfile();
        const profileData = response.data.data;
        setFormData({
          phone: profileData.phone || "",
          bio: profileData.bio || "",
          address: profileData.address || "",
          skills: profileData.skills || "",
          educationDegree: profileData.educationDegree || "",
          educationInstitution: profileData.educationInstitution || "",
          educationBranch: profileData.educationBranch || "",
          educationPassingYear: profileData.educationPassingYear || "",
        });
      } catch (err) {
        console.error("Failed to load profile:", err);
      }
    };

    if (user) {
      loadProfile();
    }
  }, [user]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handlePasswordChange = (e) => {
    const { name, value } = e.target;
    setPasswordData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSaveProfile = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      const response = await employeeAPI.updateMyProfile(formData);
      updateUser(response.data.data);
      toast.success("Profile updated successfully!");
      setIsEditing(false);
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to update profile");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSavePassword = async (e) => {
    e.preventDefault();
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      toast.error("New passwords do not match.");
      return;
    }

    setIsSubmitting(true);
    try {
      await employeeAPI.changePassword({
        currentPassword: passwordData.currentPassword,
        newPassword: passwordData.newPassword,
      });
      toast.success("Password changed successfully!");
      setPasswordData({
        currentPassword: "",
        newPassword: "",
        confirmPassword: "",
      });
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to change password");
    } finally {
      setIsSubmitting(false);
    }
  };

  const renderAvatar = () => {
    return (
      <img
        src={`https://api.dicebear.com/7.x/initials/svg?seed=${user?.fullName || "User"}&backgroundColor=7c3aed`}
        alt={user?.fullName}
        className="profile-avatar-img"
      />
    );
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>My Profile</h1>
          <p>View and manage your account information securely.</p>
        </div>
        {activeTab === "security" ? null : !isEditing ? (
          <button
            className="btn btn-primary"
            onClick={() => setIsEditing(true)}
          >
            <IconEdit /> Edit Profile
          </button>
        ) : (
          <div className="flex gap-sm">
            <button
              className="btn btn-secondary"
              onClick={() => setIsEditing(false)}
            >
              <IconX /> Cancel
            </button>
            <button
              className="btn btn-primary"
              onClick={handleSaveProfile}
              disabled={isSubmitting}
            >
              <IconSave /> {isSubmitting ? "Saving..." : "Save Changes"}
            </button>
          </div>
        )}
      </div>

      <div className="profile-layout">
        <div className="profile-sidebar">
          {/* Main Profile Info Card */}
          <div className="profile-card card">
            <div className="profile-header">
              <div className="profile-avatar-wrapper">{renderAvatar()}</div>

              <h2>{user?.fullName}</h2>
              <span
                className={`badge ${
                  user?.role === "ADMIN" ? "badge-primary" : "badge-info"
                }`}
              >
                {user?.role}
              </span>
            </div>

            <div className="profile-nav">
              <button
                className={`profile-nav-btn ${
                  activeTab === "personal" ? "active" : ""
                }`}
                onClick={() => {
                  setActiveTab("personal");
                  setIsEditing(false);
                }}
              >
                Personal Details
              </button>
              <button
                className={`profile-nav-btn ${
                  activeTab === "education" ? "active" : ""
                }`}
                onClick={() => {
                  setActiveTab("education");
                  setIsEditing(false);
                }}
              >
                Education Details
              </button>
              <button
                className={`profile-nav-btn ${
                  activeTab === "employment" ? "active" : ""
                }`}
                onClick={() => {
                  setActiveTab("employment");
                  setIsEditing(false);
                }}
              >
                Employment Details
              </button>
              <button
                className={`profile-nav-btn ${
                  activeTab === "security" ? "active" : ""
                }`}
                onClick={() => {
                  setActiveTab("security");
                  setIsEditing(false);
                }}
              >
                Account Security
              </button>
            </div>
          </div>
        </div>

        {/* Dynamic Detail Panes */}
        <div className="profile-content">
          <div className="card">
            {/* ======= PERSONAL TAB ======= */}
            {activeTab === "personal" && (
              <>
                <h3>Personal Information</h3>
                {isEditing ? (
                  <form className="profile-form" onSubmit={handleSaveProfile}>
                    <div className="form-grid">
                      <div
                        className="form-group"
                        style={{ gridColumn: "1 / -1" }}
                      >
                        <label>Email (Read-only)</label>
                        <input
                          type="text"
                          className="form-input"
                          value={user?.email || ""}
                          disabled
                        />
                      </div>
                      <div
                        className="form-group"
                        style={{ gridColumn: "1 / -1" }}
                      >
                        <label>Phone Number</label>
                        <input
                          type="tel"
                          className="form-input"
                          name="phone"
                          value={formData.phone}
                          onChange={handleInputChange}
                          placeholder="e.g. +1 555-1234"
                        />
                      </div>
                      <div
                        className="form-group"
                        style={{ gridColumn: "1 / -1" }}
                      >
                        <label>Skills</label>
                        <input
                          type="text"
                          className="form-input"
                          name="skills"
                          value={formData.skills}
                          onChange={handleInputChange}
                          placeholder="e.g. Project Management, Java, Node.js"
                        />
                      </div>
                      <div
                        className="form-group"
                        style={{ gridColumn: "1 / -1" }}
                      >
                        <label>Address</label>
                        <textarea
                          className="form-input"
                          name="address"
                          value={formData.address}
                          onChange={handleInputChange}
                          placeholder="Your residential address"
                          rows="2"
                        />
                      </div>
                      <div
                        className="form-group"
                        style={{ gridColumn: "1 / -1" }}
                      >
                        <label>Bio</label>
                        <textarea
                          className="form-input"
                          name="bio"
                          value={formData.bio}
                          onChange={handleInputChange}
                          placeholder="Write a little about yourself"
                          rows="3"
                        />
                      </div>
                    </div>
                  </form>
                ) : (
                  <div className="profile-info-grid">
                    <div className="info-item" style={{ gridColumn: "1 / -1" }}>
                      <span className="info-label">Email Address</span>
                      <span className="info-value">{user?.email}</span>
                    </div>
                    <div className="info-item" style={{ gridColumn: "1 / -1" }}>
                      <span className="info-label">Phone Number</span>
                      <span className="info-value">
                        {user?.phone || (
                          <span className="text-muted">Not provided</span>
                        )}
                      </span>
                    </div>
                    <div className="info-item" style={{ gridColumn: "1 / -1" }}>
                      <span className="info-label">Skills</span>
                      <span className="info-value">
                        {user?.skills || (
                          <span className="text-muted">Not provided</span>
                        )}
                      </span>
                    </div>
                    <div className="info-item" style={{ gridColumn: "1 / -1" }}>
                      <span className="info-label">Address</span>
                      <span className="info-value">
                        {user?.address || (
                          <span className="text-muted">Not provided</span>
                        )}
                      </span>
                    </div>
                    <div className="info-item" style={{ gridColumn: "1 / -1" }}>
                      <span className="info-label">Bio</span>
                      <p className="info-value bio-text">
                        {user?.bio || (
                          <span className="text-muted">Not provided</span>
                        )}
                      </p>
                    </div>
                  </div>
                )}
              </>
            )}

            {/* ======= EDUCATION TAB ======= */}
            {activeTab === "education" && (
              <>
                <h3>Education Background</h3>
                {isEditing ? (
                  <form className="profile-form" onSubmit={handleSaveProfile}>
                    <div className="form-grid">
                      <div className="form-group">
                        <label>Degree Type</label>
                        <input
                          type="text"
                          className="form-input"
                          name="educationDegree"
                          value={formData.educationDegree}
                          onChange={handleInputChange}
                          placeholder="e.g. Bachelor of Science"
                        />
                      </div>
                      <div className="form-group">
                        <label>Institution / University</label>
                        <input
                          type="text"
                          className="form-input"
                          name="educationInstitution"
                          value={formData.educationInstitution}
                          onChange={handleInputChange}
                          placeholder="e.g. Stanford University"
                        />
                      </div>
                      <div className="form-group">
                        <label>Branch / Major</label>
                        <input
                          type="text"
                          className="form-input"
                          name="educationBranch"
                          value={formData.educationBranch}
                          onChange={handleInputChange}
                          placeholder="e.g. Computer Science"
                        />
                      </div>
                      <div className="form-group">
                        <label>Passing Year</label>
                        <input
                          type="text"
                          className="form-input"
                          name="educationPassingYear"
                          value={formData.educationPassingYear}
                          onChange={handleInputChange}
                          placeholder="e.g. 2021"
                        />
                      </div>
                    </div>
                  </form>
                ) : (
                  <div className="profile-info-grid">
                    <div className="info-item">
                      <span className="info-label">Degree Type</span>
                      <span className="info-value">
                        {user?.educationDegree || (
                          <span className="text-muted">Not provided</span>
                        )}
                      </span>
                    </div>
                    <div className="info-item">
                      <span className="info-label">Institution</span>
                      <span className="info-value">
                        {user?.educationInstitution || (
                          <span className="text-muted">Not provided</span>
                        )}
                      </span>
                    </div>
                    <div className="info-item">
                      <span className="info-label">Branch / Major</span>
                      <span className="info-value">
                        {user?.educationBranch || (
                          <span className="text-muted">Not provided</span>
                        )}
                      </span>
                    </div>
                    <div className="info-item">
                      <span className="info-label">Passing Year</span>
                      <span className="info-value">
                        {user?.educationPassingYear || (
                          <span className="text-muted">Not provided</span>
                        )}
                      </span>
                    </div>
                  </div>
                )}
              </>
            )}

            {/* ======= EMPLOYMENT TAB ======= */}
            {activeTab === "employment" && (
              <>
                <h3>Employment Details</h3>
                <div className="profile-message-box">
                  <IconShield style={{ color: "var(--primary)" }} />
                  <p>
                    These fields are managed securely by your administrator.
                  </p>
                </div>
                <div className="profile-info-grid">
                  <div className="info-item">
                    <span className="info-label">Employee ID</span>
                    <span className="info-value">
                      {user?.employeeId || "N/A"}
                    </span>
                  </div>
                  <div className="info-item">
                    <span className="info-label">System Role</span>
                    <span className="info-value">{user?.role}</span>
                  </div>
                  <div className="info-item" style={{ gridColumn: "1 / -1" }}>
                    <span className="info-label">Account Status</span>
                    <span className="info-value">
                      <span className="status-badge status-active">Active</span>
                    </span>
                  </div>
                </div>
              </>
            )}

            {/* ======= SECURITY TAB ======= */}
            {activeTab === "security" && (
              <>
                <h3>Account Security</h3>
                <p
                  className="text-muted"
                  style={{ marginBottom: "var(--space-md)" }}
                >
                  Ensure your account is using a long, random password to stay
                  secure.
                </p>
                <form className="profile-form" onSubmit={handleSavePassword}>
                  <div className="form-grid">
                    <div
                      className="form-group"
                      style={{ gridColumn: "1 / -1" }}
                    >
                      <label>Current Password</label>
                      <PasswordInput
                        id="current-password"
                        value={passwordData.currentPassword}
                        onChange={(e) =>
                          handlePasswordChange({
                            target: {
                              name: "currentPassword",
                              value: e.target.value,
                            },
                          })
                        }
                        placeholder="Enter current password"
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label>New Password</label>
                      <PasswordInput
                        id="new-password"
                        value={passwordData.newPassword}
                        onChange={(e) =>
                          handlePasswordChange({
                            target: {
                              name: "newPassword",
                              value: e.target.value,
                            },
                          })
                        }
                        placeholder="Min 8 chars: uppercase, lowercase, digit, special char"
                        required
                      />
                    </div>
                    <div className="form-group">
                      <label>Confirm Password</label>
                      <PasswordInput
                        id="confirm-password"
                        value={passwordData.confirmPassword}
                        onChange={(e) =>
                          handlePasswordChange({
                            target: {
                              name: "confirmPassword",
                              value: e.target.value,
                            },
                          })
                        }
                        placeholder="Retype new password"
                        required
                      />
                    </div>
                    <div
                      className="form-group flex justify-end"
                      style={{
                        gridColumn: "1 / -1",
                        marginTop: "var(--space-sm)",
                      }}
                    >
                      <button
                        type="submit"
                        className="btn btn-primary"
                        disabled={isSubmitting}
                      >
                        <IconSave />{" "}
                        {isSubmitting ? "Changing..." : "Change Password"}
                      </button>
                    </div>
                  </div>
                </form>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;

import { createContext, useContext, useState, useEffect } from "react";
import { authAPI } from "../services/api";

const AuthContext = createContext(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within AuthProvider");
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Load user from localStorage on mount
  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    const token = localStorage.getItem("token");
    if (storedUser && token) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    const response = await authAPI.login({ email, password });
    const { data } = response.data;

    // Split fullName into firstName and lastName
    const nameParts = data.fullName ? data.fullName.split(" ") : ["", ""];
    const firstName = nameParts[0] || "";
    const lastName = nameParts.slice(1).join(" ") || "";

    const userData = {
      token: data.token,
      email: data.email,
      role: data.role,
      employeeId: data.employeeId,
      fullName: data.fullName,
      firstName: firstName,
      lastName: lastName,
      phone: data.phone,
      profilePhoto: data.profilePhoto,
      bio: data.bio,
      educationDegree: data.educationDegree,
      educationInstitution: data.educationInstitution,
      educationBranch: data.educationBranch,
      educationPassingYear: data.educationPassingYear,
      skills: data.skills,
      address: data.address,
    };

    localStorage.setItem("token", data.token);
    localStorage.setItem("refreshToken", data.refreshToken || data.token);
    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
    return userData;
  };

  const register = async (registerData) => {
    const response = await authAPI.register(registerData);
    return response.data;
  };

  const updateUser = (newUserData) => {
    const updatedUser = { ...user, ...newUserData };
    setUser(updatedUser);
    localStorage.setItem("user", JSON.stringify(updatedUser));
  };

  const logout = async () => {
    try {
      const refreshToken = localStorage.getItem("refreshToken");
      if (refreshToken) {
        await authAPI.logout(refreshToken);
      }
    } catch (err) {
      console.error("Logout API call failed:", err);
    } finally {
      localStorage.removeItem("token");
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("user");
      setUser(null);
    }
  };

  const isAdmin = () => user?.role === "ADMIN";
  const isEmployee = () => user?.role === "EMPLOYEE";
  const isAuthenticated = () => !!user;

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        login,
        register,
        logout,
        updateUser,
        isAdmin,
        isEmployee,
        isAuthenticated,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthContext;

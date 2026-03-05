import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

// Create a separate instance for refresh requests to avoid interceptor loops
const refreshApi = axios.create({
  baseURL: API_BASE_URL,
  headers: { "Content-Type": "application/json" },
});

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { "Content-Type": "application/json" },
});

// Request interceptor — attach JWT token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor — handle 401 with automatic refresh
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then((token) => {
            originalRequest.headers.Authorization = `Bearer ${token}`;
            return api(originalRequest);
          })
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const refreshToken = localStorage.getItem("refreshToken");
        if (!refreshToken) {
          throw new Error("No refresh token available");
        }

        const response = await refreshApi.post("/auth/refresh", {
          refreshToken,
        });

        const newToken = response.data?.data?.token;
        if (newToken) {
          localStorage.setItem("token", newToken);
          api.defaults.headers.common.Authorization = `Bearer ${newToken}`;
          processQueue(null, newToken);
          return api(originalRequest);
        } else {
          throw new Error("No token in refresh response");
        }
      } catch (err) {
        processQueue(err, null);
        localStorage.removeItem("token");
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("user");
        window.location.href = "/login";
        return Promise.reject(err);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  },
);

// ===== Auth API =====
export const authAPI = {
  login: (data) => api.post("/auth/login", data),
  register: (data) => api.post("/auth/register", data),
  refresh: (refreshToken) => api.post("/auth/refresh", { refreshToken }),
  logout: (refreshToken) => api.post("/auth/logout", { refreshToken }),
};

// ===== Employee API =====
export const employeeAPI = {
  getAll: (page = 0, size = 10, sortBy = "id", sortDir = "asc", search = "") =>
    api.get("/employees", {
      params: { page, size, sortBy, sortDir, search: search || undefined },
    }),
  getById: (id) => api.get(`/employees/${id}`),
  create: (data) => api.post("/employees", data),
  update: (id, data) => api.put(`/employees/${id}`, data),
  delete: (id) => api.delete(`/employees/${id}`),
  uploadPhoto: (id, file) => {
    const formData = new FormData();
    formData.append("file", file);
    return api.post(`/employees/${id}/photo`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  },
  getMyProfile: () => api.get("/employees/profile"),
  updateMyProfile: (data) => api.put("/employees/profile", data),
  updateMyPhoto: (avatarUrl) => {
    return api.post(
      `/employees/profile/photo?avatarUrl=${encodeURIComponent(avatarUrl)}`,
    );
  },
  changePassword: (data) => api.put("/employees/profile/password", data),
};

// ===== Department API =====
export const departmentAPI = {
  getAll: () => api.get("/departments"),
  getById: (id) => api.get(`/departments/${id}`),
  create: (data) => api.post("/departments", data),
  update: (id, data) => api.put(`/departments/${id}`, data),
  delete: (id) => api.delete(`/departments/${id}`),
  getEmployees: (id) => api.get(`/departments/${id}/employees`),
};

// ===== Attendance API =====
export const attendanceAPI = {
  checkIn: (employeeId) => api.post(`/attendance/check-in/${employeeId}`),
  checkOut: (employeeId) => api.post(`/attendance/check-out/${employeeId}`),
  getToday: (employeeId) => api.get(`/attendance/today/${employeeId}`),
  getMonthly: (employeeId, month, year) =>
    api.get(`/attendance/monthly/${employeeId}`, { params: { month, year } }),
  getByDateRange: (employeeId, startDate, endDate) =>
    api.get(`/attendance/range/${employeeId}`, {
      params: { startDate, endDate },
    }),
  getByDate: (date) => api.get("/attendance/date", { params: { date } }),
};

// ===== Leave API =====
export const leaveAPI = {
  apply: (employeeId, data) => api.post(`/leaves/apply/${employeeId}`, data),
  approve: (leaveId, data) => api.put(`/leaves/approve/${leaveId}`, data),
  reject: (leaveId, data) => api.put(`/leaves/reject/${leaveId}`, data),
  getByEmployee: (employeeId, page = 0, size = 10) =>
    api.get(`/leaves/employee/${employeeId}`, { params: { page, size } }),
  getPending: (page = 0, size = 10) =>
    api.get("/leaves/pending", { params: { page, size } }),
  getAll: (page = 0, size = 10) =>
    api.get("/leaves", { params: { page, size } }),
  getById: (id) => api.get(`/leaves/${id}`),
  getBalance: (employeeId) => api.get(`/leaves/balance/${employeeId}`),
};

// ===== Dashboard API =====
export const dashboardAPI = {
  getAdmin: () => api.get("/dashboard/admin"),
  getEmployee: (employeeId) => api.get(`/dashboard/employee/${employeeId}`),
};

export default api;

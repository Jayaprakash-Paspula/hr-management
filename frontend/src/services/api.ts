import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { useAuthStore } from '@/context/AuthContext';

// Create axios instance
const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = useAuthStore.getState().token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors
api.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid
      useAuthStore.getState().logout();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;

// Auth API
export const authAPI = {
  login: (credentials: { username: string; password: string }) =>
    api.post('/auth/login', credentials),

  logout: () => {
    useAuthStore.getState().logout();
  },
};

// Employee API
export const employeeAPI = {
  getAll: (params?: any) => api.get('/employees', { params }),
  getById: (id: string) => api.get(`/employees/${id}`),
  create: (data: any) => api.post('/employees', data),
  update: (id: string, data: any) => api.put(`/employees/${id}`, data),
  delete: (id: string) => api.delete(`/employees/${id}`),
  search: (query: string, params?: any) =>
    api.get('/employees/search', { params: { search: query, ...params } }),
};

// Timesheet API
export const timesheetAPI = {
  getByEmployee: (employeeId: string, params?: any) =>
    api.get(`/timesheets/employee/${employeeId}`, { params }),
  getById: (id: string) => api.get(`/timesheets/${id}`),
  create: (data: any) => api.post('/timesheets', data),
  update: (id: string, data: any) => api.put(`/timesheets/${id}`, data),
  submit: (id: string) => api.post(`/timesheets/${id}/submit`),
  getPendingApprovals: (managerId: string, params?: any) =>
    api.get('/timesheets/approvals/pending', { params: { managerId, ...params } }),
};

// Compensation Calculator API
export const compensationAPI = {
  calculate: (data: any) => api.post('/compensation-calculator/calculate', data),
  preview: (data: any) => api.post('/compensation-calculator/preview', data),
};

// Payslip API
export const payslipAPI = {
  generate: (employeeId: string, yearMonth: string) =>
    api.post('/payslips/generate', null, { params: { employeeId, yearMonth } }),
  getById: (id: string) => api.get(`/payslips/${id}`),
  getByEmployee: (employeeId: string, params?: any) =>
    api.get(`/payslips/employee/${employeeId}`, { params }),
  getByMonth: (payrollMonth: string, params?: any) =>
    api.get('/payslips/month/' + payrollMonth, { params }),
};


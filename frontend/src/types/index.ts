/**
 * Type definitions for the application
 */

export type UserRole = 'ADMIN' | 'HR_MANAGER' | 'EMPLOYEE';

export interface User {
  id: string;
  username: string;
  email: string;
  role: UserRole;
}

export interface Employee {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  departmentId: string;
  departmentName?: string;
  designation: string;
  joiningDate: string;
  baseSalary: string;
  hourlyRate?: string;
  overtimeRate?: string;
  managerId?: string;
  managerName?: string;
  active: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface Timesheet {
  id: string;
  employeeId: string;
  employeeName?: string;
  timesheetDate: string;
  hoursWorked: string;
  project?: string;
  taskDescription?: string;
  status: 'DRAFT' | 'SUBMITTED' | 'APPROVED' | 'REJECTED';
  submittedAt?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface Payslip {
  id: string;
  employeeId: string;
  employeeName?: string;
  payrollMonth: string;
  basePay: string;
  overtimePay: string;
  bonus: string;
  totalEarnings: string;
  taxDeduction: string;
  otherDeductions: string;
  totalDeductions: string;
  netPay: string;
  pdfPath?: string;
  createdAt?: string;
  details?: PayslipDetail[];
}

export interface PayslipDetail {
  id: string;
  description: string;
  amount: string;
  category: 'EARNINGS' | 'DEDUCTION';
}

export interface CompensationCalculatorRequest {
  employeeId?: string;
  baseSalary: string;
  hourlyRate: string;
  hoursWorked: string;
  overtimeMultiplier: string;
  bonusPercentage?: string;
  taxPercentage?: string;
  otherDeductions?: string;
}

export interface CompensationCalculatorResponse {
  basePay: string;
  overtimePay: string;
  bonus: string;
  totalEarnings: string;
  taxDeduction: string;
  otherDeductions: string;
  totalDeductions: string;
  netPay: string;
  hoursWorked: string;
  overtimeHours: string;
  regularHours: string;
  hourlyRate: string;
  overtimeRate: string;
}

export interface LoginResponse {
  token: string;
  type: string;
  username: string;
  userId: string;
  role: string;
  email: string;
}

export interface DashboardStats {
  totalEmployees: number;
  totalPayroll: number;
  pendingApprovals: number;
  monthlyExpense: number;
}


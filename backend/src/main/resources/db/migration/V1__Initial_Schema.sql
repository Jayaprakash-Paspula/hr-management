-- Flyway Migration: V1__Initial_Schema.sql
-- Database schema for HR Management System

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Departments table
CREATE TABLE departments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    manager_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

-- Users table (for authentication)
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

-- Employees table
CREATE TABLE employees (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    department_id UUID NOT NULL REFERENCES departments(id),
    designation VARCHAR(50) NOT NULL,
    joining_date DATE NOT NULL,
    base_salary DECIMAL(12, 2) NOT NULL,
    hourly_rate DECIMAL(10, 2),
    overtime_rate DECIMAL(10, 2), -- Multiplier (e.g., 1.5x)
    manager_id UUID REFERENCES employees(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    CONSTRAINT salary_positive CHECK (base_salary > 0),
    CONSTRAINT hourly_rate_positive CHECK (hourly_rate IS NULL OR hourly_rate > 0)
);

-- Add foreign key for department manager
ALTER TABLE departments ADD FOREIGN KEY (manager_id) REFERENCES employees(id);

-- Timesheets table
CREATE TABLE timesheets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    employee_id UUID NOT NULL REFERENCES employees(id),
    timesheet_date DATE NOT NULL,
    hours_worked DECIMAL(5, 2) NOT NULL,
    project VARCHAR(100),
    task_description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT', -- DRAFT, SUBMITTED, APPROVED, REJECTED
    submitted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT hours_valid CHECK (hours_worked > 0 AND hours_worked <= 24),
    UNIQUE(employee_id, timesheet_date)
);

-- Timesheet Approvals table
CREATE TABLE timesheet_approvals (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    timesheet_id UUID NOT NULL REFERENCES timesheets(id),
    approved_by UUID NOT NULL REFERENCES employees(id),
    approval_status VARCHAR(20) NOT NULL, -- PENDING, APPROVED, REJECTED
    comments TEXT,
    approved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Compensation Policies table
CREATE TABLE compensation_policies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    overtime_multiplier DECIMAL(3, 2) NOT NULL DEFAULT 1.5,
    max_hours_per_day DECIMAL(5, 2) NOT NULL DEFAULT 8,
    max_hours_per_week DECIMAL(5, 2) NOT NULL DEFAULT 40,
    bonus_percentage DECIMAL(5, 2) DEFAULT 0, -- Annual bonus as % of base salary
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

-- Payslips table
CREATE TABLE payslips (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    employee_id UUID NOT NULL REFERENCES employees(id),
    payroll_month DATE NOT NULL,
    base_pay DECIMAL(12, 2) NOT NULL,
    overtime_pay DECIMAL(12, 2) DEFAULT 0,
    bonus DECIMAL(12, 2) DEFAULT 0,
    total_earnings DECIMAL(12, 2) NOT NULL,
    tax_deduction DECIMAL(12, 2) DEFAULT 0,
    other_deductions DECIMAL(12, 2) DEFAULT 0,
    total_deductions DECIMAL(12, 2) NOT NULL,
    net_pay DECIMAL(12, 2) NOT NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pdf_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(employee_id, payroll_month)
);

-- Payslip Details table (for breakdown)
CREATE TABLE payslip_details (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    payslip_id UUID NOT NULL REFERENCES payslips(id) ON DELETE CASCADE,
    description VARCHAR(100) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    category VARCHAR(50) NOT NULL, -- EARNINGS, DEDUCTION
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for optimization
CREATE INDEX idx_employees_department_id ON employees(department_id);
CREATE INDEX idx_employees_manager_id ON employees(manager_id);
CREATE INDEX idx_employees_user_id ON employees(user_id);
CREATE INDEX idx_timesheets_employee_id ON timesheets(employee_id);
CREATE INDEX idx_timesheets_date ON timesheets(timesheet_date);
CREATE INDEX idx_timesheet_approvals_employee_id ON timesheet_approvals(approved_by);
CREATE INDEX idx_payslips_employee_id ON payslips(employee_id);
CREATE INDEX idx_payslips_month ON payslips(payroll_month);
CREATE INDEX idx_payslip_details_payslip_id ON payslip_details(payslip_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);


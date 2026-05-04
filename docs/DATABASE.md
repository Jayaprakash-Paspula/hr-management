# Database Schema Documentation

## Overview

The database uses PostgreSQL and is organized into logical tables representing the HR management domain.

## Tables

### users
Stores authentication information for all system users.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| username | VARCHAR(50) | NOT NULL, UNIQUE | Login username |
| email | VARCHAR(100) | NOT NULL, UNIQUE | User email |
| password_hash | VARCHAR(255) | NOT NULL | Bcrypt hashed password |
| role | VARCHAR(50) | NOT NULL | ADMIN, HR_MANAGER, EMPLOYEE |
| active | BOOLEAN | NOT NULL, DEFAULT true | Account active status |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | Last update timestamp |

### departments
Stores department information and hierarchy.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| name | VARCHAR(100) | NOT NULL, UNIQUE | Department name |
| description | TEXT | | Department description |
| manager_id | UUID | FOREIGN KEY (employees) | Department manager |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| active | BOOLEAN | NOT NULL, DEFAULT true | Department active status |

### employees
Stores employee profile and compensation information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| user_id | UUID | NOT NULL, UNIQUE, FK | Reference to user |
| first_name | VARCHAR(50) | NOT NULL | First name |
| last_name | VARCHAR(50) | NOT NULL | Last name |
| email | VARCHAR(100) | NOT NULL, UNIQUE | Employee email |
| phone | VARCHAR(20) | | Phone number |
| department_id | UUID | NOT NULL, FK | Reference to department |
| designation | VARCHAR(50) | NOT NULL | Job title |
| joining_date | DATE | NOT NULL | Employment start date |
| base_salary | DECIMAL(12,2) | NOT NULL | Monthly base salary |
| hourly_rate | DECIMAL(10,2) | | Hourly pay rate |
| overtime_rate | DECIMAL(10,2) | | Overtime multiplier (e.g., 1.5) |
| manager_id | UUID | FK | Direct manager |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| active | BOOLEAN | NOT NULL, DEFAULT true | Employment status |

**Indexes:**
- department_id, manager_id, user_id, email relationships

### timesheets
Stores daily timesheet entries for employees.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| employee_id | UUID | NOT NULL, FK | Reference to employee |
| timesheet_date | DATE | NOT NULL | Date of work |
| hours_worked | DECIMAL(5,2) | NOT NULL, CHECK >= 0 AND <= 24 | Hours worked on date |
| project | VARCHAR(100) | | Project name |
| task_description | TEXT | | Description of tasks |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'DRAFT' | DRAFT, SUBMITTED, APPROVED, REJECTED |
| submitted_at | TIMESTAMP | | Submission timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Unique Constraint:** (employee_id, timesheet_date)
**Indexes:** employee_id, timesheet_date

### timesheet_approvals
Stores approval workflow for timesheets.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| timesheet_id | UUID | NOT NULL, FK | Reference to timesheet |
| approved_by | UUID | NOT NULL, FK | Approving manager |
| approval_status | VARCHAR(20) | NOT NULL | PENDING, APPROVED, REJECTED |
| comments | TEXT | | Approval comments |
| approved_at | TIMESTAMP | | Approval timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

### compensation_policies
Stores configurable compensation rules and policies.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| name | VARCHAR(100) | NOT NULL, UNIQUE | Policy name |
| description | TEXT | | Policy description |
| overtime_multiplier | DECIMAL(3,2) | NOT NULL, DEFAULT 1.5 | Overtime pay multiplier |
| max_hours_per_day | DECIMAL(5,2) | NOT NULL, DEFAULT 8 | Daily hour limit |
| max_hours_per_week | DECIMAL(5,2) | NOT NULL, DEFAULT 40 | Weekly hour limit |
| bonus_percentage | DECIMAL(5,2) | DEFAULT 0 | Annual bonus % of salary |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| active | BOOLEAN | NOT NULL, DEFAULT true | Policy active status |

### payslips
Stores generated payslips for employees.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| employee_id | UUID | NOT NULL, FK | Reference to employee |
| payroll_month | DATE | NOT NULL | Month (1st of month) |
| base_pay | DECIMAL(12,2) | NOT NULL | Base salary for month |
| overtime_pay | DECIMAL(12,2) | DEFAULT 0 | Overtime pay |
| bonus | DECIMAL(12,2) | DEFAULT 0 | Bonus amount |
| total_earnings | DECIMAL(12,2) | NOT NULL | Total gross pay |
| tax_deduction | DECIMAL(12,2) | DEFAULT 0 | Tax amount |
| other_deductions | DECIMAL(12,2) | DEFAULT 0 | Other deductions |
| total_deductions | DECIMAL(12,2) | NOT NULL | Total deductions |
| net_pay | DECIMAL(12,2) | NOT NULL | Net pay (gross - deductions) |
| pdf_path | VARCHAR(255) | | Path to generated PDF |
| generated_at | TIMESTAMP | DEFAULT NOW() | Generation timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |

**Unique Constraint:** (employee_id, payroll_month)
**Indexes:** employee_id, payroll_month

### payslip_details
Stores breakdown details for payslips (earnings and deductions).

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| payslip_id | UUID | NOT NULL, FK | Reference to payslip |
| description | VARCHAR(100) | NOT NULL | Description (e.g., "Base Salary") |
| amount | DECIMAL(12,2) | NOT NULL | Amount |
| category | VARCHAR(50) | NOT NULL | EARNINGS or DEDUCTION |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |

## Relationships

```
users (1) ──→ (1) employees
         ├─ Set Authentication

departments (1) ──→ (many) employees
          ├─ Organize by department

employees (1) ──→ (1) employees
      (as manager) ├─ Manager relationship

employees (1) ──→ (many) timesheets
         ├─ Employee timesheets

timesheets (1) ──→ (many) timesheet_approvals
         ├─ Approval workflow

employees (1) ──→ (many) payslips
         ├─ Employee payslips

payslips (1) ──→ (many) payslip_details
     ├─ Payslip breakdown

compensation_policies (config) ──→ (used by) CompensationCalculatorService
                     ├─ Policy rules
```

## Key Constraints & Validations

### Data Integrity
- **Foreign Key Constraints**: Prevent orphaned records
- **Unique Constraints**: Prevent duplicate usernames, emails, dates
- **Check Constraints**: Validate numeric ranges
  - hoursWorked: 0 < hours ≤ 24
  - salary/rates: Must be positive

### Business Rules
- One user per employee (unique user_id)
- One timesheet entry per employee per date (unique composite)
- One payslip per employee per month (unique composite)
- Soft deletes: `active` column used instead of hard deletion

## Audit Columns

All tables include audit timestamps:
- `created_at`: Immutable, set at creation
- `updated_at`: Updated on every modification

## Indexing Strategy

Indexes are created on:
1. **Foreign Keys**: For JOIN performance
2. **Search Fields**: username, email, name
3. **Date Fields**: timesheet_date, payroll_month
4. **Status Fields**: For filtering

## Sample Queries

### Get Employee with Department
```sql
SELECT e.*, d.name as department
FROM employees e
JOIN departments d ON e.department_id = d.id
WHERE e.id = ?;
```

### Get Monthly Payroll Summary
```sql
SELECT 
    COUNT(DISTINCT employee_id) as total_employees,
    SUM(net_pay) as total_payroll,
    AVG(net_pay) as avg_salary
FROM payslips
WHERE payroll_month = ? AND active = true;
```

### Get Pending Timesheet Approvals
```sql
SELECT t.*, e.first_name, e.last_name
FROM timesheets t
JOIN employees e ON t.employee_id = e.id
WHERE t.status = 'SUBMITTED'
  AND e.manager_id = ?
ORDER BY t.submitted_at DESC;
```

### Calculate Overtime Hours
```sql
SELECT 
    employee_id,
    SUM(hours_worked) - 160 as overtime_hours
FROM timesheets
WHERE EXTRACT(YEAR_MONTH FROM timesheet_date) = ?
GROUP BY employee_id;
```

## Backup & Recovery

Regular backups are recommended:
```bash
# Backup
pg_dump hrms_db > hrms_backup.sql

# Restore
psql hrms_db < hrms_backup.sql
```

## Performance Considerations

1. **Connection Pooling**: Use HikariCP (default in Spring Boot)
2. **Query Optimization**: Use explain plan for slow queries
3. **Pagination**: Always paginate large result sets
4. **Batch Operations**: Use batch inserts for bulk operations
5. **Archiving**: Archive old payslips periodically

## Migration Strategy

Database migrations are managed by **Flyway**:
- Location: `src/main/resources/db/migration/`
- Naming: `V{number}__{description}.sql`
- Applied automatically on startup

To create new migration:
```sql
-- V2__Add_new_column.sql
ALTER TABLE employees ADD COLUMN middle_name VARCHAR(50);
```


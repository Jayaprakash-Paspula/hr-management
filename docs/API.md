# API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication
All protected endpoints require Bearer token in Authorization header:
```
Authorization: Bearer <token>
```

## Public Endpoints

### Authentication

#### Login
```
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "admin",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "role": "ADMIN",
  "email": "admin@example.com"
}
```

#### Health Check
```
GET /auth/health

Response: OK
```

## Protected Endpoints

### Employee Management

#### Get All Employees
```
GET /employees?page=0&size=10
Role: EMPLOYEE, HR_MANAGER, ADMIN

Response:
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john@example.com",
      "designation": "Senior Developer",
      "departmentName": "Engineering",
      "baseSalary": "75000.00",
      "active": true
    }
  ],
  "totalElements": 124,
  "totalPages": 13,
  "currentPage": 0
}
```

#### Get Employee by ID
```
GET /employees/{employeeId}
Role: EMPLOYEE, HR_MANAGER, ADMIN

Response: Employee object
```

#### Create Employee
```
POST /employees
Role: HR_MANAGER, ADMIN
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane@example.com",
  "departmentId": "550e8400-e29b-41d4-a716-446655440000",
  "designation": "HR Specialist",
  "joiningDate": "2024-01-15",
  "baseSalary": "55000.00",
  "hourlyRate": "30.00",
  "overtimeRate": "1.5"
}
```

#### Update Employee
```
PUT /employees/{employeeId}
Role: HR_MANAGER, ADMIN
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "designation": "Senior HR Specialist",
  "baseSalary": "65000.00"
}
```

#### Delete Employee (Soft Delete)
```
DELETE /employees/{employeeId}
Role: ADMIN
```

#### Search Employees
```
GET /employees/search?search=john&page=0&size=10
Role: EMPLOYEE, HR_MANAGER, ADMIN

Response: List of matching employees
```

#### Get Employees by Department
```
GET /employees/department/{departmentId}?page=0&size=10
Role: HR_MANAGER, ADMIN
```

#### Get Direct Reports
```
GET /employees/manager/{managerId}/reports?page=0&size=10
Role: HR_MANAGER, ADMIN
```

### Timesheet Management

#### Get Timesheets by Employee
```
GET /timesheets/employee/{employeeId}?page=0&size=10
Role: EMPLOYEE, HR_MANAGER, ADMIN
```

#### Get Timesheet by ID
```
GET /timesheets/{timesheetId}
Role: EMPLOYEE, HR_MANAGER, ADMIN
```

#### Create Timesheet Entry
```
POST /timesheets
Role: EMPLOYEE, HR_MANAGER, ADMIN
Content-Type: application/json

{
  "employeeId": "550e8400-e29b-41d4-a716-446655440000",
  "timesheetDate": "2024-01-15",
  "hoursWorked": "8.0",
  "project": "Project Alpha",
  "taskDescription": "Implemented user authentication"
}
```

#### Update Timesheet
```
PUT /timesheets/{timesheetId}
Role: EMPLOYEE, HR_MANAGER, ADMIN

{
  "hoursWorked": "8.5",
  "project": "Project Alpha",
  "taskDescription": "Updated description"
}
```

#### Submit Timesheet
```
POST /timesheets/{timesheetId}/submit
Role: EMPLOYEE, HR_MANAGER, ADMIN
```

#### Get Pending Approvals
```
GET /timesheets/approvals/pending?managerId={managerId}&page=0&size=10
Role: HR_MANAGER, ADMIN
```

#### Get Timesheets by Date Range
```
GET /timesheets/employee/{employeeId}/date-range?fromDate=2024-01-01&toDate=2024-01-31&page=0&size=10
Role: EMPLOYEE, HR_MANAGER, ADMIN
```

### Compensation Calculator (CRC)

#### Calculate Compensation
```
POST /compensation-calculator/calculate
Role: HR_MANAGER, ADMIN
Content-Type: application/json

{
  "baseSalary": "50000.00",
  "hourlyRate": "25.00",
  "hoursWorked": "160.0",
  "overtimeMultiplier": "1.5",
  "bonusPercentage": "5.0",
  "taxPercentage": "10.0",
  "otherDeductions": "0.00"
}

Response:
{
  "basePay": "4000.00",
  "overtimePay": "0.00",
  "bonus": "2500.00",
  "totalEarnings": "6500.00",
  "taxDeduction": "650.00",
  "otherDeductions": "0.00",
  "totalDeductions": "650.00",
  "netPay": "5850.00",
  "hoursWorked": "160.0",
  "overtimeHours": "120.0",
  "regularHours": "40.0",
  "hourlyRate": "25.00",
  "overtimeRate": "37.50"
}
```

#### Preview Compensation
```
POST /compensation-calculator/preview
Role: HR_MANAGER, ADMIN

Same as calculate endpoint - used for real-time preview
```

### Payslip Management

#### Generate Payslip
```
POST /payslips/generate?employeeId={employeeId}&yearMonth=2024-01
Role: HR_MANAGER, ADMIN
```

#### Get Payslip by ID
```
GET /payslips/{payslipId}
Role: HR_MANAGER, ADMIN, EMPLOYEE
```

#### Get Payslips by Employee
```
GET /payslips/employee/{employeeId}?page=0&size=10
Role: HR_MANAGER, ADMIN, EMPLOYEE
```

#### Get Payslips by Month
```
GET /payslips/month/{payrollMonth}?page=0&size=10
Role: HR_MANAGER, ADMIN

Example: /payslips/month/2024-01-01
```

## Error Responses

All error responses follow this format:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Detailed error message",
  "path": "/api/v1/endpoint"
}
```

Common HTTP Status Codes:
- 200: Success
- 201: Created
- 204: No Content
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error

## Pagination

All list endpoints support pagination:
- `page`: Zero-indexed page number (default: 0)
- `size`: Number of records per page (default: 10)

Response includes:
```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 10,
  "currentPage": 0,
  "pageSize": 10
}
```

## Sample Test Data

### Users (for login)
- Admin: username=`admin`, password=`admin123`
- HR Manager: username=`hr`, password=`hr123`
- Employee: username=`emp`, password=`emp123`

### Employee Example
- Name: John Doe
- Email: john.doe@example.com
- Department: Engineering
- Designation: Senior Developer
- Base Salary: $75,000
- Hourly Rate: $40
- Overtime Rate: 1.5x

## Rate Limiting
Currently no rate limiting is enforced, but this should be implemented for production.

## CORS
CORS is configured to allow requests from:
- http://localhost:3000
- http://localhost:5173
- http://localhost:4200


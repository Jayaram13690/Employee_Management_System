# Employee Management System - API Endpoints

Base URL: `http://localhost:8080/api`

## Authentication Endpoints

### Register Admin
**POST** `/auth/register`

### Login
**POST** `/auth/login`

### Refresh Token
**POST** `/auth/refresh`

### Logout
**POST** `/auth/logout`

## Employee Endpoints

### List Employees
**GET** `/employees?page=0&size=10&sortBy=id&sortDir=asc`

### Get Employee
**GET** `/employees/{id}`

### Create Employee
**POST** `/employees`

### Update Employee
**PUT** `/employees/{id}`

### Delete Employee
**DELETE** `/employees/{id}`

### Get My Profile
**GET** `/employees/profile`

### Update My Profile
**PUT** `/employees/profile`

### Upload Profile Photo
**POST** `/employees/{id}/photo`

## Leave Endpoints

### Apply for Leave
**POST** `/leaves/apply/{employeeId}`

### Get Employee Leaves
**GET** `/leaves/employee/{employeeId}?page=0&size=10`

### Get Pending Leaves
**GET** `/leaves/pending?page=0&size=10`

### Get All Leaves
**GET** `/leaves?page=0&size=10`

### Approve Leave
**PUT** `/leaves/approve/{leaveId}`

### Reject Leave
**PUT** `/leaves/reject/{leaveId}`

### Get Leave Balance
**GET** `/leaves/balance/{employeeId}`

## Attendance Endpoints

### Check In
**POST** `/attendance/check-in/{employeeId}`

### Check Out
**POST** `/attendance/check-out/{employeeId}`

### Get Today Attendance
**GET** `/attendance/today/{employeeId}`

### Get Monthly Attendance
**GET** `/attendance/monthly/{employeeId}?month=3&year=2024`

### Get Attendance by Date Range
**GET** `/attendance/range/{employeeId}?startDate=2024-03-01&endDate=2024-03-31`

### Get Attendance by Date
**GET** `/attendance/date?date=2024-03-13`

## Department Endpoints

### List Departments
**GET** `/departments`

### Get Department
**GET** `/departments/{id}`

### Create Department
**POST** `/departments`

### Update Department
**PUT** `/departments/{id}`

### Delete Department
**DELETE** `/departments/{id}`

### Get Department Employees
**GET** `/departments/{id}/employees`

## Dashboard Endpoints

### Admin Dashboard
**GET** `/dashboard/admin`

### Employee Dashboard
**GET** `/dashboard/employee/{employeeId}`

## Common Query Parameters

- `page`: Page number (0-indexed)
- `size`: Items per page (default 10)
- `sortBy`: Field to sort by
- `sortDir`: Sort direction (asc/desc)

## Authentication

All endpoints except `/auth/register` and `/auth/login` require:

```
Authorization: Bearer {token}
```

## Response Format

### Success (200, 201)
```json
{
  "data": {...},
  "message": "Success message"
}
```

### Error (400, 401, 403, 404, 500)
```json
{
  "timestamp": "2024-03-13T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Error message",
  "fieldErrors": {}
}
```

## Status Codes

- `200` OK - Request successful
- `201` Created - Resource created
- `400` Bad Request - Invalid input
- `401` Unauthorized - Invalid/expired token
- `403` Forbidden - Access denied
- `404` Not Found - Resource not found
- `500` Server Error - Internal error

# Employee Management System - Architecture

## System Design

### Three-Tier Architecture

```
Frontend (React + Vite)
    ↓
API Gateway (Spring Boot)
    ↓
Database (MySQL)
```

## Backend Architecture

### Spring Boot 3.2.3

**Configuration Layer**
- SecurityConfig: JWT authentication and authorization
- JacksonConfig: JSON serialization/deserialization
- JwtService: Token generation and validation
- JwtAuthenticationFilter: Request authentication
- OpenApiConfig: Swagger/OpenAPI documentation
- WebConfig, CorsConfig, WebMvcConfig: Web configuration
- EnvConfig: Environment variable loading

**Controller Layer**
- AuthController: Authentication endpoints
- EmployeeController: Employee CRUD
- DepartmentController: Department management
- LeaveController: Leave workflow
- AttendanceController: Attendance tracking
- DashboardController: Dashboard metrics
- LeaveBalanceController: Leave balance queries

**Service Layer**
- AuthService: Authentication logic
- EmployeeService: Employee operations
- DepartmentService: Department operations
- LeaveService: Leave workflow
- LeaveBalanceService: Balance management
- AttendanceService: Attendance operations
- DashboardService: Dashboard data aggregation
- EmailService: Email notifications

**Repository Layer**
- UserRepository: User queries
- EmployeeRepository: Employee queries
- DepartmentRepository: Department queries
- LeaveRequestRepository: Leave queries
- LeaveBalanceRepository: Balance queries
- AttendanceRepository: Attendance queries
- RefreshTokenRepository: Token queries

**Entity Layer**
- User: Authentication user
- Employee: Employee profile
- Department: Organization unit
- LeaveRequest: Leave application
- LeaveBalance: Leave quota
- Attendance: Daily records
- RefreshToken: Token management

**DTO Layer**
- Request DTOs: Input validation and mapping
- Response DTOs: Output serialization

**Validation Layer**
- Custom annotations: Password strength
- Jakarta Bean Validation: Field constraints
- Input validation on all endpoints

**Exception Handling**
- GlobalExceptionHandler: Centralized error handling
- Custom exceptions: ResourceNotFoundException, BadRequestException

**Utilities**
- SortFieldValidator: SQL injection prevention
- Password validation: Strong password enforcement

## Frontend Architecture

### React + Vite

**Context API**
- AuthContext: Global authentication state
- User management and token storage
- Role-based access control

**Service Layer**
- api.js: Axios configuration
- API endpoint organization
- Token refresh interceptor
- Error handling

**Components**
- Reusable UI components
- Icons, PasswordInput, ProtectedRoute
- Form components with validation
- Layout components

**Pages**
- Login: User authentication
- AdminDashboard: Admin overview
- Employees: Employee management
- Departments: Department management
- LeaveAdmin: Leave approval workflow
- MyLeaves: Employee leave requests
- AttendanceAdmin: Attendance reports
- MyAttendance: Employee attendance
- Profile: User profile management
- Register: Admin registration

**Routing**
- React Router v6
- Protected routes for authentication
- Role-based route access
- Nested routes for modules

## Database Schema

### Tables (7 total)

1. **users** (Authentication)
   - Email (unique), password hash
   - Role (ADMIN/EMPLOYEE)
   - Active flag

2. **employees** (Employee profiles)
   - User reference (1:1)
   - Personal details (name, phone, address)
   - Professional info (designation, salary, DOJ)
   - Department reference (M:1)
   - Education and skills

3. **departments** (Organization structure)
   - Name (unique)
   - Description

4. **leave_requests** (Leave workflow)
   - Employee reference (M:1)
   - Type, dates, reason
   - Status (PENDING/APPROVED/REJECTED)
   - Approval comment

5. **leave_balance** (Leave quotas)
   - Employee reference (M:1)
   - Leave type, year
   - Used and remaining balance

6. **attendance** (Daily records)
   - Employee reference (M:1)
   - Date, status
   - Check-in and check-out times
   - Unique constraint on (employee, date)

7. **refresh_tokens** (Token management)
   - User reference (M:1)
   - Token value
   - Expiry date
   - For token revocation

### Indexes (18 total)

- Performance optimization for common queries
- Composite indexes for multi-column filters
- Expiry-date index for token cleanup

## Security Features

1. **Authentication**
   - JWT tokens with claims
   - Refresh token mechanism
   - Token storage in database

2. **Authorization**
   - Role-based access control (RBAC)
   - Method-level security
   - Route protection in frontend

3. **Data Protection**
   - Password hashing (bcrypt)
   - Environment variable configuration
   - No hardcoded secrets

4. **Input Validation**
   - Custom validators
   - Jakarta Bean Validation
   - Strong password requirements
   - Phone and date validation

5. **SQL Injection Prevention**
   - Parameterized queries (JPA)
   - Sort field whitelisting
   - Input sanitization

6. **API Security**
   - CORS configuration
   - Security headers (HSTS, X-Frame-Options)
   - Token-based request authentication

## Deployment Architecture

### Local Development
- Backend: Spring Boot application
- Frontend: Vite dev server
- Database: MySQL instance
- Environment variables in .env

### Production
- Backend: Docker container or standalone
- Frontend: Static files served by web server
- Database: Managed MySQL service
- Environment variables in CI/CD platform

## Data Flow

### Authentication Flow
1. User submits credentials
2. Backend validates and generates JWT tokens
3. Tokens stored in database and local storage
4. Subsequent requests include token
5. Token refresh on expiration

### Leave Request Flow
1. Employee applies for leave
2. Request saved with PENDING status
3. Admin reviews and approves/rejects
4. Email notification sent
5. Leave balance updated on approval

### Attendance Flow
1. Employee checks in/out
2. Record created in database
3. Status determined automatically
4. Monthly reports aggregated
5. Statistics calculated for dashboard


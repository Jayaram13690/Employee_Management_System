# Database Schema Documentation

## Overview

The Employee Management System uses MySQL 8.0+ with 7 main tables, 18 indexes, and proper relationships.

## Tables

### 1. users

User credentials and authentication.

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'EMPLOYEE') DEFAULT 'EMPLOYEE',
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_email (email),
    INDEX idx_active (active)
);
```

**Columns:**
- `id`: Primary key, auto-increment
- `email`: Unique email for login
- `password`: Bcrypt hashed password
- `role`: ADMIN or EMPLOYEE
- `active`: Whether account is active
- `created_at`: Account creation timestamp
- `updated_at`: Last modification timestamp

**Indexes:**
- `idx_email`: Fast login lookups
- `idx_active`: Filter active users

### 2. employees

Employee profile information.

```sql
CREATE TABLE employees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(15) UNIQUE,
    designation VARCHAR(100),
    date_of_joining DATE,
    salary DOUBLE,
    profile_photo VARCHAR(255),
    bio TEXT,
    education_degree VARCHAR(100),
    education_institution VARCHAR(200),
    education_branch VARCHAR(100),
    education_passing_year VARCHAR(4),
    skills TEXT,
    address TEXT,
    department_id BIGINT,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    INDEX idx_user_id (user_id),
    INDEX idx_department_id (department_id),
    INDEX idx_phone (phone),
    INDEX idx_active (active)
);
```

**Columns:**
- `user_id`: Reference to user account (1:1)
- `first_name`, `last_name`: Employee name
- `phone`: Contact number (unique)
- `designation`: Job title
- `date_of_joining`: Start date
- `salary`: Annual salary
- `profile_photo`: Profile picture path
- `bio`: Employee biography
- Education fields: Degree, institution, branch, year
- `skills`: Employee skills
- `address`: Physical address
- `department_id`: Department assignment (M:1)
- `active`: Whether employed

**Indexes:**
- `idx_user_id`: Fast user lookup
- `idx_department_id`: Department filtering
- `idx_phone`: Phone number lookup
- `idx_active`: Active employee filtering

### 3. departments

Organization departments.

```sql
CREATE TABLE departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_name (name)
);
```

**Columns:**
- `name`: Department name (unique)
- `description`: Department details

**Indexes:**
- `idx_name`: Fast department lookup

### 4. leave_requests

Leave application workflow.

```sql
CREATE TABLE leave_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    leave_type ENUM('CASUAL', 'EARNED', 'SICK', 'MATERNITY', 'OTHER'),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    reason TEXT,
    approval_comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (employee_id) REFERENCES employees(id),
    INDEX idx_employee_id (employee_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);
```

**Columns:**
- `employee_id`: Applying employee (M:1)
- `leave_type`: Type of leave
- `start_date`, `end_date`: Leave period
- `status`: PENDING/APPROVED/REJECTED
- `reason`: Why taking leave
- `approval_comment`: Approval/rejection reason

**Indexes:**
- `idx_employee_id`: Employee's leaves
- `idx_status`: Pending leaves
- `idx_created_at`: Recent requests

### 5. leave_balance

Leave quota tracking.

```sql
CREATE TABLE leave_balance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    leave_type ENUM('CASUAL', 'EARNED', 'SICK', 'MATERNITY', 'OTHER'),
    year INT NOT NULL,
    total_days INT DEFAULT 0,
    used_days INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (employee_id) REFERENCES employees(id),
    UNIQUE KEY uk_employee_leave_year (employee_id, leave_type, year),
    INDEX idx_employee_id (employee_id),
    INDEX idx_employee_leave_year (employee_id, year)
);
```

**Columns:**
- `employee_id`: Employee (M:1)
- `leave_type`: Type of leave
- `year`: Balance year
- `total_days`: Annual allocation
- `used_days`: Days used

**Constraints:**
- Unique combination of (employee, type, year)

**Indexes:**
- `idx_employee_id`: Employee's balances
- `idx_employee_leave_year`: Balance lookup

### 6. attendance

Daily attendance records.

```sql
CREATE TABLE attendance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    date DATE NOT NULL,
    check_in_time DATETIME,
    check_out_time DATETIME,
    status ENUM('PRESENT', 'ABSENT', 'HALF_DAY') DEFAULT 'ABSENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (employee_id) REFERENCES employees(id),
    UNIQUE KEY uk_employee_date (employee_id, date),
    INDEX idx_employee_id_date (employee_id, date),
    INDEX idx_date (date),
    INDEX idx_status (status)
);
```

**Columns:**
- `employee_id`: Employee (M:1)
- `date`: Attendance date
- `check_in_time`: Check-in timestamp
- `check_out_time`: Check-out timestamp
- `status`: PRESENT/ABSENT/HALF_DAY

**Constraints:**
- One record per employee per day

**Indexes:**
- `idx_employee_id_date`: Employee's attendance
- `idx_date`: Daily reports
- `idx_status`: Status filtering

### 7. refresh_tokens

Token management and revocation.

```sql
CREATE TABLE refresh_tokens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token TEXT NOT NULL,
    expiry_date DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY uk_token (token(100)),
    INDEX idx_user_id (user_id),
    INDEX idx_expiry_date (expiry_date)
);
```

**Columns:**
- `user_id`: Token owner (M:1)
- `token`: JWT refresh token
- `expiry_date`: Token expiration

**Indexes:**
- `idx_user_id`: User's tokens
- `idx_expiry_date`: Token cleanup

## Relationships

```
users (1) ──────── (1) employees
                    ├── (M:1) departments
                    ├── (1:M) leave_requests
                    ├── (1:M) leave_balance
                    └── (1:M) attendance

users (1) ──────── (1:M) refresh_tokens
```

## Default Values

### Leave Allocations
- Casual: 12 days/year
- Earned: 20 days/year
- Sick: 10 days/year
- Maternity: 90 days
- Other: 0 days

## Performance Indexes

Total: **18 indexes**

### Authentication (3)
- `users.idx_email` - Login queries
- `users.idx_active` - Active user filtering
- `refresh_tokens.idx_user_id` - Token lookup

### Employee Operations (4)
- `employees.idx_user_id` - User-employee mapping
- `employees.idx_department_id` - Department filtering
- `employees.idx_phone` - Phone lookup
- `employees.idx_active` - Active employee filtering

### Leave Management (5)
- `leave_requests.idx_employee_id` - Employee's leaves
- `leave_requests.idx_status` - Pending leaves
- `leave_requests.idx_created_at` - Recent requests
- `leave_balance.idx_employee_id` - Employee's balance
- `leave_balance.idx_employee_leave_year` - Balance lookup

### Attendance Tracking (3)
- `attendance.idx_employee_id_date` - Day attendance
- `attendance.idx_date` - Daily reports
- `attendance.idx_status` - Status filtering

### Organization (2)
- `departments.idx_name` - Department lookup
- `refresh_tokens.idx_expiry_date` - Token cleanup

### Others (1)
- Unique constraint on `users.email`
- Unique constraint on `employees.phone`

## Database Character Set

All tables use: `utf8mb4_unicode_ci`

This provides:
- Full Unicode support
- Emoji support
- Proper sorting for all languages
- Case-insensitive collation

## Backup Recommendation

Daily backups using:
```bash
mysqldump --single-transaction --quick --lock-tables=false \
  -u user -p database > backup_$(date +%Y%m%d).sql
```

## Maintenance

### Regular Tasks
- Monitor index usage
- Optimize tables: `OPTIMIZE TABLE table_name;`
- Check constraints: `ANALYZE TABLE table_name;`
- Remove old refresh tokens (7+ days expired)
- Archive old attendance records (annual)
- Verify backups work

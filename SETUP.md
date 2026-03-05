# Employee Management System - Setup Guide

## Prerequisites

- Node.js 18+ (for frontend)
- Java 17 (for backend)
- Maven 3.6+ (for building backend)
- MySQL 8.0+ (for database)
- Git

## Quick Start

### Backend Setup

1. **Install Maven**
```bash
# Download and install from https://maven.apache.org/download.cgi
# Add to PATH
```

2. **Configure Environment**
```bash
cd backend
cp .env.example .env
# Edit .env with your database credentials and JWT secret
```

3. **Start Backend**
```bash
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`

### Frontend Setup

1. **Install Dependencies**
```bash
cd frontend
npm install
```

2. **Configure API URL** (if needed)
```bash
# Edit .env.local
VITE_API_URL=http://localhost:8080/api
```

3. **Start Development Server**
```bash
npm run dev
```

Frontend runs on `http://localhost:5173`

## Features

### Authentication
- Admin and Employee portals
- JWT token-based authentication
- Token refresh mechanism
- Secure password storage (bcrypt)

### Employee Management
- CRUD operations for employees
- Profile photo upload
- Department assignment
- Salary and designation management

### Leave Management
- Leave application workflow
- Leave balance tracking
- Approval/rejection system
- Email notifications

### Attendance
- Check-in/check-out tracking
- Monthly attendance reports
- Attendance statistics

### Dashboard
- Admin dashboard with metrics
- Employee dashboard with personal stats
- Real-time notifications

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`
API Docs: `http://localhost:8080/api-docs`

## Database

Database is automatically created on first run with:
- 7 tables
- Proper indexes for performance
- Foreign key constraints

## Security

- Environment variables for secrets (no hardcoded values)
- JWT authentication and authorization
- CORS configuration
- Security headers (X-Frame-Options, HSTS, etc.)
- SQL injection prevention in sorting
- Password validation and strength requirements
- Input validation on all endpoints

## Troubleshooting

### Backend won't start
- Check MySQL is running
- Verify .env file has correct credentials
- Check port 8080 is not in use

### Frontend API errors
- Ensure backend is running on http://localhost:8080
- Check VITE_API_URL in .env.local
- Check browser console for CORS errors

### Database errors
- Verify MySQL 8.0+
- Check character encoding (utf8mb4)
- Run `mvn clean compile` to regenerate schema


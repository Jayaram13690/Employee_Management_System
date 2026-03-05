# Changelog

All notable changes to this project will be documented in this file.

## [1.0.0] - 2024-03-13

### Added

#### Backend Features
- JWT authentication with access and refresh tokens
- User role-based authorization (ADMIN, EMPLOYEE)
- Employee CRUD operations with full profile management
- Department management system
- Leave request workflow with approval process
- Leave balance tracking per employee per year
- Attendance tracking with check-in/check-out
- Dashboard with admin and employee views
- Email notifications for leave events
- User profile management with password change
- Profile photo upload functionality

#### Security Features
- Password hashing with bcrypt
- Strong password validation (8+ chars, uppercase, lowercase, digit, special char)
- SQL injection prevention with sort field whitelisting
- CORS configuration for frontend
- Security headers (X-Frame-Options, X-Content-Type-Options, HSTS)
- Secure environment variable configuration
- No hardcoded secrets

#### API Features
- Swagger/OpenAPI documentation at `/swagger-ui.html`
- Pagination on all list endpoints
- Sorting support with field validation
- Comprehensive input validation
- Consistent error response format
- 40+ REST endpoints

#### Database Features
- 7 main entities (User, Employee, Department, LeaveRequest, LeaveBalance, Attendance, RefreshToken)
- 18 performance indexes
- Foreign key constraints
- Unique constraints for data integrity
- Automatic timestamp tracking
- MySQL 8.0+ compatibility

#### Frontend Features
- React 18+ with Vite
- React Router for SPA navigation
- Authentication context with global state
- Protected routes with role-based access
- Admin dashboard with metrics
- Employee dashboard with personal stats
- Leave management UI for both roles
- Attendance tracking interface
- Employee and department management pages
- User profile management
- Error message handling with close button
- Toast notifications

#### DevOps/Deployment
- Maven build configuration
- Docker support with Dockerfile examples
- Docker Compose for local development
- Environment-based configuration
- Spring Boot 3.2.3 setup
- Async email service

### Fixed

#### Error Handling
- Login error messages now persist until dismissed
- Error messages don't disappear when typing
- Form doesn't reset when switching between Sign In/Sign Up tabs
- Proper error responses with validation feedback

#### Security
- Fixed token refresh mechanism to prevent 401 loops
- Improved exception handling with security headers
- Added sort field validation to prevent SQL injection
- Enhanced input validation across all endpoints

### Improved

#### User Experience
- Added close button (×) to error and success messages
- 5-second toast notifications for important actions
- Better error message visibility
- Improved form validation feedback
- Consistent styling across pages

#### Code Quality
- Comprehensive documentation (8 guide files)
- Clean architecture with separation of concerns
- Service layer abstraction
- Repository pattern for data access
- Custom validators for business rules
- Proper exception handling
- Detailed API documentation

#### Performance
- Database indexes for common queries
- Pagination for large datasets
- Async email sending
- Connection pooling
- Optimized queries

### Documentation

- SETUP.md: Comprehensive setup guide
- ARCHITECTURE.md: System design and structure
- FEATURES.md: Complete feature list
- SECURITY.md: Security features and best practices
- DEPLOYMENT.md: Production deployment guide
- CONTRIBUTING.md: Contributing guidelines
- API_ENDPOINTS.md: API reference
- TROUBLESHOOTING.md: Common issues and solutions
- This CHANGELOG.md

### Dependencies

#### Backend
- Spring Boot 3.2.3
- Java 17
- MySQL 8.0+
- Maven 3.6+
- JJWT 0.12.5
- Jackson for JSON
- Springdoc OpenAPI 2.2.0
- dotenv-java 3.0.0
- Spring Security
- Hibernate JPA

#### Frontend
- React 18+
- Vite 4+
- React Router 6+
- Axios for API calls
- React Toastify for notifications
- Node.js 18+

## [Future Releases]

### Planned Features
- Two-factor authentication (2FA)
- Employee skill tracking
- Performance evaluation system
- Project assignment and tracking
- Expense tracking and reimbursement
- Payroll integration
- Reports and analytics dashboard
- Bulk import/export functionality
- Multi-language support
- Dark mode UI

### Planned Improvements
- Advanced search with filters
- Real-time notifications (WebSocket)
- Caching layer (Redis)
- API rate limiting
- Audit logging
- Data encryption at rest
- Automated backups
- User activity tracking
- Field-level authorization
- Advanced analytics

### Security Enhancements
- Two-factor authentication (2FA)
- OAuth2 integration
- SAML support
- IP whitelisting
- Session management
- Data encryption
- Intrusion detection
- Vulnerability scanning

## [0.1.0] - Initial Development

- Project initialization
- Basic Spring Boot structure
- Database schema design
- API endpoint scaffolding
- Frontend project setup

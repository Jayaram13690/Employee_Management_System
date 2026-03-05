# Employee Management System

A comprehensive, production-ready employee management system built with **React + Spring Boot + MySQL**.

## 🌟 Features

- **User Authentication**: Secure JWT-based auth with refresh tokens
- **Employee Management**: Complete CRUD operations with profiles
- **Leave Management**: Application, approval workflow, and balance tracking
- **Attendance Tracking**: Check-in/out with monthly reports
- **Department Management**: Organize employees by departments
- **Admin Dashboard**: Executive summary with key metrics
- **Employee Dashboard**: Personal statistics and quick actions
- **Email Notifications**: Async email on leave events
- **API Documentation**: Swagger UI with interactive testing
- **Security**: Password validation, SQL injection prevention, security headers

## 📊 Technology Stack

### Backend
- **Spring Boot 3.2.3** - Java 17
- **MySQL 8.0+** - Relational database
- **JWT** - Token-based authentication
- **Hibernate JPA** - ORM

### Frontend
- **React 18+** - UI library
- **Vite** - Build tool
- **React Router 6** - SPA routing
- **Axios** - HTTP client

### DevOps
- **Docker** - Containerization
- **Maven** - Build tool
- **Nginx/Apache** - Web server

## 🚀 Quick Start

### Prerequisites
- Node.js 18+
- Java 17
- Maven 3.6+
- MySQL 8.0+

### Backend Setup

```bash
cd backend
cp .env.example .env
# Edit .env with your database credentials
mvn spring-boot:run
```

Backend runs on: `http://localhost:8080`

### Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on: `http://localhost:5173`

## 📝 Documentation

| Document | Description |
|----------|-------------|
| [SETUP.md](SETUP.md) | Comprehensive setup and installation guide |
| [ARCHITECTURE.md](ARCHITECTURE.md) | System design and code structure |
| [FEATURES.md](FEATURES.md) | Complete feature list with descriptions |
| [SECURITY.md](SECURITY.md) | Security features and best practices |
| [DEPLOYMENT.md](DEPLOYMENT.md) | Production deployment strategies |
| [API_ENDPOINTS.md](API_ENDPOINTS.md) | API reference and examples |
| [DATABASE.md](DATABASE.md) | Database schema and relationships |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | Common issues and solutions |
| [CONTRIBUTING.md](CONTRIBUTING.md) | How to contribute to the project |
| [CHANGELOG.md](CHANGELOG.md) | Version history and releases |

## 🔐 Security Highlights

✅ **Strong Password Validation**
- Minimum 8 characters
- Uppercase, lowercase, digit, special character required

✅ **JWT Authentication**
- Access token: 1-hour expiration
- Refresh token: 7-day expiration
- Token revocation on logout

✅ **Data Protection**
- Bcrypt password hashing
- Parameterized queries (no SQL injection)
- Environment variable configuration
- Security headers (HSTS, X-Frame-Options)

✅ **Input Validation**
- Custom validators for business rules
- Type checking on all fields
- Clear error messages

## 📊 API Overview

### Authentication
- `POST /api/auth/register` - Register admin account
- `POST /api/auth/login` - Login
- `POST /api/auth/refresh` - Refresh token
- `POST /api/auth/logout` - Logout

### Employees
- `GET /api/employees` - List (paginated)
- `POST /api/employees` - Create
- `PUT /api/employees/{id}` - Update
- `DELETE /api/employees/{id}` - Delete

### Leaves
- `POST /api/leaves/apply/{employeeId}` - Apply
- `PUT /api/leaves/approve/{leaveId}` - Approve
- `PUT /api/leaves/reject/{leaveId}` - Reject
- `GET /api/leaves/balance/{employeeId}` - Check balance

### Attendance
- `POST /api/attendance/check-in/{employeeId}` - Check in
- `POST /api/attendance/check-out/{employeeId}` - Check out
- `GET /api/attendance/monthly/{employeeId}` - Reports

Full API documentation: `http://localhost:8080/swagger-ui.html`

## 📂 Project Structure

```
EMP_Management/
├── backend/
│   ├── src/main/java/com/emp/management/
│   │   ├── config/          # Spring configuration
│   │   ├── controller/      # REST endpoints
│   │   ├── service/         # Business logic
│   │   ├── repository/      # Data access
│   │   ├── entity/          # JPA entities
│   │   ├── dto/             # Request/Response objects
│   │   ├── exception/       # Custom exceptions
│   │   └── validation/      # Validators
│   ├── pom.xml              # Maven dependencies
│   └── .env.example         # Environment template
├── frontend/
│   ├── src/
│   │   ├── components/      # Reusable components
│   │   ├── pages/           # Page components
│   │   ├── context/         # Auth context
│   │   ├── services/        # API calls
│   │   └── App.jsx          # Main app
│   ├── package.json         # NPM dependencies
│   └── vite.config.js       # Vite config
└── docs/                    # Documentation files
```

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 🚢 Deployment

### Docker Deployment

```bash
docker-compose up
```

### Production Deployment

See [DEPLOYMENT.md](DEPLOYMENT.md) for:
- Standalone Java deployment
- Nginx configuration
- SSL/TLS setup
- Database backup
- Performance tuning

## 📈 Performance

- **Database Indexes**: 18 optimized indexes
- **Pagination**: All list endpoints paginated
- **Async Email**: Non-blocking email sending
- **Query Optimization**: Indexed lookups for fast queries
- **Connection Pooling**: Efficient database connection management

## 🤝 Contributing

Contributions are welcome! See [CONTRIBUTING.md](CONTRIBUTING.md) for:
- Code style guidelines
- Commit message format
- Pull request process
- Testing requirements

## 🆘 Troubleshooting

Common issues and solutions: [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

## 📄 License

This project is open source and available under the MIT License.

## 👥 Author

**Jayaram Bantumilli**
- GitHub: [@Jayaram13690](https://github.com/Jayaram13690)
- Email: bantumillijayaram885@gmail.com

## 🗺️ Roadmap

### Version 1.1
- [ ] Two-factor authentication (2FA)
- [ ] Advanced search filters
- [ ] Bulk import/export

### Version 1.2
- [ ] Performance evaluation system
- [ ] Employee skill tracking
- [ ] Project assignment

### Version 2.0
- [ ] Mobile app (iOS/Android)
- [ ] Real-time notifications (WebSocket)
- [ ] Analytics dashboard
- [ ] Multi-language support

## 📞 Support

For issues or questions:
1. Check [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
2. Search GitHub issues
3. Create new issue with details

## 🙏 Acknowledgments

Built with modern web technologies and best practices for security, performance, and maintainability.

---

**Ready to get started?** See [SETUP.md](SETUP.md) for installation instructions.

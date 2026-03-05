# Employee Management System - Troubleshooting Guide

## Backend Issues

### Backend Won't Start

**Error:** `Connection refused` or `Cannot connect to database`

**Solutions:**
1. Verify MySQL is running: `mysql -u root -p`
2. Check .env file has correct credentials
3. Verify database exists: `CREATE DATABASE emp_management;`
4. Check port 8080 is not in use: `lsof -i :8080`

### Database Connection Error

**Error:** `java.sql.SQLException: Unknown database 'emp_management'`

**Solution:**
```sql
CREATE DATABASE emp_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

### Maven Build Fails

**Error:** `[ERROR] FAILURE`

**Solutions:**
1. Clean build: `mvn clean install`
2. Update Maven: `mvn -v`
3. Check Java version: `java -version` (need Java 17+)
4. Clear Maven cache: `rm -rf ~/.m2/repository`

### API Returns 401 Unauthorized

**Cause:** Invalid or expired JWT token

**Solutions:**
1. Log in again to get new token
2. Use refresh endpoint to refresh token
3. Check token hasn't expired (1 hour default)

### CORS Error in Frontend

**Error:** `Access to XMLHttpRequest blocked by CORS policy`

**Solutions:**
1. Verify backend is running
2. Check VITE_API_URL in frontend/.env.local
3. Verify CORS is enabled in SecurityConfig
4. Check frontend is on correct port

## Frontend Issues

### Blank Page After Login

**Cause:** Frontend can't connect to API

**Solutions:**
1. Check backend is running: `curl http://localhost:8080/api/health`
2. Check browser console for errors
3. Verify VITE_API_URL matches backend URL
4. Clear browser cache and localStorage

### "Invalid credentials" on Login

**Causes:**
1. Wrong email or password
2. User doesn't exist
3. User account deactivated
4. Backend error (check server logs)

**Solutions:**
1. Verify email is registered
2. Check capslock is off
3. Reset password if forgotten
4. Check backend logs: `tail -f /var/log/emp-management.log`

### Token Refresh Not Working

**Error:** Auto-redirect to login after 1 hour

**Solutions:**
1. Refresh token may have expired (7 days)
2. Login again to get new tokens
3. Check refresh endpoint in backend: `/api/auth/refresh`

### Form Validation Errors Not Showing

**Cause:** Frontend not displaying validation errors

**Solutions:**
1. Check console for JavaScript errors
2. Verify form component is updated
3. Check API response includes fieldErrors
4. Reload page and try again

### Slow Page Load

**Causes:**
1. Large dataset without pagination
2. Slow internet connection
3. Backend performance issue

**Solutions:**
1. Check network tab in DevTools
2. Verify API returns paginated results
3. Check backend logs for slow queries
4. Consider adding indexes to database

## Database Issues

### High Memory Usage

**Cause:** Unoptimized queries or large dataset

**Solutions:**
1. Check for missing indexes: `SHOW INDEXES FROM employees;`
2. Analyze slow queries: `SET GLOBAL slow_query_log = 1;`
3. Increase MySQL buffer: `innodb_buffer_pool_size = 2G`

### Duplicate Key Error

**Error:** `Duplicate entry for key 'idx_email'`

**Cause:** Trying to create user with existing email

**Solutions:**
1. Use different email
2. Delete existing user first
3. Check for orphaned records

### Table Doesn't Exist

**Error:** `Table 'emp_management.users' doesn't exist`

**Cause:** Tables not created

**Solutions:**
1. Restart application (auto-creates tables)
2. Run schema script manually
3. Check database: `USE emp_management; SHOW TABLES;`

## Email Issues

### Emails Not Sending

**Error:** Email not received after action

**Causes:**
1. Email configuration not set in .env
2. SMTP credentials invalid
3. Email service throttled

**Solutions:**
1. Check .env: `MAIL_USERNAME=`, `MAIL_PASSWORD=`
2. Test SMTP: `telnet smtp.gmail.com 587`
3. Enable 2FA and use App Password for Gmail
4. Check spam folder

### Wrong Sender Email

**Cause:** MAIL_FROM not set in .env

**Solution:**
```bash
MAIL_FROM=noreply@company.com
```

## Performance Issues

### Slow Employee List Load

**Cause:** No pagination or missing indexes

**Solutions:**
1. Verify using pagination: `?page=0&size=10`
2. Add index: `CREATE INDEX idx_active ON employees(active);`
3. Check query: `EXPLAIN SELECT * FROM employees;`

### High CPU Usage

**Cause:** Inefficient queries or heavy operations

**Solutions:**
1. Check backend logs for slow queries
2. Restart application: `systemctl restart emp-management`
3. Increase JVM heap: `java -Xmx4g -jar app.jar`

## Port Conflicts

### Port 8080 Already in Use

**Solutions:**
```bash
# Find process using port
lsof -i :8080

# Kill process
kill -9 <PID>

# Or use different port
java -jar emp-management.jar --server.port=8081
```

### Port 3306 Already in Use

**Solutions:**
```bash
# MySQL port conflict
sudo systemctl restart mysql

# Or run on different port in docker
docker run -p 3307:3306 mysql:8.0
```

## Login Issues

### Stuck in Login Loop

**Cause:** Token refresh failing

**Solutions:**
1. Clear localStorage: Open DevTools → Application → Clear all
2. Logout completely
3. Log back in
4. Check refresh token expiration (7 days)

### Password Requirements Not Clear

**Minimum requirements:**
- 8+ characters
- Uppercase letter (A-Z)
- Lowercase letter (a-z)
- Number (0-9)
- Special character (!@#$%^&*)

Example: `SecurePass123!`

## Permission Issues

### Cannot Create/Edit Employees

**Cause:** Not an admin

**Solutions:**
1. Use admin account
2. Ask admin to create your account
3. Check role in database: `SELECT role FROM users;`

### Cannot Approve Leave

**Cause:** Employee account (admins only)

**Solution:** Log in with admin account

## File Upload Issues

### Profile Photo Not Uploading

**Causes:**
1. File too large (max 5MB)
2. Wrong file type
3. Upload directory not writable

**Solutions:**
1. Resize image
2. Use JPEG, PNG, or GIF
3. Check uploads folder permissions: `chmod 755 uploads/`

## Testing Issues

### Tests Fail Locally

**Solutions:**
1. Clean build: `mvn clean test`
2. Start fresh database
3. Check test data setup
4. Run individual test: `mvn test -Dtest=TestClassName`

## Getting Help

1. Check logs: `tail -f /var/log/emp-management.log`
2. Check browser console: F12 → Console tab
3. Check network requests: F12 → Network tab
4. Enable debug logging: Add `--debug` flag
5. Check GitHub issues for similar problems

## Reporting Issues

Include:
- Error message (full stack trace)
- Steps to reproduce
- Browser/Java version
- Environment (dev/prod)
- Recent changes
- Backend and frontend logs

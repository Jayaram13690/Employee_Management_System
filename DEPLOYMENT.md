# Employee Management System - Deployment Guide

## Prerequisites

- Docker (optional, for containerization)
- MySQL 8.0+ (managed or self-hosted)
- Node.js 18+ (for frontend build)
- Java 17 JDK
- Apache or Nginx (for frontend serving)

## Backend Deployment

### Build Backend

```bash
cd backend
mvn clean package
```

Creates `target/emp-management-1.0.0.jar`

### Server Setup

1. **Install Java 17**
```bash
java -version  # Verify installation
```

2. **Create Application Directory**
```bash
mkdir /opt/emp-management
mkdir /opt/emp-management/uploads
```

3. **Set Environment Variables**
```bash
export DB_URL=jdbc:mysql://db.example.com:3306/emp_management
export DB_USERNAME=emp_user
export DB_PASSWORD=secure_password
export JWT_SECRET=base64_encoded_key
export MAIL_USERNAME=noreply@company.com
export MAIL_PASSWORD=app_password
```

4. **Run Application**
```bash
java -jar emp-management-1.0.0.jar
```

### Production Run

Using system service (systemd):

```bash
[Unit]
Description=Employee Management System Backend
After=network.target

[Service]
Type=simple
User=appuser
WorkingDirectory=/opt/emp-management
Environment="DB_URL=jdbc:mysql://db.example.com/emp_management"
Environment="DB_USERNAME=emp_user"
Environment="DB_PASSWORD=password"
ExecStart=/usr/bin/java -jar emp-management-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Save as `/etc/systemd/system/emp-management.service`

```bash
systemctl daemon-reload
systemctl enable emp-management
systemctl start emp-management
systemctl status emp-management
```

## Frontend Deployment

### Build Frontend

```bash
cd frontend
npm install
npm run build
```

Creates `dist/` directory with static files

### Serve with Nginx

```nginx
server {
    listen 80;
    server_name app.example.com;

    # Redirect HTTP to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name app.example.com;

    ssl_certificate /etc/letsencrypt/live/app.example.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/app.example.com/privkey.pem;

    root /var/www/emp-management;
    index index.html;

    # SPA routing
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Static assets with caching
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {
        expires 30d;
        add_header Cache-Control "public, immutable";
    }

    # API proxy
    location /api/ {
        proxy_pass http://backend.example.com:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### Serve with Apache

```apache
<VirtualHost *:443>
    ServerName app.example.com
    DocumentRoot /var/www/emp-management

    SSLEngine on
    SSLCertificateFile /etc/ssl/certs/app.example.com.crt
    SSLCertificateKeyFile /etc/ssl/private/app.example.com.key

    <Directory /var/www/emp-management>
        RewriteEngine On
        RewriteBase /
        RewriteRule ^index\.html$ - [L]
        RewriteCond %{REQUEST_FILENAME} !-f
        RewriteCond %{REQUEST_FILENAME} !-d
        RewriteRule . /index.html [L]
    </Directory>

    ProxyPass /api http://backend.example.com:8080/api
    ProxyPassReverse /api http://backend.example.com:8080/api
</VirtualHost>
```

## Docker Deployment

### Dockerfile (Backend)

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/emp-management-1.0.0.jar app.jar
EXPOSE 8080
ENV DB_URL=jdbc:mysql://mysql:3306/emp_management
CMD ["java", "-jar", "app.jar"]
```

### Dockerfile (Frontend)

```dockerfile
FROM node:18-alpine as builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### Docker Compose

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_DATABASE: emp_management
    volumes:
      - mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"

  backend:
    build: ./backend
    environment:
      DB_URL: jdbc:mysql://mysql:3306/emp_management
      DB_USERNAME: root
      DB_PASSWORD: rootpass
    ports:
      - "8080:8080"
    depends_on:
      - mysql

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
```

## Database Setup

### MySQL Configuration

```sql
CREATE DATABASE emp_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'emp_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON emp_management.* TO 'emp_user'@'localhost';
FLUSH PRIVILEGES;
```

Application will auto-create tables on first run.

## SSL/TLS Certificate

Using Let's Encrypt (certbot):

```bash
sudo apt-get install certbot python3-certbot-nginx
sudo certbot certonly --nginx -d app.example.com
```

## Performance Tuning

### MySQL

```ini
[mysqld]
max_connections = 1000
max_allowed_packet = 256M
query_cache_size = 64M
innodb_buffer_pool_size = 2G
```

### Java Application

```bash
java -Xms1g -Xmx4g -jar emp-management-1.0.0.jar
```

### Nginx

```nginx
worker_processes auto;
worker_connections 2048;
keepalive_timeout 65;
gzip on;
gzip_types text/plain text/css application/json application/javascript;
```

## Monitoring

### Logging

- Backend logs: `/var/log/emp-management.log`
- Frontend errors: Browser console
- MySQL logs: `/var/log/mysql/error.log`

### Health Checks

Backend health endpoint: `http://localhost:8080/actuator/health`

### Monitoring Tools

- ELK Stack for log analysis
- Prometheus for metrics
- Grafana for dashboards
- New Relic or DataDog for APM

## Backup & Recovery

### Database Backup

```bash
mysqldump -u emp_user -p emp_management > backup.sql
```

### Application Backup

```bash
tar -czf app-backup.tar.gz /opt/emp-management
```

### Restore

```bash
mysql -u emp_user -p emp_management < backup.sql
```

## Scaling

### Horizontal Scaling

- Run multiple backend instances
- Use load balancer (Nginx, HAProxy)
- Shared MySQL database
- Session management via database

### Vertical Scaling

- Increase server resources
- Optimize JVM heap size
- Increase database connections


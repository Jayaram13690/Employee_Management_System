# Contributing to Employee Management System

## Getting Started

1. Fork the repository
2. Clone your fork
3. Create a feature branch
4. Make your changes
5. Commit with clear messages
6. Push and create a Pull Request

## Development Setup

### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Code Style

### Java

- Use Google Java Style Guide
- Indent: 4 spaces
- Max line length: 100 characters
- Use meaningful variable names
- Add comments for complex logic

### JavaScript/React

- Use ES6+ syntax
- Indent: 2 spaces
- Use const/let (no var)
- Meaningful component names
- JSDoc for functions

## Commit Messages

Format: `type: subject`

Types:
- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation
- `style:` Code style changes
- `refactor:` Code refactoring
- `test:` Adding tests
- `chore:` Build/dependency updates

Example:
```
feat: Add employee search functionality

- Added search by name and email
- Implemented debounced search
- Added loading state
```

## Testing

### Backend Tests

```bash
cd backend
mvn test
```

### Frontend Tests

```bash
cd frontend
npm run test
```

## Pull Request Process

1. Update documentation if needed
2. Add tests for new features
3. Ensure all tests pass
4. Update CHANGELOG if major change
5. Get 1-2 approvals before merging

## Reporting Issues

Include:
- Browser/Java version
- Steps to reproduce
- Expected vs actual behavior
- Screenshots/logs if applicable

## Feature Requests

Describe:
- What problem it solves
- How it should work
- Why it's important
- Any alternatives considered

## Areas to Contribute

### Backend
- Add more validations
- Implement missing features
- Optimize queries
- Add unit tests
- Improve error handling

### Frontend
- UI/UX improvements
- Additional pages
- Better error messages
- Responsive design fixes
- Accessibility improvements

### Documentation
- Setup guides
- API documentation
- Code examples
- Troubleshooting
- Video tutorials

## Code Review Guidelines

- Provide constructive feedback
- Ask questions for clarity
- Suggest improvements
- Approve only if confident
- Be respectful and helpful


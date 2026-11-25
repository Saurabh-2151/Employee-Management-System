# Employee Management System

A RESTful Employee Management System developed using **Java, Spring Boot, Spring Data JPA, and MySQL**. The application provides APIs to manage employees and departments with pagination, sorting, searching, filtering, validation, and centralized exception handling.

## Features

### Employee Management
- Create a new employee.
- Retrieve employee details by ID.
- Update employee information.
- Soft delete employees by setting their active status to `false`.
- Retrieve all employees with pagination.
- Search employees by name or email.
- Filter employees by department and active status.
- Sort employees by name and salary in ascending or descending order.
- Combine multiple search and filter criteria.
- Prevent duplicate employee email addresses.

### Department Management
- Create a department.
- Retrieve all departments.
- Manage the relationship between employees and departments.

### Additional Features
- Layered architecture (Controller, Service, Repository).
- DTO-based request and response handling.
- Request validation using Jakarta Validation and `@Valid`.
- Centralized exception handling.
- Automatic `createdAt` and `updatedAt` timestamps.
- Database-level pagination, searching, and filtering.
- Soft deletion of employee records.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Programming Language |
| Spring Boot 4.0.10 | Backend Framework |
| Spring Data JPA | Database Operations |
| Hibernate | ORM |
| MySQL / MariaDB | Database |
| Maven | Build Management |
| Jakarta Validation | Request Validation |
| Postman | API Testing |

## Project Structure

```text
employee-management-system/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/employee_management_system/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── EmployeeManagementSystemApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/example/employee_management_system/
│
├── postman/
│   └── Employee_Management_System.postman_collection.json
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Database Configuration

### 1. Create Database

Create a MySQL database:

```sql
CREATE DATABASE ems_db;
```

### 2. Configure Database Connection

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ems_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

Replace the database username and password with your local database credentials.

## How to Run the Project

### Prerequisites

- Java 17 or higher
- MySQL or compatible MariaDB database
- Maven (optional, Maven Wrapper is included)
- Postman

### 1. Clone the Repository

```bash
git clone https://github.com/Saurabh-2151/Employee-Management-System.git
```

### 2. Navigate to the Project

```bash
cd employee-management-system
```

### 3. Run the Application

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

The application will start at:

```text
http://localhost:8080
```

## API Endpoints

Base URL:

```text
http://localhost:8080
```

### Employee APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/employees` | Create employee |
| GET | `/api/employees/{id}` | Get employee by ID |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Soft delete employee |
| GET | `/api/employees` | Get all employees |

The employee listing API supports pagination, sorting, searching, and filtering.

Example:

```http
GET /api/employees?page=0&size=10
```

### Department APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/departments` | Create department |
| GET | `/api/departments` | Get all departments |

## Employee Listing Capabilities

The employee listing API supports:

- Pagination with a default page size of 10.
- Sorting by employee name and salary.
- Ascending and descending sorting.
- Searching by employee name or email.
- Filtering by department.
- Filtering by active status.
- Combining multiple filters.

Example pagination request:

```http
GET /api/employees?page=0&size=10
```

Example search request:

```http
GET /api/employees?search=john
```

Example department filter:

```http
GET /api/employees?departmentId=1
```

Example active employee filter:

```http
GET /api/employees?active=true
```

Use the Postman collection for the complete set of tested API requests and supported query parameters.

## Validation and Exception Handling

The application handles the following scenarios:

- Employee or department not found.
- Invalid email address.
- Duplicate employee email.
- Missing mandatory fields.
- Negative employee salary.
- Invalid pagination parameters.
- Invalid request data.
- Database constraint violations.

Centralized exception handling provides consistent error responses with HTTP status codes and relevant error messages.

## Testing

Unit tests are implemented using JUnit and Mockito.

### EmployeeServiceTest
- Duplicate employee email validation.
- Employee not found scenario.
- Employee soft deletion.

### DepartmentServiceTest
- Retrieve all departments.
- Successful department creation.
- Duplicate department name validation.

The Spring Boot application context test is also included.

### Run All Tests

```bash
.\mvnw.cmd clean test
```

### Test Results

**Latest test execution:**

```text
Tests run: 7
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

## Postman Collection

The Postman collection is available in:

```text
postman/Employee_Management_System.postman_collection.json
```

Import this JSON file into Postman to test the employee and department APIs.

## Author

**Saurabh Ganjale**

Java | Spring Boot | Flutter | Full Stack Development


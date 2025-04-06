# **Todo List REST API**

A comprehensive **Todo List API** built with **Spring Boot**, **Spring Security (JWT)**, **Flyway**, **Hibernate**, and **PostgreSQL**. This API provides secure user authentication and full CRUD operations for managing todo items with POC role-based access control.

---

## **📌 Table of Contents**
1. [Features](#features)
2. [Technologies](#technologies)
3. [Setup & Installation](#setup--installation)
4. [API Endpoints](#api-endpoints)
5. [Authentication](#authentication)
6. [Database Entities](#-database-entities)
7. [Error Handling](#error-handling)
8. [Testing](#testing)
9. [Deployment](#deployment)

---

## **✨ Features**
✅ **User Authentication** (JWT-based)  
✅ **Role-Based Access Control**  
✅ **Full CRUD Operations** for Todos  
✅ **Pagination & Filtering** (by status, priority, due date)  
✅ **Request Validation & Error Handling**  
✅ **Flyway Database Migrations**  
✅ **Audit Logging**  
✅ **Soft Delete** for Todos  
✅ **Swagger API Documentation**  

---

## **🛠 Technologies**
- **Backend**:
    - Java 17, Spring Boot 3.2.x
    - Spring Security (JWT)
    - Hibernate, PostgreSQL
    - Flyway (Database Migrations)
    - Lombok (Reduced Boilerplate)
    - MapStruct (DTO Mappings)
    - Swagger (API Documentation)

- **Infrastructure**:
    - Docker (Containerization)
    - Prometheus + Grafana (Monitoring)

---

## **⚙ Setup & Installation**
### **Prerequisites**
- Java 17+
- PostgreSQL 14+
- Maven 3.9+
- Docker (optional)

### **1. Clone the Repository**
```bash
git clone https://github.com/your-repo/todo-list-api.git
cd todo-list-api
```

### **2. Configure Database**
Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todo_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.flyway.locations=classpath:database/migrations
```

### **3. Run the Application**
```bash
mvn spring-boot:run
```
Or with Docker:
```bash
docker-compose up -d
```

### **4. Access API Docs**
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

---

## **🔐 Authentication**
### **Register a New User**
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "john", 
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

### **Login & Get JWT Token**
```http
POST /api/v1/auth/authenticate
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### **Protected Endpoints**
Include the JWT token in the `Authorization` header:
```
Authorization: Bearer <your-jwt-token>
```

---

## **📋 API Endpoints**
| Method | Endpoint | Description               | Access |
|--------|----------|---------------------------|--------|
| `POST` | `/auth/register` | Creates a new user        | Public |
| `POST` | `/auth/login` | Login & get JWT token     | Public |
| `GET` | `/todos` | Get all todos (paginated) | USER, ADMIN |
| `GET` | `/todos/{id}` | Get a todo by ID          | USER, ADMIN |
| `POST` | `/todos` | Create a new todo         | USER, ADMIN |
| `PUT` | `/todos/{id}` | Update a todo             | USER, ADMIN |
| `DELETE` | `/todos/{id}` | Delete a todo             | USER, ADMIN |
| `GET` | `/todos/completed` | Get completed todos       | USER, ADMIN |

---

## **🗄 Database Entities**
 - User
 - Todo

---


## **🚀 Deployment**
### **1. Build & Run Locally**
```bash
mvn clean install
java -jar target/todo-list-1.0.0.jar
```

### **2. Docker Deployment**
```bash
docker build -t todo-api .
docker run -p 8080:8080 todo-api
```

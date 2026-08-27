# E-Commerce Reliability Platform

A Spring Boot based e-commerce backend designed to improve **seller reliability, order management, inventory management, complaint handling, and delivery tracking**.

The project uses **Spring Boot, Spring Security, JWT, MySQL, Redis, and Apache Kafka** to provide a secure and event-driven e-commerce platform.

---

## 🚀 Features

### Authentication & Authorization

* User registration
* User login
* JWT-based authentication
* Role-based authorization
* Three main roles:

  * `CUSTOMER`
  * `SELLER`
  * `ADMIN`
* Protected REST APIs using Spring Security

### Product Management

* Create products
* View all products
* View individual products
* Seller-specific product creation

### Inventory Management

* Track available inventory
* Track reserved inventory
* Automatically reduce inventory when an order is created
* Automatically restore inventory when an order is cancelled

### Order Management

* Create orders
* View orders
* Cancel orders
* Inventory reservation
* Inventory restoration on cancellation
* Order-created Kafka events
* Order-cancelled Kafka events

### Shipment Management

* Shipment tracking
* Shipment information associated with orders

### Complaint Management

* Create customer complaints
* Delivery complaints
* View complaints
* Filter complaints by status
* Resolve complaints
* Admin complaint management
* Complaint-created Kafka events

### Seller Reliability

The platform calculates seller reliability based on seller-related activity such as orders and complaints.

Redis is used to cache seller reliability results.

The cache is cleared when relevant order or complaint changes occur.

### Event-Driven Architecture

Apache Kafka is used for asynchronous events:

* `order-created`
* `order-cancelled`
* `complaint-created`

### Caching

Redis is used for:

* Seller reliability caching
* Reducing repeated reliability calculations
* Improving response performance

---

# 🏗️ Technology Stack

| Technology      | Purpose                         |
| --------------- | ------------------------------- |
| Java 17         | Programming language            |
| Spring Boot     | Backend framework               |
| Spring Web      | REST APIs                       |
| Spring Data JPA | Database access                 |
| Spring Security | Authentication & authorization  |
| JWT             | Stateless authentication        |
| MySQL           | Relational database             |
| Redis           | Caching                         |
| Apache Kafka    | Event messaging                 |
| Maven           | Build and dependency management |
| Lombok          | Reduce boilerplate code         |
| Postman         | API testing                     |

---

# 📁 Project Structure

```text
ecommerce-reliability-platform/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── reliability/
│   │   │           └── ecommerce/
│   │   │
│   │   │               ├── config/
│   │   │               │   ├── KafkaConfig.java
│   │   │               │   ├── RedisConfig.java
│   │   │               │   └── SecurityConfig.java
│   │   │
│   │   │               ├── controller/
│   │   │               │   ├── AdminController.java
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── OrderController.java
│   │   │               │   ├── ProductController.java
│   │   │               │   └── ...
│   │   │
│   │   │               ├── dto/
│   │   │               │   ├── LoginRequest.java
│   │   │               │   ├── LoginResponse.java
│   │   │               │   ├── UserRequest.java
│   │   │               │   ├── UserResponse.java
│   │   │               │   ├── OrderRequest.java
│   │   │               │   ├── OrderResponse.java
│   │   │               │   └── ...
│   │   │
│   │   │               ├── entity/
│   │   │               │   ├── User.java
│   │   │               │   ├── Product.java
│   │   │               │   ├── Inventory.java
│   │   │               │   ├── Order.java
│   │   │               │   ├── OrderItem.java
│   │   │               │   ├── Shipment.java
│   │   │               │   ├── Complaint.java
│   │   │               │   └── ...
│   │   │
│   │   │               ├── event/
│   │   │               │   ├── KafkaProducerService.java
│   │   │               │   ├── OrderCreatedEvent.java
│   │   │               │   ├── OrderCancelledEvent.java
│   │   │               │   └── ComplaintCreatedEvent.java
│   │   │
│   │   │               ├── exception/
│   │   │               │   ├── ResourceNotFoundException.java
│   │   │               │   ├── DuplicateEmailException.java
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │
│   │   │               ├── repository/
│   │   │               │   ├── UserRepository.java
│   │   │               │   ├── ProductRepository.java
│   │   │               │   ├── OrderRepository.java
│   │   │               │   ├── InventoryRepository.java
│   │   │               │   ├── ComplaintRepository.java
│   │   │               │   └── ...
│   │   │
│   │   │               ├── security/
│   │   │               │   ├── JwtService.java
│   │   │               │   └── JwtAuthenticationFilter.java
│   │   │
│   │   │               └── service/
│   │   │                   ├── AuthService.java
│   │   │                   ├── OrderService.java
│   │   │                   ├── ProductService.java
│   │   │                   ├── ComplaintService.java
│   │   │                   ├── DeliveryComplaintService.java
│   │   │                   ├── SellerReliabilityService.java
│   │   │                   └── ...
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── pom.xml
└── README.md
```

---

# 🔐 User Roles

## CUSTOMER

Customers can:

* Register
* Login
* View products
* Create orders
* View orders
* Cancel orders
* Create complaints

## SELLER

Sellers can:

* Login
* Create products
* Manage inventory
* View seller-related information
* Access seller APIs

## ADMIN

Administrators can:

* View complaints
* Filter complaints by status
* Resolve complaints
* View seller reliability information
* Access administrative APIs

---

# 🔑 Authentication

The application uses JWT authentication.

After successful login, the API returns a JWT token.

Example:

```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "email": "customer@test.com",
    "role": "CUSTOMER"
}
```

For protected APIs, use:

```text
Authorization: Bearer <JWT_TOKEN>
```

In Postman:

```text
Authorization
    ↓
Type: Bearer Token
    ↓
Paste JWT token
```

---

# 🌐 API Endpoints

## Authentication

### Register

```text
POST /api/auth/register
```

Example request:

```json
{
    "name": "Test Customer",
    "email": "customer@test.com",
    "password": "password123",
    "role": "CUSTOMER"
}
```

Response:

```text
201 Created
```

---

### Login

```text
POST /api/auth/login
```

Example request:

```json
{
    "email": "customer@test.com",
    "password": "password123"
}
```

Response:

```text
200 OK
```

---

# 📦 Products

### Get All Products

```text
GET /api/products
```

Authentication:

```text
Not required
```

---

### Get Product

```text
GET /api/products/{id}
```

Authentication:

```text
Not required
```

---

### Create Product

```text
POST /api/products
```

Authentication:

```text
SELLER token required
```

---

# 🛒 Orders

### Create Order

```text
POST /api/orders
```

Authentication:

```text
CUSTOMER token required
```

Example:

```json
{
    "customerId": 1,
    "items": [
        {
            "productId": 1,
            "quantity": 2
        }
    ]
}
```

The system:

1. Validates the customer.
2. Checks the product.
3. Checks inventory.
4. Reduces available inventory.
5. Increases reserved inventory.
6. Creates the order.
7. Publishes an `order-created` Kafka event.

---

### Get Order

```text
GET /api/orders/{id}
```

Authentication:

```text
JWT required
```

---

### Cancel Order

```text
PUT /api/orders/{id}/cancel
```

Authentication:

```text
CUSTOMER token required
```

When an order is cancelled:

1. Inventory is restored.
2. Reserved inventory is reduced.
3. Order status becomes `CANCELLED`.
4. Seller reliability cache is cleared.
5. An `order-cancelled` Kafka event is published.

---

# 📢 Complaints

### Create Complaint

```text
POST /api/complaints
```

Authentication:

```text
CUSTOMER token required
```

Example:

```json
{
    "customerId": 1,
    "orderId": 1,
    "shipmentId": 1,
    "category": "DELIVERY",
    "description": "My order was delivered late."
}
```

---

### Get Complaint

```text
GET /api/complaints/{id}
```

Authentication:

```text
JWT required
```

---

# 👨‍💼 Admin APIs

### Get Open Complaints

```text
GET /api/admin/complaints
```

Authentication:

```text
ADMIN token required
```

---

### Get Complaints by Status

```text
GET /api/admin/complaints/{status}
```

Example:

```text
GET /api/admin/complaints/OPEN
```

Authentication:

```text
ADMIN token required
```

---

### Resolve Complaint

```text
PUT /api/admin/complaints/{id}/resolve
```

Authentication:

```text
ADMIN token required
```

---

### Get Seller Reliability

```text
GET /api/admin/sellers/{sellerId}/reliability
```

Authentication:

```text
ADMIN token required
```

---

# 📊 Seller Reliability

Seller reliability is calculated by:

```text
Seller
   ↓
Orders + Complaints + Reliability Data
   ↓
SellerReliabilityService
   ↓
Redis Cache
   ↓
SellerReliabilityResponse
```

Redis prevents the application from recalculating the same seller reliability information repeatedly.

When relevant data changes, the cache is cleared:

```text
Order cancelled
      ↓
Seller ID
      ↓
clearReliabilityCache()
      ↓
Redis cache cleared
```

The same approach is used when relevant complaints are resolved/updated.

---

# 📨 Kafka

The project uses Apache Kafka for asynchronous event communication.

## Kafka Topics

### Order Created

```text
order-created
```

Published when a new order is created.

### Order Cancelled

```text
order-cancelled
```

Published when an order is cancelled.

### Complaint Created

```text
complaint-created
```

Published when a complaint is created/processed.

Architecture:

```text
                     ┌──────────────────┐
                     │   OrderService   │
                     └────────┬─────────┘
                              │
                              ▼
                       KafkaProducerService
                              │
                ┌─────────────┼─────────────┐
                ▼             ▼             ▼
        order-created  order-cancelled  complaint-created
```

---

# 🗄️ MySQL Configuration

Example `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_reliability
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Create the database:

```sql
CREATE DATABASE ecommerce_reliability;
```

---

# 🔴 Redis Configuration

Redis is expected to run on:

```text
localhost:6379
```

Example:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

The application uses Redis to cache seller reliability calculations.

---

# 📨 Kafka Configuration

Kafka is expected to run on:

```text
localhost:9092
```

The Kafka configuration creates:

```text
complaint-created
order-created
order-cancelled
```

topics.

Make sure Kafka is running before testing APIs that publish events.

---

# ▶️ Running the Application

## 1. Start MySQL

Make sure MySQL is running.

Create the database:

```sql
CREATE DATABASE ecommerce_reliability;
```

---

## 2. Start Redis

Make sure Redis is running on:

```text
localhost:6379
```

---

## 3. Start Kafka

Make sure Kafka is running on:

```text
localhost:9092
```

---

## 4. Build the project

From the project directory:

```bash
mvn clean package
```

Expected result:

```text
BUILD SUCCESS
```

---

## 5. Run Spring Boot

Using Maven:

```bash
mvn spring-boot:run
```

Or run the main Spring Boot application from your IDE.

The backend runs on:

```text
http://localhost:8081
```

if `server.port=8081` is configured.

---

# 🧪 Testing with Postman

Recommended testing order:

```text
1. Register CUSTOMER
       ↓
2. Login CUSTOMER
       ↓
3. Register SELLER
       ↓
4. Login SELLER
       ↓
5. Create Product
       ↓
6. Create Inventory
       ↓
7. Login CUSTOMER
       ↓
8. Create Order
       ↓
9. Create Shipment
       ↓
10. Create Complaint
       ↓
11. Login ADMIN
       ↓
12. View Complaints
       ↓
13. Resolve Complaint
       ↓
14. Check Seller Reliability
```

---

# 🔒 Common HTTP Responses

| Status                      | Meaning                                        |
| --------------------------- | ---------------------------------------------- |
| `200 OK`                    | Request successful                             |
| `201 Created`               | Resource successfully created                  |
| `400 Bad Request`           | Invalid request/data                           |
| `401 Unauthorized`          | Missing or invalid authentication              |
| `403 Forbidden`             | Authenticated user does not have required role |
| `404 Not Found`             | Resource does not exist                        |
| `409 Conflict`              | Duplicate/conflicting resource                 |
| `500 Internal Server Error` | Unexpected server error                        |

---

# ⚠️ Troubleshooting

## 403 Forbidden

Check:

1. The JWT token is present.
2. The token is not expired.
3. The correct user is logged in.
4. The database role is correct.
5. The request uses the correct role.

For example:

```text
POST /api/orders
```

requires:

```text
ROLE_CUSTOMER
```

while:

```text
POST /api/products
```

requires:

```text
ROLE_SELLER
```

---

## 400 Bad Request

Check:

* JSON field names
* Required fields
* IDs
* Quantity
* Request DTO validation
* Inventory availability

For example, creating an order with insufficient inventory results in a bad-request error.

---

## Insufficient Inventory

If you receive:

```text
Insufficient inventory for product: Wireless Bluetooth Headphones
```

check the inventory table and make sure the product has enough available quantity.

---

## Kafka Errors

If Kafka-related errors appear in the Spring Boot console, make sure Kafka is running on:

```text
localhost:9092
```

---

## Redis Errors

If Redis-related errors appear, make sure Redis is running on:

```text
localhost:6379
```

---

# 🧩 Architecture

```text
                    ┌─────────────────┐
                    │    Frontend     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   REST APIs     │
                    │ Spring Boot     │
                    └────────┬────────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │
              ▼              ▼              ▼
          Security        Services       Controllers
          + JWT              │
                             │
              ┌──────────────┼──────────────┐
              │              │              │
              ▼              ▼              ▼
            MySQL          Redis          Kafka
          Database        Cache          Events
```

---

# 🎯 Project Goal

The goal of this project is to provide a reliable e-commerce platform where:

* Customers can safely place and manage orders.
* Sellers can manage products and inventory.
* Administrators can monitor complaints.
* Seller reliability can be calculated and monitored.
* Redis improves reliability-query performance.
* Kafka provides asynchronous event processing.
* JWT and role-based security protect the APIs.

---

# 📌 Project Status

### Backend

* Authentication & JWT: ✅
* Role-based security: ✅
* Product management: ✅
* Inventory management: ✅
* Order management: ✅
* Order cancellation: ✅
* Complaint management: ✅
* Shipment functionality: ✅
* Seller reliability: ✅
* Redis caching: ✅
* Kafka events: ✅
* Exception handling: ✅
* REST APIs: ✅

### Frontend

🚧 To be implemented.

---

# 👨‍💻 Development

This project is intended as a full-stack e-commerce reliability platform.

The backend is implemented using a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL
```

with supporting infrastructure:

```text
JWT Security
Redis Cache
Apache Kafka
```

---

# 📄 License

This project is developed for educational and project purposes.

```
```

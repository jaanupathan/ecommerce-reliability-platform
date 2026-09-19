# E-Commerce Reliability Platform

## 🚀 Project Overview

The **E-Commerce Reliability Platform** is a Spring Boot backend designed to make online shopping more reliable and secure.

The system focuses on common e-commerce problems such as:

* Inventory overselling
* Order management
* Delivery verification
* Customer complaints
* Seller reliability
* Slow database access
* Reliable event processing

## 🛠️ Technology Stack

### Backend

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* REST APIs

### Database & Messaging

* MySQL
* Redis
* Apache Kafka

### AWS

* **Amazon EC2** – runs the Spring Boot application
* **Amazon RDS for MySQL** – managed relational database
* **Amazon ElastiCache for Redis** – managed Redis caching
* **Amazon MSK (Managed Streaming for Apache Kafka)** – event streaming
* **Amazon CloudWatch** – application logs and monitoring
* **AWS IAM** – access and permissions
* **AWS CLI** – AWS resource management and deployment

## ☁️ How AWS Is Used

The project uses AWS to move the application from a local development environment toward a cloud-based production architecture.

```text
                    Users
                      |
                      v
               Spring Boot API
                Amazon EC2
                      |
        +-------------+-------------+
        |             |             |
        v             v             v
   Amazon RDS    ElastiCache     Amazon MSK
     MySQL          Redis          Kafka
        |             |             |
        |             |             v
        |             |       Event Processing
        |             |
        |             v
        |       Fast Cached Data
        |
        v
   Application Data

              |
              v
        Amazon CloudWatch
          Logs & Monitoring
```

### 1. Amazon EC2

The Spring Boot backend can be deployed on an Amazon EC2 instance.

EC2 provides the server environment required to run the Java application.

### 2. Amazon RDS for MySQL

The application uses MySQL for storing users, orders, inventory, shipments, complaints, and seller reliability information.

Amazon RDS provides a managed MySQL database so that database management can be handled through AWS.

### 3. Amazon ElastiCache for Redis

Redis is used as a caching layer.

Frequently accessed data can be stored in Redis so that the application does not need to query MySQL for every request.

This helps reduce database load and improve response time.

### 4. Amazon MSK

The application uses Apache Kafka for event-driven processing.

Important events such as order and complaint events can be published to Kafka.

Amazon MSK provides a managed Kafka environment for running these event streams on AWS.

### 5. Amazon CloudWatch

CloudWatch is used for monitoring application activity and collecting logs.

This helps identify errors and understand application behavior in a cloud environment.

### 6. AWS IAM

IAM is used to control access to AWS resources.

Only the required permissions should be given to the application and deployment users.

### 7. AWS CLI

AWS CLI is used to interact with AWS resources from the command line and assist with deployment and management.

## 🔄 How the Application Works

### Order Flow

```text
Customer
   |
   v
Login with JWT
   |
   v
Create Order
   |
   v
Check Inventory
   |
   v
Reserve Stock
   |
   v
Create Order
   |
   v
Publish Order Event to Kafka
```

### Delivery Flow

```text
Order
  |
  v
Shipment Created
  |
  v
OTP Generated
  |
  v
Customer Provides OTP
  |
  +---- Correct ----> Delivery Completed
  |
  +---- Wrong ------> Complaint Flow
```

### Seller Reliability

The system uses order outcomes and complaint information to calculate seller reliability metrics.

This helps the platform track seller performance using actual order and complaint data.

## ⭐ Key Features

* JWT-based authentication
* Role-based authorization
* Customer and seller management
* Inventory management
* Transactional order creation
* Shipment management
* OTP-based delivery verification
* Complaint management
* Seller reliability tracking
* Redis caching
* Kafka event processing
* AWS cloud deployment
* Cloud monitoring with CloudWatch

## 🎯 Why I Built This

I wanted to build more than a basic CRUD application.

E-commerce systems have real backend challenges such as inventory consistency, security, delivery verification, caching, asynchronous processing, and failure handling.

This project allowed me to understand how these concepts can work together in a real-world backend system.

## 💡 Impact

The platform is designed to:

* Reduce the risk of inventory overselling
* Improve delivery verification
* Make frequently accessed data faster
* Separate asynchronous processing from the main request flow
* Track seller reliability
* Improve visibility through application monitoring

## 👨‍💻 My Contribution

I designed and developed the backend of the platform.

My work includes:

* Designing the database entities and relationships
* Developing REST APIs using Spring Boot
* Implementing JWT authentication and authorization
* Implementing order and inventory workflows
* Implementing shipment OTP verification
* Creating the complaint workflow
* Integrating Redis caching
* Integrating Kafka event processing
* Preparing the application for AWS deployment

## 🏗️ AWS Open Source Stack

The project uses AWS-managed infrastructure around open-source technologies:

| Open Source Technology | AWS Service        |
| ---------------------- | ------------------ |
| MySQL                  | Amazon RDS         |
| Redis                  | Amazon ElastiCache |
| Apache Kafka           | Amazon MSK         |
| Java / Spring Boot     | Amazon EC2         |
| Application monitoring | Amazon CloudWatch  |

AWS provides managed services for several popular open-source technologies, including MySQL, Redis, and Apache Kafka.

## 🔮 Future Improvements

* Add automated CI/CD
* Add more automated tests
* Add detailed application metrics
* Add distributed tracing
* Improve failure recovery
* Add load testing
* Deploy additional components using AWS managed services
* Improve scalability for high traffic

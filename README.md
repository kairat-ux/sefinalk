# Luxury Reservations - Restaurant Booking System

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue)
![Java](https://img.shields.io/badge/Java-17+-orange)
![License](https://img.shields.io/badge/License-MIT-green)

## 📋 Project Overview

**Luxury Reservations** is a comprehensive restaurant table booking system. The application allows customers to search for restaurants, make reservations, leave reviews, and process payments. Restaurant owners can manage their establishments, tables, and bookings. Administrators have full system control.

### Key Features:
- 🔐 JWT Authentication and Authorization
- 👥 Role-Based Access Control (ADMIN, USER, OWNER)
- 🍽️ Restaurant Search and Discovery
- 📅 Table Reservations for Future Dates
- 💳 Payment Processing with Refunds
- ⭐ Review System with Admin Moderation
- 🔔 User Notifications
- 📊 Multi-language Support (Russian, Kazakh, English)

---

## 🏗️ System Architecture

### Application Layers:

```
┌─────────────────────────────────────┐
│         Controllers (API)            │
├─────────────────────────────────────┤
│         Services (Business Logic)    │
├─────────────────────────────────────┤
│    Repositories (Data Access)       │
├─────────────────────────────────────┤
│      Database (PostgreSQL)          │
└─────────────────────────────────────┘
```

### Components:

- **Controllers** - REST endpoints without business logic
- **Services** - All business logic and processing
- **Repositories** - Database operations via Spring Data JPA
- **Entities** - JPA entities for database tables
- **DTOs** - Data Transfer Objects for API requests/responses
- **Security** - Spring Security + JWT authentication

---

## 📊 Database Schema

### Main Tables:

```sql
users              -- Users (ADMIN, USER, OWNER)
restaurants        -- Restaurant information
restaurant_tables  -- Tables in restaurants
reservations       -- Customer reservations
payments           -- Payment records
reviews            -- Restaurant reviews
notifications      -- User notifications
working_hours      -- Restaurant operating hours
time_slots         -- Available time slots
cuisines           -- Types of cuisine
promotions         -- Discounts and promotions
restaurant_images  -- Restaurant photos
```

**Total Entities: 12** (minimum required: 5)

---

## 🚀 Quick Start

### Requirements:

- Java 17+
- PostgreSQL 12+
- Gradle 8.5+
- Git

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/luxury-reservations.git
cd luxury-reservations
```

### Step 2: Database Setup

#### Create Database in PostgreSQL:

```sql
CREATE DATABASE restaurant_booking;
```

#### Configure `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/restaurant_booking
    username: postgres
    password: YOUR_PASSWORD
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.xml
    enabled: true
```

### Step 3: Run the Application

```bash
# Clean and build
./gradlew clean build

# Run the server
./gradlew bootRun
```

The application will be available at: **http://localhost:8008**

### Step 4: API Context

All API requests should be sent to: **http://localhost:8008/api**

---

## 🔐 Security & Authentication

### Roles:

| Role | Description | Access |
|------|-------------|--------|
| **ADMIN** | System Administrator | User management, restaurant moderation, review approval |
| **USER** | Regular Customer | Restaurant search, bookings, reviews, payments |
| **OWNER** | Restaurant Owner | Manage restaurant, tables, reservations |

### JWT Authentication:

All protected endpoints require Authorization header:

```bash
Authorization: Bearer YOUR_JWT_TOKEN
```

---

## 📚 API Endpoints

### Authentication

```bash
# User Registration
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+77001234567"
}

# User Login
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

# Response (201 Created)
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "USER",
    "createdAt": "2025-12-25T10:00:00"
  }
}
```

### Restaurants

```bash
# Get all restaurants
GET /api/restaurants

# Get restaurant by ID
GET /api/restaurants/{id}

# Get restaurants by city
GET /api/restaurants/city/{city}

# Create restaurant (OWNER)
POST /api/restaurants
Authorization: Bearer TOKEN
Content-Type: application/json

{
  "name": "Restaurant Name",
  "description": "Description",
  "address": "Street Address",
  "city": "City",
  "phone": "+77001234567",
  "email": "restaurant@example.com"
}
```

### Reservations

```bash
# Create reservation (USER)
POST /api/reservations
Authorization: Bearer TOKEN
Content-Type: application/json

{
  "restaurantId": 1,
  "reservationDate": "2025-12-30",
  "startTime": "19:00:00",
  "endTime": "21:00:00",
  "guestCount": 4,
  "specialRequests": "Window seat preferred"
}

# Get user reservations
GET /api/reservations/user/{userId}
Authorization: Bearer TOKEN

# Cancel reservation
DELETE /api/reservations/{id}?reason=User%20request
Authorization: Bearer TOKEN
```

### Payments

```bash
# Create payment
POST /api/payments
Authorization: Bearer TOKEN
Content-Type: application/json

{
  "reservationId": 1,
  "amount": 15000.00,
  "paymentMethod": "CARD"
}

# Complete payment
PUT /api/payments/{id}/complete
Authorization: Bearer TOKEN

# Refund payment
PUT /api/payments/{id}/refund?reason=Refund%20reason
Authorization: Bearer TOKEN
```

### Reviews

```bash
# Create review
POST /api/reviews
Authorization: Bearer TOKEN
Content-Type: application/json

{
  "reservationId": 1,
  "rating": 5,
  "comment": "Excellent service and delicious food!"
}

# Get restaurant reviews
GET /api/reviews/restaurant/{restaurantId}

# Approve review (ADMIN)
PUT /api/reviews/{id}/approve
Authorization: Bearer ADMIN_TOKEN

# Reject review (ADMIN)
PUT /api/reviews/{id}/reject
Authorization: Bearer ADMIN_TOKEN
```

---

## 📋 Usage Examples

### Example 1: Complete Booking Workflow

```bash
# 1. Register
curl -X POST http://localhost:8008/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'

# 2. Login and get token
curl -X POST http://localhost:8008/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'

# Save token from response

# 3. Search restaurants
curl -X GET http://localhost:8008/api/restaurants \
  -H "Content-Type: application/json"

# 4. Make reservation
curl -X POST http://localhost:8008/api/reservations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "restaurantId": 1,
    "reservationDate": "2025-12-30",
    "startTime": "19:00:00",
    "endTime": "21:00:00",
    "guestCount": 4
  }'

# 5. Process payment
curl -X POST http://localhost:8008/api/payments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "reservationId": 1,
    "amount": 15000,
    "paymentMethod": "CARD"
  }'

# 6. Leave review
curl -X POST http://localhost:8008/api/reviews \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "reservationId": 1,
    "rating": 5,
    "comment": "Excellent experience!"
  }'
```

---

## 📧 Test Accounts

### Administrator:
- **Email:** admin@restaurant.kz
- **Password:** admin123

### Restaurant Owners:
1. azamat.nurlan@gmail.com / owner123
2. dana.suleimenova@gmail.com / owner123
3. murat.kairatov@gmail.com / owner123

### Regular Users:
1. timur.zhanibek@mail.ru / user123
2. aruzhan.tastanova@mail.ru / user123

### Test Restaurants:
- Dastarkhan (ID: 1) - Almaty - Kazakh Cuisine
- Silk Road (ID: 2) - Almaty - Uzbek Cuisine
- Bellissimo (ID: 3) - Almaty - Italian Cuisine
- Baiterek Grill (ID: 4) - Astana - Caucasian Cuisine
- Tokyo Garden (ID: 5) - Astana - Japanese Cuisine
- Arman (ID: 6) - Shymkent - Mixed Cuisine

---

## 🧪 Unit Testing

The project includes unit tests for all services:

```bash
# Run all tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport
```

### Test Classes:
- `UserServiceTest` - Registration and profile tests
- `ReservationServiceTest` - Reservation logic tests
- `PaymentServiceTest` - Payment processing tests
- `ReviewServiceTest` - Review system tests

---

## 📦 Database Migrations (Liquibase)

All migrations are located in: `src/main/resources/db/changelog/migrations/`

```
001-create-users-table.xml
002-create-restaurants-table.xml
003-create-restaurant-tables-table.xml
004-create-reservations-table.xml
005-create-reviews-table.xml
006-create-payments-table.xml
007-create-time-slots-table.xml
008-create-working-hours-table.xml
009-create-restaurant-images-table.xml
010-create-notifications-table.xml
011-create-cuisines-table.xml
012-create-promotions-table.xml
013-create-indexes.xml
014-insert-base-data.xml
015-insert-test-tables.xml
```

Migrations are automatically executed on application startup.

---

## 📮 Postman Collection

Download `Luxury_Reservations.postman_collection.json` and import it into Postman:

1. Open Postman
2. Click **Import**
3. Select the collection file
4. All endpoints are ready to use

---

## 🏢 Project Structure

```
src/main/java/com/example/demo/
├── controller/               # REST controllers
│   ├── AuthController
│   ├── UserController
│   ├── RestaurantController
│   ├── ReservationController
│   ├── PaymentController
│   ├── ReviewController
│   └── NotificationController
├── service/                  # Business logic
│   ├── interfaces/
│   │   ├── UserService
│   │   ├── RestaurantService
│   │   ├── ReservationService
│   │   ├── PaymentService
│   │   ├── ReviewService
│   │   └── NotificationService
│   └── impl/
│       ├── UserServiceImpl
│       ├── RestaurantServiceImpl
│       ├── ReservationServiceImpl
│       ├── PaymentServiceImpl
│       ├── ReviewServiceImpl
│       └── NotificationServiceImpl
├── repository/               # Data access layer
│   ├── UserRepository
│   ├── RestaurantRepository
│   ├── ReservationRepository
│   ├── PaymentRepository
│   ├── ReviewRepository
│   └── NotificationRepository
├── entity/                   # JPA entities
│   ├── User
│   ├── Restaurant
│   ├── RestaurantTable
│   ├── Reservation
│   ├── Payment
│   ├── Review
│   ├── Notification
│   └── ...
├── dto/                      # Data transfer objects
│   ├── request/
│   │   ├── UserRegistrationRequestDTO
│   │   ├── ReservationCreateRequestDTO
│   │   ├── PaymentCreateRequestDTO
│   │   ├── ReviewCreateRequestDTO
│   │   └── ...
│   └── response/
│       ├── UserResponseDTO
│       ├── ReservationResponseDTO
│       ├── PaymentResponseDTO
│       ├── ReviewResponseDTO
│       └── ...
├── security/                 # JWT & Security
│   ├── JwtTokenProvider
│   ├── JwtAuthenticationFilter
│   ├── UserPrincipal
│   └── SecurityConfig
├── config/                   # Configuration
│   ├── SecurityConfig
│   ├── WebConfig
│   └── JwtConfig
└── exception/                # Custom exceptions
    ├── TableNotAvailableException
    ├── ReservationNotFoundException
    └── ...

src/main/resources/
├── application.yml           # Main configuration
├── application-dev.yml       # Development profile
├── application-prod.yml      # Production profile
├── application-test.yml      # Test profile
├── db/changelog/             # Liquibase migrations
│   ├── db.changelog-master.xml
│   └── migrations/
│       ├── 001-*.xml
│       ├── 002-*.xml
│       └── ...
└── messages/                 # Internationalization
    ├── messages.properties    # English
    ├── messages_ru.properties # Russian
    └── messages_kk.properties # Kazakh

tests/
└── service/                  # Unit tests
    ├── UserServiceTest
    ├── ReservationServiceTest
    ├── PaymentServiceTest
    └── ReviewServiceTest
```

---

## 🔧 Configuration

### Environment Variables:

```bash
# Server port
SERVER_PORT=8008

# Database
DB_URL=jdbc:postgresql://localhost:5432/restaurant_booking
DB_USER=postgres
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_secret_key
JWT_EXPIRATION_MS=86400000

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_EXAMPLE_DEMO=DEBUG
```

---

## 📝 Logging

Logging is configured in `application.yml`:

```yaml
logging:
  level:
    root: INFO
    com.example.demo: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
```

Logs are output to console and saved to `logs/application.log`

---

## 🐛 Troubleshooting

### Error: "Cannot connect to PostgreSQL"

Solution:
1. Ensure PostgreSQL is running
2. Check credentials in application.yml
3. Verify database exists

```bash
sudo service postgresql start
```

### Error: "Migration failed"

Solution:
1. Clean build cache
2. Recreate the database
3. Run clean build

```bash
rm -rf .gradle/ build/
./gradlew clean build
```

### Error: "401 Unauthorized" on reservations

Solution:
1. Verify you are logged in
2. Check token is in Authorization header
3. Ensure reservation date is in future
4. Verify restaurant and table exist

---

## 🚢 Deployment

### Docker (Optional):

```dockerfile
FROM openjdk:17-jdk-slim
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
docker build -t luxury-reservations .
docker run -p 8008:8008 \
  -e DB_URL=jdbc:postgresql://db:5432/restaurant_booking \
  luxury-reservations
```

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details

---

## 👥 Author

- **Kairat Zhaksybek** - Developer
- **Email:** kairat@example.com
- **GitHub:** https://github.com/yourusername

---

## 🎓 Project Submission

This README includes:
- ✅ Complete project description
- ✅ System architecture
- ✅ Setup instructions
- ✅ API documentation with examples
- ✅ Project structure
- ✅ Testing information
- ✅ Database migrations
- ✅ Troubleshooting guide
- ✅ Test accounts and data

**Ready for presentation!** 🚀

---

## 📞 Support

For issues, questions, or suggestions, please open an issue on GitHub or contact the developer.

---

**Last Updated:** December 25, 2025
**Version:** 1.0.0
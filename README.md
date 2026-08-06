# Patient Management System — Full-Stack Microservices

A complete hospital management system built with **Spring Boot microservices** (Java 21) and **React** (Vite). Features JWT authentication, service discovery, API gateway, and role-based access control for 8 user types.

## 🏗️ Architecture

### Backend Microservices (Spring Boot 3.2.3 + Spring Cloud 2023.0.0)
- **service-registry** (8761) — Eureka Server for service discovery
- **api-gateway** (8080) — Spring Cloud Gateway with JWT verification, rate limiting, circuit breakers
- **auth-service** (8081) — Authentication, JWT issuing, BCrypt password hashing
- **patient-service** (8082) — Patient profiles, medical history (PostgreSQL)
- **doctor-service** (8083) — Doctor schedules, prescriptions, appointments (PostgreSQL)
- **lab-service** (8084) — X-Ray/Blood/Sugar test workflows with JSONB diagnostic payloads (PostgreSQL)
- **pharmacy-service** (8085) — Drug inventory, prescription fulfillment, billing (PostgreSQL)

### Frontend (React 19 + Vite 8 + Tailwind CSS)
- **Role-based dashboards**: Admin, Doctor, Patient, Receptionist, Pharmacy, Lab (X-Ray, Blood, Sugar)
- **JWT authentication** with localStorage persistence
- **Protected routes** with role validation
- **Responsive UI** with Lucide icons

### Infrastructure
- **PostgreSQL** — 5 isolated databases (Database-Per-Service pattern)
- **Docker Compose** — One-command orchestration
- **Flyway** — Database versioning and migrations

---

## 🚀 Quick Start

### Prerequisites
- **Docker** + **Docker Compose** (recommended) **OR**
- **Java 21**, **Maven 3.9+**, **Node 20+**, **PostgreSQL 16**

### Option 1: Docker Compose (Recommended)

```bash
# Clone and navigate
cd Patient_Management_System

# Start all services (backend + frontend + postgres)
docker-compose up --build

# Wait 2-3 minutes for all services to start
# Monitor logs: docker-compose logs -f
```

**Access Points:**
- Frontend: http://localhost:5173
- API Gateway: http://localhost:8080
- Eureka Dashboard: http://localhost:8761
- PostgreSQL: localhost:5432 (user: postgres, password: postgres)

**Default Login:**
- Email: `admin@hospital.com`
- Password: `Admin@123`
- Role: ADMIN (can access all portals)

### Option 2: Local Development

#### 1. Start PostgreSQL
```bash
# Create 5 databases
createdb pms_auth_db
createdb pms_patient_db
createdb pms_doctor_db
createdb pms_lab_db
createdb pms_pharmacy_db
```

#### 2. Start Backend Services (in order)
```bash
cd backend

# 1. Service Registry (wait for startup)
cd service-registry && mvn spring-boot:run &

# 2. API Gateway
cd ../api-gateway && mvn spring-boot:run &

# 3. Auth Service
cd ../auth-service && mvn spring-boot:run &

# 4-7. Domain Services (parallel)
cd ../patient-service && mvn spring-boot:run &
cd ../doctor-service && mvn spring-boot:run &
cd ../lab-service && mvn spring-boot:run &
cd ../pharmacy-service && mvn spring-boot:run &
```

#### 3. Start Frontend
```bash
cd Frontend
npm install
npm run dev
```

Visit http://localhost:5173

---

## 📊 System Roles & Dashboards

| Role          | Email Pattern         | Dashboard Features |
|---------------|-----------------------|--------------------|
| ADMIN         | admin@hospital.com    | System overview, user management, service health monitoring |
| DOCTOR        | doctor@hospital.com   | Appointments, prescriptions, patient records, consultation notes |
| PATIENT       | patient@hospital.com  | View appointments, medical history, prescriptions, lab reports |
| RECEPTIONIST  | reception@hospital.com| Patient registration, appointment scheduling, token management |
| PHARMACY      | pharmacy@hospital.com | Drug inventory, prescription fulfillment, low-stock alerts, billing |
| LAB_XRAY      | xray@hospital.com     | X-Ray order queue, result upload with JSONB diagnostic data |
| LAB_BLOOD     | blood@hospital.com    | Blood test queue (CBC, LFT, RFT), haematology, biochemistry |
| LAB_SUGAR     | sugar@hospital.com    | Blood sugar tests (Fasting, PP, HbA1c, GTT), reference ranges |

---

## 🔑 API Endpoints

All requests go through **API Gateway** (port 8080) with JWT bearer token (except login/register).

### Auth Service
```
POST   /api/v1/auth/register
POST   /api/v1/auth/login
GET    /api/v1/auth/validate?token={jwt}
```

### Patient Service
```
POST   /api/v1/patients
GET    /api/v1/patients/{id}
GET    /api/v1/patients/by-auth/{authUserId}
POST   /api/v1/patients/{patientId}/medical-records
GET    /api/v1/patients/{patientId}/medical-records
```

### Doctor Service
```
POST   /api/v1/doctors
GET    /api/v1/doctors/{id}
GET    /api/v1/doctors/specialization/{spec}
POST   /api/v1/doctors/appointments
PATCH  /api/v1/doctors/appointments/{id}/status
POST   /api/v1/prescriptions
GET    /api/v1/prescriptions/patient/{patientId}
```

### Lab Service
```
POST   /api/v1/lab/orders
GET    /api/v1/lab/orders/type/{XRAY|BLOOD|SUGAR}
PUT    /api/v1/lab/orders/{id}/results
PATCH  /api/v1/lab/orders/{id}/assign?technicianName={name}
GET    /api/v1/lab/stats
```

### Pharmacy Service
```
POST   /api/v1/pharmacy/drugs
GET    /api/v1/pharmacy/drugs/low-stock
GET    /api/v1/pharmacy/drugs/expiring?daysAhead=30
POST   /api/v1/pharmacy/dispense
GET    /api/v1/pharmacy/dispense/patient/{patientId}
GET    /api/v1/pharmacy/stats
```

---

## 🗄️ Database Schema Highlights

### Auth Service (pms_auth_db)
- **users** — email, password (BCrypt), role enum, isActive

### Patient Service (pms_patient_db)
- **patients** — demographics, bloodGroup, allergies, chronicConditions
- **medical_records** — diagnosis, symptoms, treatment, doctorId

### Doctor Service (pms_doctor_db)
- **doctors** — specialization enum, consultationFee, experienceYears
- **appointments** — status enum (SCHEDULED, IN_PROGRESS, COMPLETED), consultationNotes
- **prescriptions** — status enum (ISSUED, SENT_TO_PHARMACY, DISPENSED)
- **prescription_items** — medicineName, dosage, frequency, duration

### Lab Service (pms_lab_db)
- **lab_orders** — labType enum (XRAY, BLOOD, SUGAR), priority enum (ROUTINE, URGENT, STAT)
- **diagnostic_payload** (JSONB) — flexible structure per lab type:
  - XRAY: `{ bodyPart, view, findings, impression }`
  - BLOOD: `{ CBC: {...}, LFT: {...}, RFT: {...} }`
  - SUGAR: `{ fasting, postPrandial, HbA1c, unit }`

### Pharmacy Service (pms_pharmacy_db)
- **drugs** — category enum (16 types), quantityInStock, reorderLevel, expiryDate, unitPrice
- **dispense_logs** — prescriptionId (unique), billingStatus enum, totalAmount
- **dispense_items** — drugId, quantityDispensed, subtotal

---

## 🛠️ Tech Stack

### Backend
- **Java 21** — Modern JDK with virtual threads support
- **Spring Boot 3.2.3** — REST APIs, JPA, Security
- **Spring Cloud 2023.0.0** — Gateway, Eureka, OpenFeign, Circuit Breaker (Resilience4j)
- **PostgreSQL 16** — Relational database with JSONB support
- **Flyway** — Database migration tool
- **JJWT 0.11.5** — JWT signing and validation (HS256)
- **Hypersistence Utils** — JSONB mapping for Hibernate
- **Lombok** — Boilerplate reduction

### Frontend
- **React 19** — UI library
- **Vite 8** — Build tool
- **React Router DOM 6** — Client-side routing
- **Axios** — HTTP client with interceptors
- **Tailwind CSS 4** — Utility-first styling
- **Lucide React** — Icon library

### DevOps
- **Docker** — Containerization
- **Docker Compose** — Multi-container orchestration
- **Maven** — Build automation
- **Nginx** — Production web server

---

## 🔐 Security

- **JWT Authentication** — HS256 tokens with 24-hour expiration
- **BCrypt Password Hashing** — 10 rounds (auth-service)
- **API Gateway JWT Filter** — Validates all requests, injects `X-User-Email` and `X-User-Role` headers
- **Role-Based Access Control** — Frontend + backend authorization
- **CORS** — Configured for localhost:5173 and localhost:3000
- **Database Isolation** — Each service owns its schema (Database-Per-Service)

---

## 📦 Project Structure

```
Patient_Management_System/
├── backend/
│   ├── pom.xml                     # Parent POM (Spring Boot 3.2.3, Spring Cloud 2023.0.0)
│   ├── service-registry/           # Eureka Server (8761)
│   ├── api-gateway/                # Spring Cloud Gateway (8080)
│   ├── auth-service/               # Authentication (8081)
│   ├── patient-service/            # Patient management (8082)
│   ├── doctor-service/             # Doctor management (8083)
│   ├── lab-service/                # Lab workflows (8084)
│   └── pharmacy-service/           # Pharmacy management (8085)
├── Frontend/
│   ├── src/
│   │   ├── features/               # 8 role-based dashboards
│   │   ├── context/AuthContext.jsx # Global auth state
│   │   ├── routes/ProtectedRoute.jsx # Role guard
│   │   └── services/api.js         # Axios instance
│   ├── Dockerfile
│   └── nginx.conf
├── scripts/
│   └── init-databases.sh           # PostgreSQL multi-DB init
├── docker-compose.yml
└── README.md
```

---

## 🧪 Testing

### Test Default Login
1. Navigate to http://localhost:5173/login
2. Select "Admin Portal"
3. Email: `admin@hospital.com`, Password: `Admin@123`
4. Access any department dashboard (Admin can view all)

### Create Test Users
Use the auth-service `/register` endpoint or SQL:
```sql
INSERT INTO users (full_name, email, password, role, is_active) VALUES
('Dr. John Smith', 'doctor@hospital.com', '$2a$10$...', 'DOCTOR', true);
```

---

## 📝 Development Notes

- **Flyway Migrations**: Located in each service at `src/main/resources/db/migration/V1__*.sql`
- **Eureka Dashboard**: http://localhost:8761 shows all registered services
- **Circuit Breaker**: Configured on API Gateway with fallback endpoints
- **Hot Reload**: Frontend (`npm run dev`), Backend (Spring Boot DevTools if enabled)

---

## 🐛 Troubleshooting

**Services not registering with Eureka?**
- Check Eureka dashboard at http://localhost:8761
- Verify `eureka.client.service-url.defaultZone` in application.yml

**Database connection errors?**
- Ensure PostgreSQL is running: `pg_isready`
- Check database exists: `psql -l`
- Verify credentials in application.properties/yml

**Frontend can't reach API?**
- Check API Gateway is running on port 8080
- Verify `baseURL` in `Frontend/src/services/api.js`
- Check browser console for CORS errors

**Docker build fails?**
- Increase Docker memory limit (Settings → Resources → Memory: 4GB+)
- Rebuild without cache: `docker-compose build --no-cache`

---

## 📄 License

This is a learning/portfolio project. Use freely.

---

## 👨‍💻 Author

Built as a demonstration of modern microservices architecture.

**Key Features Demonstrated:**
- Service discovery (Eureka)
- API Gateway pattern
- JWT authentication
- Database-per-service
- JSONB for flexible schema
- Circuit breakers
- Role-based access control
- Full-stack Docker deployment

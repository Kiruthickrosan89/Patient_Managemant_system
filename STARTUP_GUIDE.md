# 🚀 How to Run the Patient Management System

## Quick Start (Docker — Recommended)

```bash
cd /home/kiruthick/SpringBoot-projects/Patient_Management_System

# Start all services (backend + database + frontend)
docker-compose up --build

# Or run in detached mode (background)
docker-compose up --build -d

# Watch logs
docker-compose logs -f

# Watch specific service logs
docker-compose logs -f api-gateway
docker-compose logs -f auth-service
```

**First-time startup takes 3-5 minutes** (Maven downloads dependencies, builds JARs, Flyway migrations run).

### Access the System

Once all services show "Started" in logs:

- **Frontend (React)**: http://localhost:5173
- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **PostgreSQL**: localhost:5432 (user: `postgres`, password: `postgres`)

### Default Login Credentials

Navigate to http://localhost:5173/login

**Admin Account** (can access all portals):
- Email: `admin@hospital.com`
- Password: `Admin@123`
- Role: ADMIN

---

## Stop & Cleanup

```bash
# Stop all containers
docker-compose down

# Stop and remove volumes (deletes all database data)
docker-compose down -v

# Remove all built images
docker-compose down --rmi all
```

---

## Troubleshooting

### Check Service Status
```bash
docker-compose ps
```

You should see all 9 services running:
- `pms-postgres` (port 5432)
- `pms-service-registry` (port 8761)
- `pms-api-gateway` (port 8080)
- `pms-auth-service` (port 8081)
- `pms-patient-service` (port 8082)
- `pms-doctor-service` (port 8083)
- `pms-lab-service` (port 8084)
- `pms-pharmacy-service` (port 8085)
- `pms-frontend` (port 5173)

### Service Won't Start?

**Check logs for specific service:**
```bash
docker-compose logs auth-service
```

**Common issues:**

1. **Port already in use** — Stop other services:
   ```bash
   # Check what's using port 8080
   lsof -i :8080
   # Kill process
   kill -9 <PID>
   ```

2. **PostgreSQL connection error** — Wait for DB to be healthy:
   ```bash
   docker-compose ps postgres
   # Should show "healthy" status
   ```

3. **Flyway migration failed** — Reset database:
   ```bash
   docker-compose down -v
   docker-compose up postgres -d
   # Wait 10 seconds, then start services
   docker-compose up --build
   ```

4. **Maven build errors** — Clear Docker cache:
   ```bash
   docker-compose build --no-cache service-registry
   docker-compose up service-registry
   ```

### Rebuild Single Service

```bash
# Rebuild and restart just one service
docker-compose up --build auth-service

# Or rebuild without starting
docker-compose build auth-service
```

---

## Alternative: Local Development (Without Docker)

### Prerequisites
- Java 21 (check: `java -version`)
- Maven 3.9+ (check: `mvn -version`)
- PostgreSQL 16+ running locally
- Node.js 20+ (check: `node -version`)

### Step 1: Create Databases

```bash
# Connect to PostgreSQL
psql -U postgres

# Create databases
CREATE DATABASE pms_auth_db;
CREATE DATABASE pms_patient_db;
CREATE DATABASE pms_doctor_db;
CREATE DATABASE pms_lab_db;
CREATE DATABASE pms_pharmacy_db;

# Exit
\q
```

Or use the script:
```bash
psql -U postgres -f scripts/create-databases.sql
```

### Step 2: Start Backend Services (In Order)

**Terminal 1 — Service Registry:**
```bash
cd backend/service-registry
mvn spring-boot:run
```
Wait until you see: `Started ServiceRegistryApplication`

**Terminal 2 — API Gateway:**
```bash
cd backend/api-gateway
mvn spring-boot:run
```

**Terminal 3 — Auth Service:**
```bash
cd backend/auth-service
mvn spring-boot:run
```

**Terminal 4-7 — Domain Services (can run in parallel):**
```bash
cd backend/patient-service && mvn spring-boot:run &
cd backend/doctor-service && mvn spring-boot:run &
cd backend/lab-service && mvn spring-boot:run &
cd backend/pharmacy-service && mvn spring-boot:run &
```

### Step 3: Start Frontend

```bash
cd Frontend
npm install  # First time only
npm run dev
```

Access: http://localhost:5173

---

## Database Details

### PostgreSQL Setup

**Single PostgreSQL Instance** running **5 isolated databases** (Database-Per-Service pattern).

| Service           | Database Name      | Port | Tables                                          |
|-------------------|--------------------|------|-------------------------------------------------|
| auth-service      | pms_auth_db        | 5432 | users                                           |
| patient-service   | pms_patient_db     | 5432 | patients, medical_records                       |
| doctor-service    | pms_doctor_db      | 5432 | doctors, appointments, prescriptions, prescription_items |
| lab-service       | pms_lab_db         | 5432 | lab_orders                                      |
| pharmacy-service  | pms_pharmacy_db    | 5432 | drugs, dispense_logs, dispense_items            |

**Connection Details:**
- Host: `localhost` (or `postgres` in Docker network)
- Port: `5432`
- User: `postgres`
- Password: `postgres`

### Database Initialization

**Automated via Docker Compose:**
1. PostgreSQL container starts
2. `scripts/init-databases.sh` runs (creates 5 databases)
3. Each Spring Boot service starts
4. **Flyway migrations** run automatically (located at `src/main/resources/db/migration/V1__*.sql`)
5. Seed data inserted (e.g., admin user in auth_db, sample drugs in pharmacy_db)

### Flyway Migrations Summary

**auth-service:** `V1__init_auth_schema.sql`
```sql
-- Creates users table
-- Columns: id, full_name, email, password (BCrypt), role (enum), is_active, created_at
-- Seeds: admin@hospital.com / Admin@123 (ADMIN role)
```

**patient-service:** `V1__init_patient_schema.sql`
```sql
-- Creates patients table (demographics, blood_group, allergies, chronic_conditions)
-- Creates medical_records table (diagnosis, symptoms, treatment, visit_date, doctor_id)
-- Indexes: email, auth_user_id, patient_id, doctor_id
```

**doctor-service:** `V1__init_doctor_schema.sql`
```sql
-- Creates doctors table (specialization enum, consultation_fee, experience_years)
-- Creates appointments table (scheduled_at, status enum, consultation_notes)
-- Creates prescriptions table (status enum, diagnosis, instructions)
-- Creates prescription_items table (medicine_name, dosage, frequency, duration)
-- Indexes: email, auth_user_id, specialization, patient_id, doctor_id, status
```

**lab-service:** `V1__init_lab_schema.sql`
```sql
-- Creates lab_orders table with JSONB diagnostic_payload column
-- Columns: lab_type (XRAY/BLOOD/SUGAR), priority (ROUTINE/URGENT/STAT), status
-- GIN index on diagnostic_payload for fast JSONB queries
-- Indexes: patient_id, doctor_id, lab_type, status, (lab_type + status)
```

**pharmacy-service:** `V1__init_pharmacy_schema.sql`
```sql
-- Creates drugs table (category enum, quantity_in_stock, reorder_level, expiry_date, unit_price)
-- Creates dispense_logs table (prescription_id unique, billing_status, total_amount)
-- Creates dispense_items table (drug_id, quantity_dispensed, subtotal)
-- Seeds: 5 common drugs (Paracetamol, Amoxicillin, Metformin, Amlodipine, Cetirizine)
-- Indexes: drug name, category, expiry, patient_id, billing_status
```

---

## Connect to Database (Inspect Data)

### Using Docker

```bash
# Connect to running PostgreSQL container
docker exec -it pms-postgres psql -U postgres

# Or connect to specific database
docker exec -it pms-postgres psql -U postgres -d pms_auth_db
```

### Using psql (Local)

```bash
# Connect to auth database
psql -U postgres -d pms_auth_db

# List all databases
\l

# Switch database
\c pms_patient_db

# List tables
\dt

# View users table
SELECT * FROM users;

# Exit
\q
```

### Using GUI Tool

**DBeaver / pgAdmin / DataGrip:**
- Host: `localhost`
- Port: `5432`
- Database: `pms_auth_db` (repeat for each DB)
- Username: `postgres`
- Password: `postgres`

---

## Test the System

### 1. Login as Admin
- Go to http://localhost:5173/login
- Select "Admin Portal"
- Email: `admin@hospital.com`
- Password: `Admin@123`
- You'll see Admin Dashboard

### 2. Create a Doctor Account

**Option A — Via API (Postman / cURL):**
```bash
# First, login to get JWT token
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@hospital.com",
    "password": "Admin@123"
  }'

# Copy the "token" from response

# Register a doctor
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Dr. Priya Sharma",
    "email": "priya@hospital.com",
    "password": "Doctor@123",
    "role": "DOCTOR"
  }'

# Create doctor profile
curl -X POST http://localhost:8080/api/v1/doctors \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "fullName": "Dr. Priya Sharma",
    "email": "priya@hospital.com",
    "phone": "+919876543210",
    "specialization": "CARDIOLOGY",
    "qualification": "MBBS, MD (Cardiology)",
    "experienceYears": 12,
    "consultationFee": 800.00
  }'
```

**Option B — Direct SQL:**
```bash
docker exec -it pms-postgres psql -U postgres -d pms_auth_db

INSERT INTO users (full_name, email, password, role, is_active) VALUES
('Dr. Priya Sharma', 'priya@hospital.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'DOCTOR', true);
```

### 3. Login as Doctor
- Logout from Admin
- Select "Doctor Portal"
- Email: `priya@hospital.com`
- Password: `Doctor@123`
- You'll see Doctor Dashboard

### 4. Test Other Roles

Create users with these roles:
- `PATIENT` → Patient Dashboard
- `RECEPTIONIST` → Reception Dashboard
- `PHARMACY` → Pharmacy Dashboard
- `LAB_XRAY` → X-Ray Lab Dashboard
- `LAB_BLOOD` → Blood Lab Dashboard
- `LAB_SUGAR` → Sugar Lab Dashboard

---

## Production Deployment Notes

### Environment Variables Override

Update `docker-compose.yml` or create `.env` file:

```env
# Database
POSTGRES_USER=produser
POSTGRES_PASSWORD=strongpassword123

# JWT
JWT_SECRET=your-256-bit-secret-key-here-change-this-in-production
JWT_EXPIRATION=3600000

# Frontend API URL
REACT_APP_API_URL=https://api.yourdomain.com
```

### Security Checklist

- [ ] Change PostgreSQL credentials
- [ ] Generate new JWT secret (256-bit)
- [ ] Enable HTTPS (add reverse proxy like Nginx/Traefik)
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` (never `create-drop` in prod)
- [ ] Enable Spring Security CSRF for non-JWT endpoints
- [ ] Add rate limiting to API Gateway
- [ ] Set up database backups
- [ ] Configure monitoring (Prometheus + Grafana)
- [ ] Enable Spring Boot Actuator security
- [ ] Review CORS allowed origins

---

## Development Tips

### Hot Reload

**Backend:**
- Add Spring Boot DevTools to `pom.xml`
- Changes to Java files auto-restart the service

**Frontend:**
```bash
npm run dev
# Vite hot-reloads on file changes
```

### View Eureka Dashboard

http://localhost:8761

Shows all registered services:
- API-GATEWAY
- AUTH-SERVICE
- PATIENT-SERVICE
- DOCTOR-SERVICE
- LAB-SERVICE
- PHARMACY-SERVICE

### API Gateway Routes

All configured in `backend/api-gateway/src/main/resources/application.yml`:

```yaml
routes:
  - /api/v1/auth/**       → auth-service (port 8081)
  - /api/v1/patients/**   → patient-service (port 8082)
  - /api/v1/doctors/**    → doctor-service (port 8083)
  - /api/v1/lab/**        → lab-service (port 8084)
  - /api/v1/pharmacy/**   → pharmacy-service (port 8085)
```

### Check Service Health

```bash
# All services expose /actuator/health
curl http://localhost:8080/actuator/health  # API Gateway
curl http://localhost:8081/actuator/health  # Auth Service
curl http://localhost:8082/actuator/health  # Patient Service
# ... etc
```

---

## Backup & Restore Database

### Backup All Databases

```bash
docker exec pms-postgres pg_dumpall -U postgres > backup.sql
```

### Backup Single Database

```bash
docker exec pms-postgres pg_dump -U postgres pms_auth_db > auth_backup.sql
```

### Restore Database

```bash
cat backup.sql | docker exec -i pms-postgres psql -U postgres
```

---

## Next Steps

1. **Add More Seed Data** — Insert sample patients, doctors, appointments
2. **Implement Missing Features** — Patient registration form, appointment booking UI
3. **Add Tests** — JUnit for backend, Jest for frontend
4. **Set up CI/CD** — GitHub Actions, Jenkins
5. **Add Monitoring** — Spring Boot Admin, Prometheus
6. **Enable Distributed Tracing** — Spring Cloud Sleuth + Zipkin
7. **Add Message Queue** — RabbitMQ or Kafka for async events (e.g., prescription → pharmacy notification)

---

## Support

**Check logs if something breaks:**
```bash
docker-compose logs -f <service-name>
```

**Reset everything:**
```bash
docker-compose down -v
docker-compose up --build
```

**Clean Docker cache:**
```bash
docker system prune -a --volumes
```

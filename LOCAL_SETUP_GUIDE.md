# 🏠 Local Setup Guide — Running Without Docker

This guide shows you how to run the Patient Management System on your local machine **without Docker**, using PostgreSQL + JPA entity auto-creation.

---

## ✅ What Changed

**Flyway Removed:**
- Removed `flyway-core` dependency from all `pom.xml` files
- Deleted all `db/migration/*.sql` files
- Changed `ddl-auto` from `validate` → `update`

**Now Using:**
- **JPA/Hibernate DDL Auto** — Tables are created automatically from `@Entity` classes
- **PostgreSQL Driver** — JDBC connection to local PostgreSQL
- **`ddl-auto=update`** — Hibernate creates/updates tables on startup

---

## 📋 Prerequisites

Install these on your local Linux machine:

### 1. Java 21
```bash
java -version
# Should show: openjdk version "21"
```

If not installed:
```bash
sudo apt update
sudo apt install openjdk-21-jdk -y
```

### 2. Maven 3.9+
```bash
mvn -version
# Should show: Apache Maven 3.9.x
```

If not installed:
```bash
sudo apt install maven -y
```

### 3. PostgreSQL 16
```bash
psql --version
# Should show: psql (PostgreSQL) 16.x
```

If not installed:
```bash
sudo apt install postgresql postgresql-contrib -y
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### 4. Node.js 20+
```bash
node -version
# Should show: v20.x
```

If not installed:
```bash
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install nodejs -y
```

---

## 🗄️ Step 1: Create PostgreSQL Databases

```bash
# Switch to postgres user
sudo -i -u postgres

# Open PostgreSQL shell
psql

# Create 5 databases (one per microservice)
CREATE DATABASE pms_auth_db;
CREATE DATABASE pms_patient_db;
CREATE DATABASE pms_doctor_db;
CREATE DATABASE pms_lab_db;
CREATE DATABASE pms_pharmacy_db;

# Verify
\l

# Exit
\q
exit
```

**Alternative — Use your own user:**

```bash
# Create databases as your user (if you have superuser privileges)
createdb pms_auth_db
createdb pms_patient_db
createdb pms_doctor_db
createdb pms_lab_db
createdb pms_pharmacy_db
```

**Verify connection works:**
```bash
psql -U postgres -d pms_auth_db -c "SELECT version();"
```

---

## 🚀 Step 2: Start Backend Services

**Open 7 separate terminal windows** (or use `tmux`/`screen`).

### Terminal 1 — Service Registry (START FIRST)

```bash
cd /home/kiruthick/SpringBoot-projects/Patient_Management_System/backend/service-registry
mvn spring-boot:run
```

**Wait until you see:**
```
Started ServiceRegistryApplication in X.XXX seconds
```

Then open http://localhost:8761 — you should see Eureka dashboard.

---

### Terminal 2 — API Gateway (WAIT FOR EUREKA)

```bash
cd /home/kiruthick/SpringBoot-projects/Patient_Management_System/backend/api-gateway
mvn spring-boot:run
```

**Wait for:**
```
Started ApiGatewayApplication in X.XXX seconds
```

---

### Terminal 3 — Auth Service

```bash
cd /home/kiruthick/SpringBoot-projects/Patient_Management_System/backend/auth-service
mvn spring-boot:run
```

**Important:** On first run, Hibernate will create the `users` table automatically. You'll see SQL DDL logs:

```
Hibernate: create table users (...
```

**But the admin user won't be seeded!** (We removed Flyway seed). See Step 4 to create the admin user manually.

---

### Terminal 4 — Patient Service

```bash
cd /home/kiruthick/SpringBoot-projects/Patient_Management_System/backend/patient-service
mvn spring-boot:run
```

Tables `patients` and `medical_records` will be auto-created.

---

### Terminal 5 — Doctor Service

```bash
cd /home/kiruthick/SpringBoot-projects/Patient_Management_System/backend/doctor-service
mvn spring-boot:run
```

Tables `doctors`, `appointments`, `prescriptions`, `prescription_items` will be auto-created.

---

### Terminal 6 — Lab Service

```bash
cd /home/kiruthick/SpringBoot-projects/Patient_Management_System/backend/lab-service
mvn spring-boot:run
```

Table `lab_orders` with **JSONB `diagnostic_payload` column** will be auto-created.

---

### Terminal 7 — Pharmacy Service

```bash
cd /home/kiruthick/SpringBoot-projects/Patient_Management_System/backend/pharmacy-service
mvn spring-boot:run
```

Tables `drugs`, `dispense_logs`, `dispense_items` will be auto-created.

**But no seed drugs!** See Step 5 to add sample drugs.

---

## 🌐 Step 3: Start Frontend

### Terminal 8 — React Frontend

```bash
cd /home/kiruthick/SpringBoot-projects/Patient_Management_System/Frontend

# First time only — install dependencies
npm install

# Start Vite dev server
npm run dev
```

**Access the app:** http://localhost:5173

---

## 👤 Step 4: Create Admin User (Required!)

Since Flyway seeds are gone, you must manually create the admin user.

```bash
# Connect to auth database
psql -U postgres -d pms_auth_db

# Insert admin user (password: Admin@123)
INSERT INTO users (full_name, email, password, role, is_active, created_at) VALUES
('System Admin', 'admin@hospital.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', true, NOW());

# Verify
SELECT id, full_name, email, role FROM users;

# Exit
\q
```

**Now you can login:**
- URL: http://localhost:5173/login
- Email: `admin@hospital.com`
- Password: `Admin@123`

---

## 💊 Step 5: Seed Sample Drugs (Optional)

```bash
psql -U postgres -d pms_pharmacy_db
```

```sql
INSERT INTO drugs (name, generic_name, manufacturer, category, strength, dosage_form, quantity_in_stock, reorder_level, unit_price, expiry_date, batch_number, created_at, updated_at) VALUES
('Paracetamol', 'Acetaminophen', 'Sun Pharma', 'ANALGESIC', '500mg', 'Tablet', 500, 50, 2.50, '2027-12-31', 'PCM-001', NOW(), NOW()),
('Amoxicillin', 'Amoxicillin', 'Cipla', 'ANTIBIOTIC', '250mg', 'Capsule', 200, 30, 8.00, '2026-06-30', 'AMX-002', NOW(), NOW()),
('Metformin', 'Metformin HCl', 'Dr. Reddys', 'ANTIDIABETIC', '500mg', 'Tablet', 300, 40, 3.50, '2027-03-31', 'MET-003', NOW(), NOW()),
('Amlodipine', 'Amlodipine Besylate', 'Lupin', 'ANTIHYPERTENSIVE', '5mg', 'Tablet', 250, 30, 5.00, '2027-08-31', 'AML-004', NOW(), NOW()),
('Cetirizine', 'Cetirizine HCl', 'Mankind', 'ANTIHISTAMINE', '10mg', 'Tablet', 400, 50, 1.50, '2026-12-31', 'CTZ-005', NOW(), NOW());

SELECT id, name, strength, dosage_form, quantity_in_stock FROM drugs;
\q
```

---

## 🧪 Verify Everything Works

### 1. Check Eureka Dashboard
http://localhost:8761

You should see all 5 services registered:
- API-GATEWAY
- AUTH-SERVICE  
- PATIENT-SERVICE
- DOCTOR-SERVICE
- LAB-SERVICE
- PHARMACY-SERVICE

### 2. Check Service Health
```bash
curl http://localhost:8080/actuator/health  # API Gateway
curl http://localhost:8081/actuator/health  # Auth Service
curl http://localhost:8082/actuator/health  # Patient Service
```

### 3. Check Database Tables Created

```bash
# Auth DB
psql -U postgres -d pms_auth_db -c "\dt"
# Should show: users

# Patient DB
psql -U postgres -d pms_patient_db -c "\dt"
# Should show: patients, medical_records

# Doctor DB
psql -U postgres -d pms_doctor_db -c "\dt"
# Should show: doctors, appointments, prescriptions, prescription_items

# Lab DB
psql -U postgres -d pms_lab_db -c "\dt"
# Should show: lab_orders

# Pharmacy DB
psql -U postgres -d pms_pharmacy_db -c "\dt"
# Should show: drugs, dispense_logs, dispense_items
```

### 4. Test Login
1. Go to http://localhost:5173/login
2. Select "Admin Portal"
3. Email: `admin@hospital.com`
4. Password: `Admin@123`
5. You should see Admin Dashboard

---

## 🔧 Configuration Details

### Database Connections

All services point to `localhost:5432` with these credentials:

| Service           | Database         | User     | Password |
|-------------------|------------------|----------|----------|
| auth-service      | pms_auth_db      | postgres | postgres |
| patient-service   | pms_patient_db   | postgres | postgres |
| doctor-service    | pms_doctor_db    | postgres | postgres |
| lab-service       | pms_lab_db       | postgres | postgres |
| pharmacy-service  | pms_pharmacy_db  | postgres | postgres |

**Location:** Each service's `application.properties` or `application.yml`

Example from `auth-service/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pms_auth_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

## 📊 JPA Entity → Table Mapping

### Auth Service
```
@Entity User → table: users
  - id, full_name, email, password (BCrypt), role, is_active, created_at
```

### Patient Service
```
@Entity Patient → table: patients
  - id, auth_user_id, full_name, email, phone, date_of_birth, gender, 
    blood_group, address, allergies, chronic_conditions, is_active, created_at, updated_at

@Entity MedicalRecord → table: medical_records
  - id, patient_id (FK), doctor_id, doctor_name, diagnosis, symptoms, 
    treatment, notes, visit_date
```

### Doctor Service
```
@Entity Doctor → table: doctors
  - id, auth_user_id, full_name, email, phone, specialization, qualification,
    experience_years, consultation_fee, is_available, created_at, updated_at

@Entity Appointment → table: appointments
  - id, doctor_id (FK), patient_id, patient_name, scheduled_at, status, 
    reason_for_visit, consultation_notes, duration_minutes, created_at

@Entity Prescription → table: prescriptions
  - id, doctor_id (FK), patient_id, patient_name, appointment_id, status,
    diagnosis, instructions, issued_at

@Entity PrescriptionItem → table: prescription_items
  - id, prescription_id (FK), medicine_name, dosage, frequency, duration, instructions
```

### Lab Service
```
@Entity LabOrder → table: lab_orders
  - id, patient_id, patient_name, doctor_id, doctor_name, lab_type, test_name,
    priority, status, diagnostic_payload (JSONB), processed_by, remarks,
    ordered_at, completed_at
```

**JSONB Column Mapping:**
```java
@Type(JsonType.class)
@Column(columnDefinition = "jsonb")
private Map<String, Object> diagnosticPayload;
```

### Pharmacy Service
```
@Entity Drug → table: drugs
  - id, name, generic_name, manufacturer, category, strength, dosage_form,
    quantity_in_stock, reorder_level, unit_price, expiry_date, batch_number,
    created_at, updated_at

@Entity DispenseLog → table: dispense_logs
  - id, prescription_id (unique), patient_id, patient_name, doctor_id, doctor_name,
    status, billing_status, total_amount, dispensed_by, notes, created_at, dispensed_at

@Entity DispenseItem → table: dispense_items
  - id, dispense_log_id (FK), drug_id (FK), medicine_name, quantity_dispensed,
    unit_price, subtotal
```

---

## 🛠️ Troubleshooting

### Database Connection Refused

**Error:** `Connection to localhost:5432 refused`

**Fix:**
```bash
# Check PostgreSQL is running
sudo systemctl status postgresql

# If not running, start it
sudo systemctl start postgresql

# Check it's listening on port 5432
sudo netstat -plnt | grep 5432
```

### Authentication Failed for User

**Error:** `password authentication failed for user "postgres"`

**Fix — Reset postgres password:**
```bash
sudo -u postgres psql
ALTER USER postgres PASSWORD 'postgres';
\q
```

### Table Already Exists Error

**Error:** `relation "users" already exists`

**This happens if ddl-auto=create (drops and recreates tables on every restart).**

**Current setting is `update`** — safe, only adds columns, never drops.

If you want a fresh start:
```bash
# Drop and recreate all databases
psql -U postgres -c "DROP DATABASE pms_auth_db;"
psql -U postgres -c "CREATE DATABASE pms_auth_db;"
# Repeat for other 4 databases
```

### Port Already in Use

**Error:** `Port 8081 is already in use`

**Fix:**
```bash
# Find process using port
lsof -i :8081

# Kill it
kill -9 <PID>
```

### Eureka Client Cannot Register

**Error:** `Cannot execute request on any known server`

**Fix:** Make sure service-registry (Eureka) is running on port 8761 FIRST, then start other services.

### Frontend Cannot Reach API

**Error:** `Network Error` in browser console

**Fix:**
1. Check API Gateway is running on port 8080
2. Verify `Frontend/src/services/api.js` has correct baseURL:
   ```javascript
   baseURL: 'http://localhost:8080/api/v1'
   ```

---

## 🎯 Next Steps

### 1. Create More Users

```bash
psql -U postgres -d pms_auth_db
```

```sql
-- Create a doctor
INSERT INTO users (full_name, email, password, role, is_active, created_at) VALUES
('Dr. Priya Sharma', 'priya@hospital.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'DOCTOR', true, NOW());

-- Create a patient
INSERT INTO users (full_name, email, password, role, is_active, created_at) VALUES
('John Doe', 'john@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PATIENT', true, NOW());

-- Password for all: Admin@123
```

### 2. Stop All Services

Press `Ctrl + C` in each terminal to stop the Maven process.

Or if running in background:
```bash
# Find all Java processes
ps aux | grep java

# Kill specific service
pkill -f "auth-service"
```

---

## 📝 Summary

✅ **No Docker required** — Pure local setup  
✅ **No Flyway** — JPA creates tables from entities  
✅ **`ddl-auto=update`** — Safe, adds columns, never drops  
✅ **PostgreSQL on localhost:5432**  
✅ **5 separate databases** — Database-Per-Service pattern  
✅ **JSONB support** — Lab service diagnostic_payload column  
✅ **Manual seed data** — SQL inserts for admin user + drugs  

**To run:** Start 7 terminal windows (Eureka first, then others), then npm run dev for frontend.

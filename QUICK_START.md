# ⚡ Quick Start — Run Locally (No Docker)

## What I Changed For You

✅ **Removed Flyway** — No more migration files  
✅ **JPA Auto-Creates Tables** — `ddl-auto=update` creates tables from `@Entity` classes  
✅ **Local PostgreSQL** — All services point to `localhost:5432`  

---

## Run This Project

### 1. Create Databases

```bash
sudo -i -u postgres
psql
```

```sql
CREATE DATABASE pms_auth_db;
CREATE DATABASE pms_patient_db;
CREATE DATABASE pms_doctor_db;
CREATE DATABASE pms_lab_db;
CREATE DATABASE pms_pharmacy_db;
\q
exit
```

---

### 2. Start Backend (7 Terminals)

```bash
# Terminal 1 (START FIRST — wait until "Started ServiceRegistryApplication")
cd ~/SpringBoot-projects/Patient_Management_System/backend/service-registry && mvn spring-boot:run

# Terminal 2 (wait for Eureka)
cd ~/SpringBoot-projects/Patient_Management_System/backend/api-gateway && mvn spring-boot:run

# Terminal 3
cd ~/SpringBoot-projects/Patient_Management_System/backend/auth-service && mvn spring-boot:run

# Terminal 4
cd ~/SpringBoot-projects/Patient_Management_System/backend/patient-service && mvn spring-boot:run

# Terminal 5
cd ~/SpringBoot-projects/Patient_Management_System/backend/doctor-service && mvn spring-boot:run

# Terminal 6
cd ~/SpringBoot-projects/Patient_Management_System/backend/lab-service && mvn spring-boot:run

# Terminal 7
cd ~/SpringBoot-projects/Patient_Management_System/backend/pharmacy-service && mvn spring-boot:run
```

**Watch each service log** — you'll see SQL DDL statements as Hibernate creates tables:
```
Hibernate: create table users (id bigserial not null, ...)
Hibernate: create table patients (id bigserial not null, ...)
...
```

---

### 3. Create Admin User (REQUIRED)

**Tables are auto-created but empty!** You must manually insert the admin user:

```bash
psql -U postgres -d pms_auth_db
```

```sql
INSERT INTO users (full_name, email, password, role, is_active, created_at) VALUES
('System Admin', 'admin@hospital.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', true, NOW());
```

Password is: `Admin@123`

```sql
SELECT * FROM users;
\q
```

---

### 4. Start Frontend

```bash
# Terminal 8
cd ~/SpringBoot-projects/Patient_Management_System/Frontend
npm run dev
```

---

### 5. Login

Open http://localhost:5173

- Email: `admin@hospital.com`
- Password: `Admin@123`
- Select "Admin Portal"

---

## Database Details

### How Tables Are Created

**From Entity Classes** — JPA reads your `@Entity` annotations and creates tables:

```java
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false, unique = true)
    private String email;
    // ... etc
}
```

Becomes:
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    ...
);
```

---

### All Tables Created

**pms_auth_db:**
- `users` (from `User.java`)

**pms_patient_db:**
- `patients` (from `Patient.java`)
- `medical_records` (from `MedicalRecord.java`)

**pms_doctor_db:**
- `doctors` (from `Doctor.java`)
- `appointments` (from `Appointment.java`)
- `prescriptions` (from `Prescription.java`)
- `prescription_items` (from `PrescriptionItem.java`)

**pms_lab_db:**
- `lab_orders` (from `LabOrder.java`)
  - **Special:** `diagnostic_payload` column is **JSONB** type

**pms_pharmacy_db:**
- `drugs` (from `Drug.java`)
- `dispense_logs` (from `DispenseLog.java`)
- `dispense_items` (from `DispenseItem.java`)

---

### JSONB Column (Lab Service)

The lab service uses a special JSONB column to store flexible diagnostic data:

```java
@Entity
public class LabOrder {
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> diagnosticPayload;
}
```

This creates:
```sql
CREATE TABLE lab_orders (
    ...
    diagnostic_payload JSONB,
    ...
);
```

You can store any JSON structure:
```json
{
  "bodyPart": "Chest",
  "view": "PA",
  "findings": "Normal lung fields",
  "impression": "No acute disease"
}
```

---

### Verify Tables Created

```bash
# Check each database
psql -U postgres -d pms_auth_db -c "\dt"
psql -U postgres -d pms_patient_db -c "\dt"
psql -U postgres -d pms_doctor_db -c "\dt"
psql -U postgres -d pms_lab_db -c "\dt"
psql -U postgres -d pms_pharmacy_db -c "\dt"
```

---

## Check Eureka

Open http://localhost:8761 — you should see all 5 services registered:
- API-GATEWAY (port 8080)
- AUTH-SERVICE (port 8081)
- PATIENT-SERVICE (port 8082)
- DOCTOR-SERVICE (port 8083)
- LAB-SERVICE (port 8084)
- PHARMACY-SERVICE (port 8085)

---

## Seed Sample Data (Optional)

### Add Sample Drugs

```bash
psql -U postgres -d pms_pharmacy_db
```

```sql
INSERT INTO drugs (name, generic_name, manufacturer, category, strength, dosage_form, quantity_in_stock, reorder_level, unit_price, expiry_date, batch_number, created_at, updated_at) VALUES
('Paracetamol', 'Acetaminophen', 'Sun Pharma', 'ANALGESIC', '500mg', 'Tablet', 500, 50, 2.50, '2027-12-31', 'PCM-001', NOW(), NOW()),
('Amoxicillin', 'Amoxicillin', 'Cipla', 'ANTIBIOTIC', '250mg', 'Capsule', 200, 30, 8.00, '2026-06-30', 'AMX-002', NOW(), NOW());
```

### Add More Users

```bash
psql -U postgres -d pms_auth_db
```

```sql
-- Doctor (password: Admin@123)
INSERT INTO users (full_name, email, password, role, is_active, created_at) VALUES
('Dr. Priya Sharma', 'priya@hospital.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'DOCTOR', true, NOW());

-- Patient (password: Admin@123)
INSERT INTO users (full_name, email, password, role, is_active, created_at) VALUES
('John Doe', 'john@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PATIENT', true, NOW());
```

---

## Stop Everything

Press `Ctrl + C` in each terminal to stop services.

---

## Troubleshooting

### Database connection refused?
```bash
sudo systemctl start postgresql
```

### Port already in use?
```bash
lsof -i :8081  # Find process
kill -9 <PID>  # Kill it
```

### Tables not created?
Check service logs — you should see:
```
Hibernate: create table users ...
```

If you see errors, check:
1. PostgreSQL is running
2. Databases exist (`\l` in psql)
3. `ddl-auto=update` in application.properties/yml

---

## Summary

✅ **PostgreSQL** — 5 databases on localhost:5432 (user: postgres, password: postgres)  
✅ **JPA Creates Tables** — From `@Entity` classes with `ddl-auto=update`  
✅ **No Flyway** — No migration files needed  
✅ **JSONB Support** — Lab service uses PostgreSQL JSONB column  
✅ **Manual Seed** — Insert admin user via SQL after first run  

**Access:** http://localhost:5173 → Login with `admin@hospital.com` / `Admin@123`

Read **LOCAL_SETUP_GUIDE.md** for detailed troubleshooting.

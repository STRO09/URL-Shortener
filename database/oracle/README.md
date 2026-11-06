# Oracle Database Free (26ai) Migration Guide

This directory contains standalone, non-destructive migration scripts to set up, populate, and verify the URL Shortener database on a local **Oracle Database 26ai Free** instance.

None of your original files, databases, or tables have been altered or deleted.

---

## 📁 Migration Files Overview

| File | Purpose | Who Runs It | Where to Run |
| :--- | :--- | :--- | :--- |
| [`01_sysdba_setup.sql`](file:///c:/Users/Sagar/Documents/Projects/URL%20shortener/database/oracle/01_sysdba_setup.sql) | Switches to `FREEPDB1`, creates application user `URLAPP`, assigns quota and privileges | DBA (`sysdba`) | `sqlplus / as sysdba` |
| [`02_create_tables.sql`](file:///c:/Users/Sagar/Documents/Projects/URL%20shortener/database/oracle/02_create_tables.sql) | Creates table `URL_MAPPING` with `IDENTITY` primary key, unique constraints, and comments | Application user (`URLAPP`) | `sqlplus URLAPP/...@localhost:1521/FREEPDB1` |
| [`03_data_migration_template.sql`](file:///c:/Users/Sagar/Documents/Projects/URL%20shortener/database/oracle/03_data_migration_template.sql) | Repeatable, idempotent data import using `MERGE` (safe to re-run without duplicate key errors) | Application user (`URLAPP`) | `sqlplus URLAPP/...@localhost:1521/FREEPDB1` |
| [`04_mysql_export_queries.sql`](file:///c:/Users/Sagar/Documents/Projects/URL%20shortener/database/oracle/04_mysql_export_queries.sql) | Read-only SQL queries to extract data from your existing MySQL database formatted as Oracle MERGE statements | Developer | MySQL Workbench / CLI |
| [`05_verification.sql`](file:///c:/Users/Sagar/Documents/Projects/URL%20shortener/database/oracle/05_verification.sql) | Validates table structure, constraints, row count, and queries used by Spring Boot | Application user (`URLAPP`) | `sqlplus URLAPP/...@localhost:1521/FREEPDB1` |
| [`06_rollback.sql`](file:///c:/Users/Sagar/Documents/Projects/URL%20shortener/database/oracle/06_rollback.sql) | Cleanly drops Oracle tables/schema if you ever wish to undo the Oracle setup | Application user / DBA | SQL*Plus |

---

## 🚀 Step-by-Step Execution Guide

### Step 1: Create Oracle User (as SYSDBA)
1. Open PowerShell / Command Prompt on Windows.
2. Launch SQL*Plus as SYSDBA:
   ```cmd
   sqlplus / as sysdba
   ```
3. Edit [`01_sysdba_setup.sql`](file:///c:/Users/Sagar/Documents/Projects/URL%20shortener/database/oracle/01_sysdba_setup.sql) to set your desired password for `URLAPP`, then execute:
   ```sql
   @c:\Users\Sagar\Documents\Projects\URL shortener\database\oracle\01_sysdba_setup.sql
   ```
4. Type `exit` to leave SQL*Plus.

---

### Step 2: Create Schema & Constraints (as URLAPP)
1. Connect to SQL*Plus as `URLAPP`:
   ```cmd
   sqlplus URLAPP/<YOUR_PASSWORD>@localhost:1521/FREEPDB1
   ```
2. Execute the table creation script:
   ```sql
   @c:\Users\Sagar\Documents\Projects\URL shortener\database\oracle\02_create_tables.sql
   ```

---

### Step 3: Migrate / Import Data
- **Option A (If migrating existing records from MySQL):**
  1. Open [`04_mysql_export_queries.sql`](file:///c:/Users/Sagar/Documents/Projects/URL%20shortener/database/oracle/04_mysql_export_queries.sql) in MySQL Workbench or CLI.
  2. Run Query 2 to generate Oracle-ready `MERGE` statements.
  3. Copy and run the generated statements in your `URLAPP` SQL*Plus session.

- **Option B (Fresh start or sample records):**
  1. In your `URLAPP` SQL*Plus session, run:
     ```sql
     @c:\Users\Sagar\Documents\Projects\URL shortener\database\oracle\03_data_migration_template.sql
     ```

---

### Step 4: Verify Schema and Data Integrity
In your `URLAPP` SQL*Plus session, execute:
```sql
@c:\Users\Sagar\Documents\Projects\URL shortener\database\oracle\05_verification.sql
```
Check that:
- `URL_MAPPING` columns and data types match.
- Constraints (`pk_url_mapping`, `uk_url_mapping_longurl`, `uk_url_mapping_shortcode`) are listed and valid.
- The row counts match your expected numbers.

---

### Step 5: Run Spring Boot with Oracle Profile
The original [`application.properties`](file:///c:/Users/Sagar/Documents/Projects/URL%20shortener/backend/application.properties) remains unchanged.

To run with Oracle, set the environment variable or launch Spring Boot with the `oracle` profile:
```cmd
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=oracle -DORACLE_DB_PASSWORD=<YOUR_PASSWORD>
```
Or directly from your IDE by activating the `oracle` Spring profile.

---

## 🔄 Rollback Plan
If you ever want to revert back to MySQL:
1. No changes are required to MySQL—it was never touched.
2. In Spring Boot, start without the `oracle` profile (it defaults to your standard `application.properties` pointing to MySQL).
3. (Optional) Run [`06_rollback.sql`](file:///c:/Users/Sagar/Documents/Projects/URL%20shortener/database/oracle/06_rollback.sql) in SQL*Plus to drop the Oracle table or user.

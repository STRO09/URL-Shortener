-- ============================================================================
-- Script: 05_verification.sql
-- Purpose: Verify schema structure, constraints, row count, and record accessibility
-- Execution: Run in SQL*Plus connected as URLAPP:
--            sqlplus URLAPP/"YOUR_SECURE_PASSWORD"@localhost:1521/FREEPDB1
-- ============================================================================

SET PAGESIZE 50
SET LINESIZE 150
COLUMN column_name FORMAT A20
COLUMN data_type FORMAT A18
COLUMN nullable FORMAT A10
COLUMN constraint_name FORMAT A25
COLUMN constraint_type FORMAT A15
COLUMN longurl FORMAT A40
COLUMN shortcode FORMAT A15

PROMPT 1. Verify Table Columns and Data Types:
SELECT column_name, 
       data_type || 
       CASE 
         WHEN data_type IN ('VARCHAR2', 'CHAR') THEN '(' || data_length || ')'
         WHEN data_type = 'NUMBER' AND data_precision IS NOT NULL THEN '(' || data_precision || ',' || data_scale || ')'
         ELSE ''
       END AS data_type, 
       nullable, 
       identity_column
FROM user_tab_cols
WHERE table_name = 'URL_MAPPING'
ORDER BY column_id;

PROMPT 2. Verify Constraints (Primary Key and Unique constraints):
SELECT constraint_name, 
       CASE constraint_type
         WHEN 'P' THEN 'PRIMARY KEY'
         WHEN 'U' THEN 'UNIQUE'
         WHEN 'C' THEN 'CHECK'
         ELSE constraint_type
       END AS constraint_type, 
       status
FROM user_constraints
WHERE table_name = 'URL_MAPPING';

PROMPT 3. Total Row Count in Oracle:
SELECT COUNT(*) AS total_oracle_records FROM URL_MAPPING;

PROMPT 4. Sample Record Inspection:
SELECT id, shortcode, SUBSTR(longurl, 1, 40) AS longurl, created_at, expires_at, click_count 
FROM URL_MAPPING
FETCH FIRST 5 ROWS ONLY;

PROMPT 5. Test Key Queries Used by Spring Boot:
-- Simulated repository.findByshortcode('springboot')
SELECT * FROM URL_MAPPING WHERE shortcode = 'springboot';

-- Simulated repository.findBylongurl(...)
SELECT * FROM URL_MAPPING WHERE longurl = 'https://spring.io/projects/spring-boot';

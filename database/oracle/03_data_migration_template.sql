-- ============================================================================
-- Script: 03_data_migration_template.sql
-- Purpose: Repeatable, idempotent data migration/import into Oracle URL_MAPPING
-- Execution: Run in SQL*Plus connected as URLAPP:
--            sqlplus URLAPP/"YOUR_SECURE_PASSWORD"@localhost:1521/FREEPDB1
-- ============================================================================

SET SERVEROUTPUT ON;

-- Use MERGE statements to safely insert records without violating UNIQUE constraints
-- If a record with the same shortcode or longurl already exists, it will NOT be duplicated.

MERGE INTO URL_MAPPING target
USING (
    SELECT 
        'https://spring.io/projects/spring-boot' AS longurl,
        'springboot'                             AS shortcode,
        TO_TIMESTAMP('2026-01-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS') AS created_at,
        TO_TIMESTAMP('2026-02-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS') AS expires_at,
        5                                        AS click_count
    FROM dual
) src
ON (target.shortcode = src.shortcode OR target.longurl = src.longurl)
WHEN NOT MATCHED THEN
    INSERT (longurl, shortcode, created_at, expires_at, click_count)
    VALUES (src.longurl, src.shortcode, src.created_at, src.expires_at, src.click_count);

MERGE INTO URL_MAPPING target
USING (
    SELECT 
        'https://docs.oracle.com/en/database/oracle/oracle-database/23/index.html' AS longurl,
        'oracle23'                                                                 AS shortcode,
        TO_TIMESTAMP('2026-01-15 12:00:00', 'YYYY-MM-DD HH24:MI:SS')             AS created_at,
        TO_TIMESTAMP('2026-02-15 12:00:00', 'YYYY-MM-DD HH24:MI:SS')             AS expires_at,
        12                                                                         AS click_count
    FROM dual
) src
ON (target.shortcode = src.shortcode OR target.longurl = src.longurl)
WHEN NOT MATCHED THEN
    INSERT (longurl, shortcode, created_at, expires_at, click_count)
    VALUES (src.longurl, src.shortcode, src.created_at, src.expires_at, src.click_count);

COMMIT;

PROMPT Data import completed and committed.

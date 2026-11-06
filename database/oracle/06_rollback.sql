-- ============================================================================
-- Script: 06_rollback.sql
-- Purpose: Rollback script for Oracle migration
-- ============================================================================

-- ============================================================================
-- OPTION A: Clean up objects within URLAPP schema (Run as URLAPP)
-- Execution: sqlplus URLAPP/"YOUR_SECURE_PASSWORD"@localhost:1521/FREEPDB1
-- ============================================================================

PROMPT Dropping table URL_MAPPING in URLAPP...
DROP TABLE URL_MAPPING CASCADE CONSTRAINTS PURGE;
PROMPT Table URL_MAPPING dropped cleanly.

-- ============================================================================
-- OPTION B: Completely remove URLAPP schema (Run as SYSDBA)
-- Execution: sqlplus / as sysdba
-- ============================================================================
/*
ALTER SESSION SET CONTAINER = FREEPDB1;
DROP USER URLAPP CASCADE;
PROMPT Schema URLAPP and all its objects removed.
*/

-- ============================================================================
-- APPLICATION ROLLBACK:
-- To revert the Spring Boot application back to MySQL:
-- 1. In backend/application.properties, keep the MySQL configuration active.
-- 2. Do not pass --spring.profiles.active=oracle when starting the application.
-- 3. The original MySQL database 'urlshortener' was never modified and remains intact.
-- ============================================================================

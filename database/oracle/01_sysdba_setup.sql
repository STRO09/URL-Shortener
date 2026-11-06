-- ============================================================================
-- Script: 01_sysdba_setup.sql
-- Purpose: Setup Oracle Database Free (26ai) user, tablespace, and privileges
-- Execution: Run in SQL*Plus connected as SYSDBA:
--            sqlplus / as sysdba
-- ============================================================================

-- Ensure we are connected to the correct Pluggable Database (PDB)
ALTER SESSION SET CONTAINER = FREEPDB1;

PROMPT Switched session to FREEPDB1.

-- Check if URLAPP user already exists
SET SERVEROUTPUT ON;
DECLARE
    v_user_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_user_count FROM dba_users WHERE username = 'URLAPP';
    IF v_user_count = 0 THEN
        -- Create the application schema user with unlimited quota on USERS tablespace
        -- NOTE: Replace YOUR_SECURE_PASSWORD with your desired application password before executing!
        EXECUTE IMMEDIATE 'CREATE USER URLAPP IDENTIFIED BY "YOUR_SECURE_PASSWORD" ' ||
                          'DEFAULT TABLESPACE USERS ' ||
                          'TEMPORARY TABLESPACE TEMP ' ||
                          'QUOTA UNLIMITED ON USERS';
        DBMS_OUTPUT.PUT_LINE('User URLAPP created successfully.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('User URLAPP already exists. Skipping creation.');
    END IF;
END;
/

-- Grant minimal required permissions for the Spring Boot application
GRANT CREATE SESSION TO URLAPP;
GRANT CREATE TABLE TO URLAPP;
GRANT CREATE SEQUENCE TO URLAPP;
GRANT CREATE TRIGGER TO URLAPP;
GRANT CREATE VIEW TO URLAPP;

PROMPT Permissions granted to URLAPP.

-- Verify account status and tablespace
COLUMN username FORMAT A15
COLUMN account_status FORMAT A20
COLUMN default_tablespace FORMAT A20
COLUMN temporary_tablespace FORMAT A20

SELECT username, account_status, default_tablespace, temporary_tablespace
FROM dba_users
WHERE username = 'URLAPP';

PROMPT Setup complete. You can now connect as:
PROMPT sqlplus URLAPP@localhost:1521/FREEPDB1

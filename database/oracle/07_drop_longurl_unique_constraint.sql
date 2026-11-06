-- ============================================================================
-- Script: 07_drop_longurl_unique_constraint.sql
-- Purpose: Remove the UNIQUE constraint on URL_MAPPING.longurl so that
--          multiple shortcodes / aliases can point to the same destination URL
--          without overwriting each other.
--
-- Background: The original design had longurl UNIQUE, meaning one URL = one row.
--             When a custom alias was added for an existing URL the row was
--             overwritten and the old shortcode became invalid.
--             The new design allows one URL → many independent rows, one per
--             shortcode or alias.  shortcode remains UNIQUE.
--
-- Execution:  Run as URLAPP (or any user with ALTER TABLE privilege):
--             sqlplus URLAPP/"YOUR_PASSWORD"@localhost:1521/FREEPDB1
--             @database/oracle/07_drop_longurl_unique_constraint.sql
--
-- Safe to re-run: wrapped in PL/SQL to skip gracefully if already removed.
-- ============================================================================

SET SERVEROUTPUT ON;

DECLARE
    v_count NUMBER;
BEGIN
    -- Check whether the unique constraint still exists
    SELECT COUNT(*)
      INTO v_count
      FROM user_constraints
     WHERE table_name      = 'URL_MAPPING'
       AND constraint_type = 'U'
       AND constraint_name = 'UK_URL_MAPPING_LONGURL';

    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE URL_MAPPING DROP CONSTRAINT UK_URL_MAPPING_LONGURL';
        DBMS_OUTPUT.PUT_LINE('Constraint UK_URL_MAPPING_LONGURL dropped successfully.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Constraint UK_URL_MAPPING_LONGURL does not exist — skipping.');
    END IF;
END;
/

-- Verify: longurl should no longer appear in unique constraints
SELECT constraint_name, constraint_type, status
  FROM user_constraints
 WHERE table_name = 'URL_MAPPING'
   AND constraint_type IN ('P', 'U');

PROMPT Migration complete.


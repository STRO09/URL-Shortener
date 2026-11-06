-- ============================================================================
-- Script: 08_add_custom_alias_column.sql
-- Purpose: Add the CUSTOM_ALIAS boolean column to URL_MAPPING.
--
--   FALSE (0) → row holds an auto-generated shortcode (at most one per longurl)
--   TRUE  (1) → row holds a user-supplied custom alias (many allowed per longurl)
--
-- Existing rows (all auto-generated before this change) default to 0 (FALSE).
--
-- Execution: Run as URLAPP on FREEPDB1:
--   sqlplus URLAPP/"YOUR_PASSWORD"@localhost:1521/FREEPDB1
--   @database/oracle/08_add_custom_alias_column.sql
--
-- Safe to re-run: wrapped in PL/SQL to skip if column already exists.
-- ============================================================================

SET SERVEROUTPUT ON;

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_count
      FROM user_tab_columns
     WHERE table_name  = 'URL_MAPPING'
       AND column_name = 'CUSTOM_ALIAS';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'ALTER TABLE URL_MAPPING ADD (custom_alias NUMBER(1) DEFAULT 0 NOT NULL)';
        DBMS_OUTPUT.PUT_LINE('Column CUSTOM_ALIAS added successfully.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Column CUSTOM_ALIAS already exists — skipping.');
    END IF;
END;
/

-- Verify
SELECT column_name, data_type, nullable, data_default
  FROM user_tab_columns
 WHERE table_name = 'URL_MAPPING'
 ORDER BY column_id;

PROMPT Migration complete.


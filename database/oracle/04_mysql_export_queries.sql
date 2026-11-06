-- ============================================================================
-- Script: 04_mysql_export_queries.sql
-- Purpose: Safely export records from MySQL in Oracle-compatible MERGE/INSERT format
-- Execution: Run in MySQL Workbench, MySQL CLI, or DBeaver against MySQL 'urlshortener' DB.
-- Note: This is 100% READ-ONLY and will not alter or delete any MySQL data.
-- ============================================================================

-- Query 1: Quick count and review of existing records in MySQL
SELECT COUNT(*) AS total_mysql_records FROM url_mapping;

SELECT id, longurl, shortcode, created_at, expires_at, click_count 
FROM url_mapping;

-- Query 2: Generate Oracle-compatible MERGE statements directly from MySQL
-- You can copy the generated text and run it directly in SQL*Plus connected as URLAPP:

SELECT CONCAT(
    'MERGE INTO URL_MAPPING target ',
    'USING (SELECT ''', REPLACE(longurl, '''', ''''''), ''' AS longurl, ',
    ''''', REPLACE(shortcode, '''', ''''''), ''' AS shortcode, ',
    'TO_TIMESTAMP(''', DATE_FORMAT(created_at, '%Y-%m-%d %H:%i:%s'), ''', ''YYYY-MM-DD HH24:MI:SS'') AS created_at, ',
    'TO_TIMESTAMP(''', DATE_FORMAT(expires_at, '%Y-%m-%d %H:%i:%s'), ''', ''YYYY-MM-DD HH24:MI:SS'') AS expires_at, ',
    IFNULL(click_count, 0), ' AS click_count FROM dual) src ',
    'ON (target.shortcode = src.shortcode OR target.longurl = src.longurl) ',
    'WHEN NOT MATCHED THEN ',
    'INSERT (longurl, shortcode, created_at, expires_at, click_count) ',
    'VALUES (src.longurl, src.shortcode, src.created_at, src.expires_at, src.click_count);'
) AS oracle_migration_sql
FROM url_mapping;

-- ============================================================================
-- MySQL equivalent of 07_drop_longurl_unique_constraint.sql
-- Run as the app user against the urlshortener database.
-- ============================================================================

-- Drop the unique index Hibernate created for longurl
-- (Hibernate names it after the column by default)
ALTER TABLE url_mapping DROP INDEX UK_url_mapping_longurl;

-- Verify
SHOW INDEX FROM url_mapping;


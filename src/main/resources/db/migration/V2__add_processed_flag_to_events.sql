-- V2__add_processed_flag_to_events.sql

ALTER TABLE events ADD COLUMN processed BOOLEAN DEFAULT FALSE;

CREATE INDEX idx_events_unprocessed ON events (id) WHERE processed IS FALSE;
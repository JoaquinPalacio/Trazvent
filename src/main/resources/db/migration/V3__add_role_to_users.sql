-- V3__add_role_to_users.sql
-- Añadimos la columna 'role' explícitamente para integrarla fácilmente con Spring Security.
-- Ponemos 'ADMIN' por defecto en caso de que ya existieran registros.

ALTER TABLE users 
ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT 'ADMIN';

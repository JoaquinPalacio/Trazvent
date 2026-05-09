-- 1. Tabla de Proyectos (Clientes del SaaS)
CREATE TABLE projects (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          api_key_hash VARCHAR(255) NOT NULL UNIQUE,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla de Usuarios (Usuarios del Dashboard de Trazvent)
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       project_id UUID NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       traits JSONB,

                       CONSTRAINT fk_users_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- 3. Tabla de Eventos Crudos (Raw Events generados por los End-Users)
CREATE TABLE events (
                        id UUID PRIMARY KEY,
                        project_id UUID NOT NULL,
                        user_id VARCHAR(255),
                        anonymous_id VARCHAR(255),
                        event_name VARCHAR(255) NOT NULL,
                        timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
                        properties JSONB NOT NULL,

                        CONSTRAINT fk_events_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Índices vitales para optimizar las consultas analíticas
-- GIN permite buscar claves específicas muy rápido dentro del JSON de propiedades
CREATE INDEX idx_events_properties ON events USING GIN (properties);
-- Este índice acelerará las búsquedas por rangos de fecha para un proyecto específico
CREATE INDEX idx_events_project_time ON events (project_id, timestamp);

-- 4. Tabla de Agregados (Métricas Precomputadas)
CREATE TABLE aggregates (
                            project_id UUID NOT NULL,
                            event_name VARCHAR(255) NOT NULL,
                            date DATE NOT NULL,
                            count INTEGER DEFAULT 0,
                            sum DOUBLE PRECISION DEFAULT 0.0,
                            avg DOUBLE PRECISION DEFAULT 0.0,

    -- Clave primaria compuesta para evitar duplicados en el proceso asíncrono (Cron Job)
                            PRIMARY KEY (project_id, event_name, date),
                            CONSTRAINT fk_aggregates_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- 5. Tabla de Metadatos de Eventos (Diccionario de datos)
CREATE TABLE event_metadata (
                                project_id UUID NOT NULL,
                                event_name VARCHAR(255) NOT NULL,
                                property_name VARCHAR(255) NOT NULL,
                                type VARCHAR(50) NOT NULL,

    -- Clave primaria compuesta para el diccionario de variables
                                PRIMARY KEY (project_id, event_name, property_name),
                                CONSTRAINT fk_metadata_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);
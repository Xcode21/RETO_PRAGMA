-- Database initialization script for UserService
-- This script will be executed when the PostgreSQL container starts

-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS public;

-- Grant permissions to the app user
GRANT ALL PRIVILEGES ON SCHEMA public TO app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO app;

-- Set default privileges for future tables and sequences
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO app;

-- Create users table (adjust according to your domain model)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    document_number VARCHAR(20) NOT NULL UNIQUE,
    document_type VARCHAR(10) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    birth_date DATE,
    monthly_income DECIMAL(12, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_document ON users(document_type, document_number);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- Insert sample data (optional - remove if not needed)
INSERT INTO users (document_number, document_type, first_name, last_name, email, phone, birth_date, monthly_income) 
VALUES 
    ('12345678', 'CC', 'Juan', 'Pérez', 'juan.perez@email.com', '3001234567', '1990-01-15', 2500000.00),
    ('87654321', 'CC', 'Maria', 'González', 'maria.gonzalez@email.com', '3009876543', '1985-05-20', 3000000.00)
ON CONFLICT (document_number) DO NOTHING;
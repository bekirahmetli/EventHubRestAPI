-- EventHubRestAPI Seed Data
-- This file contains sample data for testing the application
-- Execute this file after the database schema is created

-- Clear existing data (optional - uncomment if needed)
-- TRUNCATE TABLE registrations CASCADE;
-- TRUNCATE TABLE ticket_types CASCADE;
-- TRUNCATE TABLE events CASCADE;
-- TRUNCATE TABLE categories CASCADE;
-- TRUNCATE TABLE users CASCADE;

-- Insert Categories
INSERT INTO categories (name) VALUES
    ('Müzik'),
    ('Spor'),
    ('Teknoloji'),
    ('Sanat'),
    ('Eğitim'),
    ('Kültür'),
    ('Eğlence'),
    ('İş & Kariyer')
ON CONFLICT (name) DO NOTHING;

-- Insert Users (with BCrypt hashed passwords)
-- Password for all users: "password123"
-- BCrypt hash for "password123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- This hash is generated with BCrypt strength 10
-- Note: In production, users should be created via API to ensure proper password hashing
-- You can generate a new hash using BCryptPasswordEncoder or via the API registration endpoint
INSERT INTO users (name, email, password, auth_provider, role, created_at, avatar_url) VALUES
    ('Admin User', 'admin@eventhub.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'LOCAL', 'ADMIN', NOW(), NULL),
    ('John Doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'LOCAL', 'ORGANIZER', NOW(), NULL),
    ('Jane Smith', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'LOCAL', 'USER', NOW(), NULL),
    ('Bob Johnson', 'bob@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'LOCAL', 'USER', NOW(), NULL),
    ('Alice Brown', 'alice@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'LOCAL', 'ORGANIZER', NOW(), NULL)
ON CONFLICT (email) DO NOTHING;

-- Insert Events
-- Note: Adjust category_id and event_user_id based on your actual IDs
INSERT INTO events (title, description, poster_url, location, date, event_category_id, event_user_id) VALUES
    ('Rock Festival 2024', 'Büyük rock festivali! 20+ sanatçı sahne alacak.', 'https://example.com/posters/rock-festival.jpg', 'Ankara Hipodrom', NOW() + INTERVAL '30 days', 1, 2),
    ('Marathon Istanbul', 'İstanbul Maratonu - Tüm seviyeler için koşu etkinliği', 'https://example.com/posters/marathon.jpg', 'İstanbul Tarihi Yarımada', NOW() + INTERVAL '15 days', 2, 2),
    ('Tech Conference 2024', 'Yazılım geliştirme ve teknoloji konferansı', 'https://example.com/posters/tech-conf.jpg', 'İstanbul Kongre Merkezi', NOW() + INTERVAL '45 days', 3, 5),
    ('Resim Sergisi', 'Modern sanat resim sergisi - Ücretsiz giriş', 'https://example.com/posters/art-exhibition.jpg', 'İstanbul Modern Sanat Müzesi', NOW() + INTERVAL '10 days', 4, 5),
    ('Yazılım Geliştirme Bootcamp', 'Full-stack yazılım geliştirme eğitimi', 'https://example.com/posters/bootcamp.jpg', 'Online', NOW() + INTERVAL '20 days', 5, 2),
    ('Klasik Müzik Konseri', 'Senfoni orkestrası ile klasik müzik gecesi', 'https://example.com/posters/classical.jpg', 'Ankara CSO Konser Salonu', NOW() + INTERVAL '25 days', 1, 2),
    ('Startup Meetup', 'Girişimciler ve yatırımcılar buluşması', 'https://example.com/posters/startup.jpg', 'İstanbul Koç Üniversitesi', NOW() + INTERVAL '35 days', 8, 5),
    ('Film Festivali', 'Bağımsız sinema filmleri gösterimi', 'https://example.com/posters/film-fest.jpg', 'İstanbul Sinema Müzesi', NOW() + INTERVAL '40 days', 6, 2)
ON CONFLICT DO NOTHING;

-- Insert Ticket Types
-- Note: Adjust ticket_type_event_id based on your actual event IDs
INSERT INTO ticket_types (name, price, quota, ticket_type_event_id) VALUES
    -- Rock Festival 2024 (event_id = 1)
    ('VIP', 500.00, 100, 1),
    ('Normal', 200.00, 500, 1),
    ('Öğrenci', 100.00, 200, 1),
    
    -- Marathon Istanbul (event_id = 2)
    ('Maraton', 150.00, 5000, 2),
    ('10K', 100.00, 3000, 2),
    ('5K', 75.00, 2000, 2),
    
    -- Tech Conference 2024 (event_id = 3)
    ('Early Bird', 800.00, 200, 3),
    ('Standart', 1200.00, 300, 3),
    ('Öğrenci', 400.00, 100, 3),
    
    -- Resim Sergisi (event_id = 4)
    ('Ücretsiz', 0.00, 1000, 4),
    
    -- Yazılım Geliştirme Bootcamp (event_id = 5)
    ('Full Access', 2000.00, 50, 5),
    ('Online', 1500.00, 100, 5),
    
    -- Klasik Müzik Konseri (event_id = 6)
    ('VIP', 300.00, 50, 6),
    ('Normal', 150.00, 300, 6),
    
    -- Startup Meetup (event_id = 7)
    ('Katılımcı', 50.00, 200, 7),
    
    -- Film Festivali (event_id = 8)
    ('Günlük Bilet', 40.00, 500, 8),
    ('Festival Geçişi', 200.00, 100, 8)
ON CONFLICT DO NOTHING;

-- Insert Registrations
-- Note: Adjust registration_user_id and registration_ticket_type_id based on your actual IDs
INSERT INTO registrations (registered_at, status, registration_user_id, registration_ticket_type_id) VALUES
    (NOW() - INTERVAL '5 days', 'ACTIVE', 3, 2),
    (NOW() - INTERVAL '4 days', 'ACTIVE', 3, 5),
    (NOW() - INTERVAL '3 days', 'ACTIVE', 4, 8),
    (NOW() - INTERVAL '2 days', 'CANCELED', 4, 3),
    (NOW() - INTERVAL '1 day', 'ACTIVE', 3, 11),
    (NOW() - INTERVAL '6 hours', 'ACTIVE', 4, 13),
    (NOW() - INTERVAL '2 hours', 'ACTIVE', 3, 1),
    (NOW() - INTERVAL '1 hour', 'ACTIVE', 4, 7)
ON CONFLICT DO NOTHING;

-- Verify inserted data
SELECT 'Categories' as table_name, COUNT(*) as count FROM categories
UNION ALL
SELECT 'Users', COUNT(*) FROM users
UNION ALL
SELECT 'Events', COUNT(*) FROM events
UNION ALL
SELECT 'Ticket Types', COUNT(*) FROM ticket_types
UNION ALL
SELECT 'Registrations', COUNT(*) FROM registrations;


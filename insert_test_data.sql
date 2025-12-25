\
INSERT INTO cuisines (name, description, created_at) VALUES
('Kazakhskaya', 'Traditional Kazakh cuisine', CURRENT_TIMESTAMP),
('Uzbekskaya', 'Uzbek cuisine with plov', CURRENT_TIMESTAMP),
('Evropeyskaya', 'European cuisine', CURRENT_TIMESTAMP),
('Aziatskaya', 'Asian cuisine', CURRENT_TIMESTAMP),
('Kavkazskaya', 'Caucasian cuisine', CURRENT_TIMESTAMP),
('Italyanskaya', 'Italian cuisine', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Шаг 3: Добавление пользователей
-- Пароль для всех: admin123
-- Hash: $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi

-- Администратор
INSERT INTO users (email, password, first_name, last_name, phone, role, is_active, is_blocked, created_at, updated_at)
VALUES ('admin@restaurant.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin', 'System', '+77001234567', 'ADMIN', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Владельцы ресторанов
INSERT INTO users (email, password, first_name, last_name, phone, role, is_active, is_blocked, created_at, updated_at)
VALUES
('owner1@test.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Azamat', 'Nurlan', '+77011234567', 'OWNER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('owner2@test.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Dana', 'Suleimenova', '+77021234567', 'OWNER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('owner3@test.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Murat', 'Kairatov', '+77031234567', 'OWNER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Обычные пользователи
INSERT INTO users (email, password, first_name, last_name, phone, role, is_active, is_blocked, created_at, updated_at)
VALUES
('user1@test.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Timur', 'Zhanibek', '+77051234567', 'USER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user2@test.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Aruzhan', 'Tastanova', '+77061234567', 'USER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Шаг 4: Добавление ресторанов
-- Используем подзапросы для получения owner_id

-- Рестораны владельца owner1@test.kz
INSERT INTO restaurants (owner_id, name, description, address, city, zip_code, phone, email, rating, total_reviews, is_active, created_at, updated_at)
SELECT
    u.id,
    'Dastarkhan',
    'Traditional Kazakh cuisine in modern style. Cozy atmosphere and dishes from our grandmothers recipes.',
    'prospekt Nazarbaeva, 123',
    'Almaty',
    '050000',
    '+77272501234',
    'info@dastarkhan.kz',
    4.8,
    127,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'owner1@test.kz';

INSERT INTO restaurants (owner_id, name, description, address, city, zip_code, phone, email, rating, total_reviews, is_active, created_at, updated_at)
SELECT
    u.id,
    'Baiterek Grill',
    'Caucasian cuisine restaurant with panoramic city view. Fresh shashlik and traditional dishes.',
    'prospekt Kabanbay batira, 11',
    'Astana',
    '010000',
    '+77172504567',
    'info@baiterekgrill.kz',
    4.7,
    143,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'owner1@test.kz';

-- Рестораны владельца owner2@test.kz
INSERT INTO restaurants (owner_id, name, description, address, city, zip_code, phone, email, rating, total_reviews, is_active, created_at, updated_at)
SELECT
    u.id,
    'Silk Road',
    'Uzbek restaurant with authentic dishes and eastern hospitality.',
    'ulitsa Abaya, 45',
    'Almaty',
    '050010',
    '+77272502345',
    'contact@silkroad.kz',
    4.6,
    98,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'owner2@test.kz';

INSERT INTO restaurants (owner_id, name, description, address, city, zip_code, phone, email, rating, total_reviews, is_active, created_at, updated_at)
SELECT
    u.id,
    'Tokyo Garden',
    'Japanese restaurant with wide selection of sushi, rolls and traditional Japanese dishes.',
    'ulitsa Dostyk, 23',
    'Astana',
    '010010',
    '+77172505678',
    'orders@tokyogarden.kz',
    4.5,
    87,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'owner2@test.kz';

-- Рестораны владельца owner3@test.kz
INSERT INTO restaurants (owner_id, name, description, address, city, zip_code, phone, email, rating, total_reviews, is_active, created_at, updated_at)
SELECT
    u.id,
    'Bellissimo',
    'Italian restaurant with homemade pasta, wood-fired pizza and selected wines.',
    'prospekt Al-Farabi, 77',
    'Almaty',
    '050020',
    '+77272503456',
    'reservation@bellissimo.kz',
    4.9,
    215,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'owner3@test.kz';

INSERT INTO restaurants (owner_id, name, description, address, city, zip_code, phone, email, rating, total_reviews, is_active, created_at, updated_at)
SELECT
    u.id,
    'Arman',
    'Family restaurant with homey atmosphere and Kazakh and European cuisine.',
    'prospekt Tauke hana, 56',
    'Shymkent',
    '160000',
    '+77252506789',
    'contact@arman.kz',
    4.4,
    64,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.email = 'owner3@test.kz';

-- Проверка результата
SELECT 'Cuisines loaded:' as status, COUNT(*) as count FROM cuisines;
SELECT 'Users loaded:' as status, COUNT(*) as count FROM users;
SELECT 'Restaurants loaded:' as status, COUNT(*) as count FROM restaurants;

-- Показать все рестораны
SELECT id, name, city, rating FROM restaurants ORDER BY rating DESC;

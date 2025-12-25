-- Простая загрузка данных

-- 1. Сначала добавим кухни
INSERT INTO cuisines (name, description, created_at) VALUES
('Kazakhskaya', 'Traditional Kazakh cuisine', CURRENT_TIMESTAMP),
('Uzbekskaya', 'Uzbek cuisine with plov', CURRENT_TIMESTAMP),
('Evropeyskaya', 'European cuisine', CURRENT_TIMESTAMP),
('Aziatskaya', 'Asian cuisine: sushi and wok', CURRENT_TIMESTAMP),
('Kavkazskaya', 'Caucasian cuisine with shashlik', CURRENT_TIMESTAMP),
('Italyanskaya', 'Italian cuisine: pasta and pizza', CURRENT_TIMESTAMP);

-- 2. Добавим тестовых пользователей
-- Пароль для всех: admin123
INSERT INTO users (email, password, first_name, last_name, phone, role, is_active, is_blocked, created_at, updated_at) VALUES
('admin@restaurant.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin', 'System', '+77001234567', 'ADMIN', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('owner1@test.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Azamat', 'Nurlan', '+77011234567', 'OWNER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('owner2@test.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Dana', 'Suleimenova', '+77021234567', 'OWNER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('owner3@test.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Murat', 'Kairatov', '+77031234567', 'OWNER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user1@test.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Timur', 'Zhanibek', '+77051234567', 'USER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. Добавим рестораны (используя явные ID владельцев)
INSERT INTO restaurants (owner_id, name, description, address, city, zip_code, phone, email, rating, total_reviews, is_active, created_at, updated_at) VALUES
(2, 'Dastarkhan', 'Traditional Kazakh cuisine in modern style', 'prospekt Nazarbaeva, 123', 'Almaty', '050000', '+77272501234', 'info@dastarkhan.kz', 4.8, 127, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Silk Road', 'Uzbek restaurant with authentic dishes', 'ulitsa Abaya, 45', 'Almaty', '050010', '+77272502345', 'contact@silkroad.kz', 4.6, 98, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Bellissimo', 'Italian restaurant with homemade pasta', 'prospekt Al-Farabi, 77', 'Almaty', '050020', '+77272503456', 'reservation@bellissimo.kz', 4.9, 215, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Baiterek Grill', 'Caucasian cuisine with panoramic view', 'prospekt Kabanbay batira, 11', 'Astana', '010000', '+77172504567', 'info@baiterekgrill.kz', 4.7, 143, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Tokyo Garden', 'Japanese restaurant with sushi', 'ulitsa Dostyk, 23', 'Astana', '010010', '+77172505678', 'orders@tokyogarden.kz', 4.5, 87, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Arman', 'Family restaurant with homey atmosphere', 'prospekt Tauke hana, 56', 'Shymkent', '160000', '+77252506789', 'contact@arman.kz', 4.4, 64, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

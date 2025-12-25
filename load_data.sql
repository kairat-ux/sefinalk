-- Загрузка базовых данных для Казахстана

-- Кухни
INSERT INTO cuisines (name, description, created_at) VALUES
('Казахская', 'Традиционная казахская кухня с бешбармаком, куырдаком и другими национальными блюдами', CURRENT_TIMESTAMP),
('Узбекская', 'Узбекская кухня с пловом, самсой, шурпой и лагманом', CURRENT_TIMESTAMP),
('Европейская', 'Классическая европейская кухня', CURRENT_TIMESTAMP),
('Азиатская', 'Азиатская кухня: суши, wok, лапша и другие блюда', CURRENT_TIMESTAMP),
('Кавказская', 'Кавказская кухня с шашлыком, хачапури, хинкали', CURRENT_TIMESTAMP),
('Итальянская', 'Итальянская кухня: паста, пицца, ризотто', CURRENT_TIMESTAMP);

-- Администратор (email: admin@restaurant.kz, пароль: admin123)
INSERT INTO users (email, password, first_name, last_name, phone, role, is_active, is_blocked, created_at, updated_at) VALUES
('admin@restaurant.kz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Администратор', 'Системы', '+77001234567', 'ADMIN', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Владельцы ресторанов (пароль: owner123)
INSERT INTO users (email, password, first_name, last_name, phone, role, is_active, is_blocked, created_at, updated_at) VALUES
('azamat.nurlan@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Азамат', 'Нурланов', '+77011234567', 'OWNER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('dana.suleimenova@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Дана', 'Сулейменова', '+77021234567', 'OWNER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('murat.kairatov@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Мурат', 'Кайратов', '+77031234567', 'OWNER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('aida.bekenova@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Аида', 'Бекенова', '+77041234567', 'OWNER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Обычные пользователи (пароль: user123)
INSERT INTO users (email, password, first_name, last_name, phone, role, is_active, is_blocked, created_at, updated_at) VALUES
('timur.zhanibek@mail.ru', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Тимур', 'Жанибеков', '+77051234567', 'USER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('aruzhan.tastanova@mail.ru', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Аружан', 'Тастанова', '+77061234567', 'USER', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Рестораны в Алматы
INSERT INTO restaurants (owner_id, name, description, address, city, zip_code, phone, email, rating, total_reviews, is_active, created_at, updated_at) VALUES
((SELECT id FROM users WHERE email = 'azamat.nurlan@gmail.com'), 'Dastarkhan', 'Традиционная казахская кухня в современном исполнении. Уютная атмосфера и блюда по рецептам наших бабушек.', 'проспект Назарбаева, 123', 'Алматы', '050000', '+77272501234', 'info@dastarkhan.kz', 4.8, 127, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE email = 'dana.suleimenova@gmail.com'), 'Silk Road', 'Ресторан узбекской кухни с аутентичными блюдами и восточным гостеприимством.', 'улица Абая, 45', 'Алматы', '050010', '+77272502345', 'contact@silkroad.kz', 4.6, 98, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE email = 'murat.kairatov@gmail.com'), 'Bellissimo', 'Итальянский ресторан с домашней пастой, пиццей из дровяной печи и избранными винами.', 'проспект Аль-Фараби, 77', 'Алматы', '050020', '+77272503456', 'reservation@bellissimo.kz', 4.9, 215, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Рестораны в Астане
INSERT INTO restaurants (owner_id, name, description, address, city, zip_code, phone, email, rating, total_reviews, is_active, created_at, updated_at) VALUES
((SELECT id FROM users WHERE email = 'aida.bekenova@gmail.com'), 'Baiterek Grill', 'Ресторан кавказской кухни с панорамным видом на город. Свежий шашлык и традиционные блюда.', 'проспект Кабанбай батыра, 11', 'Астана', '010000', '+77172504567', 'info@baiterekgrill.kz', 4.7, 143, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE email = 'azamat.nurlan@gmail.com'), 'Tokyo Garden', 'Японский ресторан с широким выбором суши, роллов и традиционных японских блюд.', 'улица Достык, 23', 'Астана', '010010', '+77172505678', 'orders@tokyogarden.kz', 4.5, 87, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Ресторан в Шымкенте
INSERT INTO restaurants (owner_id, name, description, address, city, zip_code, phone, email, rating, total_reviews, is_active, created_at, updated_at) VALUES
((SELECT id FROM users WHERE email = 'dana.suleimenova@gmail.com'), 'Arman', 'Семейный ресторан с домашней атмосферой и блюдами казахской и европейской кухни.', 'проспект Тауке хана, 56', 'Шымкент', '160000', '+77252506789', 'contact@arman.kz', 4.4, 64, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

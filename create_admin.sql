-- Create admin user
-- Password: admin123
INSERT INTO users (email, password, first_name, last_name, phone, role, is_active, is_blocked, created_at, updated_at)
VALUES (
    'admin@test.com',
    '$2a$10$8O7wqwMKQQx5Z5Z5Z5Z5Z.vN/N5qN5qN5qN5qN5qN5qN5qN5qN5qO',
    'Admin',
    'User',
    '+77001234567',
    'ADMIN',
    true,
    false,
    NOW(),
    NOW()
)
ON CONFLICT (email) DO UPDATE
SET role = 'ADMIN',
    is_active = true,
    is_blocked = false;

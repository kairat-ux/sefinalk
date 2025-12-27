# Учетные данные для тестирования

## Тестовые пользователи из миграции

**ВАЖНО**: Пароли в файле миграции `014-insert-base-data-NEW.xml` используют BCrypt хеши-примеры, которые могут не работать.

### Вариант 1: Предполагаемые учетные данные из миграции

1. **Администратор**
   - Email: `admin@restaurant.kz`
   - Пароль: `admin123` *(может не работать)*
   - Роль: ADMIN

2. **Владелец ресторана**
   - Email: `owner1@test.kz`
   - Пароль: `owner123` *(может не работать)*
   - Роль: OWNER

3. **Обычный пользователь**
   - Email: `user1@test.kz`
   - Пароль: `user123` *(может не работать)*
   - Роль: USER

### Вариант 2: Создание новых пользователей (РЕКОМЕНДУЕТСЯ)

Используйте endpoint регистрации для создания пользователей с любыми паролями:

```bash
curl -X POST http://localhost:8008/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "yourpassword",
    "firstName": "Test",
    "lastName": "User",
    "phone": "+77777777777"
  }'
```

## Тестовые данные в базе

- **Ресторан**: Dastarkhan (Казахская кухня, Almaty)
- **Cuisines**: Казахская, Узбекская, Итальянская
- **Столики**: 2 столика для ресторана Dastarkhan
- **Рабочие часы**: Понедельник 11:00-23:00

## Примеры API запросов

### Вход в систему
```bash
curl -X POST http://localhost:8008/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@restaurant.kz",
    "password": "admin123"
  }'
```

### Получение списка ресторанов
```bash
curl http://localhost:8008/api/restaurants
```

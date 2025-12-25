# Тестирование API через Postman

## ✅ Исправлено!

Проблема с 403 ошибкой решена:
- Все контроллеры перенесены на `/api/*`
- CORS настроен правильно
- SecurityConfig обновлен

---

## 🚀 Перезапустите Backend!

**ВАЖНО**: Перед тестированием **ОБЯЗАТЕЛЬНО** перезапустите backend в IntelliJ IDEA:
1. Остановите текущий процесс (Stop или Ctrl+F2)
2. Запустите `FinalApplication.java` заново

---

## 📮 Тестирование через Postman

### 1. Регистрация нового пользователя

**POST** `http://localhost:8008/api/auth/register`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "email": "test@example.com",
  "password": "password123",
  "firstName": "Test",
  "lastName": "User",
  "phone": "+1234567890"
}
```

**Ожидаемый ответ** (201 Created):
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "phone": "+1234567890",
    "role": "USER",
    "isActive": true,
    "isBlocked": false,
    "createdAt": "2025-12-21T20:00:00",
    "updatedAt": "2025-12-21T20:00:00"
  }
}
```

**Скопируйте токен!** Он понадобится для других запросов.

---

### 2. Вход в систему

**POST** `http://localhost:8008/api/auth/login`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

**Ожидаемый ответ** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "email": "test@example.com",
    ...
  }
}
```

---

### 3. Получить текущего пользователя (требуется авторизация)

**GET** `http://localhost:8008/api/auth/me`

**Headers**:
```
Authorization: Bearer ВАШ_ТОКЕН_СЮДА
Content-Type: application/json
```

**Ожидаемый ответ** (200 OK):
```json
{
  "id": 1,
  "email": "test@example.com",
  "firstName": "Test",
  "lastName": "User",
  "role": "USER",
  ...
}
```

---

### 4. Получить список ресторанов (публичный endpoint)

**GET** `http://localhost:8008/api/restaurants`

**Ожидаемый ответ** (200 OK):
```json
[]
```
(пустой массив, так как еще нет ресторанов)

---

## ❌ Если всё ещё 403:

### Проверьте логи backend:
В консоли IntelliJ IDEA должно быть:
```
Tomcat started on port(s): 8008 (http)
```

И при запросе к `/api/auth/login` должно быть:
```
DEBUG o.s.security.web.FilterChainProxy : Securing POST /api/auth/login
DEBUG o.s.s.w.a.AnonymousAuthenticationFilter : Set SecurityContextHolder to anonymous
```

### Проверьте Postman:
1. Убедитесь что URL правильный: `http://localhost:8008/api/auth/login`
2. Убедитесь что Headers содержат `Content-Type: application/json`
3. Убедитесь что Body имеет тип `raw` и формат `JSON`

### Попробуйте через curl:

Windows PowerShell:
```powershell
Invoke-WebRequest -Uri "http://localhost:8008/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"email":"test@example.com","password":"password123"}'
```

Linux/Mac:
```bash
curl -X POST http://localhost:8008/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

---

## 🎯 Все API endpoints

### Auth
- POST `/api/auth/register` - Регистрация (публично)
- POST `/api/auth/login` - Вход (публично)
- GET `/api/auth/me` - Текущий пользователь (требуется токен)

### Restaurants
- GET `/api/restaurants` - Список (публично)
- GET `/api/restaurants/{id}` - Детали (публично)
- POST `/api/restaurants` - Создать (требуется роль OWNER)

### Users
- GET `/api/users/{id}` - По ID (требуется токен)
- GET `/api/users` - Список (требуется токен)

### Reservations
- GET `/api/reservations` - Мои бронирования (требуется токен)
- POST `/api/reservations` - Создать (требуется токен)

### Reviews
- GET `/api/reviews` - Список (публично)
- POST `/api/reviews` - Создать отзыв (требуется токен)

### Admin
- GET `/api/admin/users` - Все пользователи (роль ADMIN)
- POST `/api/admin/users/{id}/block` - Заблокировать (роль ADMIN)

---

## 🔐 Как использовать токен в Postman:

1. После успешного login скопируйте `token` из ответа
2. В новом запросе перейдите на вкладку **Headers**
3. Добавьте заголовок:
   - Key: `Authorization`
   - Value: `Bearer ВАШ_ТОКЕН` (не забудьте пробел после Bearer!)

Или используйте Auth:
1. Вкладка **Authorization**
2. Type: `Bearer Token`
3. Вставьте токен

---

## ✅ Готово!

Если всё работает:
- Регистрация возвращает 201
- Login возвращает 200 с токеном
- `/api/auth/me` с токеном возвращает 200
- Публичные endpoints (restaurants) доступны без токена

Если не работает - напишите какую ошибку видите!

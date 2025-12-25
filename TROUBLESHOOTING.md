# Решение проблем с авторизацией

## Ошибка 403 при /api/auth/login

### Что было исправлено:

1. **CORS в SecurityConfig**
   - Добавлена конфигурация CORS в Spring Security
   - Разрешены запросы с `http://localhost:3000`
   - Включена поддержка credentials

2. **WebConfig**
   - Обновлены настройки CORS
   - Изменено с `allowedOrigins("*")` на `allowedOrigins("http://localhost:3000")`
   - Добавлено `allowCredentials(true)`

### Как перезапустить проект:

#### 1. Остановите текущий процесс
В IntelliJ IDEA нажмите красную кнопку "Stop" или Ctrl+F2

#### 2. Перезапустите backend
- Найдите `FinalApplication.java`
- Правой кнопкой → Run 'FinalApplication'

#### 3. Проверьте что сервер запустился
Должно быть в консоли:
```
Tomcat started on port(s): 8008 (http) with context path '/api'
```

#### 4. Проверьте доступность API
Откройте браузер или Postman:
```
http://localhost:8008/api/auth/me
```
Должно вернуть 401 (это нормально, значит сервер работает)

### Тест авторизации через Postman:

#### POST http://localhost:8008/api/auth/register
Body (JSON):
```json
{
  "email": "test@example.com",
  "password": "password123",
  "firstName": "Test",
  "lastName": "User",
  "phone": "+1234567890"
}
```

Ожидаемый ответ (200):
```json
{
  "token": "eyJhbGc...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "role": "USER"
  }
}
```

#### POST http://localhost:8008/api/auth/login
Body (JSON):
```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

### Проверка из браузера (React):

1. Откройте DevTools (F12)
2. Перейдите на вкладку **Network**
3. Попробуйте залогиниться
4. Найдите запрос `/api/auth/login`
5. Проверьте:
   - **Request Headers**: должен быть `Content-Type: application/json`
   - **Response Status**: должен быть **200 OK** (не 403!)
   - **Response**: должен содержать `token` и `user`

### Если всё ещё 403:

#### Проверьте логи backend:
```
logging.level.org.springframework.security=DEBUG
```

В логах должно быть:
```
DEBUG o.s.security.web.FilterChainProxy : Securing POST /auth/login
DEBUG o.s.s.w.a.AnonymousAuthenticationFilter : Set SecurityContextHolder to anonymous SecurityContext
```

#### Проверьте что CORS работает:
В DevTools → Network → запрос `/api/auth/login` → Headers:
- **Access-Control-Allow-Origin**: `http://localhost:3000`
- **Access-Control-Allow-Credentials**: `true`

#### Очистите кеш браузера:
- Ctrl + Shift + Delete
- Удалите cookies и cache
- Перезапустите браузер

### Если база данных не работает:

```sql
-- Проверьте подключение
SELECT version();

-- Создайте базу заново
DROP DATABASE IF EXISTS kako;
CREATE DATABASE kako;
```

### Альтернативный способ тестирования (curl):

```bash
# Регистрация
curl -X POST http://localhost:8008/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "phone": "+1234567890"
  }'

# Логин
curl -X POST http://localhost:8008/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### React Router warnings (не критично):

Предупреждения о `v7_startTransition` и `v7_relativeSplatPath` можно игнорировать.
Это просто уведомления о будущих изменениях в React Router v7.

Чтобы убрать их, обновите `App.jsx`:
```jsx
<BrowserRouter future={{
  v7_startTransition: true,
  v7_relativeSplatPath: true
}}>
```

## Успешная авторизация

Если всё работает правильно:
1. В DevTools → Application → Local Storage должен появиться `token`
2. В консоли браузера НЕ должно быть ошибок
3. После логина должен быть редирект на `/restaurants`

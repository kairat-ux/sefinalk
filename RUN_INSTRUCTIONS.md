# Инструкция по запуску проекта

## Требования
- Java 17
- PostgreSQL
- Node.js и npm (для фронтенда)

## Настройка базы данных

1. Запустите PostgreSQL
2. Создайте базу данных:
```sql
CREATE DATABASE kako;
```

3. Проверьте настройки в `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/kako
spring.datasource.username=postgres
spring.datasource.password=password
```

## Запуск через IntelliJ IDEA

### Backend (Spring Boot)

1. Откройте проект в IntelliJ IDEA
2. Найдите класс `FinalApplication.java` в папке `src/main/java/com/example/demo/`
3. Нажмите правой кнопкой мыши на файл
4. Выберите `Run 'FinalApplication'`

Сервер запустится на `http://localhost:8008`
API доступен по адресу: `http://localhost:8008/api`

### Альтернативный способ через Gradle

```bash
cd final
./gradlew bootRun
```

### Frontend (React + Vite)

1. Откройте терминал в папке `final/frontend`
2. Установите зависимости (если ещё не установлены):
```bash
npm install
```

3. Запустите фронтенд:
```bash
npm run dev
```

Фронтенд будет доступен на `http://localhost:3000`

## API Endpoints

### Авторизация
- POST `/api/auth/register` - Регистрация нового пользователя
- POST `/api/auth/login` - Вход в систему
- GET `/api/auth/me` - Получить текущего пользователя

### Рестораны
- GET `/api/restaurants` - Список ресторанов
- GET `/api/restaurants/{id}` - Детали ресторана
- POST `/api/restaurants` - Создать ресторан (требуется роль OWNER/ADMIN)

### Бронирования
- GET `/api/reservations` - Список бронирований
- POST `/api/reservations` - Создать бронирование
- GET `/api/reservations/{id}` - Детали бронирования

## Решение проблем

### Порт уже занят
Если при запуске видите ошибку "Port 8008 was already in use", нужно остановить процесс:

PowerShell:
```powershell
Get-Process -Id (Get-NetTCPConnection -LocalPort 8008).OwningProcess | Stop-Process -Force
```

Или измените порт в `application.properties`:
```properties
server.port=8009
```
И обновите порт в `frontend/src/services/api.js`:
```javascript
baseURL: 'http://localhost:8009/api'
```

### База данных не подключается
1. Проверьте что PostgreSQL запущен
2. Проверьте логин/пароль в `application.properties`
3. Убедитесь что база данных `kako` создана

### Авторизация не работает
1. Проверьте что JWT секретный ключ настроен в `application.properties`
2. Проверьте что фронтенд отправляет токен в заголовке `Authorization: Bearer <token>`
3. Откройте DevTools в браузере → Network → проверьте запросы

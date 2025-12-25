# Luxury Restaurant Reservations System

Премиум система бронирования ресторанов с красивым дизайном и полным функционалом.

## Технологии

### Backend:
- Java 17
- Spring Boot 3.2.0
- Spring Security + JWT
- PostgreSQL
- Liquibase
- MapStruct
- Lombok

### Frontend:
- React 18
- Vite
- React Router
- Axios
- Framer Motion
- React Toastify

## Настройка проекта

### Требования:
- Java 17+
- PostgreSQL 14+
- Node.js 18+
- npm или yarn

### База данных

1. Создайте базу данных PostgreSQL:
```sql
CREATE DATABASE finalSE;
```

2. База данных и таблицы создадутся автоматически через Liquibase при первом запуске

### Backend

1. Перейдите в корневую директорию проекта:
```bash
cd C:\Users\rinxo\Downloads\final\final
```

2. Запустите приложение:
```bash
./gradlew bootRun
```

Или в Windows:
```bash
gradlew.bat bootRun
```

Backend будет доступен на: `http://localhost:8080`

### Frontend

1. Перейдите в директорию фронтенда:
```bash
cd frontend
```

2. Установите зависимости:
```bash
npm install
```

3. Запустите dev-сервер:
```bash
npm run dev
```

Frontend будет доступен на: `http://localhost:3000`

## API Endpoints

### Аутентификация
- `POST /api/auth/register` - Регистрация нового пользователя
- `POST /api/auth/login` - Вход в систему
- `GET /api/auth/me` - Получить текущего пользователя

### Рестораны
- `GET /api/restaurants` - Список всех ресторанов
- `GET /api/restaurants/{id}` - Детали ресторана
- `GET /api/restaurants/city/{city}` - Рестораны по городу
- `POST /api/restaurants` - Создать ресторан (только для владельцев)
- `PUT /api/restaurants/{id}` - Обновить ресторан
- `DELETE /api/restaurants/{id}` - Удалить ресторан

### Бронирования
- `GET /api/reservations/my` - Мои бронирования
- `GET /api/reservations/{id}` - Детали бронирования
- `POST /api/reservations` - Создать бронирование
- `PUT /api/reservations/{id}` - Обновить бронирование
- `DELETE /api/reservations/{id}` - Отменить бронирование

### Пользователи
- `GET /api/users` - Список пользователей (только для админов)
- `GET /api/users/{id}` - Детали пользователя
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

## Роли пользователей

- **USER** - Обычный пользователь (может бронировать столики)
- **OWNER** - Владелец ресторана (может управлять своими ресторанами)
- **ADMIN** - Администратор системы (полный доступ)

## Настройка профилей

Проект поддерживает несколько профилей:

- **dev** (по умолчанию) - Разработка
- **prod** - Продакшен
- **test** - Тестирование

Для запуска с определенным профилем:
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## Особенности дизайна

- Премиум цветовая палитра (золото, коричневый, кремовый)
- Адаптивный дизайн для всех устройств
- Плавные анимации и переходы
- Современные UI/UX паттерны
- Типографика с Playfair Display и Montserrat

## Структура проекта

```
final/
├── src/main/java/com/example/demo/
│   ├── config/          # Конфигурация Spring
│   ├── controller/      # REST контроллеры
│   ├── dto/            # Data Transfer Objects
│   ├── entity/         # JPA сущности
│   ├── exception/      # Обработка исключений
│   ├── mapper/         # MapStruct маперы
│   ├── repository/     # JPA репозитории
│   ├── security/       # Безопасность и JWT
│   ├── service/        # Бизнес-логика
│   └── util/           # Утилиты
├── src/main/resources/
│   ├── db/changelog/   # Liquibase миграции
│   └── application-*.yml
└── frontend/
    ├── src/
    │   ├── components/ # React компоненты
    │   ├── pages/      # Страницы
    │   ├── services/   # API сервисы
    │   ├── context/    # React Context
    │   └── styles/     # Глобальные стили
    └── package.json
```

## Разработка

Для разработки рекомендуется:

1. Запустить backend в dev-режиме
2. Запустить frontend с hot-reload
3. Использовать PostgreSQL локально

## Production Deployment

1. Собрать backend:
```bash
./gradlew build
```

2. Собрать frontend:
```bash
cd frontend && npm run build
```

3. Развернуть JAR файл и собранный frontend на сервере

## Troubleshooting

**Проблема**: База данных не создается
**Решение**: Убедитесь что PostgreSQL запущен и доступен на localhost:5432

**Проблема**: Frontend не подключается к backend
**Решение**: Проверьте что backend запущен на порту 8080

**Проблема**: Ошибки JWT
**Решение**: Очистите localStorage в браузере

## Лицензия

MIT License

## Контакты

Для вопросов и предложений создайте issue в репозитории.

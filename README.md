# testproj

## Описание
Проект представляет собой систему управления документами с использованием **Java Spring Boot** на **backend**, **Vue.js** на **frontend**, а также **PostgreSQL** в качестве базы данных.

## Требования
Перед установкой убедитесь, что у вас установлены следующие зависимости:

### Backend
- **Java 17+** ([скачать](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- **OpenJDK 17+** ([скачать](https://adoptium.net/))
- **Maven 3+** ([скачать](https://maven.apache.org/download.cgi))
- **PostgreSQL 14+** ([скачать](https://www.postgresql.org/download/))

### Frontend
- **Node.js 16+** ([скачать](https://nodejs.org/en))
- **Vue CLI** ([установка](https://v3.ru.vuejs.org/ru/guide/installation.html#cli))
- **Дополнительные npm-пакеты:**
  - `@fortawesome/fontawesome-free@6.7.2`
  - `@vitejs/plugin-vue@5.2.1`
  - `axios@1.7.9`
  - `font-awesome@4.7.0`
  - `vite-plugin-vue-devtools@7.7.1`
  - `vite@6.1.0`
  - `vue-router@4.5.0`
  - `vue@3.5.13`

## Установка и настройка

### 1. Импорт базы данных
1. Скачать файл `db-testproj.sql` из репозитория ([GitHub](https://github.com/zhoracementov/testproj)).
2. Импортировать базу данных в **PostgreSQL** с помощью **pgAdmin 4** или консоли:
   ```sh
   psql -U postges -d postgres -f db-testproj.sql
   ```

### 2. Клонирование репозитория
```sh
git clone https://github.com/zhoracementov/testproj.git
cd testproj
```

### 3. Настройка папки для хранения файлов
По умолчанию файлы хранятся в папке `backend-java-spring/uploads` внутри корневой директории проекта. Для изменения пути откройте файл `application.properties` в папке `backend-java-springsrc\main\resources` и измените переменную окружения:
```sh
file.upload-dir=${YOUR_NEW_PATH}
```

### 4. Запуск Backend
```sh
cd backend-java-spring
mvn clean install
mvn spring-boot:run
```

### 5. Запуск Frontend
```sh
cd ../frontend-vue
npm install
npm run dev
```

### 6. Открытие в браузере
После успешного запуска **backend** и **frontend**, откройте браузер и перейдите по адресу:
```
http://localhost:5173
```

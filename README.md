# 🔗 URL Shortener

A full-stack **URL Shortener** built with **Spring Boot (Java)** and **React**.  
Users can shorten long URLs, choose custom aliases(up to 15 characters), and instantly check alias availability — all with real-time validation, QR code generation and a sleek responsive UI.

---

## 🚀 Features

✅ **Shorten URLs instantly** using a clean REST API

✅ **Custom aliases** — users can pick their own short codes (max length: 15)

✅ **Live alias availability check** (frontend + backend integration)

✅ **Smart reusability**

- If the same long URL is shortened again, the _existing short code_ is returned
- If a user later adds a _custom alias_ for the same URL, it replaces the old code

✅ **TTL / Expiration** — short URLs are valid for 1 month

✅ **Automatic redirect** to the original URL

✅ **QR Code generation** for each shortened URL (using ZXing)

✅ **CORS-enabled backend** for easy frontend integration

✅ **Beautiful UI** with gradient design, instant feedback, and responsive layout

---

## 🧩 Tech Stack

### 🖥️ Frontend

- **React.js** (Hooks)
- **Axios** for communication with backend
- **Debounce + in-memory caching** for live alias checks
- **Clipboard API** for “Copy Link” functionality
- Styled using plain **CSS** and inline styles
- Environment-based Base URL (can point to **localhost:8080** or your **ngrok URL**)

### ⚙️ Backend

- **Java 8**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Hibernate**
- **Databases Supported**:
  - **Oracle Database** (26ai Free / 23ai / 19c via `ojdbc8`)
  - **MySQL** (via `mysql-connector-java`)
- **CORS Configuration** via `@CrossOrigin`
- **ZXing** for QR code generation
- Environment-based Base URL stored in **application.properties** in backend and **.env** in frontend (can be localhost:8080 or ngrok URL)

---

## 🛠️ Setup Instructions

### 🧑‍💻 Backend

1. Import the **backend** folder into Eclipse ( or any IDE of your choice.)
2. **Switch Database Profile in `application.yml`:**
   Open `src/main/resources/application.yml` and set the active profile:

   ```yaml
   spring:
     profiles:
       active: mysql   # Change to 'oracle' to use Oracle Database Free
   ```

   - **MySQL Profile (`active: mysql`):**
     Configuration loaded from `src/main/resources/application-mysql.properties`.
   - **Oracle Profile (`active: oracle`):**
     Configuration loaded from `src/main/resources/application-oracle.properties`.
     Before running Oracle for the first time, execute the SQL scripts in `database/oracle/`:
     - As SYSDBA: `@database/oracle/01_sysdba_setup.sql` (creates `URLAPP` user on `FREEPDB1`)
     - As URLAPP: `@database/oracle/02_create_tables.sql` (creates `URL_MAPPING` table)
     - (Optional) `@database/oracle/03_data_migration_template.sql` (sample/migrated data)

3. **Run the Spring Boot App:**
   - In your IDE: Run `mainn/UrlShortenerApplication.java` as a Java/Spring Boot Application.
   - Or from terminal:
     ```bash
     mvn spring-boot:run
     # For Oracle:
     mvn spring-boot:run -Dspring-boot.run.profiles=oracle -DORACLE_DB_PASSWORD=yourpassword
     ```
     The server starts on `http://localhost:8080`.

### 🌐 Frontend Setup

1. Navigate to the frontend app directory:
   ```bash
   cd frontend/app
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Verify `.env`:
   ```properties
   VITE_API_BASE_URL=http://localhost:8080/api
   ```
4. Start the React app:
   ```bash
   npm run dev
   ```
   Visit `http://localhost:5173` in your browser.

## 🚀 Tech Stack Summary

### Frontend: React, JavaScript, HTML5, CSS3, Vite

### Backend: Java 8, Spring Boot 2.7, Spring Data JPA, REST API

### Database: Oracle Database Free (26ai) / MySQL

### Build Tools: Maven, npm

## 🧩 Future Enhancements

📅 Configurable expiration dates for short links (currently 1 month)

🛡️ Input sanitization and rate limiting

📊 Click tracking and analytics dashboard

🌍 Public API documentation (Swagger)

## 👨‍💻 Author

**Sagar Janjoted**  
📍 Mumbai, India

- 🔗 [LinkedIn](https://www.linkedin.com/in/sagar-janjoted-100912svj)
- 💻 [GitHub](https://github.com/STRO09)

⭐ If you like this project, give it a star on GitHub!

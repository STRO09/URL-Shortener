# 🔗 URL Shortener

A full-stack **URL Shortener** built with **Spring Boot (Java)** and **React**.  
Users can shorten long URLs, choose custom aliases(up to 15 characters), and instantly check alias availability — all with real-time validation, QR code generation and a sleek responsive UI.

---

## 🚀 Features

✅ **Shorten URLs instantly** using a clean REST API  
✅ **Custom aliases** — users can pick their own short codes (max length: 15)
✅ **Live alias availability check** (frontend + backend integration)  
✅ **Smart reusability**
   - If the same long URL is shortened again, the *existing short code* is returned  
   - If a user later adds a *custom alias* for the same URL, it replaces the old code
✅ **TTL / Expiration** — short URLs are valid for 1 month
✅ **Automatic redirect** to the original URL
✅ **QR Code generation** for each shortened URL (using ZXing)
✅ **CORS-enabled backend** for easy frontend integration  
✅ **Beautiful UI** with gradient design, instant feedback, and responsive layout

---

## 🧩 Tech Stack

### 🖥️ Frontend
- **React.js** (Hooks)
- **Fetch API** for backend communication
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
- **MySQL** (for persistent storage)
- **CORS Configuration** via `@CrossOrigin`
- **ZXing** for QR code generation
- Environment-based Base URL stored in **application.properties** in backend and **.env** in frontend (can be localhost:8080 or ngrok URL)

---

## 🛠️ Setup Instructions

### 🧑‍💻 Backend

1. Import the **backend** folder into Eclipse ( or any IDE of your choice.)
2. Update your database details in `application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/urlshortener
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   app.base-url=http://localhost:8080/api   # or your ngrok URL

3. Run the Spring Boot app:
   run the mainn/**UrlShortenerApplication** as application.
The server starts on http://localhost:8080 (or your ngrok URL).

### 🌐 Frontend

1. Navigate to the frontend folder.
2. Install dependencies:

   ```properties
   npm install

3. Add .env 
   ```doesanythingworkhere?
   VITE_BASE_URL=http://localhost:8080/api   # or your ngrok URL

4. Start the React app:
   ```properties
   npm run dev
Visit http://localhost:5173


## 🚀 Tech Stack
### Frontend: React, JavaScript, HTML5, CSS3
### Backend: Java, Spring Boot, Spring Data JPA, REST API
### Database: MySQL
### Build Tools: Maven, Vite

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



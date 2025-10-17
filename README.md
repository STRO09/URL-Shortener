# 🔗 URL Shortener

A full-stack **URL Shortener** built with **Spring Boot (Java)** and **React**.  
Users can shorten long URLs, choose custom aliases, and instantly check alias availability — all with real-time validation and a sleek responsive UI.

---

## 🚀 Features

✅ **Shorten URLs instantly** using a clean REST API  
✅ **Custom aliases** — users can pick their own short codes  
✅ **Live alias availability check** (frontend + backend integration)  
✅ **Smart reusability**
   - If the same long URL is shortened again, the *existing short code* is returned  
   - If a user later adds a *custom alias* for the same URL, it replaces the old code  
✅ **Automatic redirect** to the original URL  
✅ **CORS-enabled backend** for easy frontend integration  
✅ **Beautiful UI** with gradient design, instant feedback, and responsive layout

---

## 🧩 Tech Stack

### 🖥️ Frontend
- **React.js** (Hooks)
- **Fetch API** for backend communication
- **Debounce + caching** for live alias checks
- **Clipboard API** for “Copy Link” functionality
- Styled using plain **CSS** and inline styles

### ⚙️ Backend
- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Hibernate**
- **MySQL** (for persistent storage)
- **CORS Configuration** via `@CrossOrigin`

---

## 🛠️ Setup Instructions

### 🧑‍💻 Backend

1. Navigate to the **backend** folder.
2. Update your database details in `application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/urlshortener
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
Run the Spring Boot app:

bash
Copy code
mvn spring-boot:run
The server starts on http://localhost:8080

🌐 Frontend
Navigate to the frontend folder.

Install dependencies:

bash
Copy code
npm install
Start the React app:

bash
Copy code
npm run dev
Visit http://localhost:5173

🧠 How It Works
User enters a long URL (and optionally a custom alias).

Frontend checks alias availability in real time.

Backend logic:

Generates a random short code if no alias is provided.

Returns the existing code if the URL was shortened before.

Replaces the existing short code with the new alias if the user adds one later.

URLs are stored in a MySQL database and fetched during redirect.

🧾 Features
✅ Create short URLs instantly
✅ Custom alias support
✅ Live alias availability check
✅ Reuse or update shortcodes if the same URL is re-entered
✅ MySQL database integration
✅ Redirect logic with full backend validation

🚀 Tech Stack
Frontend: React, JavaScript, HTML5, CSS3
Backend: Java, Spring Boot, Spring Data JPA, REST API
Database: MySQL
Build Tools: Maven, Vite
Others: Fetch API, JSON, CORS

🧩 Future Enhancements
📅 Expiration dates for short links

🧾 QR code generation for each shortened link

🛡️ Input sanitization and rate limiting

📊 Click tracking and analytics dashboard

🌍 Public API documentation (Swagger)

💻 Demo Screenshots
(Add screenshots here once you have your UI finalized)
Example:

👨‍💻 Author
Sagar Janjoted
📍 Mumbai, India

🔗 LinkedIn

💻 GitHub

⭐ If you like this project, give it a star on GitHub!
## 🏗️ Project Structure


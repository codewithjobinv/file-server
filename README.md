# Spring Boot File Server

A secure, configurable file upload & download server built using **Spring Boot**, **Thymeleaf**, and **Spring Security**.  
This repository is designed for learning, experimentation, and small-team use.

---

## 📌 Features

- 🔐 **Username/password authentication** (credentials via environment variables)
- 📁 **File uploads** via web UI  
- 📥 **File downloads** with safe path validation  
- 💾 **Configurable storage directory**  
- 📊 **Upload progress bar** (AJAX-powered)
- 🚀 **Run locally OR expose using ngrok**  
- 🔧 **Override configurations at runtime**  
- 🧹 **Secure-by-default** (no secrets in Git, validated file paths)

---

## 🏗 Project Structure

```
file-server/
 ├─ src/main/java/com/example/fileserver
 │   ├─ FileServerApplication.java     # Main entry point
 │   ├─ FileController.java            # Handles upload/download logic
 │   ├─ SecurityConfig.java            # Authentication config
 │
 ├─ src/main/resources
 │   ├─ templates/upload.html          # UI template
 │   ├─ application.properties         # Base configuration (no secrets)
 │
 ├─ pom.xml                            # Maven build file
 └─ README.md
```

---

## ⚙️ How the App Works (High-Level)

### 🔹 1. User accesses `/`
- Spring Security asks for login  
- After login, the controller loads `upload.html`  
- File list is fetched from storage directory  

### 🔹 2. User uploads a file
- AJAX request sends file → `/upload`
- Spring receives it as `MultipartFile`
- File is validated and stored on disk
- Page reloads showing updated file list  

### 🔹 3. User downloads a file
- Request goes to `/download/{fileName}`
- Path is validated to prevent directory traversal
- File is streamed back to user

---

## 🔐 Security

### ✔ No secrets in code  
Credentials are injected via environment variables:

- `APP_USER`
- `APP_PASSWORD`

### ✔ Safe file handling  
- Path validation prevents escaping upload directory  
- Upload size limits configurable  
- Uploads never stored inside classpath  
- Users see only what exists in the storage folder  

### ✔ Strong defaults  
- Must configure password manually  
- CSRF disabled for now (simple app)
- HTTPS recommended if running outside local machine

---

## 🛠️ Installation

### 1. Clone the repo

```
git clone https://github.com/codewithjobinv/file-server.git
cd file-server
```

### 2. Build the JAR

```
mvn clean package
```

Your JAR will be generated at:

```
target/file-server-0.0.1-SNAPSHOT.jar
```

---

## 🚀 Run the App

### ✔ macOS / Linux

```
APP_USER=friend APP_PASSWORD=StrongPass123 \
java -jar target/file-server-0.0.1-SNAPSHOT.jar \
  --file.storage.location=/Users/yourname/uploads
```

### ✔ Windows PowerShell

```powershell
$env:APP_USER="friend"
$env:APP_PASSWORD="StrongPass123"
java -jar target/file-server-0.0.1-SNAPSHOT.jar --file.storage.location="C:\uploads"
```

Then open:

```
http://localhost:8080
```

Login using your configured username/password.

---

## 🌍 Expose the App Publicly (Optional)

You can use **ngrok** to share your app with others:

```
ngrok http 8080
```

ngrok will give you an HTTPS URL like:

```
https://xxxxx.ngrok-free.app
```

Share this URL with anyone who should upload/download files.

---

## ⚙️ Configuration Reference

### `application.properties`

```
server.port=8080

file.storage.location=/tmp/uploads

spring.servlet.multipart.max-file-size=200MB
spring.servlet.multipart.max-request-size=200MB

app.security.user.name=${APP_USER}
app.security.user.password=${APP_PASSWORD}
```

### Override at runtime

```
--file.storage.location=/custom/folder
```

### Override via environment variables

```
SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE=500MB
SPRING_SERVLET_MULTIPART_MAX_REQUEST_SIZE=500MB
SERVER_PORT=9090
```

---

## 🧪 Development Mode (Hot Reload)

```
APP_USER=friend APP_PASSWORD=StrongPass123 mvn spring-boot:run
```

---

## 🤝 Contributing

We welcome contributions!

### 1. Fork this repository

Click **Fork** on GitHub.

### 2. Clone your fork

```
git clone https://github.com/<your-username>/file-server.git
cd file-server
```

### 3. Create a feature branch

```
git checkout -b feature/your-feature-name
```

### 4. Make changes & commit

```
git add .
git commit -m "Add feature XYZ"
```

### 5. Push your branch

```
git push -u origin feature/your-feature-name
```

### 6. Open a Pull Request on GitHub

Describe:
- What you added  
- Why  
- Any testing steps  

---

## 🗺️ Roadmap (Future Enhancements)

- Per-user private storage areas  
- Role-based access control  
- File type restrictions (safe uploads only)  
- Better-looking UI (Bootstrap)  
- Upload history & audit logs  
- Optional JWT authentication  
- Cloud deployment version (AWS EC2/S3)

---

## 📄 License

MIT License – feel free to use and extend.

---

## 👨‍💻 Author

**Jobin Varghese (codewithjobinv)**  
A Java Developer exploring Spring Boot, security best practices, and backend engineering.

**Connect with me on LinkedIn:** [Jobin Varghese](https://www.linkedin.com/in/jobin-varghese-thonnurinchira/)

---

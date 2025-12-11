# 🚀 How to Fix ERR_CONNECTION_REFUSED Error

## Problem

The frontend at `http://localhost:3000` cannot connect to the backend at `http://localhost:8080` because the backend server is not running.

## ✅ Solution: Start the Backend Server

### **Method 1: Using VS Code (RECOMMENDED - EASIEST)**

1. **Open the file**: `Eight-Queens/Eight-Queens/src/main/java/com/PDSA/Eight_Queens/EightQueensApplication.java`

2. **Look for the green "Run" button** above the `main()` method (line ~8):

   ```java
   public static void main(String[] args) {  // ← Green "Run" button here
       SpringApplication.run(EightQueensApplication.class, args);
   }
   ```

3. **Click the "Run" button** or **Right-click** → **"Run Java"**

4. **Wait for the server to start**. You'll see in the terminal:

   ```
   Started EightQueensApplication in X.XXX seconds
   ```

5. **Refresh your browser** at `http://localhost:3000`

---

### **Method 2: Using PowerShell Terminal**

If you have Maven working properly:

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
mvn spring-boot:run
```

**If Maven has plugin issues**, try:

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
mvn clean compile
# Then use Method 1 (VS Code Run button)
```

---

### **Method 3: Using IntelliJ IDEA (if installed)**

1. Open the project in IntelliJ IDEA
2. Navigate to `EightQueensApplication.java`
3. Click the green play button next to the class or main method
4. Wait for "Started EightQueensApplication..."

---

## 🔍 Verify Backend is Running

**Test in browser or PowerShell:**

```powershell
# PowerShell
Test-NetConnection -ComputerName localhost -Port 8080
```

Or open in browser:

```
http://localhost:8080/api/eight-queens/health
```

Should return: **"Eight Queens API is running"**

---

## ✅ Pre-Verification Checklist

Before starting the backend, ensure:

- ✅ **MySQL is running on port 4306**

  ```powershell
  Test-NetConnection -ComputerName localhost -Port 4306
  ```

  Should return: `True`

- ✅ **Database `eight_queens` exists** (create if not):

  ```sql
  CREATE DATABASE IF NOT EXISTS eight_queens;
  ```

- ✅ **Java 21 is installed**:
  ```powershell
  java -version
  ```
  Should show version 21

---

## 🐛 If Problems Persist

### Error: "Cannot connect to database"

- Start MySQL/XAMPP
- Ensure port 4306 is correct in `application.properties`
- Check username/password (default: root with no password)

### Error: "Port 8080 already in use"

- Stop other applications using port 8080:
  ```powershell
  netstat -ano | findstr :8080
  taskkill /PID <PID_NUMBER> /F
  ```

### Maven Plugin Errors

- **Ignore Maven errors** - Just use **VS Code's Run button** instead
- The compiled classes are already in `target/classes`
- VS Code Java Extension runs the application without Maven

---

## 📋 Quick Reference

### API Endpoints (Backend must be running):

- Health: `GET http://localhost:8080/api/eight-queens/health`
- Statistics: `GET http://localhost:8080/api/eight-queens/statistics`
- Computations: `GET http://localhost:8080/api/eight-queens/computations`
- Scoreboard: `GET http://localhost:8080/api/eight-queens/scoreboard`
- Submit Solution: `POST http://localhost:8080/api/eight-queens/submit`

### Ports:

- **Frontend**: http://localhost:3000
- **Backend**: http://localhost:8080
- **MySQL**: localhost:4306

---

## 🎯 Summary

**The issue is simply that the backend isn't running.**

**Quickest fix:**

1. Open `EightQueensApplication.java` in VS Code
2. Click the green **"Run"** button above `main()` method
3. Wait for "Started EightQueensApplication..."
4. Refresh your browser

**That's it!** The ERR_CONNECTION_REFUSED errors will disappear. ✨

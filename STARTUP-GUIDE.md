# 🚀 Eight Queens Game - Startup Guide

## ⚠️ Issue: ERR_CONNECTION_REFUSED

The error you're seeing means the **backend server is not running** on `http://localhost:8080`.

---

## 📋 Prerequisites

Before starting, ensure you have:

✅ **Java 21** installed  
✅ **Maven 3.9+** installed  
✅ **Node.js 18+** installed  
✅ **npm** installed

---

## 🎯 Quick Start (Easiest Method)

### Option 1: Using Batch Scripts

#### Step 1: Start Backend

1. Double-click `START-BACKEND.bat` in the root folder
2. Wait for message: "Started EightQueensApplication"
3. Server will run on `http://localhost:8080`

#### Step 2: Start Frontend

1. Double-click `START-FRONTEND.bat` in the root folder
2. Wait for message: "Compiled successfully!"
3. Browser will open at `http://localhost:3000`

---

## 🔧 Manual Startup (Alternative Method)

### Backend (Spring Boot)

#### Option A: Using Maven

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
mvn clean compile
mvn spring-boot:run
```

#### Option B: Using IDE (VS Code/IntelliJ)

1. Open `Eight-Queens\Eight-Queens` folder
2. Right-click on `EightQueensApplication.java`
3. Select "Run Java" or "Debug Java"

#### Option C: Build and Run JAR

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
mvn clean package -DskipTests
java -jar target\Eight-Queens-0.0.1-SNAPSHOT.jar
```

**Expected Output:**

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

Started EightQueensApplication in X.XXX seconds
```

### Frontend (React)

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\gameinterfaces"
npm install
npm start
```

**Expected Output:**

```
Compiled successfully!

You can now view gameinterfaces in the browser.

  Local:            http://localhost:3000
  On Your Network:  http://192.168.x.x:3000
```

---

## 🔍 Troubleshooting

### 1. Backend Won't Start

#### Problem: "Failed to execute goal spring-boot-maven-plugin"

```powershell
# Solution: Update Maven settings
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
mvn clean
mvn compile
mvn spring-boot:run
```

#### Problem: "Port 8080 already in use"

```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual number)
taskkill /PID <PID> /F

# Or change port in application.properties
# server.port=8081
```

#### Problem: Database connection error

- **Already Fixed!** Application now uses H2 in-memory database
- No MySQL installation required
- Database creates automatically on startup

### 2. Frontend Won't Connect

#### Problem: "net::ERR_CONNECTION_REFUSED"

**Cause:** Backend is not running

**Solution:**

1. Check if backend is running (see backend startup above)
2. Verify backend URL in browser: `http://localhost:8080/api/eight-queens/health`
3. Should return: "Eight Queens API is running"

#### Problem: "CORS error"

**Already Fixed!** WebConfig.java has CORS enabled for `http://localhost:3000`

### 3. npm Errors

#### Problem: "npm: command not found"

```powershell
# Check Node.js installation
node --version
npm --version

# If not installed, download from: https://nodejs.org/
```

#### Problem: "Module not found"

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\gameinterfaces"
rm -rf node_modules
rm package-lock.json
npm install
npm start
```

---

## ✅ Verification Steps

### 1. Check Backend is Running

Open browser and visit:

```
http://localhost:8080/api/eight-queens/health
```

Should return: `"Eight Queens API is running"`

### 2. Check H2 Database Console

```
http://localhost:8080/h2-console
```

- JDBC URL: `jdbc:h2:mem:eight_queens`
- Username: `sa`
- Password: (leave empty)

### 3. Test API Endpoints

```
GET http://localhost:8080/api/eight-queens/statistics
GET http://localhost:8080/api/eight-queens/scoreboard
GET http://localhost:8080/api/eight-queens/computations
```

### 4. Check Frontend

```
http://localhost:3000
```

Should show the Eight Queens game with green theme!

---

## 📊 Current Configuration

### Backend

- **Port:** 8080
- **Database:** H2 In-Memory (auto-created)
- **API Base:** `http://localhost:8080/api/eight-queens`
- **H2 Console:** `http://localhost:8080/h2-console`

### Frontend

- **Port:** 3000
- **API Target:** `http://localhost:8080`
- **Dev Server:** React Development Server

---

## 🎮 Testing the Complete Flow

1. **Start Backend** (see instructions above)
2. **Start Frontend** (see instructions above)
3. **Open Game:** `http://localhost:3000`
4. **Enter Name:** Type your name in the input field
5. **Place Queens:** Click cells to place 8 queens
6. **Submit:** Click "Submit Solution"
7. **View Scoreboard:** Click "🏆 View Scoreboard"

---

## 🛠️ Development Mode

### Watch Mode (Auto-reload)

**Backend** (IntelliJ/VS Code):

- Use IDE's debug/run configuration
- Changes require manual restart

**Frontend** (Automatic):

```powershell
npm start
```

- Changes auto-reload in browser
- Hot Module Replacement (HMR) enabled

---

## 🔐 Security Notes

### H2 Database

- **Type:** In-memory (data lost on restart)
- **Security:** Development only, not for production
- **Console:** Enabled for debugging

### CORS

- **Allowed Origin:** `http://localhost:3000`
- **Methods:** GET, POST, PUT, DELETE, OPTIONS
- **Status:** Development configuration

---

## 📝 Important Files Changed

### Application Configuration

```
Eight-Queens/Eight-Queens/src/main/resources/application.properties
```

**Changed:** MySQL → H2 In-Memory Database

**Why:**

- No MySQL installation required
- Faster development
- Auto-creates tables
- Data resets on each restart (clean state)

---

## 🎯 Next Steps After Startup

Once both servers are running:

1. ✅ Backend health check passes
2. ✅ Frontend loads with green theme
3. ✅ No console errors
4. ✅ Can enter name and place queens
5. ✅ Can submit solutions
6. ✅ Can view scoreboard

**You're ready to play!** 🎉

---

## 🚨 Common Error Messages Explained

### "Failed to fetch"

- **Cause:** Backend not running
- **Fix:** Start backend server

### "net::ERR_CONNECTION_REFUSED"

- **Cause:** Backend not accessible on port 8080
- **Fix:** Check backend is running, check port 8080

### "CORS policy blocked"

- **Cause:** Wrong frontend port or CORS misconfiguration
- **Fix:** Use `http://localhost:3000` for frontend

### "Cannot connect to database"

- **Cause:** Database configuration issue
- **Fix:** Already fixed - using H2 in-memory

---

## 📞 Still Having Issues?

1. **Check Java Version:**

   ```powershell
   java -version
   # Should be Java 21
   ```

2. **Check Maven Version:**

   ```powershell
   mvn -version
   # Should be 3.9+
   ```

3. **Check Node Version:**

   ```powershell
   node --version
   # Should be 18+
   ```

4. **Check Ports:**

   ```powershell
   # Backend (should be free)
   netstat -ano | findstr :8080

   # Frontend (should be free)
   netstat -ano | findstr :3000
   ```

5. **Clean Everything:**

   ```powershell
   # Backend
   cd "Eight-Queens\Eight-Queens"
   mvn clean

   # Frontend
   cd "gameinterfaces"
   rm -rf node_modules
   npm install
   ```

---

## 🎨 What You Should See

### Backend Console

```
Started EightQueensApplication in 3.456 seconds (process running for 4.123)
```

### Frontend Browser

- Dark green background (matches landing page)
- Glowing green chessboard
- Stats panel with timer and moves
- Gold queen pieces
- Professional glassmorphism design

### No Errors In:

- Backend console
- Frontend console (browser DevTools)
- Network tab (browser DevTools)

---

**Last Updated:** December 11, 2025  
**Status:** H2 Database Configured ✅  
**Ready to Run:** Yes 🚀

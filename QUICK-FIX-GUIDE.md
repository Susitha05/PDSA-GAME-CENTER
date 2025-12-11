# 🔧 Quick Fix for Connection Refused Error

## ❗ The Problem

Your frontend shows: `net::ERR_CONNECTION_REFUSED` at `http://localhost:8080`

**Root Cause:** Backend server is not running due to Maven plugin dependency issue.

---

## ✅ IMMEDIATE SOLUTION

### Option 1: Run from VS Code (EASIEST)

1. **Open EightQueensApplication.java**

   - Path: `Eight-Queens\Eight-Queens\src\main\java\com\PDSA\Eight_Queens\EightQueensApplication.java`

2. **Click "Run" button** above `public static void main`

   - OR right-click → "Run Java"
   - OR press `F5`

3. **Wait for:**

   ```
   Started EightQueensApplication in X.XXX seconds
   ```

4. **Test:** Open `http://localhost:8080/api/eight-queens/health`
   - Should show: `"Eight Queens API is running"`

---

### Option 2: Fix Maven and Run

#### Step 1: Clear Maven Cache

```powershell
cd "$env:USERPROFILE\.m2"
Remove-Item -Recurse -Force repository\org\springframework\boot\spring-boot-buildpack-platform
Remove-Item -Recurse -Force repository\org\springframework\boot\spring-boot-loader-tools
```

#### Step 2: Rebuild

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
mvn clean install -U -DskipTests
```

#### Step 3: Run

```powershell
mvn spring-boot:run
```

---

### Option 3: Use IntelliJ IDEA

1. Open project in IntelliJ
2. Find `EightQueensApplication.java`
3. Click green ▶️ play button
4. Select "Run 'EightQueensApplication'"

---

## 🎯 What Changed (Already Fixed)

### Database Configuration

**Before:** MySQL on port 4306 (not running)  
**After:** H2 In-Memory Database (auto-starts)

**File:** `application.properties`

```properties
# Now using H2 - no MySQL needed!
spring.datasource.url=jdbc:h2:mem:eight_queens
spring.datasource.driverClassName=org.h2.Driver
```

**Benefits:**

- ✅ No external database required
- ✅ Automatic table creation
- ✅ Fresh start every time
- ✅ Perfect for development

---

## ✅ Verification Checklist

### 1. Backend Running

```powershell
# Check if listening on port 8080
netstat -ano | findstr :8080
# Should show LISTENING
```

### 2. API Health Check

Open in browser:

```
http://localhost:8080/api/eight-queens/health
```

Expected: `"Eight Queens API is running"`

### 3. Test Endpoints

```
http://localhost:8080/api/eight-queens/statistics
http://localhost:8080/api/eight-queens/scoreboard
http://localhost:8080/api/eight-queens/computations
```

### 4. Frontend Connection

1. Refresh `http://localhost:3000`
2. Open DevTools Console (F12)
3. No more "Failed to fetch" errors!
4. Stats should load automatically

---

## 🚀 Complete Startup Sequence

### 1. Start Backend (Choose ONE method)

**Method A: VS Code**

```
Open EightQueensApplication.java → Click Run button
```

**Method B: Command Line**

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
mvn clean compile exec:java -Dexec.mainClass="com.PDSA.Eight_Queens.EightQueensApplication"
```

**Method C: Build JAR First**

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
mvn clean package -DskipTests
java -jar target\Eight-Queens-0.0.1-SNAPSHOT.jar
```

### 2. Verify Backend

```
http://localhost:8080/api/eight-queens/health
```

### 3. Start Frontend

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\gameinterfaces"
npm start
```

### 4. Play Game!

```
http://localhost:3000
```

---

## 🐛 Still Getting Errors?

### Error: "Maven plugin resolution issue"

**Fix:** Use VS Code or IntelliJ to run instead of Maven CLI

### Error: "Port 8080 already in use"

```powershell
# Find and kill process
netstat -ano | findstr :8080
taskkill /PID <NUMBER> /F
```

### Error: "Cannot find main class"

**Fix:** Compile first

```powershell
mvn clean compile
```

### Error: "H2 driver not found"

**Fix:** Already in pom.xml, but rebuild:

```powershell
mvn clean install -U
```

---

## 📊 Expected Console Output

### Backend (Success)

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

2025-12-11T15:10:00.123  INFO 12345 --- [main] c.P.E.EightQueensApplication : Starting EightQueensApplication
2025-12-11T15:10:02.456  INFO 12345 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http)
2025-12-11T15:10:02.789  INFO 12345 --- [main] c.P.E.EightQueensApplication : Started EightQueensApplication in 3.456 seconds
```

### Frontend (Success)

```
Compiled successfully!

You can now view gameinterfaces in the browser.

  Local:            http://localhost:3000
  On Your Network:  http://192.168.x.x:3000

webpack compiled successfully
```

### Browser Console (Success)

```
No errors - clean console!
Stats loaded successfully
```

---

## 💡 Pro Tips

1. **Keep Backend Running**

   - Don't close the terminal/console
   - It needs to stay open while developing

2. **Hot Reload**

   - Frontend: Auto-reloads on file save
   - Backend: Requires manual restart

3. **Database Console**

   - Access H2: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:eight_queens`
   - Username: `sa`
   - Password: (empty)

4. **API Testing**
   - Use Postman, Insomnia, or Thunder Client
   - Base URL: `http://localhost:8080/api/eight-queens`

---

## 🎯 Summary

### What Was Wrong

- Backend not running (Maven issue)
- MySQL not available

### What Was Fixed

- ✅ Changed to H2 in-memory database
- ✅ Updated application.properties
- ✅ Created startup scripts
- ✅ Provided multiple startup methods

### Next Action

**Run backend using VS Code "Run Java" button** - This is the simplest and most reliable method!

---

**Status:** Ready to run! 🚀  
**Database:** H2 In-Memory (configured) ✅  
**CORS:** Configured for localhost:3000 ✅  
**API:** All endpoints ready ✅

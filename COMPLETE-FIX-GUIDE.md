# 🎮 Eight Queens Game - Complete Startup Guide

## 🚨 Current Issue: ERR_CONNECTION_REFUSED

**Frontend Error**: `GET http://localhost:8080/api/eight-queens/statistics net::ERR_CONNECTION_REFUSED`

**Root Cause**: The Spring Boot backend server is NOT running on port 8080.

**Solution**: Start the backend server (see below) ⬇️

---

## ✅ Status Check

### What's Working:

- ✅ **MySQL Database**: Running on port 4306
- ✅ **Frontend**: Running on port 3000 (React app)
- ✅ **Code Compiled**: All Java classes compiled successfully

### What's NOT Working:

- ❌ **Backend Server**: Not running on port 8080
- ❌ **API Endpoints**: Cannot be reached

---

## 🚀 How to Start the Backend (3 Methods)

### **Method 1: VS Code Run Button** (⭐ RECOMMENDED - EASIEST)

This is the BEST and SIMPLEST way:

1. **Open this file in VS Code**:

   ```
   Eight-Queens/Eight-Queens/src/main/java/com/PDSA/Eight_Queens/EightQueensApplication.java
   ```

2. **You'll see this code**:

   ```java
   @SpringBootApplication
   public class EightQueensApplication {

       public static void main(String[] args) {  // ← Look here!
           SpringApplication.run(EightQueensApplication.class, args);
       }
   }
   ```

3. **Click the green "Run" button** that appears **above** the `main()` method:

   - It looks like: `Run | Debug`
   - Or **right-click on the file** → **"Run Java"**

4. **Watch the terminal** - you'll see output like:

   ```
   Started EightQueensApplication in 3.456 seconds (JVM running for 4.123)
   ```

5. **Done!** The backend is now running on `http://localhost:8080`

6. **Refresh your browser** at `http://localhost:3000` - errors should be gone! ✨

---

### **Method 2: Use Existing Terminal**

You might already have a terminal called **"Run: EightQueensApplication"**:

1. Click on that terminal
2. If it's not running, use **Method 1** instead

---

### **Method 3: Maven Command** (If Maven is working)

Open PowerShell terminal:

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
mvn spring-boot:run
```

⚠️ **If you see Maven plugin errors**, this means Maven has network/plugin issues. Use **Method 1** instead.

---

## 🔍 How to Verify Backend is Running

### Option 1: Check in Browser

Open: http://localhost:8080/api/eight-queens/health

**Expected Response**: `"Eight Queens API is running"`

### Option 2: Check in PowerShell

```powershell
Test-NetConnection -ComputerName localhost -Port 8080
```

**Expected Result**: `TcpTestSucceeded : True`

### Option 3: Check Console Output

Look for this message in the terminal:

```
Tomcat started on port(s): 8080 (http)
Started EightQueensApplication in X.XXX seconds
```

---

## 📊 Application Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Your Browser                              │
│              http://localhost:3000                           │
│         (React Frontend - Eight Queens Game)                 │
└─────────────────────┬───────────────────────────────────────┘
                      │ Fetch API Calls
                      ├─ GET /api/eight-queens/statistics
                      ├─ GET /api/eight-queens/computations
                      ├─ POST /api/eight-queens/submit
                      └─ GET /api/eight-queens/scoreboard
                      ↓
┌─────────────────────────────────────────────────────────────┐
│            Spring Boot Backend (NOT RUNNING!)                │
│              http://localhost:8080                           │
│         (REST API - Eight Queens Solver)                     │
└─────────────────────┬───────────────────────────────────────┘
                      │ JDBC
                      ↓
┌─────────────────────────────────────────────────────────────┐
│              MySQL Database (RUNNING ✓)                      │
│              localhost:4306                                  │
│         (Database: eight_queens)                             │
└─────────────────────────────────────────────────────────────┘
```

**The missing piece**: Backend needs to be started! ⚠️

---

## 🎯 Step-by-Step Solution

### Step 1: Verify MySQL is Running

```powershell
Test-NetConnection -ComputerName localhost -Port 4306
```

✅ Result should be: `True`

### Step 2: Start Backend Server

Use **Method 1** (VS Code Run button) - It's the simplest!

### Step 3: Wait for Startup

Watch terminal for:

```
Started EightQueensApplication in 3.456 seconds
```

### Step 4: Verify Backend

Open: http://localhost:8080/api/eight-queens/health
Should show: `"Eight Queens API is running"`

### Step 5: Refresh Frontend

Go to: http://localhost:3000
The game should now work! All errors gone! ✨

---

## 🐛 Troubleshooting

### "Maven plugin errors"

**Solution**: Ignore Maven - Use VS Code's Run button instead

### "Port 8080 already in use"

**Solution**:

```powershell
netstat -ano | findstr :8080
taskkill /PID <number> /F
```

### "Cannot connect to database"

**Solution**:

- Start MySQL/XAMPP
- Check port 4306 is correct
- Verify database `eight_queens` exists

### "Application won't start"

**Solution**:

- Check Java version: `java -version` (should be 21)
- Check MySQL is running
- Look at error messages in terminal
- Try Method 1 again

---

## 📁 Project Structure

```
Eight-Queens/Eight-Queens/
├── src/main/java/com/PDSA/Eight_Queens/
│   ├── EightQueensApplication.java  ← START HERE (Run button)
│   ├── controller/
│   │   └── EQController.java        ← REST API endpoints
│   ├── service/
│   │   └── EQService.java           ← Business logic
│   ├── data/
│   │   ├── EightQueens.java         ← Entity model
│   │   └── EightQueensRepository.java
│   └── config/
│       └── WebConfig.java           ← CORS configuration
├── src/main/resources/
│   └── application.properties       ← Database config
└── pom.xml                          ← Maven dependencies
```

---

## 🔥 Quick Command Reference

### Check Backend Status:

```powershell
Test-NetConnection localhost -Port 8080
```

### Check MySQL Status:

```powershell
Test-NetConnection localhost -Port 4306
```

### View Running Java Processes:

```powershell
Get-Process java
```

### Stop Backend (if needed):

```powershell
# Find process using port 8080
netstat -ano | findstr :8080
# Kill it
taskkill /PID <number> /F
```

---

## 🎊 Expected Outcome

Once backend is running:

### Terminal will show:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

Started EightQueensApplication in 3.456 seconds
```

### Browser will show:

- ✅ No more ERR_CONNECTION_REFUSED errors
- ✅ Statistics panel loads with data
- ✅ Computation results display
- ✅ Game is fully functional
- ✅ Beautiful dark green theme
- ✅ Queens can be placed
- ✅ Solutions can be submitted
- ✅ Scoreboard works

---

## ✨ That's It!

The fix is simple:

1. Open `EightQueensApplication.java`
2. Click "Run" button
3. Wait for "Started EightQueensApplication..."
4. Refresh browser
5. Enjoy the game! 🎮👑

**The visual enhancements are already done. You just need to start the backend!**

# 🔧 Fix for Data Fetching Issues - Step by Step

## Root Cause

The frontend is getting `ERR_CONNECTION_REFUSED` errors because **the backend Spring Boot server is not running**.

## ✅ Current Status

### What's Working:

- ✅ MySQL database is running on port 4306
- ✅ Frontend React app is running on port 3000
- ✅ All backend code is compiled without errors
- ✅ CORS is properly configured
- ✅ All API endpoints are implemented correctly

### What's Missing:

- ❌ **Backend server is not running on port 8080**

---

## 🚀 SOLUTION: Start the Backend Server

Since you're currently viewing `EightQueensApplication.java`, here's the **SIMPLEST** way:

### ⚡ Option 1: VS Code Run Button (RECOMMENDED)

**You're already in the right file! Just:**

1. **Look at the code** around line 9-11:

   ```java
   public static void main(String[] args) {  // ← Green "Run" button here
       SpringApplication.run(EightQueensApplication.class, args);
   }
   ```

2. **Click the green "Run" button** that appears **above** the `main()` method

   - OR right-click anywhere in the file → **"Run Java"**
   - OR use keyboard shortcut: **F5**

3. **Watch the Debug Console** (bottom panel) - wait for:

   ```
   Started EightQueensApplication in 3.456 seconds (JVM running for 4.123)
   ```

4. **Refresh your browser** at `http://localhost:3000`

**✨ That's it! All data fetching issues will be resolved!**

---

### 🔄 Option 2: Use the Batch Script

Double-click: `START-BACKEND.bat` (just created in the project root)

This will:

1. Check if MySQL is running
2. Compile the code
3. Start the server in a new window
4. Open the health check page

---

### 💻 Option 3: PowerShell Command

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
mvn spring-boot:run
```

⚠️ If Maven has plugin errors, use **Option 1** instead.

---

## 🔍 Verify It's Working

### Test 1: Check Port

```powershell
Test-NetConnection -ComputerName localhost -Port 8080
```

**Expected**: `TcpTestSucceeded : True`

### Test 2: Health Check

Open in browser: http://localhost:8080/api/eight-queens/health

**Expected Response**: `"Eight Queens API is running"`

### Test 3: Test Statistics Endpoint

Open in browser: http://localhost:8080/api/eight-queens/statistics

**Expected**: JSON data with game statistics

### Test 4: Frontend Connection

1. Open: http://localhost:3000
2. Open browser console (F12)
3. **You should see NO errors**
4. Statistics panel should load with data

---

## 📋 What Will Work After Backend Starts

All these API calls will succeed:

✅ `GET /api/eight-queens/statistics` → Game statistics
✅ `GET /api/eight-queens/computations` → Computation results  
✅ `GET /api/eight-queens/scoreboard` → Top players
✅ `GET /api/eight-queens/answers` → 92 solutions
✅ `POST /api/eight-queens/submit` → Submit player solution
✅ `POST /api/eight-queens/compute/sequential` → Sequential computation
✅ `POST /api/eight-queens/compute/threaded` → Threaded computation
✅ `DELETE /api/eight-queens/reset` → Reset game

---

## 🐛 Troubleshooting

### "Port 8080 already in use"

```powershell
# Find what's using port 8080
netstat -ano | findstr :8080

# Kill that process (replace PID with actual number)
taskkill /PID <PID> /F
```

### "Cannot connect to database"

1. Verify MySQL is running:
   ```powershell
   Test-NetConnection -ComputerName localhost -Port 4306
   ```
2. Start MySQL/XAMPP if not running
3. Verify database exists:
   ```sql
   CREATE DATABASE IF NOT EXISTS eight_queens;
   ```

### "Run button doesn't appear"

1. Make sure you have "Extension Pack for Java" installed in VS Code
2. Restart VS Code
3. Try right-clicking the file → "Run Java"

### "Maven errors"

**Don't worry about Maven errors!** The VS Code Run button works independently of Maven.

---

## 🎯 Quick Summary

**The ONLY issue is that the backend isn't running.**

**Fix:** Click the **"Run"** button above the `main()` method in the file you're currently viewing.

**Result:** All data fetching issues will be instantly resolved!

---

## 📊 Architecture Overview

```
Browser (localhost:3000)
    ↓ Fetch API calls
    ✗ ERR_CONNECTION_REFUSED ← PROBLEM HERE
    ↓
Backend (localhost:8080) ← NOT RUNNING
    ↓ JDBC
MySQL (localhost:4306) ← Running ✓
```

**After starting backend:**

```
Browser (localhost:3000)
    ↓ Fetch API calls
    ✓ 200 OK ← WORKING!
    ↓
Backend (localhost:8080) ← Running ✓
    ↓ JDBC
MySQL (localhost:4306) ← Running ✓
```

---

## ⏱️ Expected Startup Time

- **First run**: 5-10 seconds
- **Subsequent runs**: 3-5 seconds

---

## 🎉 Success Indicators

You'll know it's working when you see:

1. **In VS Code Console**:

   ```
   Started EightQueensApplication in 3.456 seconds
   ```

2. **In Browser**:

   - No console errors
   - Statistics panel shows data
   - Computation results display
   - Game is fully functional

3. **Health Check Works**:
   ```
   http://localhost:8080/api/eight-queens/health
   → "Eight Queens API is running"
   ```

---

## 🚀 Next Steps After Starting

1. **Refresh browser** at http://localhost:3000
2. **Enter your name** in the input field
3. **Start playing** - place queens on the board
4. **Submit your solution**
5. **View the scoreboard**

All data fetching will work perfectly! ✨

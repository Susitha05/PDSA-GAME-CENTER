# Eight Queens Game - Quick Start Guide

## 🚀 Quick Setup (5 minutes)

### Step 1: Database Setup (1 minute)

```sql
-- Open MySQL and run:
CREATE DATABASE eight_queens;
USE eight_queens;

CREATE USER 'eq_user'@'%' IDENTIFIED BY 'strong_password';
GRANT ALL PRIVILEGES ON eight_queens.* TO 'eq_user'@'%';
FLUSH PRIVILEGES;
```

### Step 2: Start Backend (2 minutes)

```powershell
# Navigate to backend
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"

# Set Java 21
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"

# Run Spring Boot
mvn spring-boot:run
```

✅ Backend running at: http://localhost:8080

### Step 3: Start Frontend (2 minutes)

```powershell
# Open NEW terminal
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\gameinterfaces"

# Install dependencies (first time only)
npm install

# Start React
npm start
```

✅ Frontend running at: http://localhost:3000

### Step 4: Play! 🎮

1. Open browser: http://localhost:3000/eight-queens
2. Enter your name
3. Click cells to place 8 queens
4. Click "Submit Solution"
5. Try to find all 92 solutions!

## 🧪 Test the API

### Health Check

```powershell
curl http://localhost:8080/api/eight-queens/health
```

### Run Sequential Algorithm

```powershell
curl -X POST http://localhost:8080/api/eight-queens/compute/sequential
```

### Run Threaded Algorithm

```powershell
curl -X POST http://localhost:8080/api/eight-queens/compute/threaded
```

### Get Statistics

```powershell
curl http://localhost:8080/api/eight-queens/statistics
```

### Submit a Solution (Example)

```powershell
curl -X POST http://localhost:8080/api/eight-queens/submit `
  -H "Content-Type: application/json" `
  -d '{"name":"TestUser","board":[0,4,7,5,2,6,1,3]}'
```

## 📊 Features to Try

### 1. Interactive Game

- Place queens by clicking cells
- Visual feedback for attacking positions
- Real-time validation

### 2. Algorithm Comparison

- Click "Run Sequential" button
- Click "Run Threaded" button
- Click "Compare Results" to see speedup

### 3. Solution Tracking

- Watch progress: X/92 solutions found
- Try different solutions
- See if someone already found your solution

## 🛠️ Troubleshooting

### Backend won't start?

- ✅ Check MySQL is running
- ✅ Verify database credentials
- ✅ Ensure port 8080 is free

### Frontend won't start?

- ✅ Run `npm install` first
- ✅ Check backend is running
- ✅ Ensure port 3000 is free

### Can't connect to API?

- ✅ Check CORS settings
- ✅ Verify backend URL in React code
- ✅ Check browser console for errors

## 📝 Valid Solution Example

One valid solution (board array):

```
[0, 4, 7, 5, 2, 6, 1, 3]
```

This represents:

- Row 0: Queen at column 0
- Row 1: Queen at column 4
- Row 2: Queen at column 7
- Row 3: Queen at column 5
- Row 4: Queen at column 2
- Row 5: Queen at column 6
- Row 6: Queen at column 1
- Row 7: Queen at column 3

## 🎯 Project Requirements Met

✅ Interactive UI with React
✅ Spring Boot REST API
✅ MySQL database persistence
✅ Sequential algorithm implementation
✅ Threaded algorithm implementation
✅ Time tracking for both algorithms
✅ Performance comparison
✅ Player name + solution storage
✅ Duplicate detection
✅ Solution validation
✅ Input validation
✅ Exception handling
✅ Unit tests included

## 📚 Full Documentation

See `README.md` for complete documentation including:

- Detailed setup instructions
- API documentation
- Architecture explanation
- Testing guide
- Performance benchmarks

---

## Need Help?

Check the main README.md or create an issue in the repository.

Happy puzzle solving! 🎉

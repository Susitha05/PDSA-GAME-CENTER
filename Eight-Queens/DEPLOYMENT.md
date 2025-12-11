# Eight Queens Game - Deployment Checklist

## ✅ Pre-Deployment Checklist

### Database

- [ ] MySQL 8.0+ installed
- [ ] Database `eight_queens` created
- [ ] User `eq_user` created with password
- [ ] User has full privileges on `eight_queens` database
- [ ] Connection tested successfully

### Backend (Spring Boot)

- [ ] Java 21 JDK installed
- [ ] Maven 3.9+ installed
- [ ] `pom.xml` dependencies resolved
- [ ] Database credentials configured in `application.properties`
- [ ] Project compiles without errors (`mvn clean compile`)
- [ ] All tests pass (`mvn test`)
- [ ] Port 8080 is available

### Frontend (React)

- [ ] Node.js 18+ installed
- [ ] npm dependencies installed (`npm install`)
- [ ] API endpoint URL configured correctly
- [ ] Port 3000 is available
- [ ] CORS configured in backend

## 🚀 Deployment Steps

### 1. Database Setup

```sql
CREATE DATABASE eight_queens;
CREATE USER 'eq_user'@'%' IDENTIFIED BY 'strong_password';
GRANT ALL PRIVILEGES ON eight_queens.* TO 'eq_user'@'%';
FLUSH PRIVILEGES;
```

### 2. Backend Deployment

```powershell
cd Eight-Queens/Eight-Queens
mvn clean package
mvn spring-boot:run
```

### 3. Frontend Deployment

```powershell
cd gameinterfaces
npm install
npm start
```

## 🧪 Testing After Deployment

### Health Check

```powershell
curl http://localhost:8080/api/eight-queens/health
# Expected: "Eight Queens API is running"
```

### Test Sequential Algorithm

```powershell
curl -X POST http://localhost:8080/api/eight-queens/compute/sequential
# Expected: JSON with 92 solutions and execution time
```

### Test Frontend

- Open: http://localhost:3000/eight-queens
- Should see chessboard
- Click to place queens
- Submit test solution

### Test Database

```sql
USE eight_queens;
SELECT COUNT(*) FROM games;
SELECT COUNT(*) FROM computation_results;
```

## 📊 Verification Tests

### Test 1: Submit Valid Solution

```json
POST /api/eight-queens/submit
{
  "name": "Test User",
  "board": [0, 4, 7, 5, 2, 6, 1, 3]
}
```

Expected: Success message, solution saved

### Test 2: Submit Invalid Solution

```json
POST /api/eight-queens/submit
{
  "name": "Test User",
  "board": [0, 1, 2, 3, 4, 5, 6, 7]
}
```

Expected: Error message about attacking queens

### Test 3: Submit Duplicate

Submit the same solution twice.
Expected: Second attempt shows "already found" message

### Test 4: Get Statistics

```powershell
curl http://localhost:8080/api/eight-queens/statistics
```

Expected: JSON with submission counts

### Test 5: Algorithm Performance

1. Run sequential algorithm
2. Run threaded algorithm
3. Compare execution times
   Expected: Threaded should be faster

## 🔍 Common Issues

### Issue: "Connection refused" on port 8080

**Solution:**

- Check if backend is running
- Check firewall settings
- Try different port

### Issue: CORS errors in browser console

**Solution:**

- Verify WebConfig.java CORS settings
- Check frontend API URL
- Clear browser cache

### Issue: Database connection failed

**Solution:**

- Verify MySQL is running
- Check credentials in application.properties
- Test MySQL connection manually

### Issue: Tests failing

**Solution:**

- Run `mvn clean install`
- Check database is accessible
- Review test logs

## 📈 Performance Benchmarks

Expected performance on modern hardware:

| Metric    | Sequential  | Threaded   | Speedup |
| --------- | ----------- | ---------- | ------- |
| Time      | 1000-2000ms | 300-800ms  | 2-4x    |
| Solutions | 92          | 92         | Same    |
| CPU Usage | Single core | Multi-core | Higher  |

## 🎯 Success Criteria

- [ ] Backend starts without errors
- [ ] Frontend loads in browser
- [ ] Can place queens on board
- [ ] Can submit valid solutions
- [ ] Invalid solutions are rejected
- [ ] Duplicate detection works
- [ ] Statistics update correctly
- [ ] Sequential algorithm finds 92 solutions
- [ ] Threaded algorithm finds 92 solutions
- [ ] Threaded is faster than sequential
- [ ] Database persists data
- [ ] Unit tests pass (100%)

## 📝 Post-Deployment

### Monitor Logs

- Check Spring Boot console for errors
- Monitor React dev server output
- Review MySQL error logs

### Performance Monitoring

- Monitor API response times
- Check database query performance
- Track memory usage

### Data Management

```sql
-- View all solutions
SELECT * FROM games ORDER BY created_at DESC LIMIT 10;

-- View computation results
SELECT * FROM computation_results ORDER BY created_at DESC;

-- Clear game data (keep computation results)
DELETE FROM games;
```

## 🔐 Security Notes

### For Production Deployment:

- [ ] Change database password
- [ ] Use environment variables for credentials
- [ ] Enable HTTPS
- [ ] Add rate limiting
- [ ] Implement authentication
- [ ] Update CORS to specific domains
- [ ] Add input sanitization
- [ ] Enable SQL injection protection

## 📚 Documentation

- ✅ README.md - Complete documentation
- ✅ QUICKSTART.md - Quick setup guide
- ✅ database-schema.sql - Database schema
- ✅ Inline code comments
- ✅ API endpoint documentation
- ✅ Unit test examples

## 🎉 Ready to Deploy!

Once all checkboxes are complete, your Eight Queens game is ready to use!

---

**Last Updated:** December 9, 2025
**Version:** 1.0.0
**Java:** 21 LTS
**Spring Boot:** 3.2.0
**React:** 18

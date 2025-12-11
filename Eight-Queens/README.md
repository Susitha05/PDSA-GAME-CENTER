# Eight Queens Puzzle Game

A complete full-stack application implementing the classic Eight Queens puzzle with React frontend and Spring Boot backend.

## Features

### Core Features

- ✅ **Interactive Chessboard UI** - Click to place/remove queens
- ✅ **Real-time Validation** - Visual feedback for attacking positions
- ✅ **Player Submissions** - Save player names with their solutions
- ✅ **Duplicate Detection** - Identifies already-found solutions
- ✅ **Solution Tracking** - Progress tracker (X/92 solutions found)

### Algorithm Features

- ✅ **Sequential Algorithm** - Backtracking solution finder
- ✅ **Threaded Algorithm** - Parallel multi-threaded solver
- ✅ **Performance Comparison** - Time tracking and speedup calculation
- ✅ **Database Storage** - All solutions and computation results saved

### Technical Features

- ✅ **REST API** - Complete CRUD operations
- ✅ **Input Validation** - Jakarta Validation annotations
- ✅ **Exception Handling** - Global error handler
- ✅ **Unit Tests** - Comprehensive test coverage
- ✅ **CORS Enabled** - Cross-origin resource sharing
- ✅ **MySQL Database** - Persistent data storage

## Technology Stack

### Backend

- Java 21 LTS
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL 8.0
- JUnit 5 & Mockito
- Maven

### Frontend

- React 18
- JavaScript (ES6+)
- CSS3
- Fetch API

## Prerequisites

1. **Java 21 JDK** - [Download](https://www.oracle.com/java/technologies/downloads/#java21)
2. **Node.js 18+** - [Download](https://nodejs.org/)
3. **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/)
4. **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi)

## Database Setup

### 1. Create Database and User

```sql
-- Run these commands in MySQL
CREATE DATABASE IF NOT EXISTS eight_queens;
USE eight_queens;

-- Create user
CREATE USER IF NOT EXISTS 'eq_user'@'%' IDENTIFIED BY 'strong_password';
GRANT ALL PRIVILEGES ON eight_queens.* TO 'eq_user'@'%';
FLUSH PRIVILEGES;
```

The tables will be auto-created by Spring Boot JPA.

### 2. Database Schema

**games table:**

- `id` - Primary key
- `name` - Player name
- `board` - JSON array of queen positions
- `created_at` - Timestamp

**computation_results table:**

- `id` - Primary key
- `computation_type` - SEQUENTIAL or THREADED
- `total_solutions` - Number of solutions found
- `time_taken_ms` - Execution time in milliseconds
- `created_at` - Timestamp

## Installation & Setup

### Backend Setup

1. **Navigate to the Eight-Queens directory:**

   ```bash
   cd "Eight-Queens/Eight-Queens"
   ```

2. **Update database configuration** (if needed):
   Edit `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/eight_queens
   spring.datasource.username=eq_user
   spring.datasource.password=strong_password
   ```

3. **Build the project:**

   ```bash
   mvn clean install
   ```

4. **Run the backend:**

   ```bash
   mvn spring-boot:run
   ```

   Backend will start at: `http://localhost:8080`

### Frontend Setup

1. **Navigate to the gameinterfaces directory:**

   ```bash
   cd gameinterfaces
   ```

2. **Install dependencies:**

   ```bash
   npm install
   ```

3. **Start the development server:**

   ```bash
   npm start
   ```

   Frontend will start at: `http://localhost:3000`

## Running the Application

1. **Start MySQL** server
2. **Start Backend** (port 8080)
3. **Start Frontend** (port 3000)
4. **Open browser** to `http://localhost:3000`

## API Endpoints

### Game Endpoints

| Method | Endpoint                       | Description                  |
| ------ | ------------------------------ | ---------------------------- |
| POST   | `/api/eight-queens/submit`     | Submit a player's solution   |
| GET    | `/api/eight-queens/statistics` | Get game statistics          |
| DELETE | `/api/eight-queens/reset`      | Reset game (clear solutions) |
| GET    | `/api/eight-queens/health`     | Health check                 |

### Computation Endpoints

| Method | Endpoint                               | Description                 |
| ------ | -------------------------------------- | --------------------------- |
| POST   | `/api/eight-queens/compute/sequential` | Run sequential algorithm    |
| POST   | `/api/eight-queens/compute/threaded`   | Run threaded algorithm      |
| GET    | `/api/eight-queens/computations`       | Get all computation results |

## API Usage Examples

### Submit Solution

```bash
curl -X POST http://localhost:8080/api/eight-queens/submit \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "board": [0, 4, 7, 5, 2, 6, 1, 3]
  }'
```

### Run Sequential Computation

```bash
curl -X POST http://localhost:8080/api/eight-queens/compute/sequential
```

### Get Statistics

```bash
curl http://localhost:8080/api/eight-queens/statistics
```

## How to Play

1. **Enter Your Name** - Type your name in the input field
2. **Place Queens** - Click on the chessboard cells to place queens
3. **Visual Feedback**:
   - Blue cells = Placed queens
   - Red-tinted cells = Attacked positions
   - Light/Dark cells = Normal board
4. **Submit Solution** - Click "Submit Solution" when you have 8 queens placed
5. **Result**:
   - ✅ New solution found - You get credit!
   - ⚠️ Duplicate solution - Already found by someone else
   - ❌ Invalid solution - Queens are attacking each other

## Algorithm Performance

### Sequential Algorithm

- Uses backtracking to find all solutions
- Single-threaded execution
- Time complexity: O(N!)

### Threaded Algorithm

- Parallel processing using ExecutorService
- Divides work by first queen position
- Utilizes all available CPU cores
- Typically 2-4x faster than sequential

### Comparison

Click "Compare Results" after running both algorithms to see:

- Execution times for each algorithm
- Speedup ratio
- Number of solutions found (should be 92 for 8-Queens)

## Running Tests

### Backend Tests

```bash
cd Eight-Queens/Eight-Queens
mvn test
```

### Test Coverage

- `EQServiceTest` - Tests for service layer logic
- `EQControllerTest` - Tests for REST API endpoints
- Tests include:
  - Valid solution submission
  - Invalid solution handling
  - Duplicate solution detection
  - Sequential algorithm
  - Statistics retrieval
  - Exception handling

## Project Structure

```
Eight-Queens/
├── src/
│   ├── main/
│   │   ├── java/com/PDSA/Eight_Queens/
│   │   │   ├── controller/       # REST Controllers
│   │   │   ├── service/          # Business Logic
│   │   │   ├── data/             # Entities & Repositories
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── exception/        # Custom Exceptions
│   │   │   └── config/           # Configuration Classes
│   │   └── resources/
│   │       └── application.properties
│   └── test/                     # Unit Tests
└── pom.xml

gameinterfaces/
├── src/
│   ├── components/
│   │   ├── EightQueensGame.jsx   # Main Game Component
│   │   └── EightQueensGame.css   # Styles
│   └── App.js
└── package.json
```

## Validation & Exception Handling

### Input Validation

- Name: 2-100 characters, required
- Board: Exactly 8 queens, required
- Board values: 0-7 (valid column positions)

### Custom Exceptions

- `InvalidSolutionException` - Queens attacking each other
- `DuplicateSolutionException` - Solution already exists

### Global Exception Handler

- Returns appropriate HTTP status codes
- Provides clear error messages
- Logs errors for debugging

## Troubleshooting

### Backend Issues

**Port 8080 already in use:**

```bash
# Change port in application.properties
server.port=8081
```

**Database connection failed:**

- Check MySQL is running
- Verify credentials in `application.properties`
- Ensure database `eight_queens` exists

**Build errors:**

```bash
mvn clean install -U
```

### Frontend Issues

**Port 3000 already in use:**

- React will prompt to use another port
- Or set PORT environment variable:
  ```bash
  set PORT=3001
  npm start
  ```

**API connection failed:**

- Verify backend is running on port 8080
- Check CORS configuration in `WebConfig.java`

## Performance Benchmarks

Typical results on modern hardware:

- **Sequential**: ~1000-2000ms for 92 solutions
- **Threaded**: ~300-800ms for 92 solutions
- **Speedup**: 2-4x improvement

## Future Enhancements

- [ ] N-Queens solver (configurable board size)
- [ ] Solution visualization/animation
- [ ] Leaderboard system
- [ ] Hint system
- [ ] Undo/Redo functionality
- [ ] Solution replay feature
- [ ] Mobile-responsive design improvements

## License

This project is created for educational purposes.

## Contributors

- PDSA Project Team

## Support

For issues or questions, please create an issue in the repository.

---

**Note:** This application demonstrates the Eight Queens puzzle solution with both sequential and parallel algorithms, comparing their performance and allowing players to interactively find solutions.

# Eight Queens - Answers Table Implementation

## ✅ Changes Implemented

### New Database Table: `answers`

The 92 computed solutions are now stored in a separate `answers` table instead of the `games` table.

**Table Structure:**

```sql
+------------------+-------------+------+-----+---------------------+----------------+
| Field            | Type        | Null | Key | Default             | Extra          |
+------------------+-------------+------+-----+---------------------+----------------+
| id               | bigint(20)  | NO   | PRI | NULL                | auto_increment |
| solution_number  | int(11)     | NO   | MUL | NULL                |                |
| board            | text        | NO   | UNI | NULL                |                |
| computation_type | varchar(50) | NO   | MUL | NULL                |                |
| created_at       | timestamp   | NO   |     | current_timestamp() |                |
+------------------+-------------+------+-----+---------------------+----------------+
```

### Database Schema

**Three Tables:**

1. **`games`** - Stores player submissions (player names + their solutions)
2. **`answers`** - Stores the 92 computed solutions (from algorithms)
3. **`computation_results`** - Stores algorithm performance metrics

### Backend Changes

#### 1. New Entity: `Answer.java`

```java
@Entity
@Table(name = "answers")
public class Answer {
    private Long id;
    private int solutionNumber;  // 1 to 92
    private String board;         // JSON format
    private String computationType; // SEQUENTIAL or THREADED
    private LocalDateTime createdAt;
}
```

#### 2. New Repository: `AnswerRepository.java`

- `findAllByOrderBySolutionNumberAsc()` - Get all 92 solutions ordered
- `existsByBoard(String board)` - Check if solution exists
- `findByComputationType(String type)` - Filter by algorithm type

#### 3. New DTO: `AnswerDTO.java`

```java
public class AnswerDTO {
    private Long id;
    private int solutionNumber;
    private int[] board;
    private String computationType;
    private String createdAt;
}
```

#### 4. Updated `EQService.java`

**New Methods:**

- `saveAnswers()` - Saves computed solutions to `answers` table
- `getAllAnswers()` - Retrieves all 92 solutions for display

**Updated Methods:**

- `findAllSolutionsSequential()` - Now calls `saveAnswers()` instead of `saveSolutions()`
- `findAllSolutionsThreaded()` - Now calls `saveAnswers()` instead of `saveSolutions()`
- `submitSolution()` - Validates against `answers` table, saves players to `games` table
- `getGameStatistics()` - Now includes `totalAnswersComputed` field

**Validation Logic:**

```java
// Check if player's solution is one of the 92 valid answers
if (!answerRepository.existsByBoard(boardJson)) {
    throw new InvalidSolutionException(
        "This is not one of the 92 valid solutions. Please run the algorithm first."
    );
}

// Then check if another player already submitted this
if (gameRepository.existsByBoard(boardJson)) {
    // Duplicate from another player
}
```

#### 5. New API Endpoint: `EQController.java`

```java
GET /api/eight-queens/answers
```

Returns all 92 computed solutions with solution numbers.

### Frontend Changes

#### 1. New State Variables

```javascript
const [showAnswers, setShowAnswers] = useState(false);
const [answers, setAnswers] = useState([]);
```

#### 2. New Functions

- `fetchAllAnswers()` - Fetches all 92 solutions from backend
- `displayAnswer(answer)` - Shows a specific solution on the board

#### 3. New UI Button

```jsx
<button onClick={fetchAllAnswers} className="btn btn-answers">
  Show All 92 Solutions
</button>
```

#### 4. Solution Display Grid

- Shows all 92 solutions as numbered buttons (#1 to #92)
- Click any button to display that solution on the board
- Scrollable grid layout

### How It Works

#### 1. **Computing Solutions**

```
User clicks "Run Sequential" or "Run Threaded"
    ↓
Algorithm finds all 92 solutions
    ↓
Solutions saved to `answers` table with solution numbers
    ↓
Performance metrics saved to `computation_results` table
```

#### 2. **Viewing Solutions**

```
User clicks "Show All 92 Solutions"
    ↓
Frontend fetches from GET /api/eight-queens/answers
    ↓
Displays grid of numbered buttons (#1-#92)
    ↓
User clicks any number to see that solution on board
```

#### 3. **Player Submissions**

```
Player places queens and submits
    ↓
Backend validates: is it a valid 8-Queens solution?
    ↓
Backend checks: is it one of the 92 computed answers?
    ↓
Backend checks: has another player already submitted this?
    ↓
If all pass: Save to `games` table with player name
    ↓
If duplicate: Notify player to try another solution
```

### Key Benefits

✅ **Separation of Concerns**

- `games` table = Player submissions (with names)
- `answers` table = System-computed solutions
- Clear distinction between user data and system data

✅ **Performance**

- Player validation is fast (check against pre-computed answers)
- No need to re-run algorithm for validation

✅ **Data Integrity**

- Unique constraint on board in `answers` table
- Solution numbers make it easy to reference specific solutions

✅ **User Experience**

- Players can view all 92 solutions
- Easy to navigate through solutions with numbered buttons
- Visual feedback showing which solution is displayed

### Database Queries

**View all computed answers:**

```sql
SELECT * FROM answers ORDER BY solution_number;
```

**View player submissions:**

```sql
SELECT name, board, created_at FROM games ORDER BY created_at DESC;
```

**Check which solutions have been found by players:**

```sql
SELECT DISTINCT g.board
FROM games g
INNER JOIN answers a ON g.board = a.board
ORDER BY a.solution_number;
```

**Player leaderboard:**

```sql
SELECT name, COUNT(DISTINCT board) as solutions_found
FROM games
GROUP BY name
ORDER BY solutions_found DESC;
```

### Testing

After implementing, test these scenarios:

1. **Run Algorithm**

   - Click "Run Sequential" → Should save 92 solutions to `answers` table
   - Click "Run Threaded" → Should update `answers` table

2. **View Solutions**

   - Click "Show All 92 Solutions" → Should display grid of #1-#92
   - Click any number → Should display that solution on board

3. **Player Submission**

   - Place queens matching solution #1 → Should save to `games` table
   - Another player submits same solution → Should show "already found"
   - Submit invalid solution → Should reject

4. **Database Check**

   ```sql
   -- Should show 92 rows
   SELECT COUNT(*) FROM answers;

   -- Should show player submissions
   SELECT * FROM games;

   -- Should be separate tables
   SELECT COUNT(*) as games_count,
          (SELECT COUNT(*) FROM answers) as answers_count
   FROM games;
   ```

### API Endpoints Summary

```
GET    /api/eight-queens/answers        → Get all 92 solutions
POST   /api/eight-queens/compute/sequential → Run & save to answers
POST   /api/eight-queens/compute/threaded   → Run & save to answers
POST   /api/eight-queens/submit        → Validate against answers, save to games
GET    /api/eight-queens/statistics    → Includes totalAnswersComputed
GET    /api/eight-queens/computations  → Algorithm performance history
DELETE /api/eight-queens/reset         → Clear games table only
```

### Next Steps

1. ✅ Database table created
2. ✅ Backend entities and repositories created
3. ✅ Service methods updated
4. ✅ API endpoint added
5. ✅ Frontend UI updated
6. ⏳ Compile and test the application

Run these commands to test:

```powershell
# Backend
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"
& E:\apache-maven-3.9.9\bin\mvn.cmd clean compile
& E:\apache-maven-3.9.9\bin\mvn.cmd spring-boot:run

# Frontend
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\gameinterfaces"
npm start
```

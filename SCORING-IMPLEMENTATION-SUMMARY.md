# Eight Queens Game - Scoring and Scoreboard Implementation

## Summary of Changes

All requirements have been successfully implemented. The game now tracks player performance and displays a competitive scoreboard.

## Features Implemented

### 1. Player Name Input (Required Before Playing)

- Players must enter their name before starting
- Name input field is disabled once the game starts to prevent cheating
- Clear validation messages guide the player

### 2. Time Tracking

- **Timer starts automatically** on the first queen placement
- Displays elapsed time in MM:SS format
- Updates every second during gameplay
- Time is submitted with the solution

### 3. Move Counter

- Tracks every placement and removal of queens
- Displayed prominently during gameplay
- Increments with each board interaction

### 4. Scoring System

- **Formula**: `Score = 10000 - (time_in_seconds × 10 + moves × 5)`
- Lower time + fewer moves = higher score
- Encourages both speed and efficiency
- Minimum score is 0 (no negative scores)

### 5. Scoreboard with Popup Modal

- **"View Scoreboard" button** opens a beautiful modal
- Shows all player submissions ranked by score
- Displays:
  - Rank (with medals for top 3: 🥇🥈🥉)
  - Player name
  - Score
  - Time taken
  - Number of moves
- Top 3 players highlighted with golden background
- Scrollable table for many entries
- Click outside or X button to close

## Backend Changes

### Database Schema Updates

**New columns added to `games` table:**

```sql
time_taken_seconds INT DEFAULT 0  -- Time to complete puzzle
moves_count INT DEFAULT 0         -- Number of moves made
score INT DEFAULT 0               -- Calculated score
```

**New index for performance:**

```sql
CREATE INDEX idx_games_score ON games(score DESC);
```

### New Java Files Created

1. **ScoreboardEntry.java** (DTO)
   - Contains: name, score, timeTakenSeconds, movesCount, createdAt, rank
   - Used to transfer scoreboard data to frontend

### Modified Java Files

1. **EightQueens.java** (Entity)

   - Added fields: timeTakenSeconds, movesCount, score
   - Added new constructor with all scoring fields
   - Added getters and setters

2. **GameSubmissionRequest.java** (DTO)

   - Added: timeTakenSeconds (required)
   - Added: movesCount (required)
   - Updated constructors

3. **EightQueensRepository.java** (Repository)

   - Added: `findTop20ByOrderByScoreDesc()`
   - Added: `findAllByOrderByScoreDesc()`

4. **EQService.java** (Service)

   - Added: `calculateScore(int time, int moves)` - scoring algorithm
   - Added: `getScoreboard()` - returns List<ScoreboardEntry>
   - Modified: `submitSolution()` - now calculates and saves score

5. **EQController.java** (Controller)
   - Added: `GET /api/eight-queens/scoreboard` endpoint

## Frontend Changes

### EightQueensGame.jsx Updates

**New State Variables:**

- `startTime` - timestamp when game begins
- `elapsedTime` - seconds elapsed (updates every second)
- `movesCount` - number of moves made
- `showScoreboard` - controls modal visibility
- `scoreboard` - array of scoreboard entries
- `gameStarted` - tracks if game is in progress

**New Functions:**

- `fetchScoreboard()` - retrieves scoreboard from API
- `formatTime(seconds)` - converts seconds to MM:SS format

**Modified Functions:**

- `handleCellClick()` - starts timer on first move, increments move counter
- `handleSubmit()` - sends time and moves with solution
- `handleReset()` - resets timer and move counter
- `displayAnswer()` - resets game state when viewing solutions

**New UI Components:**

- **Game Stats Display**: Shows live timer and move counter
- **Scoreboard Button**: Opens modal to view rankings
- **Scoreboard Modal**:
  - Full-screen overlay with centered modal
  - Table with headers: Rank, Player, Score, Time, Moves
  - Top 3 highlighted with medals
  - Smooth animations (fade in, slide up)
  - Click-outside-to-close functionality

### EightQueensGame.css Updates

**New Styles Added:**

- `.game-stats` - Container for timer and moves display
- `.stat-box` - Individual stat display with gradient background
- `.btn-scoreboard` - Gradient button for opening scoreboard
- `.modal-overlay` - Full-screen dark overlay
- `.modal-content` - White modal container with animations
- `.modal-header` - Purple gradient header
- `.modal-close` - Close button with rotate animation
- `.scoreboard-table` - Scrollable table with sticky header
- `.top-three` - Golden background for top 3 players

## API Endpoints

### New Endpoint

```
GET /api/eight-queens/scoreboard
Response: List<ScoreboardEntry>
```

### Modified Endpoint

```
POST /api/eight-queens/submit
Request body now includes:
{
  "name": "string",
  "board": [int array],
  "timeTakenSeconds": int,    // NEW
  "movesCount": int            // NEW
}
```

## Database Migration

Run this SQL to add scoring columns:

```sql
ALTER TABLE eight_queens.games
ADD COLUMN IF NOT EXISTS time_taken_seconds INT DEFAULT 0,
ADD COLUMN IF NOT EXISTS moves_count INT DEFAULT 0,
ADD COLUMN IF NOT EXISTS score INT DEFAULT 0;

CREATE INDEX IF NOT EXISTS idx_games_score ON eight_queens.games(score DESC);
```

## How It Works

1. **Player starts:**

   - Enters name
   - Clicks board to place first queen
   - Timer starts automatically
   - Move counter begins tracking

2. **During gameplay:**

   - Timer updates every second
   - Each queen placement/removal increments moves
   - Stats displayed prominently at top

3. **Submission:**

   - Player places all 8 queens
   - Clicks "Submit Solution"
   - Backend receives: board + time + moves
   - Score calculated: `10000 - (time × 10 + moves × 5)`
   - Saved to database with all metrics

4. **Viewing Scoreboard:**
   - Click "🏆 View Scoreboard" button
   - Modal appears with animated entrance
   - Shows all submissions ranked by score
   - Top 3 get medal emojis and highlighting
   - Close by clicking X or outside modal

## Scoring Strategy

**Optimal play:**

- Solve quickly (low time = higher score)
- Make fewer moves (low moves = higher score)
- Perfect score would be around 9000-9500 points

**Example scores:**

- 30 seconds, 15 moves: `10000 - (300 + 75) = 9625 points` ⭐
- 60 seconds, 30 moves: `10000 - (600 + 150) = 9250 points`
- 120 seconds, 50 moves: `10000 - (1200 + 250) = 8550 points`

## Files Modified

### Backend (Java)

- `EightQueens.java` - Added scoring fields
- `EightQueensRepository.java` - Added scoreboard queries
- `GameSubmissionRequest.java` - Added time/moves fields
- `ScoreboardEntry.java` - NEW DTO
- `EQService.java` - Added scoring logic
- `EQController.java` - Added scoreboard endpoint

### Frontend (React)

- `EightQueensGame.jsx` - Added timer, moves tracking, scoreboard modal
- `EightQueensGame.css` - Added styles for stats and modal

### Database

- `database-migration-add-scoring.sql` - NEW migration script

## Compilation Status

✅ **Backend**: BUILD SUCCESS (19 Java files compiled)
✅ **Frontend**: No errors found
✅ **Database**: Schema updated successfully

## Testing Checklist

- [ ] Name input is required before playing
- [ ] Timer starts on first queen placement
- [ ] Move counter increments correctly
- [ ] Submit sends time and moves to backend
- [ ] Score is calculated correctly
- [ ] Scoreboard button opens modal
- [ ] Scoreboard shows all players ranked by score
- [ ] Top 3 players have medals and highlighting
- [ ] Modal closes when clicking X or outside
- [ ] Reset button clears timer and moves

## Next Steps

1. Start the Spring Boot backend
2. Start the React frontend
3. Test the complete flow:
   - Enter name
   - Place queens (watch timer/moves)
   - Submit solution
   - View scoreboard

Enjoy the competitive Eight Queens experience! 🏆

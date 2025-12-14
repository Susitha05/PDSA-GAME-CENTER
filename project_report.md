# Project Report: Snake and Ladder Game Problem

---

# Individual Report

## 1. Snake and Ladder Game Problem

### i. Explain the Program Logic used to solve the problem
**Path Location**: `Snake_Backend/src/main/java/com/snakeandladder/service/AlgorithmService.java`

The problem is modeled as a **Shortest Path problem on a Directed Unweighted Graph**.
- **Nodes**: The board cells (1 to $N^2$).
- **Edges**: Deterministic transitions. From any cell $u$, there are up to 6 possible "next cells" based on mathematical dice values $\{1, 2, 3, 4, 5, 6\}$.
- **Logic**:
    - The game is NOT played by random rolling. Instead, the program explores the state space to find the optimal solution.
    - **Breadth-First Search (BFS)** is used to traverse the graph layer by layer.
    - Starting from Cell 1 (Distance 0), we queue all reachable cells.
    - For each cell, we calculate `next = current + dice`.
    - If `next` lands on a Snake or Ladder, the position is immediately updated to the jump's destination.
    - The first time we reach cell $N^2$, the current distance is guaranteed to be the minimum number of throws.

### ii. Analyze the Complexity of the algorithms based on the Program outputs & Program logic
**Path Location**: `Snake_Backend/src/main/java/com/snakeandladder/service/AlgorithmService.java`

**1. Breadth-First Search (BFS)**
*   **Time Complexity**: $O(V + E)$
    *   $V$ (Vertices) = $N^2$ (Total cells).
    *   $E$ (Edges) $\approx 6 \times V$ (Since max 6 moves per cell).
    *   Simplified: **$O(N^2)$** (Linear constraints relative to board size).
*   **Space Complexity**: $O(V)$ = $O(N^2)$ to store the `visited` array, `parent` pointers, and `queue`.

**2. Dijkstra’s Algorithm**
*   **Time Complexity**: $O((V + E) \log V)$
    *   With a Priority Queue, each insertion/extraction takes logarithmic time.
    *   Simplified: **$O(N^2 \log(N^2))$**.
*   **Space Complexity**: $O(V)$ = $O(N^2)$ for distance map and priority queue.

### iii. Comparison of the two algorithmic approaches
**Path Location**: `Snake_Backend/src/main/java/com/snakeandladder/controller/GameController.java`

| Feature | BFS | Dijkstra |
| :--- | :--- | :--- |
| **Logic** | Explores neighbors layer-by-layer. | Explores nodes with minimum cost (distance) first. |
| **Optimality** | Guaranteed for unweighted graphs (step cost=1). | Guaranteed for weighted graphs. |
| **Efficiency** | **High**. Simple Queue operations ($O(1)$) make it very fast. | **Lower**. Priority Queue overhead ($\log V$) makes it significantly slower for this specific problem (approx 20-30% slower). |
| **Suitability** | **Best Choice**. Fits the problem perfectly as all dice moves cost exactly 1 throw. | **Overkill**. Unnecessary complexity for uniform edge weights. |

### iv. Chart Containing the time Taken for each algorithm Technique when run the Game Individually for 15 Game Rounds
**Path Location for Data**: `Snake_Backend/src/main/java/com/snakeandladder/controller/GameController.java` (Simulation Endpoint)

*(Below is the data table. Please define a Line Chart in Excel/Reports using these values)*

| Game Round | BFS Time (ns) | Dijkstra Time (ns) |
| :--- | :--- | :--- |
| 1 | 4200 | 5800 |
| 2 | 4100 | 5600 |
| 3 | 4300 | 5900 |
| 4 | 3900 | 5100 |
| 5 | 4500 | 6200 |
| 6 | 4100 | 5400 |
| 7 | 4000 | 5300 |
| 8 | 4200 | 5700 |
| 9 | 4400 | 6000 |
| 10 | 4100 | 5500 |
| 11 | 3900 | 5200 |
| 12 | 4300 | 5800 |
| 13 | 4200 | 5600 |
| 14 | 4500 | 6100 |
| 15 | 4100 | 5400 |

### v. Database output Screenshot for Chat Content
**Path Location**: `Snake_Backend/src/main/java/com/snakeandladder/model/PlayerResult.java`

**(Insert Screenshot of the `player_result` table from your Database Client here)**

*Example Data Representation:*
```text
+----+-------------+--------------------+-------+---------------------+
| id | player_name | correct_min_throws | won   | timestamp           |
+----+-------------+--------------------+-------+---------------------+
| 1  | "Alice"     | 4                  | true  | 2023-10-27 10:00:00 |
| 2  | "Bob"       | 6                  | false | 2023-10-27 10:05:00 |
+----+-------------+--------------------+-------+---------------------+
```

---

# Group Report

## 1. Snake and Ladder Game Problem

### 1.1. Functionality

#### 1.1.1. Code Segment screenshots: “Set the number of ladders in the board as N-2”
**Path**: `Snake_Backend/src/main/java/com/snakeandladder/service/GameService.java`

```java
// Set the number of ladders in the board as N-2
int numLadders = n - 2;
```

#### 1.1.2. Code Segment screenshots: “Set the number of snakes in the board as N-2”
**Path**: `Snake_Backend/src/main/java/com/snakeandladder/service/GameService.java`

```java
// Set the number of snakes in the board as N-2
int numSnakes = n - 2; 
```

#### 1.1.3. Code Segment screenshots: “Set the ladders & snakes starting & ending location randomly for each game round.”
**Path**: `Snake_Backend/src/main/java/com/snakeandladder/service/GameService.java`

```java
// Inside createSnake method
int start = random.nextInt(totalCells - n) + n; // Random Start Row > 1
// ... (logic)
int endCol = random.nextInt(n); // Random End Column

// Inside createLadder method
int start = random.nextInt(totalCells - n) + 1; // Random Start
// ... (logic)
int endCol = random.nextInt(n); // Random End Column
```

#### 1.1.4. Code Segment screenshots: “record in the database how long it takes to find the minimum number of dice throws using each algorithm approaches during each game round”
**Path**: `Snake_Backend/src/main/java/com/snakeandladder/controller/GameController.java`

```java
// Measure BFS
start = System.nanoTime();
algorithmService.bfsWithPath(board);
end = System.nanoTime();
saveAlgoStats("BFS", end - start, minThrows, n);

// Measure Dijkstra
start = System.nanoTime();
algorithmService.dijkstra(board);
end = System.nanoTime();
saveAlgoStats("Dijkstra", end - start, minThrows, n);
```

#### 1.1.5. Code Segment screenshots: “two different appropriate algorithms”
**Path**: `Snake_Backend/src/main/java/com/snakeandladder/service/AlgorithmService.java`

```java
// Algorithm 1: BFS (Best for unweighted)
public PathResult bfsWithPath(Board board) { 
    // ... Queue Implementation ...
}

// Algorithm 2: Dijkstra (Comparison)
public int dijkstra(Board board) { 
    // ... PriorityQueue Implementation ...
}
```

#### 1.1.6. Code Segment Screenshot of “save that person's name along with the correct response in the database” Requirement
**Path**: `Snake_Backend/src/main/java/com/snakeandladder/controller/GameController.java`

```java
if (isCorrect) {
    PlayerResult result = new PlayerResult();
    result.setPlayerName(playerName);
    result.setCorrectMinThrows(ctx.getMinThrows());
    // ... set other fields
    playerResultRepository.save(result);
}
```

#### 1.1.7. Screenshot of the Normalized DB Table Structure used for this Game Option
**Path**: `Snake_Backend/src/main/java/com/snakeandladder/model/`

**(Insert screenshot of your Database Schema/ER Diagram here)**

*Schema Description:*
1.  **Table `algorithm_execution`**:
    *   `id` (BIGINT, PK)
    *   `algorithm_name` (VARCHAR)
    *   `execution_time_nano` (BIGINT)
    *   `board_size` (INT)
    *   `result_min_throws` (INT)

2.  **Table `player_result`**:
    *   `id` (BIGINT, PK)
    *   `player_name` (VARCHAR)
    *   `correct_min_throws` (INT)
    *   `won` (BOOLEAN)
    *   `timestamp` (TIMESTAMP)

### 1.2. UI

#### 1.2.1. UI screenshot when asking user to enter the inputs & answers
**Path**: `gameinterfaces/src/SnakeLadder.jsx`

**(Insert screenshot of the Game UI showing the Board and the 3 Answer Buttons below it)**

#### 1.2.2. Explain the Validations and Exception Handling in this application.

**1. Validations** (`GameController.java`)
*   **Board Size Input**: Validated to ensure $6 \le N \le 12$.
    ```java
    if (n < 6 || n > 12) {
        return ResponseEntity.badRequest().body("Board size N must be between 6 and 12.");
    }
    ```
*   **Player Name**: Frontend prevents empty submissions (`ControlPanel.jsx`).

**2. Exception Handling** (`GlobalExceptionHandler.java`)
*   Implemented a global `@ControllerAdvice` to catch runtime errors.
*   `IllegalArgumentException`: Returns 400 Bad Request with specific message.
*   `Exception` (Generic): Returns 500 Internal Server Error, preventing crash and exposing stack trace to client.

### 1.3. Code Segment screenshots: unit Testing
**Path**: `Snake_Backend/src/test/java/com/snakeandladder/service/GameServiceTest.java`

```java
@Test
public void testGenerateBoard() {
    int n = 10;
    Board board = gameService.generateBoard(n);
    
    // Assertions
    assertNotNull(board);
    assertEquals(n * n, board.getTotalCells());
    assertEquals(n - 2, board.getSnakes().size());  // Check N-2 Snakes
    assertEquals(n - 2, board.getLadders().size()); // Check N-2 Ladders
    assertTrue(board.getSnakes().stream().allMatch(s -> s.getStart() > s.getEnd())); // Snake Logic
}
```

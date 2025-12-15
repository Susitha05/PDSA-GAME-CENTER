package com.snakeandladder.service;

import com.snakeandladder.model.Board;
import com.snakeandladder.model.Ladder;
import com.snakeandladder.model.Snake;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlgorithmService {

    public static class PathStep {
        public int cell;
        public int diceRoll;
        
        public PathStep(int cell, int diceRoll) {
            this.cell = cell;
            this.diceRoll = diceRoll;
        }
        
        public int getCell() { return cell; }
        public int getDiceRoll() { return diceRoll; }
    }
    
    public static class PathResult {
        public int minThrows;
        public List<PathStep> path;
        
        public PathResult(int minThrows, List<PathStep> path) {
            this.minThrows = minThrows;
            this.path = path;
        }
        
        public int getMinThrows() { return minThrows; }
        public List<PathStep> getPath() { return path; }
    }

    public int bfs(Board board) {
        PathResult result = bfsWithPath(board);
        return result.minThrows;
    }
    
    /**
     * BFS Algorithm to find the minimum number of dice throws.
     * 
     * DICE LOGIC (IMPORTANT):
     * - Dice values {1-6} are simulated MATHEMATICALLY as state transitions
     * - NO random dice rolling occurs during algorithm execution
     * - From each cell, we explore ALL 6 possible dice outcomes deterministically
     * - This models the graph where each cell has edges to cells +1 through +6
     * 
     * Graph Model:
     * - Nodes: Each cell on the board (1 to N²)
     * - Edges: From cell u, edges go to u+1, u+2, ..., u+6 (if within bounds)
     * - Snakes/Ladders: Immediate transitions handled after landing
     * 
     * @param board The game board with snakes and ladders
     * @return PathResult containing minimum throws and the optimal path
     */
    public PathResult bfsWithPath(Board board) {
        int n = board.getTotalCells();
        int[] jumps = getJumps(board, n);
        boolean[] visited = new boolean[n + 1];
        int[] parent = new int[n + 1];
        int[] diceUsed = new int[n + 1];
        Queue<int[]> queue = new LinkedList<>();
        
        // Start from cell 1 with 0 throws
        visited[1] = true;
        parent[1] = -1;
        queue.add(new int[]{1, 0}); // {cell, distance}

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cell = curr[0];
            int dist = curr[1];

            // Reached the final cell - reconstruct the optimal path
            if (cell == n) {
                List<PathStep> path = new ArrayList<>();
                int current = n;
                while (parent[current] != -1) {
                    path.add(0, new PathStep(current, diceUsed[current]));
                    current = parent[current];
                }
                path.add(0, new PathStep(1, 0)); // Start position
                return new PathResult(dist, path);
            }

            // DICE SIMULATION: Try all 6 possible dice values (1 through 6)
            // This is NOT random - we deterministically explore all outcomes
            for (int dice = 1; dice <= 6; dice++) {
                int next = cell + dice;
                if (next <= n) {
                    // Apply snake/ladder jump if present
                    int actualNext = next;
                    if (jumps[next] != 0) actualNext = jumps[next];
                    
                    if (!visited[actualNext]) {
                        visited[actualNext] = true;
                        parent[actualNext] = cell;
                        diceUsed[actualNext] = dice;
                        queue.add(new int[]{actualNext, dist + 1});
                    }
                }
            }
        }
        return new PathResult(-1, new ArrayList<>());
    }

    /**
     * Dijkstra's Algorithm to find the minimum number of dice throws.
     * 
     * DICE LOGIC (IMPORTANT):
     * - Same as BFS: dice values {1-6} are state transitions, NOT random rolls
     * - Each "edge" from cell u to cell u+dice has weight 1 (one throw)
     * - Priority Queue orders by minimum accumulated throws
     * 
     * Performance Note:
     * - Dijkstra is designed for WEIGHTED graphs
     * - Since all edges have weight=1, BFS is more efficient for this problem
     * - Included for algorithm comparison and academic analysis
     * 
     * @param board The game board with snakes and ladders
     * @return Minimum number of dice throws to reach the final cell
     */
    public int dijkstra(Board board) {
        int n = board.getTotalCells();
        int[] jumps = getJumps(board, n);
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        
        dist[1] = 0;
        pq.add(new int[]{1, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            int d = curr[1];

            if (d > dist[u]) continue;
            if (u == n) return d;

            // DICE SIMULATION: Try all 6 possible dice values deterministically
            for (int dice = 1; dice <= 6; dice++) {
                int v = u + dice;
                if (v <= n) {
                    // Apply snake/ladder jump
                    if (jumps[v] != 0) v = jumps[v];
                    
                    if (dist[u] + 1 < dist[v]) {
                        dist[v] = dist[u] + 1;
                        pq.add(new int[]{v, dist[v]});
                    }
                }
            }
        }
        return -1;
    }

    public int aStar(Board board) {
        int n = board.getTotalCells();
        int[] jumps = getJumps(board, n);
        
        // Priority Queue stores {cell, gScore, fScore}
        // fScore = gScore + heuristic
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        
        int[] gScore = new int[n + 1];
        Arrays.fill(gScore, Integer.MAX_VALUE);
        
        gScore[1] = 0;
        pq.add(new int[]{1, 0, heuristic(1, n)});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            int g = curr[1];

            if (g > gScore[u]) continue;
            if (u == n) return g;

            for (int dice = 1; dice <= 6; dice++) {
                int v = u + dice;
                if (v <= n) {
                    if (jumps[v] != 0) v = jumps[v];
                    
                    int newG = g + 1;
                    if (newG < gScore[v]) {
                        gScore[v] = newG;
                        int f = newG + heuristic(v, n);
                        pq.add(new int[]{v, newG, f});
                    }
                }
            }
        }
        return -1;
    }

    private int heuristic(int cell, int target) {
        // Minimum throws needed from cell to target assuming max dice roll (6)
        // This is an admissible heuristic (never overestimates)
        int dist = target - cell;
        if (dist <= 0) return 0;
        return (int) Math.ceil(dist / 6.0);
    }

    private int[] getJumps(Board board, int n) {
        int[] jumps = new int[n + 1];
        if (board.getSnakes() != null)
            for (Snake s : board.getSnakes()) jumps[s.getStart()] = s.getEnd();
        if (board.getLadders() != null)
            for (Ladder l : board.getLadders()) jumps[l.getStart()] = l.getEnd();
        return jumps;
    }
}

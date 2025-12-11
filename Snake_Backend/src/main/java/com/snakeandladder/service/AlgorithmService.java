package com.snakeandladder.service;

import com.snakeandladder.model.Board;
import com.snakeandladder.model.Ladder;
import com.snakeandladder.model.Snake;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlgorithmService {

    public int bfs(Board board) {
        int n = board.getTotalCells();
        int[] jumps = getJumps(board, n);
        boolean[] visited = new boolean[n + 1];
        Queue<int[]> queue = new LinkedList<>();
        
        visited[1] = true;
        queue.add(new int[]{1, 0}); // cell, dist

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cell = curr[0];
            int dist = curr[1];

            if (cell == n) return dist;

            for (int dice = 1; dice <= 6; dice++) {
                int next = cell + dice;
                if (next <= n) {
                    if (jumps[next] != 0) next = jumps[next];
                    
                    if (!visited[next]) {
                        visited[next] = true;
                        queue.add(new int[]{next, dist + 1});
                    }
                }
            }
        }
        return -1;
    }

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

            for (int dice = 1; dice <= 6; dice++) {
                int v = u + dice;
                if (v <= n) {
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

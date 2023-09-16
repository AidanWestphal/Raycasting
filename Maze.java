
import java.util.*;

public class Maze {
    public Maze(int length, int width) {
        // Cells are easier to work with in terms of display as they can be controlled
        // with their boundaries as lines
        Maze = new Cell[length][width];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                Maze[i][j] = new Cell(i, j);
            }
        }
        // Preemptively adds first spot to the list.
        Path = new ArrayList<Cell>();
        Path.add(Maze[0][0]);
        generate(0, 0);

    }

    // Selects a random neighbor from a given location
    private Cell Neighbor(int L, int W) {
        ArrayList<Cell> Neighbors = new ArrayList<Cell>();
        // Add all neighbors to a list
        if (L - 1 >= 0 && Maze[L - 1][W].isVisited == false)
            Neighbors.add(Maze[L - 1][W]);
        if (L + 1 < Maze.length && Maze[L + 1][W].isVisited == false)
            Neighbors.add(Maze[L + 1][W]);
        if (W - 1 >= 0 && Maze[L][W - 1].isVisited == false)
            Neighbors.add(Maze[L][W - 1]);
        if (W + 1 < Maze[0].length && Maze[L][W + 1].isVisited == false)
            Neighbors.add(Maze[L][W + 1]);

        // Generate a random neghbor
        int rand = (int) (Math.random() * Neighbors.size());

        if (Neighbors.size() != 0)
            return Neighbors.get(rand);
        else
            return null;
    }

    private int visitedSquares;

    // Depth-first recursive backtracking to generate a maze
    private void generate(int L, int W) {
        // Keep track of visited squares
        if (Maze[L][W].isVisited == false) {
            visitedSquares++;
            Maze[L][W].isVisited = true;

        }
        // Have visited every square
        if (visitedSquares == Maze.length * Maze[0].length)
            return;
        
        // Grab a random neighbor to explore next
        Cell next = Neighbor(L, W);

        // Given no neighbors, backtrack to the last neighbor with an unvisited neighbor.
        for (; next == null;) {
            if (Path.size() == 1)
                return;

            Path.remove(Path.size() - 1);
            Cell temp = Path.get(Path.size() - 1);
            next = Neighbor(temp.L, temp.W);

            L = temp.L;
            W = temp.W;
        }

        // Given neighbors, find orientation and tunnel
        if (L == next.L && next.W == W - 1) {
            Path.add(Maze[L][W - 1]);
            // Open doors for each cell
            Maze[L][W].westOpen = true;
            Maze[L][W - 1].eastOpen = true;
            // Recursively continue the search
            generate(L, W - 1);
        }
        if (L == next.L && next.W == W + 1) {
            Path.add(Maze[L][W + 1]);
            Maze[L][W].eastOpen = true;
            Maze[L][W + 1].westOpen = true;
            generate(L, W + 1);
        }
        if (next.L == L + 1 && next.W == W) {
            Path.add(Maze[L + 1][W]);
            Maze[L][W].southOpen = true;
            Maze[L + 1][W].northOpen = true;
            generate(L + 1, W);
        }
        if (next.L == L - 1 && next.W == W) {
            Path.add(Maze[L - 1][W]);
            Maze[L][W].northOpen = true;
            Maze[L - 1][W].southOpen = true;
            generate(L - 1, W);
        }

    }

    public Cell[][] getMaze() {
        return Maze;
    }

    private ArrayList<Cell> Path;
    private Cell[][] Maze;
}

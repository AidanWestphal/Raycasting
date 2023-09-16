import java.util.*;
import java.awt.geom.*;

// Represents a scene in terms of lines: The scene is representative of the maze.

// Renders the maze into an array of lines.

// NOTE: YOU WOULD NOT RENDER USING THE SCENE. YOU MUST DISPLAY THE SCENE AS ITS A SET OF SHAPES IN ANOTHER JPANEL / JFRAME CLASS.

public class Scene {
    public Scene(int MazeL, int MazeW) {
        Maze = new Maze(MazeL, MazeW);
        Scene = new ArrayList<Line2D>();
        SizeIndex = 10;
        generate();

    }

    public Scene(ArrayList<Line2D> Elements) {
        Scene = Elements;
    }

    private void generate() {
        // Generate a maze
        Cell[][] Maze = this.Maze.getMaze();
        // Set the lines for the maze
        for (int i = 0; i < Maze.length; i++) {
            for (int j = 0; j < Maze[0].length; j++) {
                if (Maze[i][j].northOpen == false)
                    Scene.add(new Line2D.Double(10 + SizeIndex * j, 50 + SizeIndex * i, 10 + SizeIndex * (j + 1),
                            50 + SizeIndex * i));
                if (Maze[i][j].southOpen == false)
                    Scene.add(new Line2D.Double(10 + SizeIndex * j, 50 + SizeIndex * (i + 1), 10 + SizeIndex * (j + 1),
                            50 + SizeIndex * (i + 1)));
                if (Maze[i][j].westOpen == false)
                    Scene.add(new Line2D.Double(10 + SizeIndex * j, 50 + SizeIndex * i, 10 + SizeIndex * j,
                            50 + SizeIndex * (i + 1)));
                if (Maze[i][j].eastOpen == false)
                    Scene.add(new Line2D.Double(10 + SizeIndex * (j + 1), 50 + SizeIndex * i, 10 + SizeIndex * (j + 1),
                            50 + SizeIndex * (i + 1)));
            }
        }
    }

    // Specify the bounds of the window through here, if needed. This class does not
    // extend JFrame so others need to supply.
    // ALTERNATIVE: Scale the maze by some factor in another class.
    public void redoBounds(int width, int height) {
        SizeIndex = (int) ((Math.min(width, 50 + height) / Math.max(Maze.getMaze().length, Maze.getMaze()[0].length))
                / 1.1);
        Scene.clear();
        generate();
    }

    public ArrayList<Line2D> getScene() {
        return Scene;
    }

    public int SizeIndex;
    private ArrayList<Line2D> Scene;
    private Maze Maze;
}

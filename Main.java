
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

// FINAL INTEGRATION OF ALL COMPONENTS
public class Main extends JFrame implements MouseMotionListener, KeyListener {

    public Main(int MazeLength, int MazeWidth, int L, int H) {
        this.addMouseMotionListener(this);
        addMouseMotionListener(this);

        this.addKeyListener(this);
        addKeyListener(this);

        Rays = new ArrayList<Ray>();
        S = new Scene(MazeLength, MazeWidth);
        Camera = new Point2D.Double(150, 320);
        S.redoBounds(L, H);

        // PARAMETERS:
        // NumRays determines quality: the number of rays to be used
        // initAngle = 0 implies facing East to start
        // FOV is the total viewing angle in radians
        numRays = 100;
        initAngle = 0;
        FOV = 1.047;

        oldPos = 0;

        initRays(numRays, initAngle, FOV);

    }

    // Rotates the set of rays corresponding to mouse movement
    private void Rotate(double angle) {
        for (int i = 0; i < Rays.size(); i++) {
            // Change each angle and update distances
            Rays.get(i).changeAngle(angle);
            Rays.get(i).distance(S);
        }
        // Update main angle
        initAngle += angle;

        if (angle != 0) {
            repaint();
        }
    }

    public void initRays(int numRays, double angle, double FOV) {
        // FOV is total stretch of view.
        // Angle is central angle of view.
        // numRays needs to split up the FOV into X amounts. Center of numRays should be
        // at angle.
        double pt = FOV / numRays;
        double initAngle = angle - FOV / 2;
        for (int i = 0; i < numRays; i++) {
            Rays.add(new Ray(initAngle + i * pt, Camera));
            Rays.get(i).distance(S);
        }
    }

    // 2D PAINTING (manually change)

    public void paint2D(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        // Get scene
        ArrayList<Line2D> Scene = S.getScene();
        // Set background to black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        // Color the walls of the maze
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        for (int i = 0; i < Scene.size(); i++) {
            g.draw(Scene.get(i));
        }
        // Color each ray
        g.setColor(Color.GRAY);
        for (int i = 0; i < Rays.size(); i++) {
            g.draw(Rays.get(i).Line);
        }
    }

    // 3D PAINTING

    public void paint3D(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        // tan(FOV/2) = halfProjPlaneWidth/toProjPlane by means of FOV/2 splitting the
        // viewing angle into two
        // Distance to projection plane is f, the "focal length," but they aren't the
        // same necessarily.
        double f = getWidth() / 2 / Math.tan(FOV / 2);
        // Generates the height of the walls in the real world. Custom function which
        // scaled by size of the maze.
        double wallHeight = getHeight() / Math.sqrt(S.getScene().size()) * .8;

        // Set background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Print each rectangle for each ray
        for (int i = 0; i < numRays; i++) {
            // Collects a corrected distance by subtracting the initial angle from the angle
            // of the ray
            double corrected = Rays.get(i).distance * Math.cos(Rays.get(i).theta - initAngle);

            // 255,255,255 to 0,0,0 by distance:

            // LINEAR SCALE from 255 to 0, 0 being getWidth() + getHeight() /4 (half of avg)
            // (0, 255) to (AVG, 0) == y - 255 = (-255/AVG)(x - 0) means y = (-255/AVG)x +
            // 255
            double avg = 10 * S.SizeIndex;

            int colorIndex = (int) ((-255 / avg) * corrected + 255);
            if (colorIndex > 255) {
                colorIndex = 255;
            }
            if (colorIndex < 0) {
                colorIndex = 0;
            }

            g.setColor(new Color(colorIndex, colorIndex, colorIndex));

            // We have the following arrangement: The orthogonal distance to the wall
            // (corrected), the real hight of the wall (wallHeight), and the
            // distance to the image plane (f). We want to find the height of the wall in
            // the image plane (X). As such, X/f = wallHeight/corrected.
            int pinchFactor = (int) (wallHeight / corrected * f);

            // Buffer is the black space offsetting the rectangle, found on top and below
            // each.
            int buffer = (int) ((getHeight() - pinchFactor) / 2);

            g.fillRect(i * getWidth() / numRays, buffer, getWidth() / numRays, pinchFactor);
        }
    }

    // MAIN PAINT FUNCTION, can change between paint2D and paint3D
    public void paint(Graphics g1) {
        paint3D(g1);
    }

    public static void main(String[] args) {
        Main n = new Main(20, 20, 1200, 800);
        n.setBounds(5, 5, 1200, 800);
        n.setVisible(true);
        n.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private double initAngle;
    private double FOV;
    private int numRays;
    private Scene S;
    private ArrayList<Ray> Rays;
    private Point2D.Double Camera;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    double STD_STEP;

    public void Move(double X, double Y) {
        // Update camera and update rays
        Camera.x += X;
        Camera.y += Y;
        for (int i = 0; i < Rays.size(); i++) {
            Rays.get(i).distance(S);
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        STD_STEP = S.SizeIndex / 10;
        if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W') {
            // Move in direction of initAngle by 10.
            Move(STD_STEP * Math.cos(initAngle), STD_STEP * Math.sin(initAngle));
        }
        if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
            Move(-STD_STEP * Math.sin(initAngle), STD_STEP * Math.cos(initAngle));
        }
        if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
            Move(-STD_STEP * Math.cos(initAngle), -STD_STEP * Math.sin(initAngle));
        }
        if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
            Move(STD_STEP * Math.sin(initAngle), -STD_STEP * Math.cos(initAngle));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    private int oldPos;

    @Override
    public void mouseMoved(MouseEvent e) {
        int newPos = e.getX();
        // Calculate angle changed in mouse motion
        double changeX = (newPos - oldPos) * Math.PI / 180;
        oldPos = newPos;
        Rotate(changeX);
    }
}

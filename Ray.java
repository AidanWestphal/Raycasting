import java.awt.geom.*;
import java.util.*;

// Ray extends out from a camera point. Stops when it intersects a line within the scene.
public class Ray {

    public Ray(double theta, Point2D Camera) {
        this.Camera = (Point2D.Double) Camera;
        // General polar angle from due East
        this.theta = theta;

    }

    public void changeAngle(double factor) {
        theta += factor;
    }

    public void changePos(double XFactor, double YFactor) {
        Camera.x += XFactor;
        Camera.y += YFactor;
    }

    public double distance(Scene s) {

        // PART 1: Define the Ray's line.
        Line = new Line2D.Double(Camera.x, Camera.y, Camera.x + 1000 * Math.cos(theta),
                Camera.y + 1000 * Math.sin(theta));

        // PART 2: Find the min distance by checking all lines within the scene.
        double minD = Double.MAX_VALUE;

        ArrayList<Line2D> Scene = s.getScene();

        double y1 = Line.y1;
        double y2 = Line.y2;
        double x1 = Line.x1;
        double x2 = Line.x2;

        double minX = 0;
        double minY = 0;

        for (int i = 0; i < Scene.size(); i++) {
            if (Line.intersectsLine(Scene.get(i))) {
                // 1: make new reference points
                double x3 = Scene.get(i).getX1();
                double x4 = Scene.get(i).getX2();
                double y3 = Scene.get(i).getY1();
                double y4 = Scene.get(i).getY2();

                // 2: vector algorithm for intersection point of two lines
                double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
                if (d != 0) {
                    double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
                    double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

                    double dist = Math.sqrt((xi - x1) * (xi - x1) + (yi - y1) * (yi - y1));
                    if (dist < minD) {
                        minD = dist;
                        minX = xi;
                        minY = yi;
                    }
                }
            }
        }
        Line = new Line2D.Double(Camera.x, Camera.y, minX, minY);
        distance = minD;
        return minD;
    }

    public Line2D.Double Line;
    public double theta;
    private Point2D.Double Camera;
    public double distance;
}

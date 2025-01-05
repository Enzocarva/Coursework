/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument in insert()\n");

        if (!points.contains(p))
            points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument in contains()\n");

        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points)
            point.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("null argument in range()\n");

        // Get the coordinates of the query range/rectangle, bottom left and top right coordinates (min and max)
        double xmin = rect.xmin();
        double ymin = rect.ymin();
        double xmax = rect.xmax();
        double ymax = rect.ymax();

        // Stack where points within the query range will be inserted (if any)
        Stack<Point2D> pointsInRange = new Stack<>();

        // Go through every point in the unit square, get its coordinates and compare them to the rectangle's coordinates
        for (Point2D point : points) {
            double x = point.x();
            double y = point.y();

            if (x >= xmin && x <= xmax && y >= ymin && y <= ymax) // if in rectangle/range, put it in stack
                pointsInRange.push(point);
        }
        return pointsInRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument in nearest()\n");
        if (points.isEmpty()) return null;

        // Go through every point int the unit square and check which is closest to the query point by calculating their distance
        double distance;
        double closest = 2.0; // Largest possible distance = sqrt 2, around 1.4
        Point2D closestPoint = null;
        for (Point2D point : points) {
            // Distance formula (unit square values range from 0.0 to 1.0)
            // distance = Math.sqrt(Math.pow((point.x() - p.x()), 2) + Math.pow((point.y() - p.y()), 2));
            distance = point.distanceSquaredTo(p);
            if (distance < closest) {
                closest = distance;
                closestPoint = point;
            }
        }
        return closestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}

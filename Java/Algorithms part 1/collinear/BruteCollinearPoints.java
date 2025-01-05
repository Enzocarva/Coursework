/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segments = new ArrayList<>();
    private int numberOfSegments;

    public BruteCollinearPoints(Point[] points) {

        /// Check for corner cases
        if (points == null) throw new IllegalArgumentException("Input is null"); // Argument is null

        for (int i = 1; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Input contains null point");
        }

        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);
        for (int i = 1; i < pointsCopy.length; i++) {
            if (pointsCopy[i].compareTo(pointsCopy[i - 1]) == 0)
                throw new IllegalArgumentException("Input contains duplicate.");
        }

//        Point[] copyOfPoints = Arrays.copyOf(points, points.length); // Make a copy of points array and sort it
        Arrays.sort(points);

        // Check whether the slopes of all points (p, q, r, s) are the same. If so, they are collinear, and it's add it to segments array list
        for (int p = 0; p < points.length - 3; p++)
            for (int q = p + 1; q < points.length - 2; q++)
                for (int r = q + 1; r < points.length - 1; r++)
                    for (int s = r + 1; s < points.length; s++) {
                        if (points[p].slopeTo(points[q]) == points[p].slopeTo(points[r]) &&
                                points[p].slopeTo(points[r]) == points[p].slopeTo(points[s]))
                            segments.add(new LineSegment(points[p], points[s]));
                    }
        numberOfSegments = segments.size();
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }

    // Unit tests
    public static void main(String[] args) {
        In input = new In(args[0]);
        int size = input.readInt();
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            int x = input.readInt();
            int y = input.readInt();
            points[i] = new Point(x, y);
        }

        // Set the grid's max values and draw the points
        StdDraw.enableDoubleBuffering(); // Delays the calls to line() until the next call to show()
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points)
            p.draw();
        StdDraw.show();

        BruteCollinearPoints lines = new BruteCollinearPoints(points);
        StdOut.println("Testing the collinear points, looking for " + lines.numberOfSegments() + " lines");
        for (LineSegment segment : lines.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

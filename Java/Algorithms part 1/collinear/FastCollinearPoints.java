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

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {

        // Check for corner cases
        if (points == null) throw new IllegalArgumentException("Input is null"); // Argument is null

        for (int i = 1; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Input contains null point");
        }

        Point[] copyOfPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(copyOfPoints);
        for (int i = 1; i < copyOfPoints.length; i++) {
            if (copyOfPoints[i].compareTo(copyOfPoints[i - 1]) == 0)
                throw new IllegalArgumentException("Input contains duplicate.");
        }

        copyOfPoints = Arrays.copyOf(points, points.length); // Make a copy array to be able to change its order
        for (Point p : points) {                                     // Iterate through every point in original points array
            Arrays.sort(copyOfPoints, p.slopeOrder());               // Sort the copy array in order of the slopes relative to the p point, which is the "origin"
            double slope = p.slopeTo(copyOfPoints[0]);               // Find the slope between the origin point p from the original array and the first point in the
            int count = 1, i;                                        // copy array. Every slope will be compared to this slope for this "run". Set count to 1 due to
            for (i = 1; i < copyOfPoints.length; i++) {              // that first "line" between the origin point and first point in copy array. Need 3 lines (count)
                if (p.slopeTo(copyOfPoints[i]) == slope)
                    count++;    // to have 4 collinear points. This if statement "breaks" the for loop, since they are ordered by
                else {                                               // slope, they will only be checked sequentially, so when the if is false, there are 4 points
                    if (count >= 3) {                                // with that particular slope.
                        Arrays.sort(copyOfPoints, i - count, i); // Sort the collinear points based on y value, and only add the segment when the origin is
                        if (p.compareTo(copyOfPoints[i - count]) < 0)     // minimum (lowest) point. This is done to avoid duplicate line segments.
                            segments.add(new LineSegment(p, copyOfPoints[i - 1]));
                    }
                    slope = p.slopeTo(copyOfPoints[i]);
                    count = 1;
                }
            }
            if (count >= 3) {                                           // This whole if statement is only here to catch the corner case where the 4th point is found
                Arrays.sort(copyOfPoints, i - count, i);      // exactly on the last index of the loop, which means it wouldn't trigger the "else" and add
                if (p.compareTo(copyOfPoints[i - count]) < 0)          // that particular line segment.
                    segments.add(new LineSegment(p, copyOfPoints[i - 1]));
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    // Unit testing
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

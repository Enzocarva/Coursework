/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    // Nested class Node for the nodes of the tree
    private static class Node {
        private Point2D point;
        private Node left;           // also bottom
        private Node right;          // also top
        private boolean orientation; // true = Vertical, false = Horizontal

        public Node(Point2D point) {
            this.point = point;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("received null argument in insert()\n");
        root = insert(root, p, VERTICAL);
    }

    private Node insert(Node h, Point2D p, boolean orientation) {
        if (h == null) {
            Node newNode = new Node(p);
            newNode.orientation = orientation;
            size++;
            return newNode;
        }

        double x = p.x();           // Point being inserted x
        double y = p.y();           // Point being inserted y
        double hx = h.point.x();    // Current existing node's x
        double hy = h.point.y();    // Current existing node's y

        if (h.orientation == VERTICAL) {
            if (x < hx) h.left = insert(h.left, p, !h.orientation); // Insert the opposite orientation of the parent node
            else if (x > hx) h.right = insert(h.right, p, !h.orientation);
            else if (y != hy) h.right = insert(h.right, p, !h.orientation); // If x == hx, only insert if y != hy, because otherwise the node already exists
        } else { // Orientation == horizontal
            if (y < hy) h.left = insert(h.left, p, !h.orientation);
            else if (y > hy) h.right = insert(h.right, p, !h.orientation);
            else if (x != hx) h.right = insert(h.right, p, !h.orientation); // If y == hy, only insert if x != hx, because otherwise the node already exists
        }
        return h;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("received null argument in contains()\n");
        return contains(root, p);
    }

    private boolean contains(Node h, Point2D p) {
        while (h != null) {
            if (h.orientation == VERTICAL) {
                if (p.x() < h.point.x()) h = h.left;
                else if (p.x() > h.point.x()) h = h.right;
                else if (p.y() != h.point.y()) h = h.right;
                else return true; // can only get here if all coordinates are equal
            } else { // h.orientation == HORIZONTAL
                if (p.y() < h.point.y()) h = h.left;
                else if (p.y() > h.point.y()) h = h.right;
                else if (p.x() != h.point.x()) h = h.right;
                else return true; // can only get here if all coordinates are equal
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, 0.0, 0.0, 1.0, 1.0);
    }

    private void draw(Node h, double xmin, double ymin, double xmax, double ymax) {
        if (h == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        h.point.draw();

        if (h.orientation == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            RectHV rectangle = new RectHV(h.point.x(), ymin, h.point.x(), ymax); // Vertical line
            rectangle.draw();
            draw(h.right, h.point.x(), ymin, xmax, ymax);
            draw(h.left, xmin, ymin, h.point.x(), ymax);
        } else { // h.orientation == HORIZONTAL
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            RectHV rectangle = new RectHV(xmin, h.point.y(), xmax, h.point.y()); // Horizontal line
            rectangle.draw();
            draw(h.right, xmin, h.point.y(), xmax, ymax);
            draw(h.left, xmin, ymin, xmax, h.point.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("null argument in range()\n");

        Stack<Point2D> pointsInRect = new Stack<>();
        RectHV rootRect = new RectHV(0.0, 0.0, 1.0, 1.0); // Each node corresponds to an axis-aligned rectangle in the unit square,
        range(root, rootRect, rect, pointsInRect);                              // which encloses all of the points in its subtree. The root corresponds to
        return pointsInRect;                                                    // the unit square; the left and right children of the root corresponds to the
    }                                                                           // two rectangles split by the x-coordinate of the point at the root; and so on.

    private void range(Node h, RectHV hRect, RectHV queryRect, Stack<Point2D> pointsInRect) {
        if (h == null) return;
        if (!hRect.intersects(queryRect)) return;

        if (queryRect.contains(h.point)) pointsInRect.push(h.point); // Is the point of the current node in the query rectangle?
//                                                                                                ^
        if (h.orientation == VERTICAL) { // Refer to big comment above to the right to understand |
            double xmin = hRect.xmin();
            double ymin = hRect.ymin();
            double xmax = h.point.x();
            double ymax = hRect.ymax();
            range(h.left, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect);

            xmin = h.point.x();
            xmax = hRect.xmax();
            range(h.right, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect);
        } else { // h.orientation == HORIZONTAL
            double xmin = hRect.xmin();
            double ymin = hRect.ymin();
            double xmax = hRect.xmax();
            double ymax = h.point.y();
            range(h.left, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect); // Bottom rect (left)

            ymin = h.point.y();
            ymax = hRect.ymax();
            range(h.right, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect); // Top rect (right)
        }
    }

    /* To find a closest point to a given query point, start at the root and recursively search in both subtrees using the following pruning rule: if the
    closest point discovered so far is closer than the distance between the query point and the rectangle corresponding to a node, there is no need to explore
    that node (or its subtrees). That is, search a node only if it might contain a point that is closer than the best one found so far. The effectiveness
    of the pruning rule depends on quickly finding a nearby point. To do this, organize the recursive method so that when there are two possible subtrees to go
    down, you always choose the subtree that is on the same side of the splitting line as the query point as the first subtree to explore—the closest point
    found while exploring the first subtree may enable pruning of the second subtree. */
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument in nearest()");
        if (isEmpty()) return null;

        Node closest = new Node(root.point); // Start at root
        closest.left = root.left;
        closest.right = root.right;
        closest.orientation = root.orientation;

        RectHV rootRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        nearest(root, rootRect, closest, p);
        return closest.point;
    }

    private void nearest(Node h, RectHV hRect, Node closest, Point2D queryP) {
        if (h == null) return;

        // Check for closest and update if one is found
        if (queryP.distanceSquaredTo(h.point) < queryP.distanceSquaredTo(closest.point))
            closest.point = h.point;

        double hx = h.point.x();
        double hy = h.point.y();
        double x = queryP.x();
        double y = queryP.y();
        double xmin, ymin, xmax, ymax;
        if (h.orientation == VERTICAL) {
            xmin = hRect.xmin();
            ymin = hRect.ymin();
            xmax = hx;
            ymax = hRect.ymax();
            RectHV leftRect = new RectHV(xmin, ymin, xmax, ymax);

            xmin = hx;
            xmax = hRect.xmax();
            RectHV rightRect = new RectHV(xmin, ymin, xmax, ymax);

            // if query point x coordinate is greater, look right, else look left
            if (x >= hx) {
                nearest(h.right, rightRect, closest, queryP);
                if (leftRect.distanceSquaredTo(queryP) < closest.point.distanceSquaredTo(queryP))
                    nearest(h.left, leftRect, closest, queryP);
            } else { // x <= hx
                nearest(h.left, leftRect, closest, queryP);
                if (rightRect.distanceSquaredTo(queryP) < closest.point.distanceSquaredTo(queryP))
                    nearest(h.right, rightRect, closest, queryP);
            }
        } else { // h.orientation == HORIZONTAL
            xmin = hRect.xmin();
            ymin = hRect.ymin();
            xmax = hRect.xmax();
            ymax = hy;
            RectHV leftRect = new RectHV(xmin, ymin, xmax, ymax); // Look bottom (left)

            ymin = hy;
            ymax = hRect.ymax();
            RectHV rightRect = new RectHV(xmin, ymin, xmax, ymax); // Look top (right)

            // if query point y coordinate is greater, look top (right), else look bottom (left)
            if (y >= hy) {
                nearest(h.right, rightRect, closest, queryP);
                if (leftRect.distanceSquaredTo(queryP) < closest.point.distanceSquaredTo(queryP))
                    nearest(h.left, leftRect, closest, queryP);
            } else { // y <= hy
                nearest(h.left, leftRect, closest, queryP);
                if (rightRect.distanceSquaredTo(queryP) < closest.point.distanceSquaredTo(queryP))
                    nearest(h.right, rightRect, closest, queryP);
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}

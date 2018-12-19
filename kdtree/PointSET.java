import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * Write a data type to represent a set of points in the unit square (all points have x- and
 * y-coordinates between 0 and 1) using a 2d-tree to support efficient range search (find all of the
 * points contained in a query rectangle) and nearest-neighbor search (find a closest point to a
 * query point).
 * <p>
 * Brute-force implementation. Write a mutable data type PointSET.java that represents a set of
 * points in the unit square. Implement the following API by using a red–black BST: Implementation
 * requirements.  You must use either SET or java.util.TreeSet; do not implement your own red–black
 * BST.
 * <p>
 * Corner cases.  Throw a java.lang.IllegalArgumentException if any argument is null. Performance
 * requirements.  Your implementation should support insert() and contains() in time proportional to
 * the logarithm of the number of points in the set in the worst case; it should support nearest()
 * and range() in time proportional to the number of points in the set.
 */
public class PointSET {

    private final TreeSet<Point2D> redBlackBST = new TreeSet<>();

    /**
     * construct an empty set of points
     */
    public PointSET() {
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        //
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {

        checkForNull(p);
        redBlackBST.add(p);
    }

    private List<Point2D> points() {
        List<Point2D> result = new ArrayList<Point2D>();

        Iterator<Point2D> i = redBlackBST.iterator();
        while (i.hasNext()) {
            result.add(i.next());
        }

        return result;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     * <p>
     * Note: this is a suboptimal solution, since the linear search results in O(n) complexity, but
     * using a red black tree results in O(n lg n) which is slightly worst.
     *
     * @param p the query point
     * @return the nearest point to the query point
     */
    //
    public Point2D nearest(Point2D p) {
        checkForNull(p);

        if (isEmpty()) {
            return null;
        }

        TreeSet<Point2D> ts = new TreeSet<>((p1, p2) -> {
            return (int) (Math.signum(p1.distanceSquaredTo(p) - p2.distanceSquaredTo(p)));
        });
        ts.clear();
        for (Point2D px : redBlackBST) {
            ts.add(px);
        }
        return ts.first();
    }

    // is the set empty?
    public boolean isEmpty() {
        return redBlackBST.isEmpty();
    }

    // number of points in the set
    public int size() {
        return redBlackBST.size();
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {

        checkForNull(p);
        return redBlackBST.contains(p);
    }

    private static void checkForNull(Object p) {
        if (p == null) {
            throw new IllegalArgumentException("argument should be non null!");
        }
    }

    // draw all points to standard draw
    public void draw() {

        for (Point2D point : redBlackBST) {
            point.draw();
        }
    }

    /**
     * Returns all points that are inside the rectangle (or on the boundary)
     * <p>
     * Note: using a binary search tree's range operation removes the needs to compare points above
     * and below the search rectangle. Still need to compare points by the x coordinate below the
     * top and above the bottom of the search rectangle.
     *
     * @param rect the search rectangle
     * @return all points inside or on the edge of the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {

        checkForNull(rect);

        double x1 = rect.xmin();
        double x2 = rect.xmax();
        double y1 = rect.ymin();
        double y2 = rect.ymax();

        Point2D fromPoint = new Point2D(x1, y1);
        Point2D toPoint = new Point2D(x2, y2);

        java.util.NavigableSet<Point2D> points = redBlackBST.subSet(fromPoint, true, toPoint, true);
        List<Point2D> result = new ArrayList<>();
        for (Point2D p : points) {
            if (x1 <= p.x() && x2 >= p.x()) {
                result.add(p);
            }
        }
        return result;
    }
}
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;
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

    private TreeSet<Point2D> redBlackBST = new TreeSet<>();

    /**
     * construct an empty set of points
     */
    public PointSET() {
    }

    private static void time(Runnable r) {
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        System.out.println("" + (end - start) + "ns");
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
/*
        int max = 100;

        StdDraw.setScale(0, max);
        StdDraw.setPenColor(Color.BLACK);

        PointSET ps = new PointSET();
        for (int y = 0; y < max; y += max / 10) {
            for (int x = 0; x < max; x += max / 10) {
                Point2D p = new Point2D(StdRandom.uniform(0,max), StdRandom.uniform(0,max));
                p.draw();
                ps.insert(p);
            }
        }


        RectHV rect = new RectHV(max * 0.25, max * 0.25, 0.75 * max, 0.75 * max);
        StdDraw.setPenColor(Color.ORANGE);
        rect.draw();

        StdDraw.setPenColor(Color.RED);
        Iterable<Point2D> range = ps.range(rect);
        for (Point2D p : range) {
            StdDraw.circle(p.x(), p.y(), max / 50);
            p.draw();
        }
*/


        int max = 100;
        PointSET ps = new PointSET();
        for (int i = 0; i < max; i++) {
            int rndX = StdRandom.uniform(0, max);
            int rndY = StdRandom.uniform(0, max);
            Point2D p = new Point2D(rndX, rndY);
            // StdDraw.circle(rndX, rndY, max / 100);
            // p.draw();
            ps.insert(p);
        }
        StdDraw.setScale(0, max);
        StdDraw.enableDoubleBuffering();

        while (true) {
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.filledRectangle(0, 0, max, max);


            StdDraw.setPenColor(Color.BLACK);


            for (Point2D point : ps.points()) {
                // System.out.println(point);
                StdDraw.circle(point.x(), point.y(), 2);
            }
            StdDraw.setPenColor(Color.RED);
            Point2D latticePoint = new Point2D(StdDraw.mouseX(), StdDraw.mouseY());


            Point2D nearest = ps.nearest(latticePoint);
            // System.out.println(nearest);
            StdDraw.line(latticePoint.x(), latticePoint.y(), nearest.x(), nearest.y());

            StdDraw.circle(latticePoint.x(), latticePoint.y(), 1);
            StdDraw.show();
        }

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

    // a nearest neighbor in the set to point p; null if the set is empty
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

    // all points that are inside the rectangle (or on the boundary)
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
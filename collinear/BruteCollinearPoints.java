import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final Point[] points;
    private final List<LineSegment> lineSegments;

    /**
     * finds all line segments containing 4 points
     *
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
        this.points = clonePoints(points);
        this.lineSegments = createLineSegments();
    }

    /**
     * The method segments() should include each line segment containing 4 points exactly once. If 4
     * points appear on a line segment in the order p→q→r→s, then you should include either the line
     * segment p→s or s→p (but not both) and you should not include subsegments such as p→r or q→r.
     * For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more
     * collinear points.
     *
     * @return
     */
    private List<LineSegment> createLineSegments() {

        List<Point[]> listOfLineSegments = new ArrayList<>();

        for (int p = 0; p < points.length; p++) {

            for (int q = 0; q < points.length; q++) {
                if (q != p) {

                    double pq = points[p].slopeTo(points[q]);

                    for (int r = 0; r < points.length; r++) {
                        if (r != p && r != q) {

                            double qr = points[q].slopeTo(points[r]);

                            iterateOverPoints(listOfLineSegments, p, q, pq, r, qr);
                        }
                    }
                }
            }
        }


        return getLineSegmentsFromStartAndEndPoints(listOfLineSegments);
    }

    private void iterateOverPoints(List<Point[]> listOfLineSegments, int p, int q, double pq, int r,
                                   double qr) {
        for (int s = 0; s < points.length; s++) {

            if (s != r && s != q && s != p) {

                double rs = points[r].slopeTo(points[s]);

                if (pq == qr && qr == rs) {

                    addLineSegment(listOfLineSegments, new Point[] {
                            points[p],
                            points[q],
                            points[r],
                            points[s]
                    });
                }
            }
        }
    }


    /**
     * Makes a copy from the points (to avoid PMD errors), checks if the points array is not null,
     * checks if array elements are not null
     *
     * @param points the array of {@link Point}s
     * @return the copy of <code>points</code>
     */
    private static Point[] clonePoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points should not be null");
        }
        Point[] clone = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException(
                        "point[" + i + "] is null, no null values are allowed");
            }
            else {
                clone[i] = points[i];
            }
        }
        Arrays.sort(clone);
        checkDuplicates(clone);
        return clone;
    }

    private static void checkDuplicates(Point[] clone) {
        for (int i = 1; i < clone.length; i++) {
            if (clone[i - 1].compareTo(clone[i]) == 0) {
                throw new IllegalArgumentException("Duplicate points!");
            }
        }

    }

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }


    /**
     * the number of line segments
     *
     * @return
     */
    public int numberOfSegments() {
        return lineSegments.size();
    }

    /**
     * the line segments
     *
     * @return
     */
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }

    private boolean equalPoints(Point p0, Point p1) {

        if (p0 == null && p1 == null) {
            return true;
        }
        else if (p0 == null && p1 != null || p0 != null && p1 == null) {
            return false;
        }
        else if (p0 != null && p1 != null) {
            return p0.compareTo(p1) == 0;
        }
        return false;
    }


    private boolean sameArray(Point[] arr1, Point[] arr2) {

        if (arr1.length == arr2.length) {
            for (int j = 0; j < arr1.length; j++) {
                if (!equalPoints(arr1[j], arr2[j])) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    private void addLineSegment(List<Point[]> listOfLineSegments, Point[] pointsOfLineSegment) {

        Arrays.sort(pointsOfLineSegment);

        boolean found = false;
        for (Point[] current : listOfLineSegments) {
            if (sameArray(current, pointsOfLineSegment)) {
                found = true;
            }
        }
        if (!found) {
            listOfLineSegments.add(pointsOfLineSegment);
        }

    }


    private List<LineSegment> getLineSegmentsFromStartAndEndPoints(
            List<Point[]> listofLineSegemnts) {
        List<LineSegment> result = new ArrayList<>();

        for (Point[] segment : listofLineSegemnts) {
            result.add(new LineSegment(segment[0], segment[segment.length - 1]));
        }


        return result;
    }


}
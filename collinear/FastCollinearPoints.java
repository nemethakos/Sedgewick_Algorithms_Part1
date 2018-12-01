import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>A faster, sorting-based solution. Remarkably, it is possible to solve the problem much faster
 * than the brute-force solution described above. Given a point p, the following method determines
 * whether p participates in a set of 4 or more collinear points.
 * <ul>
 * <li>Think of p as the origin.
 * <li>For each other point q, determine the slope it makes with p.
 * <li>Sort the points according to the slopes they makes with p.
 * <li>Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect
 * to p. If so, these points, together with p, are collinear.
 * </ul>
 * <p>Applying this method for each of the n points in turn yields an efficient algorithm to the
 * problem. The algorithm solves the problem because points that have equal slopes with respect to p
 * are collinear, and sorting brings such points together. The algorithm is fast because the
 * bottleneck operation is sorting.
 */
public class FastCollinearPoints {

    private final Point[] points;
    private final List<LineSegment> lineSegments;

    /**
     * Finds all line segments containing 4 or more points Corner cases. Throw a
     * java.lang.IllegalArgumentException if the argument to the constructor is null, if any point
     * in the array is null, or if the argument to the constructor contains a repeated point.
     *
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
        this.points = clonePoints(points);
        this.lineSegments = createLineSegments();
    }


    /**
     * <p>The method segments() should include each maximal line segment containing 4 (or more)
     * points exactly once. For example, if 5 points appear on a line segment in the order
     * p→q→r→s→t, then do not include the subsegments p→s or q→t.
     * <p>Performance requirement. The order of growth of the running time of your program should
     * be n*n log n in the worst case and it should use space proportional to n plus the number of
     * line segments returned. FastCollinearPoints should work properly even if the input has 5 or
     * more collinear points.
     *
     * @return
     */
    private List<LineSegment> createLineSegments() {

        List<Point[]> listOfLineSegments = new ArrayList<>();

        for (int p = 0; p < points.length; p++) {

            Point[] otherPoints = getOtherPoints(p);

            if (otherPoints.length > 0) {

                Arrays.sort(otherPoints, points[p].slopeOrder());

                addSameSlopeLineSegments(listOfLineSegments, points[p], otherPoints);
            }
        }

        return getLineSegmentsFromStartAndEndPoints(listOfLineSegments);
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
                    // System.out.println("Different");
                    return false;
                }
            }
        }
        else {
            //    System.out.println("Different");
            return false;
        }
        // System.out.println("Same");
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

    /**
     * <p>Returns {@link List} of {@link LineSegment}s which has the same slope compared to
     * <code>point</code>. For each series the first and last occurence of the same slope {@link
     * Point} will make a {@link LineSegment}
     * <p>Check if any 3 (or more) adjacent points in the sorted order have equal slopes with
     * respect to p. If so, these points, together with p, are collinear.
     *
     * @param point       the base point
     * @param otherPoints {@link List} of {@link Point}s sorted by slope in reference to
     *                    <code>point</code>
     * @return the {@link List} of {@link LineSegment}s which has the same slope in reference to
     * <code>point</code>.
     */
    private void addSameSlopeLineSegments(List<Point[]> listOfLineSegment, Point point,
                                          Point[] otherPoints) {

        double slope;
        double prevSlope;
        int i = 1;
        slope = point.slopeTo(otherPoints[0]);

        while (i < otherPoints.length) {

            // find the start of a series
            do {
                prevSlope = slope;
                slope = point.slopeTo(otherPoints[i]);
                i++;
            }
            while (i < otherPoints.length && prevSlope != slope);


            if (prevSlope == slope) {

                int startIndex = i - 2;

                int endIndex = i - 1;

                if (i < otherPoints.length) {

                    double intervalSlope = slope;

                    // find the end of the interval
                    while (i < otherPoints.length && intervalSlope == point
                            .slopeTo(otherPoints[i])) {
                        i++;
                    }


                    endIndex = i - 1;
                }

                if (endIndex >= startIndex + 2) {

                    Point[] pointsOfLineSegment = new Point[1 + endIndex - startIndex + 1];
                    pointsOfLineSegment[0] = point;

                    for (int j = 1; j <= endIndex - startIndex + 1; j++) {
                        pointsOfLineSegment[j] = otherPoints[startIndex - 1 + j];
                    }

                    addLineSegment(listOfLineSegment, pointsOfLineSegment);
                }
            }
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

    private Point[] getOtherPoints(int p) {
        Point[] other = new Point[points.length - 1];
        int j = 0;
        for (int i = 0; i < points.length; i++) {
            if (i != p) {
                other[j] = points[i];
                j++;
            }
        }
        return other;
    }

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

    /**
     * Sample client. This client program takes the name of an input file as a command-line
     * argument; read the input file (in the format specified below); prints to standard output the
     * line segments that your program discovers, one per line; and draws to standard draw the line
     * segments.
     *
     * @param args
     */
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

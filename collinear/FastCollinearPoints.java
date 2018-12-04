import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
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

    /**
     * Stores the points
     */
    private final Point[] points;
    /**
     * Stores the line segments
     */
    private final List<LineSegment> lineSegments = new ArrayList<>();

    /**
     * Finds all line segments containing 4 or more points Corner cases. Throw a
     * java.lang.IllegalArgumentException if the argument to the constructor is null, if any point
     * in the array is null, or if the argument to the constructor contains a repeated point.
     *
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
        this.points = clonePointsWithNullAndDuplicateCheck(points);
        if (points.length > 3) {
            createLineSegments();
        }
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
    private void createLineSegments() {

        // use otherPoints as the working array
        Point[] otherPoints = clonePointsWithoutCheck(points);

        // iterate over the points to use the current one as the basePoint
        for (int basePointIndex = 0; basePointIndex < points.length - 1; basePointIndex++) {

            Point basePoint = points[basePointIndex];

            // sort the otherPoints by the slope order of the basePoint
            Arrays.sort(otherPoints, basePoint.slopeOrder());

            // select the appropriate points from the sorted array to be the line segments
            addSameSlopeLineSegments(basePoint, otherPoints);

        }
    }

    /**
     * <p>Returns {@link List} of {@link LineSegment}s which has the same slope compared to
     * <code>point</code>. For each series the first and last occurence of the same slope {@link
     * Point} will make a {@link LineSegment}
     * <p>Check if any 3 (or more) adjacent points in the sorted order have equal slopes with
     * respect to p. If so, these points, together with p, are collinear.
     *
     * @param basePoint   the base point
     * @param otherPoints {@link List} of {@link Point}s sorted by slope in reference to
     *                    <code>point</code>
     * @return the {@link List} of {@link LineSegment}s which has the same slope in reference to
     * <code>point</code>.
     */
    private void addSameSlopeLineSegments(Point basePoint,
                                          Point[] otherPoints) {

        // current slope value
        double slope;
        // previous slope value
        double prevSlope;
        // current index
        int i = 1;
        // initialize current slope
        slope = basePoint.slopeTo(otherPoints[0]);

        // loop until index is valid
        while (i < otherPoints.length) {

            // find the start of a series of same slope values
            do {
                prevSlope = slope;
                slope = basePoint.slopeTo(otherPoints[i]);
                i++;
            }
            while (i < otherPoints.length && Double.compare(prevSlope, slope) != 0);


            // check if the end of the series is reached
            if (Double.compare(prevSlope, slope) == 0) {

                int startIndex = i - 2;

                int endIndex = i - 1;

                // find the end of the interval
                if (i < otherPoints.length) {

                    double intervalSlope = slope;

                    // find the end of the interval
                    while (i < otherPoints.length && intervalSlope == basePoint
                            .slopeTo(otherPoints[i])) {
                        i++;
                    }

                    endIndex = i - 1;
                }

                // check if there are at least 3 elements in the found series
                if (endIndex >= startIndex + 2) {

                    // sort the array by natural ordering
                    Arrays.sort(otherPoints, startIndex, endIndex + 1);

                    // only add the line segment, if it starts with the basePoint. This way
                    // the duplicates can be avoided.
                    int compareResult = basePoint.compareTo(otherPoints[startIndex]);
                    if (compareResult <= 0) {
                        LineSegment lineSegment = new LineSegment(basePoint, otherPoints[endIndex]);
                        lineSegments.add(lineSegment);
                    }
                }
            }
        }
    }

    /**
     * Creates a shallow copy of the array <code>points</code>
     *
     * @param points the array to clone
     * @return the duplicate of the array
     */
    private static Point[] clonePointsWithoutCheck(Point[] points) {
        Point[] clone = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            clone[i] = points[i];
        }
        return clone;
    }

    /**
     * Creates a shallow copy from the array <code>points</code>, check for null argument value,
     * null and duplicate array elements and throws {@link IllegalArgumentException} when encounters
     * them
     *
     * @param points the array of {@link Point}
     * @return the copied array
     * @throws IllegalArgumentException when the <code>points</code> array is null, array elements
     *                                  null or duplicated.
     */
    private static Point[] clonePointsWithNullAndDuplicateCheck(Point[] points) {
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

    /**
     * Returns true, when the array of {@link Point}s contains duplicates
     *
     * @param pointsArray the array containing the {@link Point}s
     */
    private static void checkDuplicates(Point[] pointsArray) {
        for (int i = 1; i < pointsArray.length; i++) {
            if (pointsArray[i - 1].compareTo(pointsArray[i]) == 0) {
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

        StdDraw.show();
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(0, 0, 32768, 32768);

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);

        StdDraw.setPenColor(30, 30, 150);
        for (LineSegment segment : collinear.segments()) {
            segment.draw();
            StdOut.println(segment);
        }
        StdDraw.setPenColor(Color.WHITE);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
    }
}

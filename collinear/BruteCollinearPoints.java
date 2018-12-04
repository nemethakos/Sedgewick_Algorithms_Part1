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

        for (int p0 = 0; p0 < points.length - 3; p0++) {

            for (int p1 = p0 + 1; p1 < points.length - 2; p1++) {

                double pq = points[p0].slopeTo(points[p1]);

                for (int p2 = p1 + 1; p2 < points.length - 1; p2++) {

                    double qr = points[p1].slopeTo(points[p2]);

                    for (int p3 = p2 + 1; p3 < points.length; p3++) {

                        double rs = points[p2].slopeTo(points[p3]);

                        if (Double.compare(pq, qr) == 0 && Double.compare(qr, rs) == 0) {

                            addLineSegment(listOfLineSegments, new Point[] {
                                    points[p0],
                                    points[p1],
                                    points[p2],
                                    points[p3]
                            });

                        }
                    }
                }
            }
        }


        return getLineSegmentsFromStartAndEndPoints(listOfLineSegments);
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

    /**
     * Returns true if the two array contains identical elements
     *
     * @param arr1 the first array
     * @param arr2 the second array
     * @return true, if the two array are identical, false otherwise
     */
    private boolean sameArray(Point[] arr1, Point[] arr2) {

        if (arr1.length == arr2.length) {
            for (int j = 0; j < arr1.length; j++) {
                if ((arr1[j] != arr2[j])) {

                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    /**
     * Adds the <code>pointsOfLineSegment</code> to <code>listofLineSegemnts</code> only, when the
     * later does not contain the earlier
     *
     * @param listOfLineSegments  the list of already added line segments
     * @param pointsOfLineSegment the new array of {@link Point}s to be added to the
     *                            <code>listOfLineSegments</code>
     */
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
     * Returns the {@link List} of {@link LineSegment} from the <code>listOfLineSegments</code>
     *
     * @param listofLineSegemnts the {@link List} of array of {@link Point}s containing the line
     *                           segments defined by the points
     * @return
     */
    private List<LineSegment> getLineSegmentsFromStartAndEndPoints(
            List<Point[]> listofLineSegemnts) {
        List<LineSegment> result = new ArrayList<>();

        for (Point[] segment : listofLineSegemnts) {
            result.add(new LineSegment(segment[0], segment[segment.length - 1]));
        }

        return result;
    }
}
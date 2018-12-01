/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * <p>Returns the slope between this point and the specified point.
     * <p>Formally, if the two points are (x0, y0) and (x1, y1), then the
     * slope is (y1 - y0) / (x1 - x0).
     * <p>For completeness, the slope is defined to be
     * <ul>
     *
     * <li> +0.0 if the line segment connecting the two
     * points is horizontal (dy == 0);
     *
     * <li>Double.POSITIVE_INFINITY if the line segment is vertical (dx == 0) and
     *
     * <li>Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * </ul>
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        double result = 0;
        Point p0 = this;
        Point p1 = that;

        double dy = p1.y - p0.y;
        double dx = p1.x - p0.x;

        // normal: dy/dx
        if (dx != 0 && dy != 0) {
            result = dy / dx;
        }
        // horizontal: 0
        else if (dx != 0 && dy == 0) {
            result = 0.0;
        }
        // vertical: positive infinity
        else if (dx == 0 && dy != 0) {
            result = Double.POSITIVE_INFINITY;
        }
        // degenerate: negative infinity
        else if (dx == 0 && dy == 0) {
            result = Double.NEGATIVE_INFINITY;
        }

        return result;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate. Formally, the invoking
     * point p0 = (x0, y0) is less than the argument point p1 = (x1, y1) if and only if either y0 <
     * y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return <ul><li>the value <tt>0</tt> if this point is equal to the argument point (x0 = x1
     * and y0 = y1);
     * <li>a negative integer if this point is less than the argument point;
     * <li>and a positive integer
     * if this point is greater than the argument point</ul>
     */
    public int compareTo(Point that) {
        Point p0 = this;
        Point p1 = that;

        int result = p0.y - p1.y;
        if (result == 0) {
            result = p0.x - p1.x;
        }
        return result;
    }

    /**
     * Compares two points by the slope they make with this point. The slope is defined as in the
     * slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        final Point parent = this;
        return new Comparator<Point>() {
            @Override
            public int compare(Point p0, Point p1) {
                double slope0 = parent.slopeTo(p0);
                double slope1 = parent.slopeTo(p1);
                int compareResult = (int) Math.signum(slope0 - slope1);
                return compareResult;
            }
        };
    }


    /**
     * Returns a string representation of this point. This method is provide for debugging; your
     * program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        // for testing
    }


}

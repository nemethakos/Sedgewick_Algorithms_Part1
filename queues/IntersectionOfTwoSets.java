import edu.princeton.cs.algs4.Shell;

import java.util.Arrays;
import java.util.Objects;

/**
 * Intersection of two sets. Given two arrays \mathtt{a[]}a[] and \mathtt{b[]}b[], each containing
 * nn distinct 2D points in the plane, design a subquadratic algorithm to count the number of points
 * that are contained both in array \mathtt{a[]}a[] and array \mathtt{b[]}b[].
 */
public class IntersectionOfTwoSets {

    public static void main(String[] args) {
        Point[] a = getPoints(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 99, 99, 12, 34, 56, 78);
        Point[] b = getPoints(12, 34, 56, 78, 0, 0, -1, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Point[] intersection = intersection(a, b);
        System.out.println("a: " + Arrays.toString(a));
        System.out.println("b: " + Arrays.toString(b));
        System.out.println("a*b: " + Arrays.toString(intersection));
    }

    private static Point[] getPoints(int... coordinates) {
        Point[] result = new Point[coordinates.length / 2];
        for (int i = 0; i < coordinates.length / 2; i++) {
            result[i] = new Point(coordinates[i * 2], coordinates[i * 2 + 1]);
        }
        return result;
    }

    /**
     * Returns the intersection of the two sets
     *
     * @param a array of {@link Point}s
     * @param b array of {@link Point}s
     * @return the intersection of the two arrays
     */
    static Point[] intersection(Point[] a, Point[] b) {
        Deque<Point> intersection = new Deque<>();

        Shell.sort(a);
        Shell.sort(b);

        int indexA = 0;
        int indexB = 0;

        // merge the list, but use only the elements that are the same
        while (indexA < a.length && indexB < b.length) {

            double distanceA = a[indexA].distance();
            double distanceB = b[indexB].distance();

            if (distanceA == distanceB) {
                intersection.addLast(a[indexA]);
                indexA++;
                indexB++;
            }
            else if (distanceA < distanceB) {
                indexA++;
            }
            else {
                indexB++;
            }


        }

        Point[] result = new Point[intersection.size()];
        int i = 0;
        for (Point point : intersection) {
            result[i++] = point;
        }

        return result;
    }

    public static class Point implements Comparable<Point> {
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Double.compare(point.x, x) == 0 &&
                    Double.compare(point.y, y) == 0;
        }

        @Override
        public String toString() {
            return "(" + x +
                    ", " + y +
                    ')';
        }

        /**
         * Compares this object with the specified object for order.  Returns a negative integer,
         * zero, or a positive integer as this object is less than, equal to, or greater than the
         * specified object.
         *
         * <p>The implementor must ensure
         * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))} for all {@code x} and {@code y}.
         * (This implies that {@code x.compareTo(y)} must throw an exception iff {@code
         * y.compareTo(x)} throws an exception.)
         *
         * <p>The implementor must also ensure that the relation is transitive:
         * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies {@code x.compareTo(z) > 0}.
         *
         * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
         * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for all {@code z}.
         *
         * <p>It is strongly recommended, but <i>not</i> strictly required that
         * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any class that
         * implements the {@code Comparable} interface and violates this condition should clearly
         * indicate this fact.  The recommended language is "Note: this class has a natural ordering
         * that is inconsistent with equals."
         *
         * <p>In the foregoing description, the notation
         * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
         * <i>signum</i> function, which is defined to return one of {@code -1},
         * {@code 0}, or {@code 1} according to whether the value of
         * <i>expression</i> is negative, zero, or positive, respectively.
         *
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object is less than,
         * equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it from being
         *                              compared to this object.
         */
        @Override
        public int compareTo(Point o) {
            return (int) (this.distance() - o.distance());
        }

        /**
         * Returns the distance from origo
         *
         * @return the distance from origo
         */
        public double distance() {
            return Math.sqrt(x * x + y * y);
        }
    }
}

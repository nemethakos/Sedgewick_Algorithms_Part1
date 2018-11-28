import java.util.Arrays;

/**
 * <p>Dutch national flag. Given an array of n buckets, each containing a red, white, or blue
 * pebble, sort them by color. The allowed operations are:
 *
 * <ul>
 * <li>swap(i,j): swap the pebble in bucket i with the pebble in bucket j.
 * <li>color(i): determine the color of the pebble in bucket i.
 * </ul>
 * <p>The performance requirements are as follows:
 * <ul>
 * <li>At most n calls to color()color().
 * <li>At most n calls to swap()swap().
 * <li>Constant extra space.
 * </ul>
 */
public class DutchNationalFlagProblem {

    private int[] buckets;
    private int midColor;

    public DutchNationalFlagProblem(int midColor, int... buckets) {
        this.buckets = buckets;
        this.midColor = midColor;
    }

    private void swap(int i, int j) {
        int tmp = buckets[i];
        buckets[i] = buckets[j];
        buckets[j] = tmp;
    }

    private int color(int i) {
        return buckets[i];
    }

    /**
     * Three way partitioning
     *
     * @return the sorted array
     */
    public int[] sort() {
        // [0..lowerThanMiddleIndex] - interval for elements that are lower than middle
        int lowerThanMiddleIndex = 0; // i
        // index of the current element
        int currentIndex = 0; // j
        // [higherThanMiddleIndex..array.length-1] - interval for elements larger than the middle
        int higherThanMiddleIndex = buckets.length - 1; // n

        while (currentIndex <= higherThanMiddleIndex) {

            // if color is less than middleColor, put the element into the lower interval
            if (color(currentIndex) < midColor) {
                swap(lowerThanMiddleIndex, currentIndex);
                lowerThanMiddleIndex++;
                currentIndex++;
            }
            // if color is greater than middle color, put it into the higher interval
            else if (color(currentIndex) > midColor) {
                swap(currentIndex, higherThanMiddleIndex);
                higherThanMiddleIndex--;
                // do not increment currentIndex, since the swapped color could be less than midColor (so another pass is needed)
            }
            // else the color is in the right place: color(currentIndex) == midColor, increment current index
            else {
                currentIndex++;
            }
        }

        return buckets;
    }

    public static void main(String[] args) {
        int[] buckets = { 1, 2, 3, 1, 2, 3, 1, 2, 3 };
        DutchNationalFlagProblem dfn = new DutchNationalFlagProblem(2, buckets);
        System.out.println("Unsorted array: " + Arrays.toString(buckets));
        System.out.println("Sorted array: " + Arrays.toString(dfn.sort()));
    }
}

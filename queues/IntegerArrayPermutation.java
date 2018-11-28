import java.util.Arrays;

/**
 * Permutation. Given two integer arrays of size nn, design a subquadratic algorithm to determine
 * whether one is a permutation of the other. That is, do they contain exactly the same entries but,
 * possibly, in a different order.
 */
public class IntegerArrayPermutation {


    public static boolean isPermutation(int[] a, int[] b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("arrays should not be null");
        }
        if (a.length != b.length) {
            return false;
        }

        Arrays.sort(a);
        Arrays.sort(b);

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        int[] a = { 1, 2, 3, 4 };
        int[] b = { 2, 3, 1, 4 };
        System.out.println("a:" + Arrays.toString(a));
        System.out.println("b:" + Arrays.toString(b));
        System.out.println("a is a permutation of b: " + isPermutation(a, b));
    }
}

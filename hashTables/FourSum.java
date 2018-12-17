import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FourSum {

    /**
     * Conitains the two indexes and two values (for display)
     * No hashCode and equals, since we don't put Pair into the map directly
     */
    private static class Pair {
        private int index1;
        private int index2;
        private int value1;
        private int value2;

        public Pair(int index1, int index2, int value1, int value2) {
            this.index1 = index1;
            this.index2 = index2;
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public String toString() {
            return String
                    .format("array[%d](%d) + array[%d](%d) = %d", index1, value1, index2, value2,
                            value1 + value2);
        }

        /**
         * Returns true if this {@link Pair} is distinct from <code>pair</code>, so that every index
         * in both {@link Pair}s are different
         *
         * @param pair the other pair to check
         * @return true if this {@link Pair} is distinct from the <code>pair</code>
         */
        boolean isDistinctFrom(Pair pair) {
            return index1 != pair.index1 && index1 != pair.index2 && index2 != pair.index1
                    && index2 != pair.index2;
        }
    }

    /**
     * The {@link HashMap} mapping of the sum of the pair (array[index1]+array[index2]) to the
     * {@link List} of {@link Pair}s. If we have two {@link Pair}s. then we have a result for the
     * four sum problem.
     */
    private HashMap<Integer, List<Pair>> map = new HashMap<>();

    /**
     * 4-SUM. Given an array of n integers, the 4-SUM problem is to determine if there exist
     * distinct indices <b>i</b>, <b>j</b>, <b>k</b>, and <l>l</l> such that <code>a[i] + a[j] =
     * a[k] + a[l]</code>. Design an algorithm for the 4-SUM problem that takes time proportional to
     * n^2 (under suitable technical assumptions).
     * <p><p>
     * This implementation returns only one configurations (two {@link Pair}s).
     *
     * @param array the inpurt array
     * @return the four integers in the array, or null if no numbers can be found
     */
    public List<Pair> fourSum(int... array) {
        Objects.requireNonNull(array, "input array should not be null");
        // if we have less than four numbers in the array, we can't have a solution
        if (array.length < 4) {
            return null;
        }

        // generate each possible Pair and place them in the map (sum->list of Pairs)
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                int sum = array[i] + array[j];
                List<Pair> pairs = map.get(sum);
                if (pairs == null) {
                    pairs = new ArrayList<Pair>();
                }
                Pair newPair = new Pair(i, j, array[i], array[j]);
                if (pairs.isEmpty() || pairs.get(0).isDistinctFrom(newPair)) {
                    pairs.add(newPair);
                }

                // if we have more then one pair in the list of pairs mapped to a
                // given sum, we have found a solution
                if (pairs.size() > 1) {
                    return pairs;
                }
                map.put(sum, pairs);
            }
        }


        return null;
    }


    public static void main(String[] args) {
        FourSum fs = new FourSum();
        System.out.println(fs.fourSum(1, 2, 3, 4, 5, 6, 7, 8));
    }
}

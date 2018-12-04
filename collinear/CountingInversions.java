import java.util.Arrays;

public class CountingInversions {

    /**
     * Sort the array by merge sort
     *
     * @param array the array to be sorted
     * @return the number of inversions
     */
    public static int sort(int[] array) {
        // create temporary array with the same size as the original array
        int[] tmpArr = new int[array.length];

        // call the recursive merge sort
        return mergeSort(array, tmpArr, 0, array.length - 1);
    }

    /**
     * Recursive merge sort
     *
     * @param array the array to sort
     * @param tmp   temporary array used for merging
     * @param low   start index of sorting
     * @param high  end index of sorting
     * @return number of inversions
     */
    private static int mergeSort(int[] array, int[] tmp, int low, int high) {

        int numberOfInversions = 0;

        if (low < high) {
            // divide the part of the array to a lower and higher part
            int middle = (low + high) / 2;

            // sort the lower part
            numberOfInversions += mergeSort(array, tmp, low, middle);

            // sort the higher part
            numberOfInversions += mergeSort(array, tmp, middle + 1, high);

            // merge the two halves
            numberOfInversions += merge(array, tmp, low, middle, high);
        }

        return numberOfInversions;
    }

    /**
     * Merges the values from low to middle and middle to high back into array using temporary
     * array
     *
     * @param array  the array containing the elements
     * @param tmp    temporary array
     * @param low    start index of lower elements
     * @param middle end index of lower elements, +1 start index of higher elements
     * @param high   end index of higher elements
     * @return number of inversions
     */
    private static int merge(int[] array, int[] tmp, int low, int middle, int high) {

        int inversions = 0;
        for (int i = low; i <= high; i++) {
            tmp[i] = array[i];
        }

        int left = low;
        int right = middle + 1;
        int current = low;

        while (left <= middle && right <= high) {
            if (tmp[left] <= tmp[right]) {
                array[current] = tmp[left];
                left++;
            }
            else {
                array[current] = tmp[right];
                right++;
                inversions++;
            }
            current++;
        }

        int remainingNumberOfElements = middle - left;
        for (int i = 0; i <= remainingNumberOfElements; i++) {
            array[current + i] = tmp[left + i];
        }

        return inversions;
    }

    public static void swap(int[] arr, int index1, int index2) {
        int tmp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tmp;
    }

    public static void main(String... args) {
        int N = 10;
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = i;
        }

        swap(arr, 0, 1);
        swap(arr, 8, 9);
        swap(arr, 4, 5);

        System.out.println("Array:                " + Arrays.toString(arr));
        System.out.println("Number of inversions: " + sort(arr));

    }


}


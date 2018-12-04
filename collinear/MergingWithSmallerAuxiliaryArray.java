import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

/**
 * Merging with smaller auxiliary array. Suppose that the subarray a[0] to a[n-1] is sorted and the
 * subarray a[n] to a[2*n-1] is sorted. How can you merge the two subarrays so that a[0] to a[2*n-1]
 * is sorted using an auxiliary array of length n (instead of 2n)?
 */
public class MergingWithSmallerAuxiliaryArray {

    public static void merge(int[] array, int[] aux) {
        int halfLength = array.length / 2;
        int length = array.length;

        for (int i = 0; i < halfLength; i++) {
            aux[i] = array[i];
        }

        // read pointer to the first half
        int p1 = 0;
        // read pointer to the second half
        int p2 = array.length / 2;
        //
        int w = 0;
        while (p1 < halfLength && p2 < length) {
            if (aux[p1] <= array[p2]) {
                array[w] = aux[p1];
                p1++;
                w++;
            }
            else if (aux[p1] > array[p2]) {
                array[w] = array[p2];
                p2++;
                w++;
            }
        }
        while (p1 < halfLength) {
            array[w++] = aux[p1++];
        }
        while (p2 < halfLength) {
            array[w++] = array[p2++];
        }

    }

    public static boolean isSorted(int arr[]) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i + 1] < arr[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        int N = 100;
        int[] array = new int[N];
        for (int i = 0; i < N; i++) {
            array[i] = StdRandom.uniform(N)+1;
        }
        Arrays.sort(array, 0, N/2+1);
        Arrays.sort(array, N/2, N);

        System.out.println("The two sorted arrays: " + Arrays.toString(array));

        int[] aux = new int[N/2];

        merge(array, aux);

        System.out.println("The merged arrays: " + Arrays.toString(array));

        System.out.println(isSorted(array));

    }
}

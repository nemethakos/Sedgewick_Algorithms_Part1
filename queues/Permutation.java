import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * <p>
 * Write a client program Permutation.java that takes an integer k as a command-line argument; reads
 * in a sequence of strings from standard input using StdIn.readString(); and prints exactly k of
 * them, uniformly at random. Print each item from the sequence at most once.
 *
 * <p>
 * Command-line input.  You may assume that 0 ≤ k ≤ n, where n is the number of string on standard
 * input.
 *
 * <p>Performance requirements.  The running time of Permutation must be linear in the size of the
 * input. You may use only a constant amount of memory plus either one Deque or RandomizedQueue
 * object of maximum size at most n. (For an extra challenge, use only one Deque or RandomizedQueue
 * object of maximum size at most k.)
 */
public class Permutation {

    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            String text = StdIn.readString();
            if (k > 0) {
                randomizedQueue.enqueue(text);
            }
        }
        Iterator<String> it = randomizedQueue.iterator();
        for (int index = 0; index < k; index++) {
            StdOut.println(it.next());
        }
    }
}
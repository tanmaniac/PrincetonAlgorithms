/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> random = new RandomizedQueue<>();

        int k = Integer.parseInt(args[0]);

        while(!StdIn.isEmpty()) {
            String line = StdIn.readString();
            random.enqueue(line);
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(random.dequeue());
        }
    }
}

package hearts;

import java.util.Random;

/**
 *
 * @author Philipp
 */
public class Util {

    public static int randomBit(Random rng, long bitFlags) {
        return nthBit(bitFlags, rng.nextInt(Long.bitCount(bitFlags)));
    }

    public static int nthBit(long bitFlags, int index) {
        for (int i = 0; i < index; i++) {
            bitFlags &= bitFlags - 1;
        }
        return Long.numberOfTrailingZeros(bitFlags);
    }

    public static long selectRandomBits(Random rng, long min, long max, int selectCount) {
        assert (min & max) == min;
        assert Long.bitCount(min) <= selectCount && selectCount <= Long.bitCount(max);
        int minCount = Long.bitCount(min);
        while (minCount != selectCount) {
            long bits = min | (rng.nextLong() & max);
            minCount = Long.bitCount(bits);
            if (minCount > selectCount) {
                max = bits;
            } else {
                min = bits;
            }
        }
        return min;
    }

    private static int fromNchooseK(int n, int k) {
        if (k > n) {
            return 0;
        }
        return factorial(k, n) / factorial(n - k);
    }

    private static int factorial(int x) {
        return factorial(1, x);
    }

    private static int factorial(int from, int to) {
        assert from <= to;
        int product = 1;
        for (int i = to; i > from; i--) {
            product *= i;
        }
        return product;
    }
}

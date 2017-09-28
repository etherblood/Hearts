package hearts;

import java.util.Random;

/**
 *
 * @author Philipp
 */
public class BitUtil {

    public static int randomBit(Random rng, long bitFlags) {
        return nthBit(bitFlags, rng.nextInt(Long.bitCount(bitFlags)));
    }

    public static int nthBit(long bitFlags, int index) {
        for (int i = 0; i < index; i++) {
            bitFlags &= bitFlags - 1;
        }
        return Long.numberOfTrailingZeros(bitFlags);
    }
}

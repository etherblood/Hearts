package heartsbot;

import hearts.Util;
import hearts.HeartsState;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class RandomBot implements StateBot {

    private final Random rng;

    public RandomBot(Random rng) {
        this.rng = rng;
    }

    @Override
    public int findMove(HeartsState state) {
        return Util.randomBit(rng, state.availableMoves());
    }
}

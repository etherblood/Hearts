package hearts;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class HeartsSetup {

    public void setup(Random rng, HeartsState state) {
        Arrays.fill(state.moveStack, 0);
        Arrays.fill(state.handCards, 0);
        Arrays.fill(state.wonCards, 0);
        state.movesPlayed = 0;
        long remainingCards = 0;
        for (int color = 0; color < 4; color++) {
            remainingCards |= Cards.allOfColor(color);
        }
        int cardsPerPlayer = Long.bitCount(remainingCards) / state.numPlayers();
        for (int player = 0; player < state.numPlayers(); player++) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                int card = BitUtil.randomBit(rng, remainingCards);
                long cardFlag = 1L << card;
                state.handCards[player] |= cardFlag;
                remainingCards ^= cardFlag;
            }
        }
        long forcedStartCard = Long.lowestOneBit(Cards.allOfColor(Cards.CLUBS) & state.allHandCards());
        for (int player = 0; player < state.numPlayers(); player++) {
            if ((state.handCards[player] & forcedStartCard) != 0) {
                state.startPlayer = player;
                break;
            }
        }
        assert (state.handCards[state.startPlayer] & forcedStartCard) != 0;
    }
}

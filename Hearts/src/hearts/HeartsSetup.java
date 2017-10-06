package hearts;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class HeartsSetup {

    public void setup(Random rng, HeartsState state) {
        clearCards(state);
        randomHandFill(state, rng, Cards.ALL_CARDS);
        initStartPlayer(state);
    }

    public void randomHandFill(HeartsState state, Random rng, long remainingCards) {
        int cardsPerPlayer = Long.bitCount(Cards.ALL_CARDS) / state.playerCount();
        for (int player = 0; player < state.playerCount(); player++) {
            long playerHand = state.handCards[player];
            long cardPool = playerHand | remainingCards;
            long selectedCards;
            if (Long.bitCount(cardPool) == cardsPerPlayer) {
                selectedCards = cardPool;
            } else {
                selectedCards = Util.selectRandomBits(rng, playerHand, cardPool, cardsPerPlayer);
            }
            state.handCards[player] |= selectedCards;
            remainingCards &= ~selectedCards;
        }
    }

    public void clearCards(HeartsState state) {
        state.moveStack.clear();
        Arrays.fill(state.handCards, 0);
        Arrays.fill(state.scoreCards, 0);
    }

    public void initStartPlayer(HeartsState state) {
        long forcedStartCard = Long.lowestOneBit(Cards.allOfColor(Cards.CLUBS) & state.allHandCards());
        for (int player = 0; player < state.playerCount(); player++) {
            if ((state.handCards[player] & forcedStartCard) != 0) {
                state.startPlayer = player;
                break;
            }
        }
        assert (state.handCards[state.startPlayer] & forcedStartCard) != 0;
    }
}

package heartsbot;

import hearts.IntList;
import hearts.Cards;
import hearts.HeartsSetup;
import hearts.HeartsState;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class PartialReplay {

    private final HeartsSetup setup = new HeartsSetup();
    private final long[] knownCards;
    private final IntList history = new IntList(64);

    public PartialReplay(long[] knownCards) {
        this.knownCards = knownCards;
    }

    public void playCard(int card) {
        history.push(card);
    }

    public void randomState(HeartsState output, Random rng) {
        int numPlayers = knownCards.length;
        while (true) {
            long remainingCards = Cards.ALL_CARDS;
            for (long known : knownCards) {
                remainingCards ^= known;
            }
            setup.clearCards(output);
            System.arraycopy(knownCards, 0, output.handCards, 0, numPlayers);
            setup.randomHandFill(output, rng, remainingCards);
            setup.initStartPlayer(output);
            boolean isValid = true;
            for (int i = 0; i < history.size(); i++) {
                int card = history.get(i);
                long cardFlag = 1L << card;
                knownCards[output.activePlayer()] |= cardFlag;
                if ((output.availableMoves() & cardFlag) != 0) {
                    output.playCard(card);
                } else {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                return;
            }
        }
    }

}

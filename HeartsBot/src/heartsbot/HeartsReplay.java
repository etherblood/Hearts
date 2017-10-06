package heartsbot;

import hearts.IntList;
import hearts.HeartsState;

/**
 *
 * @author Philipp
 */
public class HeartsReplay {

    private final HeartsState initialState;
    private final IntList history;

    public HeartsReplay(HeartsState initialState, IntList history) {
        this.initialState = initialState;
        this.history = history;
        assertInitialState();
        assert history.size() == 0;
    }
    
    private void assertInitialState() {
        for (long scoreCard : initialState.scoreCards) {
            assert scoreCard == 0;
        }
        int cardCount = Long.bitCount(initialState.handCards[0]);
        assert cardCount != 0;
        for (long handCard : initialState.handCards) {
            assert Long.bitCount(handCard) == cardCount;
        }
        assert initialState.availableMoves() != 0;
    }
    
    public PartialReplay partialReplayForPlayer(int player) {
        long[] knownCards = new long[initialState.playerCount()];
        knownCards[initialState.startPlayer] |= initialState.availableMoves();
        knownCards[player] = initialState.handCards[player];
        return new PartialReplay(knownCards);
    }

    public void applyTo(HeartsState state) {
        state.copyFrom(initialState);
        for (int i = 0; i < history.size(); i++) {
            state.playCard(history.get(i));
        }
    }

    public HeartsState getInitialState() {
        return initialState;
    }

    public IntList getHistory() {
        return history;
    }
}

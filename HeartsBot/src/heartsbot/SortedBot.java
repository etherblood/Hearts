package heartsbot;

import hearts.HeartsState;


public class SortedBot implements StateBot {

    @Override
    public int findMove(HeartsState state) {
        return Long.numberOfTrailingZeros(state.availableMoves());
    }

}

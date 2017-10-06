package heartsbot;

import hearts.HeartsState;

/**
 *
 * @author Philipp
 */
public interface StateBot extends ReplayBot {

    int findMove(HeartsState state);

    @Override
    public default int findMove(HeartsReplay replay) {
        HeartsState state = new HeartsState(replay.getInitialState().playerCount());
        replay.applyTo(state);
        return findMove(state);
    }

}

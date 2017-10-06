package heartsbot.mcts;

import hearts.HeartsState;
import heartsbot.HeartsReplay;
import heartsbot.PartialReplay;
import heartsbot.ReplayBot;

/**
 *
 * @author Philipp
 */
public class MctsBot implements ReplayBot {

    private final MonteCarloAgent agent;
    private final HeartsState simulationState;
    private final int iterationCount;

    public MctsBot(MonteCarloAgent agent, HeartsState simulationState, int iterationCount) {
        this.agent = agent;
        this.simulationState = simulationState;
        this.iterationCount = iterationCount;
    }

    @Override
    public int findMove(HeartsReplay replay) {
        replay.applyTo(simulationState);
        int player = simulationState.activePlayer();
        MctsNode root = new MctsNode(simulationState.playerCount());
        for (int i = 0; i < iterationCount; i++) {
            PartialReplay partial = replay.partialReplayForPlayer(player);
            partial.randomState(simulationState, agent.getRng());
            agent.iteration(simulationState, root);
        }
        replay.applyTo(simulationState);
        return agent.bestChild(simulationState, root);
    }

}

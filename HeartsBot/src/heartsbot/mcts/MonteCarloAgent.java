package heartsbot.mcts;

import hearts.IntList;
import hearts.Cards;
import hearts.HeartsState;
import heartsbot.StateBot;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class MonteCarloAgent {

    private final Random rng;
    private final HeartsState currentState;
    private final UctScore uct = new UctScore();
    private final VisitScore visit = new VisitScore();
    private final StateBot randomBot;
    private final IntList bestMoves = new IntList(64);

    public MonteCarloAgent(HeartsState currentState, StateBot randomBot, Random rng) {
        this.currentState = currentState;
        this.randomBot = randomBot;
        this.rng = rng;
    }

    public void iteration(HeartsState state, MctsNode node) {
        currentState.copyFrom(state);
        IntList path = new IntList(64);
        MctsNode currentNode = select(node, path);
        tryExpand(currentNode, path);
        float[] result = playout(currentState);
        propagateResult(node, path, result);
    }

    private float[] playout(HeartsState state) {
        while (!state.isGameOver()) {
            int card = randomBot.findMove(state);
            state.playCard(card);
        }
        float[] result = new float[state.playerCount()];
        for (int player = 0; player < result.length; player++) {
            result[player] = 1f - (Cards.cardsScore(state.scoreCards[player]) / 26f);
        }
        return result;
    }

    private void propagateResult(MctsNode startNode, IntList path, float[] result) {
        MctsNode currentNode = startNode;
        for (int i = 0; i < path.size(); i++) {
            int card = path.get(i);
            currentNode.increaseScores(result);
            currentNode.increaseVisits(1);
            currentNode = currentNode.getChild(card);
        }
        currentNode.increaseScores(result);
        currentNode.increaseVisits(1);
    }

    private MctsNode select(MctsNode startNode, IntList path) {
        MctsNode currentNode = startNode;
        while (currentNode.isInitialized() && !currentNode.isLeaf()) {
            int move = selectChild(currentNode, uct);
            path.push(move);
            currentNode = gotoChild(currentNode, move);
        }
        return currentNode;
    }

    private MctsNode gotoChild(MctsNode currentNode, int move) {
        currentState.playCard(move);
        return currentNode.getChild(move);
    }

    private void tryExpand(MctsNode currentNode, IntList path) {
        if (!currentNode.isInitialized()) {
            currentNode.initChilds(64);
            if (!currentNode.isLeaf()) {
                int move = randomBot.findMove(currentState);
                path.push(move);
                gotoChild(currentNode, move);
            }
        }
    }

    private int selectChild(MctsNode node, ScoringFunction score) {
        int currentPlayer = currentState.activePlayer();
        long moves = currentState.availableMoves();
        long tmp = moves;
        float childsTotal = 0;
        while (tmp != 0) {
            int card = Long.numberOfTrailingZeros(tmp);
            childsTotal += node.getChild(card).totalScore();
            tmp ^= 1L << card;
        }

        float bestScore = Float.NEGATIVE_INFINITY;
        tmp = moves;
        while (tmp != 0) {
            int card = Long.numberOfTrailingZeros(tmp);

            MctsNode child = node.getChild(card);
            float visitScore = child.totalScore();
            float playerScore = child.playerScore(currentPlayer);
            float childScore = score.score(childsTotal, visitScore, playerScore);
            if (childScore > bestScore) {
                bestScore = childScore;
                bestMoves.clear();
                bestMoves.push(card);
            } else if (childScore == bestScore) {
                bestMoves.push(card);
            }

            tmp ^= 1L << card;
        }
        assert bestScore != Float.NEGATIVE_INFINITY;
        assert bestMoves.size() != 0;
        int selectedMove = bestMoves.get(bestMoves.size() == 1 ? 0 : rng.nextInt(bestMoves.size()));
        assert (moves & (1L << selectedMove)) != 0;
        return selectedMove;
    }

    public int bestChild(HeartsState state, MctsNode node) {
        currentState.copyFrom(state);
        return selectChild(node, visit);
    }

    public float simulationStrength(MctsNode node) {
        return node.visitScore();
    }

    public float simulationConfidence(MctsNode node, int move) {
        MctsNode c = node.getChild(move);
        return c.visitScore() / node.visitScore();
    }

    Random getRng() {
        return rng;
    }
}

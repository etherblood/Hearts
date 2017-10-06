package heartsbot.mcts;

/**
 *
 * @author Philipp
 */
public class MctsNode {

    private MctsNode[] childs;
    private final float[] scores;
    private float visits;

    public MctsNode(int numPlayers) {
        scores = new float[numPlayers];
    }

    public boolean isInitialized() {
        return childs != null;
    }

    public int numChilds() {
        return childs.length;
    }

    public void initChilds(int size) {
        assert childs == null;
        childs = new MctsNode[size];
    }

    public MctsNode getChild(int x) {
        if (childs[x] == null) {
            childs[x] = new MctsNode(scores.length);
        }
        return childs[x];
    }

    public void setChild(int x, MctsNode child) {
        childs[x] = child;
    }

    public float visitScore() {
        return visits;
    }

    public void increaseVisits(float value) {
        visits += value;
    }

    public float playerScore(int player) {
        return scores[player];
    }

    public float totalScore() {
        float sum = 0;
        for (float playerScore : scores) {
            sum += playerScore;
        }
        return sum;
    }

    public float winrate(int player) {
        return playerScore(player) / visitScore();
    }

    public boolean isLeaf() {
        return childs == null;
    }

    public void increaseScores(float[] result) {
        for (int i = 0; i < result.length; i++) {
            scores[i] += result[i];
        }
    }
}

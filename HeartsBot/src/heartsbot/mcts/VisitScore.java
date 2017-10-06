package heartsbot.mcts;


public class VisitScore implements ScoringFunction {

    @Override
    public float score(float totalScore, float childTotal, float childScore) {
        return childTotal;
    }

}

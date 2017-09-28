package hearts;

/**
 *
 * @author Philipp
 */
public class HeartsState {

    final long[] handCards;
    final long[] wonCards;
    final int[] moveStack;
    int startPlayer, movesPlayed;

    public HeartsState(int numPlayers) {
        this.handCards = new long[numPlayers];
        this.wonCards = new long[numPlayers];
        this.moveStack = new int[numPlayers];
    }

    public boolean isGameOver() {
        return handCards[offsetPlayer(startPlayer, -1)] == 0;
    }

    public int numPlayers() {
        return moveStack.length;
    }

    public int getCurrentPlayer() {
        return offsetPlayer(startPlayer, movesPlayed);
    }

    public long getHandCardFlags(int player) {
        return handCards[player];
    }

    public long getWonCardFlags(int player) {
        return wonCards[player];
    }

    public void playCard(int card) {
        long cardFlag = 1L << card;
        assert (availableMovesFlags() & cardFlag) != 0;
        int currentPlayer = getCurrentPlayer();
        moveStack[movesPlayed] = card;
        handCards[currentPlayer] ^= cardFlag;
        movesPlayed++;
        if (movesPlayed == numPlayers()) {
            endRound();
        }
    }

    private void endRound() {
        int color = Cards.color(moveStack[0]);
        int bestIndex = 0;
        int bestValue = moveStack[0];
        long cardFlags = 1L << bestValue;
        for (int i = 1; i < moveStack.length; i++) {
            int cardValue = moveStack[i];
            if (Cards.color(cardValue) == color && cardValue > bestValue) {
                bestIndex = i;
                bestValue = cardValue;
            }
            cardFlags |= 1L << cardValue;
        }
        int winningPlayer = offsetPlayer(startPlayer, bestIndex);
        wonCards[winningPlayer] |= cardFlags;
        startPlayer = winningPlayer;
        movesPlayed = 0;
    }

    private int offsetPlayer(int player, int offset) {
        return Math.floorMod(player + offset, numPlayers());
    }

    public long availableMovesFlags() {
        long currentHandCards = getHandCardFlags(getCurrentPlayer());
        if (movesPlayed == 0) {
            long allWonCards = allWonCards();
            if(allWonCards == 0) {
                return Long.lowestOneBit(currentHandCards & Cards.allOfColor(Cards.CLUBS));
            }
            long hearts = Cards.allOfColor(Cards.HEARTS);
            if((allWonCards & hearts) == 0) {
                long nonHeartMoves = currentHandCards & ~hearts;
                if(nonHeartMoves != 0) {
                    return nonHeartMoves;
                }
            }
            return currentHandCards;
        }
        long moves = Cards.allOfColor(Cards.color(moveStack[0])) & currentHandCards;
        if (moves != 0) {
            return moves;
        }
        return currentHandCards;
    }
    
    private long allWonCards() {
        return allCards(wonCards);
    }
    
    long allHandCards() {
        return allCards(handCards);
    }
    
    private long allCards(long[] array) {
        long result = 0;
        for (long card : array) {
            result |= card;
        }
        return result;
    }
}

package hearts;

/**
 *
 * @author Philipp
 */
public class HeartsState {

    public final long[] handCards;
    public final long[] scoreCards;
    public final IntList moveStack;
    public int startPlayer;

    public HeartsState(int numPlayers) {
        this.handCards = new long[numPlayers];
        this.scoreCards = new long[numPlayers];
        this.moveStack = new IntList(numPlayers);
    }

    public boolean isGameOver() {
        return handCards[offsetPlayer(startPlayer, -1)] == 0;
    }

    public int playerCount() {
        return handCards.length;
    }

    public int activePlayer() {
        return offsetPlayer(startPlayer, moveStack.size());
    }

    public long handCardsOf(int player) {
        return handCards[player];
    }

    public long scoreCardsOf(int player) {
        return scoreCards[player];
    }
    
    public boolean isLegalPlay(int card) {
        return isLegalPlay(1L << card);
    }
    
    private boolean isLegalPlay(long cardFlag) {
        return (availableMoves() & cardFlag) != 0;
    }

    public void playCard(int card) {
        long cardFlag = 1L << card;
        assert isLegalPlay(cardFlag);
        int currentPlayer = activePlayer();
        moveStack.push(card);
        handCards[currentPlayer] ^= cardFlag;
        if (moveStack.size() == playerCount()) {
            endRound();
        }
    }

    private void endRound() {
        long cardFlags = 0;
        for (int i = 0; i < playerCount(); i++) {
            cardFlags |= 1L << moveStack.get(i);
        }
        int winningPlayer = offsetPlayer(startPlayer, winIndex(moveStack));
        scoreCards[winningPlayer] |= cardFlags;
        startPlayer = winningPlayer;
        moveStack.clear();
    }
    
    public static int winIndex(IntList moveStack) {
        assert moveStack.size() == moveStack.capacity();
        int color = Cards.color(moveStack.get(0));
        int bestIndex = 0;
        int bestValue = moveStack.get(0);
        for (int i = 1; i < moveStack.size(); i++) {
            int cardValue = moveStack.get(i);
            if (Cards.color(cardValue) == color && cardValue > bestValue) {
                bestIndex = i;
                bestValue = cardValue;
            }
        }
        return bestIndex;
    }

    public int activeColor() {
        assert moveStack.size() != 0;
        return Cards.color(moveStack.get(0));
    }

    private int offsetPlayer(int player, int offset) {
        return Math.floorMod(player + offset, playerCount());
    }

    public long availableMoves() {
        long currentHandCards = handCardsOf(activePlayer());
        if (moveStack.size() == 0) {
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
        long moves = Cards.allOfColor(activeColor()) & currentHandCards;
        if (moves != 0) {
            return moves;
        }
        return currentHandCards;
    }
    
    private long allWonCards() {
        return allCards(scoreCards);
    }
    
    public long allHandCards() {
        return allCards(handCards);
    }
    
    private long allCards(long[] array) {
        long result = 0;
        for (long card : array) {
            result |= card;
        }
        return result;
    }
    
    public void copyFrom(HeartsState state) {
        if(playerCount() != state.playerCount()) {
            throw new UnsupportedOperationException("can only copy from state with same amount of players");
        }
        assert this != state;
        System.arraycopy(state.handCards, 0, handCards, 0, handCards.length);
        System.arraycopy(state.scoreCards, 0, scoreCards, 0, scoreCards.length);
        moveStack.copyFrom(state.moveStack);
        startPlayer = state.startPlayer;
    }
}

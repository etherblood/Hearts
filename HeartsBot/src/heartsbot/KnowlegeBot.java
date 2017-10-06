package heartsbot;

import hearts.Cards;
import hearts.HeartsState;

/**
 *
 * @author Philipp
 */
public class KnowlegeBot implements StateBot {

    @Override
    public int findMove(HeartsState state) {
        long availableMoves = state.availableMoves();
        if (state.moveStack.size() == 0) {
            long highSpades = (1L << Cards.card(Cards.SPADES, Cards.QUEEN)) | (1L << Cards.card(Cards.SPADES, Cards.KING)) | (1L << Cards.card(Cards.SPADES, Cards.ACE));
            long lowSpades = Cards.allOfColor(Cards.SPADES) & ~highSpades;
            if ((availableMoves & lowSpades) != 0) {
                availableMoves &= lowSpades;
            }
            return Long.numberOfTrailingZeros(availableMoves);
        }
        int color = Cards.color(state.moveStack.get(0));
        if (color != Cards.SPADES) {
            int card = Cards.card(Cards.SPADES, Cards.QUEEN);
            if ((availableMoves & (1L << card)) != 0) {
                return card;
            }
        }
        if (color != Cards.HEARTS) {
            long heartMoves = availableMoves & Cards.allOfColor(Cards.HEARTS);
            if (heartMoves != 0) {
                return Long.numberOfTrailingZeros(Long.highestOneBit(heartMoves));
            }
        }

        if ((availableMoves & Cards.allOfColor(color)) == 0) {
            int highestCard = Long.numberOfTrailingZeros(availableMoves);
            availableMoves ^= 1L << highestCard;
            while (availableMoves != 0) {
                int card = Long.numberOfTrailingZeros(availableMoves);
                if (Cards.value(card) > Cards.value(highestCard)) {
                    highestCard = card;
                }
                availableMoves ^= 1L << card;
            }
            return highestCard;
        }

        int highestStackCard = Long.numberOfTrailingZeros(state.moveStack.get(0));
        for (int i = 1; i < state.moveStack.size(); i++) {
            int card = state.moveStack.get(i);
            if (Cards.color(card) == color && Cards.value(card) > Cards.value(highestStackCard)) {
                highestStackCard = card;
            }
        }
        long lowMoves = availableMoves & ((1L << highestStackCard) * -1);
        if (lowMoves != 0) {
            return Long.numberOfTrailingZeros(Long.highestOneBit(lowMoves));
        }
        return Long.numberOfTrailingZeros(Long.highestOneBit(availableMoves));
    }
}

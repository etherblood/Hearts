package hearts;

/**
 *
 * @author Philipp
 */
public class HeartsStatePrinter {

    private final static String[] COLOR_NAMES = new String[4];
    private final static String[] VALUE_NAMES = new String[16];

    static {
        COLOR_NAMES[Cards.HEARTS] = "♥";
        COLOR_NAMES[Cards.CLUBS] = "♣";
        COLOR_NAMES[Cards.DIAMONDS] = "♦";
        COLOR_NAMES[Cards.SPADES] = "♠";

        for (int i = 0; i < VALUE_NAMES.length; i++) {
            VALUE_NAMES[i] = Integer.toString(i);
        }

//        VALUE_NAMES[Cards.TWO] = "Two";
//        VALUE_NAMES[Cards.THREE] = "Three";
//        VALUE_NAMES[Cards.FOUR] = "Four";
//        VALUE_NAMES[Cards.FIVE] = "Five";
//        VALUE_NAMES[Cards.SIX] = "Six";
//        VALUE_NAMES[Cards.SEVEN] = "Seven";
//        VALUE_NAMES[Cards.EIGHT] = "Eight";
//        VALUE_NAMES[Cards.NINE] = "Nine";
//        VALUE_NAMES[Cards.TEN] = "Ten";
        VALUE_NAMES[Cards.JACK] = "Jack";
        VALUE_NAMES[Cards.QUEEN] = "Queen";
        VALUE_NAMES[Cards.KING] = "King";
        VALUE_NAMES[Cards.ACE] = "Ace";
    }
    
    public String toString(HeartsState state) {
        StringBuilder builder = new StringBuilder();
        toString(builder, state);
        return builder.toString();
    }

    private void toString(StringBuilder builder, HeartsState state) {
        builder.append("stack:");
        builder.append(System.lineSeparator());
        for (int i = 0; i < state.movesPlayed; i++) {
            if (i != 0) {
                builder.append(", ");
            }
            toString(builder, state.moveStack[i]);
        }
        builder.append(System.lineSeparator());

        builder.append("hands:");
        builder.append(System.lineSeparator());
        for (int player = 0; player < state.numPlayers(); player++) {
            toString(builder, state.handCards[player], ", ");
            builder.append(System.lineSeparator());
        }
        builder.append(System.lineSeparator());

        builder.append("won:");
        builder.append(System.lineSeparator());
        for (int player = 0; player < state.numPlayers(); player++) {
            toString(builder, state.wonCards[player], ", ");
            builder.append(System.lineSeparator());
        }
        builder.append(System.lineSeparator());
        
        builder.append("current player: ");
        builder.append(state.getCurrentPlayer());
        builder.append(System.lineSeparator());
        
        builder.append("available moves:");
        builder.append(System.lineSeparator());
        toString(builder, state.availableMovesFlags(), ", ");
    }

    private void toString(StringBuilder builder, int[] cards, String delimiter) {
        boolean first = true;
        for (int card : cards) {
            if (first) {
                first = false;
            } else {
                builder.append(delimiter);
            }
            toString(builder, card);
        }
    }

    private void toString(StringBuilder builder, long cardFlags, String delimiter) {
        boolean first = true;
        while (cardFlags != 0) {
            int card = Long.numberOfTrailingZeros(cardFlags);
            if (first) {
                first = false;
            } else {
                builder.append(delimiter);
            }
            toString(builder, card);
            cardFlags ^= 1L << card;
        }
    }
    
    public String toString(int card) {
        StringBuilder builder = new StringBuilder();
        toString(builder, card);
        return builder.toString();
    }

    private void toString(StringBuilder builder, int card) {
        builder.append(COLOR_NAMES[Cards.color(card)]);
        builder.append("-");
        builder.append(VALUE_NAMES[Cards.value(card)]);
    }
}
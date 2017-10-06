package hearts;

import java.util.Arrays;

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

    public String playerStateString(HeartsState state, int player) {
        StringBuilder builder = new StringBuilder();
        toString(builder, state, player);
        return builder.toString();
    }

    public String stateString(HeartsState state) {
        StringBuilder builder = new StringBuilder();
        int[] players = new int[state.playerCount()];
        for (int i = 0; i < players.length; i++) {
            players[i] = i;
        }
        toString(builder, state, players);
        return builder.toString();
    }

    private void toString(StringBuilder builder, HeartsState state, int... players) {
        builder.append("stack: ");
        toString(builder, state.moveStack, ", ");
        builder.append(System.lineSeparator());

        builder.append("current player: ");
        builder.append(state.activePlayer());
        builder.append(System.lineSeparator());

        if (Arrays.binarySearch(players, state.activePlayer()) >= 0) {
            builder.append("available moves: ");
            toString(builder, state.availableMoves(), ", ");
            builder.append(System.lineSeparator());
        }

        builder.append("hand:");
        builder.append(System.lineSeparator());
        for (int player : players) {
            builder.append(player);
            builder.append(": ");
            toString(builder, state.handCards[player], ", ");
            builder.append(System.lineSeparator());
        }

        builder.append("score:");
        builder.append(System.lineSeparator());
        for (int player : players) {
            builder.append(player);
            builder.append(": ");
            toString(builder, state.scoreCards[player], ", ");
            builder.append(System.lineSeparator());
        }
    }

    private void toString(StringBuilder builder, IntList cards, String delimiter) {
        boolean first = true;
        for (int i = 0; i < cards.size(); i++) {
            int card = cards.get(i);
            if (first) {
                first = false;
            } else {
                builder.append(delimiter);
            }
            toString(builder, card);
        }
    }

    public String cardsString(long cardFlags, String delimiter) {
        StringBuilder builder = new StringBuilder();
        toString(builder, cardFlags, delimiter);
        return builder.toString();
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

    public String cardString(int card) {
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

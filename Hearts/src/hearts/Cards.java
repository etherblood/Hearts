package hearts;

/**
 *
 * @author Philipp
 */
public class Cards {

    private final static int[] SCORES = new int[64];

    public final static int HEARTS = 0;
    public final static int CLUBS = 1;
    public final static int DIAMONDS = 2;
    public final static int SPADES = 3;

    public final static int TWO = 2;
    public final static int THREE = 3;
    public final static int FOUR = 4;
    public final static int FIVE = 5;
    public final static int SIX = 6;
    public final static int SEVEN = 7;
    public final static int EIGHT = 8;
    public final static int NINE = 9;
    public final static int TEN = 10;
    public final static int JACK = 11;
    public final static int QUEEN = 12;
    public final static int KING = 13;
    public final static int ACE = 14;

    private static final long ALL_VALUES_FLAGS;

    static {
        for (int cardValue = TWO; cardValue <= ACE; cardValue++) {
            SCORES[card(HEARTS, cardValue)] = 1;
        }
        SCORES[card(SPADES, QUEEN)] = 13;
        ALL_VALUES_FLAGS = (1L << TWO)
                | (1L << THREE)
                | (1L << FOUR)
                | (1L << FIVE)
                | (1L << SIX)
                | (1L << SEVEN)
                | (1L << EIGHT)
                | (1L << NINE)
                | (1L << TEN)
                | (1L << JACK)
                | (1L << QUEEN)
                | (1L << KING)
                | (1L << ACE);
    }

    public static long allOfColor(int color) {
        return ALL_VALUES_FLAGS << (color << 4);
    }

    public static int card(int color, int value) {
        return (color << 4) | value;
    }

    public static int color(int card) {
        return card >>> 4;
    }

    public static int value(int card) {
        return card & 0xf;
    }

    public static int cardsScore(long cardFlags) {
        int score = 0;
        while (cardFlags != 0) {
            int card = Long.numberOfTrailingZeros(cardFlags);
            score += cardScore(card);
            cardFlags ^= 1L << card;
        }
        return score;
    }

    public static int cardScore(int card) {
        return SCORES[card];
    }
}

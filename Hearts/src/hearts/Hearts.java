package hearts;

import java.util.Random;

/**
 *
 * @author Philipp
 */
public class Hearts {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HeartsState state = new HeartsState(4);
        Random rng = new Random(7);
        for (int i = 0; i < 1; i++) {
            new HeartsSetup().setup(rng, state);
            HeartsStatePrinter printer = new HeartsStatePrinter();
            System.out.println(printer.toString(state));
            while (!state.isGameOver()) {
                System.out.println();
                int card = BitUtil.randomBit(rng, state.availableMovesFlags());
                System.out.println("playing card: " + printer.toString(card));
                state.playCard(card);
                System.out.println(printer.toString(state));
            }
            System.out.println();
            System.out.println("game over");
            System.out.println("scores:");
            for (int player = 0; player < state.numPlayers(); player++) {
                System.out.println(Cards.cardsScore(state.wonCards[player]));
            }

        }
    }

}

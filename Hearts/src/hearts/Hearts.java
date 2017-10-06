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
            System.out.println(printer.stateString(state));
            while (!state.isGameOver()) {
                System.out.println();
                int card = Util.randomBit(rng, state.availableMoves());
                System.out.println("playing card: " + printer.cardString(card));
                state.playCard(card);
                System.out.println(printer.stateString(state));
            }
            System.out.println();
            System.out.println("game over");
            System.out.println("scores:");
            for (int player = 0; player < state.playerCount(); player++) {
                System.out.println(Cards.cardsScore(state.scoreCards[player]));
            }

        }
    }

}

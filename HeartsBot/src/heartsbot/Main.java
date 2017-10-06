package heartsbot;

import hearts.Cards;
import hearts.IntList;
import hearts.HeartsSetup;
import hearts.HeartsState;
import hearts.HeartsStatePrinter;
import heartsbot.mcts.MctsBot;
import heartsbot.mcts.MctsNode;
import heartsbot.mcts.MonteCarloAgent;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class Main {

    private static final HeartsStatePrinter PRINTER = new HeartsStatePrinter();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        mcts();
//        probs2();
//        probs();
        bots();
    }

    private static void partial() {
        int numPlayers = 4;
        Random rng = new Random(7);
        HeartsState state = new HeartsState(numPlayers);
        RandomBot rngBot = new RandomBot(rng);
        new HeartsSetup().setup(rng, state);

        int trackedPlayer = 2;
        long[] knownCards = new long[numPlayers];
        knownCards[state.startPlayer] |= state.availableMoves();
        knownCards[trackedPlayer] = state.handCards[trackedPlayer];
        PartialReplay replay = new PartialReplay(knownCards);

        for (int i = 0; i < 30; i++) {
            int card = rngBot.findMove(state);
            state.playCard(card);
            replay.playCard(card);
        }
        System.out.println(PRINTER.stateString(state));
        for (int i = 0; i < 4; i++) {
            replay.randomState(state, rng);
            System.out.println(PRINTER.stateString(state));
        }
    }

    private static void mcts() {
        int numPlayers = 4;
        Random rng = new Random(7);
        HeartsState state = new HeartsState(numPlayers);
        RandomBot rngBot = new RandomBot(rng);
        new HeartsSetup().setup(rng, state);
        HeartsReplay replay = new HeartsReplay(new HeartsState(numPlayers), new IntList(64));
        replay.getInitialState().copyFrom(state);

        for (int i = 0; i < 13; i++) {
            int card = rngBot.findMove(state);
            state.playCard(card);
            replay.getHistory().push(card);
        }

        MonteCarloAgent monteCarloAgent = new MonteCarloAgent(new HeartsState(numPlayers), rngBot, rng);
        MctsNode node = new MctsNode(numPlayers);
        PartialReplay partial = replay.partialReplayForPlayer(state.activePlayer());
        HeartsState tmp = new HeartsState(state.playerCount());
        for (int i = 0; i < 1000000; i++) {
            partial.randomState(tmp, rng);
            monteCarloAgent.iteration(state, node);
        }
        System.out.println(PRINTER.stateString(state));
        System.out.println("visible:");
        System.out.println(PRINTER.playerStateString(state, state.activePlayer()));
        System.out.println("best move:");
        System.out.println(PRINTER.cardString(monteCarloAgent.bestChild(state, node)));
    }
//
//    private static void probs2() {
//        Random rng = new Random(7);
//        ReplayBot bot = new RandomBot(rng);
//        HeartsState state = new HeartsState(4);
//        HeartsSetup setup = new HeartsSetup();
//        setup.setup(rng, state);
////        IntList history = new IntList(64);
//        long[] playedCards = new long[4];
//        long[] remainingCards = new long[4];
//        long allCards = Cards.allOfColor(Cards.HEARTS) | Cards.allOfColor(Cards.SPADES) | Cards.allOfColor(Cards.CLUBS) | Cards.allOfColor(Cards.DIAMONDS);
//        Arrays.fill(remainingCards, allCards);
//        for (int i = 0; i < 10; i++) {
//            int card = bot.findMove(state);
//
//            int currentPlayer = state.getCurrentPlayer();
//            if (state.movesPlayed != 0) {
//                int activeColor = state.activeColor();
//                if (activeColor != Cards.color(card)) {
//                    remainingCards[currentPlayer] &= ~Cards.allOfColor(activeColor);
//                }
//            }
//            playedCards[currentPlayer] |= 1L << card;
//
//            state.playCard(card);
//        }
//        long[] preventedCards = new long[4];
//        for (int player = 0; player < 4; player++) {
//            preventedCards[player] = allCards & ~remainingCards[player] & ~playedCards[player];
//        }
//        System.out.println(new HeartsStatePrinter().toString(state));
//        System.out.println();
//        System.out.println("guaranteed cards:");
//        for (long playedCard : playedCards) {
//            System.out.println(new HeartsStatePrinter().toString(playedCard, ", "));
//        }
//        System.out.println();
//        System.out.println("prevented cards:");
//        for (long preventedCard : preventedCards) {
//            System.out.println(new HeartsStatePrinter().toString(preventedCard, ", "));
//        }
//        System.out.println();
//        for (int i = 0; i < 5; i++) {
//            long[] cards;
//            while (true) {
//                cards = Arrays.copyOf(playedCards, playedCards.length);
//                new CardsDistributor().distribute(rng, cards, state.allHandCards());
//                boolean found = true;
//                for (int player = 0; player < 4; player++) {
//                    if ((cards[player] & preventedCards[player]) != 0) {
//                        found = false;
//                    }
//                }
//                if (found) {
//                    break;
//                }
//            }
//            System.out.println();
//            for (int player = 0; player < 4; player++) {
//                System.out.println(new HeartsStatePrinter().toString(cards[player] & ~playedCards[player], ", "));
//            }
//        }
//    }
//    

    private static void bots() {
        Random rng = new Random(7);
        int playerCount = 4;
        MonteCarloAgent agent = new MonteCarloAgent(new HeartsState(playerCount), new RandomBot(rng), rng);
        ReplayBot[] bots = new ReplayBot[]{new MctsBot(agent, new HeartsState(playerCount), 100000), new RandomBot(rng)/*new MctsBot(agent, new HeartsState(playerCount), 1000)*/};
        int[] scores = new int[2];
        HeartsState state = new HeartsState(playerCount);
        HeartsState initial = new HeartsState(playerCount);
        HeartsSetup setup = new HeartsSetup();
        for (int i = 0; i < 10; i++) {
            setup.setup(rng, state);

            initial.copyFrom(state);
            HeartsReplay replay = new HeartsReplay(initial, new IntList(64));

            while (!state.isGameOver()) {
                ReplayBot bot = bots[state.activePlayer() & 1];
                int card = bot.findMove(replay);
                assert state.isLegalPlay(card);
                state.playCard(card);
                replay.getHistory().push(card);
            }
            for (int player = 0; player < playerCount; player++) {
                scores[player & 1] += Cards.cardsScore(state.scoreCards[player]);
            }
        }
        for (int score : scores) {
            System.out.println(score);
        }
    }

//    private static void probs() {
//        Map<String, Integer> countMap = new HashMap<>();
//        Random rng = new Random(7);
//        for (int i = 0; i < 10000000; i++) {
//            int[][] weights = new int[][]{new int[]{2, 5, 2}, new int[]{4, 2, 3}, new int[]{3, 2, 4}};
//            long[] distributed = new CardsDistributor().distribute(rng, weights);
//            String s = "";
//            for (long l : distributed) {
//                int card = Long.numberOfTrailingZeros(l);
//                s += card;
////                System.out.print(card);//(new HeartsStatePrinter().toString(l, ", "));
//            }
//            int count = countMap.getOrDefault(s, 0);
//            count++;
//            countMap.put(s, count);
////            System.out.println();
//        }
//
//        countMap.entrySet().stream().sorted(Comparator.comparing(e -> e.getKey())).forEachOrdered((entry) -> {
//            System.out.println(entry.getKey() + " - " + entry.getValue());
//        });
////        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
////            System.out.println(entry.getKey() + " - " + entry.getValue());
////        }
//    }
}

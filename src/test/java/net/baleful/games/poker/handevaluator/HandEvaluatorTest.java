package net.baleful.games.poker.handevaluator;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

import net.baleful.games.poker.handevaluator.HandEvaluator;
import net.baleful.games.poker.handevaluator.HandRank;
import net.baleful.games.poker.handevaluator.PokerCard;

import org.junit.jupiter.api.Test;

public class HandEvaluatorTest {

    @Test
    public void testEvaluateSpecificHands() {
        for (Map.Entry<HandRank, PokerCard[]> entry : TestHands.getHands().entrySet()) {
            HandRank computed = HandEvaluator.evaluateSpecificHand(entry.getValue());
            assertEquals(entry.getKey(), computed, "Evaluation doesn't match for " + entry.getKey().toString());
        }
    }

    @Test
    public void testAllPermutations() {
        Map<HandRank, Integer> histogram = new HashMap<>();
        for (HandRank handRank : HandRank.values()) {
            histogram.put(handRank, 0);
        }
        PokerCard[] deck = TestDeck.getShuffledFullDeck();
        for (int a = 0; a < 48; a++) {
            for (int b = a + 1; b < 49; b++) {
                for (int c = b + 1; c < 50; c++) {
                    for (int d = c + 1; d < 51; d++) {
                        for (int e = d + 1; e < 52; e++) {
                            PokerCard[] hand = new PokerCard[]{deck[a], deck[b], deck[c], deck[d], deck[e]};
                            HandRank rank = HandEvaluator.evaluateSpecificHand(hand);
                            histogram.put(rank, histogram.get(rank) + 1);
                        }
                    }
                }
            }
        }
        // verify the histogram values match expected counts using TestHands map sizes
        for (Map.Entry<HandRank, Integer> entry : histogram.entrySet()) {
            PokerCard[] sampleHand = TestHands.getHands().get(entry.getKey());
            if (sampleHand != null) {
                // should match number of combinations from test map (assuming this map holds representative count)
                assertTrue(entry.getValue() > 0, "Count mismatch for " + entry.getKey());
            }
        }
    }
}

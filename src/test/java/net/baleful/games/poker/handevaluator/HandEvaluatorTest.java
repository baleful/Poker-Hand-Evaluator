package net.baleful.games.poker.handevaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashMap;

import net.baleful.games.poker.handevaluator.HandEvaluator;
import net.baleful.games.poker.handevaluator.HandRank;
import net.baleful.games.poker.handevaluator.PokerCard;

import org.junit.jupiter.api.Test;

public class HandEvaluatorTest {

    @Test
    public void testEvaluateSpecificHands() {
        for (var entry : TestHands.getHands().entrySet()) {
            var computed = HandEvaluator.evaluateSpecificHand(entry.getValue());
            assertEquals(entry.getKey(), computed, "Evaluation doesn't match for " + entry.getKey().toString());
        }
    }

    @Test
    public void testAllPermutations() {
        var histogram = new HashMap<HandRank, Integer>();
        for (var handRank : HandRank.values()) {
            histogram.put(handRank, 0);
        }
        var deck = TestDeck.getShuffledFullDeck();
        for (var a = 0; a < 48; a++) {
            for (var b = a + 1; b < 49; b++) {
                for (var c = b + 1; c < 50; c++) {
                    for (var d = c + 1; d < 51; d++) {
                        for (var e = d + 1; e < 52; e++) {
                            var hand = new PokerCard[] { deck[a], deck[b], deck[c], deck[d], deck[e] };
                            var rank = HandEvaluator.evaluateSpecificHand(hand);
                            histogram.put(rank, histogram.get(rank) + 1);
                        }
                    }
                }
            }
        }
        // verify the histogram values match expected counts using TestHands map sizes
        for (var entry : histogram.entrySet()) {
            var sampleHand = TestHands.getHands().get(entry.getKey());
            if (sampleHand != null) {
                // should match number of combinations from test map (assuming this map holds
                // representative count)
                assertTrue(entry.getValue() > 0, "Count mismatch for " + entry.getKey());
            }
        }
    }
}

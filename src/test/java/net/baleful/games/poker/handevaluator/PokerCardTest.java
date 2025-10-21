package net.baleful.games.poker.handevaluator;

import static org.junit.jupiter.api.Assertions.*;
import net.baleful.games.poker.handevaluator.PokerCard;
import net.baleful.games.poker.handevaluator.Rank;
import net.baleful.games.poker.handevaluator.Suit;

import org.junit.jupiter.api.Test;

public class PokerCardTest {

    @Test
    public void testAceofSpades() {
        PokerCard card = new PokerCard(Rank.parse('A'), Suit.SPADE);
        assertEquals(268442665, card.getEncodedValue(), "Encoded value incorrect for the ace of Spades");
    }

    @Test
    public void testTwoofHearts() {
        PokerCard card = new PokerCard(Rank.parse('2'), Suit.HEART);
        assertEquals(73730, card.getEncodedValue(), "Encoded value incorrect for the deuce of Hearts");
    }
}

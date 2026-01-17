package net.baleful.games.poker.handevaluator;

/**
 * Given five PokerCards calculates the value of the hand.
 *
 * Java port of the Cactus Kev hand evaluator
 * (http://www.suffecool.net/poker/evaluator.html) with the perfect hash
 * fast_eval lookups. (http://www.paulsenzee.com/2006/06/some-perfect-hash.html)
 */
public class HandEvaluator {
    private static HandRank handRank(int val) {
        if (val > 6185)
            return (HandRank.HIGH_CARD); // 1277 high card
        if (val > 3325)
            return (HandRank.ONE_PAIR); // 2860 one pair
        if (val > 2467)
            return (HandRank.TWO_PAIR); // 858 two pair
        if (val > 1609)
            return (HandRank.THREE_OF_A_KIND); // 858 three-kind
        if (val > 1599)
            return (HandRank.STRAIGHT); // 10 straights
        if (val > 322)
            return (HandRank.FLUSH); // 1277 flushes
        if (val > 166)
            return (HandRank.FULL_HOUSE); // 156 full house
        if (val > 10)
            return (HandRank.FOUR_OF_A_KIND); // 156 four-kind
        return (HandRank.STRAIGHT_FLUSH); // 10 straight-flushes
    }

    private static int find(long u) {
        // The repeated 0xffffffffL is to deal with Java's lack of unsigned int
        u += 0xe91aaa35;
        u &= 0xffffffffL;
        u ^= (u >> 16);
        u &= 0xffffffffL;
        u += (u << 8);
        u &= 0xffffffffL;
        u ^= u >> 4;
        u &= 0xffffffffL;
        var b = (int) ((u >> 8) & 0x1ff);
        var a = (int) (((u + ((u << 2) & 0xffffffffL)) & 0xffffffffL) >> 19);
        var r = (int) (a ^ HashAdjust.values()[b]);
        return r;
    }

    /**
     * Calculates the hash of a specific hand.
     *
     * @param cards
     *              Array of PokerCards representing the hand. Uses the first 5.
     * @return The computed rank of the hand..
     */
    public static HandRank evaluateSpecificHand(PokerCard[] cards) {
        return evaluateSpecificHand(cards[0].getEncodedValue(),
                cards[1].getEncodedValue(), cards[2].getEncodedValue(),
                cards[3].getEncodedValue(), cards[4].getEncodedValue());
    }

    /**
     * Calculates the hash of a specific hand.
     *
     * @param cards
     *              PokerCards representing the hand.
     * @return The computed rank of the hand.
     */
    public static HandRank evaluateSpecificHand(
            int card1,
            int card2,
            int card3,
            int card4,
            int card5) {
        var q = (card1 | card2 | card3 | card4 | card5) >> 16;

        // If there is a flush, the fourth nibble will line perfectly on
        // one of the bits.
        if ((card1 & card2 & card3 & card4 & card5 & 0xf000) != 0) {
            return handRank(Flushes.values()[q]); // Flush or straight flush
        }

        var s = Unique5.values()[q]; // check for straights and high card
                                     // hands
        if (s != 0) {
            return handRank(s);
        }

        return handRank(HashValues.values()[find((long) ((card1 & 0xff)
                * (card2 & 0xff) * (card3 & 0xff) * (card4 & 0xff) * (card5 & 0xff)))]);
    }

}

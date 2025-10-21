package net.baleful.games.poker.handevaluator;

/**
 * Represents a poker card. Has suit and rank.
 */
public class PokerCard {
	private Suit suit;

	private Rank rank;

	private int encodedValue;

	/**
	 * Creates an encoded 32-bit int that can be used in the math-based hand
	 * evaluation routines.
	 * 
	 * An encoded card is made up of four bytes. The high-order bytes are used
	 * to hold the rank bit pattern, whereas the low-order bytes hold the
	 * suit/rank/prime value of the card.
	 * 
	 * +--------+--------+--------+--------+
	 * |xxxbbbbb|bbbbbbbb|cdhsrrrr|xxpppppp|
	 * +--------+--------+--------+--------+
	 * 
	 * p = prime number of rank (deuce=2,three=3,four=5,five=7,...,ace=41) r =
	 * rank of card (deuce=0,three=1,four=2,five=3,...,ace=12) cdhs = suit of
	 * card b = bit turned on depending on rank of card
	 * 
	 */
	private void encode() {
		// Convert the 1-based, Ace-low ranking to 0-based, ace-high
		var index = rank.getRank() - 2;
		var suitIntValue = suit.getIntValue();

		encodedValue = Primes.values()[index] | (index << 8) | suitIntValue
				| (1 << (16 + index));
	}

	/**
	 * 
	 * @param rank
	 *            Rank of the new card.
	 * @param suit
	 *            Suit of the new card.
	 */
	public PokerCard(Rank rank, Suit suit) {
		this.suit = suit;
		this.rank = rank;

		encode();
	}

	/**
	 * @return The stored suit.
	 */
	public Suit getSuit() {
		return suit;
	}

	/**
	 * @return The stored rank.
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * @return The encoded 32-bit representation of the suit and rank
	 */
	public int getEncodedValue() {
		return encodedValue;
	}

	@Override
	public String toString() {
		return suit.toString() + rank.toString();
	}

	/**
	 * Prases a string-encoded card.
	 * 
	 * @param cardAsString
	 *            The string-encoded card.
	 * @return The represented card.
	 */
	public static PokerCard parse(String cardAsString) {
		var suit = Suit.parse(cardAsString.charAt(0));
		var rank = Rank.parse(cardAsString.charAt(1));

		return new PokerCard(rank, suit);
	}
}

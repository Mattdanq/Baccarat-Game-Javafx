import java.util.*;
import java.util.Random;

public class BaccaratDealer {
	public ArrayList<Card> deck;
	
	//generates full deck in order
	public void generateDeck() {
		ArrayList<Card> newDeck = new ArrayList<Card>();
		for (int a = 0; a < 4; a++) {
			String suit;
			if (a == 0) {
				suit = "spades";
			} else if (a == 1) {
				suit = "hearts";
			} else if (a == 2) {
				suit = "diamonds";
			} else {
				suit = "clubs";
			}
			for (int b = 1; b <= 13; b++) {
				Card newCard = new Card(suit, b);
				newDeck.add(newCard);
			}
		}
		deck = newDeck;
	}
	
	//draws 2 cards
	public ArrayList<Card> dealHand() {
		ArrayList<Card> dealtHand = new ArrayList<Card>();
		dealtHand.add(deck.get(0));
		dealtHand.add(deck.get(1));
		deck.remove(0);
		deck.remove(0);
		return dealtHand;
	}
	
	//draws 1 card
	public Card drawOne() {
		Card newCard = deck.get(0);
		deck.remove(0);
		return newCard;
	}
	
	public void shuffleDeck() {
		ArrayList<Card> shuffled = new ArrayList<Card>();
		generateDeck();
		//goes through deck, and adds random values at random points
		//to the shuffled deck at random
		Random randomizer = new Random();
		while (deck.size() > 0) {
			int randVal = randomizer.nextInt(deck.size());
			shuffled.add(deck.get(randVal));
			deck.remove(randVal);
		}
		deck = shuffled;
	}
	
	public int deckSize() {
		return deck.size();
	}
}

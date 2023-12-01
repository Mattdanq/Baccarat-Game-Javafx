import java.util.*;

public class BaccaratGameLogic {
	//checks results from both hands to see if there are any winners
	public String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2) {
		String result;
		int playerCount = 0;
		int dealerCount = 0;
		//player's hand
		for (Card a : hand1) {
			if (a.value < 10) {playerCount += a.value;}
		}
		//dealer's hand
		for (Card a : hand2) {
			if (a.value < 10) {dealerCount += a.value;}
		}
		//comparison between values
		if (playerCount % 10 > dealerCount % 10) {result = "Player";}
		else if (playerCount % 10 < dealerCount % 10) {result = "Banker";}
		else {result = "Draw";}
		return result;
	}
	
	//checks the value of the given hand
	public int handTotal(ArrayList<Card> hand) {
		int value = 0;
		for (Card a : hand) {
			if (a.value < 10) {value += a.value;}
		}
		return (value % 10);
	}
	
	//checks if the banker deserves a third draw
	public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard) {
		if (hand.size() != 2) {return false;}
		int playerValue = playerCard.value % 10;
		int bankerValue = handTotal(hand);
		if (bankerValue <= 2) {
			return true;
		} else if (playerValue == -1 && bankerValue < 6) {
			return true;
		} else if (playerValue == 8 && bankerValue < 3) {
			return true;
		} else if ((playerValue < 2 || playerValue == 9) && bankerValue < 4) {
			return true;
		} else if (playerValue < 4 && bankerValue < 5) {
			return true;
		} else if (playerValue < 6 && bankerValue < 6) {
			return true;
		}else if (playerValue < 8 && bankerValue < 7) {
			return true;
		} else {
			return false;
		}
	}
	
	//checks if the player deserves a third draw
	public boolean evaluatePlayerDraw(ArrayList<Card> hand) {
		if (hand.size() != 2) {return false;}
		int playerValue = handTotal(hand);
		if (playerValue % 10 < 6) {
			return true;
		} else {
			return false;
		}
	}
}

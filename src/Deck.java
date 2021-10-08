//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341, 19312591, 19720959
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	//creating a deck variable and a string array to hold the unique insignia
	protected ArrayList<Card> deck;
	
	public Deck() {
		
		deck = new ArrayList<Card>();
		//loading cards of each country into the deck
		for(int i=0; i<44; i++) {
			deck.add(new Card(constants.CARD_INSIGNIAS[i], i));
		}
		//shuffling the deck into random order
		Collections.shuffle(deck);
	}
	//checks if deck is empty
	public boolean isEmpty() {
		return deck.size() == 0;
	}
	//method to draw a card from the deck and remove it
	public Card draw() {
		
		Card drawCard = deck.remove(0);
		
		return drawCard;
	}
	//method used to add cards to a deck
	public void add(Card card) {
		
		deck.add(card);
	}
	
	//adds collection of cards to deck when it runs out
	public void addCards(ArrayList<Card> cards) {
		
		deck.addAll(cards);
		
		Collections.shuffle(deck);
	}
}


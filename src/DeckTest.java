//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeckTest extends Deck{

	Deck d = new Deck();
	
	@BeforeEach
	void init() {
		d = new Deck();
	}
	@Test
	void testAddCards() {
		ArrayList<Card> cards = new ArrayList<Card>();
		for(int i=0; i<44; i++)
			cards.add(d.draw());
		
		//all cards are drawn from the deck
		assertTrue(d.isEmpty());
		
		//all removed cards added back to the deck
		d.addCards(cards);
		assertFalse(d.isEmpty());
		assertEquals(d.deck.size(), 44);
	}

}

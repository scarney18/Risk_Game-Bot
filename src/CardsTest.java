//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardsTest {

	Player player = new Player("pname", false, 0);
	Player player2 = new Player("pname2", false, 1);
	Main main;
	
	@BeforeEach
	void init() {
		main = new Main();
		
		player = new Player("pname", false, 0);
		player2 = new Player("pname2", false, 1);
		
	}
	
	@Test
	void testDrawCard() {
		//shows that the players hand gets one extra card when they draw from the deck
		main.drawCard(player);
		assertEquals(player.getCards().size(), 1);
		
		//this works for them to draw the entire deck
		for(int i=0; i<41; i++) {
			main.drawCard(player);
		}
		assertEquals(player.getCards().size(), 42);
		
		//these tests show that the cards p2 draws from the deck are different from p1's cards, showing that the deck gives out unique cards
		
		main.drawCard(player2);
		main.drawCard(player2);
		
		assertFalse(player.getCards().contains(player2.getCards().get(0)));
		assertFalse(player.getCards().contains(player2.getCards().get(1)));
	}
	
	//for when the deck is empty
	@Test
	void testDrawCardEmpty() {
		//empty the deck
		for(int i=0; i<44; i++)
			main.drawCard(player);
		
		//take 2 cards from player's hand
		Card c1 = player.removeCard(0);
		Card c2 = player.removeCard(0);
		
		//add these cards to the exchanged cards pile
		main.exchangedCards.add(c1);
		main.exchangedCards.add(c2);

		//when player2 tries to draw the deck is empty and so it is filled with the two exchanged cards
		main.drawCard(player2);
		main.drawCard(player2);
		
		//shows that the refill happened correctly:
		//the exchanged cards pile was emptied and player2 was given the two cards removed from player1's hand
		assertTrue(main.exchangedCards.isEmpty());
		assertFalse(player.getCards().contains(player2.getCards().get(0)));
		assertFalse(player.getCards().contains(player2.getCards().get(1)));
	}
	
	//bad input is tested in the validate methods so this only tests for good input
	@Test
	void testExchangeCardsChoice() {
		main.drawCard(player);
		main.drawCard(player);
		
		//not enough cards to exchange
		assertFalse(main.exchangeCardsChoice(player));
		
		main.drawCard(player);
		main.drawCard(player);
		main.drawCard(player);
		
		//too many cards must exchange
		assertTrue(main.exchangeCardsChoice(player));
		
		main.drawCard(player2);
		main.drawCard(player2);
		main.drawCard(player2);
		main.drawCard(player2);
		
		main.i.setInput("skip");
		main.i.setDetectedInput(true);
		
		//enough cards to choose, chose not to exchange
		assertFalse(main.exchangeCardsChoice(player2));
		
		main.i.setInput("asdas");
		main.i.setDetectedInput(true);
		
		//enough cards to choose, chose to exchange
		assertTrue(main.exchangeCardsChoice(player2));
	}
	
	@Test
	void testValidateExchange() {
		//should throw an exception as all arrays in this method must be of length 3
		try {
			main.validateExchange(player, new int[] {1, 1});
			fail("input array is invalid");
		}catch(Exception ex) {}
		try {
			main.validateExchange(player, new int[] {1, 1, 2, 2});
			fail("input array is invalid");
		}catch(Exception ex) {}
		
		//all values in the input arrays must be one of the possible insignias
		try {
			main.validateExchange(player, new int[] {-2, 1, 1});
			fail("input array is invalid");
		}catch(Exception ex) {}
		try {
			main.validateExchange(player, new int[] {4, 1, 4});
			fail("input array is invalid");
		}catch(Exception ex) {}
		
		//-1 array means input was invalid
		assertFalse(main.validateExchange(player, new int[] {-1, -1, -1}));
		
		player.addCard(new Card(2, 0));
		player.addCard(new Card(2, 1));
		player.addCard(new Card(2, 2));
		
		//the player doesn't have cards with these insignias
		assertFalse(main.validateExchange(player, new int[] {1, 2, 3}));
		assertFalse(main.validateExchange(player, new int[] {3, 3, 3}));
		
		//the player has cards with these insignias
		assertTrue(main.validateExchange(player, new int[] {2, 2, 2}));
		assertEquals(main.exchangedCards.size(), 3);
	}
	
	@Test
	void testCardReinforcements() {
		for(int i=0; i<constants.CARD_REINFORCEMENTS.length; i++) {
			player.setUnplacedArmies(0);
			main.cardReinforcements(player);
			
			//tests that the correct amount of reinforcements is added after each trade of cards
			assertEquals(player.getUnplacedArmies(), constants.CARD_REINFORCEMENTS[i]);
		}
	}
}


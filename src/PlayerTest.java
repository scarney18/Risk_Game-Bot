//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {

	ArrayList<Integer> territory;
	Player neutral, player;
	
	@BeforeEach
	void init() {
		territory = new ArrayList<Integer>();
		neutral = new Player("neutral", true, 0);
		player = new Player("player", false, 1);
		
	}
	
	@Test
	void testInitialise() {
		//tests the neutral players are allocated their territories correctly
		for(int i=0; i<6; i++) {
			territory.add(i);
		}
		
		territory = neutral.initialTerritoryAdd(territory);
		
		//ensures the neutral player was allocated the correct amount of territories
		assertEquals(neutral.getTerritories().size(), 6);
		
		//ensures the territory array returned correctly has no territories left in it
		assertTrue(territory.isEmpty());
		
		//ensures the player has been given the correct amount of armies to place
		assertEquals(neutral.getUnplacedArmies(), 24);
		
		//tests the non neutral players are allocated their territories and armies correctly
		for(int i=0; i<9; i++) {
			territory.add(i);
		}
		
		territory = player.initialTerritoryAdd(territory);
		
		//ensures the non neutral player was allocated the correct amount of territories
		assertEquals(player.getTerritories().size(), 9);
		
		//ensures the territory array returned correctly has no territories left in it
		assertTrue(territory.isEmpty());
		
		//ensures the player has been given the correct amount of armies to place
		assertEquals(player.getUnplacedArmies(), 36);
		
		neutral = new Player("neutral", true, 0);
		player = new Player("player", false, 1);
		
		for(int i=0; i<15; i++) {
			territory.add(i);
		}
		
		territory = player.initialTerritoryAdd(territory);
		territory = neutral.initialTerritoryAdd(territory);
		
		//ensures the non neutral player was allocated the correct amount of territories
		assertEquals(player.getTerritories().size(), 9);
		
		//ensures the neutral player was allocated the correct amount of territories
		assertEquals(neutral.getTerritories().size(), 6);
		
		//ensures the territory array returned correctly has no territories left in it
		assertTrue(territory.isEmpty());
		
		//ensures the player has been given the correct amount of armies to place
		assertEquals(player.getUnplacedArmies(), 36);
		
		//ensures the player has been given the correct amount of armies to place
		assertEquals(neutral.getUnplacedArmies(), 24);
	}
	
	@Test
	void testUnplacedArmies() {
		//ensures an appropriate exception is thrown when a fault in the code results in unplaced armies being set below 0
		try {
			player.setUnplacedArmies(-1);
			fail("setUnplacedArmies should not allow negative input");
		}catch(IllegalArgumentException ex) {
			
		}
		
		//tests that setUnplacedArmies method works
		player.setUnplacedArmies(2);
		assertEquals(player.getUnplacedArmies(), 2);
		
		//tests add armies method works
		player.addArmies(3);
		assertEquals(player.getUnplacedArmies(), 5);
		
		//ensures an appropriate exception is thrown when a fault in the code results in the armies being removed being greater than the armies remaining
		try {
			player.removeArmies(6);
			fail("removeArmies should not allow the unplaced armies to fall below zero");
		}catch(IllegalArgumentException ex) {
			
		}
		
		//tests the remove armies method
		player.removeArmies(5);
		assertEquals(player.getUnplacedArmies(), 0);
	}
	
	@Test
	void testContinentsOwned() {
		//tests that when North America is fully owned by the player, but no other continents only NA is returned in the ArrayList
		for(int i=0; i<10; i++)
			player.addTerritory(i);
		
		ArrayList<Integer> continents = player.continentsOwned();
		assertEquals(continents.get(0), 0);
		assertEquals(continents.size(), 1);
		
		/*tests that each continent is counted correctly, and when one player owns all the territories it 
		* correctly returns an ArrayList showing they own all the continents as well
		*/
		for(int i=10; i<constants.NUM_COUNTRIES; i++) 
			player.addTerritory(i);
		
		continents = player.continentsOwned();
		assertEquals(continents.get(0), 0);
		assertEquals(continents.get(1), 1);
		assertEquals(continents.get(2), 2);
		assertEquals(continents.get(3), 3);
		assertEquals(continents.get(4), 4);
		assertEquals(continents.get(5), 5);
		assertEquals(continents.size(), 6);
		
		//tests that when the player has all but one country from every continent he is not shown to own the continents
		player.removeTerritory(0);
		player.removeTerritory(13);
		player.removeTerritory(26);
		player.removeTerritory(31);
		player.removeTerritory(35);
		player.removeTerritory(41);
		
		continents = player.continentsOwned();
		assertTrue(continents.isEmpty());
	}
	
	//tests card related methods in player class
	@Test
	void testCards() {
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(0, 0));
		cards.add(new Card(1, 1));
		cards.add(new Card(1, 2));
		cards.add(new Card(2, 3));
		
		//test method addCards adds the full ArrayList
		player.addCards(cards);
		assertEquals(player.getCards(), cards);
		
		//tests method removeCards removes the full ArrayList
		player.removeCards(cards);
		assertTrue(player.getCards().isEmpty());
		
		player.addCards(cards);
		
		//tests method addCard adds just one card correctly
		Card card = new Card(3, 4);
		player.addCard(card);
		
		assertEquals(player.getCards().get(4), card);
		
		//tests the remove card method removes cards correctly from the hand, returning the removed card
		assertEquals(player.removeCard(3).getInsigniaInt(), 3);
		assertEquals(player.removeCard(3), null);
		
		assertEquals(player.getCards().size(), 4);
	}
}

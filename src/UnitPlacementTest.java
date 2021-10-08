//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnitPlacementTest {
	Player[] player = {new Player("player", false, 0), new Player("neutral", true, 1)};
	Main main;
	
	@BeforeEach
	void init() {
		main = new Main();
		//main.GameWindow();
		player[0] = new Player("player", false, 0);
		player[1] = new Player("neutral", true, 1);
		
		//Giving territories "Ontario","Quebec","NW Territory","Alberta" to player
		player[0].addTerritory(0);
		player[0].addTerritory(1);
		player[0].addTerritory(2);
		player[0].addTerritory(3);
		
		//Giving territories "Greenland","E United States","W United States","Central America", "Alaska" to neutral
		player[1].addTerritory(4);
		player[1].addTerritory(5);
		player[1].addTerritory(6);
		player[1].addTerritory(7);
		player[1].addTerritory(8);
		
		//sets up the countryList
		for(int i = 0;(i < constants.NUM_COUNTRIES); i++) {
			MapNode country = new MapNode(i);
			Main.countryList.add(country);
		}
	}
	
	@Test
	void testValidateCountry() {
		//returns -1 as 3 letters is not enough to recognise a country
		main.i.setInput("ont");
		main.i.setDetectedInput(true);
		assertEquals(main.validateCountry(player[0]), -1);
		
		//returns -1 as India is not owned by the player
		main.i.setInput("India");
		main.i.setDetectedInput(true);
		assertEquals(main.validateCountry(player[0]), -1);
		
		//returns -1 as Alaska is not owned by the player
		main.i.setInput("alas");
		main.i.setDetectedInput(true);
		assertEquals(main.validateCountry(player[0]), -1);
				
		//returns 1 as queb is recognised as Quebec which is owned by player 
		main.i.setInput("queb");
		main.i.setDetectedInput(true);
		assertEquals(main.validateCountry(player[0]), 1);
	}
	
	@Test
	void testUnitPlacement() {
		//sets the input to ontario and adds units there
		main.i.setInput("Ontario");
		main.i.setDetectedInput(true);
		main.unitPlacement(player[0], player);
		
		//checks that units were added there and deducted from the players unplaced armies
		assertEquals(player[0].getUnplacedArmies(), 33);
		assertEquals(Main.countryList.get(0).armySize, 3);
		
		//sets the input to quebec and adds units there
		main.i.setInput("Quebec");
		main.i.setDetectedInput(true);
		main.unitPlacement(player[0], player);
				
		//checks that units were added there and deducted from the players unplaced armies
		assertEquals(player[0].getUnplacedArmies(), 30);
		assertEquals(Main.countryList.get(1).armySize, 3);
	}
	
	@Test
	void testNeutralUnitPlacement() {
		//places one unit randomly from the neutral player to one of its territories
		main.i.setInput("green");
		main.i.setDetectedInput(true);
		main.neutralUnitPlacement(player[1]);
		
		//checks the unit was deducted from the players unplaced armies
		assertEquals(player[1].getUnplacedArmies(), 23);
		
		//checks that one unit was added to Greenland
		assertEquals(Main.countryList.get(4).getArmySize(), 1);
		
		//places another unit randomly from the neutral player to one of its territories
		main.i.setInput("central america");
		main.i.setDetectedInput(true);
		main.neutralUnitPlacement(player[1]);
		
		//checks the unit was deducted from the players unplaced armies
		assertEquals(player[1].getUnplacedArmies(), 22);
		
		//checks that one unit was added to Central America
		assertEquals(Main.countryList.get(7).getArmySize(), 1);
		
		try {
			main.neutralUnitPlacement(player[0]);
			fail("should be unreachable code");
		}
		catch(IllegalArgumentException ex) {}
	}

}

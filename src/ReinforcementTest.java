//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReinforcementTest {

	Player player = new Player("pname", false, 0);
	Main main;
	
	@BeforeEach
	void init() {
		main = new Main();
		
		//gives player "Ontario","Quebec","NW Territory"
		player.addTerritory(0);
		player.addTerritory(1);
		player.addTerritory(2);
		
		//sets up the country list
		for(int i = 0;(i < constants.NUM_COUNTRIES); i++) {
			MapNode country = new MapNode(i);
			Main.countryList.add(country);
		}
	}
	
	@Test
	void testValidateUnitReinforce() {
		player.setUnplacedArmies(5);
		
		//tests that when bad input is provided the method returns -1
		int[] arr = new int[] {-1, -1};
		assertFalse(main.validateUnitReinforce(player, arr));
		
		//tests that when a country which the player does not own is input them method returns -1
		int[] arr2 = new int[] {5, 2};
		assertFalse(main.validateUnitReinforce(player, arr2));
		
		//tests that when more units are to be placed then the player the method returns -1
		int[] arr3 = new int[] {0, 6};
		assertFalse(main.validateUnitReinforce(player, arr3));
		
		//tests that when the inputs are good the method returns the index of the country
		int[] arr4 = new int[] {0, 4};
		assertTrue(main.validateUnitReinforce(player, arr4));
		int[] arr5 = new int[] {1, 5};
		assertTrue(main.validateUnitReinforce(player, arr5));
	}
	
	@Test
	void testReinforcementsGiven() {
		player.setUnplacedArmies(0);
		
		//tests that when the player owns less than 8 territories they are still allocated the minimum 3 units
		main.reinforcementsGiven(player);
		assertEquals(player.getUnplacedArmies(), 3);
		
		//adds all 8 North American territories to player
		for(int i=3; i<9; i++) {
			player.addTerritory(i);
		}
		
		player.setUnplacedArmies(0);
		
		//allocated 3 troops for owning 9 territories + 5 troops for owning North America
		main.reinforcementsGiven(player);
		assertEquals(player.getUnplacedArmies(), 3+5);
	
		//adds all 12 Asian territories to player
		for(int i=16; i<28; i++) {
			player.addTerritory(i);
		}
		
		player.setUnplacedArmies(0);
		
		//allocated 7 troops for owning 21 territories + 5 troops for owning North America + 7 troops for owning Asia
		main.reinforcementsGiven(player);
		assertEquals(player.getUnplacedArmies(), 7+5+7);
	}
	
	//bad input is tested in the validate methods so this only tests for good input
	@Test
	void testUnitReinforce() {
		
		player.setUnplacedArmies(3);
		//setting input manually to nwte 3
		main.i.setInput("nwte 3");
		main.i.setDetectedInput(true);
		
		//calling reinforce method
		main.unitReinforce(player);
		
		//checking that the correct changes are made to map node and the players armies
		assertEquals(Main.countryList.get(2).getArmySize(), 3);
		assertEquals(player.getUnplacedArmies(), 0);
		
		//setting input manually to ontario 2
		player.setUnplacedArmies(2);
		main.i.setInput("ontario 2");
		main.i.setDetectedInput(true);
		
		main.unitReinforce(player);
		
		//checking that the correct changes are made to map node and the players armies
		assertEquals(Main.countryList.get(0).getArmySize(), 2);
		assertEquals(player.getUnplacedArmies(), 0);
	}
}


//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CombatTest {

	Player player = new Player("pname", false, 0);
	Player player2 = new Player("p2name", false, 1);
	Main main;
	
	@BeforeEach
	void init() {
		main = new Main();
		
		//gives player "Ontario","Quebec","NW Territory"
		player.addTerritory(0);
		player.addTerritory(1);
		player.addTerritory(2);
		
		//gives player 2 "Alberta","Greenland","E United States","W United States"
		player2.addTerritory(3);
		player2.addTerritory(4);
		player2.addTerritory(5);
		player2.addTerritory(6);
		
		//sets up the country list
		for(int i = 0;(i < constants.NUM_COUNTRIES); i++) {
			MapNode country = new MapNode(i);
			Main.countryList.add(country);
		}
	}

	//test uses some same tests as testValidateCountry() test
	@Test
	void testValidateAttackingCountry() {
		//returns -1 as 3 letters is not enough to recognise a country
		main.i.setInput("ont");
		main.i.setDetectedInput(true);
		assertEquals(main.validateAttackingCountry(player), -1);
		
		//returns -1 as India is not owned by the player
		main.i.setInput("India");
		main.i.setDetectedInput(true);
		assertEquals(main.validateAttackingCountry(player), -1);
		
		//returns -1 as Alaska is not owned by the player
		main.i.setInput("alas");
		main.i.setDetectedInput(true);
		assertEquals(main.validateAttackingCountry(player), -1);
				
		//returns -1 as quebec only has 1 unit,not enough to attack with
		main.i.setInput("queb");
		main.i.setDetectedInput(true);
		Main.countryList.get(1).addArmies(1);
		assertEquals(main.validateAttackingCountry(player), -1);
		
		//returns 1 as quebec has 2 units, and so can attack
		main.i.setInput("queb");
		main.i.setDetectedInput(true);
		Main.countryList.get(1).addArmies(2);
		assertEquals(main.validateAttackingCountry(player), 1);
		
	}
	
	//test uses some same tests as testValidateCountry() test
	@Test
	void testValidateInvadedCountry() {
		//returns -1 as 3 letters is not enough to recognise a country
		main.i.setInput("ont");
		main.i.setDetectedInput(true);
		assertEquals(main.validateInvadedCountry(player, 1), -1);
		
		//returns -1 as Alberta is not owned by the player
		main.i.setInput("albe");
		main.i.setDetectedInput(true);
		assertEquals(main.validateInvadedCountry(player, 1), -1);
		
		//returns -1 as Quebec is owned by the player
		main.i.setInput("queb");
		main.i.setDetectedInput(true);
		assertEquals(main.validateInvadedCountry(player, 1), -1);
				
		//returns -1 as Brazil is not adjacent to Ontario
		main.i.setInput("braz");
		main.i.setDetectedInput(true);
		assertEquals(main.validateInvadedCountry(player, 0), -1);
		
		//returns 6 as W US is adjacent to Ontario and not owned by the player
		main.i.setInput("wuni");
		main.i.setDetectedInput(true);
		assertEquals(main.validateInvadedCountry(player, 0), 6);
	}
	@Test
	void testValidateAttackerInvasionNum() {
		//false because the attackers army size must be below 4 and above 0
		assertFalse(main.validateAttackerInvasionNum(0, 0));
		assertFalse(main.validateAttackerInvasionNum(4, 0));
		
		Main.countryList.get(0).addArmies(2);
		//false because the attackers army size must leave 1 unit on the territory they attack from
		assertFalse(main.validateAttackerInvasionNum(2, 0));
		
		Main.countryList.get(0).addArmies(1);
		//true
		assertTrue(main.validateAttackerInvasionNum(2, 0));
	}
	
	@Test
	void testTerritoryChange() {
		//checks that everything that changes when a territory is taken changes correctly
		Main.countryList.get(0).setArmySize(4);
		main.territoryChange(player, player2, 3, 1, 0, 3);
		
		
		//the player who owns th node is now the attacker
		assertEquals(Main.countryList.get(3).playerOwnGet(), player.getPlayerID());
		
		//The army sizes are adjusted accordingly
		assertEquals(Main.countryList.get(3).getArmySize(), 2);
		assertEquals(Main.countryList.get(0).getArmySize(), 1);
		
		//The players territory lists are adjusted correctly
		assertTrue(player.getTerritories().contains(3));
		assertFalse(player2.getTerritories().contains(3));
	}
	
	@Test
	void testFortifyAfterCapture() {
		//TODO
	}
}

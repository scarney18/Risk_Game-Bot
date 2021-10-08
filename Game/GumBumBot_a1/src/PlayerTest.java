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
		for(int i=0; i<6; i++) {
			territory.add(i);
		}
		
		ArrayList<Integer> territory2 = (ArrayList<Integer>) territory.clone();
		
		territory = neutral.initialTerritoryAdd(territory);
		
		assertEquals(neutral.getTerritories(), territory2);
		
		for(int i=0; i<9; i++) {
			territory.add(i);
		}
		
		territory2 = (ArrayList<Integer>) territory.clone();
		
		territory = player.initialTerritoryAdd(territory);
		
		assertEquals(player.getTerritories(), territory2);
		assertTrue(territory.isEmpty());
	}

}

//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GumBumBotTest {
	Player player;
	Board board;
	GumBumBot bot;
	
	@BeforeEach
	void init() {
		board = new Board();
		player = new Player(0);
		bot = new GumBumBot(board,player);
		
	}
	
	@Test
	void testGetName() {
		player.setName(bot.getName());
		
		assertEquals(player.getName(), "GumBumBot");
	}
	
	@Test
	void testGetDefence() {
		//if available the bot uses two troops to defend
		board.addUnits(2, 3);
		assertEquals(bot.getDefence(2), "2");
		
		//if only one is available it uses one
		board.addUnits(4, 1);
		assertEquals(bot.getDefence(4), "1");
	}
	
	@Test
	void testGetCardExchange() {
		player.addCard(new Card(0, "Ontario", 1, "Cavalry"));
		player.addCard(new Card(1, "Quebec", 2, "Artillery"));
		player.addCard(new Card(3, "Alberta", 1, "Cavalry"));
		player.addCard(new Card(2, "NW Territory", 2, "Artillery"));
		
		//not able to exchange
		assertEquals(bot.getCardExchange(), "skip");
		
		player.addCard(new Card(2, "NW Territory", 2, "Artillery"));
		
		assertEquals(bot.getCardExchange(), "aaa");
		
		player.removeCards();
		
		player.addCard(new Card(0, "Ontario", 1, "Cavalry"));
		player.addCard(new Card(1, "Quebec", 0, "Infantry"));
		player.addCard(new Card(3, "Alberta", 1, "Cavalry"));
		player.addCard(new Card(2, "NW Territory", 2, "Artillery"));
		
		assertEquals(bot.getCardExchange(), "ica");
		
		player.removeCards();
		
		player.addCard(new Card(1, "Quebec", 2, "Artillery"));
		player.addCard(new Card(2, "NW Territory", 2, "Artillery"));
		player.addCard(new Card(41, "Wild", 3, "Wild Card"));
		player.addCard(new Card(0, "Ontario", 1, "Cavalry"));
		
		assertEquals(bot.getCardExchange(), "waa");
		
		player.removeCards();
		
		player.addCard(new Card(41, "Wild", 3, "Wild Card"));
		player.addCard(new Card(41, "Wild", 3, "Wild Card"));
		player.addCard(new Card(3, "Alberta", 2, "Infantry"));
		
		assertEquals(bot.getCardExchange(), "wwa");
		
	}
	
	@Test
	void testGetMoveIn() {
		board.addUnits(1, 25);
		
		assertEquals(bot.getMoveIn(1), "24");
		
		board.addUnits(3, 5);
		
		assertEquals(bot.getMoveIn(3), "4");
	}

}	
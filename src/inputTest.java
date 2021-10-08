//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class inputTest {
input i;
	@BeforeEach
	void init() {
		i = new input();
	}
	
	@Test
	void testIsValidCommand() {
		//tests that the function returns -1 on error input
		assertEquals(-1, i.isValidCommand("sdgsd"));
		//the function needs four letters to recognise the country
		assertEquals(-1, i.isValidCommand("ind"));
		assertEquals(-1, i.isValidCommand("ont"));
		
		//tests for different types of correct input
		assertEquals(0, i.isValidCommand("onta"));
		assertEquals(0, i.isValidCommand("Ontari"));
		
		assertEquals(1, i.isValidCommand("queb"));
		assertEquals(1, i.isValidCommand("Quebec"));
		
		assertEquals(2, i.isValidCommand("nW te"));
		assertEquals(2, i.isValidCommand("NWterritory"));
		
		assertEquals(3, i.isValidCommand("ALBERTA"));
		assertEquals(3, i.isValidCommand("albe"));
		
		assertEquals(4, i.isValidCommand("gree"));
		assertEquals(4, i.isValidCommand("GrEEnLAnd"));
		
		assertEquals(5, i.isValidCommand("eunit"));
		assertEquals(5, i.isValidCommand("E united States"));
		
		assertEquals(6, i.isValidCommand("W united States"));
		assertEquals(6, i.isValidCommand("wuni"));
		
		assertEquals(7, i.isValidCommand("cent"));
		assertEquals(7, i.isValidCommand("Central America"));
		
		assertEquals(8, i.isValidCommand("alas"));
		assertEquals(9, i.isValidCommand("grea"));
		assertEquals(10, i.isValidCommand("w eur"));
		assertEquals(11, i.isValidCommand("seur"));
		assertEquals(12, i.isValidCommand("ukra"));
		assertEquals(13, i.isValidCommand("n eur"));
		assertEquals(14, i.isValidCommand("icel"));
		assertEquals(15, i.isValidCommand("scan"));
		assertEquals(16, i.isValidCommand("afgh"));
		assertEquals(17, i.isValidCommand("indi"));
		assertEquals(18, i.isValidCommand("midd"));
		assertEquals(19, i.isValidCommand("japa"));
		assertEquals(20, i.isValidCommand("ural"));
		assertEquals(21, i.isValidCommand("yakut"));
		assertEquals(22, i.isValidCommand("kamc"));
		assertEquals(23, i.isValidCommand("Siam"));
		assertEquals(24, i.isValidCommand("Irku"));
		assertEquals(25, i.isValidCommand("sibe"));
		assertEquals(26, i.isValidCommand("mong"));
		assertEquals(27, i.isValidCommand("chin"));
		assertEquals(28, i.isValidCommand("eaus"));
		assertEquals(29, i.isValidCommand("newg"));
		assertEquals(30, i.isValidCommand("waus"));
		assertEquals(31, i.isValidCommand("indo"));
		assertEquals(32, i.isValidCommand("vene"));
		assertEquals(33, i.isValidCommand("peru"));
		assertEquals(34, i.isValidCommand("brazi"));
		assertEquals(35, i.isValidCommand("arge"));
		assertEquals(36, i.isValidCommand("cong"));
		assertEquals(37, i.isValidCommand("nafr"));
		assertEquals(38, i.isValidCommand("s afr"));
		assertEquals(39, i.isValidCommand("egyp"));
		assertEquals(40, i.isValidCommand("e afr"));
		assertEquals(41, i.isValidCommand("mada"));
		
	}
	
	@Test
	void testExchangeInsignias() {
		//checks if the correct values are returned for correct input
		//3 cavalry cards
		i.setInput("ccC");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {2, 2, 2});
		
		//3 artillery cards
		i.setInput("AAa");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {3, 3, 3});
		
		//3 infantry cards
		i.setInput("iIi");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {1, 1, 1});
		
		//1 of each
		i.setInput("AIc");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {1, 2, 3});
		
		i.setInput("cia");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {1, 2, 3});
		
		i.setInput("ACI");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {1, 2, 3});
		
		i.setInput("ICa");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {1, 2, 3});
		
		//2 of each and a wild card
		i.setInput("awC");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {2, 3, 0});
		
		i.setInput("awi");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {1, 3, 0});
		
		i.setInput("Wic");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {1, 2, 0});
		
		//2 of one insignia and a wild card
		i.setInput("iiw");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {1, 1, 0});
		
		i.setInput("cwc");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {2, 2, 0});
		
		i.setInput("WAA");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {3, 3, 0});
		
		//1 insignia and 2 wild cards
		i.setInput("wiW");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {1, 0, 0});
		
		i.setInput("wwC");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {2, 0, 0});
		
		i.setInput("AWW");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {3, 0, 0});
		
		//bad input
		//3 wilds is not possible, only 2 in deck
		i.setInput("wwW");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {-1, -1, -1});
		
		//2 of one type and 1 of another, not valid exchange
		i.setInput("cci");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {-1, -1, -1});
		
		i.setInput("acc");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {-1, -1, -1});
		
		//too many characters
		i.setInput("iiii");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {-1, -1, -1});
		
		//too few characters
		i.setInput("ic");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {-1, -1, -1});
		
		i.setInput("AA");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {-1, -1, -1});
		
		i.setInput("ww");
		i.setDetectedInput(true);
		assertArrayEquals(i.exchangeInsignias(), new int[] {-1, -1, -1});
	}
	

}

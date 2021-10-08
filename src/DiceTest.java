//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959

import org.junit.jupiter.api.Test;

class DiceTest {

	@Test
	void test() {
		Dice dice = new Dice();
		for(int i = 0;i<100;i++) {
		dice.roll();
		System.out.println(dice.getRoll());
		}
	}

}

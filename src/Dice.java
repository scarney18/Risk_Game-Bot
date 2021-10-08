//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import java.util.Random;

public class Dice {
	int roll;
	Random rand = new Random();
	
	Dice(){
		roll = rand.nextInt(6) + 1;
	}
	
	public int getRoll() {
		return roll;
	}
	
	public void roll() {
		roll = rand.nextInt(6) + 1;
	}
}

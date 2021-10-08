//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341, 19312591, 19720959
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;


public class Main {
	
	private JFrame Game;
	private input i = new input();
	private output o = new output();
	public static ArrayList<MapNode> countryList = new ArrayList<MapNode>();
	
	public void GameWindow() {
		
		//initialising game window 
		JFrame Game = new JFrame();
		
		//adding input text box to the frame
		Game.add(i,BorderLayout.SOUTH);
		
		//adding output text box to the frame
		Game.add(o,BorderLayout.EAST);
		
		//adding screen
		Game.add(new Screen(),BorderLayout.CENTER);
		Game.setTitle("Risk");
		Game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//allow resizing
		Game.setResizable(true);
		Game.setPreferredSize(new Dimension(constants.FRAME_WIDTH, constants.FRAME_HEIGHT));
		
		//pack components into window
		Game.pack();
		Game.setVisible(true);
		
		
	}
	
	public JFrame getFrame() {
		return Game;
	}
		
	
	
	public static void main (String[] args) {
		
		Main m = new Main();
		m.GameWindow();
		
		//prints key with continent colours
		m.o.displayContinentInfo();
		
		//create list of numbers of countries
		for(int i = 0;(i < constants.NUM_COUNTRIES); i++) {
			MapNode country = new MapNode(i);
			countryList.add(country);
		}
		
		//gets the name of player 1 from the output class when the button is pressed
		m.o.outputString("Enter player 1 name:");
		String name1 = m.i.getName();
		m.o.outputString(name1);
		
		
		//gets the name of player 2 from the output class when the button is pressed
		m.o.outputString("Enter player 2 name:");
		String name2 = m.i.getName();
		m.o.outputString(name2);
		
		Player[] playerArr = initializeGame(name1, name2);
		
		m.printPlayerData(playerArr[0], 0);
		m.printPlayerData(playerArr[1], 1);
		
		
	}
	
	public static Player[] initializeGame(String name1, String name2) {
		
		//Initializing players
		Player playerArr[] = {
				new Player(name1, false, 0),
				new Player(name2, false, 1),
				new Player("Neutral 1", true, 2),
				new Player("Neutral 2", true, 3),
				new Player("Neutral 3", true, 4),
				new Player("Neutral 4", true, 5)
			};
		
		//Creates list of 1-42 each number representing 1 territory
		ArrayList<Integer> territoriesRemaining = new ArrayList<Integer>();
		for(int i=0; i<constants.NUM_COUNTRIES; i++) {
			territoriesRemaining.add(i);
		}
		
		/* Iterates thorugh array of players and adds territories to each of them
		 * The ArrayList has each of the numbers corresponding to the current players allocated territories removed
		 * So that the next players can't be allocated the same territories
		 *   
		*/
		for(Player i : playerArr) {
			territoriesRemaining = i.initialTerritoryAdd(territoriesRemaining);
		}
		
		return playerArr;
	}
	
	public void printPlayerData(Player player1, int playerNo) {
		StringBuilder string = new StringBuilder();
		
		string.append("\nPlayer " + (playerNo+1) + ":\n");
		string.append("Name: " + player1.getName() + "\n");
		string.append("Owned Territories:\n");
		
		//Appends the names of each of the territories owned by the player to the string
		for(Integer i: player1.getTerritories()) {
			string.append("-" + constants.COUNTRY_NAMES[i] + "\n");
		}
		
		o.outputString(string.toString());
	}
	
}

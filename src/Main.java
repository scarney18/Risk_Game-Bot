//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341, 19312591, 19720959
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;


public class Main {
	
	private JFrame Game;
	public input i = new input();
	public output o = new output();
	public static ArrayList<MapNode> countryList = new ArrayList<MapNode>();
	private boolean attackWon;
	private Deck territoryCards = new Deck();
	ArrayList<Card> exchangedCards = new ArrayList<Card>();
	private static int cardTradeCount = 0;
	public static boolean diceAnimation = false;
	
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
		
	//initialising player array
	public static Player[] playerArr;
	
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

		//create dice
		Dice dice = new Dice();
		
		//gets the name of player 1 from the output class when the button is pressed
		m.o.outputString("Enter player 1 name:");
		String name1 = m.i.getName();
		m.o.outputString(name1);
		
		
		//gets the name of player 2 from the output class when the button is pressed
		m.o.outputString("Enter player 2 name:");
		String name2 = m.i.getName();
		m.o.outputString(name2);
		
		playerArr = initializeGame(name1, name2);
		
		m.printPlayerData(playerArr[0], 0);
		m.printPlayerData(playerArr[1], 1);
		
		boolean rolled = false;
		int playerGoFirst = 0;
		while(rolled == false) {
			//dice roll
			m.o.outputString(name1+", the dice is being rolled!");
			diceAnimation = true;
			for(int i = 0;i< 3;i++) {
				int timeToWait = 1; //second
				m.o.outputString("...");
		        try {
		            for (int ii=0; ii<timeToWait ; ii++) {
		                Thread.sleep(1000);
		            }
		        } catch (InterruptedException ie)
		        {
		            Thread.currentThread().interrupt();
		        }
			}
			dice.roll();
			diceAnimation = false;
			int roll1 = dice.getRoll();
			m.o.outputString(name1 +" rolled "+Integer.toString(dice.getRoll()));
			m.o.outputString(" ");
			
			try {
	                Thread.sleep(1000);
	        } catch (InterruptedException ie)
	        {
	            Thread.currentThread().interrupt();
	        }
			
			//dice roll2
			m.o.outputString(name2+", the dice is being rolled!");
			diceAnimation = true;
			for(int i = 0;i< 3;i++) {
				int timeToWait = 1; //second
				m.o.outputString("...");
		        try {
		            for (int ii=0; ii<timeToWait ; ii++) {
		                Thread.sleep(1000);
		            }
		        } catch (InterruptedException ie)
		        {
		            Thread.currentThread().interrupt();
		        }
			}
			dice.roll();
			int roll2 = dice.getRoll();
			diceAnimation = false;
			m.o.outputString(name2 +" rolled "+Integer.toString(dice.getRoll()));
			m.o.outputString(" ");

			//checks which player wins dice roll
			if(roll1 > roll2) {
				m.o.outputString(name1 + " rolled higher and has the first turn");
				playerGoFirst = 0;
				rolled = true;
			}else if(roll2 > roll1) {
				m.o.outputString(name2 + " rolled higher and has the first turn");
				playerGoFirst = 1;
				rolled = true;
			}else if(roll1 == roll2) {
				m.o.outputString("it's a draw! Reroll");

				try {
					Thread.sleep(2000);
				} catch (InterruptedException ie)
				{
					Thread.currentThread().interrupt();
				}
				
				m.o.outputString(" ");
			}
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie)
		{
			Thread.currentThread().interrupt();
		}
		
		//placing troops at start
		while(playerArr[0].getUnplacedArmies() != 0) {
			//if player 1 rolled highest then they go first
			if(playerGoFirst == 0) {
				//player 1 places their units and then chooses one country for each of the neutral players to reinforce by 1 unit
				m.unitPlacement(playerArr[0], playerArr);
				for(int i=2; i<playerArr.length; i++) {
					m.neutralUnitPlacement(playerArr[i]);
				}
				
				//player 2 places their units and then chooses one country for each of the neutral players to reinforce by 1 unit
				m.unitPlacement(playerArr[1], playerArr);
				for(int i=2; i<playerArr.length; i++) {
					m.neutralUnitPlacement(playerArr[i]);
				}

			}
			//otherwise player 2 won the dice roll an they go first
			else {
				//player 2 places their units and then chooses one country for each of the neutral players to reinforce by 1 unit
				m.unitPlacement(playerArr[1], playerArr);
				for(int i=2; i<playerArr.length; i++) {
					m.neutralUnitPlacement(playerArr[i]);
				}
				
				//player 1 places their units and then chooses one country for each of the neutral players to reinforce by 1 unit
				m.unitPlacement(playerArr[0], playerArr);
				for(int i=2; i<playerArr.length; i++) {
					m.neutralUnitPlacement(playerArr[i]);
				}
			}
			
		}
		//records who goes first
		int playerTurn = 0;
		if(playerGoFirst != 0) {
			playerTurn = 1;
		}
		
		m.o.outputString("TURNS START");
		
		//Gameplay Loop
		//while neither player has lost yet
		while(!playerArr[0].getTerritories().isEmpty() && !playerArr[1].getTerritories().isEmpty()) {
			
			m.o.outputString(playerArr[playerTurn].getName() + " turn\n");
			
			//sets the attack won to false so players only get territory cards if they conquer a territory
			m.attackWon = false;
						
			//Allocate the reinforcements for the player
			m.reinforcementsGiven(playerArr[playerTurn]);
			
			//tell the player what cards they have if any
			if(playerArr[playerTurn].getCards().isEmpty()) 
				m.o.outputString(playerArr[playerTurn].getName() + "has no cards");
			else
				m.o.outputString("Your cards are :" + playerArr[playerTurn].getCards().toString());
			
			//Allow players to exchange territory cards
			m.exchangeCards(playerArr[playerTurn]);
			
			//Allow them to reinforce their territories
			m.unitReinforce(playerArr[playerTurn]);
			
			//Allow them to attack
			m.invasion(playerArr[playerTurn]);
			
			//Allow them to fortify
			m.unitFortify(playerArr[playerTurn]);
			
			//If they won one of their attacks, they get a territory card
			if(m.attackWon) 
				m.drawCard(playerArr[playerTurn]);
			else 
				m.o.outputString("No territories conquered, so no territory cards given");
			
			//sets the turn to the next player after invasion
			playerTurn++;
			if(playerTurn==2) {
				playerTurn = 0;
			}
		}
		
		//prints who won after the game is over
		m.o.outputString("GAME OVER");
		if(playerArr[0].getTerritories().isEmpty())
			m.o.outputString(playerArr[0].getName() + " wins");
		else
			m.o.outputString(playerArr[1].getName() + " wins");
	}

	//INITIALISATION METHODS
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
		*/
		for(Player i : playerArr) {
			territoriesRemaining = i.initialTerritoryAdd(territoriesRemaining);
			nodeData(i);
			
		}
		
		return playerArr;
	}
	
	//Adds the required node data to the country list for each player     
	public static void nodeData(Player player) {         
		for(Integer i : player.getTerritories()) {             
			countryList.get(i).playerOwnSet(player.getPlayerID());            
			countryList.get(i).setArmySize(1);         
		}     
	}
	
	//prints the relevant data for the initialised players
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
	
	//Allows a player to choose which territory they want to place their troops in, then places 3 troops there
	public void unitPlacement(Player player, Player[] playerArr) {
				
		int country = -1;
		
		o.outputString("\nPlayer " + player.getName() + ", Input the name of the country you wish to add 3 troops to.");
		o.outputString("You can enter the first 4 letters of the desired territory");
		
		//if country == -1 then there has been an error with input
		while(country == -1) {
			country = validateCountry(player);
		}
		
		//adds 3 troops to the selected territory
		countryList.get(country).addArmies(3);
		player.removeArmies(3);
		
		//shows what was done to the players
		o.outputString("\nAdded 3 troops to " + constants.COUNTRY_NAMES[country]);
		o.outputString("You have " + player.getUnplacedArmies() + " troops remaining\n");
		
	}
	
	//Places one unit from each of the neutral players into a random one of their territories
	public void neutralUnitPlacement(Player player) {
		
		int country = -1;
		//only changes the neutral players territories
		if(!player.getIsNeutral()) {
			throw new IllegalArgumentException ("this player must be neutral");
		}
	
		o.outputString("\nEnter a country occupied by " + player.getName() + " to reinforce by 1 unit");
		
		while(country == -1) {
			country = validateCountry(player);
		}
						
		//adds one troop to that territory, removes a troop from their unplaced armies
		countryList.get(country).addArmies(1);
		player.removeArmies(1);
		
		o.outputString(player.getName() + " added 1 troop to " + constants.COUNTRY_NAMES[country] + "\n");
	}	
	//END OF INITIALISATION METHODS
	
	
	//REINFORCEMENT PHASE METHODS
	//player choose to reinforce
	public void unitReinforce(Player player) {
		
		boolean repeat = true;
		
		while(repeat) {
			o.outputString("Player " + player.getName() + ", Input the name of the country you wish to reinforce,"
					+ " followed by the amount you want to reinforce by. Do not include any spaces in country name input.");
			o.outputString("Example: Alaska 2");
		
			//index 0 of countryArr is index of country, index 1 of countryArr is amount to reinforce by
			int[] countryArr = i.reinforce(player.getUnplacedArmies());

			if(validateUnitReinforce(player, countryArr)) {
				//removes the troops from the player and adds them to his map node
				player.removeArmies(countryArr[1]);
				countryList.get(countryArr[0]).addArmies(countryArr[1]);
				o.outputString("Placed " + countryArr[1] + " units in " + constants.COUNTRY_NAMES[countryArr[0]]);
				
				//if the player has troops remaining they must place them
				if(player.getUnplacedArmies() > 0) 
					o.outputString("You have " + player.getUnplacedArmies() + " troops remaining to place");
				else
					repeat = false;
			}
		}
	}
	
	//ensures that the player reinforcing owns the territory and has enough troops to place down
	public boolean validateUnitReinforce(Player player, int[] countryArr) {
		if(countryArr[0] == -1 || countryArr[1] == -1) {
			o.outputString("error with country input, try again");
			return false;
		}
		else if(!player.getTerritories().contains(countryArr[0])) {
			o.outputString(player.getName() + " does not own " + constants.COUNTRY_NAMES[countryArr[0]] + " choose new territory");
			return false;
		}
		else if(player.getUnplacedArmies() < countryArr[1]) {
			o.outputString(player.getName() + " only has " + player.getUnplacedArmies() + " troops and can't place " + countryArr[1] + " troops");
			return false;
		}
		else 
			return true;
	}
				
	public void reinforcementsGiven(Player player) {
		//countries
		int numCountries = player.getTerritories().size();
		
		if(numCountries<9) {
			player.addArmies(3);
			o.outputString("Player " + player.getName() + " owns " + numCountries + " territories, and so receives 3 reinforcements");
		}
		else{
			player.addArmies(numCountries/3);
			o.outputString("Player " + player.getName() + " owns " + numCountries + " territories, and so receives " + numCountries/3 + " reinforcements");
		}
		
		//continents
		ArrayList<Integer> continentsOwned = player.continentsOwned();
		
		for(Integer i: continentsOwned) {
			player.addArmies(constants.CONTINENT_VALUES[i]);
			
			o.outputString("Player " + player.getName() + " owns continent " + constants.CONTINENT_NAMES[i] + ", and so receives " + constants.CONTINENT_VALUES[i] + " reinforcements");
		}
		
	}
	//END OF REINFORCEMENT PHASE METHODS
	
	
	//ATTACK PHASE METHODS
	//starts the invasion, allows attacker to choose what they want to do
	public void invasion(Player player) {
		boolean willInvade = true;
		while(willInvade){
			o.outputString(player.getName() + ": Enter skip if you do not wish to invade, otherwise enter anything else\n");
			if(i.getName().equalsIgnoreCase("skip")) {
				willInvade = false;
			}
			//otherwise they will invade
			else {
				willInvade = true;
			}
			if(willInvade) {
				//finds the country the player wants to invade from
				o.outputString(player.getName() + ": Enter the country you will invade from\n");
				int attCountry = validateAttackingCountry(player);
				
				if(attCountry != -1) {
					//finds the country the player wants to invade
					o.outputString("Enter the country you will invade\n");
					int defCountry = validateInvadedCountry(player, attCountry);
					
					if(defCountry != -1) {
						int attSize=0;
						//finds how many troops the attacker wants to attack with
						o.outputString("Enter the size of army you will invade with, max 3\n");
						
						try {
							attSize = Integer.parseInt(i.getName().trim());
							}
							catch(NumberFormatException e) {
								o.outputString("Not a valid input");
							}
						
						while(!validateAttackerInvasionNum(attSize, attCountry)) {
							try {
							attSize = Integer.parseInt(i.getName().trim());
							}
							catch(NumberFormatException e) {
								o.outputString("Not a valid input");
							}
						}
						//calls combat method
						combat(player,attCountry,defCountry,attSize);
					}
				}
			}
		}
	}
	
	//allows the combat to happen, allows defender to decide how they will defend
	public void combat(Player Attacker, int AttCountry, int DefCountry, int AttSize) {
		if(countryList.get(AttCountry).isAdj(DefCountry)
			&& countryList.get(AttCountry).armySize > AttSize
			&& countryList.get(AttCountry).armySize > 1) {
			
			//initialises the defending player for the combat function using the invaded country
			Player Defender = playerArr[countryList.get(DefCountry).playerOwnGet()];
			
			//declaring variables for the troops lost in combat and arrays for dice rolls
			int AttLoss=0,DefLoss=0;
			int[] AttDice = new int[3];
			int[] DefDice = new int[2];
			
			//rolling the dice for the attacking player
			Dice dice = new Dice();
			
			//dice roll for attackers
			diceAnimation = true;
			for(int i=0;i<AttSize;i++) {
				dice.roll();
				AttDice[i] = dice.getRoll(); 
				for(int ii = 0;ii< 3;ii++) {
					int timeToWait = 1; //second
			        try {
			            for (int iii=0; iii<timeToWait ; iii++) {
			                Thread.sleep(100);
			            }
			        } catch (InterruptedException ie)
			        {
			            Thread.currentThread().interrupt();
			        }
				}
			}
			diceAnimation = false;
			//sorting the arrays in descending order for easy comparison
			Arrays.sort(AttDice);
			int temp = AttDice[0];
			AttDice[0] = AttDice[AttDice.length-1];
			AttDice[AttDice.length-1] = temp;
			
			o.outputString(constants.COUNTRY_NAMES[AttCountry] + " attacking " + constants.COUNTRY_NAMES[DefCountry] + " with " + AttSize + " units");
			
			int DefSize = 0;
			//can't use 2 units to defend if there is only one unit in the territory
			if(countryList.get(DefCountry).getArmySize() == 1)
				DefSize = 1;
			
			//ask user to input defensive army size and neutrals will choose the max amount
			while (DefSize<=0 || DefSize>2) {
				if(Defender.getIsNeutral()) {
					DefSize = 2;
				}
				else {
					o.outputString(Defender.getName()+": Enter number of units to defend with, either 1 or 2");
					try {
						DefSize = Integer.parseInt(i.getName());
					}
					catch(NumberFormatException e) {
						o.outputString("Not a valid input");
					}
				}
				
			}
			
			//rolling the dice for the defending player and sorting the array
			diceAnimation = true;
			for(int j=0;j<DefSize;j++) {
				dice.roll();
				DefDice[j] = dice.getRoll(); 
				for(int ii = 0;ii< 3;ii++) {
					int timeToWait = 1; //second
			        try {
			            for (int iii=0; iii<timeToWait ; iii++) {
			                Thread.sleep(100);
			            }
			        } catch (InterruptedException ie)
			        {
			            Thread.currentThread().interrupt();
			        }
				}
			}
			diceAnimation = false;
			Arrays.sort(DefDice);
			temp = DefDice[0];
			DefDice[0] = DefDice[DefDice.length-1];
			DefDice[DefDice.length-1] = temp;
			
			//outputting the results of the dice rolls to the text box
			o.outputString(Attacker.getName() + " rolled " + Arrays.toString(AttDice).replaceAll(", 0", "")
					+ Defender.getName() + " rolled " + Arrays.toString(DefDice).replaceAll(", 0", "")+"\n");
			
			//using roll to iterate through the arrays of dice rolls and calculate the troops lost
			int roll=0;
			while(roll<2 && DefDice[roll] != 0) {
				if(DefDice[roll] >= AttDice[roll]) {
					AttLoss++;
				}
				else if(DefDice[roll] < AttDice[roll]) {
					DefLoss++;
				}
				roll++;
			}
			
			//setting the army sizes to their new calculated values
			countryList.get(AttCountry).setArmySize(countryList.get(AttCountry).getArmySize()-AttLoss);
			countryList.get(DefCountry).setArmySize(countryList.get(DefCountry).getArmySize()-DefLoss);
			
			//telling the players how many troops were lost
			o.outputString(Attacker.getName() + " loses " + AttLoss + " troops, " + Defender.getName() + " loses " + DefLoss + " troops\n");
			
			//transfers territory from the attacker to the defender if it runs out of units, allows them to fortify their new country
			if(countryList.get(DefCountry).getArmySize()<=0) {
				territoryChange(Attacker, Defender, AttSize, AttLoss, AttCountry, DefCountry);
				fortifyAfterCapture(Attacker, Defender, AttSize, AttLoss, AttCountry, DefCountry);
			}
		}
		else {
			throw new IllegalArgumentException("territories entered are bad");
		}
	}
	
	//ensures that the country is input correctly, that the player does not won the country, and that the country is adjacent to the invading country
	//returns or the index of the country, or -1 on error 
	public int validateAttackingCountry(Player player) {
		int country = -1;
		
		country = validateCountry(player);
		if(country != -1) {
			if(countryList.get(country).getArmySize() == 1) {
				o.outputString(constants.COUNTRY_NAMES[country] + " only has 1 unit and can't attack");
				return -1;
			}
		}				
		return country;
	}
	
	
	//ensures that the country is input correctly, that the player does not won the country, and that the country is adjacent to the invading country
	//returns or the index of the country, or -1 on error 
	public int validateInvadedCountry(Player player, int attacker) {
		int country = -1;
		
		//takes input of user
		String countryStr = i.getCountryInput();
			
		//when a country is found to have the same string as the input, the index of that country is recorded
		for(int j=0; j<constants.NUM_COUNTRIES; j++) {
			if(countryStr.compareTo(constants.COUNTRY_NAMES[j]) == 0) {
				country = j;
				break;
			}
		}
		
		//if a country is not found to have the same string, return -1
		if(country == -1)  {
			o.outputString("No country recognised, please try again\n");
			return -1;
		}
		//if the player owns the territory ask them for input again
		else if(player.getTerritories().contains(country)) {
			o.outputString(player.getName() + " owns " + constants.COUNTRY_NAMES[country] + ", please enter another territory\n");
			return -1;
		}
		//checks that the invading country is adjacent to input country
		for(Integer i: constants.ADJACENT[attacker]) {
			if(country == i)
				return country;
		}
		
		//if the country is not found to be adjacent to the invading country then return -1 to show an error
		o.outputString(constants.COUNTRY_NAMES[country] + " is not adjacent to " + constants.COUNTRY_NAMES[attacker] + " try again");
		return -1;
	}
	
	//makes sure that the attackers input for the size of their invading army is valid
	public boolean validateAttackerInvasionNum(int size, int country) {
		//the army size must be between 1 and 3
		if(size < 1 || size > 3) {
			o.outputString("must be between 1 and 3, please try again");
			return false;
		}
		//the invasion must leave at least one troop on the country they invade from
		else if(size >= countryList.get(country).getArmySize()) {
			o.outputString("must leave at least one unit in country, please try again");
			return false;
		}
		else 
			return true;
	}
	
	//transfers a territory from one player to another if they lose
	public void territoryChange(Player Attacker, Player Defender, int AttSize, int AttLoss, int AttCountry, int DefCountry) {
		countryList.get(DefCountry).playerOwnSet(Attacker.getPlayerID());
		Attacker.addTerritory(DefCountry);
		Defender.removeTerritory(DefCountry);
		countryList.get(DefCountry).setArmySize(AttSize-AttLoss);
		countryList.get(AttCountry).setArmySize(countryList.get(AttCountry).getArmySize()-AttSize);
		o.outputString(constants.COUNTRY_NAMES[DefCountry] + " captured by " + Attacker.getName());
				
		//set attackWon to true to give a card after the player's turn
		attackWon = true;
		
		//exchange the defender's cards when they lose all their territories
		if(Defender.getTerritories() == null) {
			Attacker.addCards(Defender.getCards());
			Defender.removeCards(Defender.getCards());
		}

	}
	
	public void fortifyAfterCapture(Player Attacker, Player Defender, int AttSize, int AttLoss, int AttCountry, int DefCountry) {
		//allow user to fortify new country from their attacking country if they succesfully invade
		int amountToFortify = -1;
		while(amountToFortify < 0) {
		o.outputString("Enter the number of units you would like to send into " + constants.COUNTRY_NAMES[DefCountry] + " from " + constants.COUNTRY_NAMES[AttCountry]);
		int tempFort = -1;
		
		while(tempFort <0) {
			try{
				tempFort = Integer.parseInt(i.getName());
			}
			catch(NumberFormatException e){
				o.outputString("Not a valid input");
			}
		}
		
		//check if the the user attempts to fortify with more armies than possible
		if(tempFort > countryList.get(AttCountry).getArmySize()-1 || tempFort < 0) {
			o.outputString("invalid number");
		}
		else{//otherwise go trough with the fortification
			amountToFortify = tempFort;
			}
		}
		//update the territory armies after
		if(amountToFortify >= 0) {
	
			countryList.get(AttCountry).addArmies(-amountToFortify);
			countryList.get(DefCountry).addArmies(amountToFortify);
			
			o.outputString("Sent " + amountToFortify + " units\n");
		}
	}
	
	//END OF ATTACK PHASE METHODS
	
	
	//FORTIFY PHASE METHODS
	//player choose to fortify
	public void unitFortify(Player player) {

		boolean repeat = true;

		while(repeat == true) {
			o.outputString("\nPlayer " + player.getName() + ", Enter country to move units from, country to fortify and number of units to move, or enter skip");
			o.outputString("Example: wuni albe 2");
			
			String countryStr = i.fortify();
			o.outputString(countryStr);
			
			if(!countryStr.equals("Invalid Input, try again")) {
				repeat = false;
			}
		}
	}
	//END OF FORTIFY PHASE METHODS
	
	//Validation method used in every phase of the game
	//ensures that the country is input correctly and that the player owns the country
	//returns or the index of the country, or -1 on error 
	public int validateCountry(Player player) {
		int country = -1;
		
		//takes input of user
		o.outputString("Please input name of desired territory");
		String countryStr = i.getCountryInput();

		//when a country is found to have the same string as the input, the index of that country is recorded
		for(int j=0; j<constants.NUM_COUNTRIES; j++) {
			if(countryStr.compareTo(constants.COUNTRY_NAMES[j]) == 0) {
				country = j;
				break;
			}
		}
		
		//if a country is not found to have the same string, return -1
		if(country == -1)  {
			o.outputString("No country recognised, please try again\n");
		}
		//if the player does not own the territory ask them for input again
		else if(!player.getTerritories().contains(country)) {
		o.outputString(player.getName() + " does not own " + constants.COUNTRY_NAMES[country] + ", please enter another territory\n");
			country = -1;
		}
		return country;
	}
	
	//TERRITORY CARD METHODS
	public void drawCard(Player player) {
		
		//if the deck is empty add any exchanged cards back into it and keep playing
		if(territoryCards.isEmpty()) {
			territoryCards.addCards(this.exchangedCards);
			exchangedCards = new ArrayList<Card>();
		}
		
		//draws the card from the deck and adds it to the players cards
		Card currentCard = territoryCards.draw();
		player.addCard(currentCard);
		
		//prints the card the player got to them
		o.outputString(player.getName() + " conquered a territory and received the " + currentCard.toString() + " card");
		
	}
	
	//main control method for the exchange of territory cards
	public void exchangeCards(Player player) {
		//allows player to choose if they want to exchange cards
		boolean willExchange = exchangeCardsChoice(player);
		
		//if they chose yes then enter loop
		while(willExchange) {
			//takes in their input
			o.outputString("Enter first letter of the insignias of the cards you wish to exchange, eg. \"wwa\", \"ccc\"");
			int[] input = i.exchangeInsignias();
			
			//checks if its valid and if it is, gives them reinforcements and removes their cards, exits loop
			if(validateExchange(player, input)) {
				cardReinforcements(player);
				willExchange = false;
			}
			//otherwise gives them a chane to exchange again if they want to
			else 
				willExchange = exchangeCardsChoice(player);
		}
	}

	//deals with if a player can exchange cards, if they have to or if they want to
	//returns true if they will exchange cards or false if they will not
	public boolean exchangeCardsChoice(Player player) {
		//exchange is impossible if the player has less than 3 cards
		if(player.getCards().size() < 3) {
			o.outputString(player.getName() + " can't exchange cards, not enough cards\n");
			return false;
		}
		//player must exchange cards if they have 5 or more of them
		else if(player.getCards().size() >= 5) {
			o.outputString(player.getName() + " has too many cards and must exchange some\n");
			return true;
		}
		//otherwise they may choose to exchange or not
		else {
			o.outputString(player.getName() + ", enter skip if you do not wish to exchange cards, otherwise enter anything else\n");
			if(i.getName().equalsIgnoreCase("skip"))
				return false;
			else
				return true;
		}
	}
	
	//validates that the player has the cards they entered, and if they do it removes the cards from their hand
	//returns true if cards are found successfully or false otherwise
	public boolean validateExchange(Player player, int[] input) {
		if(input.length != 3)
			throw new IllegalArgumentException("Array length must be 3 here");
		//if the players input was invalid
		else if(input[0] == -1) {
			o.outputString("Invalid input, please try again\n");
			return false;
		}
		else {
			//stores all the cards removed from the players hand
			ArrayList<Card> removed = new ArrayList<Card>();
			
			for(int i=0; i<input.length; i++) {
				
				if(input[i] < 0 || input[i] > 3) {
					throw new IllegalArgumentException("Must be one of the possible insignias");
				}
				
				//removes a card from the players hand that has the insignia specified by the array to avoid double counting
				Card c = player.removeCard(input[i]);
				
				//if c is not null add it to the removed cards
				if(c != null) {
					removed.add(c);
				}
				//if c is null no cards with the correct insignia were found and all the removed cards are added back to the players hand and false is returned
				else {
					player.addCards(removed);
					o.outputString("You do not have these cards, try again");
					return false;
				}
				
			}
			//if all three cards were found successfully the player is told what cards were removed and true is returned
			o.outputString("Exchanged cards " + removed.toString());
			exchangedCards.addAll(removed);
			return true;
		}
	}
	
	//adds troops to the player after they successfully exchange cards, increments the amount of reinforcements reeived for trade to the next amount
	public void cardReinforcements(Player player) {
		player.addArmies(constants.CARD_REINFORCEMENTS[cardTradeCount]);
		o.outputString("Gained " + constants.CARD_REINFORCEMENTS[cardTradeCount] + " units\n");
		cardTradeCount++;
	}
	
	//ANIMATION
	public static boolean getIsDiceRolling() {
		return diceAnimation;
	}
}
